package com.eharmony.services.configservice.sampler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.eharmony.services.configservice.sampler.BouncyCastleMD5Sampler;
import com.eharmony.services.configservice.sampler.ModSampler;

public class CSVGenerator {

    public void runSingleThread(ModSampler s, int start, int end, String name) throws Exception {
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        File f = new File(name + "." + format.format(d) + ".csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(f));
        try  {
            for (int i = start; i < end; i++) {
                String userString = Integer.toString(i);
                int bucket = s.convertToBucket(name, userString);
                writer.write(userString);
                writer.write(',');
                writer.write(Integer.toString(bucket));
                writer.write(System.getProperty("line.separator"));
            }
        } finally {
            writer.close();
        }
    }

    public static void main(String args[]) throws InterruptedException, NoSuchAlgorithmException,
            UnsupportedEncodingException, Exception {
        if (args.length != 1) {
            System.err.println("This generates all of the buckets from 1 to 100 million for the md5 sampler");
            System.err.println("Usage: CSVGenerator {property.name.random}");
            System.exit(1);
        }
        // performance test warmup
        CSVGenerator gen = new CSVGenerator();
//      ModSampler s = new ModSampler(1);
//      gen.runSingleThread(s, 1, 100000000, "bool");
//        ModSampler s = new ModSampler(100);
//        gen.runSingleThread(s, 1, 100000000, "mod");

        ModSampler s = new BouncyCastleMD5Sampler(1000);
        gen.runSingleThread(s, 1, 100000000, args[0]);

    }

}
