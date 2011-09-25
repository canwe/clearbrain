package com.nilhcem.clearbrain.form;

import java.util.Date;

/**
 * Contains form data while updating a quick memo.
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class QuickMemoForm {
	private String input;
	private Date saveDate;

	public String getInput() {
		return this.input;
	}
	public void setInput(String input) {
		this.input = input;
	}

	public Date getSaveDate() {
		return this.saveDate;
	}
	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}
}
