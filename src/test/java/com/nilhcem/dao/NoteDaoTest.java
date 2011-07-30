package com.nilhcem.dao;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.core.test.TestUtils;
import com.nilhcem.enums.DashboardDateEnum;
import com.nilhcem.model.Note;
import com.nilhcem.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public class NoteDaoTest {
	@Autowired
	private NoteDao dao;
	@Autowired
	private TestUtils testUtils;
	private Date threeDaysAgo;
	private Date today;
	private Date tomorrow;
	private Date sixDaysAfter;
	private Date eightDaysAfter;

    @Before
    public void setUp() {
		Calendar cal = Calendar.getInstance();
		cal.clear(Calendar.HOUR);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		today = cal.getTime();
		cal.add(Calendar.DATE, -3);
		threeDaysAgo = cal.getTime();
		cal.add(Calendar.DATE, +4);
		tomorrow = cal.getTime();
		cal.add(Calendar.DATE, +5);
		sixDaysAfter = cal.getTime();
		cal.add(Calendar.DATE, +3);
		eightDaysAfter = cal.getTime();
    }

	@Test
	@TransactionalReadWrite
	@Rollback(true)
	public void testNbTaskTodoHeader() {
		User user = testUtils.getTestUser();
		Map<DashboardDateEnum, Long> map;

		//Create 1 note with a due date < yesterday
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

		//Create 1 note with a due date today
		note.setDueDate(today);
		dao.update(note);
		map = dao.getNbTaskTodoHeader(user);
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.TODAY));
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TOMORROW));
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.THIS_WEEK));

		//Set this note as done
		note.setResolvedDate(today);
		dao.update(note);
		map = dao.getNbTaskTodoHeader(user);
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TODAY));
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TOMORROW));
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.THIS_WEEK));

		//Create 1 note with a due date tomorrow
		note.setResolvedDate(null);
		note.setDueDate(tomorrow);
		dao.update(note);
		map = dao.getNbTaskTodoHeader(user);
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TODAY));
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.TOMORROW));
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.THIS_WEEK));

		//Create 1 note with a due date in 6 days
		Note note2 = new Note();
		note2.setCategory(null);
		note2.setCreationDate(today);
		note2.setDueDate(sixDaysAfter);
		note2.setName("My note2");
		note2.setResolvedDate(null);
		note2.setUser(user);
		dao.save(note2);
		map = dao.getNbTaskTodoHeader(user);
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TODAY));
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.TOMORROW));
		assertEquals(Long.valueOf(2), (Long)map.get(DashboardDateEnum.THIS_WEEK));

		//Create 1 note with a due date in 8 days
		note.setDueDate(eightDaysAfter);
		dao.update(note);
		map = dao.getNbTaskTodoHeader(user);
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TODAY));
		assertEquals(Long.valueOf(0), (Long)map.get(DashboardDateEnum.TOMORROW));
		assertEquals(Long.valueOf(1), (Long)map.get(DashboardDateEnum.THIS_WEEK));
	}
}
