package com.nilhcem.business;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import com.nilhcem.core.hibernate.TransactionalReadOnly;
import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.core.spring.UserDetailsAdapter;
import com.nilhcem.dao.RightDao;
import com.nilhcem.dao.UserDao;
import com.nilhcem.form.SettingsForm;
import com.nilhcem.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

/**
 * Business class for accessing {@code User} data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Service
@TransactionalReadOnly
public class UserBo {
	@Autowired
	private UserDao userDao;
	@Autowired
	private RightDao rightDao;
	@Autowired
	private LanguageBo langBo;
	@Autowired
	private QuickMemoBo memoBo;
	@Autowired
	private ShaPasswordEncoder passwordEncoder;
	@Autowired
	private SaltSource saltSource;
	@Autowired
	private AuthenticationManager authenticationManager;

	private final Logger logger = LoggerFactory.getLogger(UserBo.class);

	/**
	 * Save a user in database and hash his password in SHA-256.
	 *
	 * @param user User we want to save.
	 * @param locale User's locale.
	 */
	@TransactionalReadWrite
	public void signUpUser(User user, Locale locale) {
		user.setEnabled(true);
		user.setRegistrationDate(Calendar.getInstance().getTime());
		user.getRights().add(rightDao.findByName(RightDao.RIGHT_USER));
		user.setLanguage(langBo.findByLocale(locale));
		user.setDeleteDate(null);
		userDao.save(user);

		//Hash password
		User myUser = userDao.findByEmail(user.getEmail());
		myUser.setPassword(hashPassword(myUser, myUser.getPassword()));
		userDao.update(myUser);

		//Create a quick memo for this user
		memoBo.createQuickMemo(myUser);
	}

	/**
	 * Find a user from his email.
	 *
	 * @param email Email of the User we are searching for.
	 * @return User object, or null if not found.
	 */
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	/**
	 * Auto log the user in, after he signs up.
	 * Must be called from a request filtered by Spring Security, otherwise {@code SecurityContextHolder} would not be updated.
	 *
	 * @param username Email of the user who will be automatically logged-in.
	 * @param password User's password.
	 * @param request HTTP request.
	 */
	public void autoLoginAfterSignup(String username, String password, HttpServletRequest request) {
		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
			token.setDetails(new WebAuthenticationDetails(request));
			Authentication authentication = authenticationManager.authenticate(token);
			logger.debug("Logging in with [{}]", authentication.getPrincipal());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		catch (Exception e) {
			SecurityContextHolder.getContext().setAuthentication(null);
			logger.error("Failure in autoLogin", e);
		}
	}

	/**
	 * Hash and Salt a password.
	 *
	 * @param user User object.
	 * @param password The password we want to salt.
	 * @return New hashed and salted password.
	 */
	public String hashPassword(User user, String password) {
		UserDetailsAdapter userDetails = new UserDetailsAdapter(user);
		Object salt = saltSource.getSalt(userDetails);
		return passwordEncoder.encodePassword(password, salt);
	}

	/**
	 * Update a {@code User} object from a SettingsForm object.
	 *
	 * @param user User we need to update.
	 * @param settings The settingsForm object which contains data to update.
	 */
	@TransactionalReadWrite
	public void updateSettings(User user, SettingsForm settings) {
		user.setEmail(settings.getEmail());
		user.setLanguage(langBo.findByLocale(langBo.getLocalFromCode(settings.getLang())));
		if (settings.getEditPassword().equals("yes")) {
			user.setPassword(hashPassword(user, settings.getNewPassword()));
		}
		userDao.update(user);
	}

	/**
	 * Set a {@code User} as deletable by deactivating his account and setting a delete date.
	 * A cron will then remove the user.
	 *
	 * @param user User we need to delete.
	 */
	@TransactionalReadWrite
	public void markAsDeletable(User user) {
		user.setEnabled(false);
		user.setDeleteDate(Calendar.getInstance().getTime());
		userDao.update(user);
	}

	/**
	 * Get all users which should be removed, and delete them
	 */
	@TransactionalReadWrite
	public void removeDeletableUsers() {
		List<User> users = userDao.getDeletableUsers();
		for (User user : users) {
			userDao.delete(user);
		}
	}
}
