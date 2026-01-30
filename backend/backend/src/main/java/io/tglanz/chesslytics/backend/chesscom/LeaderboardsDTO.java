package io.tglanz.chesslytics.backend.chesscom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class LeaderboardsDTO {
  public List<LeaderboardEntry> daily;
  public List<LeaderboardEntry> daily960;

  @JsonProperty("live_rapid")
  public List<LeaderboardEntry> liveRapid;

  @JsonProperty("live_blitz")
  public List<LeaderboardEntry> liveBlitz;

  @JsonProperty("live_bullet")
  public List<LeaderboardEntry> liveBullet;

  public static class LeaderboardEntry {
    @JsonProperty("player_id")
    public Long playerId;

    public String username;
    public String name;
    public String title;
    public String url;
    public String country;
    public String status;
    public String avatar;
    public Integer score;
    public Integer rank;

    @JsonProperty("win_count")
    public Integer winCount;

    @JsonProperty("loss_count")
    public Integer lossCount;

    @JsonProperty("draw_count")
    public Integer drawCount;

    @JsonProperty("trend_score")
    public Trend trendScore;

    @JsonProperty("trend_rank")
    public Trend trendRank;
  }

  public static class Trend {
    public Integer direction;
    public Integer delta;
  }
}
