package io.tglanz.chesslytics.backend.app;

import io.tglanz.chesslytics.backend.service.ChessComIngestionService;
import io.tglanz.chesslytics.backend.service.IngestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Application for data ingestion from Chess.com/Lichess.
 *
 * <p>Runs as a batch job, ingests data, then exits. Should be scheduled externally (cron, K8s
 * CronJob, etc.)
 *
 * <p>Usage examples:
 *
 * <pre>
 * # Ingest latest 100 games
 * ./gradlew :backend:ingest -Pusername=hikaru -Plimit=100
 *
 * # Ingest all games from oldest to newest
 * ./gradlew :backend:ingest -Pusername=hikaru -Porder=asc
 *
 * # Ingest latest 500 games (newest first)
 * ./gradlew :backend:ingest -Pusername=magnuscarlsen -Porder=desc -Plimit=500
 * </pre>
 */
@SpringBootApplication
@ComponentScan(
    basePackages = {
      "io.tglanz.chesslytics.backend.repository",
      "io.tglanz.chesslytics.backend.service",
      "io.tglanz.chesslytics.backend.chesscom"
    })
@EnableJpaRepositories(basePackages = "io.tglanz.chesslytics.backend.repository")
@EntityScan(basePackages = "io.tglanz.chesslytics.backend.model")
public class ChesslyticsIngestApplication implements CommandLineRunner {
  private static final Logger log = LoggerFactory.getLogger(ChesslyticsIngestApplication.class);

  private final ChessComIngestionService ingestionService;

  public ChesslyticsIngestApplication(ChessComIngestionService ingestionService) {
    this.ingestionService = ingestionService;
  }

  public static void main(String[] args) {
    System.exit(
        SpringApplication.exit(SpringApplication.run(ChesslyticsIngestApplication.class, args)));
  }

  private IngestOptions parseArgs(String... args) {
    if (args.length == 0) {
      log.error("Usage: --username=<username> [--order=asc|desc] [--limit=<number>]");
      throw new IllegalArgumentException("Username is required");
    }

    IngestOptions.Builder builder = IngestOptions.builder();

    // Parse arguments
    for (String arg : args) {
      if (arg.startsWith("--username=")) {
        builder.username(arg.substring("--username=".length()));
      } else if (arg.startsWith("--order=")) {
        builder.order(arg.substring("--order=".length()));
      } else if (arg.startsWith("--limit=")) {
        try {
          builder.limit(Integer.parseInt(arg.substring("--limit=".length())));
        } catch (NumberFormatException e) {
          log.error("Invalid limit value: {}", arg);
          throw new IllegalArgumentException("Limit must be a number");
        }
      }
    }

    return builder.build();
  }

  @Override
  public void run(String... args) {
    var options = parseArgs(args);

    log.info("Starting Chess.com ingestion with options: {}", options);
    ingestionService.ingest(options);
    log.info("Ingestion completed successfully");
  }
}
