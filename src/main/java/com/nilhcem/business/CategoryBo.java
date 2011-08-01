package com.nilhcem.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nilhcem.core.exception.CategoriesOrderException;
import com.nilhcem.core.hibernate.TransactionalReadOnly;
import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.dao.CategoryDao;
import com.nilhcem.model.Category;
import com.nilhcem.model.User;

/**
 * Business class for accessing {@code Category} data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Service
@TransactionalReadOnly
public class CategoryBo {
	@Autowired
	private CategoryDao dao;
	private final Logger logger = LoggerFactory.getLogger(CategoryBo.class);

	/**
	 * Return the first category from the list.
	 *
	 * @param categories List of unsorted categories.
	 * @return The first category (the one which is not in the 'next' columns of the other categories).
	 */
	private Category getFirstCategory(List<Category> categories) {
		//Store 'Next category id' column
		Set<Long> next = new HashSet<Long>();
		for (Category c : categories) {
			next.add(c.getNextCategoryId());
		}

		//Find element which is not in the next column (which means the first one)
		for (Category c : categories) {
			if (!next.contains(c.getId())) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Get categories ordered by next value.
	 *
	 * @param user Owner of the categories.
	 * @return Sorted categories list.
	 */
	public List<Category> getSortedCategories(User user) {
		//Get all categories in a HashMap
		List<Category> allCategories = dao.getCategories(user);

		Map<Long, Category> mapCategories = new HashMap<Long, Category>();
		for (Category cat : allCategories) {
			mapCategories.put(cat.getId(), cat);
		}

		//Sort categories
		List<Category> sortedCategories = new ArrayList<Category>();
		Category category = getFirstCategory(allCategories);
		while (category != null) {
			sortedCategories.add(category);
			category = mapCategories.get(category.getNextCategoryId());
		}

		return sortedCategories;
	}

	/**
	 * Add a category in database and link previous last category's next value to the new created one.
	 *
	 * @param user Owner of the category.
	 * @param categoryName The name of the category we want to add.
	 * @return The new added category.
	 */
	@TransactionalReadWrite
	public Category addCategory(User user, String categoryName) {
		logger.debug("Add category {}", categoryName);

		//Get last category to update the 'next' value
		Category last = dao.getLastCategory(user);

		//Add category
		Category category = new Category();
		category.setName(categoryName);
		category.setUser(user);
		category.setCreationDate(Calendar.getInstance().getTime());
		dao.save(category);

		if (last != null) {
			last.setNext(category);
			dao.update(last);
		}
		return category;
	}

	/**
	 * Remove a category from database and update previous category to modify 'next' field.
	 *
	 * @param user Owner of the category.
	 * @param categoryId The category we want to remove.
	 */
	@TransactionalReadWrite
	public void removeCategory(User user, Long categoryId) {
		logger.debug("Remove category {}", categoryId);
		Category toRemove = dao.getById(user, categoryId);

		//Get previous category and update next value
		Category previous = dao.getPreviousCategoryOf(user, toRemove);
		if (previous != null) {
			previous.setNext(toRemove.getNext());
			dao.update(previous);
		}
		dao.delete(toRemove);
	}

	/**
	 * Rename a category.
	 *
	 * @param user Owner of the category.
	 * @param categoryId The category we want to rename.
	 * @param newName The category's new name.
	 */
	@TransactionalReadWrite
	public void renameCategory(User user, Long categoryId, String newName) {
		logger.debug("Rename category {}", categoryId);
		Category toRename = dao.getById(user, categoryId);
		toRename.setName(newName);
		dao.update(toRename);
	}

	/**
	 * Update a category's position.
	 *
	 * @param user Owner of the categories.
	 * @param catId The category's id we need to update.
	 * @param oldId The previous category (the new one will be before or after this one).
	 * @param before The category will be before (== true) or after (== false) prevId.
	 */
	@TransactionalReadWrite
	public void updatePosition(User user, Long catId, Long oldId, boolean before) throws CategoriesOrderException {
		if (logger.isDebugEnabled()) {
			logger.debug("Update position: Category {} will be {} {}", new Object[] {catId, (before ? "before" : "after"), oldId});
		}

		Category curCat = dao.getById(user, catId);
		Category prevOfCurCat = dao.getPreviousCategoryOf(user, curCat);
		Category oldCat = dao.getById(user, oldId);

		//update positions
		if (prevOfCurCat != null) {
			prevOfCurCat.setNext(curCat.getNext());
			dao.update(prevOfCurCat);
		}
		if (before) {
			Category prevOfOldCat = dao.getPreviousCategoryOf(user, oldCat);
			if (prevOfOldCat != null) {
				prevOfOldCat.setNext(curCat);
				dao.update(prevOfOldCat);
			}
			curCat.setNext(oldCat);
		} else {
			curCat.setNext(oldCat.getNext());
			oldCat.setNext(curCat);
			dao.update(oldCat);
		}
		dao.update(curCat);

		//Check if new order has a sense...
		dao.checkIfCategoriesAreProperlyOrdered(user);
	}
}
