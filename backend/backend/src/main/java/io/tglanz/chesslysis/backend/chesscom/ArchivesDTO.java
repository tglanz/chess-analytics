package io.tglanz.chesslysis.backend.chesscom;

import java.util.List;
import java.util.stream.Collectors;

public class ArchivesDTO {
  public List<String> archives;

  public List<ArchiveInfo> getArchiveInfos() {
    return archives.stream()
        .map(ArchiveInfo::parseUrl)
        .collect(Collectors.toList());
  }
}
