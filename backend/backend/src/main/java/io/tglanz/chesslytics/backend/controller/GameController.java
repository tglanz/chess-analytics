package io.tglanz.chesslytics.backend.controller;

import io.tglanz.chesslytics.backend.model.Game;
import io.tglanz.chesslytics.backend.repository.GameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
public class GameController {
  private final GameRepository gameRepository;

  public GameController(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }

  @GetMapping
  public Page<Game> listGames(Pageable pageable) {
    return gameRepository.findAll(pageable);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Game> getGame(@PathVariable Long id) {
    return gameRepository
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
