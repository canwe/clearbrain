package com.nilhcem.core.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import com.nilhcem.model.Right;
import com.nilhcem.model.User;

/**
 * Adapt a User (hibernate model) into a UserDetails (spring security) object.
 *
 * @author Nilhcem
 * @since 1.0
 */
public class UserDetailsAdapter extends org.springframework.security.core.userdetails.User {
	private static final long serialVersionUID = -4267541313252984476L;
	private final Long id;

	public UserDetailsAdapter(User user) {
		super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, toAuthorities(user.getRights()));
		this.id = user.getId();
	}

	/**
	 * Convert a List<Right> into a Collection<GrantedAutority>
	 *
	 * @param rights
	 * @return a Collection<GrantedAuthority> for Spring security
	 */
	private static Collection<GrantedAuthority> toAuthorities(List<Right> rights) {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (Right right : rights)
			authorities.add(new GrantedAuthorityImpl(right.getName()));
		return authorities;
	}

	/**
	 * getId method is used to salt password using id
	 *
	 * @return id
	 */
	public Long getId() {
		return id;
	}
}
