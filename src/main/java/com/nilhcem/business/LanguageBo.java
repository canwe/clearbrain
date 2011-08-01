package com.nilhcem.business;

import java.util.Locale;
import com.nilhcem.core.hibernate.TransactionalReadOnly;
import com.nilhcem.dao.LanguageDao;
import com.nilhcem.model.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class providing methods to simplify {@code Language} data management.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Service
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
	@TransactionalReadOnly
	public Language findByLocale(Locale locale) {
		Language lang = dao.findByCode(locale.getLanguage() + "_" + locale.getCountry());
		if (lang == null) {
			lang = dao.findByCode(DEFAULT_LANG);
		}
		return lang;
	}

	/**
	 * Convert a String code (ie: en_US) into a {@code Locale} object.
	 *
	 * @param code The String we need to convert.
	 * @return A Locale object matching the String parameter.
	 */
	public Locale getLocaleFromCode(String code) {
		if (code == null) {
			return null;
		}
		return new Locale(code.split("_")[0], code.split("_")[1]);
	}
}
