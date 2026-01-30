package io.tglanz.chesslytics.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a player account from a chess platform (Chess.com, Lichess, etc.).
 *
 * <p>Account uses a composite ID generated from the app name and external app ID:
 *
 * <pre>
 * id = "{APP_NAME}-{APP_ID}"
 * Example: "CHESS_COM-182371"
 * </pre>
 */
@Entity
public class Account {

  @Id private String id;

  @NotNull
  @Enumerated(EnumType.STRING)
  private ChessApp app;

  @NotNull private String appId;

  @NotNull private String username;

  public Account() {}

  public Account(ChessApp app, String appId, String username) {
    this.id = conventionalId(app, appId);
    this.app = app;
    this.appId = appId;
    this.username = username;
  }

  public static String conventionalId(ChessApp app, String appId) {
    return app.name() + "-" + appId;
  }

  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public ChessApp getApp() {
    return app;
  }

  public String getAppId() {
    return appId;
  }
}
