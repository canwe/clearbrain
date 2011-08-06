package com.nilhcem.validator;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;

import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.core.test.abstr.AbstractValidatorTest;
import com.nilhcem.form.SignUpForm;
import com.nilhcem.model.User;

public class SignUpValidatorTest extends AbstractValidatorTest {
	@Autowired
	private SignUpValidator validator;
	private final String password = "Password";

	@Override
	@Test
	public void testSupports() {
		assertTrue(validator.supports(SignUpForm.class));
		assertFalse(validator.supports(Object.class));
	}

	@Test
	public void testValidateFail() {
		// Create form object.
		SignUpForm form = new SignUpForm();
		User user = new User();
		user.setEmail("WrongEmail"); // wrong email.
		user.setPassword("password");
		form.setUser(user);
		form.setPasswordConfirmation("otherPassword"); // different password.

		// Create HTTP request.
		MockHttpServletRequest request = new MockHttpServletRequest("post", "url");
		request.addParameter("user.password", ""); // empty password.

		// Validate data.
		BindingResult result = getBindingResult(form, request);
		validator.validate(form, result);
		checkErrors(result, new String[] { "signup.err.pwd", "signup.err.pwdConf", "signup.err.mail" });
	}

	@Test
	@TransactionalReadWrite
	public void testValidateFailEmailAlreadyTaken() {
		// Create user.
		final String email = "SignUpValidatorTest@testValidateFailEmailAlreadyTaken";
		testUtils.createTestUser(email);

		// Create form object.
		SignUpForm form = new SignUpForm();
		User user = new User();
		user.setEmail(email); // email is already taken.
		user.setPassword(password);
		form.setUser(user);
		form.setPasswordConfirmation(password);

		// Create HTTP request.
		MockHttpServletRequest request = new MockHttpServletRequest("post", "url");
		request.addParameter("user.password", password);

		// Validate data.
		BindingResult result = getBindingResult(form, request);
		validator.validate(form, result);
		checkErrors(result, new String[] { "signup.err.mailRegist" });
	}

	@Test
	public void testValidate() {
		// Create form object.
		SignUpForm form = new SignUpForm();
		User user = new User();
		user.setEmail("new@email");
		user.setPassword(password);
		form.setUser(user);
		form.setPasswordConfirmation(password);

		// Create HTTP request.
		MockHttpServletRequest request = new MockHttpServletRequest("post", "url");
		request.addParameter("user.password", password);

		// Validate data.
		BindingResult result = getBindingResult(form, request);
		validator.validate(form, result);
		checkErrors(result, null);
	}
}
