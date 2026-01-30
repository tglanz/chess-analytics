package io.tglanz.chesslytics.backend.chesscom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class PlayerClubsDTO {
  public List<ClubEntry> clubs;

  public static class ClubEntry {
    @JsonProperty("@id")
    public String id;

    public String name;
    public String url;
    public String icon;
    public Long joined;

    @JsonProperty("last_activity")
    public Long lastActivity;
  }
}
