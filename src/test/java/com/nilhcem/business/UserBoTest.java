package com.nilhcem.business;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.core.spring.UserDetailsAdapter;
import com.nilhcem.dao.RightDao;
import com.nilhcem.dao.UserDao;
import com.nilhcem.form.SettingsForm;
import com.nilhcem.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public class UserBoTest {
	private static final String EMAIL = "###Test###@example.com";
	private static final String PASSWORD = "myPassword";
	private static final String LOCALE_FR = "fr_FR";
	private static final String LOCALE_US = "en_US";

	@Autowired
	@Qualifier(value = "userBo")
	private UserBo service;

	@Autowired
	private UserDao userDao;

	@Autowired
	private LanguageBo langBo;

	@Autowired
	private RightDao rightDao;

	@Autowired
	private ShaPasswordEncoder passwordEncoder;

	@Autowired
	private SaltSource saltSource;

	@Test
	@TransactionalReadWrite
	@Rollback(true)
	public void aUserCanSignUp() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, -1);
		Date before = cal.getTime();
		createUser();
		cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 1);
		Date after = cal.getTime();
		checkIfUserIsSavedInDB(before, after);
	}

	private void createUser() {
		User user = new User();
		user.setEmail(UserBoTest.EMAIL);
		user.setPassword(UserBoTest.PASSWORD);
		service.signUpUser(user, langBo.getLocalFromCode(UserBoTest.LOCALE_FR));
	}

	private User getUser() {
		return service.findByEmail(UserBoTest.EMAIL.toUpperCase()); //emails are case-insensitive
	}

	private void checkIfUserIsSavedInDB(Date before, Date after) {
		User userNull = service.findByEmail("");
		assertNull(userNull);
		User user = getUser();
		assertNotNull(user);
		assertEquals(UserBoTest.EMAIL, user.getEmail());
		assertEquals(UserBoTest.LOCALE_FR, user.getLanguage().getCode());
		assertNull(user.getDeleteDate());

		//Check password
		assertEquals(passwordEncoder.encodePassword(UserBoTest.PASSWORD, saltSource.getSalt(new UserDetailsAdapter(user))), user.getPassword());
		aUserWhoSignsUpShouldHaveUserRight(user);
		testDate(before, user.getRegistrationDate(), after);
	}

	private void aUserWhoSignsUpShouldHaveUserRight(User user) {
		assertNotNull(user.getRights());
		assertEquals(1, user.getRights().size());
		assertEquals(rightDao.findByName(RightDao.RIGHT_USER), user.getRights().get(0));
	}

	private void testDate(Date before, Date date, Date after) {
		assertFalse(before.after(date));
		assertFalse(after.before(date));
	}

	@Test
	@TransactionalReadWrite
	public void updateAndDeleteUser() {
		final String MY_NEW_PWD = "MY_NEW_PWD";
		final String UNTAKEN_EMAIL = "$R)R#J(R@WQW@test";
		assertFalse(UserBoTest.PASSWORD.equals(MY_NEW_PWD));

		//Create user and settings
		createUser();
		SettingsForm settings = new SettingsForm();
		settings.setEmail(UserBoTest.EMAIL);
		settings.setNewPassword(MY_NEW_PWD);
		settings.setLang(UserBoTest.LOCALE_US);

		//Updates
		updateWithoutEditingPassword(settings);
		updateWithPasswordEdit(settings, MY_NEW_PWD);
		updateLanguage(settings);
		updateWithUntakenEmail(settings, UNTAKEN_EMAIL);

		//Delete user
		setAsDeletable(UNTAKEN_EMAIL);
		deleteUser(UNTAKEN_EMAIL);
	}

	private void updateWithoutEditingPassword(SettingsForm settings) {
		settings.setEditPassword("no");
		service.updateSettings(getUser(), settings);
		User newUser = getUser();
		assertEquals(service.hashPassword(newUser, UserBoTest.PASSWORD), newUser.getPassword());
	}

	private void updateWithPasswordEdit(SettingsForm settings, String newPassword) {
		settings.setEditPassword("yes");
		service.updateSettings(getUser(), settings);
		User newUser = getUser();
		assertEquals(service.hashPassword(newUser, newPassword), newUser.getPassword());
	}

	private void updateLanguage(SettingsForm settings) {
		settings.setLang(UserBoTest.LOCALE_US);
		service.updateSettings(getUser(), settings);
		assertEquals(UserBoTest.LOCALE_US, getUser().getLanguage().getCode());

		settings.setLang(UserBoTest.LOCALE_FR);
		service.updateSettings(getUser(), settings);
		assertEquals(UserBoTest.LOCALE_FR, getUser().getLanguage().getCode());
	}

	private void updateWithUntakenEmail(SettingsForm settings, String untakenEmail) {
		assertNull(service.findByEmail(untakenEmail));
		settings.setEmail(untakenEmail);
		service.updateSettings(getUser(), settings);
		assertNull(getUser());
		assertNotNull(service.findByEmail(untakenEmail));
	}

	private void setAsDeletable(String email) {
		User user = service.findByEmail(email);
		assertNull(user.getDeleteDate());
		assertTrue(user.isEnabled());

		//Mark as deletable
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, -1);
		Date before = cal.getTime();
		service.markAsDeletable(user);
		cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 1);
		Date after = cal.getTime();

		//Check values
		assertFalse(user.isEnabled());
		testDate(before, user.getDeleteDate(), after);
	}

	private void deleteUser(String email) {
		//Try to delete user (should failed since deleteDate < 3 days)
		service.removeDeletableUsers();
		User user = service.findByEmail(email);
		assertNotNull(user);

		//Try again with another date < 3 days [should still failed]
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -2);
		user.setDeleteDate(cal.getTime());
		userDao.update(user);
		service.removeDeletableUsers();
		user = service.findByEmail(email);
		assertNotNull(user);

		//Try at last with a date >= 3 days
		cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -3);
		user.setDeleteDate(cal.getTime());
		//...but with enable == true [should failed]
		user.setEnabled(true);
		userDao.update(user);
		assertNotNull(user);

		//...and now with enable == false [should work]
		user.setEnabled(false);
		userDao.update(user);
		service.removeDeletableUsers();
		assertNull(service.findByEmail(email));
	}
}
