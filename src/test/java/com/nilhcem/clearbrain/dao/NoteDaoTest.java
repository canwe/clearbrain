package com.nilhcem.clearbrain.dao;

import static org.junit.Assert.*;
import java.util.Date;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.nilhcem.clearbrain.core.hibernate.TransactionalReadWrite;
import com.nilhcem.clearbrain.core.test.abstr.AbstractDbTest;
import com.nilhcem.clearbrain.enums.DashboardDateEnum;
import com.nilhcem.clearbrain.model.Note;
import com.nilhcem.clearbrain.model.User;
import com.nilhcem.clearbrain.util.CalendarFacade;

public class NoteDaoTest extends AbstractDbTest {
	@Autowired
	private NoteDao dao;
	@Autowired
	private CalendarFacade calendar;
	private Date threeDaysAgo;
	private Date today;
	private Date tomorrow;

    @Before
    public void setUp() {
		today = calendar.getDateTodayWithoutTime();
		threeDaysAgo = calendar.getCustomDateFromTodayWithoutTime(-3);
		tomorrow = calendar.getDateTomorrowWithoutTime();
    }

	@Test
	@TransactionalReadWrite
	public void testNbTaskTodoHeader() {
		User user = testUtils.createTestUser("NoteDaoTest@testNbTaskTodoHeader");
		Map<DashboardDateEnum, Long> map;

		// Create a note with a due date < yesterday
		Note note = new Note();
		note.setCategory(null);
		note.setCreationDate(threeDaysAgo);
		note.setDueDate(threeDaysAgo);
		note.setName("My note");
		note.setResolvedDate(null);
		note.setUser(user);
		dao.save(note);
		map = dao.getNbTaskTodoHeader(user);
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.TODAY));
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TOMORROW));
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.THIS_WEEK));

		// Create a note with a due date = today
		note.setDueDate(today);
		dao.update(note);
		map = dao.getNbTaskTodoHeader(user);
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.TODAY));
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TOMORROW));
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.THIS_WEEK));

		// Set this note as done
		note.setResolvedDate(today);
		dao.update(note);
		map = dao.getNbTaskTodoHeader(user);
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TODAY));
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TOMORROW));
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.THIS_WEEK));

		// Create a note with a due date = tomorrow
		note.setResolvedDate(null);
		note.setDueDate(tomorrow);
		dao.update(note);
		map = dao.getNbTaskTodoHeader(user);
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TODAY));
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.TOMORROW));
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.THIS_WEEK));

		// Create a note with a due date in 6 days
		Note note2 = new Note();
		note2.setCategory(null);
		note2.setCreationDate(today);
		note2.setDueDate(calendar.getCustomDateFromTodayWithoutTime(6));
		note2.setName("My note2");
		note2.setResolvedDate(null);
		note2.setUser(user);
		dao.save(note2);
		map = dao.getNbTaskTodoHeader(user);
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TODAY));
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.TOMORROW));
		assertEquals(Long.valueOf(2), (Long)map.get(DashboardDateEnum.THIS_WEEK));

		// Create a note with a due date in 8 days
		note.setDueDate(calendar.getCustomDateFromTodayWithoutTime(8));
		dao.update(note);
		map = dao.getNbTaskTodoHeader(user);
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TODAY));
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TOMORROW));
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.THIS_WEEK));
	}

	@Test
	@TransactionalReadWrite
	public void testGetAllNotesWithNewAccount() {
		User user = testUtils.createTestUser("NoteDaoTest@testGetAllNotesWithNewAccount");
		assertTrue(dao.getAllNotes(user, calendar.getCustomDateFromTodayWithoutTime(1), calendar.getCustomDateFromTodayWithoutTime(3)).isEmpty());
		assertTrue(dao.getAllNotes(user, null, null).isEmpty());
	}

	@Test
	@TransactionalReadWrite
	public void testGetDoneNotesBetweenResolvedDateWithNoNote() {
		User user = testUtils.createTestUser("NoteDaoTest@testGetDoneNotesBetweenResolvedDateWithNoNote");
		assertTrue(dao.getDoneNotesBetweenResolvedDate(user, calendar.getCustomDateFromTodayWithoutTime(1), calendar.getCustomDateFromTodayWithoutTime(3)).isEmpty());
		assertTrue(dao.getDoneNotesBetweenResolvedDate(user, null, null).isEmpty());
	}
}
