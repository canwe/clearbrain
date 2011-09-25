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

/**
 * Contains a persistent entity of an entry from the {@code "memos"} table.
 *
 * <p>
 * A Quick memo is a text a user can modify easily.<br />
 * It can contain many sentences and can have HTML code.<br />
 * It could be useful to gather a lot of different ideas.
 * </p>
 *
 * @author Nilhcem
 * @since 1.0
 */
@Entity
@Table(name = "memos")
@SequenceGenerator(name = "seq_memo", sequenceName = "memos_memo_id_seq", allocationSize = 1)
public final class QuickMemo {
	private Long id;
	private String content;
	private Date saveDate;
	private User user;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_memo")
	@Column(name = "memo_id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "content", nullable = false)
	public String getContent() {
		return this.content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "save_date", nullable = true)
	public Date getSaveDate() {
		return this.saveDate;
	}
	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return this.user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
