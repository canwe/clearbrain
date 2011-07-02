package com.nilhcem.core.spring;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Custom {@code UsernamePasswordAuthenticationFilter} bean for SpringSecurity to log information once authentication is successful.
 *
 * @author Nilhcem
 * @since 1.0
 */
public class LogAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private Logger logger = LoggerFactory.getLogger(LogAuthenticationFilter.class);

	/**
	 * Log after successful authentication.
	 *
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @param authResult The object returned from the attemptAuthentication method, which contains authentication data.
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult)
		throws IOException, ServletException {
		super.successfulAuthentication(request, response, authResult);
		logger.info("{} has logged-in", authResult.getName());
	}
}
