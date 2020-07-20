package org.ajou.realcoding.team2.riotgamesInfocrawler.api;

import org.ajou.realcoding.team2.riotgamesInfocrawler.domain.LeagueEntryDto;
import org.ajou.realcoding.team2.riotgamesInfocrawler.domain.SummonerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RiotGamesOpenApiClient {
    @Autowired
    private RestTemplate restTemplate;

    private static final String SUMMONERINFO_REQUEST = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/{summonerName}?api_key=RGAPI-2c484922-2e11-4acc-a79d-a1cb78210959";
    private static final String LEAGUEINFO_REQUEST = "https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/{summonerId}?api_key=RGAPI-042ddc06-283d-4f72-9891-7eb5f920cc58";


    public SummonerDto getSummonerInfo(String summonerName){
        SummonerDto summoner = restTemplate.getForObject(SUMMONERINFO_REQUEST, SummonerDto.class, summonerName);
        return summoner;
    }

    public LeagueEntryDto getLeagueInfo(String summonerId){
        LeagueEntryDto[] leagues = restTemplate.getForObject(LEAGUEINFO_REQUEST, LeagueEntryDto[].class, summonerId);
        for(LeagueEntryDto league : leagues) {
            if (league.getQueueType().equals("RANKED_SOLO_5x5")) return league;
        }
        return null;
    }

}
