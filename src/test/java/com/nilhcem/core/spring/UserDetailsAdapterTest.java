package com.nilhcem.core.spring;

import static org.junit.Assert.*;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import com.nilhcem.model.Right;
import com.nilhcem.model.User;

public class UserDetailsAdapterTest {
	private User user = new User();

    @Before
    public void setUp() {
		user.setEmail("test@email.com");
		user.setEnabled(true);
		user.setId(42L);
		user.setPassword("My#p4S$w0rD");

		Right right = new Right();
		right.setName("RIGHT_TEST");
		user.getRights().add(right);
    }

	@Test
	public void testConversionUserIntoUserDetails() {
		UserDetailsAdapter userDetails = new UserDetailsAdapter(user);
		assertEquals(user.getId(), userDetails.getId());
		assertEquals(user.getEmail(), userDetails.getUsername());
		assertEquals(user.isEnabled(), userDetails.isEnabled());
		assertEquals(user.getRights().size(), userDetails.getAuthorities().size());
		Iterator<GrantedAuthority> i = userDetails.getAuthorities().iterator();
		assertEquals(user.getRights().get(0).getName(), i.next().getAuthority());
	}
}
