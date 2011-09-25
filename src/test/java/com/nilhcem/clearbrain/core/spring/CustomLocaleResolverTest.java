package com.nilhcem.clearbrain.core.spring;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import java.util.Locale;

public class CustomLocaleResolverTest {
	private CustomLocaleResolver customLocaleResolver = new CustomLocaleResolver();
	private Locale locale;

	@Before
	public void setUp() {
		locale = new Locale("fr", "FR");
	}

	@Test
	public void testResolveLocale() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("locale", locale);
		request.setSession(session);

		Locale resolved = customLocaleResolver.resolveLocale(request);
		assertSame(locale, resolved);
	}

	@Test
	public void testResolveLocaleNoSession() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		Locale resolved = customLocaleResolver.resolveLocale(request);
		assertNotNull(resolved);
		assertNotSame(locale, resolved);

		request.setSession(new MockHttpSession());
		resolved = customLocaleResolver.resolveLocale(request);
		assertNotNull(resolved);
		assertNotSame(locale, resolved);
	}

	@Test
	public void testSetLocale() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockHttpSession session = new MockHttpSession();
		request.setSession(session);
		customLocaleResolver.setLocale(request, response, locale);
		assertSame(locale, session.getAttribute("locale"));
	}

	@Test
	public void testSetLocaleNoSession() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		customLocaleResolver.setLocale(request, response, locale);
	}
}
