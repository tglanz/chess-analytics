package io.tglanz.chesslytics.backend.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main application for REST API server.
 *
 * <p>Serves queries and analytics endpoints. Does NOT perform data ingestion.
 */
@SpringBootApplication
@ComponentScan(
    basePackages = {
      "io.tglanz.chesslytics.backend.controller",
      "io.tglanz.chesslytics.backend.repository",
      "io.tglanz.chesslytics.backend.service",
      "io.tglanz.chesslytics.backend.chesscom"
    })
@EnableJpaRepositories(basePackages = "io.tglanz.chesslytics.backend.repository")
@EntityScan(basePackages = "io.tglanz.chesslytics.backend.model")
public class ChesslyticsApiApplication {
  public static void main(String[] args) {
    SpringApplication.run(ChesslyticsApiApplication.class, args);
  }
}
