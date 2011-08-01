package com.nilhcem.business;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.core.test.AbstractDbTest;
import com.nilhcem.form.NoteForm;
import com.nilhcem.model.Category;
import com.nilhcem.model.Note;
import com.nilhcem.model.User;
import com.nilhcem.util.CalendarFacade;

public class NoteBoTest extends AbstractDbTest {
	private static final Long zero = Long.valueOf(0l);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private static final Logger logger = LoggerFactory.getLogger(NoteBoTest.class);

	@Autowired
	private NoteBo noteBo;
	@Autowired
	private CategoryBo categoryBo;
	@Autowired
	private CalendarFacade cal;

	@Test
	@TransactionalReadWrite
	public void testAddNoteWithoutCategory() {
		final String noteName = "My Note";
		User user = testUtils.createTestUser("NoteBoTest@testAddNoteWithoutCategory");
		logger.debug("Add note with no category");
		Date before = testUtils.getDateBeforeTest();
		Note note = noteBo.addNote(user, noteName, zero);
		Date after = testUtils.getDateAfterTest();

		assertEquals(noteName, note.getName());
		assertNull(note.getCategory());
		assertNull(note.getResolvedDate());
		assertTrue(testUtils.checkDateBetween(note.getCreationDate(), before, after));
		assertEquals(user.getId(), note.getUser().getId());
	}

	@Test
	@TransactionalReadWrite
	public void testAddNoteWithCategory() {
		final String categoryName = "My Category";
		User user = testUtils.createTestUser("NoteBoTest@testAddNoteWithCategory");
		logger.debug("Test add note with category: Add category");
		Category category = categoryBo.addCategory(user, categoryName);
		logger.debug("Test add note with category: Add note");
		Note note = noteBo.addNote(user, "Note name", category.getId());
		Note newNote = noteBo.getNoteById(user, note.getId());
		assertEquals(category.getId(), newNote.getCategory().getId());
	}

	@Test
	@TransactionalReadWrite
	public void testAddEditNoteAddNoCatNoDueDate() {
		final String noteName = "Note name";
		User user = testUtils.createTestUser("NoteBoTest@testAddEditNoteAddNoCatNoDueDate");
		Note note = new Note();
		note.setId(null);
		note.setName(noteName);
		NoteForm form = new NoteForm();
		form.setCategoryId(zero);
		form.setNote(note);
		form.setEditDueDate("no");
		form.setDueDate("05/15/2088");

		logger.debug("test add edit note (add) : Add note");
		Date before = testUtils.getDateBeforeTest();
		noteBo.addEditNote(user, form);
		Date after = testUtils.getDateAfterTest();

		Note newNote = noteBo.getNoteById(user, note.getId());
		assertNull(newNote.getResolvedDate());
		assertNull(newNote.getCategory());
		assertNull(newNote.getDueDate());
		assertEquals(noteName, newNote.getName());
		assertTrue(testUtils.checkDateBetween(note.getCreationDate(), before, after));
	}

	@Test
	@TransactionalReadWrite
	public void testAddEditNotWithWrongDueDate() {
		final String noteName = "Note name";
		User user = testUtils.createTestUser("NoteBoTest@testAddEditNotWithWrongDueDate");
		Note note = new Note();
		note.setId(null);
		note.setName(noteName);
		NoteForm form = new NoteForm();
		form.setCategoryId(zero);
		form.setNote(note);
		form.setEditDueDate("yes");
		form.setDueDate("OMG");

		logger.debug("test add edit note with wrong due date");
		noteBo.addEditNote(user, form);

		Note newNote = noteBo.getNoteById(user, note.getId());
		assertNull(newNote.getDueDate());
	}

	@Test
	@TransactionalReadWrite
	public void testAddEditNoteAddCatAndDueDate() {
		final String dueDateStr = "05/15/2088";
		User user = testUtils.createTestUser("NoteBoTest@testAddEditNoteAddCatAndDueDate");
		logger.debug("Add/edit note (add cat and due date): Add category");
		Category category = categoryBo.addCategory(user, "My category");

		Note note = new Note();
		note.setId(null);
		note.setName("Note name");
		NoteForm form = new NoteForm();
		form.setCategoryId(category.getId());
		form.setNote(note);
		form.setEditDueDate("yes");
		form.setDueDate(dueDateStr);

		logger.debug("Add/edit note (add cat and due date): Add note");
		noteBo.addEditNote(user, form);

		Note newNote = noteBo.getNoteById(user, note.getId());
		assertEquals(category.getId(), newNote.getCategory().getId());
		assertEquals(dueDateStr, dts(newNote.getDueDate()));
	}

