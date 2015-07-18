package com.eharmony.services.configservice.sampler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eharmony.services.configservice.sampler.MessageDigestSampler;

public class MessageDigestSamplerTest {
    MessageDigestSampler sampler;
    Logger log = LoggerFactory.getLogger(getClass());
    @Before
    public void setup() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        sampler = new MessageDigestSampler("MD5",1000);
    }

    @Test
    public void testConvertToBucket() {
        assertEquals(812, sampler.convertToBucket("something.something.sampling.random", "123456"));
        assertEquals(27, sampler.convertToBucket("something.something.sampling.random", "123457"));
        assertEquals(984, sampler.convertToBucket("something.somethingelse.sampling.random", "123456"));
        assertEquals(944, sampler.convertToBucket("something.somethingelse.sampling.random", "123457"));
        for (int i=10000; i<11000; i++) {
            int bucket = sampler.convertToBucket("something.something.sampling.random", Integer.toString(i));
            assertTrue(i + " was "  +bucket , bucket >= 0 && bucket < 1000);
        }
    }


}
