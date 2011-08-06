package com.nilhcem.controller;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.SimpleSessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.nilhcem.business.CategoryBo;
import com.nilhcem.business.NoteBo;
import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.core.test.abstr.AbstractControllerTest;
import com.nilhcem.dao.NoteDao;
import com.nilhcem.form.NoteForm;
import com.nilhcem.model.Category;
import com.nilhcem.model.Note;
import com.nilhcem.model.User;
import com.nilhcem.util.CalendarFacade;

public class NoteControllerTest extends AbstractControllerTest {
	@Autowired
	private NoteController controller;
	@Autowired
	private NoteBo noteBo;
	@Autowired
	private CategoryBo categoryBo;
	@Autowired
	private NoteDao noteDao;
	@Autowired
	private CalendarFacade calendar;
	@Autowired
	protected MessageSource message;
	private final Locale localeEn = new Locale("en", "US");
	private final String requestUrl = "/note";

	@Test
	public void testJavascriptLocales() {
		final String[] keys = {"err.name", "delete.confirm", "caldate.format"};
		Map<String, String> map = controller.sendI18nToJavascript(new Locale("en", "US"));
		assertNotNull(map);

		for (String key : keys) {
			assertNotNull(map.get(key));
		}
	}

	@Test
	public void testInitNotePageAdd() {
		ModelMap model = new ModelMap();
		assertEquals("logged/note", controller.initNotePage(model, null, localeEn));
		NoteForm form = (NoteForm) model.get("noteform");
		assertNotNull(form);
		assertEquals("no", form.getEditDueDate());
	}

	@Test
	@TransactionalReadWrite
	public void testInitNotePageError() {
		testUtils.createAuthenticatedTestUser("NoteControllerTest@testInitNotePageEdit");
		ModelMap model = new ModelMap();
		assertEquals("redirectWithoutModel:note", controller.initNotePage(model, 42L, localeEn));
	}

	@Test
	@TransactionalReadWrite
	public void testInitNotePageEditNoCategoryNoDueDate() {
		User user = testUtils.createAuthenticatedTestUser("NoteControllerTest@testInitNotePageEditNoCategoryNoDueDate");
		Note note = noteBo.addNote(user, "Note", 0L);

		// Check data.
		ModelMap model = new ModelMap();
		assertEquals("logged/note", controller.initNotePage(model, note.getId(), localeEn));
		NoteForm form = (NoteForm) model.get("noteform");
		assertNotNull(form);
		assertTrue(form.getCategoryId().equals(0L));
		assertEquals(note.getId(), form.getNote().getId());
		assertEquals(note.getName(), form.getNote().getName());
		assertEquals("no", form.getEditDueDate());
	}

	@Test
	@TransactionalReadWrite
	public void testInitNotePageEditCategoryAndDueDate() {
		User user = testUtils.createAuthenticatedTestUser("NoteControllerTest@testInitNotePageEditNoCategoryNoDueDate");
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

		// Create a note with a category and a due date.
		Date dueDate = calendar.getDateTomorrow();
		Category category = categoryBo.addCategory(user, "Category");
		Note note = noteBo.addNote(user, "Note", category.getId());
		note.setDueDate(dueDate);
		noteDao.update(note);

		// Check data.
		ModelMap model = new ModelMap();
		assertEquals("logged/note", controller.initNotePage(model, note.getId(), localeEn));
		NoteForm form = (NoteForm) model.get("noteform");
		assertNotNull(form);
		assertEquals(category.getId(), form.getCategoryId());
		assertEquals(note.getId(), form.getNote().getId());

		// Check due date.
		assertEquals("yes", form.getEditDueDate());
		assertEquals(dateFormat.format(dueDate), form.getDueDate());
		dateFormat = new SimpleDateFormat(message.getMessage("note.caldate.formatjava", null, localeEn), localeEn);
		assertEquals(dateFormat.format(dueDate), form.getDueDateStr());
	}

