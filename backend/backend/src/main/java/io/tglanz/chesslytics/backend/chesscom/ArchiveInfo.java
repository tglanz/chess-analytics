package io.tglanz.chesslytics.backend.chesscom;

import java.time.YearMonth;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArchiveInfo {
  private static final Pattern YEAR_MONTH_PATTERN = Pattern.compile(".*/(\\d{4})/(\\d{1,2})$");

  public final YearMonth yearMonth;
  public final String url;

  public ArchiveInfo(YearMonth yearMonth, String url) {
    this.yearMonth = yearMonth;
    this.url = url;
  }

  public static ArchiveInfo parseUrl(String url) {
    Matcher matcher = YEAR_MONTH_PATTERN.matcher(url);
    if (!matcher.matches()) {
      throw new RuntimeException(String.format("Unable to parse: %s", url));
    }

    int year = Integer.parseInt(matcher.group(1));
    int month = Integer.parseInt(matcher.group(2));

    return new ArchiveInfo(YearMonth.of(year, month), url);
  }

  @Override
  public String toString() {
    return String.format("%s - %s", yearMonth, url);
  }
}
