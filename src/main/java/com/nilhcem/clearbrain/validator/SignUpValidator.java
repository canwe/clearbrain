package com.nilhcem.clearbrain.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.nilhcem.clearbrain.business.UserBo;
import com.nilhcem.clearbrain.form.SignUpForm;

/**
 * Validates data from a {@code SignUpForm} object.
 *
 * @author Nilhcem
 * @since 1.0
 * @see SignUpForm
 */
public final class SignUpValidator implements Validator {
	@Autowired
	private UserBo userBo;

	/**
	 * Only supports {@code SignUpForm} class.
	 *
	 * @param clazz the class which should be supported.
	 * @return {@code true} if this validator supports the class passed in parameter.
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return SignUpForm.class.isAssignableFrom(clazz);
	}

	/**
	 * Validates data in a {@code SignUpForm} object.
	 *
	 * @param target the {@code SignUpForm} object.
	 * @param errors contains validation errors if any.
	 */
	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.password", "signup.err.pwd");

		// Check password confirmation
		SignUpForm signUpForm = (SignUpForm) target;
		if (!signUpForm.getUser().getPassword().equals(signUpForm.getPasswordConfirmation())) {
			errors.rejectValue("passwordConfirmation", "signup.err.pwdConf");
		}

		// Check if email is valid
		final Pattern pattern = Pattern.compile("\\S+@\\S+"); // If this change, see also SettingsValidator
		Matcher matcher = pattern.matcher(signUpForm.getUser().getEmail());
		if (matcher.find()) {
			// Check email already registered
			if (userBo.findByEmail(signUpForm.getUser().getEmail()) != null) {
				errors.rejectValue("user.email", "signup.err.mailRegist");
			}
		} else {
			errors.rejectValue("user.email", "signup.err.mail");
		}
	}
}
