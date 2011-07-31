package com.nilhcem.controller;

import static org.junit.Assert.*;
import java.util.Locale;
import java.util.Map;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nilhcem.core.test.AbstractControllerTest;

public class SettingsControllerTest extends AbstractControllerTest {
	@Autowired
	private SettingsController controller;

	@Test
	public void testJavascriptLocales() {
		final String[] keys = {"err.pwd", "err.pwdConf", "err.mailRegist", "err.mail", "ok.mail", "ok.pwd", "ok.pwdConf", "cancel.confirm"};
		Map<String, String> map = controller.sendI18nToJavascript(new Locale("en", "US"));
		assertNotNull(map);

		for (String key : keys) {
			assertNotNull(map.get(key));
		}
	}
}
