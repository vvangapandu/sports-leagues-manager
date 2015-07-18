package com.eharmony.services.configservice.configuration;

import java.util.EnumMap;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.eharmony.services.configservice.model.ConfigurationDo.ConfigurationTypeEnum;
import com.eharmony.services.configservice.service.ConfigurationTypeBooleanSamplingStrategy;
import com.eharmony.services.configservice.service.ConfigurationTypeFloatSamplingStrategy;
import com.eharmony.services.configservice.service.ConfigurationTypeIntegerSamplingStrategy;
import com.eharmony.services.configservice.service.ConfigurationTypeRandomSamplingStrategy;
import com.eharmony.services.configservice.service.DefaultSamplingStrategy;
import com.eharmony.services.configservice.service.SamplingStrategy;

@Component
public class ConfigurationTypeStrategyMapProducer {

	@Resource(name="md5SamplingStrategy")
	private ConfigurationTypeRandomSamplingStrategy randomSamplingStrategy;
	
	@Bean(name="configurationTypeStrategyMap")
	public EnumMap<ConfigurationTypeEnum, SamplingStrategy> getConfigurationTypeStrategyMap() {
		
		EnumMap<ConfigurationTypeEnum, SamplingStrategy> configurationTypeStrategyMap = new EnumMap<ConfigurationTypeEnum, SamplingStrategy>(ConfigurationTypeEnum.class);
		
		configurationTypeStrategyMap.put(ConfigurationTypeEnum.BOOLEAN, new ConfigurationTypeBooleanSamplingStrategy());
		configurationTypeStrategyMap.put(ConfigurationTypeEnum.FLOAT, new ConfigurationTypeFloatSamplingStrategy());
		configurationTypeStrategyMap.put(ConfigurationTypeEnum.INTEGER, new ConfigurationTypeIntegerSamplingStrategy());
		configurationTypeStrategyMap.put(ConfigurationTypeEnum.STRING, new DefaultSamplingStrategy());
		configurationTypeStrategyMap.put(ConfigurationTypeEnum.MD5, randomSamplingStrategy);
		
		return configurationTypeStrategyMap;
	}
}
