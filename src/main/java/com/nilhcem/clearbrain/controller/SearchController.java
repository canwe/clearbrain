package com.nilhcem.clearbrain.controller;

import java.util.List;
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

import com.nilhcem.clearbrain.business.CategoryBo;
import com.nilhcem.clearbrain.business.NoteBo;
import com.nilhcem.clearbrain.form.SearchForm;
import com.nilhcem.clearbrain.model.Note;

/**
 * Provides methods for searching notes.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Controller
@PreAuthorize("hasRole('RIGHT_USER')")
@RequestMapping("/search")
public final class SearchController extends AbstractController {
	@Autowired
	private NoteBo noteBo;
	@Autowired
	private CategoryBo categoryBo;

	/**
	 * Initializes the search form, and returns the "logged/search" view.
	 *
	 * @param model the model which will contain the search form.
	 * @return the "logged/search" view.
	 * @see SearchForm
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String initSearchPage(ModelMap model) {
		SearchForm searchForm = new SearchForm();
		model.addAttribute("searchform", searchForm);
		return "logged/search";
	}

	/**
	 * Runs the search, and adds the found notes in the model if any. Happens when the user submits the form.
	 *
	 * @param searchForm a search form containing a string entered by the user when searching for a note.
	 * @param result a binding result to contain some validation errors, if any.
	 * @param status the session status (should be set as "complete").
	 * @param request the user's HTTP request.
	 * @return a modelAndView object containing The search view ("logged/search") and the found notes, if any.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView submitSearchPage(@ModelAttribute("searchform") SearchForm searchForm, BindingResult result,
		SessionStatus status, HttpServletRequest request) {
		status.setComplete();

		List<Note> notes = noteBo.searchNote(getCurrentUser(), searchForm.getSearch());
		Map<Long, String> categories = categoryBo.getIdAndNameForEachCategory(getCurrentUser());
		categories.put(0L, getMessageSource().getMessage("dashboard.cat.unclassified", null, request.getLocale()));

		ModelAndView modelAndView = new ModelAndView("logged/search");
		modelAndView.addObject("notes", notes);
		modelAndView.addObject("categories", categories);
		modelAndView.addObject("notesCatIds", noteBo.getCatIdByNoteIdMap(notes));

		return modelAndView;
	}
}
