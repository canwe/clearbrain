package com.nilhcem.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.nilhcem.core.hibernate.AbstractHibernateDao;
import com.nilhcem.model.User;

/**
 * DAO class for accessing {@code User} data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Repository
public final class UserDao extends AbstractHibernateDao<User> {
	private static final int DAYS_BEFORE_REMOVE_USER = 3;

	@Autowired
	public UserDao(SessionFactory sessionFactory) {
		super(User.class, sessionFactory);
	}

	/**
	 * Find a user from his {@code email}, <b>ignoring case</b>.
	 *
	 * @param email Email of the User we are searching for.
	 * @return User object, or null if not found.
	 */
	public User findByEmail(String email) {
		Query query = query("FROM User WHERE lower(email)=:email")
			.setParameter("email", email.toLowerCase())
			.setMaxResults(1);
		return uniqueResult(query);
	}

	/**
	 * Return every user which should be removed:
	 * Those who have a deleteDate > 3 days.
	 *
	 * @return List of users which should be deleted.
	 */
	public List<User> getDeletableUsers() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -DAYS_BEFORE_REMOVE_USER);
		Date threeDaysAgo = cal.getTime();

		Query query = query("FROM User WHERE enabled=:enabled AND deleteDate <= :deleteDate")
			.setParameter("enabled", Boolean.FALSE)
			.setParameter("deleteDate", threeDaysAgo);
		return list(query);
	}
}
