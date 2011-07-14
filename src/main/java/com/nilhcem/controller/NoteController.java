package com.nilhcem.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
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
@RequestMapping("/note")
public final class NoteController extends AbstractController {
	@Autowired
	private NoteBo noteBo;
	@Autowired
	private CategoryBo categoryBo;
	@Autowired
	private NoteValidator noteValidator;

	/**
	 * Define JS i18n keys.
	 */
	public NoteController() {
		super();
		final String[] i18nJs = {"note.err.name"};
		super.setI18nJsValues(i18nJs, "^note\\.");
	}

	/**
	 * Initialize note form, giving it the Note model.
	 * Handle both add or edit note.
	 * Add note by default. If there is a request parameter called {@code id} with a valid id, then it's an edit.
	 *
	 * @param model Model map.
	 * @return the Note view, or a redirection without any parameter if parameter is invalid.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String initNotePage(ModelMap model, HttpServletRequest request) {
		Note note = null;
		NoteForm noteForm = new NoteForm();

		//Check if we edit a note, or we add a new one. If we edit a note, check that the id is correct.
		if (request.getParameter("id") != null) {
			try {
				Long id = Long.parseLong(request.getParameter("id"));
				note = noteBo.getNoteById(getCurrentUser(), id);
				if (note == null)
					throw new NumberFormatException();
				noteForm.setCategoryId((note.getCategory() == null) ? 0L : note.getCategory().getId());
			}
			catch (NumberFormatException e) {
				return "redirectWithoutModel:note";
			}
		}
		else {
			note = new Note();
		}
		noteForm.setNote(note);

		model.addAttribute("noteform", noteForm);
		return "logged/note";
	}

	/**
	 * Populate categories list.
	 *
	 * @return Users' categories.
	 */
	@ModelAttribute(value="categoriesList")
	public Map<Long, String> populateCategories(HttpServletRequest request) {
		Map<Long, String> map = new LinkedHashMap<Long, String>();

		//Put unclassified category
		Locale locale = RequestContextUtils.getLocale(request);
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
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView submitSettingsPage(@ModelAttribute("noteform") NoteForm noteForm, BindingResult result,
		SessionStatus status, HttpServletRequest request) {
		noteValidator.validate(noteForm, result);
		if (result.hasErrors()) {
			request.getSession().setAttribute("note_ko", ""); //to display error message on client side
			return new ModelAndView("logged/note");
		}

		Long noteId = noteForm.getNote().getId();
		noteBo.addEditNote(getCurrentUser(), noteForm);
		status.setComplete();
		request.getSession().setAttribute("note_ok", ""); //to display confirmation message on client side
		if (noteId != null)
			return new ModelAndView("redirectWithoutModel:note?id=" + noteId);
		return new ModelAndView("redirectWithoutModel:note");
	}
}
