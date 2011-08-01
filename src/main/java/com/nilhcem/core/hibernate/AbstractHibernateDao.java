package com.nilhcem.core.hibernate;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.util.Assert;

/**
 * Simplify Hibernate use. Every DAO class should extend from it.
 *
 * @author Nilhcem, inspired from http://raykrueger.blogspot.com/2007/09/best-abstracthibernatedao-ever.html
 * @since 1.0
 * @param <E> Hibernate model Element.
 */
public abstract class AbstractHibernateDao<E> {
	private final Class<E> entityClass;
	private final SessionFactory sessionFactory;

	public AbstractHibernateDao(Class<E> entityClass, SessionFactory sessionFactory) {
		Assert.notNull(entityClass, "entityClass must not be null");
		Assert.notNull(sessionFactory, "sessionFactory must not be null");
		this.entityClass = entityClass;
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Create a {@code Criteria} using the current session.
	 *
	 * @return Created criteria.
	 */
	protected final Criteria criteria() {
		return currentSession().createCriteria(entityClass);
	}

	/**
	 * Create a {@code Query} object using the current session.
	 *
	 * @param hql HQL for the Query.
	 * @return Created query.
	 */
	protected final Query query(String hql) {
		return currentSession().createQuery(hql);
	}

	/**
	 * Return the current Hibernate {@code Session}.
	 *
	 * @return Current Hibernate session.
	 */
	protected final Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * Return all elements in a list.
	 *
	 * @return All elements.
	 */
	protected final List<E> all() {
		return list(criteria());
	}

	/**
	 * Return the {@code Entity} class.
	 *
	 * @return Entity class.
	 */
	public final Class<E> getEntityClass() {
		return entityClass;
	}

	/**
	 * List all element from a {@code Criteria}.
	 * Generic suppression wrapper to avoid putting SuppressWarning annotations on DAO.
	 *
	 * @param criteria Criteria we need to list elements from.
	 * @return List of elements.
	 */
	@SuppressWarnings("unchecked")
	protected final List<E> list(Criteria criteria) {
		return criteria.list();
	}

	/**
	 * List all element from a {@code Query}.
	 * Generic suppression wrapper to avoid putting SuppressWarning annotations on DAO.
	 *
	 * @param query Query we need to list elements from.
	 * @return List of elements.
	 */
	@SuppressWarnings("unchecked")
	protected final List<E> list(Query query) {
		return query.list();
	}

	/**
	 * List all element from a {@code Criteria} to an object array.
	 * Generic suppression wrapper to avoid putting SuppressWarning annotations on DAO.
	 *
	 * @param criteria Criteria we need to list elements from.
	 * @return List of elements.
	 */
	@SuppressWarnings("unchecked")
	protected final List<Object[]> listObjectArray(Criteria crit) {
		return crit.list();
	}

	/**
	 * Return a unique result from a {@code Criteria}.
	 * Generic suppression wrapper to avoid putting SuppressWarning annotations on DAO.
	 *
	 * @param criteria Criteria we need to get the element from.
	 * @return Unique element.
	 */
	@SuppressWarnings("unchecked")
	protected final E uniqueResult(Criteria criteria) {
		return (E) criteria.uniqueResult();
	}

	/**
	 * Return a unique result from a {@code Query}.
	 * Generic suppression wrapper to avoid putting SuppressWarning annotations on DAO.
	 *
	 * @param query Query we need to get the element from.
	 * @return Unique element.
	 */
	@SuppressWarnings("unchecked")
	protected final E uniqueResult(Query query) {
		return (E) query.uniqueResult();
	}

	/**
	 * Return an element from its {@code id}.
	 * Generic suppression wrapper to avoid putting SuppressWarning annotations on DAO.
	 *
	 * @param id Id of the element we are searching for.
	 * @return Unique element.
	 */
	@SuppressWarnings("unchecked")
	protected final E get(Serializable id) {
		return (E) currentSession().get(entityClass, id);
	}

	/**
	 * Save an object.
	 *
	 * @param object the object we need to save.
	 */
	public final void save(E object) {
		currentSession().save(object);
	}

	/**
	 * Update an object.
	 *
	 * @param object the object we need to update.
	 */
	public final void update(E object) {
		currentSession().update(object);
	}

	/**
	 * Delete an object.
	 *
	 * @param object the object we need to remove.
	 */
	public final void delete(E object) {
		currentSession().delete(object);
	}
}
