package io.tglanz.chesslysis.backend.chesscom;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ChessComClient {
  private static final String BASE_URL = "https://api.chess.com/pub";
  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;

  public ChessComClient() {
    this.httpClient = HttpClient.newHttpClient();
    this.objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private <T> T get(String path, Class<T> responseType) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(BASE_URL + path))
        .GET()
        .build();

    try {
      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      return objectMapper.readValue(response.body(), responseType);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to fetch from " + path, e);
    }
  }

  private <T> T getFromUrl(String url, Class<T> responseType) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .GET()
        .build();

    try {
      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      return objectMapper.readValue(response.body(), responseType);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to fetch from " + url, e);
    }
  }

  // Player endpoints
  public PlayerProfileDTO getPlayerProfile(String username) {
    return get("/player/" + username, PlayerProfileDTO.class);
  }

  public PlayerStatsDTO getPlayerStats(String username) {
    return get("/player/" + username + "/stats", PlayerStatsDTO.class);
  }

  public PlayerClubsDTO getPlayerClubs(String username) {
    return get("/player/" + username + "/clubs", PlayerClubsDTO.class);
  }

  // Games endpoints
  public ArchivesDTO listArchives(String username) {
    return get("/player/" + username + "/games/archives", ArchivesDTO.class);
  }

  public GamesDTO listGames(String username, int year, int month) {
    return get(String.format("/player/%s/games/%d/%02d", username, year, month), GamesDTO.class);
  }

  public GamesDTO listGamesFromUrl(String archiveUrl) {
    return getFromUrl(archiveUrl, GamesDTO.class);
  }

  // Club endpoints
  public ClubDTO getClub(String clubUrlId) {
    return get("/club/" + clubUrlId, ClubDTO.class);
  }

  // Country endpoints
  public CountryDTO getCountry(String isoCode) {
    return get("/country/" + isoCode, CountryDTO.class);
  }

  // Leaderboards endpoint
  public LeaderboardsDTO getLeaderboards() {
    return get("/leaderboards", LeaderboardsDTO.class);
  }
}
