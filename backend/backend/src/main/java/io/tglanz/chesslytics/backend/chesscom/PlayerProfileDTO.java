package io.tglanz.chesslytics.backend.chesscom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerProfileDTO {
  @JsonProperty("@id")
  public String id;

  @JsonProperty("player_id")
  public Long playerId;

  public String username;
  public String name;
  public String title;
  public String url;
  public String avatar;
  public String country;
  public String location;
  public Integer followers;
  public Long joined;

  @JsonProperty("last_online")
  public Long lastOnline;

  public String status;

  @JsonProperty("is_streamer")
  public Boolean isStreamer;

  public Boolean verified;
  public String league;
}
