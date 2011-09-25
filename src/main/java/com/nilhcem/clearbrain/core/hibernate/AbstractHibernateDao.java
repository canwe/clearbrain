package com.nilhcem.clearbrain.core.hibernate;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.util.Assert;

/**
 * Superclass to simplify Hibernate use, every DAO should extend from it.
 * <p>
 * Inspired from http://raykrueger.blogspot.com/2007/09/best-abstracthibernatedao-ever.html
 * </p>
 *
 * @author Nilhcem
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
	 * Creates a {@code Criteria} using the current session.
	 *
	 * @return the created criteria.
	 */
	protected final Criteria criteria() {
		return currentSession().createCriteria(entityClass);
	}

	/**
	 * Creates a {@code Query} object using the current session.
	 *
	 * @param hql HQL for the query.
	 * @return the created query.
	 */
	protected final Query query(String hql) {
		return currentSession().createQuery(hql);
	}

	/**
	 * Returns the current Hibernate {@code Session}.
	 *
	 * @return the current Hibernate session.
	 */
	protected final Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * Returns all elements in a list.
	 *
	 * @return all elements.
	 */
	protected final List<E> all() {
		return list(criteria());
	}

	/**
	 * Returns the {@code Entity} class.
	 *
	 * @return the entity class.
	 */
	public final Class<E> getEntityClass() {
		return entityClass;
	}

	/**
	 * Lists all elements from a {@code Criteria}.
	 * <p>
	 * Generic suppression wrapper to avoid putting SuppressWarning annotations on DAO.
	 * </p>
	 *
	 * @param criteria the criteria we need to list elements from.
	 * @return a list of elements.
	 */
	@SuppressWarnings("unchecked")
	protected final List<E> list(Criteria criteria) {
		return criteria.list();
	}

	/**
	 * Lists all element from a {@code Query}.
	 * <p>
	 * Generic suppression wrapper to avoid putting SuppressWarning annotations on DAO.
	 * </p>
	 *
	 * @param query the query we need to list elements from.
	 * @return a list of elements.
	 */
	@SuppressWarnings("unchecked")
	protected final List<E> list(Query query) {
		return query.list();
	}

	/**
	 * Lists all element from a {@code Criteria} to an object array.
	 * <p>
	 * Generic suppression wrapper to avoid putting SuppressWarning annotations on DAO.
	 * </p>
	 *
	 * @param criteria the criteria we need to list elements from.
	 * @return a list of elements.
	 */
	@SuppressWarnings("unchecked")
	protected final List<Object[]> listObjectArray(Criteria criteria) {
		return criteria.list();
	}

	/**
	 * Returns a unique result from a {@code Criteria}.
	 * <p>
	 * Generic suppression wrapper to avoid putting SuppressWarning annotations on DAO.
	 * </p>
	 *
	 * @param criteria the criteria we need to get the result from.
	 * @return a unique element.
	 */
	@SuppressWarnings("unchecked")
	protected final E uniqueResult(Criteria criteria) {
		return (E) criteria.uniqueResult();
	}

	/**
	 * Returns a unique result from a {@code Query}.
	 * <p>
	 * Generic suppression wrapper to avoid putting SuppressWarning annotations on DAO.
	 * </p>
	 *
	 * @param query the query we need to get the element from.
	 * @return a unique element.
	 */
	@SuppressWarnings("unchecked")
	protected final E uniqueResult(Query query) {
		return (E) query.uniqueResult();
	}

	/**
	 * Returns an element from its {@code id}.
	 * <p>
	 * Generic suppression wrapper to avoid putting SuppressWarning annotations on DAO.
	 * </p>
	 *
	 * @param id the id of the element we are searching for.
	 * @return a unique element.
	 */
	@SuppressWarnings("unchecked")
	protected final E get(Serializable id) {
		return (E) currentSession().get(entityClass, id);
	}

	/**
	 * Saves an object.
	 *
	 * @param object the object we need to save.
	 */
	public final void save(E object) {
		currentSession().save(object);
	}

	/**
	 * Updates an object.
	 *
	 * @param object the object we need to update.
	 */
	public final void update(E object) {
		currentSession().update(object);
	}

	/**
	 * Deletes an object.
	 *
	 * @param object the object we need to remove.
	 */
	public final void delete(E object) {
		currentSession().delete(object);
	}
}
