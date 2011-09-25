package com.nilhcem.clearbrain.controller;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nilhcem.clearbrain.business.NoteBo;
import com.nilhcem.clearbrain.model.User;
import com.nilhcem.clearbrain.util.CalendarFacade;

/**
 * Provides methods for displaying statistics.
 * <p>
 * The statistics page is a part of the website where a user can see directly some charts to know, for example
 * how many notes he created and resolved, since he registered.<br />
 * It displays some key figures which should motivate the user keeping on using the website.
 * </p>
 *
 * @author Nilhcem
 * @since 1.0
 */
@Controller
@PreAuthorize("hasRole('RIGHT_USER')")
@RequestMapping("/statistics")
public final class StatsController extends AbstractController {
	@Autowired
	private NoteBo noteBo;
	@Autowired
	private CalendarFacade calendar;

	/**
	 * Defines i18n keys which will be sent to JavaScript.
	 */
	public StatsController() {
		super();
		final String[] i18nJs = {"stats.chart.title", "stats.chart.sub1", "stats.chart.sub2", "stats.chart.creat", "stats.chart.done"};
		super.setI18nJsValues(i18nJs, "^stats\\.");
	}

	/**
	 * Displays the "logged/statistics" view and all charts data.
	 * <p>
	 * To display the chart, we should give to the model 5 elements:
	 * <ul>
	 *   <li>{@code fromYear}, {@code fromMonth} and {@code fromDay} which represent the year/month/day of the beginning of the chart.</li>
	 *   <li>{@code created} a list which representing the number of created notes per day.</li>
	 *   <li>{@code done} a list representing the number of resolved notes per day.</li>
	 * </ul>
	 * The minimum visibility for the chart is a week, which means that it will always display at least 7 days.
	 * </p>
	 *
	 * @param model contains data for the view.
	 * @return the statistics view.
	 * @see NoteBo#getNbOfNotesForEachDay
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String displayChart(ModelMap model) {
		User currentUser = getCurrentUser();
		Date from = noteBo.getDateOfFirstNote(currentUser);
		Date to = calendar.getDateTomorrowWithoutTime();

		if (from != null) {
			// We want to display at least 7 days in the statistic chart
			long dayDiff = calendar.getDaysBetween(calendar.getDateWithoutTime(from), to);
			if (dayDiff < CalendarFacade.DAYS_IN_A_WEEK) {
				from = calendar.getDateXXDaysBefore((CalendarFacade.DAYS_IN_A_WEEK - (int) dayDiff), from);
			}

			Calendar fromCal = Calendar.getInstance();
			fromCal.setTime(from);
			model.addAttribute("fromYear", fromCal.get(Calendar.YEAR));
			model.addAttribute("fromMonth", fromCal.get(Calendar.MONTH));
			model.addAttribute("fromDay", fromCal.get(Calendar.DAY_OF_MONTH));
			model.addAttribute("created", noteBo.getNbOfNotesForEachDay(currentUser, from, to, false));
			model.addAttribute("done", noteBo.getNbOfNotesForEachDay(currentUser, from, to, true));
		}

		return "logged/statistics";
	}
}
