package com.nilhcem.clearbrain.model;

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
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 * Contains a persistent entity of an entry from the {@code "notes"} table.
 *
 * <p>
 * A note is a task a user should do.<br />
 * The note can be resolved, this means the user did the task he had to do.<br />
 * A note can belong to a category, or can be unclassified.
 * </p>
 *
 * @author Nilhcem
 * @since 1.0
 */
@Entity
@Indexed
@Table(name = "notes")
@SequenceGenerator(name = "seq_note", sequenceName = "notes_note_id_seq", allocationSize = 1)
public final class Note {
	private Long id;
	private String name;
	private Date creationDate;
	private Date dueDate;
	private Date resolvedDate;
	private Category category;
	private User user;

	@Id
	@DocumentId
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_note")
	@Column(name = "note_id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 255)
	@Field(index = Index.TOKENIZED)
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

	@Temporal(TemporalType.DATE)
	@Column(name = "due_date", nullable = true)
	public Date getDueDate() {
		return this.dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "resolved_date")
	public Date getResolvedDate() {
		return this.resolvedDate;
	}
	public void setResolvedDate(Date resolvedDate) {
		this.resolvedDate = resolvedDate;
	}

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	@IndexedEmbedded
	public Category getCategory() {
		return this.category;
	}
	public void setCategory(Category category) {
		this.category = category;
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
