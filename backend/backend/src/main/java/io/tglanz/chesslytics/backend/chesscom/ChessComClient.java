package io.tglanz.chesslytics.backend.chesscom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ChessComClient {
  private final RestClient restClient;
  private final String baseUrl;

  public ChessComClient(
      RestClient.Builder restClientBuilder,
      @Value("${chesslytics.chesscom.base-url}") String baseUrl) {
    this.baseUrl = baseUrl;
    this.restClient =
        restClientBuilder.baseUrl(baseUrl).defaultHeader("User-Agent", "Chesslytics/1.0").build();
  }

  private <T> T get(String path, Class<T> responseType) {
    return restClient
        .get()
        .uri(path)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .body(responseType);
  }

  private <T> T getFromUrl(String url, Class<T> responseType) {
    return RestClient.builder()
        .defaultHeader("User-Agent", "Chesslytics/1.0")
        .build()
        .get()
        .uri(url)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .body(responseType);
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
