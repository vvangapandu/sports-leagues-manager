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
package com.eharmony.services.configservice.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eharmony.services.configservice.model.SportsLeagueDo;
import com.eharmony.services.configservice.service.SportsLeagueService;

@Component
@Path("/v1")
public class LeagueResource {

    private static final Logger log = LoggerFactory.getLogger(LeagueResource.class);

    @Autowired
    private AsyncRequestHandler asyncRequestHandler;
    
    @Autowired
    private SportsLeagueService sportsLeagueService;
   
    public LeagueResource() {
        log.info("initialized LeagueResource...");
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("sports/{sport}/leagues")
    @Produces("application/json; charset=utf-8")
    public void getLeaguesForSport(@PathParam("sport") final String sport, 
    		@Suspended final AsyncResponse asyncResponse) {

        try {
            log.debug("Retrieving Leagues for sport {} ", sport);

            asyncResponse.resume(asyncRequestHandler.getResponse(sportsLeagueService.getLeaguesBySport(sport)));

        } catch (Exception ex) {
            log.error("Exception while fetching the leagues for sport {} ", sport, ex);
            asyncRequestHandler.handleException(asyncResponse, ex);
        }

    }
    
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("sports/{sport}/leagues/{leagueId}")
    @Produces("application/json; charset=utf-8")
    public void getLeaguesForSportByLeagueId(@PathParam("sport") final String sport, @PathParam("leagueId") final int leagueId,
    		@Suspended final AsyncResponse asyncResponse) {

        try {
            log.debug("Retrieving Leagues for sport {} and leaguId {}", sport, leagueId);

            asyncResponse.resume(asyncRequestHandler.getResponse(sportsLeagueService.getLeagueBySportAndId(sport, leagueId)));

        } catch (Exception ex) {
            log.error("Exception while fetching the league for sport {} and leagueId {}", sport, leagueId, ex);
            asyncRequestHandler.handleException(asyncResponse, ex);
        }

    }
    
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    @Path("sports/{sport}/leagues")
    @Produces("application/json; charset=utf-8")
    public void saveLeague(@PathParam("sport") final String sport,
            SportsLeagueDo leagueDo, @Suspended final AsyncResponse asyncResponse) {

        try {

            log.debug("saving league object for sport {}", sport);

            asyncResponse.resume(asyncRequestHandler.getResponse(sportsLeagueService.saveLeague(leagueDo)));
            
        } catch (Exception ex) {
            log.error("Exception while saving league for sport {}", sport, ex);
            asyncRequestHandler.handleException(asyncResponse, ex);
        }

    }
    
}
