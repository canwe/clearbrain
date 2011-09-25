package com.nilhcem.clearbrain.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.nilhcem.clearbrain.form.NoteForm;

/**
 * Validates data from a {@code NoteForm} object.
 *
 * @author Nilhcem
 * @since 1.0
 * @see NoteForm
 */
public final class NoteValidator implements Validator {
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	/**
	 * Only supports {@code NoteForm} class.
	 *
	 * @param clazz the class which should be supported.
	 * @return {@code true} if this validator supports the class passed in parameter.
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return NoteForm.class.isAssignableFrom(clazz);
	}

	/**
	 * Validates data in a {@code NoteForm} object.
	 *
	 * @param target the {@code NoteForm} object.
	 * @param errors contains validation errors if any.
	 */
	@Override
	public void validate(Object target, Errors errors) {
		NoteForm form = (NoteForm) target;

		// Check name
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "note.name", "note.err.name");

		// Check due date
		if (form.getEditDueDate().equals("yes")) {
			try {
				dateFormat.parse(form.getDueDate());
			} catch (ParseException e) {
				errors.rejectValue("dueDate", "note.err.duedate");
			}
		}
	}
}
