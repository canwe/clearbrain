package com.nilhcem.controller.dashboard;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.nilhcem.business.CategoryBo;
import com.nilhcem.business.NoteBo;
import com.nilhcem.controller.AbstractController;
import com.nilhcem.enums.DashboardDateEnum;
import com.nilhcem.model.Category;
import com.nilhcem.model.Note;

/**
 * Abstract Spring MVC Controller.
 * Provides some functions to simplify dashboard displaying.
 *
 * @author Nilhcem
 * @since 1.0
 */
public abstract class AbstractDashboardController extends AbstractController {
	@Autowired
	protected CategoryBo categoryBo;
	@Autowired
	protected NoteBo noteBo;
	protected DashboardDateEnum dashboardType;

	/**
	 * Define JS i18n keys and dashboard type.
	 */
	public AbstractDashboardController(DashboardDateEnum type) {
		super();
		this.dashboardType = type;
		final String[] i18nJs = {"dashboard.cat.confRm", "dashboard.cat.confRmQ", "dashboard.cat.updErr",
			"dashboard.cat.confRn1", "dashboard.cat.confRn2", "dashboard.cat.confRn3"};
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
	public abstract List<Note> populateNotesList();

	/**
	 * Populate JS array to know which note belong to which category.
	 *
	 * @return Map with key=noteId, value=catId.
	 */
	@ModelAttribute(value="notesCatIds")
	public abstract Map<Long, Long> populateNotesCatList();

	/**
	 * Populate the dashboard type.
	 * @see DashboardDateEnum
	 *
	 * @return The type of the dashboard.
	 */
	@ModelAttribute(value="typeDashboard")
	public DashboardDateEnum populateDashboardType() {
		return dashboardType;
	}
}
