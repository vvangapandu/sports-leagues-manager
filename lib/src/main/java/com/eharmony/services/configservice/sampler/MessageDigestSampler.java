package com.eharmony.services.configservice.sampler;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class is not public as it is not to be used. It is for performance testing only and to show various choices
 */
class MessageDigestSampler extends ModSampler {
    private final String digestName;

    public MessageDigestSampler(String digestName, int buckets) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        super(buckets);
        this.digestName = digestName;
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.digest("TEST".getBytes("UTF-8"));
    }

    @Override
    public int convertToBucket(String test, String id) {
        try {
            MessageDigest digest = MessageDigest.getInstance(digestName);
            digest.update(test.getBytes("UTF-8"));
            digest.update(id.getBytes("UTF-8"));
            byte[] hashDigest = digest.digest();
            int bucket = convertToBucket(hashDigest);
            return bucket >= 0 ? bucket : -bucket;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("This should never happen, thrown in constructor", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("This should never happen, thrown in constructor", e);
        }
    }

    public int convertToBucket(byte[] hashDigest) {
        int i = (hashDigest[0] << 24) & 0xff000000 | (hashDigest[1] << 16) & 0x00ff0000 | (hashDigest[2] << 8)
                & 0x0000ff00 | (hashDigest[3] << 0) & 0x000000ff;
        return i % buckets;
    }
    
}
