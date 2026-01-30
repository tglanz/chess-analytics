package io.tglanz.chesslytics.backend.service;

import io.tglanz.chesslytics.backend.chesscom.ArchiveInfo;
import io.tglanz.chesslytics.backend.chesscom.ChessComClient;
import io.tglanz.chesslytics.backend.chesscom.GameDTO;
import io.tglanz.chesslytics.backend.chesscom.GamePlayerDTO;
import io.tglanz.chesslytics.backend.model.*;
import io.tglanz.chesslytics.backend.repository.AccountRepository;
import io.tglanz.chesslytics.backend.repository.GameRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChessComIngestionService {
  private static final Logger log = LoggerFactory.getLogger(ChessComIngestionService.class);

  private final ChessComClient chessComClient;
  private final AccountRepository accountRepository;
  private final GameRepository gameRepository;

  public ChessComIngestionService(
      ChessComClient chessComClient,
      AccountRepository accountRepository,
      GameRepository gameRepository) {
    this.chessComClient = chessComClient;
    this.accountRepository = accountRepository;
    this.gameRepository = gameRepository;
  }

  @Transactional
  public void ingest(IngestOptions options) {
    log.info(
        "Starting ingestion for username: {} (order: {}, limit: {})",
        options.username(),
        options.order(),
        options.limit() != null ? options.limit() : "unlimited");

    var archivesDTO = chessComClient.listArchives(options.username());
    List<ArchiveInfo> archives = new ArrayList<>(archivesDTO.getArchiveInfos());

    // Sort archives based on order
    if (options.order() == IngestOptions.Order.DESC) {
      Collections.reverse(archives);
    }

    log.info("Found {} archives for {}", archives.size(), options.username());

    int totalGamesIngested = 0;
    boolean limitReached = false;

    archiveLoop:
    for (var archiveInfo : archives) {
      log.debug("Processing archive: {}", archiveInfo.yearMonth);

      var gamesDTO = chessComClient.listGamesFromUrl(archiveInfo.url);
      List<GameDTO> games = new ArrayList<>(gamesDTO.games);

      // Reverse games within archive if DESC order (games in archive are already chronological)
      if (options.order() == IngestOptions.Order.DESC) {
        Collections.reverse(games);
      }

      for (var gameDTO : games) {
        // Check limit
        if (options.limit() != null && totalGamesIngested >= options.limit()) {
          log.info("Reached limit of {} games, stopping ingestion", options.limit());
          limitReached = true;
          break archiveLoop;
        }

        // Filter for standard chess only
        if (!"chess".equals(gameDTO.rules)) {
          continue;
        }

        try {
          saveGame(gameDTO);
          totalGamesIngested++;

          if (totalGamesIngested % 100 == 0) {
            log.info("Ingested {} games so far...", totalGamesIngested);
          }
        } catch (Exception e) {
          log.error("Failed to save game: {}", gameDTO.uuid, e);
        }
      }
    }

    log.info(
        "Completed ingestion for username: {} - Total games ingested: {}{}",
        options.username(),
        totalGamesIngested,
        limitReached ? " (limit reached)" : "");
  }

  private void saveGame(GameDTO gameDTO) {
    // Check if game already exists
    if (gameRepository.existsByExternalId(gameDTO.uuid)) {
      log.debug("Game {} already exists, skipping", gameDTO.uuid);
      return;
    }

    // Create or get accounts
    var whiteAccount = getOrCreateAccount(gameDTO.white);
    var blackAccount = getOrCreateAccount(gameDTO.black);

    // Create game
    var game = new Game(gameDTO.uuid, gameDTO.pgn, gameDTO.eco);

    // Create game players
    var whitePlayer =
        new GamePlayer(whiteAccount, game, Color.White, parseOutcome(gameDTO.white.result));

    var blackPlayer =
        new GamePlayer(blackAccount, game, Color.Black, parseOutcome(gameDTO.black.result));

    game.setPlayers(Arrays.asList(whitePlayer, blackPlayer));

    // Save game with players (cascade will persist players)
    gameRepository.save(game);

    log.debug("Saved game: {} vs {}", whiteAccount.getUsername(), blackAccount.getUsername());
  }

  private Account getOrCreateAccount(GamePlayerDTO playerDTO) {
    return accountRepository
        .findByAppAndUsername(ChessApp.CHESS_COM, playerDTO.username)
        .orElseGet(
            () -> {
              var account = new Account(ChessApp.CHESS_COM, playerDTO.uuid, playerDTO.username);
              return accountRepository.save(account);
            });
  }

  private GameOutcome parseOutcome(String result) {
    if (result == null) return GameOutcome.UNKNOWN;

    return switch (result.toLowerCase()) {
      case "win" -> GameOutcome.WIN;
      case "checkmated" -> GameOutcome.LOSS;
      case "resigned" -> GameOutcome.LOSS;
      case "timeout" -> GameOutcome.LOSS;
      case "agreed" -> GameOutcome.DRAW;
      case "stalemate" -> GameOutcome.DRAW;
      case "repetition" -> GameOutcome.DRAW;
      case "insufficient" -> GameOutcome.DRAW;
      case "50move" -> GameOutcome.DRAW;
      case "timevsinsufficient" -> GameOutcome.DRAW;
      default -> GameOutcome.UNKNOWN;
    };
  }
}
