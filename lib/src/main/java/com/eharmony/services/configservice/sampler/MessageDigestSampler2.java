package com.eharmony.services.configservice.sampler;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;


/**
 * Class is not public as it is not to be used. It is for performance testing only and to show various choices
 */
class MessageDigestSampler2 extends ModSampler {
    private final String digestName;
    private LoadingCache<String, Integer> hashFunction = CacheBuilder.newBuilder().maximumSize(10000)
            .build(new CacheLoader<String, Integer>() {
                public Integer load(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
                    MessageDigest digest = MessageDigest.getInstance("MD5");
                    return convertToInteger(digest.digest("TEST".getBytes("UTF-8")));
                }
            });


    public MessageDigestSampler2(String digestName, int buckets) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        super(buckets);
        this.digestName = digestName;
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.digest("TEST".getBytes("UTF-8"));
    }

    @Override
    public int convertToBucket(String test, String id) {
        int testHash = hashFunction.apply(test);
        try {
            MessageDigest digest = MessageDigest.getInstance(digestName);
            digest.update(test.getBytes("UTF-8"));
            digest.update(id.getBytes("UTF-8"));
            byte[] hashDigest = digest.digest();
            int bucket = (convertToInteger(hashDigest) ^ testHash) % buckets;
            return bucket >= 0 ? bucket : -bucket;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("This should never happen, thrown in constructor", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("This should never happen, thrown in constructor", e);
        }
    }



    public int convertToInteger(byte[] hashDigest) {
        int i = (hashDigest[0] << 24) & 0xff000000 | (hashDigest[1] << 16) & 0x00ff0000 | (hashDigest[2] << 8)
                & 0x0000ff00 | (hashDigest[3] << 0) & 0x000000ff;
        return i;
    }
    
}
