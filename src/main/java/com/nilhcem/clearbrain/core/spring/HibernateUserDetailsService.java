package com.nilhcem.clearbrain.core.spring;

import com.nilhcem.clearbrain.business.UserBo;
import com.nilhcem.clearbrain.core.hibernate.TransactionalReadOnly;
import com.nilhcem.clearbrain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Handles authentication, using Hibernate. Used by Spring Security.
 * <p>
 * Custom {@code UserDetailsService} bean for SpringSecurity.<br />
 * <b>Note:</b> Do not make this class as {@code final}, otherwise cglib will throw an exception when it will try to make a proxy.
 * </p>
 *
 * @author Nilhcem
 * @since 1.0
 */
@Service("userDetailsService")
@TransactionalReadOnly
public /*no final*/ class HibernateUserDetailsService implements UserDetailsService {
	@Autowired
	private UserBo userBo;

	/**
	 * Finds the user who is authenticating in the database.
	 *
	 * @param username the email of the user who is authenticating.
	 * @return a userDetails object which contains the user's data (login/password/enabled/authorities).
	 * @throws UsernameNotFoundException if we can't find the user from the database.
	 */
	public UserDetails loadUserByUsername(String username) {
		User user = userBo.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return new UserDetailsAdapter(user);
	}
}
