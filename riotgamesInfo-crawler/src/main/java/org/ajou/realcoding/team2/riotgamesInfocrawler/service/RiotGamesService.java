package org.ajou.realcoding.team2.riotgamesInfocrawler.service;

import org.ajou.realcoding.team2.riotgamesInfocrawler.domain.*;
import org.ajou.realcoding.team2.riotgamesInfocrawler.api.RiotGamesOpenApiClient;
import org.ajou.realcoding.team2.riotgamesInfocrawler.domain.FinalGameInformation;
import org.ajou.realcoding.team2.riotgamesInfocrawler.repository.RiotGamesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RiotGamesService {
    @Autowired
    private RiotGamesRepository riotGamesRepository;
    @Autowired
    private RiotGamesOpenApiClient riotGamesOpenApiClient;


    public FinalGameInformation getFinalGameInformation(String summonerName) {

        return riotGamesRepository.findFinalGameInformation(summonerName);
    }

}
