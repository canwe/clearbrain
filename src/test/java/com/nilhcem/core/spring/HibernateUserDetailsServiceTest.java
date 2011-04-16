package com.nilhcem.core.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/security/applicationContext-security.xml"})
public class HibernateUserDetailsServiceTest {
	@Autowired
	private HibernateUserDetailsService service;

	@Test(expected = UsernameNotFoundException.class)
	public void shouldThrowExceptionWhenUserIsNotFound() {
		service.loadUserByUsername("");
	}

	//TODO: Should test loadUserByUsername once we will have Test data
}
