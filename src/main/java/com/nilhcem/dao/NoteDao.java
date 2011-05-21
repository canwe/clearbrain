package com.nilhcem.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.nilhcem.core.hibernate.AbstractHibernateDao;
import com.nilhcem.model.Note;

/**
 * DAO class for accessing Note data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Repository
public class NoteDao extends AbstractHibernateDao<Note> {
	@Autowired
	public NoteDao(SessionFactory sessionFactory) {
		super(Note.class, sessionFactory);
	}
}
