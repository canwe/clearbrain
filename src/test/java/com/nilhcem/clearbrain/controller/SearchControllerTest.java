package com.nilhcem.clearbrain.controller;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.SimpleSessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.nilhcem.clearbrain.business.NoteBo;
import com.nilhcem.clearbrain.core.hibernate.TransactionalReadWrite;
import com.nilhcem.clearbrain.core.test.abstr.AbstractControllerTest;
import com.nilhcem.clearbrain.form.SearchForm;
import com.nilhcem.clearbrain.model.User;

public class SearchControllerTest extends AbstractControllerTest {
	@Autowired
	private SearchController controller;
	@Autowired
	private NoteBo noteBo;

	@Test
	public void testInitSearchPage() {
		ModelMap model = new ModelMap();
		assertEquals("logged/search", controller.initSearchPage(model));
		assertNotNull(model.get("searchform"));
	}

	@Test
	@TransactionalReadWrite
	public void testSearch() {
		User user = testUtils.createAuthenticatedTestUser("SearchControllerTest@testSearch");

		// Create note
		noteBo.addNote(user, "test", 0L);

		// Create Search form
		SearchForm searchForm = new SearchForm();
		searchForm.setSearch("test");

		// Create mock HTTP request and data binder
		MockHttpServletRequest request = new MockHttpServletRequest("post", "/search");
		WebDataBinder binder = new WebDataBinder(searchForm, "searchform");

		// Call controller
		SessionStatus status = new SimpleSessionStatus();
		ModelAndView modelAndView = controller.submitSearchPage(searchForm, binder.getBindingResult(), status, request);
		assertEquals("logged/search", modelAndView.getViewName());

		assertNotNull(modelAndView.getModel().get("notes"));
		assertNull(modelAndView.getModel().get("cagtqwe"));
		assertNotNull(modelAndView.getModel().get("categories"));
		assertNotNull(modelAndView.getModel().get("notesCatIds"));
	}
}
