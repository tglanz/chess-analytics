package io.tglanz.chesslysis.backend.chesscom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameDTO {
    public String url;
    public String uuid;
    public String pgn;
    public String fen;

    @JsonProperty("time_control")
    public String timeControl;

    @JsonProperty("end_time")
    public Long endTime;

    public Boolean rated;

    @JsonProperty("time_class")
    public String timeClass;

    public String rules;
    public String eco;

    @JsonProperty("initial_setup")
    public String initialSetup;

    public Accuracies accuracies;

    public GamePlayerDTO white;
    public GamePlayerDTO black;

    public static class Accuracies {
        public Double white;
        public Double black;
    }
}
