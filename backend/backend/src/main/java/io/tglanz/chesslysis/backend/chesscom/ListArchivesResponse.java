package io.tglanz.chesslysis.backend.chesscom;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.YearMonth;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ListArchivesResponse {
  public final List<ArchiveInfo> archives;

  @JsonCreator
  public ListArchivesResponse(@JsonProperty("archives") List<String> archiveStrings) {
    archives = archiveStrings.stream().map(ArchiveInfo::parseUrl).collect(Collectors.toList());
  }
}
