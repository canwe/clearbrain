package com.nilhcem.clearbrain.validator;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;

import com.nilhcem.clearbrain.core.hibernate.TransactionalReadWrite;
import com.nilhcem.clearbrain.core.test.abstr.AbstractValidatorTest;
import com.nilhcem.clearbrain.form.SignUpForm;
import com.nilhcem.clearbrain.model.User;

public class SignUpValidatorTest extends AbstractValidatorTest {
	@Autowired
	private SignUpValidator validator;
	private static final String PASSWORD = "Password";

	@Override
	@Test
	public void testSupports() {
		assertTrue(validator.supports(SignUpForm.class));
		assertFalse(validator.supports(Object.class));
	}

	@Test
	public void testValidateFail() {
		// Create form object
		SignUpForm form = new SignUpForm();
		User user = new User();
		user.setEmail("WrongEmail"); // Wrong email
		user.setPassword("password");
		form.setUser(user);
		form.setPasswordConfirmation("otherPassword"); // Different password

		// Create HTTP request
		MockHttpServletRequest request = new MockHttpServletRequest("post", "url");
		request.addParameter("user.password", ""); // empty password.

		// Validate data
		BindingResult result = getBindingResult(form, request);
		validator.validate(form, result);
		checkErrors(result, new String[] { "signup.err.pwd", "signup.err.pwdConf", "signup.err.mail" });
	}

	@Test
	@TransactionalReadWrite
	public void testValidateFailEmailAlreadyTaken() {
		// Create user
		final String email = "SignUpValidatorTest@testValidateFailEmailAlreadyTaken";
		testUtils.createTestUser(email);

		// Create form object
		SignUpForm form = new SignUpForm();
		User user = new User();
		user.setEmail(email); // Email is already taken
		user.setPassword(SignUpValidatorTest.PASSWORD);
		form.setUser(user);
		form.setPasswordConfirmation(SignUpValidatorTest.PASSWORD);

		// Create HTTP request
		MockHttpServletRequest request = new MockHttpServletRequest("post", "url");
		request.addParameter("user.password", SignUpValidatorTest.PASSWORD);

		// Validate data
		BindingResult result = getBindingResult(form, request);
		validator.validate(form, result);
		checkErrors(result, new String[] { "signup.err.mailRegist" });
	}

	@Test
	public void testValidate() {
		// Create form object
		SignUpForm form = new SignUpForm();
		User user = new User();
		user.setEmail("new@email");
		user.setPassword(SignUpValidatorTest.PASSWORD);
		form.setUser(user);
		form.setPasswordConfirmation(SignUpValidatorTest.PASSWORD);

		// Create HTTP request
		MockHttpServletRequest request = new MockHttpServletRequest("post", "url");
		request.addParameter("user.password", SignUpValidatorTest.PASSWORD);

		// Validate data
		BindingResult result = getBindingResult(form, request);
		validator.validate(form, result);
		checkErrors(result, null);
	}
}
