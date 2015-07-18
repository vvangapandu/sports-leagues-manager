package com.eharmony.services.configservice.sampler;

public interface RandomSampler {

    public abstract boolean isSampled(String test, String id, int samplingRate);

}