package com.nilhcem.core.exception;

/**
 * Exception class which can happen while updating a {@code Category}'s position.
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class CategoriesOrderException extends Exception {
	private static final long serialVersionUID = -2860137864577869043L;

	/**
	 * Return a default message.
	 * @return Message explaining the exception.
	 */
	@Override
	public String getMessage() {
		return "An error occured while updating categories positions";
	}
}
