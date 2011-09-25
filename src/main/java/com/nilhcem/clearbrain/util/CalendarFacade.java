package com.nilhcem.clearbrain.util;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * Simplifies access to {@code Calendar} objects.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Component
public final class CalendarFacade {
	public static final int DAYS_IN_A_WEEK = 7;
	public static final long MILLISECONDS_IN_A_DAY = 86400000; // 1000 * 60 * 60 * 24

	/**
	 * Returns a new {@code Date} object from the one specified in parameters, without time value.
	 *
	 * @param date the date object we want to remove the time.
	 * @return a new date object, with the same date as the one in parameters, without time value.
	 */
	public Date getDateWithoutTime(final Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		removeTimeFromCalendar(cal);
		return cal.getTime();
	}

	/**
	 * Returns yesterday's date without time value.
	 *
	 * @return a date object representing yesterday's date without time.
	 */
	public Date getDateYesterdayWithoutTime() {
		Calendar cal = getCalendarDateTodayWithoutTime();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	/**
	 * Returns today's date without time value.
	 *
	 * @return a date object representing today's date without time.
	 */
	public Date getDateTodayWithoutTime() {
		return getCalendarDateTodayWithoutTime().getTime();
	}

	/**
	 * Returns tomorrow' date without time value.
	 *
	 * @return a date object representing tomorrow's date without time.
	 */
	public Date getDateTomorrowWithoutTime() {
		Calendar cal = getCalendarDateTodayWithoutTime();
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}

	/**
	 * Returns next week' date without time value.
	 *
	 * @return a date object representing next week's date (= now + 7 days) without time.
	 */
	public Date getDateNextWeekWithoutTime() {
		Calendar cal = getCalendarDateTodayWithoutTime();
		cal.add(Calendar.DATE, DAYS_IN_A_WEEK);
		return cal.getTime();
	}

	/**
	 * Returns a date without time value, adding nbDays to the current day.
	 *
	 * @param nbDays an integer to specify the number of days to add from today.
	 * @return the desired date.
	 */
	public Date getCustomDateFromTodayWithoutTime(int nbDays) {
		Calendar cal = getCalendarDateTodayWithoutTime();
		cal.add(Calendar.DATE, nbDays);
		return cal.getTime();
	}

	/**
	 * Returns today's date without time value.
	 *
	 * @return today's date.
	 */
	public Date now() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * Returns a date object XXe days before the one specified in parameter, where XX is a value in parameter.
	 *
	 * @param nbDays the number of days before the date in parameter.
	 * @param date a date object as reference.
	 * @return a new date object, set 1 day before the date in parameter.
	 */
	public Date getDateXXDaysBefore(int nbDays, final Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -nbDays);
		return cal.getTime();
	}

	/**
	 * Returns a date object one day after the one specified in parameter.
	 *
	 * @param date a date object as reference.
	 * @return a new date object, set 1 day after the date in parameter.
	 */
	public Date getDateAfter(final Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}

	/**
	 * Returns the number of days between two dates.
	 *
	 * @param min the first date.
	 * @param max the second date.
	 * @return the number of days between two dates.
	 */
	public long getDaysBetween(Date min, Date max) {
		return (max.getTime() - min.getTime()) / CalendarFacade.MILLISECONDS_IN_A_DAY;
	}

	/**
	 * Removes the time value from the calendar specified in parameters.
	 * <p>
	 * This method will set the time (hour, minute, second and millisecond) of the calendar to 00:00:0000.
	 * </p>
	 *
	 * @param cal the calendar which will have its time value removed.
	 */
	private void removeTimeFromCalendar(Calendar cal) {
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * Gets today's Calendar without time value.
	 *
	 * @return Today's Calendar.
	 */
	private Calendar getCalendarDateTodayWithoutTime() {
		Calendar cal = Calendar.getInstance();
		removeTimeFromCalendar(cal);
		return cal;
	}
}
