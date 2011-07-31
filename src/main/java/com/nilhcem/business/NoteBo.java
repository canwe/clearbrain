package com.nilhcem.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nilhcem.core.hibernate.TransactionalReadOnly;
import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.dao.CategoryDao;
import com.nilhcem.dao.NoteDao;
import com.nilhcem.form.NoteForm;
import com.nilhcem.model.Note;
import com.nilhcem.model.User;
import com.nilhcem.util.CalendarFacade;

/**
 * Business class for accessing {@code Note} data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Service
@TransactionalReadOnly
public class NoteBo {
	@Autowired
	private NoteDao dao;
	@Autowired
	private CategoryDao catDao;
	@Autowired
	private CalendarFacade calendar;
	private final Logger logger = LoggerFactory.getLogger(NoteBo.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	/**
	 * Add a {@code Note} in database.
	 *
	 * @param user Owner of the note.
	 * @param noteName The name of the note we wanted to add.
	 * @param catId The category id of the note we wanted to add, 0 if null.
	 * @return The new added note.
	 */
	@TransactionalReadWrite
	public Note addNote(User user, String noteName, Long catId) {
		logger.debug("Add note {} in category {}", noteName, catId);

		Note note = new Note();
		note.setName(noteName);
		note.setCategory((catId.equals(Long.valueOf(0l))) ? null : catDao.getById(user, catId));
		note.setCreationDate(calendar.now());
		note.setUser(user);
		dao.save(note);

		return note;
	}

	/**
	 * Add or edit (if form.note.id != null) a {@code Note} using a noteForm.
	 * If form.editDueDate == "yes", set also dueDate
	 *
	 * @param user Owner of the note.
	 * @param form the form containing data to be saved.
	 */
	@TransactionalReadWrite
	public void addEditNote(User user, NoteForm form) {
		logger.debug("Add note {} in category {}", form.getNote().getName(), form.getCategoryId());
		Note note = form.getNote();

		//Category
		note.setCategory((form.getCategoryId().equals(Long.valueOf(0l))) ? null : catDao.getById(user, form.getCategoryId()));

		//Due date
		if (form.getEditDueDate().equals("yes")) {
			try {
				note.setDueDate(dateFormat.parse(form.getDueDate()));
			}
			catch (ParseException e) {
				logger.error("", e);
			}
		}

		if (note.getId() == null) { //add
			note.setCreationDate(calendar.now());
			note.setUser(user);
			dao.save(note);
		}
		else { //edit
			Note realNote = dao.getById(user, note.getId());
			realNote.setName(note.getName());
			realNote.setCategory(note.getCategory());
			realNote.setDueDate(note.getDueDate());
			dao.update(realNote);
		}
	}

	/**
	 * Find all the undone notes owned by {@code user} and sorted by creation date.
	 *
	 * @param user Owner of the notes we are searching for.
	 * @return List of notes.
	 */
	public List<Note> getUndoneNotes(User user) {
		return dao.getUndoneNotes(user, null, null);
	}

	/**
	 * Find all the undone notes owned by user which should be done today.
	 *
	 * @param user Owner of the notes we are searching for.
	 * @return List of notes.
	 */
	public List<Note> getUndoneNotesToday(User user) {
		Date today = calendar.getDateToday();
		return dao.getUndoneNotes(user, today, today);
	}

	/**
	 * Find all the notes owned by user which should be done before today.
	 *
	 * @param user Owner of the notes we are searching for.
	 * @return List of notes.
	 */
	public List<Note> getUndoneNotesMissed(User user) {
		Date yesterday = calendar.getDateYesterday();
		return dao.getUndoneNotes(user, null, yesterday);
	}

	/**
	 * Find all the notes owned by user which should be done tomorrow.
	 *
	 * @param user Owner of the notes we are searching for.
	 * @return List of notes.
	 */
	public List<Note> getUndoneNotesTomorrow(User user) {
		Date tomorrow = calendar.getDateTomorrow();
		return dao.getUndoneNotes(user, tomorrow, tomorrow);
	}

	/**
	 * Find all the notes owned by user which should be done this week.
	 *
	 * @param user Owner of the notes we are searching for.
	 * @return List of notes.
	 */
	public List<Note> getUndoneNotesWeek(User user) {
		return dao.getUndoneNotes(user, calendar.getDateToday(), calendar.getDateNextWeek());
	}

	/**
	 * Find all the done notes owned by {@code user} and sorted by creation date.
	 *
	 * @param user Owner of the notes we are searching for.
	 * @return List of notes.
	 */
	public List<Note> getDoneNotes(User user) {
		return dao.getDoneNotes(user, null, null, null);
	}

	/**
	 * Find all today's done notes owned by {@code user} and sorted by creation date.
	 *
	 * @param user Owner of the notes we are searching for.
	 * @return List of notes.
	 */
	public List<Note> getDoneNotesToday(User user) {
		Date today = calendar.getDateToday();
		return dao.getDoneNotes(user, null, today, today);
	}

	/**
	 * Find all tomorrow's done notes owned by {@code user} and sorted by creation date.
	 *
	 * @param user Owner of the notes we are searching for.
	 * @return List of notes.
	 */
	public List<Note> getDoneNotesTomorrow(User user) {
		Date tomorrow = calendar.getDateTomorrow();
		return dao.getDoneNotes(user, tomorrow, tomorrow, null);
	}

	/**
	 * Find all the week's done notes owned by {@code user} and sorted by creation date.
	 *
	 * @param user Owner of the notes we are searching for.
	 * @return List of notes.
	 */
	public List<Note> getDoneNotesWeek(User user) {
		return dao.getDoneNotes(user, calendar.getDateToday(), calendar.getDateNextWeek(), null);
	}

	/**
	 * Find a note from its {@code id}.
	 *
	 * @param user Owner of the note we are searching for.
	 * @param id Id of the note we are searching for.
	 * @return Note.
	 */
	public Note getNoteById(User user, Long id) {
		return dao.getById(user, id);
	}

	/**
	 * Return a Map for knowing which note belong to which category.
	 *
	 * @param user Owner of the notes we are searching for.
	 * @return Associative array with key = noteId, value = catId.
	 */
	public Map<Long, Long> getCatIdByNoteIdMap(User user) {
		return dao.getCatIdByNoteIdMapDueDateBetween(user, null, null);
	}

	/**
	 * Return a Map for knowing which note belong to which category for the Today page.
	 *
	 * @param user Owner of the notes we are searching for.
	 * @return Associative array with key = noteId, value = catId.
	 */
	public Map<Long, Long> getCatIdByNoteIdMapToday(User user) {
		return dao.getCatIdByNoteIdMapDueDateBetween(user, null, calendar.getDateToday());
	}

	/**
	 * Return a Map for knowing which note belong to which category for the Tomorrow page.
	 *
	 * @param user Owner of the notes we are searching for.
	 * @return Associative array with key = noteId, value = catId.
	 */
	public Map<Long, Long> getCatIdByNoteIdMapTomorrow(User user) {
		Date tomorrow = calendar.getDateTomorrow();
		return dao.getCatIdByNoteIdMapDueDateBetween(user, tomorrow, tomorrow);
	}

	/**
	 * Return a Map for knowing which note belong to which category for the Today page.
	 *
	 * @param user Owner of the notes we are searching for.
	 * @return Associative array with key = noteId, value = catId.
	 */
	public Map<Long, Long> getCatIdByNoteIdMapWeek(User user) {
		return dao.getCatIdByNoteIdMapDueDateBetween(user, calendar.getDateToday(), calendar.getDateNextWeek());
	}

	/**
	 * Delete a Note from its Id.
	 *
	 * @param user Owner of the notes / categories.
	 * @param noteId Id of the note we need to remove.
	 */
	@TransactionalReadWrite
	public void deleteNoteById(User user, Long noteId) {
		Note note = getNoteById(user, noteId);
		if (note != null) {
			dao.delete(note);
		}
	}

	/**
	 * Check or uncheck a note from its id.
	 *
	 * @param user Owner of the note.
	 * @param noteId Id of the note we need to remove.
	 * @param checked If <code>true</code> then mark the note as checked, other wise, mark it as unchecked.
	 */
	@TransactionalReadWrite
	public void checkUncheckNote(User user, Long noteId, boolean checked) {
		Note note = getNoteById(user, noteId);
		if (checked) {
			note.setResolvedDate(Calendar.getInstance().getTime());
		}
		else {
			note.setResolvedDate(null);
		}
		dao.update(note);
	}
}
