package com.nilhcem.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.nilhcem.business.CategoryBo;
import com.nilhcem.business.NoteBo;
import com.nilhcem.model.Category;
import com.nilhcem.model.Note;

/**
 * Spring MVC Controller class for displaying dashboard.
 * 
 * @author Nilhcem
 * @since 1.0
 */
@Controller
@PreAuthorize("hasRole('RIGHT_USER')")
@RequestMapping("/dashboard")
public final class DashboardController extends AbstractController {
	@Autowired
	private CategoryBo categoryBo;
	@Autowired
	private NoteBo noteBo;

	/**
	 * Define JS i18n keys.
	 */
	public DashboardController() {
		super();
		final String[] i18nJs = {"dashboard.cat.confRm", "dashboard.cat.confRmQ", "dashboard.cat.updErr"};
		super.setI18nJsValues(i18nJs, "^dashboard\\.");
	}

	/**
	 * Populate categories list.
	 *
	 * @return Users' categories.
	 */
	@ModelAttribute(value="categoriesList")
	public List<Category> populateCategoriesList() {
		return categoryBo.getSortedCategories(getCurrentUser());
	}

	/**
	 * Return the Dashboard view.
	 *
	 * @return The logged/dashboard view.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String getDashboardView() {
		return "logged/dashboard";
	}

	/**
	 * Populate notes list.
	 *
	 * @return Users' notes.
	 */
	@ModelAttribute(value="notesList")
	public List<Note> populateNotesList() {
		return noteBo.getNotes(getCurrentUser());
	}

	/**
	 * Populate JS array to know which note belong to which category.
	 *
	 * @return Map with key=noteId, value=catId.
	 */
	@ModelAttribute(value="notesCatIds")
	public Map<Long, Long> populatesNotesCatList() {
		return noteBo.getCatIdByNoteIdMap(getCurrentUser());
	}
}
