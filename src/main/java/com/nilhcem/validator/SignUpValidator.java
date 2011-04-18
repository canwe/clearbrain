package com.nilhcem.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.nilhcem.business.UserBo;
import com.nilhcem.form.SignUpForm;

/**
 * Validate SignUp Form using Spring MVC Validator.
 * @author Nilhcem
 * @since 1.0
 */
public class SignUpValidator implements Validator {
	@Autowired
	private UserBo userBo;

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
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.email", "signup.error.email");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.password", "signup.error.password");

		//Check password confirmation
		SignUpForm signUpForm = (SignUpForm)target;
		if (!signUpForm.getUser().getPassword().equals(signUpForm.getPasswordConfirmation()))
			errors.rejectValue("passwordConfirmation", "signup.error.passwordConfirmation");

		//Check email already registered
		if (userBo.findByEmail(signUpForm.getUser().getEmail()) != null)
			errors.rejectValue("user.email", "signup.error.emailAlreadyRegistered");
	}
}
