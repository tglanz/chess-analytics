package io.tglanz.chesslytics.backend.chesscom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class PlayerStatsDTO {
  @JsonProperty("chess_daily")
  public GameTypeStats chessDaily;

  @JsonProperty("chess_rapid")
  public GameTypeStats chessRapid;

  @JsonProperty("chess_blitz")
  public GameTypeStats chessBlitz;

  @JsonProperty("chess_bullet")
  public GameTypeStats chessBullet;

  @JsonProperty("chess960_daily")
  public GameTypeStats chess960Daily;

  public Integer fide;
  public Map<String, Object> tactics;

  @JsonProperty("puzzle_rush")
  public Map<String, Object> puzzleRush;

  public static class GameTypeStats {
    public LastRating last;
    public BestRating best;
    public Record record;
  }

  public static class LastRating {
    public Integer rating;
    public Long date;
    public Integer rd;
  }

  public static class BestRating {
    public Integer rating;
    public Long date;
    public String game;
  }

  public static class Record {
    public Integer win;
    public Integer loss;
    public Integer draw;
  }
}
