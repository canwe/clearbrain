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
public final class UserDetailsAdapter extends org.springframework.security.core.userdetails.User {
	private static final long serialVersionUID = -6770798467418697926L;
	private final Long id;
	private final User hibernateUser;

	/**
	 * Constructor.
	 *
	 * @param user The user we need to convert into UserDetails.
	 */
	public UserDetailsAdapter(User user) {
		super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, toAuthorities(user.getRights()));
		this.hibernateUser = user;
		this.id = user.getId();
	}

	/**
	 * Convert a List<Right> into a Collection<GrantedAutority>.
	 *
	 * @param rights The list of rights we need to convert.
	 * @return Collection<GrantedAuthority>, for Spring security.
	 */
	private static Collection<GrantedAuthority> toAuthorities(List<Right> rights) {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (Right right : rights) {
			authorities.add(new GrantedAuthorityImpl(right.getName()));
		}
		return authorities;
	}

	/**
	 * Used to salt password using id.
	 *
	 * @return User's id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Get the current Hibernate user bean.
	 *
	 * @return Hibernate User.
	 */
	public User getHibernateUser() {
		return this.hibernateUser;
	}

    /**
     * Return {@code true} if the supplied object is a {@code UserDetailsAdapter} instance with the
     * same {@code id} value.
     * <p>
     * In other words, the objects are equal if they have the same id, representing the same principal.
     * @param obj The object we need to compare.
     * @return true if objects are equal.
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
	 * Returns the hashcode of the {@code id}.
	 * @return the hashcode of the object.
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
