package com.nilhcem.business;

import java.util.Locale;
import com.nilhcem.core.hibernate.TransactionalReadOnly;
import com.nilhcem.dao.LanguageDao;
import com.nilhcem.model.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Business class for accessing {@code Language} data.
 * 
 * @author Nilhcem
 * @since 1.0
 */
@Service
@TransactionalReadOnly
public class LanguageBo {
	private static final String DEFAULT_LANG = "en_US";
	@Autowired
	private LanguageDao dao;

	/**
	 * Find a language from its locale.
	 *
     * @param locale Locale (language we are searching for).
     * @return A Language object, or default language (en_US) if code was not found.
	 */
	public Language findByLocale(Locale locale) {
		Language lang = dao.findByCode(locale.getLanguage() + "_" + locale.getCountry());
		if (lang == null) {
			lang = dao.findByCode(DEFAULT_LANG);
		}
		return lang;
	}
}