	@Test
	@TransactionalReadWrite
	public void testAddEditNoteEdit() {
		final String noteName = "My Note Name";
		final String noteNewName = "My Note New Name";
		final String dueDateStr = "05/15/2088";
		assertFalse(noteName.equals(noteNewName));
		User user = testUtils.createTestUser("NoteBoTest@testAddEditNoteEdit");

		logger.debug("Add/edit note (edit): Add note");
		Note note = noteBo.addNote(user, noteName, zero);
		Date creationDate = note.getCreationDate();

		logger.debug("Add/edit note (edit): Add category");
		Category category = categoryBo.addCategory(user, "My category");

		NoteForm form = new NoteForm();
		form.setNote(note);
		form.getNote().setName(noteNewName);
		form.setCategoryId(category.getId());
		form.setEditDueDate("yes");
		form.setDueDate(dueDateStr);

		logger.debug("Add/edit note (edit): Edit note");
		noteBo.addEditNote(user, form);

		Note editedNote = noteBo.getNoteById(user, note.getId());
		assertEquals(noteNewName, editedNote.getName());
		assertEquals(category.getId(), editedNote.getCategory().getId());
		assertEquals(dueDateStr, dts(editedNote.getDueDate()));
		assertEquals(creationDate, editedNote.getCreationDate());
	}

