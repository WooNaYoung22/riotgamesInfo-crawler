package org.ajou.realcoding.team2.riotgamesInfocrawler.repository;

import org.ajou.realcoding.team2.riotgamesInfocrawler.api.RiotGamesOpenApiClient;
import org.ajou.realcoding.team2.riotgamesInfocrawler.domain.FinalGameInformation;
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

    public FinalGameInformation findFinalGameInformation(String summonerName) {
        return null;
    }
}
