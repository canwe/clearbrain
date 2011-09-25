package com.nilhcem.clearbrain.business;

import java.util.Locale;
import com.nilhcem.clearbrain.core.hibernate.TransactionalReadOnly;
import com.nilhcem.clearbrain.dao.LanguageDao;
import com.nilhcem.clearbrain.model.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides methods for dealing with {@code Language} objects.
 *
 * @author Nilhcem
 * @since 1.0
 * @see Language
 */
@Service
public /*no final*/ class LanguageBo {
	private static final String DEFAULT_LANG = "en_US";
	@Autowired
	private LanguageDao dao;

	/**
	 * Finds a language from its locale.
	 *
	 * @param locale a locale object representing the language we are searching for.
	 * @return the language whe are searching for, or the default language (en_US) if the locale was not found in database.
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
	 * Converts a {@code String} locale (for example: "en_US") into a {@code Locale} object.
	 *
	 * @param code a string representing the locale we are searching for.
	 * @return a locale object matching the code sent in parameters.
	 * <ul>
	 *   <li>If the parameter is {@code null}, returns a {@code null} object.</li>
	 *   <li>If the parameter is incorrect, returns the "{@code en_US}" locale.</li>
	 * </ul>
	 */
	public Locale getLocaleFromCode(String code) {
		if (code == null) {
			return null;
		}
		return new Locale(code.split("_")[0], code.split("_")[1]);
	}
}
