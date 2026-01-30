package io.tglanz.chesslysis.backend.examples;

import io.tglanz.chesslysis.backend.chesscom.ArchivesDTO;
import io.tglanz.chesslysis.backend.chesscom.ChessComClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChessComAPI {
  private static final Logger logger = LoggerFactory.getLogger(ChessComAPI.class);

  public static void main(String[] args) {
    String username = "tglanz";
    ChessComClient client = new ChessComClient();

    logger.info("Listing archives for {}", username);
    ArchivesDTO archivesDTO = client.listArchives(username);

    // Iterate in reverse order (Java 11 compatible)
    var archives = archivesDTO.getArchiveInfos();
    for (int i = archives.size() - 1; i >= 0; i--) {
      var archiveInfo = archives.get(i);
      logger.info("Listing games of {}", archiveInfo);
      var gamesDTO = client.listGamesFromUrl(archiveInfo.url);

      for (var gameDTO : gamesDTO.games) {
        var me = gameDTO.white;
        var him = gameDTO.black;

        if (!me.username.equals(username)) {
          var temp = me;
          me = him;
          him = temp;
        }

        System.out.printf("i %s (%s) against %s %s%n", me.result, me.rating, him.username, him.rating);
      }
    }
  }
}
