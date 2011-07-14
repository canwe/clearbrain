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
public class NoteControllerTest {
	@Autowired
	private NoteController controller;

	@Test
	public void testJavascriptLocales() {
		final String[] keys = {"err.name"};

		MockHttpServletRequest request = new MockHttpServletRequest("get", "/logged/note");
		Map<String, String> map = controller.sendI18nToJavascript(request);
		assertNotNull(map);

		for (String key : keys) {
			assertNotNull(map.get(key));
		}
	}
}
