package com.nilhcem.business;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.nilhcem.core.spring.UserDetailsAdapter;
import com.nilhcem.model.User;

/**
 * Business class handling processes to do when a user logs in.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Service
public class AuthenticationFilterBo {
	@Autowired
	private LanguageBo langBo;

	/**
	 * When a user logs in, fill session with useful data, such as:
	 * - locale: user's default locale (@see CustomLocaleResolver).
	 *
	 * @param request HttpRequest to set data on session.
	 */
	public void fillSession(HttpServletRequest request) {
		User user = ((UserDetailsAdapter)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getHibernateUser();
		Locale locale = langBo.getLocalFromCode(user.getLanguage().getCode());
		request.getSession().setAttribute("locale", locale);
	}
}
