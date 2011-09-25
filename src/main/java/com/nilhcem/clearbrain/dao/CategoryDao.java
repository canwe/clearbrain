package com.nilhcem.clearbrain.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.nilhcem.clearbrain.core.exception.CategoriesOrderException;
import com.nilhcem.clearbrain.core.hibernate.AbstractHibernateDao;
import com.nilhcem.clearbrain.model.Category;
import com.nilhcem.clearbrain.model.User;

/**
 * Provides methods for accessing {@code Category} data from database.
 *
 * @author Nilhcem
 * @since 1.0
 * @see Category
 */
@Repository
public final class CategoryDao extends AbstractHibernateDao<Category> {
	@Autowired
	public CategoryDao(SessionFactory sessionFactory) {
		super(Category.class, sessionFactory);
	}

	/**
	 * Finds a category from its {@code id}.
	 *
	 * @param user the owner of the category.
	 * @param id the id of the category we need to find.
	 * @return the category we need to find, or {@code null} if not found.
	 */
	public Category getById(User user, Long id) {
		Query query = query("FROM Category WHERE user=:user AND id=:id")
			.setParameter("user", user)
			.setParameter("id", id)
			.setMaxResults(1);
		return uniqueResult(query);
	}

	/**
	 * Finds all the categories owned by the user specified in parameter.
	 *
	 * @param user the owner of the categories.
	 * @return a list of all the user's categories.
	 */
	public List<Category> getCategories(User user) {
		Query query = query("FROM Category WHERE user=:user")
			.setParameter("user", user);
		return list(query);
	}

	/**
	 * Returns the last category for the user specified in parameter.
	 * <p>
	 * Categories are linked lists.<br />
	 * The last category is the one whose next value is {@code null}.
	 * </p>
	 *
	 * @param user the owner of the category.
	 * @return the last category owned by the user, or {@code null} if not found.
	 */
	public Category getLastCategory(User user) {
		Query query = query("FROM Category WHERE user=:user AND next is null")
			.setParameter("user", user)
			.setMaxResults(1);
		return uniqueResult(query);
	}

	/**
	 * Returns the category ordered just before the one in parameter.
	 * <p>
	 * Categories are linked lists.<br />
	 * This method will return a category, whose next value is the category specified in parameter.
	 * </p>
	 *
	 * @param user the owner of the categories.
	 * @param category the category which is sorted just after the one we are searching for.
	 * @return the previous category of the one specified in parameters, or {@code null} if not found.
	 */
	public Category getPreviousCategoryOf(User user, Category category) {
		Query query = query("FROM Category WHERE user=:user AND next=:next")
			.setParameter("user", user)
			.setParameter("next", category)
			.setMaxResults(1);
		return uniqueResult(query);
	}

	/**
	 * Makes sure that categories where properly inserted.
	 * <p>
	 * To make sure of that and since categories are linked lists, if two different categories have the same {@code next} value, it means a problem happened.
	 * </p>
	 *
	 * @param user the owner of the categories.
	 * @throws CategoriesOrderException when two categories have the same {@code next} value, which means something went wrong.
	 */
	public void checkIfCategoriesAreProperlyOrdered(User user) throws CategoriesOrderException {
		Query query = query("FROM Category c1 WHERE c1.user=:user AND c1.nextCategoryId IN (Select c2.nextCategoryId FROM Category c2 WHERE c2.user=:user AND c2.id != c1.id)")
			.setParameter("user", user);
		if (!list(query).isEmpty()) {
			throw new CategoriesOrderException();
		}
	}
}
