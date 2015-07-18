package com.eharmony.services.configservice.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eharmony.services.configservice.dao.ConfigurationDao;
import com.eharmony.services.configservice.dto.ConfigurationPropertiesDto;
import com.eharmony.services.configservice.model.ConfigurationDo;
import com.eharmony.services.configservice.model.ConfigurationDo.ConfigurationTypeEnum;
import com.eharmony.services.configservice.service.ConfigurationServiceImpl;
import com.google.common.collect.Lists;

public class ConfigurationServiceImplTest {
	
	ConfigurationServiceImpl service;
    Logger log = LoggerFactory.getLogger(getClass());
    
    ConfigurationDao configurationDao;
    @Before
    public void setup() throws NoSuchAlgorithmException, UnsupportedEncodingException {
    	service = new ConfigurationServiceImpl();
    	configurationDao = Mockito.mock(ConfigurationDao.class);
    	service.setConfigurationDao(configurationDao);
    }

    @Test
    public void testGetAllPropertiesForLocale() {
    	Set<ConfigurationDo> set = new HashSet<ConfigurationDo>();
    	ConfigurationDo config = new ConfigurationDo();
    	config.setConfigurationType(ConfigurationTypeEnum.STRING);
    	config.setProperty("test.property");
    	config.setValue("test.value");
    	set.add(config);
    	Mockito.when(configurationDao.findAllPropertiesByLocale(Mockito.any())).thenReturn(set);
    
    	ConfigurationPropertiesDto dto = service.getAllPropertiesForLocale(Locale.US);
    	Assert.assertNotNull(dto);
    	Assert.assertEquals("test.value", dto.getPropertiesMap().get("test.property"));
    }
    
    @Test
    public void testGetPropertiesForLocale() {
    	ConfigurationDo config = new ConfigurationDo();
    	config.setConfigurationType(ConfigurationTypeEnum.STRING);
    	config.setProperty("test.property");
    	config.setValue("test.value");
    	Mockito.when(configurationDao.findPropertyByLocale(Mockito.any(),Mockito.any())).thenReturn(config);
    
    	ConfigurationPropertiesDto dto = service.getPropertiesForLocale(Locale.US, Lists.newArrayList("test.property"));
    	Assert.assertNotNull(dto);
    	Assert.assertEquals("test.value", dto.getPropertiesMap().get("test.property"));
    }

}
