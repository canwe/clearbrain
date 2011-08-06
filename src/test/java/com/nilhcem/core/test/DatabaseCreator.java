package com.nilhcem.core.test;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring bean which initialize database, calling Liquibase.
 *
 * @author Nilhcem
 * @since 1.0
 */
public class DatabaseCreator {
	private static boolean databaseInitialized = false;
	private static final String LIQUIBASE_FILE = "src/main/config/liquibase/db.changelog-master.xml";

	@Autowired
	private DataSource dataSource;

	public synchronized void initializeDatabase() throws SQLException, LiquibaseException {
		if (!DatabaseCreator.databaseInitialized) {
			Connection conn = dataSource.getConnection();
			Liquibase liquibase = new Liquibase(LIQUIBASE_FILE, new FileSystemResourceAccessor(), new JdbcConnection(conn));
			liquibase.dropAll(); // Drop all data before launching unit tests.
			liquibase.update("");
			conn.close();
			DatabaseCreator.databaseInitialized = true;
		}
	}
}
