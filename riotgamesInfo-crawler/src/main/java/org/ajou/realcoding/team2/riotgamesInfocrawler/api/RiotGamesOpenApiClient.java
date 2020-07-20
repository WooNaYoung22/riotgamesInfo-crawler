package org.ajou.realcoding.team2.riotgamesInfocrawler.api;

import org.ajou.realcoding.team2.riotgamesInfocrawler.domain.SummonerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RiotGamesOpenApiClient {
    @Autowired
    private RestTemplate restTemplate;

    private static final String SUMMONERINFO_REQUEST = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/{summonerName}?api_key=RGAPI-2c484922-2e11-4acc-a79d-a1cb78210959";

    public SummonerDto getSummonerInfo(String summonerName){
        SummonerDto summoner = restTemplate.getForObject(SUMMONERINFO_REQUEST, SummonerDto.class, summonerName);
        return summoner;
    }

}
