package com.nilhcem.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Hibernate mapped class for "users" table.
 * 
 * @author Nilhcem
 * @since 1.0
 */
@Entity
@Table(name = "users")
@SequenceGenerator(name = "seq_user", sequenceName = "users_user_id_seq", allocationSize = 1)
public class User {
	protected Long id;
	protected String email;
	protected String password;
	protected boolean enabled;
	protected List<Right> rights = new ArrayList<Right>();

	public User() {}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user")
	@Column(name = "user_id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "email", unique = true, nullable = false, length = 254)
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "password", unique = false, nullable = false, length = 64)
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "enabled")
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@ManyToMany
	@JoinTable(name = "users_rights", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "right_id")})
	public List<Right> getRights() {
		return this.rights;
	}
	public void setRights(List<Right> rights) {
		this.rights = rights;
	}
}
