package com.nilhcem.core.test;

import java.sql.SQLException;

import liquibase.exception.LiquibaseException;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Initialize database before running controller tests.
 * Every controller test class using database should extend this abstract class.
 *
 * @author Nilhcem
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml", "classpath:/META-INF/spring/mvc/mvc-dispatcher-servlet.xml"})
public abstract class AbstractControllerTest {
	@Autowired
	private DatabaseCreator dbCreator;

    @Before
    public void setUp() throws SQLException, LiquibaseException {
		dbCreator.initializeDatabase();
    }
}
