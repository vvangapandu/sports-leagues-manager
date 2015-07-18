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
 * Sampling Strategy for Sampling values of type Integer. The Strategy uses the
 * modulo method to calculate if the user is sampled.
 *
 * @author  adigumarti
 */
public class ConfigurationTypeIntegerSamplingStrategy
          implements SamplingStrategy {

    @Override public boolean isSampled(String testName, Integer userId,
                                       String samplingValue) {

        Integer percentage = Integer.parseInt(samplingValue);
        return sampleByUserid(userId, percentage);

    }

    /**
     * Samples a user by percentage based on the userId. Returns true if the
     * sampling succeeded and false otherwise. Returns false if the percentage
     * parameter is zero or greater than 100;
     *
     * @param   userId user to sample
     * @param   percentage percent sampling 0 to 100.00
     *
     * @return true if the user is sampled
     */
    private boolean sampleByUserid(int userId,
                                   int percentage) {

        if ((userId % 100) < percentage) {

            return true;

        }

        return false;

    }

}
