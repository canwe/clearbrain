package com.nilhcem.core.exception;

/**
 * Exception class which can happen while loading a missing {@code Note}.
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class NoNoteFoundException extends Exception {
	private static final long serialVersionUID = 8956280669606376871L;
	private long id;

	/**
	 * Set the id of the missing note. Used for the getMessage() method.
	 * @param id Id of the note which was not found.
	 */
	public NoNoteFoundException(long id) {
		this.id = id;
	}

	/**
	 * Return a default message.
	 * @return Message explaining the exception.
	 */
	@Override
	public String getMessage() {
		return "No note found with the following id: " + id;
	}
}
