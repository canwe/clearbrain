package com.nilhcem.business;

import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nilhcem.core.hibernate.TransactionalReadOnly;
import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.dao.NoteDao;
import com.nilhcem.model.Note;
import com.nilhcem.model.User;

/**
 * Business class for accessing Note data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Service
@TransactionalReadOnly
public class NoteBo {
	@Autowired
	private NoteDao dao;
	private Logger logger = LoggerFactory.getLogger(NoteBo.class);

	/**
	 * Add a category in database and update last user's category to modify 'next' field.
	 *
	 * @param user Owner of the category.
	 * @param categoryName The name of the category we want to add.
	 * @return The new added category.
	 */
	@TransactionalReadWrite
	public Note addNote(User user, String noteName) {
		logger.debug("Add note {}", noteName);

		Note note = new Note();
		note.setName(noteName);
		note.setCategory(null); //TODO: Change this later
		note.setCreationDate(Calendar.getInstance().getTime());
		note.setUser(user);
		dao.save(note);

		return note;
	}
}
