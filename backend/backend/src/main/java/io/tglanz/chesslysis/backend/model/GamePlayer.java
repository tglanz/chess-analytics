package io.tglanz.chesslysis.backend.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class GamePlayer {

  @EmbeddedId private GamePlayerId id;

  @MapsId("gameId") // maps to id.gameId
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "game_id", nullable = false)
  private Game game;

  @MapsId("accountId") // maps to id.accountId
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;

  @Enumerated(EnumType.STRING)
  private GameOutcome outcome;

  public GamePlayer() {}

  public GamePlayer(Account account, Game game, Color color, GameOutcome outcome) {
    this.game = Objects.requireNonNull(game, "game must not be null");
    this.account = Objects.requireNonNull(account, "account must not be null");
    this.outcome = Objects.requireNonNull(outcome, "outcome must not be null");
    this.id = new GamePlayerId(game.getId(), account.getId(), color);
  }

  public Color getColor() {
    return id.getColor();
  }
}
