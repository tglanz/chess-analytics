package io.tglanz.chesslysis.backend.chesscom;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ChessComClient {
  public static final String SCHEME = "https";
  public static final String HOSTNAME = "api.chess.com";

  private static URI createURI(String... route) {
    return URI.create(String.format("%s://%s/%s", SCHEME, HOSTNAME, String.join("/", route)));
  }

  public String getPlayerId(String username) {
    HttpRequest request =
            HttpRequest.newBuilder()
                    .uri(createURI("pub", "player", username))
                    .GET()
                    .build();

    try {
      HttpResponse<String> response =
              HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
      return response.body();
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public ListArchivesResponse listArchives(String username) {
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(createURI("pub", "player", username, "games", "archives"))
            .GET()
            .build();

    try {
      HttpResponse<String> response =
          HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(response.body(), ListArchivesResponse.class);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public ListGamesResponse listGames(ArchiveInfo archiveInfo) {
    return listGames(archiveInfo.url);
  }

  public ListGamesResponse listGames(String archiveUrl) {
    HttpRequest request =
            HttpRequest.newBuilder()
                    .uri(URI.create(archiveUrl))
                    .GET()
                    .build();

    try {
      HttpResponse<String> response =
              HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(response.body(), ListGamesResponse.class);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
