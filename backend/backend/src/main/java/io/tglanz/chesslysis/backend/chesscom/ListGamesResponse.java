package io.tglanz.chesslysis.backend.chesscom;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.tglanz.chesslysis.backend.chesscom.GameDTO;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListGamesResponse {
    public final List<GameDTO> games;

    @JsonCreator
    public ListGamesResponse(@JsonProperty("games") List<Map<String, Object>> gameObjects) {
        this.games = gameObjects.stream()
                .map(this::deserializeGame)
                .collect(Collectors.toList());
    }

    private GamePlayerDTO deserializeGamePlayer(Map<String, Object> object) {
        var dto = new GamePlayerDTO();
        dto.uuid = (String) object.get("uuid");
        dto.username = (String) object.get("username");
        dto.rating = (int) object.get("rating");
        dto.result = (String) object.get("result");
        return dto;
    }

    private GameDTO deserializeGame(Map<String, Object> object) {
        var dto = new GameDTO();
        dto.pgn = (String) object.get("pgn");
        dto.timeControl = (String) object.get("time_control");
        dto.endTime = Instant.ofEpochSecond((int)object.get("end_time"));
        dto.timeControl = (String) object.get("time_control");
        dto.timeClass = (String) object.get("time_class");
        dto.rated = (boolean) object.get("rated");
        dto.eco = (String) object.get("eco");

        dto.white = deserializeGamePlayer((Map<String, Object>) object.get("white"));
        dto.black = deserializeGamePlayer((Map<String, Object>) object.get("black"));

        return dto;
    }
}
