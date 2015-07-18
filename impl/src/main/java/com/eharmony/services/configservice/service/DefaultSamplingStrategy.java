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
package com.eharmony.services.configservice.service;

/**
 * Default strategy for calculation Sampling for a given User and sampling
 * value.
 *
 * @author  adigumarti
 */
public class DefaultSamplingStrategy
          implements SamplingStrategy {

    /**
     * The default Strategy will always return false.
     */
    @Override public boolean isSampled(String testName, Integer userId,
                                       String samplingValue) {

        return false;

    }

}
