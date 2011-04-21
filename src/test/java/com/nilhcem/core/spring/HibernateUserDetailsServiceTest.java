package com.nilhcem.core.spring;

import static org.junit.Assert.*;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.nilhcem.dao.RightDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/security/applicationContext-security.xml"})
public class HibernateUserDetailsServiceTest {
	@Autowired
	private HibernateUserDetailsService service;

	@Test(expected = UsernameNotFoundException.class)
	public void shouldThrowExceptionWhenUserIsNotFound() {
		service.loadUserByUsername("");
	}

//	//TODO: Uncomment once we will have test data
//	@Test
//	public void loadUserByUsernameShouldReturnAUserDetailsObject() {
//		String username = "test@2ndbrn.com"; //TODO: Insert here test email
//		UserDetails springUser = service.loadUserByUsername(username);
//		assertNotNull(springUser);
//		assertEquals(springUser.getUsername(), username);
//
//		//Check this user has the default right
//		GrantedAuthority userAuth = new GrantedAuthorityImpl(RightDao.RIGHT_USER);
//		Collection<GrantedAuthority> authorities = springUser.getAuthorities();
//		assertTrue(authorities.contains(userAuth));
//	}
}
