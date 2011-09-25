package com.nilhcem.clearbrain.core.test.abstr;

import java.sql.SQLException;

import liquibase.exception.LiquibaseException;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nilhcem.clearbrain.core.test.DatabaseCreator;
import com.nilhcem.clearbrain.core.test.TestUtils;

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
	@Autowired
	protected TestUtils testUtils;

    @Before
    public void setUp() throws SQLException, LiquibaseException {
		dbCreator.initializeDatabase();
    }
}
