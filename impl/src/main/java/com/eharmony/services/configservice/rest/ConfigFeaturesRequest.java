package com.eharmony.services.configservice.rest;

import java.util.List;

public class ConfigFeaturesRequest {

    private List<String> features;

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }
}
