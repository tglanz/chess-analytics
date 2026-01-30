package io.tglanz.chesslysis.backend.examples;

import io.tglanz.chesslysis.backend.chesscom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChessComEndpointsDemo {
  private static final Logger logger = LoggerFactory.getLogger(ChessComEndpointsDemo.class);

  public static void main(String[] args) {
    String username = args.length > 0 ? args[0] : "tglanz";
    ChessComClient client = new ChessComClient();

    logger.info("=== Chess.com API Endpoints Demo ===");
    logger.info("Testing with username: {}", username);

    // 1. Get Player Profile
    logger.info("\n--- Player Profile ---");
    try {
      PlayerProfileDTO profile = client.getPlayerProfile(username);
      logger.info("Username: {}", profile.username);
      logger.info("Name: {}", profile.name);
      logger.info("Title: {}", profile.title);
      logger.info("Status: {}", profile.status);
      logger.info("Joined: {}", profile.joined);
      logger.info("Country: {}", profile.country);
      logger.info("Verified: {}", profile.verified);
    } catch (Exception e) {
      logger.error("Failed to get player profile", e);
    }

    // 2. Get Player Stats
    logger.info("\n--- Player Stats ---");
    try {
      PlayerStatsDTO stats = client.getPlayerStats(username);
      if (stats.chessRapid != null && stats.chessRapid.last != null) {
        logger.info("Rapid Rating: {}", stats.chessRapid.last.rating);
      }
      if (stats.chessBlitz != null && stats.chessBlitz.last != null) {
        logger.info("Blitz Rating: {}", stats.chessBlitz.last.rating);
      }
      if (stats.chessBullet != null && stats.chessBullet.last != null) {
        logger.info("Bullet Rating: {}", stats.chessBullet.last.rating);
      }
      if (stats.chessDaily != null && stats.chessDaily.last != null) {
        logger.info("Daily Rating: {}", stats.chessDaily.last.rating);
      }
    } catch (Exception e) {
      logger.error("Failed to get player stats", e);
    }

    // 3. Get Player Clubs
    logger.info("\n--- Player Clubs ---");
    try {
      PlayerClubsDTO clubs = client.getPlayerClubs(username);
      logger.info("Member of {} clubs", clubs.clubs != null ? clubs.clubs.size() : 0);
      if (clubs.clubs != null && !clubs.clubs.isEmpty()) {
        logger.info("First club: {}", clubs.clubs.get(0).name);
      }
    } catch (Exception e) {
      logger.error("Failed to get player clubs", e);
    }

    // 4. List Game Archives
    logger.info("\n--- Game Archives ---");
    try {
      ArchivesDTO archivesDTO = client.listArchives(username);
      var archiveInfos = archivesDTO.getArchiveInfos();
      logger.info("Total archives: {}", archiveInfos.size());
      if (!archiveInfos.isEmpty()) {
        var latestArchive = archiveInfos.get(archiveInfos.size() - 1);
        logger.info("Latest archive: {}", latestArchive.yearMonth);

        // 5. Get games from latest archive (standard chess only)
        logger.info("\n--- Latest Games (Standard Chess Only) ---");
        GamesDTO gamesDTO = client.listGamesFromUrl(latestArchive.url);

        int standardChessCount = 0;
        int variantGamesCount = 0;

        for (var game : gamesDTO.games) {
          // Filter for standard chess only (exclude chess960 and other variants)
          if (!"chess".equals(game.rules)) {
            variantGamesCount++;
            continue;
          }

          standardChessCount++;

          // Show first 5 standard chess games
          if (standardChessCount <= 5) {
            var opponent = game.white.username.equals(username) ? game.black : game.white;
            var player = game.white.username.equals(username) ? game.white : game.black;

            logger.info("Game {}: {} ({}) vs {} ({}) - Result: {}",
                standardChessCount,
                username,
                player.rating,
                opponent.username,
                opponent.rating,
                player.result);
            logger.info("  Rules: {}, Time Control: {}, Time Class: {}, Rated: {}, ECO: {}",
                game.rules,
                game.timeControl,
                game.timeClass,
                game.rated,
                game.eco);
          }
        }

        logger.info("\nTotal games in archive: {}", gamesDTO.games.size());
        logger.info("Standard chess games: {}", standardChessCount);
        logger.info("Variant games (filtered out): {}", variantGamesCount);
      }
    } catch (Exception e) {
      logger.error("Failed to get game archives", e);
    }

    // 6. Get Leaderboards
    logger.info("\n--- Leaderboards (Top 3 per category) ---");
    try {
      LeaderboardsDTO leaderboards = client.getLeaderboards();

      if (leaderboards.liveRapid != null && !leaderboards.liveRapid.isEmpty()) {
        logger.info("Rapid Top 3:");
        for (int i = 0; i < Math.min(3, leaderboards.liveRapid.size()); i++) {
          var entry = leaderboards.liveRapid.get(i);
          logger.info("  {}. {} - {} ({})", entry.rank, entry.username, entry.score, entry.title);
        }
      }

      if (leaderboards.liveBlitz != null && !leaderboards.liveBlitz.isEmpty()) {
        logger.info("Blitz Top 3:");
        for (int i = 0; i < Math.min(3, leaderboards.liveBlitz.size()); i++) {
          var entry = leaderboards.liveBlitz.get(i);
          logger.info("  {}. {} - {} ({})", entry.rank, entry.username, entry.score, entry.title);
        }
      }
    } catch (Exception e) {
      logger.error("Failed to get leaderboards", e);
    }

    logger.info("\n=== Demo Complete ===");
  }
}
