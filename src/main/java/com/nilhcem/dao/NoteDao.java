package com.nilhcem.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.nilhcem.core.hibernate.AbstractHibernateDao;
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
		Query query = query("FROM Note WHERE user=:user")
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
	 * Return an associative array (key = noteId, value = categoryId) for all the notes owned by {@code user}.
	 *
	 * @param user Owner of the notes we are searching for.
	 * @return Associative array with key = noteId, value = catId.
	 */
	public Map<Long, Long> getCatIdByNoteIdMap(User user) {
		Query query = query("SELECT n.id, n.category.id FROM Note n WHERE user=:user")
			.setParameter("user", user);

		Map<Long, Long> map = new HashMap<Long, Long>();
		List<Object[]> result = listObjectArray(query);
		for (Object[] res : result) {
			map.put((Long)res[0], (res[1] == null ? 0L : (Long)res[1]));
		}
		return map;
	}
}
