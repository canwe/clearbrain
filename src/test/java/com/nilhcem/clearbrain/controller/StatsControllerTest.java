package com.nilhcem.clearbrain.controller;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import com.nilhcem.clearbrain.business.NoteBo;
import com.nilhcem.clearbrain.core.hibernate.TransactionalReadWrite;
import com.nilhcem.clearbrain.core.test.abstr.AbstractControllerTest;
import com.nilhcem.clearbrain.model.User;

public class StatsControllerTest extends AbstractControllerTest {
	@Autowired
	private StatsController controller;
	@Autowired
	private NoteBo noteBo;

	@Test
	public void testJavascriptLocales() {
		final String[] keys = { "chart.title", "chart.sub1", "chart.sub2", "chart.creat", "chart.done" };
		Map<String, String> map = controller.sendI18nToJavascript(new Locale("en", "US"));
		assertNotNull(map);

		for (String key : keys) {
			assertNotNull(map.get(key));
		}
	}

	@Test
	@TransactionalReadWrite
	public void testDisplayChartWithNoNote() {
		testUtils.createAuthenticatedTestUser("StatsControllerTest@testDisplayChartWithNoNote");

		ModelMap model = new ModelMap();
		assertEquals("logged/statistics", controller.displayChart(model));
		assertNull(model.get("fromYear"));
		assertNull(model.get("fromMonth"));
		assertNull(model.get("fromDay"));
		assertNull(model.get("created"));
		assertNull(model.get("done"));
	}

	@Test
	@TransactionalReadWrite
	public void testDisplayChartWithNotes() {
		User user = testUtils.createAuthenticatedTestUser("StatsControllerTest@testDisplayChartWithNotes");

		// Create a note
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -6); // One week before
		noteBo.addNote(user, "Note", 0L);

		ModelMap model = new ModelMap();
		assertEquals("logged/statistics", controller.displayChart(model));
		assertEquals(cal.get(Calendar.YEAR), model.get("fromYear"));
		assertEquals(cal.get(Calendar.MONTH), model.get("fromMonth"));
		assertEquals(cal.get(Calendar.DAY_OF_MONTH), model.get("fromDay"));
		assertNotNull(model.get("created"));
		assertNotNull(model.get("done"));
	}
}
