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
 * Custom UsernamePasswordAuthenticationFilter bean for SpringSecurity to log information once authentication is successful.
 * 
 * @author Nilhcem
 * @since 1.0
 */
public class LogAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private Logger logger = LoggerFactory.getLogger(LogAuthenticationFilter.class);

	/**
	 * Log after successful authentication.
	 * 
	 * @param request
	 * @param response
	 * @param authResult
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult)
		throws IOException, ServletException {
		super.successfulAuthentication(request, response, authResult);
		logger.info("{} has logged-in", authResult.getName());
	}
}
