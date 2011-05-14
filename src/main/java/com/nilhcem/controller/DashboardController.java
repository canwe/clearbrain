package com.nilhcem.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.nilhcem.business.CategoryBo;
import com.nilhcem.core.spring.UserDetailsAdapter;
import com.nilhcem.model.Category;
import com.nilhcem.model.User;

/**
 * Spring MVC Controller class for displaying dashboard.
 * 
 * @author Nilhcem
 * @since 1.0
 */
@PreAuthorize("hasRole('RIGHT_USER')")
@RequestMapping("/dashboard")
public class DashboardController {
	@Autowired
	private CategoryBo categoryBo;

	/**
	 * Get current user from session.
	 *
	 * @return Current user.
	 */
	private User getCurrentUser() {
		//Long userId = ((UserDetailsAdapter)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
		//User user = userDao.findLazyById(userId);
		return ((UserDetailsAdapter)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getHibernateUser();
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
	 * Add a category.
	 *
	 * @param name Category's name.
	 * @return The added category, or null if failed.
	 */
	@RequestMapping(method = RequestMethod.POST, params = { "addCat" })
	public @ResponseBody Category addCategory(@RequestParam(value = "addCat", required = true) String name) {
		return categoryBo.addCategory(getCurrentUser(), name);
	}

	/**
	 * Remove a category.
	 *
	 * @param catId Category's id.
	 */
	@RequestMapping(method = RequestMethod.POST, params = { "rmCat" })
	public @ResponseBody void removeCategory(@RequestParam(value = "rmCat", required = true) Long catId) {
		categoryBo.removeCategory(getCurrentUser(), catId);
	}

	/**
	 * Update a category's position.
	 *
	 * @param catId The category's id we need to update.
	 * @param oldId The previous category (the new one will be before or after this one).
	 * @param before The category will be before (== true) or after (== false) prevId.
	 */
	@RequestMapping(method = RequestMethod.POST, params = { "updPos" })
	public @ResponseBody boolean updatePosition(@RequestParam(value = "updPos", required = true) Long catId, 
		@RequestParam(value = "prev", required = true) Long oldId,
		@RequestParam(value = "before", required = true) boolean before) {
		try {
			categoryBo.updatePosition(getCurrentUser(), catId, oldId, before);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Display or hide a category.
	 *
	 * @param catId The category's id we need to display or hide.
	 * @param display True if we need to display the category, otherwise false.
	 */
	@RequestMapping(method = RequestMethod.POST, params = { "display" })
	public @ResponseBody void showHideCategory(@RequestParam(value = "id", required = true) Long catId, 
		@RequestParam(value = "display", required = true) boolean displayed) {
		categoryBo.showHideCategory(getCurrentUser(), catId, displayed);
	}
}
