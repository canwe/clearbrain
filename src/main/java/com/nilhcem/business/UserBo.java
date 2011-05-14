package com.nilhcem.business;

import java.util.Calendar;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import com.nilhcem.core.hibernate.WithTransaction;
import com.nilhcem.core.spring.UserDetailsAdapter;
import com.nilhcem.dao.RightDao;
import com.nilhcem.dao.UserDao;
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
 * Business class for accessing User data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Service
public class UserBo {
	@Autowired
	private UserDao userDao;
	@Autowired
	private RightDao rightDao;
	@Autowired
	private LanguageBo langBo;
	@Autowired
	private ShaPasswordEncoder passwordEncoder;
	@Autowired
	private SaltSource saltSource;
	@Autowired
	private AuthenticationManager authenticationManager;
	private Logger logger = LoggerFactory.getLogger(UserBo.class);

	/**
	 * Save a user in database and hash his password in SHA-256.
	 *
	 * @param user User we want to save.
	 * @param locale User's locale.
	 */
	@WithTransaction
	public void signUpUser(User user, Locale locale) {
		user.setEnabled(true);
		user.setRegistrationDate(Calendar.getInstance().getTime());
		user.getRights().add(rightDao.findByName(RightDao.RIGHT_USER));
		user.setLanguage(langBo.findByLocale(locale));
		userDao.save(user);

		//Hash password
		UserDetailsAdapter userDetails = new UserDetailsAdapter(user);
		Object salt = saltSource.getSalt(userDetails);
		user.setPassword(passwordEncoder.encodePassword(userDetails.getPassword(), salt));
		userDao.update(user);
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
	 * Must be called from a request filtered by Spring Security, otherwise SecurityContextHolder would not be updated.
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
	    } catch (Exception e) {
	        SecurityContextHolder.getContext().setAuthentication(null);
	        logger.error("Failure in autoLogin", e);
	    }
	}
}
