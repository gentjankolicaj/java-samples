package org.sample.liquibase;

import java.sql.Connection;
import java.sql.SQLException;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class LiquibaseMigrator {

  private static final String MASTER_CHANGELOG_FILE = "db/changelog/db.changelog-master.xml";

  private LiquibaseMigrator() {
  }

  public static void runMigrations(Connection connection) {
    try {
      Database database = DatabaseFactory.getInstance()
          .findCorrectDatabaseImplementation(new JdbcConnection(connection));
      Liquibase liquibase = new Liquibase(MASTER_CHANGELOG_FILE, new ClassLoaderResourceAccessor(),
          database);
      liquibase.update(new Contexts(), new LabelExpression());
    } catch (Exception e) {
      log.error("", e);
      if (connection != null) {
        try {
          connection.rollback();
        } catch (SQLException sqe) {
          log.error("", sqe);
        }
      }
    }
  }

}
