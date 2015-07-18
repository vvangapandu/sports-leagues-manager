package com.eharmony.services.configservice.sampler;


public class ModSampler implements RandomSampler {
    protected final int buckets;
    public ModSampler(int buckets) {
        this.buckets = buckets;
    }
    /* (non-Javadoc)
     * @see com.eharmony.services.configservice.sampler.Sampler#isSampled(java.lang.String, int, int)
     */
    @Override
    public boolean isSampled(String test, String id, int samplingRate) {
        int bucket = convertToBucket(test, id);
        return bucket < samplingRate;
    }
    
    public int convertToBucket(String test, String id) {
        return Integer.parseInt(id) % buckets;
    }

}
