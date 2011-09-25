package com.nilhcem.clearbrain.controller;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.nilhcem.clearbrain.business.CategoryBo;
import com.nilhcem.clearbrain.business.NoteBo;
import com.nilhcem.clearbrain.business.SessionBo;
import com.nilhcem.clearbrain.core.exception.NoNoteFoundException;
import com.nilhcem.clearbrain.form.NoteForm;
import com.nilhcem.clearbrain.model.Category;
import com.nilhcem.clearbrain.model.Note;
import com.nilhcem.clearbrain.validator.NoteValidator;

/**
 * Provides methods for dealing with notes.
 *
 * @author Nilhcem
 * @since 1.0
 * @see Note
 */
@Controller
@PreAuthorize("hasRole('RIGHT_USER')")
public final class NoteController extends AbstractController {
	@Autowired
	private NoteBo noteBo;
	@Autowired
	private CategoryBo categoryBo;
	@Autowired
	private SessionBo sessionBo;
	@Autowired
	private NoteValidator noteValidator;
	private final Logger logger = LoggerFactory.getLogger(NoteController.class);
	private final SimpleDateFormat dateFormat;

	/**
	 * Defines i18n keys which will be sent to JavaScript.
	 */
	public NoteController() {
		super();
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		final String[] i18nJs = {"note.err.name", "note.delete.confirm", "note.caldate.format"};
		super.setI18nJsValues(i18nJs, "^note\\.");
	}

	/**
	 * Initializes note form, giving it the Note model.
	 * <p>
	 * Handles both "add" and "edit" note.<br />
	 * Adds a note by default.<br />
	 * If there is a request parameter called {@code id} with a valid id, then will edit the note.
	 * </p>
	 *
	 * @param model the model which will contain the note form.
	 * @param noteId the id of the note (if exists), otherwise {@code null}.
	 * @param locale user's locale.
	 * @return the note view, or a redirection without any parameter if the form is invalid.
	 * @see NoteForm
	 */
	@RequestMapping(value = "/note", method = RequestMethod.GET)
	public String initNotePage(ModelMap model, @RequestParam(value = "id", required = false) Long noteId, Locale locale) {
		Note note = null;
		NoteForm form = new NoteForm();

		// Check if we edit a note, or we add a new one. If we edit a note, check that the id is correct
		if (noteId != null) {
			try {
				note = noteBo.getNoteById(getCurrentUser(), noteId);
				if (note == null) {
					throw new NoNoteFoundException(noteId);
				}

				// Category
				form.setCategoryId((note.getCategory() == null) ? 0L : note.getCategory().getId());

				// Due date
				if (note.getDueDate() != null) {
					form.setEditDueDate("yes");
					form.setDueDate(dateFormat.format(note.getDueDate())); // Real date data
					SimpleDateFormat dateFormatStr = new SimpleDateFormat(getMessageSource().getMessage("note.caldate.formatjava", null, locale), locale);
					form.setDueDateStr(dateFormatStr.format(note.getDueDate())); // For user experience (display date in a better way)
				}
			} catch (NoNoteFoundException e) {
				logger.error("", e);
				return "redirectWithoutModel:note";
			}
		} else {
			note = new Note();
		}
		form.setNote(note);

		if (form.getEditDueDate() == null) {
			form.setEditDueDate("no");
		}

		model.addAttribute("noteform", form);
		return "logged/note";
	}

	/**
	 * Sends the categories (a map where the {@code key} is the category's id and the {@code value} the category's name) in the model.
	 *
	 * @param locale user's locale. Used to put the "unclassified" category.
	 * @return a map where the {@code key} is the category's id and the {@code value} the category's name.
	 */
	@ModelAttribute(value = "categoriesList")
	public Map<Long, String> populateCategories(Locale locale) {
		Map<Long, String> map = new LinkedHashMap<Long, String>();

		// Put unclassified category
		map.put(0L, getMessageSource().getMessage("note.cat.unclassified", null, locale));
		List<Category> categories = categoryBo.getSortedCategories(getCurrentUser());
		for (Category category : categories) {
			map.put(category.getId(), category.getName());
		}
		return map;
	}

	/**
	 * Adds, edits or deletes a note.
	 * <p>
	 * This method handles all the actions which can happens when a note form is submit.<br />
	 * If the form contains the {@code _action_delete} parameter, then the note should be deleted.<br />
	 * Otherwise the note will be edited (if a proper note id has been sent) or created.
	 * </p>
	 *
	 * @param noteForm the note form which contains all the data related to the note's creation or modification.
	 * @param result a binding result to contain some validation errors, if any.
	 * @param status the session status (should be set as "complete").
	 * @param request the user's HTTP request.
	 * @return <ul>
	 * <li>a redirection to the dashboard if the note was deleted.</li>
	 * <li>the "logged/note" view if there is any error, while validating the form.</li>
	 * <li>a redirection to the note's edition or creation page if the note was successfully added/created.</li>
	 * </ul>
	 */
	@RequestMapping(value = "/note", method = RequestMethod.POST)
	public ModelAndView submitNotePage(@ModelAttribute("noteform") NoteForm noteForm, BindingResult result,
		SessionStatus status, HttpServletRequest request) {
		ModelAndView modelAndView;
		HttpSession session = request.getSession();

		// Delete
		if (WebUtils.hasSubmitParameter(request, "_action_delete")) {
			// TODO: Prevent CSRF
			noteBo.deleteNoteById(getCurrentUser(), noteForm.getNote().getId());
			sessionBo.fillSession(false, session);
			modelAndView = new ModelAndView("redirectWithoutModel:dashboard");
		} else { // Add / Edit
			noteValidator.validate(noteForm, result);
			if (result.hasErrors()) {
				session.setAttribute("note_ko", ""); // To display error message on client side
				modelAndView = new ModelAndView("logged/note");
			} else {
				Long noteId = noteForm.getNote().getId();
				noteBo.addEditNote(getCurrentUser(), noteForm);
				status.setComplete();
				session.setAttribute("note_ok", ""); // To display confirmation message on client side
				sessionBo.fillSession(false, session);
				modelAndView = new ModelAndView((noteId == null) ? "redirectWithoutModel:note" : ("redirectWithoutModel:note?id=" + noteId));
			}
		}
		return modelAndView;
	}
}
