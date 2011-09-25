package com.nilhcem.clearbrain.core.exception;

/**
 * Thrown if a {@code Note} was not found.
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class NoNoteFoundException extends Exception {
	private static final long serialVersionUID = 8956280669606376871L;
	private long id;

	/**
	 * Sets the id of the missing note, to know which note id was not found.
	 *
	 * @param id the id of the note which was not found.
	 */
	public NoNoteFoundException(long id) {
		this.id = id;
	}

	/**
	 * Returns a message displaying which note id was not found.
	 *
	 * @return a message displaying which note id was not found.
	 */
	@Override
	public String getMessage() {
		return "No note found with the following id: " + id;
	}
}
