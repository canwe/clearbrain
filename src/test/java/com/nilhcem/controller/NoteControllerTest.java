package com.nilhcem.controller;

import static org.junit.Assert.*;
import java.util.Locale;
import java.util.Map;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.nilhcem.core.test.AbstractControllerTest;

public class NoteControllerTest extends AbstractControllerTest {
	@Autowired
	private NoteController controller;

	@Test
	public void testJavascriptLocales() {
		final String[] keys = {"err.name", "delete.confirm", "caldate.format"};
		Map<String, String> map = controller.sendI18nToJavascript(new Locale("en", "US"));
		assertNotNull(map);

		for (String key : keys) {
			assertNotNull(map.get(key));
		}
	}
}
