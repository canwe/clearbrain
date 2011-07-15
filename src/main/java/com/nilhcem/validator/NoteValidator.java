package com.nilhcem.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.nilhcem.form.NoteForm;

/**
 * Validate a {@code NoteForm} using Spring MVC Validator.
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class NoteValidator implements Validator {
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	/**
	 * Only support {@code NoteForm} class.
	 *
	 * @param clazz The class which should be supported.
	 * @return True if this validator supports clazz.
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return NoteForm.class.isAssignableFrom(clazz);
	}

	/**
	 * Validate data in {@code NoteForm} object.
	 *
	 * @param target The {@code NoteForm} object.
	 * @param errors Validation errors.
	 */
	@Override
	public void validate(Object target, Errors errors) {
		NoteForm form = (NoteForm)target;

		//Check name
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "note.name", "note.err.name");

		//Check due date
		if (form.getEditDueDate().equals("yes")) {
			try {
				dateFormat.parse(form.getDueDate());
			}
			catch (ParseException e) {
				errors.rejectValue("dueDate", "note.err.duedate");
			}
		}
	}
}