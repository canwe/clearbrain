package com.nilhcem.dao;

import static org.junit.Assert.*;
import java.util.Date;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.core.test.abstr.AbstractDbTest;
import com.nilhcem.enums.DashboardDateEnum;
import com.nilhcem.model.Note;
import com.nilhcem.model.User;
import com.nilhcem.util.CalendarFacade;

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
		today = calendar.getDateToday();
		threeDaysAgo = calendar.getCustomDateFromToday(-3);
		tomorrow = calendar.getDateTomorrow();
    }

	@Test
	@TransactionalReadWrite
	public void testNbTaskTodoHeader() {
		User user = testUtils.createTestUser("NoteDaoTest@testNbTaskTodoHeader");
		Map<DashboardDateEnum, Long> map;

		// Create a note with a due date < yesterday.
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

		// Create a note with a due date = today.
		note.setDueDate(today);
		dao.update(note);
		map = dao.getNbTaskTodoHeader(user);
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.TODAY));
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TOMORROW));
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.THIS_WEEK));

		// Set this note as done.
		note.setResolvedDate(today);
		dao.update(note);
		map = dao.getNbTaskTodoHeader(user);
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TODAY));
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TOMORROW));
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.THIS_WEEK));

		// Create a note with a due date = tomorrow.
		note.setResolvedDate(null);
		note.setDueDate(tomorrow);
		dao.update(note);
		map = dao.getNbTaskTodoHeader(user);
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TODAY));
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.TOMORROW));
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.THIS_WEEK));

		// Create a note with a due date in 6 days.
		Note note2 = new Note();
		note2.setCategory(null);
		note2.setCreationDate(today);
		note2.setDueDate(calendar.getCustomDateFromToday(6));
		note2.setName("My note2");
		note2.setResolvedDate(null);
		note2.setUser(user);
		dao.save(note2);
		map = dao.getNbTaskTodoHeader(user);
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TODAY));
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.TOMORROW));
		assertEquals(Long.valueOf(2), (Long)map.get(DashboardDateEnum.THIS_WEEK));

		// Create a note with a due date in 8 days.
		note.setDueDate(calendar.getCustomDateFromToday(8));
		dao.update(note);
		map = dao.getNbTaskTodoHeader(user);
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TODAY));
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TOMORROW));
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.THIS_WEEK));
	}
}
