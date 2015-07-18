package com.eharmony.services.configservice.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;

public class RequestValidator {

    public static void validateConfigFeaturesRequest(final ConfigFeaturesRequest configFeaturesRequest) {
        
        if(configFeaturesRequest == null || CollectionUtils.isEmpty(configFeaturesRequest.getFeatures())) {

            throw400();

        }
    }
    
    public static void validateConfigPropertiesRequest(final ConfigPropertiesRequest configPropertiesRequest) {
        
        if(configPropertiesRequest == null || CollectionUtils.isEmpty(configPropertiesRequest.getProperties())) {

            throw400();

        }
    }

    private static void throw400() {

        String msg = "Invalid request: Reqired Properties missing.";
        Response error400 = Response.status(Response.Status.BAD_REQUEST).entity(msg).type("text/plain").build();
        throw new WebApplicationException(error400);

    }
}
