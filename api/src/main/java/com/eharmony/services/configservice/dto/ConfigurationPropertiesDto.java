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

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *  ConfigurationProperties Data trasfer object
 *
 */
public class ConfigurationPropertiesDto {

    private String locale;
    private Map<String, String> propertiesMap;

    public ConfigurationPropertiesDto(String locale,
                                      Map<String, String> propertiesMap) {

        super();
        this.locale = locale;
        this.propertiesMap = propertiesMap;

    }

    @Override public boolean equals(Object obj) {

        if (this == obj) {

            return true;

        }
        if (obj == null) {

            return false;

        }
        if (getClass() != obj.getClass()) {

            return false;

        }
        ConfigurationPropertiesDto other = (ConfigurationPropertiesDto) obj;
        if (locale == null) {

            if (other.locale != null) {

                return false;

            }

        } else if (!locale.equals(other.locale)) {

            return false;

        }
        if (propertiesMap == null) {

            if (other.propertiesMap != null) {

                return false;

            }

        } else if (!propertiesMap.equals(other.propertiesMap)) {

            return false;

        }
        return true;

    }

    public String getLocale() {

        return locale;

    }

    public Map<String, String> getPropertiesMap() {

        return propertiesMap;

    }

    @Override public int hashCode() {

        final int prime = 31;
        int result = 1;
        result =
            (prime * result) + ((locale == null) ? 0
                                                 : locale.hashCode());
        result =
            (prime * result) + ((propertiesMap == null) ? 0
                                                        : propertiesMap.hashCode());
        return result;

    }

    public void setLocale(String locale) {

        this.locale = locale;

    }

    public void setPropertiesMap(Map<String, String> propertiesMap) {

        this.propertiesMap = propertiesMap;

    }

    @Override public String toString() {

        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("locale", locale);
        builder.append("propertiesMap", propertiesMap);
        return builder.toString();

    }

}
