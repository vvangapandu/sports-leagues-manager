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
 * Sampling Strategy for Decimal values. Decimal values are assumed to be 2
 * decimal points. The strategy uses the modulo method to calculation if the
 * user is sampled.
 *
 * @author  adigumarti
 */
public class ConfigurationTypeFloatSamplingStrategy
          implements SamplingStrategy {

    @Override public boolean isSampled(String testName, Integer userId,
                                       String samplingValue) {

        Float samplingPercent = Float.parseFloat(samplingValue);

        return sampleByUserid(userId, samplingPercent);

    }

    /**
     * Samples a user by percentage based on the userId. The percentage is a
     * floating point number with 2 decimal places.
     *
     * @param   userId user to sample
     * @param   percentage percent sampling 0 to 100.00
     *
     * @return true if the user is sampled
     */
    private boolean sampleByUserid(int userId,
                                   float percentage) {

        if ((userId % 10000) < (percentage * 100)) {

            return true;

        }

        return false;

    }

}
