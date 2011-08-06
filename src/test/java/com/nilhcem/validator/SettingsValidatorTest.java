package com.nilhcem.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;

import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.core.test.abstr.AbstractValidatorTest;
import com.nilhcem.form.SettingsForm;
import com.nilhcem.model.User;

public class SettingsValidatorTest extends AbstractValidatorTest {
	@Autowired
	private SettingsValidator validator;

	@Override
	@Test
	public void testSupports() {
		assertTrue(validator.supports(SettingsForm.class));
		assertFalse(validator.supports(Object.class));
	}

	@Test
	@TransactionalReadWrite
	public void testValidateFail() {
		// Create user.
		testUtils.createAuthenticatedTestUser("SettingsValidatorTest@testValidateFail");

		// Create form object.
		SettingsForm form = new SettingsForm();
		form.setEditPassword("yes");
		form.setNewPassword("");
		form.setCurrentPassword("NotTheSame"); // wrong current password.
		form.setConfirmPassword("Different"); // different password.
		form.setEmail("WrongEmail"); // wrong email.

		// Create HTTP request.
		MockHttpServletRequest request = new MockHttpServletRequest("post", "url");
		request.addParameter("currentPassword", "NotTheSame");
		request.addParameter("newPassword", ""); // empty password.

		// Validate data.
		BindingResult result = getBindingResult(form, request);
		validator.validate(form, result);
		checkErrors(result, new String[] { "settings.err.pwd", "settings.err.curPwd", "settings.err.pwdConf", "settings.err.mail" });
	}

	@Test
	@TransactionalReadWrite
	public void testValidateFailEmailAlreadyTaken() {
		// Create users.
		User existingUser = testUtils.createTestUser("existing@user");
		testUtils.createAuthenticatedTestUser("SettingsValidatorTest@testValidateFailEmailAlreadyTaken");

		// Create form object.
		SettingsForm form = new SettingsForm();
		form.setEditPassword("no");
		form.setEmail(existingUser.getEmail());

		// Create HTTP request.
		MockHttpServletRequest request = new MockHttpServletRequest("post", "url");

		// Validate data.
		BindingResult result = getBindingResult(form, request);
		validator.validate(form, result);
		checkErrors(result, new String[] { "settings.err.mailRegist" });
	}

	@Test
	@TransactionalReadWrite
	public void testValidate() {
		// Create user.
		final String email = "SettingsValidatorTest@testValidate";
		testUtils.createAuthenticatedTestUser(email);

		// Create form object.
		SettingsForm form = new SettingsForm();
		form.setEditPassword("no");
		form.setEmail(email);

		// Create HTTP request.
		MockHttpServletRequest request = new MockHttpServletRequest("post", "url");

		// Validate data.
		BindingResult result = getBindingResult(form, request);
		validator.validate(form, result);
		checkErrors(result, null);
	}
}
