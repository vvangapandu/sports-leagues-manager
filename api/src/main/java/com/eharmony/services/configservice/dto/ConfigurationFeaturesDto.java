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
package com.eharmony.services.configservice.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;


/**
 * Configuration Features API Response object
 *
 * @author  adigumarti
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigurationFeaturesDto {

    private final String locale;
    private final Integer userId;
    
    private final List<String> enabledFeatures;
    private final List<String> disabledFeatures;

    @JsonCreator
    public ConfigurationFeaturesDto(Integer userId,
                                    String locale,
                                    List<String> enabledFeatures,
                                    List<String> disabledFeatures) {

        super();
        this.userId = userId;
        this.locale = locale;
        this.enabledFeatures = enabledFeatures;
        this.disabledFeatures = disabledFeatures;
    }

    public String getLocale() {
        return locale;
    }
    public Integer getUserId() {
        return userId;
    }

    public List<String> getEnabledFeatures() {
        return enabledFeatures;
    }

    public List<String> getDisabledFeatures() {
        return disabledFeatures;
    }

    /* (non-Javadoc)
     * Auto generated from eclipse
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((disabledFeatures == null) ? 0 : disabledFeatures.hashCode());
        result = prime * result + ((enabledFeatures == null) ? 0 : enabledFeatures.hashCode());
        result = prime * result + ((locale == null) ? 0 : locale.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    
    /* (non-Javadoc)
     * Auto generated from eclipse
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConfigurationFeaturesDto other = (ConfigurationFeaturesDto) obj;
        if (disabledFeatures == null) {
            if (other.disabledFeatures != null)
                return false;
        } else if (!disabledFeatures.equals(other.disabledFeatures))
            return false;
        if (enabledFeatures == null) {
            if (other.enabledFeatures != null)
                return false;
        } else if (!enabledFeatures.equals(other.enabledFeatures))
            return false;
        if (locale == null) {
            if (other.locale != null)
                return false;
        } else if (!locale.equals(other.locale))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }


}
