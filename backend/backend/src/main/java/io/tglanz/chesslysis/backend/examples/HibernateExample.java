package io.tglanz.chesslysis.backend.examples;

import io.tglanz.chesslysis.backend.model.Book;
import io.tglanz.chesslysis.backend.utils.Reflection;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

public class HibernateExample {
  public static void main(String[] args) {
    var packageName = Book.class.getPackageName();
    System.out.printf("Package name: %s%n", packageName);

    var configuration = new Configuration()
              .addPackage(packageName)
              .setProperty(AvailableSettings.JAKARTA_JDBC_URL, "jdbc:h2:mem:db1")
              .setProperty(AvailableSettings.JAKARTA_JDBC_USER, "sa")
              .setProperty(AvailableSettings.JAKARTA_JDBC_PASSWORD, "")
              .setProperty("hibernate.agroal.maxSize", 20)
              .setProperty(AvailableSettings.SHOW_SQL, true)
              .setProperty(AvailableSettings.FORMAT_SQL, true)
              .setProperty(AvailableSettings.HIGHLIGHT_SQL, true);

      Reflection.getAllEntitiesInSamePackage(Book.class)
              .forEach(configuration::addAnnotatedClass);

    var sessionFactory = configuration.buildSessionFactory();

    sessionFactory.getSchemaManager().exportMappedObjects(true);

    sessionFactory.inTransaction(
        session -> {
          var book = new Book("1234", "Some book");
          session.persist(book);
        });

    sessionFactory.inSession(
        session -> {
          var book = session.find(Book.class, "1234");
          System.out.printf("Found book: %s%n", book.getTitle());
        });
  }
}
