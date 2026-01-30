package io.tglanz.chesslytics.backend.repository;

import io.tglanz.chesslytics.backend.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Game} entities.
 *
 * <p>Provides methods to check for existing games based on external identifiers to prevent
 * duplicate ingestion.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

  /**
   * Checks if a game already exists by external ID.
   *
   * <p>Generated query: {@code SELECT COUNT(*) FROM game WHERE external_id = ?}
   *
   * @param externalId the external game ID from the platform (Chess.com UUID, Lichess ID, etc.)
   * @return true if the game exists, false otherwise
   */
  boolean existsByExternalId(String externalId);
}
