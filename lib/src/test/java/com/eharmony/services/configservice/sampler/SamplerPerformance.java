package com.eharmony.services.configservice.sampler;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eharmony.services.configservice.sampler.BouncyCastleMD5Sampler;
import com.eharmony.services.configservice.sampler.GuavaMD5Sampler;
import com.eharmony.services.configservice.sampler.MessageDigestSampler;
import com.eharmony.services.configservice.sampler.MessageDigestSampler2;
import com.eharmony.services.configservice.sampler.ModSampler;
import com.eharmony.services.configservice.sampler.MurmurHashSampler;
import com.eharmony.services.configservice.sampler.RandomSampler;
import com.google.common.base.Stopwatch;

public class SamplerPerformance {
    
    public void runTestSingleThread(RandomSampler s, int start, int end, int sampling) {
        Logger log = LoggerFactory.getLogger(s.getClass());
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        AtomicInteger numSampled = new AtomicInteger(0);
        for (int i = start; i < end; i++) {
            boolean wasSampled = s.isSampled("something.something.sampling.random", Integer.toString(i), sampling);
            if (wasSampled) {
                numSampled.incrementAndGet();
            }
        }
        stopwatch.stop();
        long numSampling = end - start;
        log.info("Ran test for {} samplings. Took {} ms. Expected sampling {}, was {}", numSampling,
                stopwatch.elapsed(TimeUnit.MILLISECONDS), numSampling * sampling / 1000.0, numSampled.get());
    }

