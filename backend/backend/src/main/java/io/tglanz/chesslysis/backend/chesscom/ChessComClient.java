package io.tglanz.chesslysis.backend.chesscom;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ChessComClient {
  public final String username;

  public static final String SCHEME = "https";
  public static final String HOSTNAME = "api.chess.com";

  public ChessComClient(String username) {
    this.username = username;
  }

  private static URI createURI(String... route) {
    return URI.create(String.format("%s://%s/%s", SCHEME, HOSTNAME, String.join("/", route)));
  }

  public ListArchivesResponse listArchives() {
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
}
