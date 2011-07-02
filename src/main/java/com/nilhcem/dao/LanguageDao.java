package com.nilhcem.dao;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.nilhcem.core.hibernate.AbstractHibernateDao;
import com.nilhcem.model.Language;

/**
 * DAO class for accessing {@code Language} data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Repository
public class LanguageDao extends AbstractHibernateDao<Language> {
	@Autowired
	public LanguageDao(SessionFactory sessionFactory) {
		super(Language.class, sessionFactory);
	}

	/**
	 * Find a language from its {@code code}.
	 *
     * @param code Code of the language we are searching for.
     * @return Language object, or null if not found.
	 */
	public Language findByCode(String code) {
		Query query = query("FROM Language WHERE code=:code")
			.setParameter("code", code)
			.setMaxResults(1);
		return uniqueResult(query);
	}
}
