package com.nilhcem.clearbrain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Contains a persistent entity of an entry from the {@code "users"} table.
 *
 * <p>
 * {@code User} is the way to call someone who has an account on the website.
 * </p>
 *
 * @author Nilhcem
 * @since 1.0
 */
@Entity
@Table(name = "users")
@SequenceGenerator(name = "seq_user", sequenceName = "users_user_id_seq", allocationSize = 1)
public final class User implements Serializable {
	private static final long serialVersionUID = 1094451576207932271L;
	private Long id;
	private String email;
	private String password;
	private Date registrationDate;
	private Date deleteDate;
	private boolean enabled;
	private Language language;
	private List<Right> rights = new ArrayList<Right>();

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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "registration_date", nullable = false)
	public Date getRegistrationDate() {
		return this.registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "delete_date", nullable = true)
	public Date getDeleteDate() {
		return this.deleteDate;
	}
	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	@Column(name = "enabled")
	public boolean isEnabled() {
		return this.enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "language_id", nullable = false)
	public Language getLanguage() {
		return this.language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}

	@ManyToMany
	@JoinTable(name = "users_rights", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "right_id") })
	public List<Right> getRights() {
		return this.rights;
	}
	public void setRights(List<Right> rights) {
		this.rights = rights;
	}
}
