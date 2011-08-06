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

public class ThisWeekControllerTest extends AbstractDashboardControllerTest {
	@Autowired
	private ThisWeekController controller;

	@Test
	@TransactionalReadWrite
	public void testPopulateUndoneNotesList() {
		User user = testUtils.createAuthenticatedTestUser("ThisWeekControllerTest@testPopulateUndoneNotesList");
		createNote(user, "Note A", calendar.getDateYesterday(), null); //no
		createNote(user, "Note B", calendar.getDateToday(), null); //yes
		createNote(user, "Note C", calendar.getDateTomorrow(), null); //yes
		createNote(user, "Note D", calendar.getCustomDateFromToday(4), null); //yes
		assertEquals(3, controller.populateUndoneNotesList().size());
	}

	@Test
	@TransactionalReadWrite
	public void testPopulateDoneNotesList() {
		User user = testUtils.createAuthenticatedTestUser("ThisWeekControllerTest@testPopulateDoneNotesList");
		createNote(user, "Note A", calendar.getDateYesterday(), calendar.getDateToday()); //no
		createNote(user, "Note B", calendar.getDateToday(), calendar.getDateToday()); //yes
		createNote(user, "Note C", calendar.getDateTomorrow(), calendar.getDateToday()); //yes
		createNote(user, "Note D", calendar.getCustomDateFromToday(4), null); //no
		assertEquals(2, controller.populateDoneNotesList().size());
	}

	@Test
	@TransactionalReadWrite
	public void testPopulateNotesCatList() {
		User user = testUtils.createAuthenticatedTestUser("ThisWeekControllerTest@testPopulateNotesCatList");
		Note note = createNote(user, "Note", calendar.getDateTomorrow(), null);
		Map<Long, Long> map = controller.populateNotesCatList();
		assertEquals(1, map.size());
		assertTrue(map.get(note.getId()).equals(0L));
	}

	@Override
	@Test
	public void testPopulateDashboardType() {
		assertEquals(DashboardDateEnum.THIS_WEEK, controller.populateDashboardType());
	}
}
