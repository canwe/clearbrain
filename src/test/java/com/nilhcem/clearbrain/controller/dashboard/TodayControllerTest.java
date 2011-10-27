package com.nilhcem.clearbrain.controller.dashboard;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nilhcem.clearbrain.core.hibernate.TransactionalReadWrite;
import com.nilhcem.clearbrain.core.test.abstr.AbstractDashboardControllerTest;
import com.nilhcem.clearbrain.enums.DashboardDateEnum;
import com.nilhcem.clearbrain.model.Note;
import com.nilhcem.clearbrain.model.User;

public class TodayControllerTest extends AbstractDashboardControllerTest {
	@Autowired
	private TodayController controller;

	@Test
	@TransactionalReadWrite
	public void testPopulateUndoneNotesList() {
		User user = testUtils.createAuthenticatedTestUser("TodayControllerTest@testPopulateUndoneNotesList");
		createNote(user, "Note A", calendar.getDateYesterdayWithoutTime(), null); // No
		createNote(user, "Note B", calendar.getDateTodayWithoutTime(), null); // Yes
		createNote(user, "Note C", calendar.getDateTomorrowWithoutTime(), null); // No
		assertEquals(1, controller.populateUndoneNotesList().size());
	}

	@Test
	@TransactionalReadWrite
	public void testPopulateMissedNotesList() {
		User user = testUtils.createAuthenticatedTestUser("TodayControllerTest@testPopulateMissedNotesList");
		createNote(user, "Note A", calendar.getDateYesterdayWithoutTime(), null); // Yes
		createNote(user, "Note B", calendar.getDateTodayWithoutTime(), null); // No
		createNote(user, "Note C", calendar.getCustomDateFromTodayWithoutTime(-30), null); // Yes
		assertEquals(2, controller.populateMissedNotesList().size());
	}

	@Test
	@TransactionalReadWrite
	public void testPopulateDoneNotesList() {
		User user = testUtils.createAuthenticatedTestUser("TodayControllerTest@testPopulateDoneNotesList");
		createNote(user, "Note A", calendar.getDateYesterdayWithoutTime(), calendar.getDateTodayWithoutTime()); // Yes
		createNote(user, "Note B", calendar.getDateTodayWithoutTime(), calendar.getDateTodayWithoutTime()); // Yes
		createNote(user, "Note C", calendar.getDateTomorrowWithoutTime(), calendar.getDateTodayWithoutTime()); // Yes
		assertEquals(3, controller.populateDoneNotesList().size());
	}

	@Test
	@TransactionalReadWrite
	public void testPopulateNotesCatList() {
		User user = testUtils.createAuthenticatedTestUser("TodayControllerTest@testPopulateNotesCatList");
		Note noteA = createNote(user, "Note A", calendar.getDateYesterdayWithoutTime(), null);
		Note noteB = createNote(user, "Note B", calendar.getDateTodayWithoutTime(), null);
		Map<Long, Long> map = controller.populateNotesCatList();
		assertEquals(2, map.size());
		assertTrue(map.get(noteA.getId()).equals(0L));
		assertTrue(map.get(noteB.getId()).equals(0L));
	}

	@Override
	@Test
	public void testPopulateDashboardType() {
		assertEquals(DashboardDateEnum.TODAY, controller.populateDashboardType());
	}
}
