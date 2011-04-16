package com.nilhcem.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.nilhcem.form.SignUpForm;

/**
 * Validate SignUp Form using Spring MVC Validator.
 * @author Nilhcem
 * @since 1.0
 */
public class SignUpValidator implements Validator {
	/**
	 * Only support SignUp class.
	 * @param clazz The class which should be supported
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return SignUpForm.class.isAssignableFrom(clazz);
	}

	/**
	 * Validate data in SignUp object.
	 * @param target The SignUp object
	 * @param errors Validation errors
	 */
	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.email", "email", "Email should not be empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.password", "password", "Password should not be empty");

		SignUpForm signUpForm = (SignUpForm)target;
		if (!signUpForm.getUser().getPassword().equals(signUpForm.getPasswordConfirmation()))
			errors.rejectValue("user.password", "password", "Passwords do not match");
	}
}
