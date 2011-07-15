package com.nilhcem.controller;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.nilhcem.core.spring.UserDetailsAdapter;
import com.nilhcem.model.User;

/**
 * Abstract Spring MVC Controller.
 * Provides some function to simplify i18n in JS and to get the current user.
 * Every controller in this project should extends from it.
 *
 * @author Nilhcem
 * @since 1.0
 */
public abstract class AbstractController {
	@Autowired
	protected MessageSource message;
	protected String[] i18nJs = null;
	protected String i18nJsRemoveStr = null;

	/**
	 * Get current user from session.
	 *
	 * @return Current user.
	 */
	protected final User getCurrentUser() {
		return ((UserDetailsAdapter)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getHibernateUser();
	}

	/**
	 * Set i18n keys to display in Javascript automatically.
	 *
	 * @see #sendI18nToJavascript(HttpServletRequest) i18nJs
	 * @param i18nJs Array of keys for the Javascript.
	 * @param i18nJsRemoveStr Regex substring to remove from the keys (to transfer less data)
	 */
	protected final void setI18nJsValues(String[] i18nJs, String i18nJsRemoveStr) {
		this.i18nJs = i18nJs;
		this.i18nJsRemoveStr = i18nJsRemoveStr;
	}

	/**
	 * Inject localized strings into Javascript.
	 *
	 * @param Locale User's locale.
	 * @return A map of i18n string for Javascript.
	 * @throws Exception.
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
}
