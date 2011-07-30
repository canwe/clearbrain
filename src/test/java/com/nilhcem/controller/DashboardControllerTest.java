package com.nilhcem.controller;

import static org.junit.Assert.*;
import java.util.Locale;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nilhcem.controller.dashboard.GlobalController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml", "classpath:/META-INF/spring/mvc/mvc-dispatcher-servlet.xml"})
public class DashboardControllerTest {
	@Autowired
	private GlobalController controller;

	@Test
	public void shouldGetDashboardPage() {
		assertEquals("logged/dashboard", controller.getDashboardView());
	}

	@Test
	public void testJavascriptLocales() {
		final String[] keys = {"cat.confRm", "cat.confRmQ", "cat.updErr", "cat.confRn1", "cat.confRn2", "cat.confRn3"};
		Map<String, String> map = controller.sendI18nToJavascript(new Locale("en", "US"));
		assertNotNull(map);

		for (String key : keys) {
			assertNotNull(map.get(key));
		}
	}
}
