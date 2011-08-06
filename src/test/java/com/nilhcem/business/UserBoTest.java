package com.nilhcem.business;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.core.spring.UserDetailsAdapter;
import com.nilhcem.core.test.abstr.AbstractDbTest;
import com.nilhcem.dao.RightDao;
import com.nilhcem.dao.UserDao;
import com.nilhcem.form.SettingsForm;
import com.nilhcem.model.User;

public class UserBoTest extends AbstractDbTest {
	private static final Long ID = 42L;
	private static final String PASSWORD = "myPassword";

	@Autowired
	private UserBo userBo;
	@Autowired
	private LanguageBo langBo;
	@Autowired
	private QuickMemoBo memoBo;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ShaPasswordEncoder passwordEncoder;
	@Autowired
	private SaltSource saltSource;

	private User createUser(String email) {
		User user = new User();
		user.setEmail(email);
		user.setPassword(UserBoTest.PASSWORD);
		userBo.signUpUser(user, langBo.getLocaleFromCode(testUtils.LOCALE_FR));
		return user;
	}

	@Test
	@TransactionalReadWrite
	public void testSignUp() {
		final String email = "UserBoTest@testSignUp";

		// Create a user.
		Date before = testUtils.getDateBeforeTest();
		User user = createUser(email);
		Date after = testUtils.getDateAfterTest();

		// Check if user is saved in database.
		user = userBo.findByEmail(email);
		assertNotNull(user);
		assertTrue(user.isEnabled());
		assertTrue(testUtils.checkDateBetween(user.getRegistrationDate(), before, after));
		assertEquals(email, user.getEmail());
		assertEquals(testUtils.LOCALE_FR, user.getLanguage().getCode());
		assertNull(user.getDeleteDate());
		assertNotNull(memoBo.getByUser(user));

		// Check user's rights.
		assertNotNull(user.getRights());
		assertEquals(1, user.getRights().size());
		assertEquals(RightDao.RIGHT_USER, user.getRights().get(0).getName());

		// Check password.
		assertEquals(passwordEncoder.encodePassword(UserBoTest.PASSWORD, saltSource.getSalt(new UserDetailsAdapter(user))), user.getPassword());
	}

	@Test
	public void testFindByEmailEmpty() {
		User user = userBo.findByEmail("");
		assertNull(user);
	}

	@Test
	@TransactionalReadWrite
	public void testFindByEmail() {
		final String email = "UserBoTest@testFindByEmail";
		testUtils.createTestUser(email);

		User found = userBo.findByEmail(email);
		assertNotNull(found);
	}

	@Test
	@TransactionalReadWrite
	public void testFindByEmailCaseInsensitive() {
		User user = testUtils.createTestUser("UserBoTest@testFindByEmailCaseInsensitive");

		String upperCase = user.getEmail().toUpperCase();
		assertFalse(user.getEmail().equals(upperCase));
		User found = userBo.findByEmail(upperCase);
		assertNotNull(found);
	}

	@Test
	@TransactionalReadWrite
	public void testAutoLoginAfterSignupWithDisabledUser() {
		User user = testUtils.createTestUser("UserBoTest@testAutoLoginAfterSignupWithDisabledUser");

		// Create a security context.
		SecurityContext securityContext = new SecurityContextImpl();
		SecurityContextHolder.setContext(securityContext);

		// Create an HTTP request.
		MockHttpServletRequest request = new MockHttpServletRequest();

		// Call autologin.
		userBo.autoLoginAfterSignup(user.getEmail(), "", request);
		assertNull(securityContext.getAuthentication());
	}

	@Test
	@TransactionalReadWrite
	public void testAutoLoginAfterSignupWithWrongPassword() {
		final String password = "P@$$#0RD.~";
		User user = testUtils.createTestUser("UserBoTest@testAutoLoginAfterSignupWithWrongPassword");
		user.setPassword(userBo.hashPassword(user, password));
		userDao.update(user);

		// Create a security context.
		SecurityContext securityContext = new SecurityContextImpl();
		SecurityContextHolder.setContext(securityContext);

		// Create an HTTP request.
		MockHttpServletRequest request = new MockHttpServletRequest();

		// Call autologin.
		userBo.autoLoginAfterSignup(user.getEmail(), password.toLowerCase(), request);
		assertNull(securityContext.getAuthentication());
	}

	@Test
	@TransactionalReadWrite
	public void testAutoLoginAfterSignup() {
		final String password = "P@$$#0RD.~";
		User user = testUtils.createTestUser("UserBoTest@testAutoLoginAfterSignup");
		user.setEnabled(true);
		user.setPassword(userBo.hashPassword(user, password));
		userDao.update(user);

		// Create a security context.
		SecurityContext securityContext = new SecurityContextImpl();
		SecurityContextHolder.setContext(securityContext);

		// Create an HTTP request.
		MockHttpServletRequest request = new MockHttpServletRequest();

		// Call autologin.
		userBo.autoLoginAfterSignup(user.getEmail(), password, request);
		assertNotNull(securityContext.getAuthentication());
		assertEquals(user.getEmail(), securityContext.getAuthentication().getName());
	}

