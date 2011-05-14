package com.nilhcem.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.nilhcem.core.hibernate.CustomHibernateDaoSupport;
import com.nilhcem.model.Category;
import com.nilhcem.model.User;

/**
 * DAO class for accessing Category data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Repository("categoryDao")
public class CategoryDao extends CustomHibernateDaoSupport<Category> {
	/**
	 * Find a category from its id.
	 *
	 * @param user User of the category.
	 * @param id Category's id.
	 * @return Category.
	 */
	@SuppressWarnings("unchecked")
	public Category getById(User user, Long id) {
		List<Category> categories = getHibernateTemplate().find("FROM Category WHERE user=? AND id=?", user, id);
		if (categories.isEmpty())
			return null;
		return categories.get(0);
	}

	/**
	 * Find all the categories owned by user
	 *
	 * @param user Owner of the categories we are searching for.
	 * @return List of categories.
	 */
	@SuppressWarnings("unchecked")
	public List<Category> getCategories(User user) {
		return getHibernateTemplate().find("FROM Category WHERE user=?", user);
	}

	/**
	 * Return last category for user u.
	 *
	 * @param user User of the category
	 * @return Last category owned by user, or null if not found
	 */
	@SuppressWarnings("unchecked")
	public Category getLastCategory(User user) {
		List<Category> categories = getHibernateTemplate().find("FROM Category WHERE user=? AND next is null", user);
		if (categories.isEmpty())
			return null;
		return categories.get(0);
	}

	/**
	 * Return the category ordered just before the one in parameter.
	 *
	 * @param user User of the category we are searching for.
	 * @param category The category which is after the one we are searching for.
	 * @return The previous category of the one specified in parameters, or null if not found.
	 */
	@SuppressWarnings("unchecked")
	public Category getPreviousCategoryOf(User user, Category category) {
		List<Category> categories = getHibernateTemplate().find("FROM Category WHERE user=? AND next=?", user, category);
		if (categories.isEmpty())
			return null;
		return categories.get(0);
	}

////	@SuppressWarnings("unchecked")
//	public void checkIfCategoriesAreCorrectlyOrdered(User user) throws Exception {
////		List<Category> categories = getHibernateTemplate().find("FROM Category c1 WHERE c1.user=? AND c1.nextCategoryId IN (Select c2.nextCategoryId FROM Category c2 WHERE c2.user=? AND c2.id != c1.id)", user, user);
////		if (!categories.isEmpty())
//			throw new Exception("An error occured while updating categories positions");
//	}
}
