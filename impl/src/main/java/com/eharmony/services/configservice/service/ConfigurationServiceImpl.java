/*
 * This software is the confidential and proprietary information of
 * eharmony.com and may not be used, reproduced, modified, distributed,
 * publicly displayed or otherwise disclosed without the express written
 * consent of eharmony.com.
 *
 * This software is a work of authorship by eharmony.com and protected by
 * the copyright laws of the United States and foreign jurisdictions.
 *
 * Copyright 2000-2015 eharmony.com, Inc. All rights reserved.
 *
 */
package com.eharmony.services.configservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eharmony.services.configservice.dao.ConfigurationDao;
import com.eharmony.services.configservice.dto.ConfigurationFeaturesDto;
import com.eharmony.services.configservice.dto.ConfigurationPropertiesDto;
import com.eharmony.services.configservice.model.ConfigurationDo;
import com.eharmony.services.configservice.model.ConfigurationDo.ConfigurationTypeEnum;
import com.eharmony.services.configservice.service.ConfigurationService;
import com.google.common.base.Preconditions;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    //@Autowired
    private ConfigurationDao configurationDao;

	private SamplingStrategy DEFAULT_STRATEGY = new DefaultSamplingStrategy();

    @Resource
    private Map<ConfigurationTypeEnum, SamplingStrategy> configurationTypeStrategyMap;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public ConfigurationFeaturesDto getSampledFeaturesForUserAndLocale(Integer userId, Locale locale,
            List<String> properties) {

        ArrayList<String> enabledKeys = new ArrayList<String>(properties.size());
        ArrayList<String> disabledKeys = new ArrayList<String>(properties.size());

        properties.stream().map(prop -> configurationDao.findPropertyByLocale(locale, prop)).forEach((p) -> {
        	bucketFeatureSampling(p, userId, enabledKeys, disabledKeys);
        });

        return new ConfigurationFeaturesDto(userId, locale.toString(), enabledKeys, disabledKeys);
    }
    
    @Override
    public boolean isFeatureSampledForUser(Integer userId, Locale locale, String property) {

    	ConfigurationDo configurationDo = configurationDao.findPropertyByLocale(locale, property);
    	if(configurationDo == null) {
    		log.warn("Property {} not found in store", property);
    		return false;
    	}
    	if (!configurationTypeStrategyMap.containsKey(configurationDo.getConfigurationType())) {
    		log.warn("Configuration Type {} is not configured for evaluation for property {}", configurationDo.getConfigurationType(), property);
    		return false;
    	}
    	return isSampledForUser(configurationDo, userId);
    }
    
    @Override
    public ConfigurationFeaturesDto getSampledFeaturesForUserAndLocale(Integer userId, Locale locale) {
        ArrayList<String> enabledKeys = new ArrayList<String>();
        ArrayList<String> disabledKeys = new ArrayList<String>();

        configurationDao.findAllPropertiesByLocale(locale).stream().forEach((p) -> {
        	bucketFeatureSampling(p, userId, enabledKeys, disabledKeys);
        });

        return new ConfigurationFeaturesDto(userId, locale.toString(), enabledKeys, disabledKeys);
    }
    
    @Override
    public ConfigurationPropertiesDto getAllPropertiesForLocale(Locale locale) {

    	Map<String, String> propertiesMap = new HashMap<String, String>();
    	configurationDao.findAllPropertiesByLocale(locale).forEach((p) -> {
    		 propertiesMap.put(p.getProperty(), p.getValue());
    	});

        return new ConfigurationPropertiesDto(locale.toString(), propertiesMap);
    }
    
    @Override
    public ConfigurationPropertiesDto getPropertiesForLocale(Locale locale, List<String> properties) {

        if (CollectionUtils.isEmpty(properties)) {
            return null;
        }
        Map<String, String> propertiesMap = properties.stream()
                .map(prop -> configurationDao.findPropertyByLocale(locale, prop))
                .collect(Collectors.toMap(p -> p.getProperty(), p -> p.getValue()));

        return new ConfigurationPropertiesDto(locale.toString(), propertiesMap);
    }
    
    private void bucketFeatureSampling(final ConfigurationDo configurationDo, int userId, List<String> enabledKeys, List<String> disabledKeys) {
    	ConfigurationTypeEnum configurationType = configurationDo.getConfigurationType();
        if (configurationTypeStrategyMap.containsKey(configurationType)) {
            if (isSampledForUser(configurationDo, userId)) {
                enabledKeys.add(configurationDo.getProperty());
            } else {
                disabledKeys.add(configurationDo.getProperty());
            }
        }
    }
    
    private boolean isSampledForUser(ConfigurationDo configurationDo, Integer userId) {
        ConfigurationTypeEnum configurationType = configurationDo.getConfigurationType();
        SamplingStrategy samplingStrategy = configurationTypeStrategyMap.get(configurationType);

        if (samplingStrategy == null) {
            log.error("No strategy found for configuration {}", configurationDo.getProperty(),
                    configurationDo.getLocale());
            samplingStrategy = DEFAULT_STRATEGY;
        }

        return samplingStrategy.isSampled(configurationDo.getProperty(), userId, configurationDo.getValue());
    }
    
    void setConfigurationDao(ConfigurationDao configurationDao) {
    	Preconditions.checkNotNull(configurationDao);
		this.configurationDao = configurationDao;
	}

}
