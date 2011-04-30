package com.nilhcem.core.spring;

import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

public class RedirectViewResolverTest {
	private RedirectViewResolver resolver = new RedirectViewResolver();

	@Test
	public void redirectViewResolverOrderShouldBeTheFirstOne() {
		assertEquals(Integer.MIN_VALUE, resolver.getOrder());
	}

	@Test
	public void testWhenViewNameDoesntStartByRedirectPrefix() throws Exception {
		assertNull(resolver.resolveViewName("MyView", null));
	}

	@Test
	public void shoudRedirectWhenViewNameStartsByRedirectPrefix() throws Exception {
		String url = "my-view";
		View myView = resolver.resolveViewName(RedirectViewResolver.REDIRECT_URL_PREFIX + url, null);
		assertNotNull(myView);
		assertEquals(RedirectView.class, myView.getClass());
		assertEquals(url, ((RedirectView)myView).getUrl());
	}
}
