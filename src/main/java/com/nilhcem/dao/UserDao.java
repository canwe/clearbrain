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
public class UserDao extends CustomHibernateDaoSupport {
	/**
	 * Save a user.
	 *
	 * @param user the User we need to save
	 */
	public void save(User user) {
		getHibernateTemplate().save(user);
	}

	/**
	 * Update a user.
	 *
	 * @param user the User we need to update
	 */
	public void update(User user) {
		getHibernateTemplate().update(user);
	}

	/**
	 * Find a user from his email.
	 *
     * @param email email of the User we are searching for
     * @return a User object, or null if not found
	 */
	public User findByEmail(String email) {
		Query query = getSession().createQuery("FROM User WHERE email= :email");
		query.setParameter("email", email);
		query.setMaxResults(1);
		return (User)query.uniqueResult();
	}
}
