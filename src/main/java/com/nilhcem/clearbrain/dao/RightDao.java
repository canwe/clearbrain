package com.nilhcem.clearbrain.dao;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.nilhcem.clearbrain.core.hibernate.AbstractHibernateDao;
import com.nilhcem.clearbrain.model.Right;

/**
 * Provides methods for accessing {@code Right} data from database.
 *
 * @author Nilhcem
 * @since 1.0
 * @see Right
 */
@Repository
public final class RightDao extends AbstractHibernateDao<Right> {
	public static final String RIGHT_USER = "RIGHT_USER";
	public static final String RIGHT_ADMIN = "RIGHT_ADMIN";

	@Autowired
	public RightDao(SessionFactory sessionFactory) {
		super(Right.class, sessionFactory);
	}

	/**
	 * Finds a right from its {@code name}.
	 *
	 * @param name the name of the right we are searching for.
	 * @return the right object, or {@code null} if not found.
	 */
	public Right findByName(String name) {
		Query query = query("FROM Right WHERE name=:name")
			.setParameter("name", name)
			.setMaxResults(1);
		return uniqueResult(query);
	}
}