    public void runTestMultiThread(final RandomSampler s, final int start, final int end, final int sampling, final int numThreads)
            throws InterruptedException {
        Logger log = LoggerFactory.getLogger(s.getClass());
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        // AtomicInteger numSampled = new AtomicInteger(0);
        Thread[] threads = new Thread[numThreads];
        for (int thread = 0; thread < numThreads; thread++) {
            final int currentThread = thread;
            threads[thread] = new Thread() {
                public void run() {
                    for (int i = start + currentThread; i < end; i += numThreads) {
                        boolean wasSampled = s.isSampled("something.something.sampling.random", Integer.toString(i), sampling);
                        // if (wasSampled) {
                        // numSampled.incrementAndGet();
                        // }
                    }
                }
            };
            threads[thread].start();
        }
        for (int thread = 0; thread < numThreads; thread++) {
            threads[thread].join();
        }
        stopwatch.stop();
        long numSampling = end - start;
        log.info("Ran {} thread test for {} samplings. Took {} ms.", numThreads, numSampling,
                stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    public void runSuite(RandomSampler s) throws InterruptedException {
        runTestSingleThread(s, 0, 10000, 500);
        runTestSingleThread(s, 0, 100000, 400);
        runTestSingleThread(s, 500000, 600000, 600);
        runTestSingleThread(s, 5000000, 6000000, 700);
        runTestMultiThread(s, 500000, 600000, 600, 4);
        runTestMultiThread(s, 5000000, 6000000, 700, 4);
        runTestMultiThread(s, 50000000, 55000000, 200, 8);
        runTestMultiThread(s, 0, 60000000, 200, 8);
    }

    /*
     * Results from running on Ryan's macbook pro on 6/3 puts BouncyCastleMD5
     * almost twice as fast as all statistical samplings. ModSampler is fast but
     * not useful. Multiple runs consistantly put BouncyCastle as the fastest.
     * MurmurHashSampler is extremely inconsistent, probably an implementation
     * issue.
     * 
     * ACM paper says use MD5. As MD5 is the fastest, that we shall. Section
     * 4.1.2
     * http://www.exp-platform.com/documents/guidecontrolledexperiments.pdf
     * 
     * 2015-06-03 16:57:43,708 INFO
     * [com.eharmony.services.configservice.sampler.ModSampler]
     * Ran test for 10000 samplings. Took 1 ms. Expected sampling 5000.0, was
     * 5000 2015-06-03 16:57:43,719 INFO
     * [com.eharmony.services.configservice.sampler.ModSampler]
     * Ran test for 100000 samplings. Took 4 ms. Expected sampling 40000.0, was
     * 40000 2015-06-03 16:57:43,721 INFO
     * [com.eharmony.services.configservice.sampler.ModSampler]
     * Ran test for 100000 samplings. Took 1 ms. Expected sampling 60000.0, was
     * 60000 2015-06-03 16:57:43,732 INFO
     * [com.eharmony.services.configservice.sampler.ModSampler]
     * Ran test for 1000000 samplings. Took 11 ms. Expected sampling 700000.0,
     * was 700000 2015-06-03 16:57:43,736 INFO
     * [com.eharmony.services.configservice.sampler.ModSampler]
     * Ran 4 thread test for 100000 samplings. Took 3 ms. 2015-06-03
     * 16:57:43,744 INFO
     * [com.eharmony.services.configservice.sampler.ModSampler]
     * Ran 4 thread test for 1000000 samplings. Took 8 ms. 2015-06-03
     * 16:57:43,747 INFO
     * [com.eharmony.services.configservice.sampler.ModSampler]
     * Ran 8 thread test for 5000000 samplings. Took 2 ms. 2015-06-03
     * 16:57:43,754 INFO
     * [com.eharmony.services.configservice.sampler.ModSampler]
     * Ran 8 thread test for 60000000 samplings. Took 6 ms. 2015-06-03
     * 16:57:43,847 INFO
     * [com.eharmony.services.configservice.sampler
     * .MurmurHashSampler] Ran test for 10000 samplings. Took 52 ms. Expected
     * sampling 5000.0, was 5098 2015-06-03 16:57:43,934 INFO
     * [com.eharmony.services
     * .configservice.sampler.MurmurHashSampler] Ran test for
     * 100000 samplings. Took 86 ms. Expected sampling 40000.0, was 40298
     * 2015-06-03 16:57:43,958 INFO
     * [com.eharmony.services.configservice
     * .sampler.MurmurHashSampler] Ran test for 100000 samplings. Took 23 ms.
     * Expected sampling 60000.0, was 59903 2015-06-03 16:57:44,236 INFO
     * [com.eharmony
     * .services.configservice.sampler.MurmurHashSampler] Ran
     * test for 1000000 samplings. Took 278 ms. Expected sampling 700000.0, was
     * 700050 2015-06-03 16:57:44,275 INFO
     * [com.eharmony.services.configservice
     * .sampler.MurmurHashSampler] Ran 4 thread test for 100000 samplings. Took
     * 37 ms. 2015-06-03 16:57:44,481 INFO
     * [com.eharmony.services.configservice
     * .sampler.MurmurHashSampler] Ran 4 thread test for 1000000 samplings. Took
     * 206 ms. 2015-06-03 16:57:45,462 INFO
     * [com.eharmony.services.configservice
     * .sampler.MurmurHashSampler] Ran 8 thread test for 5000000 samplings. Took
     * 980 ms. 2015-06-03 16:58:01,932 INFO
     * [com.eharmony.services.configservice
     * .sampler.MurmurHashSampler] Ran 8 thread test for 60000000 samplings.
     * Took 16470 ms. 2015-06-03 16:58:01,989 INFO
     * [com.eharmony.services.configservice.sampler.MessageDigestSampler] Ran test for 10000
     * samplings. Took 55 ms. Expected sampling 5000.0, was 4974 2015-06-03
     * 16:58:02,095 INFO
     * [com.eharmony.services.configservice.sampler
     * .MessageDigestSampler] Ran test for 100000 samplings. Took 105 ms.
     * Expected sampling 40000.0, was 40078 2015-06-03 16:58:02,152 INFO
     * [com.eharmony
     * .services.configservice.sampler.MessageDigestSampler] Ran
     * test for 100000 samplings. Took 56 ms. Expected sampling 60000.0, was
     * 59972 2015-06-03 16:58:02,673 INFO
     * [com.eharmony.services.configservice
     * .sampler.MessageDigestSampler] Ran test for 1000000 samplings. Took 520
     * ms. Expected sampling 700000.0, was 700417 2015-06-03 16:58:02,696 INFO
     * [com
     * .eharmony.services.configservice.sampler.MessageDigestSampler
     * ] Ran 4 thread test for 100000 samplings. Took 22 ms. 2015-06-03
     * 16:58:02,901 INFO
     * [com.eharmony.services.configservice.sampler
     * .MessageDigestSampler] Ran 4 thread test for 1000000 samplings. Took 204
     * ms. 2015-06-03 16:58:04,091 INFO
     * [com.eharmony.services.configservice
     * .sampler.MessageDigestSampler] Ran 8 thread test for 5000000 samplings.
     * Took 1190 ms. 2015-06-03 16:58:18,589 INFO
     * [com.eharmony.services.configservice.sampler.MessageDigestSampler] Ran 8 thread test for
     * 60000000 samplings. Took 14497 ms. 2015-06-03 16:58:18,621 INFO
     * [com.eharmony
     * .services.configservice.sampler.GuavaMD5Sampler] Ran test
     * for 10000 samplings. Took 30 ms. Expected sampling 5000.0, was 5115
     * 2015-06-03 16:58:18,698 INFO
     * [com.eharmony.services.configservice
     * .sampler.GuavaMD5Sampler] Ran test for 100000 samplings. Took 77 ms.
     * Expected sampling 40000.0, was 40270 2015-06-03 16:58:18,760 INFO
     * [com.eharmony
     * .services.configservice.sampler.GuavaMD5Sampler] Ran test
     * for 100000 samplings. Took 61 ms. Expected sampling 60000.0, was 59977
     * 2015-06-03 16:58:19,326 INFO
     * [com.eharmony.services.configservice
     * .sampler.GuavaMD5Sampler] Ran test for 1000000 samplings. Took 565 ms.
     * Expected sampling 700000.0, was 700567 2015-06-03 16:58:19,353 INFO
     * [com.eharmony
     * .services.configservice.sampler.GuavaMD5Sampler] Ran 4
     * thread test for 100000 samplings. Took 27 ms. 2015-06-03 16:58:19,607
     * INFO
     * [com.eharmony.services.configservice.sampler.GuavaMD5Sampler
     * ] Ran 4 thread test for 1000000 samplings. Took 253 ms. 2015-06-03
     * 16:58:20,660 INFO
     * [com.eharmony.services.configservice.sampler
     * .GuavaMD5Sampler] Ran 8 thread test for 5000000 samplings. Took 1053 ms.
     * 2015-06-03 16:58:31,798 INFO
     * [com.eharmony.services.configservice
     * .sampler.GuavaMD5Sampler] Ran 8 thread test for 60000000 samplings. Took
     * 11138 ms. 2015-06-03 16:58:31,808 INFO
     * [com.eharmony.services.configservice
     * .sampler.MessageDigestSampler2] Ran test for 10000 samplings. Took 8 ms.
     * Expected sampling 5000.0, was 5013 2015-06-03 16:58:31,872 INFO
     * [com.eharmony
     * .services.configservice.sampler.MessageDigestSampler2] Ran
     * test for 100000 samplings. Took 63 ms. Expected sampling 40000.0, was
     * 40095 2015-06-03 16:58:31,931 INFO
     * [com.eharmony.services.configservice
     * .sampler.MessageDigestSampler2] Ran test for 100000 samplings. Took 58
     * ms. Expected sampling 60000.0, was 60023 2015-06-03 16:58:32,526 INFO
     * [com
     * .eharmony.services.configservice.sampler.MessageDigestSampler2
     * ] Ran test for 1000000 samplings. Took 595 ms. Expected sampling
     * 700000.0, was 700397 2015-06-03 16:58:32,556 INFO
     * [com.eharmony.services.configservice.sampler.MessageDigestSampler2] Ran 4 thread test
     * for 100000 samplings. Took 28 ms. 2015-06-03 16:58:32,830 INFO
     * [com.eharmony
     * .services.configservice.sampler.MessageDigestSampler2] Ran
     * 4 thread test for 1000000 samplings. Took 274 ms. 2015-06-03 16:58:33,862
     * INFO [com.eharmony.services.configservice.sampler.
     * MessageDigestSampler2] Ran 8 thread test for 5000000 samplings. Took 1031
     * ms. 2015-06-03 16:58:47,657 INFO
     * [com.eharmony.services.configservice
     * .sampler.MessageDigestSampler2] Ran 8 thread test for 60000000 samplings.
     * Took 13794 ms. 2015-06-03 16:58:47,788 INFO
     * [com.eharmony.services.configservice.sampler.BouncyCastleMD5Sampler] Ran test for 10000
     * samplings. Took 130 ms. Expected sampling 5000.0, was 4974 2015-06-03
     * 16:58:47,852 INFO
     * [com.eharmony.services.configservice.sampler
     * .BouncyCastleMD5Sampler] Ran test for 100000 samplings. Took 63 ms.
     * Expected sampling 40000.0, was 40078 2015-06-03 16:58:47,899 INFO
     * [com.eharmony
     * .services.configservice.sampler.BouncyCastleMD5Sampler]
     * Ran test for 100000 samplings. Took 46 ms. Expected sampling 60000.0, was
     * 59972 2015-06-03 16:58:48,360 INFO
     * [com.eharmony.services.configservice
     * .sampler.BouncyCastleMD5Sampler] Ran test for 1000000 samplings. Took 460
     * ms. Expected sampling 700000.0, was 700417 2015-06-03 16:58:48,376 INFO
     * [com.eharmony.services.configservice.sampler.
     * BouncyCastleMD5Sampler] Ran 4 thread test for 100000 samplings. Took 16
     * ms. 2015-06-03 16:58:48,528 INFO
     * [com.eharmony.services.configservice
     * .sampler.BouncyCastleMD5Sampler] Ran 4 thread test for 1000000 samplings.
     * Took 151 ms. 2015-06-03 16:58:49,003 INFO
     * [com.eharmony.services.configservice
     * .sampler.BouncyCastleMD5Sampler] Ran 8 thread test for 5000000 samplings.
     * Took 475 ms. 2015-06-03 16:58:55,530 INFO
     * [com.eharmony.services.configservice
     * .sampler.BouncyCastleMD5Sampler] Ran 8 thread test for 60000000
     * samplings. Took 6526 ms.
     */

    public static void main(String args[]) throws InterruptedException, NoSuchAlgorithmException,
            UnsupportedEncodingException {
        // performance test warmup
        SamplerPerformance test = new SamplerPerformance();
        RandomSampler s = new ModSampler(1000);
        test.runSuite(s);

        s = new MurmurHashSampler(1000);
        test.runSuite(s);

        s = new MessageDigestSampler("MD5", 1000);
        test.runSuite(s);

        s = new GuavaMD5Sampler(1000);
        test.runSuite(s);

        s = new MessageDigestSampler2("MD5", 1000);
        test.runSuite(s);

        s = new BouncyCastleMD5Sampler(1000);
        test.runSuite(s);

    }

}
