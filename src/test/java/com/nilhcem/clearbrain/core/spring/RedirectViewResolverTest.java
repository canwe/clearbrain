package com.nilhcem.clearbrain.core.spring;

import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

public class RedirectViewResolverTest {
	private final RedirectViewResolver resolver = new RedirectViewResolver();

	@Test
	public void testRedirectViewResolverOrderShouldBeTheFirstOne() {
		assertEquals(Integer.MIN_VALUE, resolver.getOrder());
	}

	@Test
	public void testNullWhenViewNameDoesntStartByRedirectPrefix() throws Exception {
		assertNull(resolver.resolveViewName("MyView", null));
	}

	@Test
	public void testRedirectWhenViewNameStartsByRedirectPrefix() throws Exception {
		final String url = "my-view";
		View myView = resolver.resolveViewName(RedirectViewResolver.REDIRECT_URL_PREFIX + url, null);
		assertNotNull(myView);
		assertEquals(RedirectView.class, myView.getClass());
		assertEquals(url, ((RedirectView)myView).getUrl());
	}
}