	@Test
	public void testPopulateCategories() {
		User user = testUtils.createTestUserNewPropagation("NoteControllerTest@testPopulateCategories");
		testUtils.authenticate(user);

		// Create some categories.
		Category cat1 = categoryBo.addCategory(user, "cat1");
		Category cat2 = categoryBo.addCategory(user, "cat2");
		Category cat3 = categoryBo.addCategory(user, "cat3");
		Map<Long, String> categories = controller.populateCategories(localeEn);

		// Check unclassified category.
		assertEquals(4, categories.size());
		assertEquals(message.getMessage("note.cat.unclassified", null, localeEn), categories.get(0L));

		// Check other categories.
		assertEquals(cat1.getName(), categories.get(cat1.getId()));
		assertEquals(cat2.getName(), categories.get(cat2.getId()));
		assertEquals(cat3.getName(), categories.get(cat3.getId()));
	}

	private ModelAndView getModelAndViewNotePage(Map<String, String> parameters, NoteForm noteForm, MockHttpServletRequest request) {
		WebDataBinder binder = new WebDataBinder(noteForm, "noteform");

		if (request == null) {
			request = new MockHttpServletRequest("post", requestUrl);
		}

		if (parameters != null) {
			request.setParameters(parameters);
			binder.bind(new MutablePropertyValues(request.getParameterMap()));
		}

		SessionStatus status = new SimpleSessionStatus();
		return controller.submitNotePage(noteForm, binder.getBindingResult(), status, request);
	}

	@Test
	@TransactionalReadWrite
	public void testDeleteNote() {
		User user = testUtils.createAuthenticatedTestUser("NoteControllerTest@testDeleteNote");

		// Create Note form.
		NoteForm noteForm = new NoteForm();
		noteForm.setNote(noteBo.addNote(user, "Note to delete", 0L));

		// Fill request parameters.
		Map<String, String> params = new HashMap<String, String>();
		params.put("_action_delete", "_action_delete");

		// Call controller.
		ModelAndView modelAndView = getModelAndViewNotePage(params, noteForm, null);
		assertEquals("redirectWithoutModel:dashboard", modelAndView.getViewName());
	}

	@Test
	public void testAddEditNoteWithError() {
		// Create request and session.
		MockHttpServletRequest request = new MockHttpServletRequest("post", requestUrl);
		MockHttpSession session = new MockHttpSession();
		request.setSession(session);

		// Create empty note
		Note note = new Note();
		note.setName("");
		NoteForm noteForm = new NoteForm();
		noteForm.setNote(note);
		noteForm.setEditDueDate("");

		// Call controller.
		ModelAndView modelAndView = getModelAndViewNotePage(null, noteForm, request);
		assertNotNull(session.getAttribute("note_ko"));
		assertEquals("logged/note", modelAndView.getViewName());
	}

	@Test
	@TransactionalReadWrite
	public void testAddNote() {
		User user = testUtils.createAuthenticatedTestUser("NoteControllerTest@testAddNote");

		// Create request and session.
		MockHttpServletRequest request = new MockHttpServletRequest("post", requestUrl);
		MockHttpSession session = new MockHttpSession();
		request.setSession(session);

		// Create empty note
		Note note = new Note();
		note.setName("New note");
		NoteForm form = new NoteForm();
		form.setCategoryId(0L);
		form.setNote(note);
		form.setEditDueDate("no");

		// Call controller.
		ModelAndView modelAndView = getModelAndViewNotePage(null, form, request);
		assertNotNull(session.getAttribute("note_ok"));
		assertEquals("redirectWithoutModel:note", modelAndView.getViewName());

		// Check if note is properly added.
		assertNotNull(noteBo.getNoteById(user, note.getId()));
	}

	@Test
	@TransactionalReadWrite
	public void testEditNote() {
		final String newNoteName = "New name";

		// Create user and note.
		User user = testUtils.createAuthenticatedTestUser("NoteControllerTest@testEditNote");
		Note note = noteBo.addNote(user, "New note", 0L);
		note.setName(newNoteName);

		// Create request and session.
		MockHttpServletRequest request = new MockHttpServletRequest("post", requestUrl);
		MockHttpSession session = new MockHttpSession();
		request.setSession(session);

		// Create note form.
		NoteForm form = new NoteForm();
		form.setCategoryId(0L);
		form.setNote(note);
		form.setEditDueDate("no");

		// Call controller.
		ModelAndView modelAndView = getModelAndViewNotePage(null, form, request);
		assertNotNull(session.getAttribute("note_ok"));
		assertEquals("redirectWithoutModel:note?id=" + note.getId(), modelAndView.getViewName());

		// Check if note is properly added.
		assertEquals(newNoteName, noteBo.getNoteById(user, note.getId()).getName());
	}
}
