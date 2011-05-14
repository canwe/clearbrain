package com.nilhcem.dao;

import java.util.List;
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
public class LanguageDao extends CustomHibernateDaoSupport<Language> {
	/**
	 * Find a language from its code.
	 *
     * @param code Code of the language we are searching for.
     * @return Language object, or null if not found.
	 */
	@SuppressWarnings("unchecked")
	public Language findByCode(String code) {
		List<Language> list = getHibernateTemplate().find("FROM Language WHERE code=?", code);
		if (list.isEmpty())
			return null;
		return list.get(0);
	}
}
