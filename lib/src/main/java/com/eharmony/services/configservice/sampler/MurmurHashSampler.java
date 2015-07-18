package com.eharmony.services.configservice.sampler;

import java.io.UnsupportedEncodingException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * Class is not public as it is not to be used. It is for performance testing only and to show various choices
 * 
 * This is a murmur hash sampler. It does murmur3(murmur3().hash(test)).hash(id)
 */
class MurmurHashSampler extends ModSampler {
    private final HashFunction testHasher;
    private LoadingCache<String, HashFunction> hashFunction = CacheBuilder.newBuilder().maximumSize(10000)
            .build(new CacheLoader<String, HashFunction>() {
                public HashFunction load(String key) throws UnsupportedEncodingException {
                    int seed = testHasher.hashBytes(key.getBytes("UTF-8")).asInt();
                    return Hashing.murmur3_32(seed);
                }
            });

    public MurmurHashSampler(int buckets) {
        super(buckets);
        testHasher = Hashing.murmur3_32();
    }

    @Override
    public int convertToBucket(String test, String id) {
        HashFunction hasher = hashFunction.apply(test);
        try {
            int bucket = hasher.hashBytes(id.getBytes("UTF-8")).asInt() % buckets;
            return bucket >= 0 ? bucket : -bucket;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Should never happend, UTF-8 not supported?", e);
        }
    }
}
