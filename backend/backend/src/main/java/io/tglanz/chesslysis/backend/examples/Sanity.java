package io.tglanz.chesslysis.backend.examples;

import io.tglanz.chesslysis.backend.chesscom.ChessComClient;
import io.tglanz.chesslysis.backend.chesscom.ListArchivesResponse;

public class Sanity {
  static void main(String[] args) {
    ChessComClient client = new ChessComClient("tglanz");
    ListArchivesResponse result = client.listArchives();

    for (ListArchivesResponse.ArchiveInfo archive : result.archives) {
      System.out.printf("%s%n", archive);
    }
  }
}
