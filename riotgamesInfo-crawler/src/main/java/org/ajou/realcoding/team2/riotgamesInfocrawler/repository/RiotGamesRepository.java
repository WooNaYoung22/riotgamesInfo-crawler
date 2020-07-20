package org.ajou.realcoding.team2.riotgamesInfocrawler.repository;

import org.ajou.realcoding.team2.riotgamesInfocrawler.api.RiotGamesOpenApiClient;
import org.ajou.realcoding.team2.riotgamesInfocrawler.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RiotGamesRepository {

    @Autowired
    private RiotGamesOpenApiClient riotGamesOpenApiClient;
    @Autowired
    private MongoTemplate summonerDtoDb;
    @Autowired
    private MongoTemplate leagueEntryDb;
    @Autowired
    private MongoTemplate matchListDtoDb;
    @Autowired
    private MongoTemplate matchDtoDb;
    @Autowired
    private MongoTemplate finalGameInformationDb;

    public void saveSummonerInfo(SummonerDto summoner){
        SummonerDto savedSummoner = summonerDtoDb.save(summoner);

    }
    public SummonerDto getSummonerInform(String summonerName) {
        Query query = Query.query(Criteria.where("_id").is(summonerName));
        SummonerDto summonerA = summonerDtoDb.findOne(query, SummonerDto.class);

        if(summonerA == null){
            SummonerDto summoner = riotGamesOpenApiClient.getSummonerInfo(summonerName);
            saveSummonerInfo(summoner);
            return summoner;
        }else{
            return summonerA;
        }
    }

    public void saveLeagueInfo(LeagueEntryDto league){

        LeagueEntryDto savedLeague = leagueEntryDb.save(league);
    }

    public LeagueEntryDto getLeagueInform(String summonerName){
        SummonerDto summoner = getSummonerInform(summonerName);
        Query query = Query.query(Criteria.where("_id").is(summoner.getId()));
        LeagueEntryDto leagueA = leagueEntryDb.findOne(query, LeagueEntryDto.class);
        if(leagueA == null){
            LeagueEntryDto league = riotGamesOpenApiClient.getLeagueInfo(summoner.getId());
            saveLeagueInfo(league);
            return league;
        } else {
            return leagueA;
        }
    }

    public void saveGameInfo(MatchListDto game){

        MatchListDto savedGame = matchListDtoDb.save(game);
    }

    public MatchListDto getGameInfo(String summonerName) {
        SummonerDto summoner = getSummonerInform(summonerName);
        Query query = Query.query(Criteria.where("_id").is(summoner.getAccountId()));
        MatchListDto gameA = matchListDtoDb.findOne(query, MatchListDto.class);
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
        //MatchDto matchDto = new MatchDto();//배열로 만들기
        List<MatchDto> matchDto = new ArrayList<>();



        Query query = Query.query(Criteria.where("_id").is(summonerName));
        FinalGameInformation finalGameInformation = finalGameInformationDb.findOne(query, FinalGameInformation.class);


        if(finalGameInformation == null){


            List<String> fiveGame = new ArrayList<>();
            int participantId =0 ;
            int championId =0;


            ArrayList<FinalGameInformation.GameDetail> gameDetailArrayList = new ArrayList<>();
            //FinalGameInformation.GameDetail gameDetail = new FinalGameInformation.GameDetail();
            FinalGameInformation saveFinalGameInformation = new FinalGameInformation();
            SummonerDto summoner = getSummonerInform(summonerName);
            //summonerNameDb.save(summoner);
            saveFinalGameInformation.setSummonerName(summoner.getName());
            LeagueEntryDto league = getLeagueInform(summonerName);
            //leagueDb.save(league);

            saveFinalGameInformation.setRank(league.getRank());
            saveFinalGameInformation.setTier(league.getTier());

            MatchListDto game = getGameInfo(summonerName);



            for(int i=0;i<5;i++) {
                //gameId 다섯개 추가
                fiveGame.add(game.getMatches().get(i).getGameId());
            }
            //log.info("MatchId {}", fiveGame);
            //게임 아이디로 조회   소환사명과 같은 이름이 있는 부분 찾기
            for(int g=0;g<5;g++) {
                matchDto.add(riotGamesOpenApiClient.getMatchDtoInfo(fiveGame.get(g)));
                matchDtoDb.save(matchDto.get(g));
                //log.info("matchId : {}", fiveGame.get(g));

                for (int k = 0; k < 10; k++) {

                    if (matchDto.get(g).getParticipantIdentities().get(k).getPlayer().getSummonerName().equals(summonerName)) {
                        FinalGameInformation.GameDetail gameDetail = new FinalGameInformation.GameDetail();

                        participantId = matchDto.get(g).getParticipantIdentities().get(k).getParticipantId();


                        championId = matchDto.get(g).getParticipants().get(participantId - 1).getChampionId();


                        gameDetail.setChampionId(championId);

                        gameDetail.setKills(matchDto.get(g).getParticipants().get(participantId - 1).getStats().getKills());
                        gameDetail.setAssists(matchDto.get(g).getParticipants().get(participantId - 1).getStats().getAssists());
                        gameDetail.setDeaths(matchDto.get(g).getParticipants().get(participantId - 1).getStats().getDeaths());
                        gameDetail.setWin(matchDto.get(g).getParticipants().get(participantId - 1).getStats().isWin());


                        gameDetailArrayList.add(gameDetail);

                        //log.info("gamedetailArray111 {}", gameDetailArrayList);
                        //log.info("gamedetail {}", gameDetail);
                        //finalGameInformationDb.save(saveFinalGameInformation);
                        // 전적 조회시 participantId 필요
                        //return saveFinalGameInformation;
                    }

                }
                //log.info("gamedetailArray222222 {}", gameDetailArrayList);

            }




            //log.info("gameDeatilArrayList : {}", gameDetailArrayList);
            saveFinalGameInformation.setGameInformation(gameDetailArrayList);
            finalGameInformationDb.save(saveFinalGameInformation);
            return saveFinalGameInformation;




        }
        return finalGameInformation;
    }


        return null;
    }
}
