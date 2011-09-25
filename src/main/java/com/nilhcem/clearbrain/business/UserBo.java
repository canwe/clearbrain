package com.nilhcem.clearbrain.business;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import com.nilhcem.clearbrain.core.hibernate.TransactionalReadOnly;
import com.nilhcem.clearbrain.core.hibernate.TransactionalReadWrite;
import com.nilhcem.clearbrain.core.spring.UserDetailsAdapter;
import com.nilhcem.clearbrain.dao.RightDao;
import com.nilhcem.clearbrain.dao.UserDao;
import com.nilhcem.clearbrain.form.SettingsForm;
import com.nilhcem.clearbrain.model.User;
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
 * Provides methods for dealing with {@code User} objects.
 *
 * @author Nilhcem
 * @since 1.0
 * @see User
 */
@Service
@TransactionalReadOnly
public /*no final*/ class UserBo {
	@Autowired
	private UserDao userDao;
	@Autowired
	private RightDao rightDao;
	@Autowired
	private LanguageBo langBo;
	@Autowired
	private QuickMemoBo memoBo;
	@Autowired
	private SessionBo sessionBo;
	@Autowired
	private ShaPasswordEncoder passwordEncoder;
	@Autowired
	private SaltSource saltSource;
	@Autowired
	private AuthenticationManager authenticationManager;

	private final Logger logger = LoggerFactory.getLogger(UserBo.class);

	/**
	 * Registers a user.
	 * <p>
	 * When a user subscribe on the website, this method should be called to save data,
	 * and use Spring Security features to hash and salt his password in SHA-256.
	 * </p>
	 *
	 * @param user the user who registered.
	 * @param locale user's locale. We store it to display the proper locale in the logged website for each user.
	 */
	@TransactionalReadWrite
	public void signUpUser(User user, Locale locale) {
		user.setEnabled(true);
		user.setRegistrationDate(Calendar.getInstance().getTime());
		user.getRights().add(rightDao.findByName(RightDao.RIGHT_USER));
		user.setLanguage(langBo.findByLocale(locale));
		user.setDeleteDate(null);
		userDao.save(user);

		// Hash password
		User myUser = userDao.findByEmail(user.getEmail());
		myUser.setPassword(hashPassword(myUser, myUser.getPassword()));
		userDao.update(myUser);

		// Create a quick memo for this user
		memoBo.createQuickMemo(myUser);
	}

	/**
	 * Finds a user from his email, this method is <b>case insensitive</b>.
	 *
	 * @param email the email of the user we are searching for.
	 * @return a user object if found, or {@code null} if not found.
	 */
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	/**
	 * Auto logs the user in, after he signs up.
	 * <p>
	 * Must be called from a request filtered by Spring Security, otherwise {@code SecurityContextHolder} won't be updated.<br />
	 * Once the user is logged in, fills his session with some important information (see {@link SessionBo#fillSession}).
	 * </p>
	 *
	 * @param email the email (username) of the user who will be automatically logged-in.
	 * @param password user's password.
	 * @param request user's HTTP request.
	 */
	public void autoLoginAfterSignup(String email, String password, HttpServletRequest request) {
		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
			token.setDetails(new WebAuthenticationDetails(request));
			Authentication authentication = authenticationManager.authenticate(token);
			logger.debug("Logging in with [{}]", authentication.getPrincipal());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			sessionBo.fillSession(true, request.getSession());
		} catch (Exception e) {
			SecurityContextHolder.getContext().setAuthentication(null);
			logger.error("Failure in autoLogin", e);
		}
	}

	/**
	 * Hashes and salts a password in SHA-256 using Spring Security features.
	 *
	 * @param user the user whose password will be hashed.
	 * @param password the password we want to hash.
	 * @return a new hashed and salted password..
	 */
	public String hashPassword(User user, String password) {
		UserDetailsAdapter userDetails = new UserDetailsAdapter(user);
		Object salt = saltSource.getSalt(userDetails);
		return passwordEncoder.encodePassword(password, salt);
	}

	/**
	 * Updates the settings of a user, using data from the form in parameter.
	 * <p>
	 * If "settings.getEditPassword" is "no", do not update the password.
	 * </p>
	 *
	 * @param user the user whose settings will be updated.
	 * @param settings a form containing data to update.
	 */
	@TransactionalReadWrite
	public void updateSettings(User user, SettingsForm settings) {
		user.setEmail(settings.getEmail());
		user.setLanguage(langBo.findByLocale(langBo.getLocaleFromCode(settings.getLang())));
		if (settings.getEditPassword().equals("yes")) {
			user.setPassword(hashPassword(user, settings.getNewPassword()));
		}
		userDao.update(user);
	}

	/**
	 * Deactivate a user's account and sets it as "deletable".
	 * <p>
	 * The account will then be removed automatically by a scheduler 3 days after.
	 * </p>
	 *
	 * @param user the user which will be deactivated.
	 * @see com.nilhcem.clearbrain.job.DeleteAccountsJob
	 */
	@TransactionalReadWrite
	public void markAsDeletable(User user) {
		user.setEnabled(false);
		user.setDeleteDate(Calendar.getInstance().getTime());
		userDao.update(user);
	}

	/**
	 * Removes all users who should be deleted.
	 * <p>
	 * A user who should be deleted has a {@code deleteDate} field >= 3 days.
	 * </p>
	 */
	@TransactionalReadWrite
	public void removeDeletableUsers() {
		List<User> users = userDao.getDeletableUsers();
		for (User user : users) {
			userDao.delete(user);
		}
	}
}
