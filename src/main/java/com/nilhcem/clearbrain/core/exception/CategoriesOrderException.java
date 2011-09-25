package com.nilhcem.clearbrain.core.exception;

/**
 * Thrown while updating a {@code Category}'s position.
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class CategoriesOrderException extends Exception {
	private static final long serialVersionUID = -2860137864577869043L;

	/**
	 * Returns a default message explaining the exception.
	 *
	 * @return a message explaining the exception.
	 */
	@Override
	public String getMessage() {
		return "An error occured while updating categories positions";
	}
}
