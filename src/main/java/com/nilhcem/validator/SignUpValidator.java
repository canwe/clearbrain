package com.nilhcem.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.nilhcem.form.SignUp;

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
		return SignUp.class.isAssignableFrom(clazz);
	}

	/**
	 * Validate data in SignUp object.
	 * @param target The SignUp object
	 * @param errors Validation errors
	 */
	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email", "Email should not be empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password", "Password should not be empty");

		SignUp user = (SignUp)target;
		if (!user.getPassword().equals(user.getPasswordConfirmation()))
			errors.rejectValue("password", "password", "Passwords do not match");
	}
}
