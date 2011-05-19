package com.nilhcem.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.nilhcem.core.hibernate.AbstractHibernateDao;
import com.nilhcem.model.Category;
import com.nilhcem.model.User;

/**
 * DAO class for accessing Category data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Repository
public class CategoryDao extends AbstractHibernateDao<Category> {
	@Autowired
	public CategoryDao(SessionFactory sessionFactory) {
		super(Category.class, sessionFactory);
	}

	/**
	 * Find a category from its id.
	 *
	 * @param user User of the category.
	 * @param id Category's id.
	 * @return Category.
	 */
	public Category getById(User user, Long id) {
		Query query = query("FROM Category WHERE user=:user AND id=:id")
			.setParameter("user", user)
			.setParameter("id", id)
			.setMaxResults(1);
		return uniqueResult(query);
	}

	/**
	 * Find all the categories owned by user.
	 *
	 * @param user Owner of the categories we are searching for.
	 * @return List of categories.
	 */
	public List<Category> getCategories(User user) {
		Query query = query("FROM Category WHERE user=:user")
			.setParameter("user", user);
		return list(query);
	}

	/**
	 * Return last category for user u.
	 *
	 * @param user User of the category.
	 * @return Last category owned by user, or null if not found.
	 */
	public Category getLastCategory(User user) {
		Query query = query("FROM Category WHERE user=:user AND next is null")
			.setParameter("user", user)
			.setMaxResults(1);
		return uniqueResult(query);
	}

	/**
	 * Return the category ordered just before the one in parameter.
	 *
	 * @param user User of the category we are searching for.
	 * @param category The category which is after the one we are searching for.
	 * @return The previous category of the one specified in parameters, or null if not found.
	 */
	public Category getPreviousCategoryOf(User user, Category category) {
		Query query = query("FROM Category WHERE user=:user AND next=:next")
			.setParameter("user", user)
			.setParameter("next", category)
			.setMaxResults(1);
		return uniqueResult(query);
	}

	/**
	 * Simple check to verify categories were properly inserted.
	 * @param user User of the categories.
	 * @throws Exception If two categories have the same 'next' value, which means something was wrong. Exception will be used to do a rollback.
	 */
	public void checkIfCategoriesAreProperlyOrdered(User user) throws Exception {
		Query query = query("FROM Category c1 WHERE c1.user=:user AND c1.nextCategoryId IN (Select c2.nextCategoryId FROM Category c2 WHERE c2.user=:user AND c2.id != c1.id)")
			.setParameter("user", user);
		if (!list(query).isEmpty())
			throw new Exception("An error occured while updating categories positions");
	}
}
