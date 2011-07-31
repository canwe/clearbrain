package com.nilhcem.core.test;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.nilhcem.business.UserBo;
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

	public Date getDateBeforeTest() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, -1);
		return cal.getTime();
	}

	public Date getDateAfterTest() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 1);
		return cal.getTime();
	}

	public boolean checkDateBetween(Date date, Date before, Date after) {
		return (before.before(date) && after.after(date));
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public User createTestUserNewPropagation(String email) {
		return createTestUser(email);
	}

	public User createTestUser(String email) {
		logger.debug("Create test user: {}", email);
		User user = new User();
		user.setEmail(email);
		user.setPassword("");
		service.signUpUser(user, new Locale("en", "US"));
		user.setEnabled(false); //test account
		dao.save(user);
		return user;
	}
}
