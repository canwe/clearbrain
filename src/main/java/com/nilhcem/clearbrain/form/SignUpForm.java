package com.nilhcem.clearbrain.form;

import com.nilhcem.clearbrain.model.User;

/**
 * Contains form data when a user is creating an account on the website.
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class SignUpForm {
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
