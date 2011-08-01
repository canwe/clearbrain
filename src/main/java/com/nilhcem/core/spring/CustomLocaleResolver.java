package com.nilhcem.core.spring;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

/**
 * Custom locale resolver to automatically get the right locale when a user logs in (depending on his profile information).
 * Act like a CookieLocaleResolver on front webiste.
 * When user is logged-in, get the locale from his profile information (instead of the "website" default one).
 *
 * @author Nilhcem
 * @since 1.0
 */
public class CustomLocaleResolver extends CookieLocaleResolver {
	/**
	 * Get the User's locale by searching on the session or the cookie.
	 * If user is logged in (and therefore must have a session), get the locale from his profile information (stored in the session).
	 * If user is not logged in, get the locale normally (CookieLocaleResolver way).
	 *
	 * @param request Http request, to get the session information.
	 * @return The user's locale.
	 */
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("locale") != null) {
			return (Locale) request.getSession().getAttribute("locale");
		}
		return super.resolveLocale(request);
	}

	/**
	 * If user is logged in and a locale is setted, set the locale in the session also.
	 * Otherwise, act normally (CookieLocaleResolver way).
	 * @param request Http request.
	 * @param response Http response.
	 * @param locale Locale we need to save.
	 */
	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			request.getSession().setAttribute("locale", locale);
		}
		super.setLocale(request, response, locale);
	}
}
