package com.nilhcem.core.test.abstr;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.nilhcem.business.NoteBo;
import com.nilhcem.dao.NoteDao;
import com.nilhcem.model.Note;
import com.nilhcem.model.User;
import com.nilhcem.util.CalendarFacade;

public abstract class AbstractDashboardControllerTest extends AbstractControllerTest {
	@Autowired
	protected NoteBo noteBo;
	@Autowired
	protected NoteDao noteDao;
	@Autowired
	protected CalendarFacade calendar;

	protected Note createNote(User user, String name, Date dueDate, Date resolvedDate) {
		Note note = noteBo.addNote(user, name, 0L);
		note.setDueDate(dueDate);
		if (resolvedDate != null) {
			note.setResolvedDate(resolvedDate);
		}
		noteDao.update(note);
		return note;
	}

	public abstract void testPopulateDashboardType();
}
