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

import java.util.List;
import java.util.Locale;

import com.eharmony.services.configservice.dto.ConfigurationFeaturesDto;
import com.eharmony.services.configservice.dto.ConfigurationPropertiesDto;

/**
 * Service to lookup the configurations and features for given user and locale.
 * 
 * @author vvangapandu
 *
 */
public interface ConfigurationService {

    /**
     * Returns all Properties and the corresponding values for a given locale and the list of provided properties. If a
     * Property is not present, it is ignored.
     *
     * @param locale     Locale of the user
     * @param properties selected properties to lookup
     *
     * @return ConfigurationPropertiesDto
     */
    public ConfigurationPropertiesDto getPropertiesForLocale(Locale locale, List<String> properties);

    /**
     * Returns all sampled features for a given locale and userId.
     *
     * @param userId   logged in user
     * @param locale   Locale of the user
     *
     * @return ConfigurationFeaturesDto configurations
     */
    public ConfigurationFeaturesDto getSampledFeaturesForUserAndLocale(Integer userId, Locale locale);

    /**
     * Returns all sampled features for a given locale and userId filtered by the list of features provided.
     *
     * @param userId   logged in user
     * @param locale   Locale of the user
     * @param features List of selected properties to lookup
     *
     * @return ConfigurationFeaturesDto configurations
     * 
     */
    public ConfigurationFeaturesDto getSampledFeaturesForUserAndLocale(Integer userId, Locale locale,
            List<String> features);
    

    /**
     * Evaluates whether the given user is sampled for given property.
     * 
     * @param userId   Integer
     * @param locale   Locale
     * @param property String
     * @return boolean
     */
    public boolean isFeatureSampledForUser(Integer userId, Locale locale, String property);
    
    /** 
     * Returns all Properties and the corresponding values for a given locale. If a
     * Property is not present, it is ignored.
     *
     * @param locale     Locale of the user
     *
     * @return ConfigurationPropertiesDto
     */
    public ConfigurationPropertiesDto getAllPropertiesForLocale(Locale locale);

}
