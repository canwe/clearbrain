package com.nilhcem.util;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * Facade to simplify access to {@code Calendar} object.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Component
public class CalendarFacade {
	/**
	 * Get today's Calendar (without time value).
	 * @return Today's Calendar.
	 */
	private Calendar getCalendarDateToday() {
		Calendar cal = Calendar.getInstance();
		cal.clear(Calendar.HOUR);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		return cal;
	}

	/**
	 * Get yesterday's Date (without time value).
	 * @return Yesterday's date.
	 */
	public Date getDateYesterday() {
		Calendar cal = getCalendarDateToday();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	/**
	 * Get today's Date (without time value).
	 * @return Today's date.
	 */
	public Date getDateToday() {
		return getCalendarDateToday().getTime();
	}

	/**
	 * Get tomorrow's Date (without time value).
	 * @return Tomorrow's date.
	 */
	public Date getDateTomorrow() {
		Calendar cal = getCalendarDateToday();
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}

	/**
	 * Get next week's Date (without time value).
	 * @return next week's date.
	 */
	public Date getDateNextWeek() {
		Calendar cal = getCalendarDateToday();
		cal.add(Calendar.DATE, 7);
		return cal.getTime();
	}

	/**
	 * Get a date (without time value) adding nbDays to the current day.
	 *
	 * @param nbDays Number of days to add to Today to get the desired date.
	 * @return Desired date.
	 */
	public Date getCustomDateFromToday(int nbDays) {
		Calendar cal = getCalendarDateToday();
		cal.add(Calendar.DATE, nbDays);
		return cal.getTime();
	}

	/**
	 * Get today's Date (with time value).
	 * @return Today's date.
	 */
	public Date now() {
		return Calendar.getInstance().getTime();
	}
}
