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
package com.eharmony.services.configservice.model;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;


@Entity @Table(name = "EHCONFIGURATION")
public class ConfigurationDo {

    public enum ConfigurationTypeEnum {

        BOOLEAN, FLOAT, INTEGER, STRING, MD5;

    }

    @Column(name = "CONFIGURATIONTYPE")
    @Enumerated(EnumType.ORDINAL)
    private ConfigurationTypeEnum configurationType;

    @Column(name = "ID")
    @GeneratedValue @Id private Integer id;

    @Column(name = "LOCALE")
    private Locale locale;

    @Column(name = "PROPERTY")
    private String property;

    @Column(name = "VALUE")
    private String value;

    public ConfigurationTypeEnum getConfigurationType() {

        return configurationType;

    }

    public Integer getId() {

        return id;

    }

    public Locale getLocale() {

        return locale;

    }

    public String getProperty() {

        return property;

    }

    public String getValue() {

        return value;

    }

    public void setConfigurationType(ConfigurationTypeEnum configurationType) {

        this.configurationType = configurationType;

    }

    public void setId(Integer id) {

        this.id = id;

    }

    public void setLocale(Locale locale) {

        this.locale = locale;

    }

    public void setProperty(String property) {

        this.property = property;

    }

    public void setValue(String value) {

        this.value = value;

    }

    @Override public String toString() {

        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("id", id);
        builder.append("property", property);
        builder.append("value", value);
        builder.append("locale", locale);
        builder.append("configurationType", configurationType);
        return builder.toString();

    }

}
