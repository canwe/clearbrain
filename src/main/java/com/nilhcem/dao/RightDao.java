package com.nilhcem.dao;

import org.springframework.stereotype.Repository;
import com.nilhcem.core.hibernate.CustomHibernateDaoSupport;
import com.nilhcem.model.Right;

/**
 * DAO class for accessing Right data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Repository("rightDao")
public class RightDao extends CustomHibernateDaoSupport<Right> {
	public static final String RIGHT_USER = "RIGHT_USER";
	public static final String RIGHT_ADMIN = "RIGHT_ADMIN";

	/**
	 * Find a right from its name.
	 *
	 * @param name Name of the Right we are searching for.
	 * @return Right object, or null if not found.
	 */
	public Right findByName(String name) {
		return (Right)getHibernateTemplate().find("from Right where name=?", name).get(0);
	}
}
