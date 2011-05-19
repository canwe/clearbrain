package com.nilhcem.dao;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.nilhcem.core.hibernate.AbstractHibernateDao;
import com.nilhcem.model.Right;

/**
 * DAO class for accessing Right data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Repository
public class RightDao extends AbstractHibernateDao<Right> {
	public static final String RIGHT_USER = "RIGHT_USER";
	public static final String RIGHT_ADMIN = "RIGHT_ADMIN";

	@Autowired
	public RightDao(SessionFactory sessionFactory) {
		super(Right.class, sessionFactory);
	}

	/**
	 * Find a right from its name.
	 *
	 * @param name Name of the Right we are searching for.
	 * @return Right object, or null if not found.
	 */
	public Right findByName(String name) {
		Query query = query("FROM Right WHERE name=:name")
			.setParameter("name", name)
			.setMaxResults(1);
		return uniqueResult(query);
	}
}
