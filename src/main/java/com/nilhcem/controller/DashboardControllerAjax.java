package com.nilhcem.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.nilhcem.business.CategoryBo;
import com.nilhcem.business.NoteBo;
import com.nilhcem.business.SessionBo;
import com.nilhcem.core.exception.CategoriesOrderException;
import com.nilhcem.model.Category;
import com.nilhcem.model.Note;

/**
 * Spring MVC Controller class for Ajax requests from dashboard.
 * 
 * @author Nilhcem
 * @since 1.0
 */
@Controller
@PreAuthorize("hasRole('RIGHT_USER')")
@RequestMapping("/dashboard-js")
public final class DashboardControllerAjax extends AbstractController {
	@Autowired
	private CategoryBo categoryBo;
	@Autowired
	private NoteBo noteBo;
	@Autowired
	private SessionBo sessionBo;

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
	 * @return true.
	 */
	@RequestMapping(method = RequestMethod.POST, params = { "rmCat" })
	public @ResponseBody boolean removeCategory(@RequestParam(value = "rmCat", required = true) Long catId) {
		categoryBo.removeCategory(getCurrentUser(), catId);
		return true;
	}

	/**
	 * Rename a category.
	 *
	 * @param catId Category's id.
	 * @param newName New Category's name.
	 * @return true.
	 */
	@RequestMapping(method = RequestMethod.POST, params = { "rnmCat" })
	public @ResponseBody boolean renameCategory(@RequestParam(value = "rnmCat", required = true) Long catId,
		@RequestParam(value = "newName", required = true) String newName) {
		categoryBo.renameCategory(getCurrentUser(), catId, newName);
		return true;
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
		} catch (CategoriesOrderException e) {
			return false;
		}
		return true;
	}

	/**
	 * Add a quick note.
	 *
	 * @param name Note's name.
	 * @param catId Category's id (or 0 if null).
	 * @return The added note.
	 */
	@RequestMapping(method = RequestMethod.POST, params = { "addNote" })
	public @ResponseBody Note addNote(@RequestParam(value = "addNote", required = true) String name,
		@RequestParam(value = "catId", required = true) Long catId) {
		return noteBo.addNote(getCurrentUser(), name, catId);
	}

	/**
	 * Mark / Unmark the note as checked.
	 *
	 * @param noteId Id of the note we need to remove.
	 * @param checked If <code>true</code> then mark the note as checked, other wise, mark it as unchecked.
	 * @param session Http session.
	 */
	@RequestMapping(method = RequestMethod.POST, params = { "checked" })
	public @ResponseBody Long[] checkNote(@RequestParam(value = "noteId", required = true) Long noteId,
		@RequestParam(value = "checked", required = true) boolean checked, HttpSession session) {
		Long[] todoHeaders = new Long[3];
		noteBo.checkUncheckNote(getCurrentUser(), noteId, checked);
		sessionBo.fillSession(false, session);
		todoHeaders[0] = (Long)session.getAttribute("today");
		todoHeaders[1] = (Long)session.getAttribute("tomorrow");
		todoHeaders[2] = (Long)session.getAttribute("week");
		return todoHeaders;
	}
}
