package com.nilhcem.clearbrain.controller.dashboard;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nilhcem.clearbrain.business.NoteBo;
import com.nilhcem.clearbrain.enums.DashboardDateEnum;
import com.nilhcem.clearbrain.model.Note;

/**
 * Provides methods for a dashboard displaying data for the current week.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Controller
@PreAuthorize("hasRole('RIGHT_USER')")
@RequestMapping("/this_week")
public final class ThisWeekController extends AbstractDashboardController {
	@Autowired
	private NoteBo noteBo;

	/**
	 * Sets the dashboard type to the super class.
	 */
	public ThisWeekController() {
		super(DashboardDateEnum.THIS_WEEK);
	}

	@Override
	public List<Note> populateUndoneNotesList() {
		return noteBo.getUndoneNotesWeek(getCurrentUser());
	}

	@Override
	public List<Note> populateDoneNotesList() {
		return noteBo.getDoneNotesWeek(getCurrentUser());
	}

	@Override
	public Map<Long, Long> populateNotesCatList() {
		return noteBo.getCatIdByNoteIdMapWeek(getCurrentUser());
	}
}
