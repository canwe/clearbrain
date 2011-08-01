package com.nilhcem.controller.dashboard;

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
public final class TodayController extends AbstractDashboardController {
	/**
	 * Set the dashboard type to the super class.
	 */
	public TodayController() {
		super(DashboardDateEnum.TODAY);
	}

	/**
	 * Populate undone notes list.
	 *
	 * @return Users' undone notes.
	 */
	@Override
	public List<Note> populateUndoneNotesList() {
		return noteBo.getUndoneNotesToday(getCurrentUser());
	}

	/**
	 * Populate the notes which should be done, but are not done yet.
	 *
	 * @return A list of notes which should be done before today and are not done yet.
	 */
	@ModelAttribute(value="missedList")
	public List<Note> populateMissedNotesList() {
		return noteBo.getUndoneNotesMissed(getCurrentUser());
	}

	/**
	 * Populate done notes list.
	 *
	 * @return Users' done notes.
	 */
	@Override
	public List<Note> populateDoneNotesList() {
		return noteBo.getDoneNotesToday(getCurrentUser());
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
