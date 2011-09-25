package com.nilhcem.clearbrain.controller;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.SimpleSessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.nilhcem.clearbrain.core.hibernate.TransactionalReadWrite;
import com.nilhcem.clearbrain.core.test.abstr.AbstractControllerTest;
import com.nilhcem.clearbrain.form.SettingsForm;
import com.nilhcem.clearbrain.model.User;

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

	@Test
	@TransactionalReadWrite
	public void testInitSettingsPage() {
		User user = testUtils.createAuthenticatedTestUser("SettingsControllerTest@testInitSettingsPage");

		ModelMap model = new ModelMap();
		assertEquals("logged/settings", controller.initSettingsPage(model));
		assertTrue(model.containsAttribute("settingsform"));
		SettingsForm settingsForm = (SettingsForm) model.get("settingsform");
		assertEquals(user.getEmail(), settingsForm.getEmail());
		assertEquals(user.getEmail(), settingsForm.getCurrentEmail());
		assertEquals("no", settingsForm.getEditPassword());
		assertEquals(user.getLanguage().getCode(), settingsForm.getLang());
	}

	private ModelAndView getModelAndViewSettingsPage(Map<String, String> parameters, SettingsForm settingsForm, MockHttpServletRequest request) {
		if (settingsForm == null) {
			settingsForm = new SettingsForm();
		}

		WebDataBinder binder = new WebDataBinder(settingsForm, "settingsform");

		if (parameters != null) {
			request.setParameters(parameters);
			binder.bind(new MutablePropertyValues(request.getParameterMap()));
		}

		SessionStatus status = new SimpleSessionStatus();
		return controller.submitSettingsPage(settingsForm, binder.getBindingResult(), status, request);
	}

	@Test
	@TransactionalReadWrite
	public void testDeleteAccount() {
		User user = testUtils.createAuthenticatedTestUser("SettingsControllerTest@testDeleteAccount");
		assertNotNull(SecurityContextHolder.getContext());

		// Create request
		MockHttpServletRequest request = new MockHttpServletRequest("post", "/settings");
		request.setCookies(new Cookie("value", "content"));
		assertTrue(request.getCookies()[0].getMaxAge() != 0);

		// Fill request parameters
		Map<String, String> params = new HashMap<String, String>();
		params.put("_action_delete", "_action_delete");
		Date before = testUtils.getDateBeforeTest();
		ModelAndView modelAndView = getModelAndViewSettingsPage(params, null, request);
		Date after = testUtils.getDateAfterTest();

		// User should be marked as "deletable" and cookies should be deleted
		assertTrue(testUtils.checkDateBetween(user.getDeleteDate(), before, after));
		assertNull(SecurityContextHolder.getContext().getAuthentication());
		assertTrue(request.getCookies()[0].getMaxAge() == 0);
		assertEquals("redirectWithoutModel:account-deleted", modelAndView.getViewName());
	}

	@Test
	@TransactionalReadWrite
	public void testUpdateAccountWithErrors() {
		testUtils.createAuthenticatedTestUser("SettingsControllerTest@testUpdateAccountWithErrors");

		// Create request and session
		MockHttpServletRequest request = new MockHttpServletRequest("post", "/settings");
		MockHttpSession session = new MockHttpSession();
		request.setSession(session);
		assertNull(session.getAttribute("settings_ko"));

		// Create settings
		SettingsForm settingsForm = new SettingsForm();
		settingsForm.setEditPassword("no");
		settingsForm.setEmail("OTHER_EMAIL");
		ModelAndView modelAndView = getModelAndViewSettingsPage(null, settingsForm, request);

		assertNotNull(session.getAttribute("settings_ko"));
		assertEquals("logged/settings", modelAndView.getViewName());
	}

	@Test
	@TransactionalReadWrite
	public void testUpdateAccount() {
		User user = testUtils.createAuthenticatedTestUser("SettingsControllerTest@testUpdateAccount");

		// Create request and session
		MockHttpServletRequest request = new MockHttpServletRequest("post", "/settings");
		MockHttpSession session = new MockHttpSession();
		request.setSession(session);
		assertNull(session.getAttribute("settings_ok"));

		// Create settings
		SettingsForm settingsForm = new SettingsForm();
		settingsForm.setEditPassword("no");
		settingsForm.setEmail(user.getEmail());
		settingsForm.setLang(testUtils.LOCALE_US);
		ModelAndView modelAndView = getModelAndViewSettingsPage(null, settingsForm, request);

		assertNotNull(session.getAttribute("settings_ok"));
		assertEquals("redirectWithoutModel:settings", modelAndView.getViewName());
	}

	@Test
	@TransactionalReadWrite
	public void testCheckEmailAvailability() {
		User user = testUtils.createAuthenticatedTestUser("SettingsControllerTest@testCheckEmailAvailability");
		User takenUser = testUtils.createTestUser("Taken@User");

		assertTrue(controller.checkEmailAvailability("CANNOT_FIND"));
		assertTrue(controller.checkEmailAvailability(user.getEmail()));
		assertTrue(controller.checkEmailAvailability(user.getEmail().toLowerCase()));
		assertFalse(controller.checkEmailAvailability(takenUser.getEmail()));
		assertFalse(controller.checkEmailAvailability(takenUser.getEmail().toLowerCase()));
	}
}
