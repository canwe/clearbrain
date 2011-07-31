package com.nilhcem.business;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import liquibase.exception.LiquibaseException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.nilhcem.core.exception.CategoriesOrderException;
import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.core.test.AbstractDbTest;
import com.nilhcem.model.Category;
import com.nilhcem.model.Note;
import com.nilhcem.model.User;

public class CategoryBoTest extends AbstractDbTest {
	private static final String[] CATEGORIES = new String[] {"1", "2", "3", "4", "5"};
	private static final Logger logger = LoggerFactory.getLogger(CategoryBoTest.class);

	@Autowired
	private CategoryBo categoryBo;
	@Autowired
	private NoteBo noteBo;

	@Test
	public void testGetSortedCategories() throws SQLException, LiquibaseException {
		final String email = "CategoryBoTest@testGetSortedCategories";
		User user = testUtils.createTestUserNewPropagation(email);
		addCategories(user);
		checkIfCategoriesAreProperlyAddedAndSorted(user, CategoryBoTest.CATEGORIES);
	}

	@Test
	@TransactionalReadWrite
	public void testAddCategory() {
		final String categoryName = "CategoryName";
		User user = testUtils.createTestUser("CategoryBoTest@testAddCategory");

		logger.debug("Add category: Add first category");
		Date before = testUtils.getDateBeforeTest();
		Category category = categoryBo.addCategory(user, categoryName);
		Date after = testUtils.getDateAfterTest();
		assertNull(category.getNext());
		assertEquals(categoryName, category.getName());
		assertTrue(testUtils.checkDateBetween(category.getCreationDate(), before, after));

		logger.debug("Add category: Check next field is properly set");
		Category category2 = categoryBo.addCategory(user, "cat2");
		assertNotNull(category.getNext());
		assertSame(category2, category.getNext());
	}

	@Test
	public void testRemoveCategory() {
		User user = testUtils.createTestUserNewPropagation("CategoryBoTest@testRemoveCategory");
		addCategories(user);
		List<Category> cats = categoryBo.getSortedCategories(user);
		logger.debug("Remove categories");
		categoryBo.removeCategory(user, cats.get(3).getId());
		checkIfCategoriesAreProperlyAddedAndSorted(user, new String[] {"1", "2", "3", "5"});
		categoryBo.removeCategory(user, cats.get(1).getId());
		checkIfCategoriesAreProperlyAddedAndSorted(user, new String[] {"1", "3", "5"});
		categoryBo.removeCategory(user, cats.get(4).getId());
		checkIfCategoriesAreProperlyAddedAndSorted(user, new String[] {"1", "3"});
		categoryBo.removeCategory(user, cats.get(0).getId());
		checkIfCategoriesAreProperlyAddedAndSorted(user, new String[] {"3"});
		categoryBo.removeCategory(user, cats.get(2).getId());
		checkIfCategoriesAreProperlyAddedAndSorted(user, new String[] {});
	}

	@Test
	public void testRemoveCategoryWithNote() {
		User user = testUtils.createTestUserNewPropagation("CategoryBoTest@testRemoveCategoryWithNote");
		Category category = categoryBo.addCategory(user, "Category");
		Note note = noteBo.addNote(user, "note", category.getId());
		assertEquals(category.getId(), note.getCategory().getId());
		categoryBo.removeCategory(user, category.getId());
		note = noteBo.getNoteById(user, note.getId());
		assertNull(note.getCategory());
	}

	@Test
	@TransactionalReadWrite
	public void testRenameCategory() {
		final String categoryName = "CategoryName";
		final String newName = "NewName";
		User user = testUtils.createTestUser("CategoryBoTest@testRenameCategory");
		logger.debug("Rename category: Create category");
		Category category = categoryBo.addCategory(user, categoryName);

		logger.debug("Rename category: Rename category");
		assertFalse(categoryName.equals(newName));
		assertEquals(categoryName, category.getName());
		categoryBo.renameCategory(user, category.getId(), newName);
		Category renamed = categoryBo.getSortedCategories(user).get(0);
		assertEquals(newName, renamed.getName());
	}

	@Test
	public void testUpdatePosition() throws CategoriesOrderException {
		User user = testUtils.createTestUserNewPropagation("CategoryBoTest@testUpdatePosition");
		addCategories(user);
		List<Category> cats = categoryBo.getSortedCategories(user);

		logger.debug("Update position");
		categoryBo.updatePosition(user, cats.get(1).getId(), cats.get(0).getId(), true);
		checkIfCategoriesAreProperlyAddedAndSorted(user, new String[] {"2", "1", "3", "4", "5"});
		categoryBo.updatePosition(user, cats.get(1).getId(), cats.get(4).getId(), false);
		checkIfCategoriesAreProperlyAddedAndSorted(user, new String[] {"1", "3", "4", "5", "2"});
		categoryBo.updatePosition(user, cats.get(4).getId(), cats.get(2).getId(), true);
		checkIfCategoriesAreProperlyAddedAndSorted(user, new String[] {"1", "5", "3", "4", "2"});
		categoryBo.updatePosition(user, cats.get(4).getId(), cats.get(2).getId(), false);
		checkIfCategoriesAreProperlyAddedAndSorted(user, new String[] {"1", "3", "5", "4", "2"});
		categoryBo.updatePosition(user, cats.get(3).getId(), cats.get(4).getId(), true);
		checkIfCategoriesAreProperlyAddedAndSorted(user, new String[] {"1", "3", "4", "5", "2"});
		categoryBo.updatePosition(user, cats.get(1).getId(), cats.get(2).getId(), true);
		checkIfCategoriesAreProperlyAddedAndSorted(user, new String[] {"1", "2", "3", "4", "5"});
	}

	private void addCategories(User user) {
		logger.debug("Add categories");
		for (String category : CategoryBoTest.CATEGORIES) {
			categoryBo.addCategory(user, category);
		}
	}

	private void checkIfCategoriesAreProperlyAddedAndSorted(User user, String[] expectedOrder) {
		logger.debug("Check if categories are properly added and sorted");
		List<Category> sortedCategories = categoryBo.getSortedCategories(user);
		assertEquals(expectedOrder.length, sortedCategories.size());
		
		int idx = 0;
		for (Category category : sortedCategories) {
			assertEquals(category.getName(), expectedOrder[idx++]);
			if (idx == expectedOrder.length) {
				assertNull(category.getNext());
			}
			else {
				assertNotNull(category.getNext());
			}
		}
	}
}
