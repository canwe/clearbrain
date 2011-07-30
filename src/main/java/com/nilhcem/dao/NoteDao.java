package com.nilhcem.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.nilhcem.core.hibernate.AbstractHibernateDao;
import com.nilhcem.enums.DashboardDateEnum;
import com.nilhcem.model.Note;
import com.nilhcem.model.User;

/**
 * DAO class for accessing {@code Note} data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Repository
public final class NoteDao extends AbstractHibernateDao<Note> {
	@Autowired
	public NoteDao(SessionFactory sessionFactory) {
		super(Note.class, sessionFactory);
	}

	/**
	 * Find all the notes owned by {@code user}.
	 *
	 * @param user Owner of the notes we are searching for.
	 * @return List of notes.
	 */
	public List<Note> getNotes(User user) {
		Query query = query("FROM Note WHERE user=:user ORDER BY creationDate ASC")
			.setParameter("user", user);
		return list(query);
	}

	/**
	 * Find a note from its {@code id}.
	 *
	 * @param user Owner of the note we are searching for.
	 * @param id Id of the note we are searching for.
	 * @return Note.
	 */
	public Note getById(User user, Long id) {
		Query query = query("FROM Note WHERE user=:user AND id=:id")
			.setParameter("user", user)
			.setParameter("id", id)
			.setMaxResults(1);
		return uniqueResult(query);
	}

	/**
	 * Return a Map of tasks to do today, tomorrow and this week for user {@code user}.
	 *
	 * @param user Owner of the notes we are counting.
	 * @return Associative array with key = TODAY/TOMORROW/THIS_WEEK, value = nb of tasks to do.
	 */
	public Map<DashboardDateEnum, Long> getNbTaskTodoHeader(User user) {
		//Set variables
		Map<DashboardDateEnum, Long> map = new HashMap<DashboardDateEnum, Long>();
		Calendar cal = Calendar.getInstance();
		cal.clear(Calendar.HOUR);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		Date today = cal.getTime();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		Date tomorrow = cal.getTime();
		cal.add(Calendar.DAY_OF_YEAR, 6);
		Date endWeek = cal.getTime();

		//Nb tasks for today
		Query query = query("SELECT COUNT(id) FROM Note WHERE user = :user AND resolvedDate IS NULL AND dueDate <= :today")
			.setParameter("user", user)
			.setParameter("today", today)
			.setMaxResults(1);
		map.put(DashboardDateEnum.TODAY, (Long)query.uniqueResult());

		//Nb tasks for tomorrow
		query = query("SELECT COUNT(id) FROM Note WHERE user = :user AND resolvedDate IS NULL AND dueDate = :tomorrow")
			.setParameter("user", user)
			.setParameter("tomorrow", tomorrow)
			.setMaxResults(1);
		map.put(DashboardDateEnum.TOMORROW, (Long)query.uniqueResult());

		//Nb tasks for this week
		query = query("SELECT COUNT(id) FROM Note WHERE user = :user AND resolvedDate IS NULL AND dueDate >= :today AND dueDate <= :endWeek")
			.setParameter("user", user)
			.setParameter("today", today)
			.setParameter("endWeek", endWeek)
			.setMaxResults(1);
		map.put(DashboardDateEnum.THIS_WEEK, (Long)query.uniqueResult());

		return map;
	}

	/**
	 * Find all the notes owned by {@code user} between from and to.
	 *
	 * @param user Owner of the notes we are searching for.
	 * @param from Beginning of dueDate. Can be null.
	 * @param to End of dueDate. Can be null
	 * @return List of notes.
	 */
	public List<Note> getNotesWithDueDateBetween(User user, Date from, Date to) {
		Criteria crit = criteria()
				.add(Restrictions.eq("user", user))
				.add(Restrictions.isNull("resolvedDate"))
				.addOrder(Order.asc("creationDate"));

		if (from != null)
			crit.add(Restrictions.ge("dueDate", from));
		if (to != null)
			crit.add(Restrictions.le("dueDate", to));
		return list(crit);
	}

	/**
	 * Return an associative array (key = noteId, value = categoryId) for all the notes owned by {@code user} with a due date between from and to.
	 *
	 * @param user Owner of the notes we are searching for.
	 * @param from Beginning of dueDate. Can be null.
	 * @param to End of dueDate. Can be null
	 * @return Associative array with key = noteId, value = catId.
	 */
	public Map<Long, Long> getCatIdByNoteIdMapDueDateBetween(User user, Date from, Date to) {
		Criteria crit = criteria()
				.setProjection(Projections.projectionList()
					.add(Projections.property("id"))
					.add(Projections.property("category.id")))
				.add(Restrictions.eq("user", user));

		if (from != null)
			crit.add(Restrictions.ge("dueDate", from));
		if (to != null)
			crit.add(Restrictions.le("dueDate", to));

		Map<Long, Long> map = new HashMap<Long, Long>();
		List<Object[]> result = listObjectArray(crit);
		for (Object[] res : result) {
			map.put((Long)res[0], (res[1] == null ? 0L : (Long)res[1]));
		}
		return map;
	}
}
