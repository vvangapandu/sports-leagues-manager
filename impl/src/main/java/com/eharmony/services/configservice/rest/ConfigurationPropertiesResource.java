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
package com.eharmony.services.configservice.rest;

import java.util.List;
import java.util.Locale;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eharmony.services.configservice.service.ConfigurationService;
import com.google.common.collect.Lists;

@Component
@Path("/v1")
public class ConfigurationPropertiesResource {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationPropertiesResource.class);

    @Autowired
    private AsyncRequestHandler asyncRequestHandler;
    
    @Autowired
    private ConfigurationService configurationService;

    public ConfigurationPropertiesResource() {

        log.info("initialized ConfigurationPropertiesResource...");
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("locale/{locale}/properties")
    @Produces("application/json; charset=utf-8")
    public void getConfigPropertiesForLocale(@PathParam("locale") String sLocale, ConfigPropertiesRequest propertiesRequest,
            @Suspended final AsyncResponse asyncResponse) {

        Locale locale = LocaleUtils.toLocale(sLocale);

        try {
            RequestValidator.validateConfigPropertiesRequest(propertiesRequest);
            List<String> properties = propertiesRequest.getProperties();
            log.debug("Retrieving Configuration Properties for Locale {} filtered by {}", sLocale, properties);
            
            asyncResponse.resume(asyncRequestHandler.getResponse(configurationService
                    .getPropertiesForLocale(locale, properties), false));

        } catch (Exception ex) {
            log.error("Exception while fetching ConfigFeatures for locale {}", locale, ex);
            asyncRequestHandler.handleException(asyncResponse, ex);
        }
    }
    
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("locale/{locale}/properties")
    @Produces("application/json; charset=utf-8")
    public void getAllConfigPropertiesForLocale(@PathParam("locale") String sLocale, @Suspended final AsyncResponse asyncResponse) {

        Locale locale = LocaleUtils.toLocale(sLocale);
        try {

            log.debug("Retrieving Configuration Features for Locale {}", sLocale);

            asyncResponse.resume(asyncRequestHandler.getResponse(configurationService.getAllPropertiesForLocale(locale), false));
            
        } catch (Exception ex) {
            log.error("Exception while fetching Config Features for locale {}", locale, ex);
            asyncRequestHandler.handleException(asyncResponse, ex);
        }

    }
    
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("locale/{locale}/properties/{property}")
    @Produces("application/json; charset=utf-8")
    public void getConfigPropertyForLocale(@PathParam("locale") final String sLocale,  @PathParam("property") final String property, 
    		@Suspended final AsyncResponse asyncResponse) {

        Locale locale = LocaleUtils.toLocale(sLocale);
        try {

            log.debug("Retrieving Configuration property {} for Locale {}", property, sLocale);

            asyncResponse.resume(asyncRequestHandler.getResponse(configurationService.getPropertiesForLocale(locale, Lists.newArrayList(property)), false));
            
        } catch (Exception ex) {
            log.error("Exception while fetching Config Features for locale {}", locale, ex);
            asyncRequestHandler.handleException(asyncResponse, ex);
        }

    }
    
}
