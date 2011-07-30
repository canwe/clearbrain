package com.nilhcem.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nilhcem.enums.DashboardDateEnum;
import com.nilhcem.model.Note;

/**
 * Spring MVC Controller class for displaying dashboard.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Controller
@PreAuthorize("hasRole('RIGHT_USER')")
@RequestMapping("/today")
public final class DashboardTodayController extends AbstractDashboardController {
	/**
	 * Set the dashboard type to the super class
	 */
	public DashboardTodayController() {
		super(DashboardDateEnum.TODAY);
	}

	/**
	 * Populate notes list.
	 *
	 * @return Users' notes.
	 */
	@Override
	public List<Note> populateNotesList() {
		return noteBo.getNotesToday(getCurrentUser());
	}

	/**
	 * Populate the notes which should be done, but are not done yet.
	 *
	 * @return A list of notes which should be done before today and are not done yet.
	 */
	@ModelAttribute(value="missedList")
	public List<Note> populateMissedNotesList() {
		return noteBo.getNotesMissed(getCurrentUser());
	}

	/**
	 * Populate JS array to know which note belong to which category.
	 *
	 * @return Map with key=noteId, value=catId.
	 */
	@Override
	public Map<Long, Long> populateNotesCatList() {
		return noteBo.getCatIdByNoteIdMapToday(getCurrentUser());
	}
}
