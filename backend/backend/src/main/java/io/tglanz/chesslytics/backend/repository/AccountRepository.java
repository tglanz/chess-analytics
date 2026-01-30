package io.tglanz.chesslytics.backend.repository;

import io.tglanz.chesslytics.backend.model.Account;
import io.tglanz.chesslytics.backend.model.ChessApp;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Account} entities.
 *
 * <p>This interface extends {@link JpaRepository} to provide basic CRUD operations, and defines
 * custom query methods using Spring Data JPA's query derivation mechanism.
 *
 * <h2>Spring Data JPA Query Derivation</h2>
 *
 * <p>No implementation is required for query methods. Spring Data JPA automatically generates the
 * implementation at runtime by parsing method names and creating corresponding queries.
 *
 * <h3>How It Works</h3>
 *
 * <p>Method names follow this pattern: {@code [action]By[Field][Operator][Field]...}
 *
 * <p>Spring parses each part:
 *
 * <ul>
 *   <li><b>Action:</b> {@code findBy}, {@code existsBy}, {@code countBy}, {@code deleteBy}
 *   <li><b>Field:</b> Matches entity field names (case-sensitive): {@code App}, {@code AppId},
 *       {@code Username}
 *   <li><b>Operator:</b> {@code And}, {@code Or}, {@code OrderBy}, {@code Top}, {@code First}
 * </ul>
 *
 * <h3>Example Query Derivations</h3>
 *
 * <pre>
 * // Method: findByAppAndUsername(ChessApp app, String username)
 * // Generated SQL: SELECT * FROM account WHERE app = ? AND username = ?
 *
 * // Method: findByAppAndAppId(ChessApp app, String appId)
 * // Generated SQL: SELECT * FROM account WHERE app = ? AND app_id = ?
 *
 * // Method: existsByAppAndUsername(ChessApp app, String username)
 * // Generated SQL: SELECT COUNT(*) FROM account WHERE app = ? AND username = ?
 *
 * // Method: findByApp(ChessApp app)
 * // Generated SQL: SELECT * FROM account WHERE app = ?
 *
 * // Method: countByApp(ChessApp app)
 * // Generated SQL: SELECT COUNT(*) FROM account WHERE app = ?
 * </pre>
 *
 * <h3>Return Types</h3>
 *
 * <ul>
 *   <li>{@code Optional<Account>} - Single result (may be empty, recommended)
 *   <li>{@code Account} - Single result (throws exception if not found)
 *   <li>{@code List<Account>} - Multiple results (empty list if none found)
 *   <li>{@code boolean} - For {@code existsBy...} queries
 *   <li>{@code long}/{@code int} - For {@code countBy...} queries
 * </ul>
 *
 * <h3>Runtime Process</h3>
 *
 * <ol>
 *   <li>Spring parses the method name at application startup
 *   <li>Generates JPQL (Java Persistence Query Language) from the parsed components
 *   <li>Creates a proxy implementation that executes the query
 *   <li>Maps query results to the specified return type
 * </ol>
 *
 * <p>You can see the generated SQL in application logs when {@code spring.jpa.show-sql=true} or
 * {@code logging.level.org.hibernate.SQL=DEBUG} is configured.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
  /**
   * Finds an account by chess app and username.
   *
   * <p>Generated query: {@code SELECT * FROM account WHERE app = ? AND username = ?}
   *
   * @param app the chess platform
   * @param username the username on the platform
   * @return an Optional containing the account if found, empty otherwise
   */
  Optional<Account> findByAppAndUsername(ChessApp app, String username);
}
