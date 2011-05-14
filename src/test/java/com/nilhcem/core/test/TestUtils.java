package com.nilhcem.core.test;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import com.nilhcem.business.UserBo;
import com.nilhcem.dao.UserDao;
import com.nilhcem.model.User;

public class TestUtils {
	private static final String TEST_USER_EMAIL = "test@example.com";
	private static final String TEST_USER_PASSWD = "myP#ssW0Rd";

	@Autowired
	private UserDao usersDao;
	@Autowired
	private UserBo usersHandler;

	public synchronized User getTestUser() {
		User user = usersHandler.findByEmail(TEST_USER_EMAIL);
		if (user == null) {
			user = new User();
			user.setEmail(TEST_USER_EMAIL);
			user.setPassword(TEST_USER_PASSWD);
			usersHandler.signUpUser(user, new Locale("en", "US"));
			user.setEnabled(false); //test account
			usersDao.update(user);
		}
		return user;
	}
}
