package io.tglanz.chesslysis.backend.examples;

import io.tglanz.chesslysis.backend.Repository;
import io.tglanz.chesslysis.backend.chesscom.ChessComClient;
import io.tglanz.chesslysis.backend.model.Account;
import io.tglanz.chesslysis.backend.model.ChessApp;
import io.tglanz.chesslysis.backend.utils.Reflection;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ChessComIngest {
  private static final Logger logger = LoggerFactory.getLogger(ChessComIngest.class);

  public static void main(String[] args) {
    logger.info("Configuring ORM");

    var username = "tglanz";
    if (args.length > 0) {
      username = args[0];
    }

    var configuration =
        new Configuration()
            .setProperty(AvailableSettings.JAKARTA_JDBC_URL, "jdbc:h2:mem:db1")
            .setProperty(AvailableSettings.JAKARTA_JDBC_USER, "sa")
            .setProperty(AvailableSettings.JAKARTA_JDBC_PASSWORD, "")
            .setProperty("hibernate.agroal.maxSize", 20)
            .setProperty(AvailableSettings.SHOW_SQL, false)
            .setProperty(AvailableSettings.FORMAT_SQL, true)
            .setProperty(AvailableSettings.HIGHLIGHT_SQL, true);

    Reflection.getAllEntitiesInSamePackage(Account.class).forEach(configuration::addAnnotatedClass);

    var sessionFactory = configuration.buildSessionFactory();
    sessionFactory.getSchemaManager().exportMappedObjects(true);

    var client = new ChessComClient();
    var repository = new Repository(sessionFactory);

    Map<String, Account> accountCache = new HashMap<>();

    var archivesDTO = client.listArchives(username);
    for (var archiveInfo : archivesDTO.getArchiveInfos()) {

    }


  }
}
