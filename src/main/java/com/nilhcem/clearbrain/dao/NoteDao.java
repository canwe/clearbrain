package com.nilhcem.clearbrain.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.Version;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nilhcem.clearbrain.core.hibernate.AbstractHibernateDao;
import com.nilhcem.clearbrain.enums.DashboardDateEnum;
import com.nilhcem.clearbrain.model.Note;
import com.nilhcem.clearbrain.model.User;
import com.nilhcem.clearbrain.util.CalendarFacade;

/**
 * Provides methods for accessing {@code Note} data from database.
 *
 * @author Nilhcem
 * @since 1.0
 * @see Note
 */
@Repository
public final class NoteDao extends AbstractHibernateDao<Note> {
	private final String[] searchFields = new String[] {"name", "category.name"};

	@Autowired
	private CalendarFacade calendar;

	@Autowired
	public NoteDao(SessionFactory sessionFactory) {
		super(Note.class, sessionFactory);
	}

	/**
	 * Finds a note from its {@code id}.
	 *
	 * @param user the owner of the note.
	 * @param id the id of the note we are searching for.
	 * @return the note we need to search, or {@code null} if not found.
	 */
	public Note getById(User user, Long id) {
		Query query = query("FROM Note WHERE user=:user AND id=:id")
			.setParameter("user", user)
			.setParameter("id", id)
			.setMaxResults(1);
		return uniqueResult(query);
	}

	/**
	 * Counts the number of tasks to do for today, tomorrow and this week for the user specified in parameter.
	 *
	 * @param user the owner of the note which will be counted.
	 * @return a map where the {@code key} represents the period (today / tomorrow / week), and the {@code value} represents the number of tasks to do for this period.
	 */
	public Map<DashboardDateEnum, Long> getNbTaskTodoHeader(User user) {
		// Set variables
		Map<DashboardDateEnum, Long> map = new HashMap<DashboardDateEnum, Long>();
		Date today = calendar.getDateTodayWithoutTime();
		Date tomorrow = calendar.getDateTomorrowWithoutTime();
		Date endWeek = calendar.getDateNextWeekWithoutTime();

		// Nb tasks for today
		Query query = query("SELECT COUNT(id) FROM Note WHERE user = :user AND resolvedDate IS NULL AND dueDate <= :today")
			.setParameter("user", user)
			.setParameter("today", today)
			.setMaxResults(1);
		map.put(DashboardDateEnum.TODAY, (Long) query.uniqueResult());

		// Nb tasks for tomorrow
		query = query("SELECT COUNT(id) FROM Note WHERE user = :user AND resolvedDate IS NULL AND dueDate = :tomorrow")
			.setParameter("user", user)
			.setParameter("tomorrow", tomorrow)
			.setMaxResults(1);
		map.put(DashboardDateEnum.TOMORROW, (Long) query.uniqueResult());

		// Nb tasks for this week
		query = query("SELECT COUNT(id) FROM Note WHERE user = :user AND resolvedDate IS NULL AND dueDate >= :today AND dueDate <= :endWeek")
			.setParameter("user", user)
			.setParameter("today", today)
			.setParameter("endWeek", endWeek)
			.setMaxResults(1);
		map.put(DashboardDateEnum.THIS_WEEK, (Long) query.uniqueResult());

		return map;
	}

	/**
	 * Finds all the notes which are not done yet owned by a specific user and a due date between the dates passed in parameters.
	 *
	 * @param user the owner of the notes.
	 * @param from the beginning of the due date, can be {@code null}.
	 * @param to the end of the due date, can be {@code null}.
	 * @return a list of notes which are not done yet.
	 */
	public List<Note> getUndoneNotes(User user, Date from, Date to) {
		Criteria crit = criteria()
				.add(Restrictions.eq("user", user))
				.add(Restrictions.isNull("resolvedDate"))
				.addOrder(Order.asc("creationDate"));

		if (from != null) {
			crit.add(Restrictions.ge("dueDate", from));
		}
		if (to != null) {
			crit.add(Restrictions.le("dueDate", to));
		}
		return list(crit);
	}

	/**
	 * Finds all the notes already done, sorted by creation date having a due date owned by a specific user and a due date between the dates passed in parameters.
	 *
	 * @param user the owner of the notes.
	 * @param from the beginning of the due date, can be {@code null}.
	 * @param to the end of the due date, can be {@code null}.
	 * @param resolvedDate the resolved date. If {@code null}, then no specific resolved date (but still searching in the "resolved" tasks).
	 * @return a list of notes which are already done.
	 */
	public List<Note> getDoneNotesBetweenDueDate(User user, Date from, Date to, Date resolvedDate) {
		Criteria crit = criteria()
				.add(Restrictions.eq("user", user))
				.add(Restrictions.isNotNull("resolvedDate"))
				.addOrder(Order.asc("creationDate"));

		if (resolvedDate != null) {
			crit.add(Restrictions.ge("resolvedDate", resolvedDate));
			crit.add(Restrictions.lt("resolvedDate", calendar.getDateAfter(resolvedDate)));
		}
		if (from != null) {
			crit.add(Restrictions.ge("dueDate", from));
		}
		if (to != null) {
			crit.add(Restrictions.le("dueDate", to));
		}
		return list(crit);
	}

