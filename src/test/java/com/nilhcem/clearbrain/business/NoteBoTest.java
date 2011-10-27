package com.nilhcem.clearbrain.business;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nilhcem.clearbrain.core.hibernate.TransactionalReadWrite;
import com.nilhcem.clearbrain.core.test.abstr.AbstractDbTest;
import com.nilhcem.clearbrain.form.NoteForm;
import com.nilhcem.clearbrain.model.Category;
import com.nilhcem.clearbrain.model.Note;
import com.nilhcem.clearbrain.model.User;
import com.nilhcem.clearbrain.util.CalendarFacade;

public class NoteBoTest extends AbstractDbTest {
	private static final Long zero = Long.valueOf(0l);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

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

		// Add note with no category
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

		// Add a category
		Category category = categoryBo.addCategory(user, categoryName);

		// Add a note
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

		// Add a note
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

		// Add/Edit a note with a wrong due date
		noteBo.addEditNote(user, form);
		Note newNote = noteBo.getNoteById(user, note.getId());
		assertNull(newNote.getDueDate());
	}

	@Test
	@TransactionalReadWrite
	public void testAddEditNoteAddCatAndDueDate() {
		final String dueDateStr = "05/15/2088";
		User user = testUtils.createTestUser("NoteBoTest@testAddEditNoteAddCatAndDueDate");

		// Add a category
		Category category = categoryBo.addCategory(user, "My category");

		Note note = new Note();
		note.setId(null);
		note.setName("Note name");
		NoteForm form = new NoteForm();
		form.setCategoryId(category.getId());
		form.setNote(note);
		form.setEditDueDate("yes");
		form.setDueDate(dueDateStr);

		// Add a note
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

		// Add a note
		Note note = noteBo.addNote(user, noteName, zero);
		Date creationDate = note.getCreationDate();

		// Add a category
		Category category = categoryBo.addCategory(user, "My category");

		NoteForm form = new NoteForm();
		form.setNote(note);
		form.getNote().setName(noteNewName);
		form.setCategoryId(category.getId());
		form.setEditDueDate("yes");
		form.setDueDate(dueDateStr);

		// Edit a note
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

		createNote(user, false, null, zero); // Yes
		createNote(user, false, "05/15/2088", zero); // Yes
		createNote(user, false, null, zero); // Yes
		createNote(user, true, null, zero); // No
		createNote(user, false, dts(cal.getDateNextWeekWithoutTime()), zero); // Yes

		assertEquals(4, noteBo.getUndoneNotes(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testGetUndoneNotesToday() {
		User user = testUtils.createTestUser("NoteBoTest@testGetUndoneNotesToday");

		createNote(user, false, null, zero); // No
		createNote(user, false, dts(cal.getDateYesterdayWithoutTime()), zero); // No
		createNote(user, false, dts(cal.getDateTodayWithoutTime()), zero); // Yes
		createNote(user, false, dts(cal.getDateTodayWithoutTime()), zero); // Yes
		createNote(user, true, dts(cal.getDateTodayWithoutTime()), zero); // No
		createNote(user, false, dts(cal.getDateTomorrowWithoutTime()), zero); // No
		createNote(user, false, dts(cal.getDateNextWeekWithoutTime()), zero); // No

		assertEquals(2, noteBo.getUndoneNotesToday(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testGetUndoneNotesMissed() {
		User user = testUtils.createTestUser("NoteBoTest@testGetUndoneNotesMissed");

		createNote(user, false, null, zero); // No
		createNote(user, false, dts(cal.getCustomDateFromTodayWithoutTime(-20)), zero); // Yes
		createNote(user, false, dts(cal.getCustomDateFromTodayWithoutTime(-2)), zero); // Yes
		createNote(user, false, dts(cal.getDateYesterdayWithoutTime()), zero); // Yes
		createNote(user, true, dts(cal.getDateYesterdayWithoutTime()), zero); // No
		createNote(user, false, dts(cal.getDateTodayWithoutTime()), zero); // No
		createNote(user, true, dts(cal.getDateTodayWithoutTime()), zero); // No
		createNote(user, false, dts(cal.getDateTomorrowWithoutTime()), zero); // No
		createNote(user, false, dts(cal.getDateNextWeekWithoutTime()), zero); // No

		assertEquals(3, noteBo.getUndoneNotesMissed(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testGetUndoneNotesTomorrow() {
		User user = testUtils.createTestUser("NoteBoTest@testGetUndoneNotesTomorrow");

		createNote(user, false, null, zero); // No
		createNote(user, false, dts(cal.getDateYesterdayWithoutTime()), zero); // No
		createNote(user, false, dts(cal.getDateTodayWithoutTime()), zero); // No
		createNote(user, true, dts(cal.getDateTomorrowWithoutTime()), zero); // No
		createNote(user, false, dts(cal.getDateTomorrowWithoutTime()), zero); // Yes
		createNote(user, false, dts(cal.getDateTomorrowWithoutTime()), zero); // Yes
		createNote(user, false, dts(cal.getDateTomorrowWithoutTime()), zero); // Yes
		createNote(user, false, dts(cal.getDateNextWeekWithoutTime()), zero); // No

		assertEquals(3, noteBo.getUndoneNotesTomorrow(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testGetUndoneNotesWeek() {
		User user = testUtils.createTestUser("NoteBoTest@testGetUndoneNotesWeek");

		createNote(user, false, null, zero); // No
		createNote(user, false, dts(cal.getDateYesterdayWithoutTime()), zero); // No
		createNote(user, false, dts(cal.getDateTodayWithoutTime()), zero); // Yes
		createNote(user, false, dts(cal.getDateTomorrowWithoutTime()), zero); // Yes
		createNote(user, false, dts(cal.getDateNextWeekWithoutTime()), zero); // Yes
		createNote(user, false, dts(cal.getCustomDateFromTodayWithoutTime(8)), zero); // No
		createNote(user, false, dts(cal.getCustomDateFromTodayWithoutTime(10)), zero); // No

		assertEquals(3, noteBo.getUndoneNotesWeek(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testGetDoneNotes() {
		User user = testUtils.createTestUser("NoteBoTest@testGetDoneNotes");

		createNote(user, true, null, zero); // Yes
		createNote(user, true, "05/15/2088", zero); // Yes
		createNote(user, true, dts(cal.getDateYesterdayWithoutTime()), zero); // Yes
		createNote(user, false, null, zero); // No
		createNote(user, false, dts(cal.getDateNextWeekWithoutTime()), zero); // No

		assertEquals(3, noteBo.getDoneNotes(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testGetDoneNotesToday() {
		User user = testUtils.createTestUser("NoteBoTest@testGetDoneNotesToday");

		createNote(user, true, null, zero); // Yes
		createNote(user, true, dts(cal.getCustomDateFromTodayWithoutTime(-4)), zero); // Yes
		createNote(user, true, dts(cal.getDateYesterdayWithoutTime()), zero); // Yes
		createNote(user, true, dts(cal.getDateTodayWithoutTime()), zero); // Yes
		createNoteXXDaysAfterToday(user, true, -2); // No
		createNoteXXDaysAfterToday(user, true, -1); // No
		createNoteXXDaysAfterToday(user, true, 0); // Yes
		createNoteXXDaysAfterToday(user, true, 2); // No
		createNote(user, true, dts(cal.getDateTomorrowWithoutTime()), zero); // Yes
		createNote(user, false, dts(cal.getDateTodayWithoutTime()), zero); // No
		createNote(user, false, dts(cal.getDateTomorrowWithoutTime()), zero); // No
		createNote(user, false, dts(cal.getDateNextWeekWithoutTime()), zero); // No
		assertEquals(6, noteBo.getDoneNotesToday(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testGetDoneNotesTomorrow() {
		User user = testUtils.createTestUser("NoteBoTest@testGetDoneNotesTomorrow");

		createNote(user, true, null, zero); // No
		createNote(user, true, dts(cal.getDateYesterdayWithoutTime()), zero); // No
		createNote(user, true, dts(cal.getDateTodayWithoutTime()), zero); // No
		createNote(user, false, dts(cal.getDateTomorrowWithoutTime()), zero); // No
		createNote(user, true, dts(cal.getDateTomorrowWithoutTime()), zero); // Yes
		createNote(user, true, dts(cal.getDateTomorrowWithoutTime()), zero); // Yes
		createNote(user, true, dts(cal.getDateTomorrowWithoutTime()), zero); // Yes
		createNote(user, true, dts(cal.getDateNextWeekWithoutTime()), zero); // No

		assertEquals(3, noteBo.getDoneNotesTomorrow(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testGetDoneNotesWeek() {
		User user = testUtils.createTestUser("NoteBoTest@testGetDoneNotesWeek");

		createNote(user, true, null, zero); // No
		createNote(user, true, dts(cal.getDateYesterdayWithoutTime()), zero); // No
		createNote(user, true, dts(cal.getDateTodayWithoutTime()), zero); // Yes
		createNote(user, true, dts(cal.getDateTomorrowWithoutTime()), zero); // Yes
		createNote(user, true, dts(cal.getDateNextWeekWithoutTime()), zero); // Yes
		createNote(user, true, dts(cal.getCustomDateFromTodayWithoutTime(8)), zero); // No
		createNote(user, true, dts(cal.getCustomDateFromTodayWithoutTime(10)), zero); // No

		assertEquals(3, noteBo.getDoneNotesWeek(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testGetNoteById() {
		User user = testUtils.createTestUser("NoteBoTest@testGetNoteById");
		assertNull(noteBo.getNoteById(user, 42L));

		// Add a note
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
	public void testGetCatIdByNoteIdMapListNotes() {
		User user = testUtils.createTestUser("NoteBoTest@testGetCatIdByNoteIdMapListNotes");
		Category catA = categoryBo.addCategory(user, "A");
		Category catB = categoryBo.addCategory(user, "B");

		Note noteA = createNote(user, false, null, zero);
		Note noteB = createNote(user, false, null, catA.getId());
		Note noteC = createNote(user, false, null, catB.getId());

		List<Note> notes = noteBo.getUndoneNotes(user);
		Map<Long, Long> map = noteBo.getCatIdByNoteIdMap(notes);
		assertEquals(3, map.size());
		assertEquals(zero, map.get(noteA.getId()));
		assertEquals(catA.getId(), map.get(noteB.getId()));
		assertEquals(catB.getId(), map.get(noteC.getId()));
	}

	@Test
	@TransactionalReadWrite
	public void testGetCatIdByNoteIdMapToday() {
		User user = testUtils.createTestUser("NoteBoTest@testGetCatIdByNoteIdMapToday");
		Category catA = categoryBo.addCategory(user, "A");
		Category catB = categoryBo.addCategory(user, "B");
		Category catC = categoryBo.addCategory(user, "C");

		Note noteA = createNote(user, true, dts(cal.getCustomDateFromTodayWithoutTime(-4)), zero);
		Note noteB = createNote(user, false, dts(cal.getDateYesterdayWithoutTime()), catA.getId());
		Note noteC = createNote(user, true, dts(cal.getDateTodayWithoutTime()), catA.getId());
		Note noteD = createNote(user, false, dts(cal.getDateTodayWithoutTime()), catC.getId());
		Note noteE = createNote(user, true, dts(cal.getDateTomorrowWithoutTime()), catB.getId());

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

		Note noteA = createNote(user, true, dts(cal.getCustomDateFromTodayWithoutTime(-4)), zero);
		Note noteB = createNote(user, false, dts(cal.getDateYesterdayWithoutTime()), catA.getId());
		Note noteC = createNote(user, true, dts(cal.getDateTodayWithoutTime()), catA.getId());
		Note noteD = createNote(user, false, dts(cal.getDateTomorrowWithoutTime()), catC.getId());
		Note noteE = createNote(user, true, dts(cal.getDateTomorrowWithoutTime()), catB.getId());
		Note noteF = createNote(user, true, dts(cal.getDateTomorrowWithoutTime()), catA.getId());

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

		Note noteA = createNote(user, true, dts(cal.getCustomDateFromTodayWithoutTime(-4)), zero);
		Note noteB = createNote(user, false, dts(cal.getDateYesterdayWithoutTime()), catA.getId());
		Note noteC = createNote(user, true, dts(cal.getDateTodayWithoutTime()), catA.getId());
		Note noteD = createNote(user, false, dts(cal.getDateTomorrowWithoutTime()), catC.getId());
		Note noteE = createNote(user, true, dts(cal.getDateTomorrowWithoutTime()), catB.getId());
		Note noteF = createNote(user, true, dts(cal.getDateNextWeekWithoutTime()), catB.getId());
		Note noteG = createNote(user, true, dts(cal.getCustomDateFromTodayWithoutTime(8)), zero);

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

		// Add a note
		Note note = noteBo.addNote(user, "Note", 0L);
		Long idNote = note.getId();

		// Delete a note
		noteBo.deleteNoteById(user, idNote);
		assertNull(noteBo.getNoteById(user, idNote));
	}

	@Test
	@TransactionalReadWrite
	public void testCheckUncheckNote() {
		User user = testUtils.createTestUser("NoteBoTest@testCheckUncheckNote");

		// Add a note
		Note note = noteBo.addNote(user, "name", 0L);

		// Check a note
		Date before = testUtils.getDateBeforeTest();
		noteBo.checkUncheckNote(user, note.getId(), true);
		Date after = testUtils.getDateAfterTest();
		assertNotNull(note.getResolvedDate());
		assertTrue(testUtils.checkDateBetween(note.getResolvedDate(), before, after));

		// Uncheck a note
		noteBo.checkUncheckNote(user, note.getId(), false);
		assertNull(note.getResolvedDate());
	}

	@Test
	public void testSearchNote() {
		User user = testUtils.createTestUserNewPropagation("NoteBoTest@testSearchNote");

		// Add notes
		Note note = noteBo.addNote(user, "test", 0L);
		noteBo.addNote(testUtils.createTestUserNewPropagation("NoteBoTest@testSearchNote2"), "test", 0L);

		List<Note> notes = noteBo.searchNote(user, "NotFound");
		assertTrue(notes.isEmpty());

		notes = noteBo.searchNote(user, "");
		assertTrue(notes.isEmpty());

		notes = noteBo.searchNote(user, "test");
		assertFalse(notes.isEmpty());
		assertEquals(note.getId(), notes.get(0).getId());
	}

	@Test
	@TransactionalReadWrite
	public void testGetDateOfFirstNoteWhenUserHasNoNote() {
		User user = testUtils.createTestUser("NoteBoTest@testGetDateOfFirstNoteWhenUserHasNoNote");
		assertNull(noteBo.getDateOfFirstNote(user));
	}

	@Test
	@TransactionalReadWrite
	public void testGetDateOfFirstNote() {
		User user = testUtils.createTestUser("NoteBoTest@testGetDateOfFirstNote");
		Date expectedDate = cal.getCustomDateFromTodayWithoutTime(-3);

		Note noteA = createNote(user, false, null, 0L);
		createNote(user, true, null, 0L);
		createNote(user, false, null, 0L);
		createNote(user, false, null, 0L);

		// Edit first note's date
		noteA.setCreationDate(expectedDate);
		NoteForm form = new NoteForm();
		form.setCategoryId(0L);
		form.setEditDueDate("no");
		form.setNote(noteA);
		noteBo.addEditNote(user, form);

		assertEquals(expectedDate, noteBo.getDateOfFirstNote(user));
	}

	@Test
	@TransactionalReadWrite
	public void testGetNbOfNotesForEachDayWithWrongData() {
		User user = testUtils.createTestUser("NoteBoTest@testGetNbOfNotesForEachDayWithWrongData");

		Date today = cal.getDateTodayWithoutTime();
		Date tomorrow = cal.getDateTomorrowWithoutTime();

		List<Long> list = noteBo.getNbOfNotesForEachDay(user, tomorrow, today, true);
		assertTrue(list.isEmpty());
	}

	@Test
	@TransactionalReadWrite
	public void testGetNbOfNotesForCreatedEachDay() {
		User user = testUtils.createTestUser("NoteBoTest@testGetNbOfNotesForCreatedEachDay");

		createNoteXXDaysAfterToday(user, false, -1);
		createNoteXXDaysAfterToday(user, false, 2);
		createNoteXXDaysAfterToday(user, false, 2);
		createNoteXXDaysAfterToday(user, false, 4);
		createNoteXXDaysAfterToday(user, false, 4);
		createNoteXXDaysAfterToday(user, false, 4);

		List<Long> nbOfNotes = noteBo.getNbOfNotesForEachDay(user, cal.getCustomDateFromTodayWithoutTime(-3), cal.getCustomDateFromTodayWithoutTime(5), false);

		Long[] expected = new Long[] { 0L, 0L, 1L, 0L, 0L, 2L, 0L, 3L, 0L };
		int i = 0;
		for (Long curNb : nbOfNotes) {
			assertEquals(expected[i++], curNb);
		}
	}

	@Test
	@TransactionalReadWrite
	public void testGetNbOfNotesForDoneEachDay() {
		User user = testUtils.createTestUser("NoteBoTest@testGetNbOfNotesForCreatedEachDay");

		createNoteXXDaysAfterToday(user, true, -1);
		createNoteXXDaysAfterToday(user, true, -1);
		createNoteXXDaysAfterToday(user, false, -1);
		createNoteXXDaysAfterToday(user, false, 2);
		createNoteXXDaysAfterToday(user, true, 2);
		createNoteXXDaysAfterToday(user, true, 2);
		createNoteXXDaysAfterToday(user, true, 3);

		List<Long> nbOfNotes = noteBo.getNbOfNotesForEachDay(user, cal.getCustomDateFromTodayWithoutTime(-2), cal.getCustomDateFromTodayWithoutTime(4), true);
		Long[] expected = new Long[] { 0L, 2L, 0L, 0L, 2L, 1L, 0L };
		int i = 0;
		for (Long curNb : nbOfNotes) {
			assertEquals(expected[i++], curNb);
		}
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

		// Add a note
		noteBo.addEditNote(user, form);
		if (checked) {
			noteBo.checkUncheckNote(user, note.getId(), true);
		}
		return note;
	}

	private void createNoteXXDaysAfterToday(User user, boolean checked, int dayDiff) {
		Long categoryId = 0L;
		Note note = createNote(user, checked, null, categoryId);
		Date date = cal.getCustomDateFromTodayWithoutTime(dayDiff);

		if (checked) {
			note.setResolvedDate(date);
		}
		else {
			note.setCreationDate(date);
		}

		NoteForm form = new NoteForm();
		form.setCategoryId(categoryId);
		form.setEditDueDate("no");
		form.setNote(note);
		noteBo.addEditNote(user, form);
	}
}
