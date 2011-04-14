package com.nilhcem.business;

import com.nilhcem.model.User;
import com.nilhcem.utils.MD5Hasher;
import com.nilhcem.core.hibernate.WithTransaction;
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
	private MD5Hasher md5Hasher;
	private Logger logger = LoggerFactory.getLogger(UserBo.class);

	/**
	 * Save a user in database and sign his password in Md5.
	 * 
	 * @param user the User we want to save
	 */
	@WithTransaction
	public void signUpUser(User user) {
		User u = new User();
		try {
			//TODO: Dirty but temporary
			u.setEmail(user.getEmail());
			u.setPassword(md5Hasher.toMd5(user.getPassword()));
			u.setEnabled(true);
		} catch (Exception e) {
			logger.error("Can't sign password in Md5");
			logger.error(e.getStackTrace().toString());
		}
		userDao.save(u);
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
