package org.ajou.realcoding.team2.riotgamesInfocrawler.controller;

import org.ajou.realcoding.team2.riotgamesInfocrawler.domain.FinalGameInformation;
import org.ajou.realcoding.team2.riotgamesInfocrawler.service.RiotGamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RiotGamesController {

    @Autowired
    private RiotGamesService riotGamesService;

    @GetMapping("/riotGamesInfo-game/{summonerName}")
    public FinalGameInformation getGameDetailInfo(@PathVariable String summonerName){
        return riotGamesService.getFinalGameInformation(summonerName);
    }
}

