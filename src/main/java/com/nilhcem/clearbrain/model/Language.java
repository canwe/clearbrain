package com.nilhcem.clearbrain.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Contains a persistent entity of an entry from the {@code "languages"} table.
 *
 * <p>
 * A user can display the website in the language he wants.<br />
 * The language of the website will be determined depending on the language of the user.
 * <p>
 *
 * @author Nilhcem
 * @since 1.0
 */
@Entity
@Table(name = "languages")
@SequenceGenerator(name = "seq_lang", sequenceName = "languages_language_id_seq", allocationSize = 1)
public final class Language implements Serializable {
	private static final long serialVersionUID = 1837022456648336570L;
	private Long id;
	private String code;
	private String name;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lang")
	@Column(name = "language_id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "code", nullable = false, length = 8)
	public String getCode() {
		return this.code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "name", nullable = false, length = 64)
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
