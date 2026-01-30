package io.tglanz.chesslytics.backend.chesscom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GamePlayerDTO {
  public String uuid;
  public String username;
  public Integer rating;
  public String result;

  @JsonProperty("@id")
  public String id;
}
