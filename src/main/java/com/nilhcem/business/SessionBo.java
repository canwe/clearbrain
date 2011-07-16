package com.nilhcem.business;

import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.nilhcem.core.hibernate.TransactionalReadOnly;
import com.nilhcem.core.spring.UserDetailsAdapter;
import com.nilhcem.dao.NoteDao;
import com.nilhcem.model.User;
import com.nilhcem.model.enums.SessionDateEnum;

/**
 * Business class handling processes to do when a user logs in:
 * Mainly save data in session.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Service
public class SessionBo {
	@Autowired
	private LanguageBo langBo;
	@Autowired
	private NoteDao noteDao;

	/**
	 * When a user logs in, fill session with useful data, such as:
	 * - locale: user's default locale (@see CustomLocaleResolver).
	 * - today: nb of todo which have a due date < tomorrow
	 * - tomorrow: nb of todo which have a due date = tomorrow
	 * - this week : nb of todo which have a due date >= (today) and <= (today + 6)
	 *
	 * @param setLocale <code>true</code> if we also save locale in session.
	 * @param session HttpSession to store data in.
	 */
	@TransactionalReadOnly
	public void fillSession(boolean setLocale, HttpSession session) {
		User user = ((UserDetailsAdapter)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getHibernateUser();

		//Set locale
		if (setLocale) {
			Locale locale = langBo.getLocalFromCode(user.getLanguage().getCode());
			session.setAttribute("locale", locale);
		}

		//Set the number of tasks to do for today/tomorrow/this week
		Map<SessionDateEnum, Long> map = noteDao.getNbTaskTodoHeader(user);
		session.setAttribute("today", map.get(SessionDateEnum.TODAY));
		session.setAttribute("tomorrow", map.get(SessionDateEnum.TOMORROW));
		session.setAttribute("week", map.get(SessionDateEnum.THIS_WEEK));
	}
}
