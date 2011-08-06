package com.nilhcem.core.test;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.nilhcem.business.UserBo;
import com.nilhcem.core.spring.UserDetailsAdapter;
import com.nilhcem.dao.UserDao;
import com.nilhcem.model.User;

public class TestUtils {
	public final String LOCALE_FR = "fr_FR";
	public final String LOCALE_US = "en_US";
	private final Logger logger = LoggerFactory.getLogger(TestUtils.class);

	@Autowired
	private UserDao dao;
	@Autowired
	private UserBo service;

	// If we want to test a date, we can call getDateBeforeTest() before the test...
	public Date getDateBeforeTest() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, -1);
		return cal.getTime();
	}

	// ...then getDateAfterTest() just after the test is called...
	public Date getDateAfterTest() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 1);
		return cal.getTime();
	}

	// ...and finally call assertTrue(checkDateBetween(date, before, after)) to make sure the date was properly saved.
	public boolean checkDateBetween(Date date, Date before, Date after) {
		return (before.before(date) && after.after(date));
	}

	// ... create a new user with a new propagation. We won't be able to roll back the test but it's helpful sometimes.
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public User createTestUserNewPropagation(String email) {
		return createTestUser(email);
	}

	// Create a test user.
	public User createTestUser(String email) {
		logger.debug("Create test user: {}", email);
		User user = new User();
		user.setEmail(email);
		user.setPassword("");
		service.signUpUser(user, new Locale("en", "US"));
		user.setEnabled(false); // Test account.
		dao.save(user);
		return user;
	}

	// Authenticate a user.
	public void authenticate(User user) {
		UserDetailsAdapter userDetailsAdapter = new UserDetailsAdapter(user);
		Authentication authentication = new TestingAuthenticationToken(userDetailsAdapter, user.getPassword());
		SecurityContext securityContext = new SecurityContextImpl();
		securityContext.setAuthentication(authentication);
		SecurityContextHolder.setContext(securityContext);
	}

	// Create a user and set him as authenticated.
	public User createAuthenticatedTestUser(String email) {
		User user = createTestUser(email);
		authenticate(user);
		return user;
	}
}
