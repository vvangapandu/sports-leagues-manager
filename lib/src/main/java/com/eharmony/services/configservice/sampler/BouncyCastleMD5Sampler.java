package com.eharmony.services.configservice.sampler;

import java.io.UnsupportedEncodingException;

import org.bouncycastle.crypto.digests.MD5Digest;

public class BouncyCastleMD5Sampler extends ModSampler {

    public BouncyCastleMD5Sampler(int buckets) {
        super(buckets);
    }

    @Override
    public int convertToBucket(String test, String id) {
        try {
            MD5Digest md5 = new MD5Digest();
            byte[] testBytes = test.getBytes("UTF-8");
            md5.update(testBytes, 0, testBytes.length);
            byte[] idBytes = id.getBytes("UTF-8");
            md5.update(idBytes, 0, idBytes.length);
            byte[] hashDigest = new byte[md5.getDigestSize()];
            md5.doFinal(hashDigest, 0);
            int bucket = convertToBucket(hashDigest);
            return bucket >= 0 ? bucket : -bucket;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("This should never happen, no UTF-8?", e);
        }
    }

    public int convertToBucket(byte[] hashDigest) {
        int i = (hashDigest[0] << 24) & 0xff000000 | (hashDigest[1] << 16) & 0x00ff0000 | (hashDigest[2] << 8)
                & 0x0000ff00 | (hashDigest[3] << 0) & 0x000000ff;
        return i % buckets;
    }
    
}
