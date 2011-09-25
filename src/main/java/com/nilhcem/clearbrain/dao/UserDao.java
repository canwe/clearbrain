package com.nilhcem.clearbrain.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.nilhcem.clearbrain.core.hibernate.AbstractHibernateDao;
import com.nilhcem.clearbrain.model.User;

/**
 * Provides methods for accessing {@code User} data from database.
 *
 * @author Nilhcem
 * @since 1.0
 * @see User
 */
@Repository
public final class UserDao extends AbstractHibernateDao<User> {
	private static final int DAYS_BEFORE_REMOVE_USER = 3;

	@Autowired
	public UserDao(SessionFactory sessionFactory) {
		super(User.class, sessionFactory);
	}

	/**
	 * Finds a user from his {@code email}, <b>ignoring case</b>.
	 *
	 * @param email the email of the user we are searching for.
	 * @return a user object, or {@code null} if not found.
	 */
	public User findByEmail(String email) {
		Query query = query("FROM User WHERE lower(email)=:email")
			.setParameter("email", email.toLowerCase())
			.setMaxResults(1);
		return uniqueResult(query);
	}

	/**
	 * Returns every user which should be removed.
	 * <p>
	 * A user which should be removed is a user who has a deleteDate > 3 days.
	 * </p>
	 *
	 * @return a list of users which should be deleted.
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
