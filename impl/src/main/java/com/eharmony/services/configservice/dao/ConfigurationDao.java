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
package com.eharmony.services.configservice.dao;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.codahale.metrics.annotation.Timed;
import com.eharmony.services.configservice.model.ConfigurationDo;

@Repository
public class ConfigurationDao {

    private static String LOCALE = "locale";
    private static String PROPERTY = "property";
    
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Returns all Sampled features for the given locale. Sampling Features have Configuration Types Boolean, Integer,
     * Float and MD5.
     *
     * @param locale Locale
     *
     * @return Set set of ConfigurationDo
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Timed(name="findAllPropertiesByLocale")
    public Set<ConfigurationDo> findAllPropertiesByLocale(Locale locale) {

        Criteria criteria = getSession().createCriteria(ConfigurationDo.class);
        criteria.add(Restrictions.eqOrIsNull(LOCALE, locale));
        List<ConfigurationDo> propertiesList = criteria.setCacheable(true).setReadOnly(true).list();

        Criteria criteriaNull = getSession().createCriteria(ConfigurationDo.class);
        criteriaNull.add(Restrictions.isNull(LOCALE));
        List<ConfigurationDo> defaultLocaleProperties = criteria.setCacheable(true).setReadOnly(true).list();
        
        Comparator<ConfigurationDo> byProperty = Comparator.comparing(ConfigurationDo::getProperty);
        Set<ConfigurationDo> returnProperties = new TreeSet<ConfigurationDo>(byProperty);
        returnProperties.addAll(propertiesList);
        returnProperties.addAll(defaultLocaleProperties);
        return returnProperties;

    }

    /**
     * Retrieves the configuration property value from db for given locale and propertyname.
     * If the locale is null or if there is no config property available for given locale, it will fallback to default locale.
     * 
     * @param locale Locale
     * @param property String
     * @return ConfigurationDo 
     */
    @Transactional(readOnly = true)
    @Timed(name="findPropertyByLocale")
    public ConfigurationDo findPropertyByLocale(Locale locale, String property) {

        Criteria criteria = getSession().createCriteria(ConfigurationDo.class);

        criteria.add(Restrictions.eqOrIsNull(LOCALE, locale));
        criteria.add(Restrictions.eq(PROPERTY, property));

        ConfigurationDo returnObj = (ConfigurationDo) criteria.setCacheable(true).setReadOnly(true).uniqueResult();

        if (returnObj == null) {
            criteria = getSession().createCriteria(ConfigurationDo.class);
            criteria.add(Restrictions.isNull(LOCALE));
            criteria.add(Restrictions.eq(PROPERTY, property));

            returnObj = (ConfigurationDo) criteria.setCacheable(true).setReadOnly(true).uniqueResult();
        }
        return returnObj;

    }

    /**
     * Gets the current Session from the backing SessionFactory. This method assumes that the SessionFactory has been
     * primed with a Session, either through Spring or some other transaction management API.
     *
     * @return The current Session from the backing SessionFactory.
     */
    private Session getSession() {

        return sessionFactory.getCurrentSession();

    }

}
