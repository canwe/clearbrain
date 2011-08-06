package com.nilhcem.controller.dashboard;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.core.test.abstr.AbstractDashboardControllerTest;
import com.nilhcem.enums.DashboardDateEnum;
import com.nilhcem.model.Note;
import com.nilhcem.model.User;

public class TodayControllerTest extends AbstractDashboardControllerTest {
	@Autowired
	private TodayController controller;

	@Test
	@TransactionalReadWrite
	public void testPopulateUndoneNotesList() {
		User user = testUtils.createAuthenticatedTestUser("TodayControllerTest@testPopulateUndoneNotesList");
		createNote(user, "Note A", calendar.getDateYesterday(), null); //no
		createNote(user, "Note B", calendar.getDateToday(), null); //yes
		createNote(user, "Note C", calendar.getDateTomorrow(), null); //no
		assertEquals(1, controller.populateUndoneNotesList().size());
	}

	@Test
	@TransactionalReadWrite
	public void testPopulateMissedNotesList() {
		User user = testUtils.createAuthenticatedTestUser("TodayControllerTest@testPopulateMissedNotesList");
		createNote(user, "Note A", calendar.getDateYesterday(), null); //yes
		createNote(user, "Note B", calendar.getDateToday(), null); //no
		createNote(user, "Note C", calendar.getCustomDateFromToday(-30), null); //yes
		assertEquals(2, controller.populateMissedNotesList().size());
	}

	@Test
	@TransactionalReadWrite
	public void testPopulateDoneNotesList() {
		User user = testUtils.createAuthenticatedTestUser("TodayControllerTest@testPopulateDoneNotesList");
		createNote(user, "Note A", calendar.getDateYesterday(), calendar.getDateToday()); //yes
		createNote(user, "Note B", calendar.getDateToday(), calendar.getDateToday()); //yes
		createNote(user, "Note C", calendar.getDateTomorrow(), calendar.getDateToday()); //no
		assertEquals(2, controller.populateDoneNotesList().size());
	}

	@Test
	@TransactionalReadWrite
	public void testPopulateNotesCatList() {
		User user = testUtils.createAuthenticatedTestUser("TodayControllerTest@testPopulateNotesCatList");
		Note noteA = createNote(user, "Note A", calendar.getDateYesterday(), null);
		Note noteB = createNote(user, "Note B", calendar.getDateToday(), null);
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
