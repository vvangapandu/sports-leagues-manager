package com.eharmony.services.configservice.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.ws.rs.container.AsyncResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.eharmony.services.configservice.dto.ConfigurationFeaturesDto;
import com.eharmony.services.configservice.service.ConfigurationService;
import com.eharmony.services.configservice.service.ExecutorServiceProvider;

@Component
public class ConfigurationFeaturesRXHandler {

	private static final Logger log = LoggerFactory.getLogger(ConfigurationFeaturesRXHandler.class);
	
	@Resource
    private ExecutorServiceProvider executorServiceProvider;
	
	@Autowired
    private AsyncRequestHandler asyncRequestHandler;
   
    @Autowired
    private ConfigurationService configurationService;
	
	public void handleSampledFeaturesForUserAndLocale(final Integer userId, final Locale locale,
			final List<String> properties, final AsyncResponse asyncResponse) {

		List<String> disabledFeaturesList = new ArrayList<String>(properties.size());
		List<String> enabledFeaturesList = new ArrayList<String>(properties.size());

		Observable
				.from(properties)
				.flatMap(
						item -> {
							return Observable.defer(
									() -> {
										return Observable.just(new Object[] { item,
												configurationService.isFeatureSampledForUser(userId, locale, item) });
									}).subscribeOn(Schedulers.from(executorServiceProvider.getTaskExecutor()));
						})
				.subscribe(
						p -> {
							if ((Boolean) p[1]) {
								enabledFeaturesList.add((String) p[0]);
							} else {
								disabledFeaturesList.add((String) p[0]);
							}
						},
						e -> {
							log.error("Exception while fething property for locale {}", locale, e);
							asyncRequestHandler.handleException(asyncResponse, e);
						},
						() -> {
							int totalEvaluatedProperties = enabledFeaturesList.size() + disabledFeaturesList.size();
							if (properties.size() != totalEvaluatedProperties) {
								log.warn("requested props list count {} is not same as evaluated properties count {}",
										properties.size(), totalEvaluatedProperties);
							}

							asyncResponse.resume(asyncRequestHandler.getResponse(new ConfigurationFeaturesDto(userId,
									locale.toString(), enabledFeaturesList, disabledFeaturesList)));
						});

	}
	
}
