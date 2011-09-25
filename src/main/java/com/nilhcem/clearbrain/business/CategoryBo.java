package com.nilhcem.clearbrain.business;

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
import com.nilhcem.clearbrain.core.exception.CategoriesOrderException;
import com.nilhcem.clearbrain.core.hibernate.TransactionalReadOnly;
import com.nilhcem.clearbrain.core.hibernate.TransactionalReadWrite;
import com.nilhcem.clearbrain.dao.CategoryDao;
import com.nilhcem.clearbrain.model.Category;
import com.nilhcem.clearbrain.model.User;

/**
 * Provides methods for dealing with {@code Category} objects.
 *
 * @author Nilhcem
 * @since 1.0
 * @see Category
 */
@Service
@TransactionalReadOnly
public /*no final*/ class CategoryBo {
	@Autowired
	private CategoryDao dao;
	private final Logger logger = LoggerFactory.getLogger(CategoryBo.class);

	/**
	 * Finds the first category from an unsorted list of categories.
	 * <p>
	 * Categories's order is done using linked list.<br />
	 * Since the list is unsorted, we have to browse it to get the first category.<br />
	 * The first category is the one which is not referenced as "next category" of any other category.
	 * </p>
	 *
	 * @param categories an unsorted list of categories.
	 * @return the first category of the unsorted list.
	 */
	private Category getFirstCategory(List<Category> categories) {
		// Store all 'Next category id' values
		Set<Long> next = new HashSet<Long>();
		for (Category c : categories) {
			next.add(c.getNextCategoryId());
		}

		// Find the element which is not referenced in the 'next category' set. It should be the first category
		for (Category c : categories) {
			if (!next.contains(c.getId())) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Returns a sorted list of the categories owned by the user specified in parameters.
	 *
	 * @param user the owner of the categories.
	 * @return a sorted list of the user's categories.
	 */
	public List<Category> getSortedCategories(User user) {
		// Put all categories in a HashMap
		List<Category> allCategories = dao.getCategories(user);

		Map<Long, Category> mapCategories = new HashMap<Long, Category>();
		for (Category cat : allCategories) {
			mapCategories.put(cat.getId(), cat);
		}

		// Sort the categories
		List<Category> sortedCategories = new ArrayList<Category>();
		Category category = getFirstCategory(allCategories);
		while (category != null) {
			sortedCategories.add(category);
			category = mapCategories.get(category.getNextCategoryId());
		}

		return sortedCategories;
	}

	/**
	 * Creates a new category object using the name specified in parameters, and saves the category in database.
	 * <p>
	 * Creating a category means adding it in database and linking the previous last category's next value to the new created one.
	 * </p>
	 *
	 * @param user the owner of the category.
	 * @param categoryName the name of the category we want to add.
	 * @return the new added category.
	 */
	@TransactionalReadWrite
	public Category addCategory(User user, String categoryName) {
		logger.debug("Add category {}", categoryName);

		// Get last category to update its 'next' value
		Category last = dao.getLastCategory(user);

		// Create and add the category
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
	 * Deletes a category identified by its id, sent in parameter.
	 * <p>
	 * Deleting a category requires two steps:
	 * <ul>
	 *   <li>We should modify the category before the one we want to delete, to modify the 'next' reference (since categories are linked lists).</li>
	 *   <li>We should remove the category in database.</li>
	 * </ul>
	 * </p>
	 *
	 * @param user the owner of the category.
	 * @param categoryId the id of the category we want to remove.
	 */
	@TransactionalReadWrite
	public void removeCategory(User user, Long categoryId) {
		logger.debug("Remove category {}", categoryId);
		Category toRemove = dao.getById(user, categoryId);

		// Get previous category and update next value
		Category previous = dao.getPreviousCategoryOf(user, toRemove);
		if (previous != null) {
			previous.setNext(toRemove.getNext());
			dao.update(previous);
		}
		dao.delete(toRemove);
	}

	/**
	 * Renames a specified category.
	 *
	 * @param user the owner of the categories.
	 * @param categoryId the id of the category we want to rename.
	 * @param newName the category's new name.
	 */
	@TransactionalReadWrite
	public void renameCategory(User user, Long categoryId, String newName) {
		logger.debug("Rename category {}", categoryId);
		Category toRename = dao.getById(user, categoryId);
		toRename.setName(newName);
		dao.update(toRename);
	}

	/**
	 * Updates a category's position.
	 * <p>
	 * Users can change the display order of their categories.<br />
	 * To update the position of a category, we need to have three information:
	 * <ul>
	 *   <li> The id of the category we want to move.</li>
	 *   <li> The id of another category, for reference.</li>
	 *   <li> A value to know if the category will be placed before or after the category in reference.</li>
	 * </ul>
	 * </p>
	 *
	 * @param user the owner of the categories.
	 * @param catId the id of the category which will be moved.
	 * @param refId the id of a category marked as reference.
	 * @param before a boolean to decide if the new category will be before ({@code true}) or after ({@code false}) the one in reference.
	 * @throws CategoriesOrderException if a problem happened while updating the category's position. In that case, transaction will be rolled-back.
	 */
	@TransactionalReadWrite
	public void updatePosition(User user, Long catId, Long refId, boolean before) throws CategoriesOrderException {
		if (logger.isDebugEnabled()) {
			logger.debug("Update position: Category {} will be {} {}", new Object[] {catId, (before ? "before" : "after"), refId});
		}

		Category curCat = dao.getById(user, catId);
		Category prevOfCurCat = dao.getPreviousCategoryOf(user, curCat);
		Category oldCat = dao.getById(user, refId);

		// Update positions
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

		// Check if new order has a sense...
		dao.checkIfCategoriesAreProperlyOrdered(user);
	}

	/**
	 * Returns a map of categories where the {@code key} is the category's id, and the {@code value} is the category's name.
	 *
	 * @param user the owner of the categories.
	 * @return a map of categories. The key represents the category's id, and the value the category's name.
	 */
	public Map<Long, String> getIdAndNameForEachCategory(User user) {
		Map<Long, String> map = new HashMap<Long, String>();
		List<Category> categories = dao.getCategories(user);

		for (Category category : categories) {
			map.put(category.getId(), category.getName());
		}
		return map;
	}
}
