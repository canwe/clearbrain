package com.nilhcem.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Hibernate mapped class for "rights" table.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Entity
@Table(name = "rights")
@SequenceGenerator(name = "seq_right", sequenceName = "rights_right_id_seq", allocationSize = 1)
public class Right implements Serializable {
	private static final long serialVersionUID = 2171721794634296671L;
	private Long id;
	private String name;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_right")
	@Column(name = "right_id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
