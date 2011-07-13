package com.nilhcem.form;

/**
 * Spring MVC Settings form object model.
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class SettingsForm {
	private String email;
	private String currentEmail;
	private String editPassword;
	private String currentPassword;
	private String newPassword;
	private String confirmPassword;
	private String lang;

	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getCurrentEmail() {
		return this.currentEmail;
	}
	public void setCurrentEmail(String currentEmail) {
		this.currentEmail = currentEmail;
	}

	public String getEditPassword() {
		return this.editPassword;
	}
	public void setEditPassword(String editPassword) {
		this.editPassword = editPassword;
	}

	public String getCurrentPassword() {
		return this.currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return this.newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return this.confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getLang() {
		return this.lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
}
