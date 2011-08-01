package com.nilhcem.core.test;

import java.sql.SQLException;

import liquibase.exception.LiquibaseException;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Initialize database before running tests.
 * Every test class using database should extend this abstract class.
 *
 * @author Nilhcem
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public abstract class AbstractDbTest {
	@Autowired
	protected TestUtils testUtils;
	@Autowired
	private DatabaseCreator dbCreator;

    @Before
    public void setUp() throws SQLException, LiquibaseException {
		dbCreator.initializeDatabase();
    }
}
