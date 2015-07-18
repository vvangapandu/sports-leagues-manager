/*
 * This software is the confidential and proprietary information of
 * eharmony.com and may not be used, reproduced, modified, distributed,
 * publicly displayed or otherwise disclosed without the express written
 * consent of eharmony.com.
 *
 * This software is a work of authorship by eharmony.com and protected by
 * the copyright laws of the United States and foreign jurisdictions.
 *
 * Copyright 2000-2015 eharmony.com, Inc. All rights reserved.
 *
 */
package com.eharmony.services.configservice.monitoring;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.graphite.PickledGraphite;


/**
 * Drives the Graphite reporting instantiation.
 */
@Configuration public class GraphiteReportingConfiguration {

    private static final MetricRegistry registry = new MetricRegistry();

    /** Enables reporting. Defaults to <code>false</code>. */
    @Value("${graphite.enabled:false}")
    private Boolean enabled;

    @Value("${graphite.host}")
    private String host;
    private Logger log = LoggerFactory.getLogger(GraphiteReportingConfiguration.class);

    @Value("${graphite.periodInSeconds}")
    private Long period;

    @Value("${graphite.port}")
    private Integer port;

    @Value("${graphite.prefix}")
    private String prefix;

    public static MetricRegistry getRegistry() {

        return registry;

    }

    @PostConstruct public void init() {

        JmxReporter jmxReporter =
            JmxReporter.forRegistry(registry)
                       .convertDurationsTo(TimeUnit.MILLISECONDS)
                       .filter(MetricFilter.ALL)
                       .build();
        jmxReporter.start();

        if (!enabled) {

            log.info("Graphite reporting disabled, only enabling Jmx");
            return;

        }

        log.info("Graphite reporting enabled to {}:{} with prefix {} sending every {} {}",
                 new Object[] { host, port, prefix, period, TimeUnit.SECONDS });
        final PickledGraphite pickledGraphite = new PickledGraphite(new InetSocketAddress(host, port));
        final GraphiteReporter reporter =
            GraphiteReporter.forRegistry(registry)
                            .prefixedWith(prefix)
                            .convertRatesTo(TimeUnit.SECONDS)
                            .convertDurationsTo(TimeUnit.MILLISECONDS)
                            .filter(MetricFilter.ALL)
                            .build(pickledGraphite);
        reporter.start(period, TimeUnit.SECONDS);

    }

}
