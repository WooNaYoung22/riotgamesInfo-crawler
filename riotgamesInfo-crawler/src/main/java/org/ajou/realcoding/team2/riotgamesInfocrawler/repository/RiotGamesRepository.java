package org.ajou.realcoding.team2.riotgamesInfocrawler.repository;

import org.ajou.realcoding.team2.riotgamesInfocrawler.api.RiotGamesOpenApiClient;
import org.ajou.realcoding.team2.riotgamesInfocrawler.domain.FinalGameInformation;
import org.ajou.realcoding.team2.riotgamesInfocrawler.domain.LeagueEntryDto;
import org.ajou.realcoding.team2.riotgamesInfocrawler.domain.MatchListDto;
import org.ajou.realcoding.team2.riotgamesInfocrawler.domain.SummonerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class RiotGamesRepository {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RiotGamesOpenApiClient riotGamesOpenApiClient;

    public void saveSummonerInfo(SummonerDto summoner){
        SummonerDto savedSummoner = mongoTemplate.save(summoner);

    }
    public SummonerDto getSummonerInform(String summonerName) {
        Query query = Query.query(Criteria.where("_id").is(summonerName));
        SummonerDto summonerA = mongoTemplate.findOne(query, SummonerDto.class);

        if(summonerA == null){
            SummonerDto summoner = riotGamesOpenApiClient.getSummonerInfo(summonerName);
            saveSummonerInfo(summoner);
            return summoner;
        }else{
            return summonerA;
        }
    }

    public void saveLeagueInfo(LeagueEntryDto league){

        LeagueEntryDto savedLeague = mongoTemplate.save(league);
    }

    public LeagueEntryDto getLeagueInform(String summonerName){
        SummonerDto summoner = getSummonerInform(summonerName);
        Query query = Query.query(Criteria.where("_id").is(summoner.getId()));
        LeagueEntryDto leagueA = mongoTemplate.findOne(query, LeagueEntryDto.class);
        if(leagueA == null){
            LeagueEntryDto league = riotGamesOpenApiClient.getLeagueInfo(summoner.getId());
            saveLeagueInfo(league);
            return league;
        } else {
            return leagueA;
        }
    }

    public void saveGameInfo(MatchListDto game){
        MatchListDto savedGame = mongoTemplate.save(game);
    }

    public MatchListDto getGameInfo(String summonerName) {
        SummonerDto summoner = getSummonerInform(summonerName);
        Query query = Query.query(Criteria.where("_id").is(summoner.getAccountId()));
        MatchListDto gameA = mongoTemplate.findOne(query, MatchListDto.class);
        if(gameA == null){
            MatchListDto game = riotGamesOpenApiClient.getGameInfo(summoner.getAccountId());
            game.setAccountId(summoner.getAccountId());
            saveGameInfo(game);
            return game;
        }
        else{
            return gameA;
        }
    }


    public FinalGameInformation findFinalGameInformation(String summonerName) {
        return null;
    }
}
