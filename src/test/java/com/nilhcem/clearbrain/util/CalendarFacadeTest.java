package com.nilhcem.clearbrain.util;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nilhcem.clearbrain.core.test.abstr.AbstractDbTest;

public class CalendarFacadeTest extends AbstractDbTest {
	@Autowired
	CalendarFacade calendar;

	@Test
	public void testGetDateWithoutTime() {
		Calendar cal = getCalendarWithTime();
		cal.add(Calendar.DATE, 2);
		Date dateWithTime = cal.getTime();
		Date dateWithoutTime = calendar.getDateWithoutTime(dateWithTime);

		cal = Calendar.getInstance();
		cal.setTime(dateWithoutTime);
		assertCalendarHasNoTimeValue(cal);
	}

	@Test
	public void testGetDateYesterdayWithoutTime() {
		testGetDateXXXWithoutTime(-1, calendar.getDateYesterdayWithoutTime());
	}

	@Test
	public void testGetDateTodayWithoutTime() {
		testGetDateXXXWithoutTime(0, calendar.getDateTodayWithoutTime());
	}

	@Test
	public void testGetDateTomorrowWithoutTime() {
		testGetDateXXXWithoutTime(1, calendar.getDateTomorrowWithoutTime());
	}

	@Test
	public void testGetDateNextWeekWithoutTime() {
		testGetDateXXXWithoutTime(7, calendar.getDateNextWeekWithoutTime());
	}

	@Test
	public void testGetCustomDateFromTodayWithoutTime() {
		testGetDateXXXWithoutTime(-8, calendar.getCustomDateFromTodayWithoutTime(-8));
		testGetDateXXXWithoutTime(-3, calendar.getCustomDateFromTodayWithoutTime(-3));
		testGetDateXXXWithoutTime(3, calendar.getCustomDateFromTodayWithoutTime(3));
		testGetDateXXXWithoutTime(8, calendar.getCustomDateFromTodayWithoutTime(8));
	}

	@Test
	public void testGetDateAfter() {
		Calendar expected = getCalendarWithTime();
		expected.add(Calendar.DATE, 8);

		Calendar current = getCalendarFromDate(calendar.getDateAfter(expected.getTime()));
		expected.add(Calendar.DATE, 1);
		compareDateWithoutTime(expected, current);
	}

	@Test
	public void testGetDateXXDaysBefore() {
		Date today = calendar.getDateTodayWithoutTime();
		testGetDateXXXWithoutTime(-3, calendar.getDateXXDaysBefore(3, today));
		testGetDateXXXWithoutTime(-50, calendar.getDateXXDaysBefore(50, today));
	}

	private Calendar getCalendarWithTime() {
		int notZero = 4; // Random value, should not be 0
		assertTrue(0 != notZero);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, notZero);
		cal.set(Calendar.MINUTE, notZero);
		cal.set(Calendar.SECOND, notZero);
		cal.set(Calendar.MILLISECOND, notZero);
		return cal;
	}

	private void assertCalendarHasNoTimeValue(Calendar cal) {
		assertEquals(0, cal.get(Calendar.HOUR));
		assertEquals(0, cal.get(Calendar.MINUTE));
		assertEquals(0, cal.get(Calendar.SECOND));
		assertEquals(0, cal.get(Calendar.MILLISECOND));
	}

	private Calendar getCalendarFromDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	private void compareDateWithoutTime(Calendar expected, Calendar current) {
		assertEquals(expected.get(Calendar.YEAR), current.get(Calendar.YEAR));
		assertEquals(expected.get(Calendar.MONTH), current.get(Calendar.MONTH));
		assertEquals(expected.get(Calendar.DAY_OF_MONTH), current.get(Calendar.DAY_OF_MONTH));
	}

	private void testGetDateXXXWithoutTime(int amount, Date currentDate) {
		Calendar expectedCal = Calendar.getInstance();
		expectedCal.add(Calendar.DATE, amount);

		Calendar currentCal = getCalendarFromDate(currentDate);
		assertCalendarHasNoTimeValue(currentCal);
		compareDateWithoutTime(expectedCal, currentCal);
	}
}
