
package com.nscube.services.leaguemanager.util;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;


/**
 * Custom version of Jackson ObjectMapper that configures serialization of dates
 * as timestamps and ignores unknown properties on JSON deserialization.
 *
 */
public class CustomObjectMapper
          extends ObjectMapper {

    /**
     * Creates a new instance with custom settings.
     */
    public CustomObjectMapper() {

        this.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

}
