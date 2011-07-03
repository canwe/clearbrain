package com.nilhcem.business;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.nilhcem.core.exception.CategoriesOrderException;
import com.nilhcem.core.test.TestUtils;
import com.nilhcem.model.Category;
import com.nilhcem.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public class CategoryBoTest {
	private static final String[] CATEGORIES = new String[] {"1", "2", "3", "4", "5"};

	@Autowired
	private TestUtils testUtils;
	@Autowired
	private CategoryBo service;

	@Test
	public void testCategories() throws CategoriesOrderException {
		User user = testUtils.getTestUser();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, -1);
		Date before = cal.getTime();
		addCategories(user);
		cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 1);
		Date after = cal.getTime();
		List<Category> sortedCategories = checkIfCategoriesAreCorrectlyAddedAndSorted(user, CATEGORIES);
		checkCreationDate(before, after, sortedCategories.get(0));
//		testShowHideCategories(user, sortedCategories);
		moveCategories(user, sortedCategories);
		removeCategories(user, sortedCategories);
	}

	private void addCategories(User user) {
		for (String category : CATEGORIES) {
			service.addCategory(user, category);
		}
	}

	private List<Category> checkIfCategoriesAreCorrectlyAddedAndSorted(User user, String[] expectedOrder) {
		List<Category> sortedCategories = service.getSortedCategories(user);
		assertEquals(expectedOrder.length, sortedCategories.size());
		
		int idx = 0;
		for (Category category : sortedCategories) {
			assertEquals(category.getName(), expectedOrder[idx++]);
			assertTrue(category.isDisplayed());
			if (idx == expectedOrder.length) {
				assertNull(category.getNext());
			}
			else {
				assertNotNull(category.getNext());
			}
		}
		return sortedCategories;
	}

	private void checkCreationDate(Date before, Date after, Category category) {
		assertNotNull(category.getCreationDate());
		assertFalse(before.after(category.getCreationDate()));
		assertFalse(after.before(category.getCreationDate()));
	}

	private void moveCategories(User user, List<Category> cats) throws CategoriesOrderException {
		service.updatePosition(user, cats.get(1).getId(), cats.get(0).getId(), true);
		checkIfCategoriesAreCorrectlyAddedAndSorted(user, new String[] {"2", "1", "3", "4", "5"});
		service.updatePosition(user, cats.get(1).getId(), cats.get(4).getId(), false);
		checkIfCategoriesAreCorrectlyAddedAndSorted(user, new String[] {"1", "3", "4", "5", "2"});
		service.updatePosition(user, cats.get(4).getId(), cats.get(2).getId(), true);
		checkIfCategoriesAreCorrectlyAddedAndSorted(user, new String[] {"1", "5", "3", "4", "2"});
		service.updatePosition(user, cats.get(4).getId(), cats.get(2).getId(), false);
		checkIfCategoriesAreCorrectlyAddedAndSorted(user, new String[] {"1", "3", "5", "4", "2"});
		service.updatePosition(user, cats.get(3).getId(), cats.get(4).getId(), true);
		checkIfCategoriesAreCorrectlyAddedAndSorted(user, new String[] {"1", "3", "4", "5", "2"});
		service.updatePosition(user, cats.get(1).getId(), cats.get(2).getId(), true);
		checkIfCategoriesAreCorrectlyAddedAndSorted(user, new String[] {"1", "2", "3", "4", "5"});
	}

	private void removeCategories(User user, List<Category> cats) {
		service.removeCategory(user, cats.get(3).getId());
		checkIfCategoriesAreCorrectlyAddedAndSorted(user, new String[] {"1", "2", "3", "5"});
		service.removeCategory(user, cats.get(1).getId());
		checkIfCategoriesAreCorrectlyAddedAndSorted(user, new String[] {"1", "3", "5"});
		service.removeCategory(user, cats.get(4).getId());
		checkIfCategoriesAreCorrectlyAddedAndSorted(user, new String[] {"1", "3"});
		service.removeCategory(user, cats.get(0).getId());
		checkIfCategoriesAreCorrectlyAddedAndSorted(user, new String[] {"3"});
		service.removeCategory(user, cats.get(2).getId());
		checkIfCategoriesAreCorrectlyAddedAndSorted(user, new String[] {});
	}

//	private void testShowHideCategories(User user, List<Category> cats) {
//		Category curCat = cats.get(0);
//		assertTrue(curCat.isDisplayed());
//		service.showHideCategory(user, curCat.getId(), false);
//		curCat = service.getSortedCategories(user).get(0);
//		assertFalse(curCat.isDisplayed());
//		service.showHideCategory(user, curCat.getId(), true);
//		curCat = service.getSortedCategories(user).get(0);
//		assertTrue(curCat.isDisplayed());
//	}
}
