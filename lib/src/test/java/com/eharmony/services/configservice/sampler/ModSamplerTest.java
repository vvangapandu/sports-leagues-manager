package com.eharmony.services.configservice.sampler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eharmony.services.configservice.sampler.ModSampler;

public class ModSamplerTest {
    ModSampler sampler;
    Logger log = LoggerFactory.getLogger(getClass());
    @Before
    public void setup() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        sampler = new ModSampler(100);
    }

    @Test
    public void testConvertToBucket() {
        // this is why mod sampling is the worst... 100% covariance
        assertEquals(56, sampler.convertToBucket("something.something.sampling.random", "123456"));
        assertEquals(57, sampler.convertToBucket("something.something.sampling.random", "123457"));
        assertEquals(56, sampler.convertToBucket("something.somethingelse.sampling.random", "123456"));
        assertEquals(57, sampler.convertToBucket("something.somethingelse.sampling.random", "123457"));
        for (int i=10000; i<11000; i++) {
            int bucket = sampler.convertToBucket("something.something.sampling.random", Integer.toString(i));
            assertTrue(i + " was "  +bucket , bucket >= 0 && bucket < 1000);
        }
    }
    @Test
    public void testIsSampled() {
        assertEquals(false, sampler.isSampled("something.something.sampling.random", "123456", 50));
        assertEquals(false, sampler.isSampled("something.something.sampling.random", "123457", 50));
        assertEquals(true, sampler.isSampled("something.somethingelse.sampling.random", "123456", 57));
        assertEquals(false, sampler.isSampled("something.somethingelse.sampling.random", "123457", 57));
        for (int i=10000; i<11000; i++) {
            boolean isSampled = sampler.isSampled("something.something.sampling.random", Integer.toString(i), 50);
            assertTrue(i + " was "  + isSampled, isSampled == i%100 < 50);
        }
    }
    

}
