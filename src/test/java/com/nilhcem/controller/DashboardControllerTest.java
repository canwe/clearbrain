package com.nilhcem.controller;

import static org.junit.Assert.*;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml", "classpath:/META-INF/spring/mvc/mvc-dispatcher-servlet.xml"})
public class DashboardControllerTest {
	@Autowired
	private DashboardController controller;

	@Test
	public void shouldGetDashboardPage() {
		assertEquals("logged/dashboard", controller.getDashboardView());
	}

	@Test
	public void testJavascriptLocales() {
		final String[] keys = {"cat.confRm", "cat.confRmQ", "cat.updErr"};

		MockHttpServletRequest request = new MockHttpServletRequest("get", "/logged/dashboard");
		Map<String, String> map = controller.sendI18nToJavascript(request);
		assertNotNull(map);

		for (String key : keys) {
			assertNotNull(map.get(key));
		}
	}
}
