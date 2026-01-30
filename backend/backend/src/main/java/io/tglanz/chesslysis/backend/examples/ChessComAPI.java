package io.tglanz.chesslysis.backend.examples;

import io.tglanz.chesslysis.backend.chesscom.ChessComClient;
import io.tglanz.chesslysis.backend.chesscom.ListArchivesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChessComAPI {
  private static final Logger logger = LoggerFactory.getLogger(ChessComAPI.class);

  public static void main(String[] args) {
    ChessComClient client = new ChessComClient("tglanz");

    logger.info("Listing archives");
    ListArchivesResponse listArchivesResponse = client.listArchives();

    for (var archiveInfo : listArchivesResponse.archives.reversed()) {
      logger.info("Listing games of {}", archiveInfo);
      var listGamesResponse = client.listGames(archiveInfo);

      for (var gameDTO : listGamesResponse.games) {
        var me = gameDTO.white;
        var him = gameDTO.black;

        if (!me.username.equals("tglanz")) {
          var temp = me;
          me = him;
          him = temp;
        }

        System.out.printf("i %s (%s) against %s %s%n", me.result, me.rating, him.username, him.rating);
      }
    }
  }
}
