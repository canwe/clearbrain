package com.nilhcem.clearbrain.controller.dashboard;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nilhcem.clearbrain.business.CategoryBo;
import com.nilhcem.clearbrain.controller.AbstractController;
import com.nilhcem.clearbrain.enums.DashboardDateEnum;
import com.nilhcem.clearbrain.model.Category;
import com.nilhcem.clearbrain.model.Note;

/**
 * Superclass for dashboard controller implementations.
 * <p>
 * Provides some methods to simplify dashboard displaying.
 * </p>
 *
 * @author Nilhcem
 * @since 1.0
 */
public abstract class AbstractDashboardController extends AbstractController {
	@Autowired
	private CategoryBo categoryBo;
	private DashboardDateEnum dashboardType;

	/**
	 * Sets some dashboard-related data, such as the dashboard type some i18 keys which will be sent to JavaScript.
	 *
	 * @param type the type of the dashboard.
	 */
	public AbstractDashboardController(DashboardDateEnum type) {
		super();
		this.dashboardType = type;
		final String[] i18nJs = {"dashboard.cat.confRm", "dashboard.cat.confRmQ", "dashboard.cat.updErr",
			"dashboard.cat.confRn1", "dashboard.cat.confRn2", "dashboard.cat.confRn3"};
		super.setI18nJsValues(i18nJs, "^dashboard\\.");
	}

	/**
	 * Sets user's categories in the model so they will be available in the dashboard view.
	 *
	 * @return user's categories.
	 */
	@ModelAttribute(value = "categoriesList")
	public List<Category> populateCategoriesList() {
		return categoryBo.getSortedCategories(getCurrentUser());
	}

	/**
	 * Returns the dashboard view.
	 *
	 * @return the "logged/dashboard" view.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String getDashboardView() {
		return "logged/dashboard";
	}

	/**
	 * Adds all the notes which are not done yet in the model.
	 *
	 * @return user's undone notes.
	 */
	@ModelAttribute(value = "notesList")
	public abstract List<Note> populateUndoneNotesList();

	/**
	 * Adds all the notes done by the user in the model.
	 *
	 * @return user's done notes.
	 */
	@ModelAttribute(value = "doneList")
	public abstract List<Note> populateDoneNotesList();

	/**
	 * Sends to the model a map where the {@code key} is the note's id, and the {@code value} is the category's id.
	 * <p>
	 * These values will be used in JavaScript to know which note belong to which category.
	 * </p>
	 *
	 * @return a map where the {@code key} is the note's id, and the {@code value} is the category's id.
	 */
	@ModelAttribute(value = "notesCatIds")
	public abstract Map<Long, Long> populateNotesCatList();

	/**
	 * Sends to the model the type of the dashboard.
	 *
	 * @return The type of the dashboard.
	 */
	@ModelAttribute(value = "typeDashboard")
	public DashboardDateEnum populateDashboardType() {
		return dashboardType;
	}
}