	@Test
	@TransactionalReadWrite
	public void testGetUndoneNotes() {
		User user = testUtils.createTestUser("NoteBoTest@testGetUndoneNotes");

		createNote(user, false, null, zero); //yes
		createNote(user, false, "05/15/2088", zero); //yes
		createNote(user, false, null, zero); //yes
		createNote(user, true, null, zero); //no
		createNote(user, false, dts(cal.getDateNextWeek()), zero); //yes

		assertEquals(4, noteBo.getUndoneNotes(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testGetUndoneNotesToday() {
		User user = testUtils.createTestUser("NoteBoTest@testGetUndoneNotesToday");

		createNote(user, false, null, zero); //no
		createNote(user, false, dts(cal.getDateYesterday()), zero); //no
		createNote(user, false, dts(cal.getDateToday()), zero); //yes
		createNote(user, false, dts(cal.getDateToday()), zero); //yes
		createNote(user, true, dts(cal.getDateToday()), zero); //no
		createNote(user, false, dts(cal.getDateTomorrow()), zero); //no
		createNote(user, false, dts(cal.getDateNextWeek()), zero); //no

		assertEquals(2, noteBo.getUndoneNotesToday(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testGetUndoneNotesMissed() {
		User user = testUtils.createTestUser("NoteBoTest@testGetUndoneNotesMissed");

		createNote(user, false, null, zero); //no
		createNote(user, false, dts(cal.getCustomDateFromToday(-20)), zero); //yes
		createNote(user, false, dts(cal.getCustomDateFromToday(-2)), zero); //yes
		createNote(user, false, dts(cal.getDateYesterday()), zero); //yes
		createNote(user, true, dts(cal.getDateYesterday()), zero); //no
		createNote(user, false, dts(cal.getDateToday()), zero); //no
		createNote(user, true, dts(cal.getDateToday()), zero); //no
		createNote(user, false, dts(cal.getDateTomorrow()), zero); //no
		createNote(user, false, dts(cal.getDateNextWeek()), zero); //no

		assertEquals(3, noteBo.getUndoneNotesMissed(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testGetUndoneNotesTomorrow() {
		User user = testUtils.createTestUser("NoteBoTest@testGetUndoneNotesTomorrow");

		createNote(user, false, null, zero); //no
		createNote(user, false, dts(cal.getDateYesterday()), zero); //no
		createNote(user, false, dts(cal.getDateToday()), zero); //no
		createNote(user, true, dts(cal.getDateTomorrow()), zero); //no
		createNote(user, false, dts(cal.getDateTomorrow()), zero); //yes
		createNote(user, false, dts(cal.getDateTomorrow()), zero); //yes
		createNote(user, false, dts(cal.getDateTomorrow()), zero); //yes
		createNote(user, false, dts(cal.getDateNextWeek()), zero); //no

		assertEquals(3, noteBo.getUndoneNotesTomorrow(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testGetUndoneNotesWeek() {
		User user = testUtils.createTestUser("NoteBoTest@testGetUndoneNotesWeek");

		createNote(user, false, null, zero); //no
		createNote(user, false, dts(cal.getDateYesterday()), zero); //no
		createNote(user, false, dts(cal.getDateToday()), zero); //yes
		createNote(user, false, dts(cal.getDateTomorrow()), zero); //yes
		createNote(user, false, dts(cal.getDateNextWeek()), zero); //yes
		createNote(user, false, dts(cal.getCustomDateFromToday(8)), zero); //no
		createNote(user, false, dts(cal.getCustomDateFromToday(10)), zero); //no

		assertEquals(3, noteBo.getUndoneNotesWeek(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testGetDoneNotes() {
		User user = testUtils.createTestUser("NoteBoTest@testGetDoneNotes");

		createNote(user, true, null, zero); //yes
		createNote(user, true, "05/15/2088", zero); //yes
		createNote(user, true, dts(cal.getDateYesterday()), zero); //yes
		createNote(user, false, null, zero); //no
		createNote(user, false, dts(cal.getDateNextWeek()), zero); //no

		assertEquals(3, noteBo.getDoneNotes(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testGetDoneNotesToday() {
		User user = testUtils.createTestUser("NoteBoTest@testGetDoneNotesToday");

		createNote(user, true, null, zero); //no
		createNote(user, true, dts(cal.getCustomDateFromToday(-4)), zero); //yes [tricky] : even if missed due date, it's done today.
		createNote(user, true, dts(cal.getDateYesterday()), zero); //yes
		createNote(user, true, dts(cal.getDateToday()), zero); //yes
		createNote(user, true, dts(cal.getDateTomorrow()), zero); //no
		createNote(user, false, dts(cal.getDateToday()), zero); //no
		createNote(user, false, dts(cal.getDateTomorrow()), zero); //no
		createNote(user, false, dts(cal.getDateNextWeek()), zero); //no

		assertEquals(3, noteBo.getDoneNotesToday(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testGetDoneNotesTomorrow() {
		User user = testUtils.createTestUser("NoteBoTest@testGetDoneNotesTomorrow");

		createNote(user, true, null, zero); //no
		createNote(user, true, dts(cal.getDateYesterday()), zero); //no
		createNote(user, true, dts(cal.getDateToday()), zero); //no
		createNote(user, false, dts(cal.getDateTomorrow()), zero); //no
		createNote(user, true, dts(cal.getDateTomorrow()), zero); //yes
		createNote(user, true, dts(cal.getDateTomorrow()), zero); //yes
		createNote(user, true, dts(cal.getDateTomorrow()), zero); //yes
		createNote(user, true, dts(cal.getDateNextWeek()), zero); //no

		assertEquals(3, noteBo.getDoneNotesTomorrow(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testGetDoneNotesWeek() {
		User user = testUtils.createTestUser("NoteBoTest@testGetDoneNotesWeek");

		createNote(user, true, null, zero); //no
		createNote(user, true, dts(cal.getDateYesterday()), zero); //no
		createNote(user, true, dts(cal.getDateToday()), zero); //yes
		createNote(user, true, dts(cal.getDateTomorrow()), zero); //yes
		createNote(user, true, dts(cal.getDateNextWeek()), zero); //yes
		createNote(user, true, dts(cal.getCustomDateFromToday(8)), zero); //no
		createNote(user, true, dts(cal.getCustomDateFromToday(10)), zero); //no

		assertEquals(3, noteBo.getDoneNotesWeek(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testGetNoteById() {
		User user = testUtils.createTestUser("NoteBoTest@testGetNoteById");
		assertNull(noteBo.getNoteById(user, 42L));

		logger.debug("Get note by id: Add note");
		Note note = noteBo.addNote(user, "NoteName", 0L);
		assertEquals(note.getId(), noteBo.getNoteById(user, note.getId()).getId());
	}

	@Test
	@TransactionalReadWrite
	public void testGetCatIdByNoteIdMap() {
		User user = testUtils.createTestUser("NoteBoTest@testGetCatIdByNoteIdMap");
		Category catA = categoryBo.addCategory(user, "A");
		Category catB = categoryBo.addCategory(user, "B");
		Category catC = categoryBo.addCategory(user, "C");

		Note noteA = createNote(user, true, null, zero);
		Note noteB = createNote(user, false, null, catA.getId());
		Note noteC = createNote(user, true, null, catA.getId());
		Note noteD = createNote(user, false, null, catC.getId());
		Note noteE = createNote(user, true, null, catB.getId());

		Map<Long, Long> catIdByNoteId = noteBo.getCatIdByNoteIdMap(user);
		assertEquals(5, catIdByNoteId.size());
		assertEquals(catIdByNoteId.get(noteA.getId()), zero);
		assertEquals(catIdByNoteId.get(noteB.getId()), catA.getId());
		assertEquals(catIdByNoteId.get(noteC.getId()), catA.getId());
		assertEquals(catIdByNoteId.get(noteD.getId()), catC.getId());
		assertEquals(catIdByNoteId.get(noteE.getId()), catB.getId());
	}

	@Test
	@TransactionalReadWrite
	public void testGetCatIdByNoteIdMapToday() {
		User user = testUtils.createTestUser("NoteBoTest@testGetCatIdByNoteIdMapToday");
		Category catA = categoryBo.addCategory(user, "A");
		Category catB = categoryBo.addCategory(user, "B");
		Category catC = categoryBo.addCategory(user, "C");

		Note noteA = createNote(user, true, dts(cal.getCustomDateFromToday(-4)), zero);
		Note noteB = createNote(user, false, dts(cal.getDateYesterday()), catA.getId());
		Note noteC = createNote(user, true, dts(cal.getDateToday()), catA.getId());
		Note noteD = createNote(user, false, dts(cal.getDateToday()), catC.getId());
		Note noteE = createNote(user, true, dts(cal.getDateTomorrow()), catB.getId());

		Map<Long, Long> catIdByNoteId = noteBo.getCatIdByNoteIdMapToday(user);
		assertEquals(4, catIdByNoteId.size());
		assertEquals(catIdByNoteId.get(noteA.getId()), zero);
		assertEquals(catIdByNoteId.get(noteB.getId()), catA.getId());
		assertEquals(catIdByNoteId.get(noteC.getId()), catA.getId());
		assertEquals(catIdByNoteId.get(noteD.getId()), catC.getId());
		assertNull(catIdByNoteId.get(noteE.getId()));
	}

	@Test
	@TransactionalReadWrite
	public void testGetCatIdByNoteIdMapTomorrow() {
		User user = testUtils.createTestUser("NoteBoTest@testGetCatIdByNoteIdMapTomorrow");
		Category catA = categoryBo.addCategory(user, "A");
		Category catB = categoryBo.addCategory(user, "B");
		Category catC = categoryBo.addCategory(user, "C");

		Note noteA = createNote(user, true, dts(cal.getCustomDateFromToday(-4)), zero);
		Note noteB = createNote(user, false, dts(cal.getDateYesterday()), catA.getId());
		Note noteC = createNote(user, true, dts(cal.getDateToday()), catA.getId());
		Note noteD = createNote(user, false, dts(cal.getDateTomorrow()), catC.getId());
		Note noteE = createNote(user, true, dts(cal.getDateTomorrow()), catB.getId());
		Note noteF = createNote(user, true, dts(cal.getDateTomorrow()), catA.getId());

		Map<Long, Long> catIdByNoteId = noteBo.getCatIdByNoteIdMapTomorrow(user);
		assertEquals(3, catIdByNoteId.size());
		assertNull(catIdByNoteId.get(noteA.getId()));
		assertNull(catIdByNoteId.get(noteB.getId()));
		assertNull(catIdByNoteId.get(noteC.getId()));
		assertEquals(catIdByNoteId.get(noteD.getId()), catC.getId());
		assertEquals(catIdByNoteId.get(noteE.getId()), catB.getId());
		assertEquals(catIdByNoteId.get(noteF.getId()), catA.getId());
	}

	@Test
	@TransactionalReadWrite
	public void testGetCatIdByNoteIdMapWeek() {
		User user = testUtils.createTestUser("NoteBoTest@testGetCatIdByNoteIdMapWeek");
		Category catA = categoryBo.addCategory(user, "A");
		Category catB = categoryBo.addCategory(user, "B");
		Category catC = categoryBo.addCategory(user, "C");

		Note noteA = createNote(user, true, dts(cal.getCustomDateFromToday(-4)), zero);
		Note noteB = createNote(user, false, dts(cal.getDateYesterday()), catA.getId());
		Note noteC = createNote(user, true, dts(cal.getDateToday()), catA.getId());
		Note noteD = createNote(user, false, dts(cal.getDateTomorrow()), catC.getId());
		Note noteE = createNote(user, true, dts(cal.getDateTomorrow()), catB.getId());
		Note noteF = createNote(user, true, dts(cal.getDateNextWeek()), catB.getId());
		Note noteG = createNote(user, true, dts(cal.getCustomDateFromToday(8)), zero);

		Map<Long, Long> catIdByNoteId = noteBo.getCatIdByNoteIdMapWeek(user);
		assertEquals(4, catIdByNoteId.size());
		assertNull(catIdByNoteId.get(noteA.getId()));
		assertNull(catIdByNoteId.get(noteB.getId()));
		assertEquals(catIdByNoteId.get(noteC.getId()), catA.getId());
		assertEquals(catIdByNoteId.get(noteD.getId()), catC.getId());
		assertEquals(catIdByNoteId.get(noteE.getId()), catB.getId());
		assertEquals(catIdByNoteId.get(noteF.getId()), catB.getId());
		assertNull(catIdByNoteId.get(noteG.getId()));
	}

	@Test
	@TransactionalReadWrite
	public void testDeleteNoteById() {
		User user = testUtils.createTestUser("NoteBoTest@testDeleteNoteById");
		noteBo.deleteNoteById(user, 42L);

		logger.debug("Delete note by id: Add note");
		Note note = noteBo.addNote(user, "Note", 0L);
		Long idNote = note.getId();

		logger.debug("Delete note by id: Delete note");
		noteBo.deleteNoteById(user, idNote);
		assertNull(noteBo.getNoteById(user, idNote));
	}

	@Test
	@TransactionalReadWrite
	public void testCheckUncheckNote() {
		User user = testUtils.createTestUser("NoteBoTest@testCheckUncheckNote");
		//TODO

		logger.debug("Check/Uncheck note: Add note");
		Note note = noteBo.addNote(user, "name", 0L);

		logger.debug("Check/Uncheck note: Test check note");
		Date before = testUtils.getDateBeforeTest();
		noteBo.checkUncheckNote(user, note.getId(), true);
		Date after = testUtils.getDateAfterTest();
		assertNotNull(note.getResolvedDate());
		assertTrue(testUtils.checkDateBetween(note.getResolvedDate(), before, after));

		logger.debug("Check/Uncheck note: Test uncheck note");
		noteBo.checkUncheckNote(user, note.getId(), false);
		assertNull(note.getResolvedDate());
	}

	private String dts(Date date) {
		return dateFormat.format(date);
	}

	private Note createNote(User user, boolean checked, String dueDateStr, Long catId) {
		Note note = new Note();
		note.setId(null);
		note.setName("name");
		NoteForm form = new NoteForm();
		form.setCategoryId(catId);
		form.setNote(note);
		if (dueDateStr == null) {
			form.setEditDueDate("no");
		}
		else {
			form.setEditDueDate("yes");
			form.setDueDate(dueDateStr);
		}

		logger.debug("create note");
		noteBo.addEditNote(user, form);
		if (checked) {
			noteBo.checkUncheckNote(user, note.getId(), true);
		}
		return note;
	}
}
