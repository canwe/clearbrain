package com.nilhcem.form;

import com.nilhcem.model.Note;

/**
 * Spring MVC SignUp form object model.
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class NoteForm {
	private Note note;
	private Long categoryId;

	public Note getNote() {
		return this.note;
	}
	public void setNote(Note note) {
		this.note = note;
	}

	public Long getCategoryId() {
		return this.categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}
