package com.nilhcem.controller;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
}
