package io.tglanz.chesslysis.backend.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GamePlayerId implements Serializable {

  private Long gameId;
  private Long accountId;

  @Enumerated(EnumType.STRING)
  private Color color;

  public GamePlayerId() {}

  public GamePlayerId(Long gameId, Long accountId, Color color) {
    this.gameId = gameId;
    this.accountId = accountId;
    this.color = color;
  }

  // Getters and setters
  public Long getGameId() {
    return gameId;
  }

  public void setGameId(Long gameId) {
    this.gameId = gameId;
  }

  public Long getAccountId() {
    return accountId;
  }

  public void setAccountId(Long accountId) {
    this.accountId = accountId;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GamePlayerId)) return false;
    GamePlayerId that = (GamePlayerId) o;
    return Objects.equals(gameId, that.gameId)
        && Objects.equals(accountId, that.accountId)
        && color == that.color;
  }

  @Override
  public int hashCode() {
    return Objects.hash(gameId, accountId, color);
  }
}
