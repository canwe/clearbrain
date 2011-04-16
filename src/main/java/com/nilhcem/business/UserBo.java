package com.nilhcem.business;

import com.nilhcem.model.User;
import com.nilhcem.utils.MD5Hasher;
import com.nilhcem.core.hibernate.WithTransaction;
import com.nilhcem.dao.RightDao;
import com.nilhcem.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	private MD5Hasher md5Hasher;
	private Logger logger = LoggerFactory.getLogger(UserBo.class);

	/**
	 * Save a user in database and sign his password in Md5.
	 * 
	 * @param user the User we want to save
	 */
	@WithTransaction
	public void signUpUser(User user) {
		try {
			user.setPassword(md5Hasher.toMd5(user.getPassword()));
			user.setEnabled(true);
			user.getRights().add(rightDao.findByName(RightDao.RIGHT_USER));
		} catch (Exception e) {
			logger.error("Can't sign password in Md5");
			logger.error(e.getStackTrace().toString());
		}
		userDao.save(user);
	}

	/**
	 * Find a user from his email.
	 * 
     * @param email email of the User we are searching for
     * @return a User object, or null if not found
	 */
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}
}
