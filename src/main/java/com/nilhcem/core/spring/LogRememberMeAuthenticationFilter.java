package com.nilhcem.core.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import com.nilhcem.business.SessionBo;

/**
 * Custom {@code RememberMeAuthenticationFilter} bean for SpringSecurity to log information once authentication is successful through remember me.
 * Set also the user's language and some important session data (nb tasks todo today/tomorrow/this week)
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class LogRememberMeAuthenticationFilter extends RememberMeAuthenticationFilter {
	private final Logger logger = LoggerFactory.getLogger(LogRememberMeAuthenticationFilter.class);

	@Autowired
	private SessionBo service;

	/**
	 * Log after successful authentication through remember me.
	 *
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @param authResult The object returned from the attemptAuthentication method, which contains authentication data.
	 */
	@Override
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
		super.onSuccessfulAuthentication(request, response, authResult);
		logger.info("{} has logged-in through 'remember me'", authResult.getName());
		service.fillSession(true, request.getSession());
	}
}
