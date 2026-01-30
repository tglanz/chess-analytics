package io.tglanz.chesslysis.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Account {

  @Id
  private String id;

  @NotNull
  @Enumerated(EnumType.STRING)
  private ChessApp app;

  @NotNull
  private String appId;

  @NotNull
  private String username;

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
}
