package com.nilhcem.clearbrain.core.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import com.nilhcem.clearbrain.model.Right;
import com.nilhcem.clearbrain.model.User;

/**
 * <i>(Spring Security)</i> object which adapts a {@code User} into a {@code UserDetails}.
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class UserDetailsAdapter extends org.springframework.security.core.userdetails.User {
	private static final long serialVersionUID = -6770798467418697926L;
	private final Long id;
	private final User hibernateUser;

	/**
	 * Constructs a UserDetailsAdapter using data from a {@code User} object.
	 *
	 * @param user the user we need to take data from, to create a {@code UserDetails} object.
	 */
	public UserDetailsAdapter(User user) {
		super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, toAuthorities(user.getRights()));
		this.hibernateUser = user;
		this.id = user.getId();
	}

	/**
	 * Converts a {@code List<Right>} into a {@code Collection<GrantedAutority>}.
	 *
	 * @param rights the list of rights we need to convert.
	 * @return a {@code Collection<GrantedAuthority>} object, for Spring Security.
	 */
	private static Collection<GrantedAuthority> toAuthorities(List<Right> rights) {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (Right right : rights) {
			authorities.add(new GrantedAuthorityImpl(right.getName()));
		}
		return authorities;
	}

	/**
	 * Gets the id, used during the password salting process.
	 *
	 * @return user's id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Gets the current Hibernate user bean.
	 *
	 * @return the Hibernate user.
	 */
	public User getHibernateUser() {
		return this.hibernateUser;
	}

    /**
     * Specifies if objects are equal or not.
     *
     * <p>
     * Returns {@code true} if the supplied object is a {@code UserDetailsAdapter} instance with the same {@code id} value.<br />
     * In other words, the objects are equal if they have the same id, representing the same principal.
     * </p>
     * @param obj the object who is going to be compared.
     * @return {@code true} if objects are equal.
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			if (obj instanceof UserDetailsAdapter) {
				UserDetailsAdapter otherObj = (UserDetailsAdapter) obj;
				return (id == null ? otherObj.getId() == null : id.equals(otherObj.getId()));
			}
			return super.equals(obj);
		}
		return false;
	}

	/**
	 * Returns the hashcode of the object.
	 *
	 * @return the hashcode of the object.
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
