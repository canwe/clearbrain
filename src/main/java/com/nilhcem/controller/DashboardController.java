package com.nilhcem.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;
import com.nilhcem.business.CategoryBo;
import com.nilhcem.business.NoteBo;
import com.nilhcem.core.spring.UserDetailsAdapter;
import com.nilhcem.model.Category;
import com.nilhcem.model.Note;
import com.nilhcem.model.User;

/**
 * Spring MVC Controller class for displaying dashboard.
 * 
 * @author Nilhcem
 * @since 1.0
 */
@Controller
@PreAuthorize("hasRole('RIGHT_USER')")
@RequestMapping("/dashboard")
public class DashboardController {
	@Autowired
	private CategoryBo categoryBo;
	@Autowired
	private NoteBo noteBo;
	@Autowired
	private MessageSource message;

	/**
	 * Get current user from session.
	 *
	 * @return Current user.
	 */
	private User getCurrentUser() {
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

	/**
	 * Add a quick note.
	 *
	 * @param name Note's name.
	 * @return The added note.
	 */
	@RequestMapping(method = RequestMethod.POST, params = { "addNote" })
	public @ResponseBody Note addNote(@RequestParam(value = "addNote", required = true) String name) {
		return noteBo.addNote(getCurrentUser(), name);
	}

	/**
	 * Inject localized strings into Javascript.
	 *
	 * @param request HTTP request.
	 * @return A map of i18n string for Javascript.
	 * @throws Exception.
	 */
	@ModelAttribute("i18nJS")
	public Map<String, String> i18nJs(HttpServletRequest request) throws Exception {
		String[] msgs = {"dashboard.cat.rm", "dashboard.cat.edit", "dashboard.cat.finEdit", "dashboard.cat.trash", "dashboard.cat.rm",
			"dashboard.cat.confRm", "dashboard.cat.confRmQ", "dashboard.cat.rmOk", "dashboard.cat.updErr"};

		Map<String, String> i18n = new LinkedHashMap<String, String>();
		Locale locale = RequestContextUtils.getLocale(request);
		for (String msg : msgs)
			i18n.put(msg.replaceFirst("^dashboard\\.", ""), message.getMessage(msg, null, locale));
		return i18n;
	}
}
