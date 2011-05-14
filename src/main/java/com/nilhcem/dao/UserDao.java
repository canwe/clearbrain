package com.nilhcem.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.nilhcem.core.hibernate.CustomHibernateDaoSupport;
import com.nilhcem.model.User;

/**
 * DAO class for accessing User data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Repository("userDao")
public class UserDao extends CustomHibernateDaoSupport<User> {
	/**
	 * Find a user from his email.
	 *
     * @param email Email of the User we are searching for.
     * @return User object, or null if not found.
	 */
	public User findByEmail(String email) {
		Query query = getSession().createQuery("FROM User WHERE email= :email");
		query.setParameter("email", email);
		query.setMaxResults(1);
		return (User)query.uniqueResult();
	}

//	public User findLazyById(Long id) {
//		return (User)getSession().load(User.class, id);
//	}
}
