package com.eharmony.services.configservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SamplingLogger {

    private static final Logger samplingLog = LoggerFactory.getLogger("SAMPLING_LOG");
    private static final Logger log = LoggerFactory.getLogger(SamplingLogger.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> void writeSamplingLog(T configFeatures) {
        try {
            samplingLog.info("{}", mapper.writeValueAsString(configFeatures));
        } catch (Exception e) {
            log.error("Could not write json");
        }
    }
}
