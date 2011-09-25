package com.nilhcem.clearbrain.dao;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.nilhcem.clearbrain.core.hibernate.AbstractHibernateDao;
import com.nilhcem.clearbrain.model.Language;

/**
 * Provides methods for accessing {@code Language} data from database.
 *
 * @author Nilhcem
 * @since 1.0
 * @see Language
 */
@Repository
public final class LanguageDao extends AbstractHibernateDao<Language> {
	@Autowired
	public LanguageDao(SessionFactory sessionFactory) {
		super(Language.class, sessionFactory);
	}

	/**
	 * Finds a language from its {@code code}.
	 *
     * @param code the code of the language we are searching for, for example "{@code en_US}".
     * @return the language we need to find, or {@code null} if not found.
	 */
	public Language findByCode(String code) {
		Query query = query("FROM Language WHERE code=:code")
			.setParameter("code", code)
			.setMaxResults(1);
		return uniqueResult(query);
	}
}
