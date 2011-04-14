package com.nilhcem.core.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Used by Spring to inject automatically the session factory dependency to every DAO.
 * <p>Every DAO should, therefore, inherit from CustomHibernateDaoSupport.</p>
 * 
 * @author Nilhcem
 * @since 1.0
 */
public abstract class CustomHibernateDaoSupport extends HibernateDaoSupport {
	/**
	 * Used to let Spring give us the session factory bean.
	 * 
	 * @param sessionFactory so that Spring wire this automatically
	 */
	@Autowired
	public void setSessionFactoryBean(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
