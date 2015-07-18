package com.eharmony.services.configservice.sampler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eharmony.services.configservice.sampler.GuavaMD5Sampler;

public class GuavaMD5SamplerTest {
    GuavaMD5Sampler sampler;
    Logger log = LoggerFactory.getLogger(getClass());
    @Before
    public void setup() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        sampler = new GuavaMD5Sampler(1000);
    }

    @Test
    public void testConvertToBucket() {
        // this does not match what we do with MessageDigestSampler
        assertEquals(665, sampler.convertToBucket("something.something.sampling.random", "123456"));
        assertEquals(753, sampler.convertToBucket("something.something.sampling.random", "123457"));
        assertEquals(536, sampler.convertToBucket("something.somethingelse.sampling.random", "123456"));
        assertEquals(544, sampler.convertToBucket("something.somethingelse.sampling.random", "123457"));
        for (int i=10000; i<11000; i++) {
            int bucket = sampler.convertToBucket("something.something.sampling.random", Integer.toString(i));
            assertTrue(i + " was "  +bucket , bucket >= 0 && bucket < 1000);
        }
    }


}
