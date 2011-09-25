package com.nilhcem.clearbrain.core.test.abstr;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.nilhcem.clearbrain.business.NoteBo;
import com.nilhcem.clearbrain.dao.NoteDao;
import com.nilhcem.clearbrain.model.Note;
import com.nilhcem.clearbrain.model.User;
import com.nilhcem.clearbrain.util.CalendarFacade;

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
