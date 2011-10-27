package com.nilhcem.clearbrain.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nilhcem.clearbrain.core.hibernate.TransactionalReadOnly;
import com.nilhcem.clearbrain.core.hibernate.TransactionalReadWrite;
import com.nilhcem.clearbrain.dao.CategoryDao;
import com.nilhcem.clearbrain.dao.NoteDao;
import com.nilhcem.clearbrain.form.NoteForm;
import com.nilhcem.clearbrain.model.Note;
import com.nilhcem.clearbrain.model.User;
import com.nilhcem.clearbrain.util.CalendarFacade;

/**
 * Provides methods for dealing with {@code Note} objects.
 *
 * @author Nilhcem
 * @since 1.0
 * @see Note
 */
@Service
@TransactionalReadOnly
public /*no final*/ class NoteBo {
	@Autowired
	private NoteDao dao;
	@Autowired
	private CategoryDao catDao;
	@Autowired
	private CalendarFacade calendar;
	private final Logger logger = LoggerFactory.getLogger(NoteBo.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	/**
	 * Inserts a {@code Note} in database.
	 *
	 * @param user the owner of the note.
	 * @param noteName The name of the note we want to add.
	 * @param catId the id of the category which will contain the note, "{@code 0L}" for no category.
	 * @return the new added note.
	 */
	@TransactionalReadWrite
	public Note addNote(User user, String noteName, Long catId) {
		logger.debug("Add note {} in category {}", noteName, catId);

		Note note = new Note();
		note.setName(noteName);
		note.setCategory((catId.equals(Long.valueOf(0L))) ? null : catDao.getById(user, catId));
		note.setCreationDate(calendar.now());
		note.setUser(user);
		dao.save(note);

		return note;
	}

	/**
	 * Inserts or edits an already existing note, using information from a {@code NoteForm} object.
	 * <p>
	 * if "form.note.id" is {@code null}, the method will create a new note,
	 * otherwise it will edit the already existing note using the new information.<br />
	 * If "form.editDueDate" equals to {@code "yes"}, sets a due date to the note.
	 * </p>
	 *
	 * @param user the owner of the note.
	 * @param form the form inputed by the user, containing the new note's data.
	 * @see NoteForm
	 */
	@TransactionalReadWrite
	public void addEditNote(User user, NoteForm form) {
		logger.debug("Add note {} in category {}", form.getNote().getName(), form.getCategoryId());
		Note note = form.getNote();

		// Category
		note.setCategory((form.getCategoryId().equals(Long.valueOf(0L))) ? null : catDao.getById(user, form.getCategoryId()));

		// Due date
		if (form.getEditDueDate().equals("yes")) {
			try {
				note.setDueDate(dateFormat.parse(form.getDueDate()));
			} catch (ParseException e) {
				logger.error("", e);
			}
		}

		if (note.getId() == null) { // Add
			note.setCreationDate(calendar.now());
			note.setUser(user);
			dao.save(note);
		} else { // Edit
			Note realNote = dao.getById(user, note.getId());
			realNote.setName(note.getName());
			realNote.setCategory(note.getCategory());
			realNote.setDueDate(note.getDueDate());
			dao.update(realNote);
		}
	}

	/**
	 * Returns a list of all the user's notes which are note done yet.
	 * <p>
	 * The list is sorted by creation date.
	 * </p>
	 *
	 * @param user the owner of the notes.
	 * @return a list of undone notes, sorted by creation date.
	 */
	public List<Note> getUndoneNotes(User user) {
		return dao.getUndoneNotes(user, null, null);
	}

	/**
	 * Returns a list of notes which should be done for today (and are not done yet).
	 * <p>
	 * The list is sorted by creation date.
	 * </p>
	 *
	 * @param user the owner of the notes.
	 * @return a list of notes which should be done for today and are still not done yet.
	 */
	public List<Note> getUndoneNotesToday(User user) {
		Date today = calendar.getDateTodayWithoutTime();
		return dao.getUndoneNotes(user, today, today);
	}

	/**
	 * Returns a list of notes which should have been done before today (and are still not done yet).
	 * <p>
	 * The list is sorted by creation date.
	 * </p>
	 *
	 * @param user the owner of the notes.
	 * @return a list of "missed" notes, which should have been done before today and are still not done yet.
	 */
	public List<Note> getUndoneNotesMissed(User user) {
		Date yesterday = calendar.getDateYesterdayWithoutTime();
		return dao.getUndoneNotes(user, null, yesterday);
	}

	/**
	 * Returns a list of notes which should be done for tomorrow (and are not done yet).
	 * <p>
	 * The list is sorted by creation date.
	 * </p>
	 *
	 * @param user the owner of the notes.
	 * @return a list of notes which should be done for tomorrow and are still not done yet.
	 */
	public List<Note> getUndoneNotesTomorrow(User user) {
		Date tomorrow = calendar.getDateTomorrowWithoutTime();
		return dao.getUndoneNotes(user, tomorrow, tomorrow);
	}

	/**
	 * Returns a list of notes which should be done for this week (and are not done yet).
	 * <p>
	 * "This week" means from today to six days after today.<br />
	 * The list is sorted by creation date.
	 * </p>
	 *
	 * @param user the owner of the notes.
	 * @return a list of notes which should be done for tomorrow and are still not done yet.
	 */
	public List<Note> getUndoneNotesWeek(User user) {
		return dao.getUndoneNotes(user, calendar.getDateTodayWithoutTime(), calendar.getDateNextWeekWithoutTime());
	}

	/**
	 * Returns a list of all the user's notes which are already done.
	 * <p>
	 * The list is sorted by creation date.
	 * </p>
	 *
	 * @param user the owner of the notes.
	 * @return a list of notes which are already done, sorted by creation date.
	 */
	public List<Note> getDoneNotes(User user) {
		return dao.getDoneNotesBetweenDueDate(user, null, null, null);
	}

	/**
	 * Returns a list of notes which are done today.
	 * <p>
	 * The list is sorted by creation date.
	 * </p>
	 *
	 * @param user the owner of the notes.
	 * @return a list of notes which should be, and are already done for today.
	 */
	public List<Note> getDoneNotesToday(User user) {
		return dao.getDoneNotesBetweenResolvedDate(user, calendar.getDateTodayWithoutTime(), calendar.getDateTomorrowWithoutTime());
	}

	/**
	 * Returns a list of notes which should be, and are already done for tomorrow.
	 * <p>
	 * The list is sorted by creation date.
	 * </p>
	 *
	 * @param user the owner of the notes.
	 * @return a list of notes which should be, and are already done for tomorrow.
	 */
	public List<Note> getDoneNotesTomorrow(User user) {
		Date tomorrow = calendar.getDateTomorrowWithoutTime();
		return dao.getDoneNotesBetweenDueDate(user, tomorrow, tomorrow, null);
	}

	/**
	 * Returns a list of notes which should be, and are already done for this week.
	 * <p>
	 * "This week" means from today to six days after today.<br />
	 * The list is sorted by creation date.
	 * </p>
	 *
	 * @param user the owner of the notes.
	 * @return a list of notes which should be, and are already done for today.
	 */
	public List<Note> getDoneNotesWeek(User user) {
		return dao.getDoneNotesBetweenDueDate(user, calendar.getDateTodayWithoutTime(), calendar.getDateNextWeekWithoutTime(), null);
	}

	/**
	 * Finds a note from its id.
	 *
	 * @param user the owner of the note.
	 * @param id the id of the note we are searching for.
	 * @return the note whose id is the one specified in parameter.
	 */
	public Note getNoteById(User user, Long id) {
		return dao.getById(user, id);
	}

	/**
	 * Returns a map to know which note from the user specified in parameter belong to which category.
	 *
	 * @param user the owner of the notes and categories.
	 * @return a map where the {@code key} is the note's id, and the {@code value} is the category's id.
	 */
	public Map<Long, Long> getCatIdByNoteIdMap(User user) {
		return dao.getCatIdByNoteIdMapDueDateBetween(user, null, null);
	}

	/**
	 * Returns a map to know which note from the list specified in parameter belong to which category.
	 *
	 * @param notes a list of notes as reference.
	 * @return a map where the {@code key} is the note's id, and the {@code value} is the category's id.
	 */
	public Map<Long, Long> getCatIdByNoteIdMap(List<Note> notes) {
		Map<Long, Long> map = new HashMap<Long, Long>();

		for (Note note : notes) {
			map.put(note.getId(), (note.getCategory() == null ? 0L : note.getCategory().getId()));
		}
		return map;
	}

	/**
	 * Returns a map to know which note belong to which category, for "today"'s dashboard.
	 *
	 * @param user the owner of the notes and categories.
	 * @return a map where the {@code key} is the note's id, and the {@code value} is the category's id.
	 */
	public Map<Long, Long> getCatIdByNoteIdMapToday(User user) {
		return dao.getCatIdByNoteIdMapDueDateBetween(user, null, calendar.getDateTodayWithoutTime());
	}

	/**
	 * Returns a map to know which note belong to which category, for "tomorrow"'s dashboard.
	 *
	 * @param user the owner of the notes and categories.
	 * @return a map where the {@code key} is the note's id, and the {@code value} is the category's id.
	 */
	public Map<Long, Long> getCatIdByNoteIdMapTomorrow(User user) {
		Date tomorrow = calendar.getDateTomorrowWithoutTime();
		return dao.getCatIdByNoteIdMapDueDateBetween(user, tomorrow, tomorrow);
	}

	/**
	 * Returns a map to know which note belong to which category, for "this week"'s dashboard.
	 *
	 * @param user the owner of the notes and categories.
	 * @return a map where the {@code key} is the note's id, and the {@code value} is the category's id.
	 */
	public Map<Long, Long> getCatIdByNoteIdMapWeek(User user) {
		return dao.getCatIdByNoteIdMapDueDateBetween(user, calendar.getDateTodayWithoutTime(), calendar.getDateNextWeekWithoutTime());
	}

	/**
	 * Deletes a Note from its id.
	 *
	 * @param user the owner of the note.
	 * @param noteId the id of the note we want to remove.
	 */
	@TransactionalReadWrite
	public void deleteNoteById(User user, Long noteId) {
		Note note = getNoteById(user, noteId);
		if (note != null) {
			dao.delete(note);
		}
	}

	/**
	 * Marks a note as done (checked) or not (unchecked).
	 *
	 * @param user the owner of the note.
	 * @param noteId the id of the note which will be marked as done / not done.
	 * @param checked a boolean to know if the note will be checked ({@code true}) or unchecked ({@code false}).
	 */
	@TransactionalReadWrite
	public void checkUncheckNote(User user, Long noteId, boolean checked) {
		Note note = getNoteById(user, noteId);
		if (checked) {
			note.setResolvedDate(Calendar.getInstance().getTime());
		} else {
			note.setResolvedDate(null);
		}
		dao.update(note);
	}

	/**
	 * Finds a note matching the pattern in parameter.
	 *
	 * @param user the owner of the notes.
	 * @param search a string representing a part of the note's content.
	 * @return a list of notes, or an empty list of notes, if no note found.
	 */
	public List<Note> searchNote(User user, String search) {
		logger.debug("Search {}", search);

		List<Note> notes = new ArrayList<Note>();
		List<Note> found = dao.searchNote(search);

		// TODO: Dirty. Find a better way later.
		for (Note note : found) {
			if (note.getUser().getId().equals(user.getId())) {
				notes.add(note);
			}
		}
		return notes;
	}

	/**
	 * Returns the creation date of the first note from the user specified in parameter.
	 *
	 * @param user owner of the note.
	 * @return a date object representing the creation date of the first note created by the user specified in parameters.
	 */
	public Date getDateOfFirstNote(User user) {
		Note firstNote = dao.getFirstNote(user);

		if (firstNote == null) {
			return null;
		}

		return firstNote.getCreationDate();
	}

	/**
	 * Creates a <b>sorted</b> {@code LinkedHashMap} and sets all the key with every day between from and to.
	 * The value for every key will be {@code 0}.
	 * <p>
	 * If we call the function with from = 2011-04-05 and to = 2011-04-08, the map will have the following content:<br />
	 * [2011-04-05] = 0L<br />
	 * [2011-04-06] = 0L<br />
	 * [2011-04-07] = 0L<br />
	 * [2011-04-08] = 0L<br />
	 * </p>
	 *
	 * @param from the first date.
	 * @param to the last date which will be in the map.
	 * @return a sorted map containing all the dates between from and to, with the value 0L for each key.
	 */
	private Map<String, Long> createMapOfDaysFromTo(Date from, Date to) {
		LinkedHashMap<String, Long> map = new LinkedHashMap<String, Long>();

		if (from.before(to)) {
			Date currentDate = calendar.getDateWithoutTime(from);

			while (to.after(currentDate)) {
				map.put(dateFormat.format(currentDate), 0L);
				currentDate = calendar.getDateAfter(currentDate);
			}
		}
		return map;
	}

	/**
	 * Returns a list representing the number of notes created or resolved by the user between two dates.
	 *
	 * @param user owner of the notes.
	 * @param from begin of the range.
	 * @param to end of the range.
	 * @param onlyDoneNotes if {@code true} then will return the number of resolved notes, for each day between the range from/to.<br />
	 * if {@code false} then the method will return the number of notes created by the user, for each day between the range from/to.
	 * @return a list representing the number of notes creates or resolved.
	 */
	public List<Long> getNbOfNotesForEachDay(User user, Date from, Date to, boolean onlyDoneNotes) {
		Map<String, Long> map = createMapOfDaysFromTo(from, to);

		if (!map.isEmpty()) {
			// Fill Map
			List<Note> notes = (onlyDoneNotes ? dao.getDoneNotesBetweenResolvedDate(user, from, to) : dao.getAllNotes(user, from, to));

			// Browse all notes to count the number of created/resolved notes by day
			String curDate = null;
			for (Note note : notes) {
				curDate = (onlyDoneNotes ? (note.getResolvedDate() == null ? null : dateFormat.format(note.getResolvedDate()))
						: (dateFormat.format(note.getCreationDate())));
				if (curDate != null) {
					map.put(curDate, map.get(curDate) + 1);
				}
			}
		}
		return new ArrayList<Long>(map.values());
	}
}