	@Test
	public void testHashPassword() {
		User user = new User();
		user.setId(UserBoTest.ID);
		user.setEmail("UserBoTest@testHashPassword");
		user.setPassword(UserBoTest.PASSWORD);
		user.setEnabled(true);

		String password = userBo.hashPassword(user, UserBoTest.PASSWORD);
		assertEquals(64, password.length());
	}

	@Test
	public void testSaltPassword() {
		User user = new User();
		user.setId(UserBoTest.ID);
		user.setEmail("UserBoTest@testSaltPassword");
		user.setPassword(UserBoTest.PASSWORD);
		user.setEnabled(true);

		// Same password should not be the same for 2 different users.
		String password = userBo.hashPassword(user, UserBoTest.PASSWORD);
		user.setId(user.getId() + 1);
		String password2 = userBo.hashPassword(user, UserBoTest.PASSWORD);
		assertFalse(password.equals(password2));
	}

	@Test
	@TransactionalReadWrite
	public void testUpdateSettingsWithoutUpdatingPassword() {
		final String MY_NEW_PWD = "MY_NEW_PWD";
		assertFalse(UserBoTest.PASSWORD.equals(MY_NEW_PWD));

		// Create a user.
		User user = createUser("UserBoTest@testUpdateSettingsWithoutUpdatingPassword");
		String previousPassword = user.getPassword();
		String previousEmail = user.getEmail();
		String previousLang = user.getLanguage().getCode();

		// Create settings (update data).
		SettingsForm settings = new SettingsForm();
		settings.setEmail("UserBoTest@UntakenEmail"); // untaken email
		settings.setNewPassword(MY_NEW_PWD);
		settings.setLang(testUtils.LOCALE_US);
		settings.setEditPassword("no");

		// Update settings.
		userBo.updateSettings(user, settings);
		assertFalse(previousEmail.equals(user.getEmail()));
		assertFalse(previousLang.equals(user.getLanguage().getCode()));
		assertEquals(previousPassword, user.getPassword());
	}

	@Test
	@TransactionalReadWrite
	public void testUpdateSettingsWithPasswordUpdate() {
		final String MY_NEW_PWD = "MY_NEW_PWD";
		assertFalse(UserBoTest.PASSWORD.equals(MY_NEW_PWD));

		// Create a user.
		User user = createUser("UserBoTest@testUpdateSettingsWithPasswordUpdate");
		String previousPassword = user.getPassword();

		// Create settings (update data).
		SettingsForm settings = new SettingsForm();
		settings.setEmail(user.getEmail());
		settings.setNewPassword(MY_NEW_PWD);
		settings.setLang(testUtils.LOCALE_US);
		settings.setEditPassword("yes");

		// Update settings.
		userBo.updateSettings(user, settings);
		assertFalse(previousPassword.equals(user.getPassword()));
	}

	@Test
	@TransactionalReadWrite
	public void testMarkAsDeletable() {
		// Create a user.
		User user = createUser("UserBoTest@testMarkAsDeletable");
		assertNull(user.getDeleteDate());
		assertTrue(user.isEnabled());

		// Mark as deletable.
		Date before = testUtils.getDateBeforeTest();
		userBo.markAsDeletable(user);
		Date after = testUtils.getDateAfterTest();

		// Check values.
		assertFalse(user.isEnabled());
		assertTrue(testUtils.checkDateBetween(user.getDeleteDate(), before, after));
	}

	@Test
	@TransactionalReadWrite
	public void testRemoveDeletableUsers() {
		// Create a user.
		User user = createUser("UserBoTest@testRemoveDeletableUsers");
		String email = user.getEmail();
		assertNull(user.getDeleteDate());
		assertTrue(user.isEnabled());

		// Try to delete user (should failed since deleteDate < 3 days).
		userBo.removeDeletableUsers();
		user = userBo.findByEmail(email);
		assertNotNull(user);

		// Try again with another date < 3 days (should still failed).
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);
		user.setDeleteDate(cal.getTime());
		userBo.removeDeletableUsers();
		user = userBo.findByEmail(email);
		assertNotNull(user);

		// Try at last with a date >= 3 days...but with enable == true (should failed).
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -3);
		user.setDeleteDate(cal.getTime());
		user.setEnabled(true);
		userDao.update(user);
		userBo.removeDeletableUsers();
		user = userBo.findByEmail(email);
		assertNotNull(user);

		// ...and now with enable == false (should work).
		user.setEnabled(false);
		userDao.update(user);
		userBo.removeDeletableUsers();
		user = userBo.findByEmail(email);
		assertNull(user);
	}
}
