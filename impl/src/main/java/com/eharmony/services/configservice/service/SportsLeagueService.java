package com.eharmony.services.configservice.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eharmony.services.configservice.dao.SportsLeagueDao;
import com.eharmony.services.configservice.model.SportsLeagueDo;

@Service
public class SportsLeagueService {

	@Resource
	private SportsLeagueDao sportsLeagueDao;
	
	public List<SportsLeagueDo> getLeaguesBySport(String sport) {
		return sportsLeagueDao.getLeaguesBySport(sport);
	}
	
	public SportsLeagueDo saveLeague(SportsLeagueDo league) {
		return sportsLeagueDao.saveLeague(league);
	}
	
	public SportsLeagueDo getLeagueBySportAndId(String sport, int leagueId) {
		return sportsLeagueDao.getLeagueBySportAndId(sport, leagueId);
	}
}
