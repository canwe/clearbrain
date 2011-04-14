package com.nilhcem.form;

import com.nilhcem.model.User;

/**
 * Spring MVC SignUp form object model.
 * @author Nilhcem
 * @since 1.0
 */
public class SignUp extends User {
	private String passwordConfirmation;

	public String getPasswordConfirmation() {
		return this.passwordConfirmation;
	}
	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}
}
