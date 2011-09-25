package com.nilhcem.clearbrain.controller.dashboard;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.nilhcem.clearbrain.business.CategoryBo;
import com.nilhcem.clearbrain.business.NoteBo;
import com.nilhcem.clearbrain.business.SessionBo;
import com.nilhcem.clearbrain.controller.AbstractController;
import com.nilhcem.clearbrain.core.exception.CategoriesOrderException;
import com.nilhcem.clearbrain.model.Category;
import com.nilhcem.clearbrain.model.Note;

/**
 * Handles all AJAX requests from the dashboard.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Controller
@PreAuthorize("hasRole('RIGHT_USER')")
@RequestMapping("/dashboard-js")
public final class AjaxController extends AbstractController {
	@Autowired
	private CategoryBo categoryBo;
	@Autowired
	private NoteBo noteBo;
	@Autowired
	private SessionBo sessionBo;
	private final Logger logger = LoggerFactory.getLogger(AjaxController.class);

	/**
	 * Adds a category.
	 *
	 * @param name the category's name.
	 * @return the added category, or {@code null} if an error happened.
	 */
	@RequestMapping(method = RequestMethod.POST, params = { "addCat" })
	@ResponseBody
	public Category addCategory(@RequestParam(value = "addCat", required = true) String name) {
		return categoryBo.addCategory(getCurrentUser(), name);
	}

	/**
	 * Deletes a category.
	 *
	 * @param catId the category's id which will be deleted.
	 * @return always {@code true}.
	 */
	@RequestMapping(method = RequestMethod.POST, params = { "rmCat" })
	@ResponseBody
	public boolean removeCategory(@RequestParam(value = "rmCat", required = true) Long catId) {
		categoryBo.removeCategory(getCurrentUser(), catId);
		return true;
	}

	/**
	 * Renames a category.
	 *
	 * @param catId the category's id which will be renamed.
	 * @param newName the new category's name.
	 * @return always {@code true}.
	 */
	@RequestMapping(method = RequestMethod.POST, params = { "rnmCat" })
	@ResponseBody
	public boolean renameCategory(@RequestParam(value = "rnmCat", required = true) Long catId,
		@RequestParam(value = "newName", required = true) String newName) {
		categoryBo.renameCategory(getCurrentUser(), catId, newName);
		return true;
	}

	/**
	 * Updates a category's position.
	 * <p>
	 * Users can change the display order of their categories.<br />
	 * To update the position of a category, we need to have three information:
	 * <ul>
	 *   <li> The id of the category we want to move.</li>
	 *   <li> The id of another category, for reference.</li>
	 *   <li> A value to know if the category will be placed before or after the category in reference.</li>
	 * </ul>
	 * </p>
	 *
	 * @param catId the id of the category which will be moved.
	 * @param refId the id of a category marked as reference.
	 * @param before a boolean to decide if the new category will be before ({@code true}) or after ({@code false}) the one in reference.
	 * @return {@code true} if category's position was successfully updated, or {@code false} if an error happened.
	 */
	@RequestMapping(method = RequestMethod.POST, params = { "updPos" })
	@ResponseBody
	public boolean updatePosition(@RequestParam(value = "updPos", required = true) Long catId,
		@RequestParam(value = "prev", required = true) Long refId,
		@RequestParam(value = "before", required = true) boolean before) {
		try {
			categoryBo.updatePosition(getCurrentUser(), catId, refId, before);
		} catch (CategoriesOrderException e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Adds a quick note.
	 * <p>
	 * A quick note is a note which can be created very fast.<br />
	 * The user just needs to enter the name and press the "Enter key".
	 * </p>
	 *
	 * @param name the note's name which will be created.
	 * @param catId the category's id (or {@code 0L} if null).
	 * @return The added note.
	 */
	@RequestMapping(method = RequestMethod.POST, params = { "addNote" })
	@ResponseBody
	public Note addNote(@RequestParam(value = "addNote", required = true) String name,
		@RequestParam(value = "catId", required = true) Long catId) {
		return noteBo.addNote(getCurrentUser(), name, catId);
	}

	/**
	 * Marks a note as done (checked) or not (unchecked).
	 *
	 * @param noteId the id of the note which will be marked as done / not done.
	 * @param checked a boolean to know if the note will be checked ({@code true}) or unchecked ({@code false}).
	 * @param session the user's Http session (used to update values from the header, see {@link SessionBo#fillSession}.
	 * @return the updated todo headers.
	 */
	@RequestMapping(method = RequestMethod.POST, params = { "checked" })
	@ResponseBody
	public Long[] checkNote(@RequestParam(value = "noteId", required = true) Long noteId,
		@RequestParam(value = "checked", required = true) boolean checked, HttpSession session) {
		final int headersSize = 3;
		Long[] todoHeaders = new Long[headersSize];
		noteBo.checkUncheckNote(getCurrentUser(), noteId, checked);
		sessionBo.fillSession(false, session);
		todoHeaders[0] = (Long) session.getAttribute("today");
		todoHeaders[1] = (Long) session.getAttribute("tomorrow");
		todoHeaders[2] = (Long) session.getAttribute("week");
		return todoHeaders;
	}
}
