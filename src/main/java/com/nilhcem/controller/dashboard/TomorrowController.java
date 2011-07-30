package com.nilhcem.controller.dashboard;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/tomorrow")
public final class TomorrowController extends AbstractDashboardController {
	/**
	 * Set the dashboard type to the super class
	 */
	public TomorrowController() {
		super(DashboardDateEnum.TOMORROW);
	}

	/**
	 * Populate notes list.
	 *
	 * @return Users' notes.
	 */
	@Override
	public List<Note> populateNotesList() {
		return noteBo.getNotesTomorrow(getCurrentUser());
	}

	/**
	 * Populate JS array to know which note belong to which category.
	 *
	 * @return Map with key=noteId, value=catId.
	 */
	@Override
	public Map<Long, Long> populateNotesCatList() {
		return noteBo.getCatIdByNoteIdMapTomorrow(getCurrentUser());
	}
}
