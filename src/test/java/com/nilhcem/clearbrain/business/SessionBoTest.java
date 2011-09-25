package com.nilhcem.clearbrain.business;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import com.nilhcem.clearbrain.core.hibernate.TransactionalReadWrite;
import com.nilhcem.clearbrain.core.test.abstr.AbstractDbTest;
import com.nilhcem.clearbrain.dao.NoteDao;
import com.nilhcem.clearbrain.model.Note;
import com.nilhcem.clearbrain.model.User;
import com.nilhcem.clearbrain.util.CalendarFacade;

public class SessionBoTest extends AbstractDbTest {
	@Autowired
	private SessionBo sessionBo;
	@Autowired
	private LanguageBo languageBo;
	@Autowired
	private NoteDao noteDao;
	@Autowired
	private CalendarFacade calendar;
	private Date fiveDaysago;

	@Before
	public void setUp() {
		fiveDaysago = calendar.getCustomDateFromTodayWithoutTime(-5);
	}

	@Test
	@TransactionalReadWrite
	public void testFillSessionWithNoData() {
		testUtils.createAuthenticatedTestUser("SessionBoTest@testFillSessionWithNoData");

		MockHttpSession session = new MockHttpSession();
		sessionBo.fillSession(false, session);

		assertEquals(0L, session.getAttribute("today"));
		assertEquals(0L, session.getAttribute("tomorrow"));
		assertEquals(0L, session.getAttribute("week"));
		assertNull(session.getAttribute("locale"));
	}

	@Test
	@TransactionalReadWrite
	public void testFillSessionWithLocale() {
		User user = testUtils.createTestUser("SessionBoTest@testFillSessionWithLocale");

		Locale expectedLocale = languageBo.getLocaleFromCode(testUtils.LOCALE_FR);
		user.setLanguage(languageBo.findByLocale(expectedLocale));
		testUtils.authenticate(user);

		MockHttpSession session = new MockHttpSession();
		sessionBo.fillSession(true, session);

		Locale locale = (Locale) session.getAttribute("locale");
		assertEquals(expectedLocale.getISO3Language(), locale.getISO3Language());
		assertEquals(expectedLocale.getISO3Country(), locale.getISO3Country());
	}

	@Test
	@TransactionalReadWrite
	public void testFillSessionWithData() {
		User user = testUtils.createAuthenticatedTestUser("SessionBoTest@testFillSessionWithData");

		// Create a note for yesterday
		createNoteWithDuDate(user, calendar.getCustomDateFromTodayWithoutTime(-3));
		MockHttpSession session = new MockHttpSession();
		sessionBo.fillSession(false, session);
		assertEquals(1L, session.getAttribute("today"));
		assertEquals(0L, session.getAttribute("tomorrow"));
		assertEquals(0L, session.getAttribute("week"));

		// Create another note for today
		createNoteWithDuDate(user, calendar.getDateTodayWithoutTime());
		session = new MockHttpSession();
		sessionBo.fillSession(false, session);
		assertEquals(2L, session.getAttribute("today"));
		assertEquals(0L, session.getAttribute("tomorrow"));
		assertEquals(1L, session.getAttribute("week"));

		// Create another note for tomorrow
		createNoteWithDuDate(user, calendar.getDateTomorrowWithoutTime());
		session = new MockHttpSession();
		sessionBo.fillSession(false, session);
		assertEquals(2L, session.getAttribute("today"));
		assertEquals(1L, session.getAttribute("tomorrow"));
		assertEquals(2L, session.getAttribute("week"));

		// Create another note for 3 days
		createNoteWithDuDate(user, calendar.getCustomDateFromTodayWithoutTime(3));
		session = new MockHttpSession();
		sessionBo.fillSession(false, session);
		assertEquals(2L, session.getAttribute("today"));
		assertEquals(1L, session.getAttribute("tomorrow"));
		assertEquals(3L, session.getAttribute("week"));

		// Create another note 7 days after
		createNoteWithDuDate(user, calendar.getDateNextWeekWithoutTime());
		session = new MockHttpSession();
		sessionBo.fillSession(false, session);
		assertEquals(2L, session.getAttribute("today"));
		assertEquals(1L, session.getAttribute("tomorrow"));
		assertEquals(4L, session.getAttribute("week"));

		// Create another note 8 days after
		createNoteWithDuDate(user, calendar.getCustomDateFromTodayWithoutTime(8));
		session = new MockHttpSession();
		sessionBo.fillSession(false, session);
		assertEquals(2L, session.getAttribute("today"));
		assertEquals(1L, session.getAttribute("tomorrow"));
		assertEquals(4L, session.getAttribute("week"));
	}

	private void createNoteWithDuDate(User user, Date dueDate) {
		Note note = new Note();
		note.setCategory(null);
		note.setCreationDate(fiveDaysago);
		note.setDueDate(dueDate);
		note.setName("Note");
		note.setResolvedDate(null);
		note.setUser(user);
		noteDao.save(note);
	}
}
