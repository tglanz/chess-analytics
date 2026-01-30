package io.tglanz.chesslytics.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Entity
public class Game {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull private String externalId;

  @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<GamePlayer> players;

  @NotNull private String pgn;
  @NotNull private String eco;

  public Game() {}

  public Game(String externalId, String pgn, String eco) {
    this.externalId = externalId;
    this.players = null;
    this.pgn = pgn;
    this.eco = eco;
  }

  public void setPlayers(List<GamePlayer> players) {
    this.players = Objects.requireNonNull(players, "players must not be null");

    if (players.size() != 2) {
      throw new IllegalArgumentException("Must provide exactly 2 players (black and white)");
    }

    this.getPlayer(Color.Black);
    this.getPlayer(Color.White);
  }

  public Long getId() {
    return id;
  }

  public String getPgn() {
    return pgn;
  }

  public String getEco() {
    return eco;
  }

  public String getExternalId() {
    return externalId;
  }

  public GamePlayer getPlayer(Color color) {
    return players.stream()
        .filter(p -> p.getColor() == color)
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalStateException(
                    String.format("Cannot find %s player for game id %s", color, id)));
  }
}
