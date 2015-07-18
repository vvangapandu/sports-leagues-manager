package com.eharmony.services.configservice.service;

import com.eharmony.services.configservice.sampler.RandomSampler;

public class ConfigurationTypeRandomSamplingStrategy implements SamplingStrategy {
    private final RandomSampler sampler;
    public ConfigurationTypeRandomSamplingStrategy(RandomSampler sampler) {
        this.sampler = sampler;
    }
    @Override
    public boolean isSampled(String testName, Integer userId, String samplingValue) {
        return sampler.isSampled(testName, userId.toString(), Integer.parseInt(samplingValue));
    }

}
