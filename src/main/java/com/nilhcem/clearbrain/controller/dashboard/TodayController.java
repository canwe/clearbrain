package com.nilhcem.clearbrain.controller.dashboard;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nilhcem.clearbrain.business.NoteBo;
import com.nilhcem.clearbrain.enums.DashboardDateEnum;
import com.nilhcem.clearbrain.model.Note;

/**
 * Provides methods for a dashboard displaying data for today.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Controller
@PreAuthorize("hasRole('RIGHT_USER')")
@RequestMapping("/today")
public final class TodayController extends AbstractDashboardController {
	@Autowired
	private NoteBo noteBo;

	/**
	 * Sets the dashboard type to the super class.
	 */
	public TodayController() {
		super(DashboardDateEnum.TODAY);
	}

	@Override
	public List<Note> populateUndoneNotesList() {
		return noteBo.getUndoneNotesToday(getCurrentUser());
	}

	/**
	 * Sends in the model a list of notes which should have been done before today (and are still not done yet).
	 *
	 * @return a list of "missed" notes, which should have been done before today and are still not done yet.
	 */
	@ModelAttribute(value = "missedList")
	public List<Note> populateMissedNotesList() {
		return noteBo.getUndoneNotesMissed(getCurrentUser());
	}

	@Override
	public List<Note> populateDoneNotesList() {
		return noteBo.getDoneNotesToday(getCurrentUser());
	}

	@Override
	public Map<Long, Long> populateNotesCatList() {
		return noteBo.getCatIdByNoteIdMapToday(getCurrentUser());
	}
}