	/**
	 * Returns a map of categories where the {@code key} is the note's id, and the {@code value} is the category's id, between the two dates passed in parameters.
	 *
	 * @param user the owner of the notes.
	 * @param from the beginning of the due date, can be {@code null}.
	 * @param to the end of the due date, can be {@code null}.
	 * @return a map of categories where the {@code key} is the note's id, and the {@code value} is the category's id.
	 */
	public Map<Long, Long> getCatIdByNoteIdMapDueDateBetween(User user, Date from, Date to) {
		Criteria crit = criteria()
				.setProjection(Projections.projectionList()
					.add(Projections.property("id"))
					.add(Projections.property("category.id")))
				.add(Restrictions.eq("user", user));

		if (from != null) {
			crit.add(Restrictions.ge("dueDate", from));
		}
		if (to != null) {
			crit.add(Restrictions.le("dueDate", to));
		}

		Map<Long, Long> map = new HashMap<Long, Long>();
		List<Object[]> result = listObjectArray(crit);
		for (Object[] res : result) {
			map.put((Long) res[0], (res[1] == null ? 0L : (Long) res[1]));
		}
		return map;
	}

	/**
	 * Returns a map of categories where the {@code key} is the note's id, and the {@code value} is the category's id, for all dates resolved today.
	 *
	 * @param user the owner of the notes.
	 * @return a map of categories where the {@code key} is the note's id, and the {@code value} is the category's id.
	 */
	public Map<Long, Long> getCatIdByNoteIdMapResolvedToday(User user) {
		Criteria crit = criteria()
				.setProjection(Projections.projectionList()
					.add(Projections.property("id"))
					.add(Projections.property("category.id")))
				.add(Restrictions.eq("user", user))
				.add(Restrictions.ge("resolvedDate", calendar.getDateTodayWithoutTime()));

		Map<Long, Long> map = new HashMap<Long, Long>();
		List<Object[]> result = listObjectArray(crit);
		for (Object[] res : result) {
			map.put((Long) res[0], (res[1] == null ? 0L : (Long) res[1]));
		}
		return map;
	}

	/**
	 * Finds a note matching the pattern in parameter.
	 *
	 * @param search a string representing a part of the note's content.
	 * @return a list of notes, or an empty list of notes, if no note found.
	 */
	@SuppressWarnings("unchecked")
	public List<Note> searchNote(String search) {
		// Create search session
		FullTextSession searchSession = Search.getFullTextSession(currentSession());

		// Create parser
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_31, searchFields, analyzer);

		// Full text query
		FullTextQuery query;

		try {
			query = searchSession.createFullTextQuery(parser.parse(search), Note.class);
		} catch (ParseException pe) {
			return new ArrayList<Note>(0);
		}
		return query.list();
	}

	/**
	 * Returns the first note created by the user specified in parameter.
	 *
	 * @param user the owner of the note.
	 * @return the first note of a user, or {@code null} if no note has been created yet.
	 */
	public Note getFirstNote(User user) {
		Query query = query("FROM Note WHERE creationDate=(SELECT MIN(creationDate) FROM Note WHERE user = :user) AND user = :user")
				.setParameter("user", user)
				.setMaxResults(1);
		return uniqueResult(query);
	}

	/**
	 * Returns all the notes of a user between two dates.
	 *
	 * @param user the owner of the notes.
	 * @param from the begin of the range.
	 * @param to the end of the range.
	 * @return a list of notes created by the user, or an empty list if no note were created yet.
	 */
	public List<Note> getAllNotes(User user, Date from, Date to) {
		Criteria crit = criteria().add(Restrictions.eq("user", user));

		if (from != null) {
			crit.add(Restrictions.ge("creationDate", from));
		}
		if (to != null) {
			crit.add(Restrictions.le("creationDate", to));
		}
		return list(crit);
	}

	/**
	 * Returns a list of notes marked as "resolved" between two dates.
	 *
	 * @param user the owner of the notes.
	 * @param from the begin of the range.
	 * @param to the end of the range.
	 * @return a list of notes marked as "resolved", or an empty list if no note found.
	 */
	public List<Note> getDoneNotesBetweenResolvedDate(User user, Date from, Date to) {
		Criteria crit = criteria()
				.add(Restrictions.eq("user", user))
				.add(Restrictions.isNotNull("resolvedDate"));

		if (from != null) {
			crit.add(Restrictions.ge("resolvedDate", from));
		}
		if (to != null) {
			crit.add(Restrictions.le("resolvedDate", to));
		}
		return list(crit);
	}
}
