package com.eharmony.services.configservice.sampler;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

/**
 * Class is not public as it is not to be used. It is for performance testing only and to show various choices
 */
class GuavaMD5Sampler extends ModSampler {
    private LoadingCache<String, HashCode> hashFunction = CacheBuilder.newBuilder().maximumSize(10000)
            .build(new CacheLoader<String, HashCode>() {
                public HashCode load(String key) throws UnsupportedEncodingException {
                    return Hashing.md5().hashBytes(key.getBytes("UTF-8"));
                }
            });

    public GuavaMD5Sampler(int buckets) {
        super(buckets);
    }

    @Override
    public int convertToBucket(String test, String id) {
        HashCode testHash = hashFunction.apply(test);
        try {
            HashCode idHash = Hashing.md5().hashBytes(id.getBytes("UTF-8"));
            int bucket = Hashing.combineOrdered(Arrays.asList(testHash,idHash)).asInt() % buckets;
            return bucket >= 0 ? bucket : -bucket;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Should never happend, UTF-8 not supported?", e);
        }
    }
}
