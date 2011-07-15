package com.nilhcem.core.spring;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.nilhcem.business.AuthenticationFilterBo;

/**
 * Custom {@code UsernamePasswordAuthenticationFilter} bean for SpringSecurity to log information once authentication is successful.
 * Set also the user's language and some important session data (nb tasks todo today/tomorrow/this week)
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class LogAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final Logger logger = LoggerFactory.getLogger(LogAuthenticationFilter.class);

	@Autowired
	private AuthenticationFilterBo authenticationService;

	/**
	 * Log after successful authentication.
	 *
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @param authResult The object returned from the attemptAuthentication method, which contains authentication data.
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, authResult);
		logger.info("{} has logged-in", authResult.getName());
		authenticationService.fillSession(request);
	}
}
