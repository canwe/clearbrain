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

public class TomorrowControllerTest extends AbstractDashboardControllerTest {
	@Autowired
	private TomorrowController controller;

	@Test
	@TransactionalReadWrite
	public void testPopulateUndoneNotesList() {
		User user = testUtils.createAuthenticatedTestUser("TomorrowControllerTest@testPopulateUndoneNotesList");
		createNote(user, "Note A", calendar.getDateYesterdayWithoutTime(), null); // No
		createNote(user, "Note B", calendar.getDateTodayWithoutTime(), null); // No
		createNote(user, "Note C", calendar.getDateTomorrowWithoutTime(), null); // Yes
		createNote(user, "Note D", calendar.getCustomDateFromTodayWithoutTime(2), null); // No
		assertEquals(1, controller.populateUndoneNotesList().size());
	}

	@Test
	@TransactionalReadWrite
	public void testPopulateDoneNotesList() {
		User user = testUtils.createAuthenticatedTestUser("TomorrowControllerTest@testPopulateDoneNotesList");
		createNote(user, "Note A", calendar.getDateYesterdayWithoutTime(), calendar.getDateTodayWithoutTime()); // No
		createNote(user, "Note B", calendar.getDateTodayWithoutTime(), calendar.getDateTodayWithoutTime()); // No
		createNote(user, "Note C", calendar.getDateTomorrowWithoutTime(), calendar.getDateTodayWithoutTime()); // Yes
		assertEquals(1, controller.populateDoneNotesList().size());
	}

	@Test
	@TransactionalReadWrite
	public void testPopulateNotesCatList() {
		User user = testUtils.createAuthenticatedTestUser("TomorrowControllerTest@testPopulateNotesCatList");
		Note note = createNote(user, "Note A", calendar.getDateTomorrowWithoutTime(), null);
		createNote(user, "Note B", calendar.getDateTodayWithoutTime(), null);
		Map<Long, Long> map = controller.populateNotesCatList();
		assertEquals(1, map.size());
		assertTrue(map.get(note.getId()).equals(0L));
	}

	@Override
	@Test
	public void testPopulateDashboardType() {
		assertEquals(DashboardDateEnum.TOMORROW, controller.populateDashboardType());
	}
}
