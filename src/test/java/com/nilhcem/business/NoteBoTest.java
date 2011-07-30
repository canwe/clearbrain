package com.nilhcem.business;

import static org.junit.Assert.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.core.test.TestUtils;
import com.nilhcem.form.NoteForm;
import com.nilhcem.model.Category;
import com.nilhcem.model.Note;
import com.nilhcem.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public class NoteBoTest {
	private static final String NOTE_NAME = "My first note";
	private static final String CATEGORY_NAME = "My Category";

	@Autowired
	private TestUtils testUtils;
	@Autowired
	private NoteBo service;
	@Autowired
	private CategoryBo categoryService;

	@Test
	public void testNote() {
		User user = testUtils.getTestUser();
		Long noteId = testAddNote(user);
		testGetCatIdByNoteId(user, noteId, 0L);
		Category category = categoryService.addCategory(user, NoteBoTest.CATEGORY_NAME);
		Long categoryId = category.getId();
		testAddNoteWithACategoryDirectly(user, categoryId);
		testDeleteNote(user, noteId, categoryId);
	}

	private Long testAddNote(User user) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, -1);
		Date before = cal.getTime();
		Note note = service.addNote(user, NoteBoTest.NOTE_NAME, 0L);
		cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 1);
		Date after = cal.getTime();
		assertNull(note.getCategory());
		assertNull(note.getResolvedDate());
		assertEquals(NoteBoTest.NOTE_NAME, note.getName());
		assertEquals(user, note.getUser());
		assertFalse(before.after(note.getCreationDate()));
		assertFalse(after.before(note.getCreationDate()));
		return note.getId();
	}

	private void testAddNoteWithACategoryDirectly(User user, Long categoryId) {
		Note note = service.addNote(user, NoteBoTest.NOTE_NAME + "2", categoryId);
		assertEquals(categoryId, note.getCategory().getId());
		testGetCatIdByNoteId(user, note.getId(), note.getCategory().getId());
		service.deleteNoteById(user, note.getId());
	}

	private void testDeleteNote(User user, Long noteId, Long categoryId) {
		categoryService.removeCategory(user, categoryId);
		assertNull(service.getUndoneNotes(user).get(0).getCategory());
		service.deleteNoteById(user, noteId);
		assertTrue(service.getUndoneNotes(user).isEmpty());
	}

	private void testGetCatIdByNoteId(User user, Long noteId, Long expectedResult) {
		Map<Long, Long> map = service.getCatIdByNoteIdMap(user);
		assertEquals(expectedResult, map.get(noteId));
	}

	@Test
	public void testAddEditNoteForm() {
		final String DUE_DATE_STR = "05/15/2088";
		final String DUE_DATE_STR_2 = "04/02/2042";
		final String NEW_NAME = "NEW_NAME";
		final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

		User user = testUtils.getTestUser();
		assertEquals(service.getUndoneNotes(user).size(), 0);
		assertFalse(DUE_DATE_STR.equals(DUE_DATE_STR_2));
		Note note = new Note();

		//Test add
		note.setId(null);
		note.setName(NoteBoTest.NOTE_NAME);
		NoteForm form = new NoteForm();
		form.setCategoryId(0L);
		form.setNote(note);
		form.setEditDueDate("yes");
		form.setDueDate(DUE_DATE_STR);
		service.addEditNote(user, form);

		assertEquals(service.getUndoneNotes(user).size(), 1);
		Note insertedNote = service.getUndoneNotes(user).get(0);
		Date createdDate = insertedNote.getCreationDate();
		assertEquals(NoteBoTest.NOTE_NAME, insertedNote.getName());
		assertEquals(DUE_DATE_STR, new SimpleDateFormat("MM/dd/yyyy").format(insertedNote.getDueDate()));
		assertNull(insertedNote.getCategory());

		//Test edit
		form.getNote().setId(insertedNote.getId());
		form.setEditDueDate("no");
		assertFalse(NEW_NAME.equals(NoteBoTest.NOTE_NAME));
		form.getNote().setName(NEW_NAME);
		Category category = categoryService.addCategory(user, NoteBoTest.CATEGORY_NAME);
		form.setCategoryId(category.getId());
		service.addEditNote(user, form);

		assertEquals(service.getUndoneNotes(user).size(), 1);
		insertedNote = service.getUndoneNotes(user).get(0);
		assertEquals(NEW_NAME, insertedNote.getName());
		assertEquals(category.getId(), insertedNote.getCategory().getId());
		assertEquals(createdDate, insertedNote.getCreationDate()); //should not change
		assertEquals(DUE_DATE_STR, dateFormat.format(insertedNote.getDueDate())); //should not change

		//Delete category, change due date, check again
		categoryService.removeCategory(user, category.getId());
		form.setCategoryId(0L);
		form.setEditDueDate("yes");
		form.setDueDate(DUE_DATE_STR_2);
		service.addEditNote(user, form);

		insertedNote = service.getUndoneNotes(user).get(0);
		assertNull(insertedNote.getCategory());
		assertEquals(DUE_DATE_STR_2, dateFormat.format(insertedNote.getDueDate()));

		//Delete note
		service.deleteNoteById(user, insertedNote.getId());
		assertEquals(service.getUndoneNotes(user).size(), 0);
	}

	@Test
	@TransactionalReadWrite
	@Rollback(true)
	public void testCheckUncheckNote() {
		User user = testUtils.getTestUser();
		Note note = service.addNote(user, "My note", 0L);
		assertNull(note.getResolvedDate());

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, -1);
		Date before = cal.getTime();
		service.checkUncheckNote(user, note.getId(), true);
		cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 1);
		Date after = cal.getTime();

		assertNotNull(note.getResolvedDate());
		assertFalse(before.after(note.getResolvedDate()));
		assertFalse(after.before(note.getResolvedDate()));

		service.checkUncheckNote(user, note.getId(), false);
		assertNull(note.getResolvedDate());
	}
}
