package com.nilhcem.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.nilhcem.core.hibernate.CustomHibernateDaoSupport;
import com.nilhcem.model.Language;

/**
 * DAO class for accessing Language data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Repository("languageDao")
public class LanguageDao extends CustomHibernateDaoSupport {
	/**
	 * Find a language from its code.
	 *
     * @param code Code of the language we are searching for
     * @return a Language object, or null if not found
	 */
	public Language findByCode(String code) {
		Query query = getSession().createQuery("FROM Language WHERE code= :code");
		query.setParameter("code", code);
		query.setMaxResults(1);
		return (Language)query.uniqueResult();
	}
}
