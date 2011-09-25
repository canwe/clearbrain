package com.nilhcem.clearbrain.dao;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.nilhcem.clearbrain.core.hibernate.AbstractHibernateDao;
import com.nilhcem.clearbrain.model.QuickMemo;
import com.nilhcem.clearbrain.model.User;

/**
 * Provides methods for accessing {@code QuickMemo} data from database.
 *
 * @author Nilhcem
 * @since 1.0
 * @see QuickMemo
 */
@Repository
public final class QuickMemoDao extends AbstractHibernateDao<QuickMemo> {
	@Autowired
	public QuickMemoDao(SessionFactory sessionFactory) {
		super(QuickMemo.class, sessionFactory);
	}

	/**
	 * Finds a memo from its {@code user}.
	 *
	 * @param user the owner of the quick memo.
	 * @return the quick memo of the user. Should never be null since a user must have a quick memo.
	 */
	public QuickMemo getByUser(User user) {
		Query query = query("FROM QuickMemo WHERE user=:user")
			.setParameter("user", user)
			.setMaxResults(1);
		return uniqueResult(query);
	}
}
