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
		assertEquals(userDetails.getId(), user.getId());
		assertEquals(userDetails.getUsername(), user.getEmail());
		assertEquals(userDetails.isEnabled(), user.isEnabled());
		assertEquals(userDetails.getAuthorities().size(), user.getRights().size());
		Iterator<GrantedAuthority> i = userDetails.getAuthorities().iterator();
		assertEquals(i.next().getAuthority(), user.getRights().get(0).getName());
	}
}
