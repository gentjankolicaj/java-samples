package org.sample.liquibase;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public final class HikariCPDataSource {

  private static final HikariDataSource HIKARI_DATA_SOURCE = create();

  private HikariCPDataSource() {
  }

  private static HikariDataSource create() {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
    hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/liquibase_sample");
    hikariConfig.setUsername("root");
    hikariConfig.setPassword("P@ssw0rd123");
    hikariConfig.setConnectionTimeout(30000);
    hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
    hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
    hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    return new HikariDataSource(hikariConfig);
  }

  public static Connection getConnection() throws SQLException {
    return HIKARI_DATA_SOURCE.getConnection();
  }

}
