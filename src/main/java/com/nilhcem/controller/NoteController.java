package com.nilhcem.controller;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import com.nilhcem.business.CategoryBo;
import com.nilhcem.business.NoteBo;
import com.nilhcem.form.NoteForm;
import com.nilhcem.model.Category;
import com.nilhcem.model.Note;
import com.nilhcem.validator.NoteValidator;

/**
 * Spring MVC Controller class for displaying settings.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Controller
@PreAuthorize("hasRole('RIGHT_USER')")
public final class NoteController extends AbstractController {
	@Autowired
	private NoteBo noteBo;
	@Autowired
	private CategoryBo categoryBo;
	@Autowired
	private NoteValidator noteValidator;
	private final Logger logger = LoggerFactory.getLogger(NoteController.class);
	private final SimpleDateFormat dateFormat;

	/**
	 * Define JS i18n keys.
	 */
	public NoteController() {
		super();
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		final String[] i18nJs = {"note.err.name", "note.delete.confirm", "note.caldate.format"};
		super.setI18nJsValues(i18nJs, "^note\\.");
	}

	/**
	 * Initialize note form, giving it the Note model.
	 * Handle both add or edit note.
	 * Add note by default. If there is a request parameter called {@code id} with a valid id, then it's an edit.
	 *
	 * @param model Model map.
	 * @param noteId Note id (if exists)
	 * @param locale User's locale
	 * @return the Note view, or a redirection without any parameter if parameter is invalid.
	 */
	@RequestMapping(value = "/note", method = RequestMethod.GET)
	public String initNotePage(ModelMap model, @RequestParam(value = "id", required = false) Long noteId, Locale locale) {
		Note note = null;
		NoteForm form = new NoteForm();

		//Check if we edit a note, or we add a new one. If we edit a note, check that the id is correct.
		if (noteId != null) {
			try {
				note = noteBo.getNoteById(getCurrentUser(), noteId);
				if (note == null)
					throw new Exception(); //TODO: throw custom exception

				//Category
				form.setCategoryId((note.getCategory() == null) ? 0L : note.getCategory().getId());

				//Due date
				if (note.getDueDate() != null) {
					form.setEditDueDate("yes");
					form.setDueDate(dateFormat.format(note.getDueDate())); //real one
					SimpleDateFormat dateFormatStr = new SimpleDateFormat(message.getMessage("note.caldate.formatjava", null, locale), locale);
					form.setDueDateStr(dateFormatStr.format(note.getDueDate())); //user experience
				}
			}
			catch (Exception e) {
				logger.error("", e);
				return "redirectWithoutModel:note";
			}
		}
		else {
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
	 * Delete a note.
	 * @param noteId note we need to delete.
	 * @return Redirection to the dashboard.
	 */
	//TODO: Put in another controller to avoid doing too much requests
	@RequestMapping(value = "/delete_note", method = RequestMethod.GET)
	public ModelAndView deleteNote(@RequestParam(value = "id", required = true) Long noteId) {
		noteBo.deleteNoteById(getCurrentUser(), noteId);
		return new ModelAndView("redirectWithoutModel:dashboard");
	}

	/**
	 * Populate categories list.
	 *
	 * @param Locale User's locale
	 * @return Users' categories.
	 */
	@ModelAttribute(value="categoriesList")
	public Map<Long, String> populateCategories(Locale locale) {
		Map<Long, String> map = new LinkedHashMap<Long, String>();

		//Put unclassified category
		map.put(0L, message.getMessage("note.cat.unclassified", null, locale));
		List<Category> categories = categoryBo.getSortedCategories(getCurrentUser());
		for (Category category : categories) {
			map.put(category.getId(), category.getName());
		}
		return map;
	}

	/**
	 * Add or edit a note.
	 *
	 * @param noteForm The note form.
	 * @param result Binding result.
	 * @param status Session status.
	 * @param request HTTP request.
	 * @return A new view (logged/note).
	 */
	@RequestMapping(value = "/note", method = RequestMethod.POST)
	public ModelAndView submitSettingsPage(@ModelAttribute("noteform") NoteForm noteForm, BindingResult result,
		SessionStatus status, HttpSession session) {
		noteValidator.validate(noteForm, result);
		if (result.hasErrors()) {
			session.setAttribute("note_ko", ""); //to display error message on client side
			return new ModelAndView("logged/note");
		}

		Long noteId = noteForm.getNote().getId();
		noteBo.addEditNote(getCurrentUser(), noteForm);
		status.setComplete();
		session.setAttribute("note_ok", ""); //to display confirmation message on client side
		if (noteId != null)
			return new ModelAndView("redirectWithoutModel:note?id=" + noteId);
		return new ModelAndView("redirectWithoutModel:note");
	}
}
