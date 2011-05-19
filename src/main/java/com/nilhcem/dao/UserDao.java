package com.nilhcem.dao;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.nilhcem.core.hibernate.AbstractHibernateDao;
import com.nilhcem.model.User;

/**
 * DAO class for accessing User data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Repository
public class UserDao extends AbstractHibernateDao<User> {
	@Autowired
	public UserDao(SessionFactory sessionFactory) {
		super(User.class, sessionFactory);
	}

    /**
	 * Find a user from his email.
	 *
     * @param email Email of the User we are searching for.
     * @return User object, or null if not found.
	 */
	public User findByEmail(String email) {
		Query query = query("FROM User WHERE email=:email")
			.setParameter("email", email)
			.setMaxResults(1);
		return uniqueResult(query);
	}
}
