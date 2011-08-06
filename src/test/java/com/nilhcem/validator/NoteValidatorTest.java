package com.nilhcem.validator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;

import com.nilhcem.core.test.abstr.AbstractValidatorTest;
import com.nilhcem.form.NoteForm;

public class NoteValidatorTest extends AbstractValidatorTest {
	private NoteValidator validator;

	@Before
	public void setUp() {
		validator = new NoteValidator();
	}

	@Override
	@Test
	public void testSupports() {
		assertTrue(validator.supports(NoteForm.class));
		assertFalse(validator.supports(Object.class));
	}

	@Test
	public void testValidateFail() {
		// Create form object.
		NoteForm form = new NoteForm();
		form.setEditDueDate("yes");
		form.setDueDate("WRONG_DATA"); // wrong due date.

		// Create HTTP request.
		MockHttpServletRequest request = new MockHttpServletRequest("post", "url");
		request.addParameter("note.name", ""); // empty note name

		// Validate data.
		BindingResult result = getBindingResult(form, request);
		validator.validate(form, result);
		checkErrors(result, new String[] { "note.err.name", "note.err.duedate" });
	}

	@Test
	public void testValidate() {
		// Create form object.
		NoteForm form = new NoteForm();
		form.setEditDueDate("no");

		// Create HTTP request.
		MockHttpServletRequest request = new MockHttpServletRequest("post", "url");
		request.addParameter("note.name", "Name");

		// Validate data.
		BindingResult result = getBindingResult(form, request);
		validator.validate(form, result);
		checkErrors(result, null);
	}
}
