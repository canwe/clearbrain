package com.nilhcem.clearbrain.controller;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.nilhcem.clearbrain.core.spring.UserDetailsAdapter;
import com.nilhcem.clearbrain.model.User;

/**
 * Convenient superclass for controller implementations.
 * <p>
 * Provides some methods to get the current logged user, or to simplify i18n in JavaScript.<br />
 * Every controller should extends from it.
 * </p>
 *
 * @author Nilhcem
 * @since 1.0
 */
public abstract class AbstractController {
	@Autowired
	private MessageSource message;
	private String[] i18nJs = null;
	private String i18nJsRemoveStr = null;

	/**
	 * Returns the current logged user.
	 *
	 * @return the current user.
	 */
	protected final User getCurrentUser() {
		return ((UserDetailsAdapter) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getHibernateUser();
	}

	/**
	 * Sets i18n keys which will be sent later to JavaScript to display some localized text in the views.
	 *
	 * @param i18nJs an array of i18n keys as written in the properties files.
	 * @param i18nJsRemoveStr a regex substring matching the beginning of the common part of the keys, to transfer less data.
	 * @see #sendI18nToJavascript
	 */
	protected final void setI18nJsValues(String[] i18nJs, String i18nJsRemoveStr) {
		this.i18nJs = i18nJs.clone();
		this.i18nJsRemoveStr = i18nJsRemoveStr;
	}

	/**
	 * Injects some localized strings into JavaScript's views.
	 *
	 * @param locale the user's locale.
	 * @return a map of i18n string for JavaScript.
	 */
	@ModelAttribute("i18nJS")
	public final Map<String, String> sendI18nToJavascript(Locale locale) {
		Map<String, String> i18n = new LinkedHashMap<String, String>();
		if (i18nJs != null && i18nJsRemoveStr != null) {
			for (String msg : i18nJs) {
				i18n.put(msg.replaceFirst(i18nJsRemoveStr, ""), message.getMessage(msg, null, locale));
			}
		}
		return i18n;
	}

	/**
	 * Returns the message source for i18n.
	 *
	 * @return a MessageSource object for i18n.
	 */
	public MessageSource getMessageSource() {
		return message;
	}
}
