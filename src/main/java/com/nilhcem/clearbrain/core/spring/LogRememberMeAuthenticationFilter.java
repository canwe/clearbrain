package com.nilhcem.clearbrain.core.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import com.nilhcem.clearbrain.business.SessionBo;

/**
 * Logs information once authentication through 'remember me' is successful, and fills some data in user's session.
 * <p>
 * Custom {@code RememberMeAuthenticationFilter} bean for SpringSecurity.<br />
 * Also sets the user's language and some important session data (nb tasks todo today/tomorrow/this week).
 * </p>
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class LogRememberMeAuthenticationFilter extends RememberMeAuthenticationFilter {
	private final Logger logger = LoggerFactory.getLogger(LogRememberMeAuthenticationFilter.class);

	@Autowired
	private SessionBo service;

	/**
	 * Logs after successful authentication through remember me and fill some data in the user's session.
	 *
	 * @param request the user's HTTP request, to get the session from it.
	 * @param response the user's HTTP response.
	 * @param authResult The object returned from the {@code attemptAuthentication} method, which contains authentication data.
	 */
	@Override
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
		super.onSuccessfulAuthentication(request, response, authResult);
		logger.info("{} has logged-in through 'remember me'", authResult.getName());
		service.fillSession(true, request.getSession());
	}
}
