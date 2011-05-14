package com.nilhcem.core.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public class HibernateUserDetailsServiceTest {
	@Autowired
	private HibernateUserDetailsService service;

	@Test(expected = UsernameNotFoundException.class)
	public void shouldThrowExceptionWhenUserIsNotFound() {
		service.loadUserByUsername("");
	}
}
