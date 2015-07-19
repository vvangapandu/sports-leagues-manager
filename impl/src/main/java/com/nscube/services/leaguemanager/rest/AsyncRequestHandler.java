package com.nscube.services.leaguemanager.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Component;

@Component
public class AsyncRequestHandler {

    /*@Value("${cache.header.timeout}")*/
    private int cacheTimeout=900;

    public void handleException(final AsyncResponse asyncResponse, Throwable t) {
    	if(t instanceof WebApplicationException) {
    		asyncResponse.resume(t);
    		return;
    	}
    	asyncResponse.resume(new WebApplicationException(t.getMessage(), Status.INTERNAL_SERVER_ERROR));
    }
    
    public <T> Response getResponse(T configFeatures, boolean writeSamplingLog) {
    	
        ResponseBuilder builder = Response.ok(configFeatures);
        builder.cacheControl(getCacheControl());

        return builder.build();
    }
    
    public <T> Response getResponse(T configFeatures) {
    	return getResponse(configFeatures, true);
    }

    private CacheControl getCacheControl() {
        CacheControl cc = new CacheControl();

        cc.setMaxAge(cacheTimeout);
        cc.setPrivate(true);

        return cc;
    }
}
