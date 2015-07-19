package com.nscube.services.leaguemanager.configuration;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.RequestContextFilter;

public class JerseyApp extends ResourceConfig {
    Logger log = LoggerFactory.getLogger(getClass());

    public JerseyApp() {
        log.info("Initializing App");
        register(RequestContextFilter.class);
        register(JacksonFeature.class);
    }

}
