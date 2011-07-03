package com.nilhcem.business;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.nilhcem.core.test.TestUtils;
import com.nilhcem.model.Category;
import com.nilhcem.model.Note;
import com.nilhcem.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public class NoteBoTest {
	private static final String NOTE_NAME = "My first note";

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
		Long categoryId = testAssignACategoryToANote(user, noteId);
		testAddNoteWithACategoryDirectly(user, categoryId);
		testDeleteNote(user, noteId, categoryId);
	}

	private Long testAddNote(User user) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, -1);
		Date before = cal.getTime();
		Note note = service.addNote(user, NOTE_NAME, 0L);
		cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 1);
		Date after = cal.getTime();
		assertNull(note.getCategory());
		assertNull(note.getResolvedDate());
		assertEquals(NOTE_NAME, note.getName());
		assertEquals(user, note.getUser());
		assertFalse(before.after(note.getCreationDate()));
		assertFalse(after.before(note.getCreationDate()));
		return note.getId();
	}

	private Long testAssignACategoryToANote(User user, Long noteId) {
		Category category = categoryService.addCategory(user, "My Category");
		service.assignCategoryToNote(user, category.getId(), noteId);
		Note note = service.getNotes(user).get(0);
		assertEquals(category.getId(), note.getCategory().getId());
		return category.getId();
	}

	private void testAddNoteWithACategoryDirectly(User user, Long categoryId) {
		Note note = service.addNote(user, NOTE_NAME + "2", categoryId);
		assertEquals(categoryId, note.getCategory().getId());
		service.deleteNoteById(user, note.getId());
	}

	private void testDeleteNote(User user, Long noteId, Long categoryId) {
		categoryService.removeCategory(user, categoryId);
		assertNull(service.getNotes(user).get(0).getCategory());
		service.deleteNoteById(user, noteId);
		assertTrue(service.getNotes(user).isEmpty());
	}
}
