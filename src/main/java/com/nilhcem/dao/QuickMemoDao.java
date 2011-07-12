package com.nilhcem.dao;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.nilhcem.core.hibernate.AbstractHibernateDao;
import com.nilhcem.model.QuickMemo;
import com.nilhcem.model.User;

/**
 * DAO class for accessing {@code QuickMemo} data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Repository
public final class QuickMemoDao extends AbstractHibernateDao<QuickMemo> {
	@Autowired
	public QuickMemoDao(SessionFactory sessionFactory) {
		super(QuickMemo.class, sessionFactory);
	}

	/**
	 * Find a memo from its {@code user}.
	 *
	 * @param user User of the memo.
	 * @return QuickMemo.
	 */
	public QuickMemo getByUser(User user) {
		Query query = query("FROM QuickMemo WHERE user=:user")
			.setParameter("user", user)
			.setMaxResults(1);
		return uniqueResult(query);
	}
}
