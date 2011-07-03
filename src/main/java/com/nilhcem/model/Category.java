package com.nilhcem.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Hibernate mapped class for {@code "categories"} table.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Entity
@Table(name = "categories")
@SequenceGenerator(name = "seq_category", sequenceName = "categories_category_id_seq", allocationSize = 1)
public final class Category {
	private Long id;
	private String name;
	private boolean displayed;
	private Date creationDate;
	private Category next;
	private Long nextCategoryId;
	private User user;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_category")
	@Column(name = "category_id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 64)
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date", nullable = false)
	public Date getCreationDate() {
		return this.creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Column(name = "is_displayed")
	public boolean isDisplayed() {
		return this.displayed;
	}
	public void setDisplayed(boolean displayed) {
		this.displayed = displayed;
	}

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "next_category_id")
	public Category getNext() {
		return this.next;
	}
	public void setNext(Category next) {
		this.next = next;
	}

	@JsonIgnore
	@Column(name = "next_category_id", insertable = false, updatable = false)
	public Long getNextCategoryId() {
		return this.nextCategoryId;
	}
	public void setNextCategoryId(Long nextCategoryId) {
		this.nextCategoryId = nextCategoryId;
	}

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return this.user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
