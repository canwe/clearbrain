package com.nilhcem.core.spring;

import com.nilhcem.business.UserBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
		return new UserDetailsAdapter(user);
	}
}
