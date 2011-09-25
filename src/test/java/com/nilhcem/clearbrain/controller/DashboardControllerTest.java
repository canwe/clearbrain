package com.nilhcem.clearbrain.controller;

import static org.junit.Assert.*;
import java.util.Locale;
import java.util.Map;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.nilhcem.clearbrain.controller.dashboard.GlobalController;
import com.nilhcem.clearbrain.core.test.abstr.AbstractControllerTest;

public class DashboardControllerTest extends AbstractControllerTest {
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
