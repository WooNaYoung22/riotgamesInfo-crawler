package org.ajou.realcoding.team2.riotgamesInfocrawler.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class MatchListDto {
    @Id
    private String accountId;
    private List<Matches> matches;
    @Data
    public static class Matches{
        private String gameId;
    }
}
