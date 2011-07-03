package com.nilhcem.business;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import com.nilhcem.core.spring.UserDetailsAdapter;
import com.nilhcem.dao.RightDao;
import com.nilhcem.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
@Transactional
public class UserBoTest {
	private static final String EMAIL = "###Test###@example.com";
	private static final String PASSWORD = "myPassword";
	private static final String LOCALE = "fr_FR";

	@Autowired
	@Qualifier(value = "userBo")
	private UserBo service;

	@Autowired
	private RightDao rightDao;

	@Autowired
	private ShaPasswordEncoder passwordEncoder;

	@Autowired
	private SaltSource saltSource;

	@Test
	@Rollback(true)
	public void aUserCanSignUp() {
		User user = new User();
		user.setEmail(UserBoTest.EMAIL);
		user.setPassword(UserBoTest.PASSWORD);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, -1);
		Date before = cal.getTime();
		String[] localSplitted = UserBoTest.LOCALE.split("_");
		service.signUpUser(user, new Locale(localSplitted[0], localSplitted[1]));
		cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 1);
		Date after = cal.getTime();
		checkIfUserIsSavedInDB(before, after);
	}

	private void checkIfUserIsSavedInDB(Date before, Date after) {
		User userNull = service.findByEmail("");
		assertNull(userNull);
		User user = service.findByEmail(UserBoTest.EMAIL);
		assertNotNull(user);
		assertEquals(UserBoTest.EMAIL, user.getEmail());
		assertEquals(UserBoTest.LOCALE, user.getLanguage().getCode());

		//Check password
		assertEquals(passwordEncoder.encodePassword(UserBoTest.PASSWORD, saltSource.getSalt(new UserDetailsAdapter(user))), user.getPassword());
		aUserWhoSignsUpShouldHaveUserRight(user);
		testRegistrationDate(before, user.getRegistrationDate(), after);
	}

	private void aUserWhoSignsUpShouldHaveUserRight(User user) {
		assertNotNull(user.getRights());
		assertEquals(1, user.getRights().size());
		assertEquals(rightDao.findByName(RightDao.RIGHT_USER), user.getRights().get(0));
	}

	private void testRegistrationDate(Date before, Date registrationDate, Date after) {
		assertFalse(before.after(registrationDate));
		assertFalse(after.before(registrationDate));
	}
}
