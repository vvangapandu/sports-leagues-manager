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
public class ConfigurationFeaturesResource {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationFeaturesResource.class);

    @Autowired
    private AsyncRequestHandler asyncRequestHandler;
   
    @Autowired
    private ConfigurationService configurationService;
    
    @Autowired
    private ConfigurationFeaturesRXHandler configurationFeaturesRXHandler;

    public ConfigurationFeaturesResource() {
        log.info("initialized ConfigurationFeaturesResource...");
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("user/{userId}/locale/{locale}/features")
    @Produces("application/json; charset=utf-8")
    public void getAllConfigFeaturesForUser(@PathParam("userId") Integer userId, @PathParam("locale") String sLocale,@Suspended final AsyncResponse asyncResponse) {

        Locale locale = LocaleUtils.toLocale(sLocale);
        try {
            log.debug("Retrieving Configuration Features in sync for UserId {} and Locale {} ", userId, sLocale);

            asyncResponse.resume(asyncRequestHandler.getResponse(configurationService
                    .getSampledFeaturesForUserAndLocale(userId, locale)));

        } catch (Exception ex) {
            log.error("Exception while fetching ConfigFeatures for user {} and locale {} ", userId, locale, ex);
            asyncRequestHandler.handleException(asyncResponse, ex);
        }

    }
    
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("user/{userId}/locale/{locale}/features/{feature}")
    @Produces("application/json; charset=utf-8")
    public void getConfigFeatureForUser(@PathParam("userId") Integer userId, @PathParam("locale") String sLocale, 
    		final @PathParam("feature") String feature, @Suspended final AsyncResponse asyncResponse) {

        Locale locale = LocaleUtils.toLocale(sLocale);
        try {
            log.debug("Retrieving Configuration Features in sync for UserId {} and Locale {} ", userId, sLocale);

            configurationFeaturesRXHandler.handleSampledFeaturesForUserAndLocale(userId, locale, Lists.newArrayList(feature), asyncResponse);

        } catch (Exception ex) {
            log.error("Exception while fetching ConfigFeatures for user {} and locale {} ", userId, locale, ex);
            asyncRequestHandler.handleException(asyncResponse, ex);
        }

    }
    
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("user/{userId}/locale/{locale}/features")
    @Produces("application/json; charset=utf-8")
    public void getConfigFeaturesForUser(@PathParam("userId") Integer userId, @PathParam("locale") String sLocale,
            ConfigFeaturesRequest featuresRequest, @Suspended final AsyncResponse asyncResponse) {

        Locale locale = LocaleUtils.toLocale(sLocale);
        try {
            RequestValidator.validateConfigFeaturesRequest(featuresRequest);
            List<String> features = featuresRequest.getFeatures();

            log.debug("Retrieving Configuration Features for UserId {} and Locale {} filtered by {}", userId, sLocale,
                    features);

            configurationFeaturesRXHandler.handleSampledFeaturesForUserAndLocale(userId, locale, features, asyncResponse);
            
        } catch (Exception ex) {
            log.error("Exception while fetching Config Features for locale {}", locale, ex);
            asyncRequestHandler.handleException(asyncResponse, ex);
        }

    }
    
}
