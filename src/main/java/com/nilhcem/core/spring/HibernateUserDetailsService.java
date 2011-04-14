package com.nilhcem.core.spring;

import java.util.ArrayList;
import java.util.Collection;
import com.nilhcem.business.UserBo;
import com.nilhcem.model.Right;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom userDetailsService bean for SpringSecurity to handle authentication using Hibernate.
 * 
 * @author Nilhcem
 * @since 1.0
 */
@Service("userDetailsService")
public class HibernateUserDetailsService implements UserDetailsService {
	@Autowired
	private UserBo userBo;

	/**
	 * Find in the database the user who wants to authenticate.
	 * 
	 * @param username the email of the user who authenticates.
	 * @return a UserDetails object which contains the user's data (login/password/enabled/authorities)
	 * @throws UsernameNotFoundException if we can't find the user from the database
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		com.nilhcem.model.User user = userBo.findByEmail(username);
		if (user == null)
			throw new UsernameNotFoundException("user not found");

		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (Right auth : user.getRights())
			authorities.add(new GrantedAuthorityImpl(auth.getName()));

		User springUser = new User(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, authorities);
		return (springUser);
	}
}
