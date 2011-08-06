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

public class TomorrowControllerTest extends AbstractDashboardControllerTest {
	@Autowired
	private TomorrowController controller;

	@Test
	@TransactionalReadWrite
	public void testPopulateUndoneNotesList() {
		User user = testUtils.createAuthenticatedTestUser("TomorrowControllerTest@testPopulateUndoneNotesList");
		createNote(user, "Note A", calendar.getDateYesterday(), null); //no
		createNote(user, "Note B", calendar.getDateToday(), null); //no
		createNote(user, "Note C", calendar.getDateTomorrow(), null); //yes
		createNote(user, "Note D", calendar.getCustomDateFromToday(2), null); //no
		assertEquals(1, controller.populateUndoneNotesList().size());
	}

	@Test
	@TransactionalReadWrite
	public void testPopulateDoneNotesList() {
		User user = testUtils.createAuthenticatedTestUser("TomorrowControllerTest@testPopulateDoneNotesList");
		createNote(user, "Note A", calendar.getDateYesterday(), calendar.getDateToday()); //no
		createNote(user, "Note B", calendar.getDateToday(), calendar.getDateToday()); //no
		createNote(user, "Note C", calendar.getDateTomorrow(), calendar.getDateToday()); //yes
		assertEquals(1, controller.populateDoneNotesList().size());
	}

	@Test
	@TransactionalReadWrite
	public void testPopulateNotesCatList() {
		User user = testUtils.createAuthenticatedTestUser("TomorrowControllerTest@testPopulateNotesCatList");
		Note note = createNote(user, "Note A", calendar.getDateTomorrow(), null);
		createNote(user, "Note B", calendar.getDateToday(), null);
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
