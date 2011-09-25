package com.nilhcem.clearbrain.core.spring;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

/**
 * Gets the locale when a user logs in <i>(depending on his profile information)</i>.
 * <p>
 * When a user is logged-in, gets the locale from his profile information (instead of the "website" default one).<br />
 * When a user is not logged-in, acts like a {@code CookieLocaleResolver}.
 * </p>
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class CustomLocaleResolver extends CookieLocaleResolver {
	/**
	 * Gets the user's locale by searching on the session or the cookie.
	 * <p>
	 * <ul>
	 *   <li>If the user is logged in (and therefore must have a session), gets the locale from his profile information (stored in the session).</li>
	 *   <li>If user is not logged in, gets the locale normally ({@code CookieLocaleResolver} way).</li>
	 * </ul>
	 * </p>
	 *
	 * @param request the user's HTTP request, to get the session information.
	 * @return the user's locale.
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
	 * Sets the locale in the session if user is logged in and has a setted locale, otherwise acts normally ({@code CookieLocaleResolver} way).
	 *
	 * @param request the user's HTTP request, to get the session and set data into it.
	 * @param response the user's HTTP response.
	 * @param locale the locale which will be saved in session.
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
