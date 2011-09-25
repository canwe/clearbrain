package com.nilhcem.clearbrain.core.spring;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.nilhcem.clearbrain.business.SessionBo;

/**
 * Logs information once authentication is successful, and fills some data in user's session.
 * <p>
 * Custom {@code UsernamePasswordAuthenticationFilter} bean for SpringSecurity.<br />
 * Also sets the user's language and some important session data (nb tasks todo today/tomorrow/this week).
 * </p>
 *
 * @author Nilhcem
 * @since 1.0
 * @see com.nilhcem.business.SessionBo#fillSession
 */
public final class LogAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final Logger logger = LoggerFactory.getLogger(LogAuthenticationFilter.class);

	@Autowired
	private SessionBo service;

	/**
	 * Logs after a successful authentication and fill some data in the user's session.
	 *
	 * @param request the user's HTTP request, to get the session from it.
	 * @param response the user's HTTP response.
	 * @param authResult The object returned from the {@code attemptAuthentication} method, which contains authentication data.
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, authResult);
		logger.info("{} has logged-in", authResult.getName());
		service.fillSession(true, request.getSession());
	}
}
