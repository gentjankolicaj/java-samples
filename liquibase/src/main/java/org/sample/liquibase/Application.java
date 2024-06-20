package org.sample.liquibase;

import java.sql.Connection;
import java.sql.SQLException;

public class Application {

  public static void main(String[] args) throws SQLException {
    Connection connection = HikariCPDataSource.getConnection();
    LiquibaseMigrator.runMigrations(connection);
  }
}
