package com.nscube.services.leaguemanager.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.nscube.services.leaguemanager.model.SportsLeagueDo;
@Component
public class SportsLeagueDao {

	private Map<String, List<SportsLeagueDo>> sportLeagues = new HashMap<String, List<SportsLeagueDo>>();
	
	public SportsLeagueDo saveLeague(SportsLeagueDo league) {
		
		List<SportsLeagueDo> leagues = sportLeagues.get(league.getSport());
		if(CollectionUtils.isEmpty(leagues)) {
			leagues = new ArrayList<SportsLeagueDo>();
			sportLeagues.put(league.getSport(), leagues);
		}
		leagues.add(league);
		return league;
	}
	
	public List<SportsLeagueDo> getLeaguesBySport(String sport) {
		
		return sportLeagues.get(sport);
	}
	
	public SportsLeagueDo getLeagueBySportAndId(String sport, int leagueId) {
		
		List<SportsLeagueDo> leagues =  sportLeagues.get(sport);
		if(CollectionUtils.isEmpty(leagues)) {
			return null;
		}
		for(SportsLeagueDo league : leagues) {
			if(league.getLeagueId() == leagueId) {
				return league;
			}
		}
		return null;
	}
}
