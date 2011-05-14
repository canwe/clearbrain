package com.nilhcem.form;

import com.nilhcem.model.User;

/**
 * Spring MVC SignUp form object model.
 *
 * @author Nilhcem
 * @since 1.0
 */
public class SignUpForm {
	private User user = new User();
	private String passwordConfirmation;

	public User getUser() {
		return this.user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public String getPasswordConfirmation() {
		return this.passwordConfirmation;
	}
	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}
}
