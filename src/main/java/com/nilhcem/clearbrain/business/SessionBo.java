package com.nilhcem.clearbrain.business;

import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.nilhcem.clearbrain.core.hibernate.TransactionalReadOnly;
import com.nilhcem.clearbrain.core.spring.UserDetailsAdapter;
import com.nilhcem.clearbrain.dao.NoteDao;
import com.nilhcem.clearbrain.enums.DashboardDateEnum;
import com.nilhcem.clearbrain.model.User;

/**
 * Handles processes to do related to user's session, such as saving some data when a user logs in.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Service
public /*no final*/ class SessionBo {
	@Autowired
	private LanguageBo langBo;
	@Autowired
	private NoteDao noteDao;

	/**
	 * Fills the session with some useful data when a user logs in.
	 * <p>
	 * Below are the data which will be filled in the session:
	 * <ul>
	 *   <li>locale: user's default locale (see {@link com.nilhcem.clearbrain.core.spring.CustomLocaleResolver}).</li>
	 *   <li>today: number of todo which have a due date {@literal <} tomorrow</li>
	 *   <li>tomorrow: number of todo which have a due date == tomorrow</li>
	 *   <li>this week: number of todo which have a due date {@literal >=} (today) and {@literal <=} (today + 6)</li>
	 * </ul>
	 * </p>
	 *
	 * @param setLocale a flag to determine if we also save the locale in session ({@code true}) or not ({@code false}).
	 * @param session a session object to store data in.
	 */
	@TransactionalReadOnly
	public void fillSession(boolean setLocale, HttpSession session) {
		User user = ((UserDetailsAdapter) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getHibernateUser();

		// Set locale
		if (setLocale) {
			Locale locale = langBo.getLocaleFromCode(user.getLanguage().getCode());
			session.setAttribute("locale", locale);
		}

		// Set the number of tasks to do for today/tomorrow/this week
		Map<DashboardDateEnum, Long> map = noteDao.getNbTaskTodoHeader(user);
		session.setAttribute("today", map.get(DashboardDateEnum.TODAY));
		session.setAttribute("tomorrow", map.get(DashboardDateEnum.TOMORROW));
		session.setAttribute("week", map.get(DashboardDateEnum.THIS_WEEK));
	}
}
