package com.nilhcem.clearbrain.controller;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.SimpleSessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.nilhcem.clearbrain.business.QuickMemoBo;
import com.nilhcem.clearbrain.core.hibernate.TransactionalReadWrite;
import com.nilhcem.clearbrain.core.test.abstr.AbstractControllerTest;
import com.nilhcem.clearbrain.form.QuickMemoForm;
import com.nilhcem.clearbrain.model.User;

public class QuickMemoControllerTest extends AbstractControllerTest {
	@Autowired
	private QuickMemoController controller;
	@Autowired
	private QuickMemoBo memoBo;
	@Autowired
	protected MessageSource message;

	@Test
	@TransactionalReadWrite
	public void testGetQuickMemoViewFirstTime() {
		User user = testUtils.createAuthenticatedTestUser("QuickMemoControllerTest@testGetQuickMemoViewFirstTime");
		ModelMap model = new ModelMap();
		Locale locale = new Locale("en", "US");

		assertEquals("logged/quick-memo", controller.getQuickMemoView(model, locale));
		QuickMemoForm memoForm = (QuickMemoForm) model.get("memoform");
		assertNotNull(memoForm);
		assertEquals(message.getMessage("memo.default.text", null, locale), memoForm.getInput());
		assertEquals(memoBo.getByUser(user).getSaveDate(), memoForm.getSaveDate());
	}

	@Test
	@TransactionalReadWrite
	public void testGetQuickMemoViewNextTimes() {
		User user = testUtils.createAuthenticatedTestUser("QuickMemoControllerTest@testGetQuickMemoView");
		ModelMap model = new ModelMap();
		Locale locale = new Locale("en", "US");

		// Set a String to the quick memo
		final String memoContent = "New memo String";
		memoBo.updateMemo(user, memoContent);

		assertEquals("logged/quick-memo", controller.getQuickMemoView(model, locale));
		QuickMemoForm memoForm = (QuickMemoForm) model.get("memoform");
		assertEquals(memoContent, memoForm.getInput());
		assertEquals(memoBo.getByUser(user).getSaveDate(), memoForm.getSaveDate());
	}

	@Test
	@TransactionalReadWrite
	public void testSubmitQuickMemoPage() {
		User user = testUtils.createAuthenticatedTestUser("QuickMemoControllerTest@testSubmitQuickMemoPage");
		final String memoInput = "New memo input";
		QuickMemoForm quickMemoForm = new QuickMemoForm();
		quickMemoForm.setInput(memoInput);
		SessionStatus status = new SimpleSessionStatus();

		ModelAndView modelAndView = controller.submitQuickMemoPage(quickMemoForm, status);
		assertEquals("redirectWithoutModel:quick_memo", modelAndView.getViewName());
		assertEquals(memoInput, memoBo.getByUser(user).getContent());
	}
}
