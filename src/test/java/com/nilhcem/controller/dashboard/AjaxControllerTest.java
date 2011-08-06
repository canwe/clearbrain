package com.nilhcem.controller.dashboard;

import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;

import com.nilhcem.business.CategoryBo;
import com.nilhcem.business.NoteBo;
import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.core.test.abstr.AbstractControllerTest;
import com.nilhcem.dao.CategoryDao;
import com.nilhcem.model.Category;
import com.nilhcem.model.Note;
import com.nilhcem.model.User;

public class AjaxControllerTest extends AbstractControllerTest {
	@Autowired
	AjaxController controller;
	@Autowired
	NoteBo noteBo;
	@Autowired
	CategoryBo categoryBo;
	@Autowired
	CategoryDao categoryDao;

	@Test
	@TransactionalReadWrite
	public void testAddCategory() {
		final String categoryName = "New ajax cat name";

		User user = testUtils.createAuthenticatedTestUser("AjaxControllerTest@testAddCategory");
		Category category = controller.addCategory(categoryName);
		assertEquals(categoryName, category.getName());
		assertEquals(1, categoryBo.getSortedCategories(user).size());
	}

	@Test
	@TransactionalReadWrite
	public void testRemoveCategory() {
		User user = testUtils.createAuthenticatedTestUser("AjaxControllerTest@AjaxControllerTest");
		Category category = categoryBo.addCategory(user, "Category");
		assertTrue(controller.removeCategory(category.getId()));
		assertTrue(categoryBo.getSortedCategories(user).isEmpty());
	}

	@Test
	@TransactionalReadWrite
	public void testRenameCategory() {
		final String newName = "New name";
		User user = testUtils.createAuthenticatedTestUser("AjaxControllerTest@testRenameCategory");
		Category category = categoryBo.addCategory(user, "category name");
		assertTrue(controller.renameCategory(category.getId(), newName));
		assertEquals(newName, categoryBo.getSortedCategories(user).get(0).getName());
	}

	@Test
	@TransactionalReadWrite
	public void testUpdatePositionFail() {
		// Create a user and set 2 categories with the same "next" value.
		User user = testUtils.createAuthenticatedTestUser("AjaxControllerTest@testUpdatePositionFail");
		Category catA = categoryBo.addCategory(user, "catA");
		Category catB = categoryBo.addCategory(user, "catB");
		Category catC = categoryBo.addCategory(user, "catC");
		catA.setNext(catC);
		catB.setNext(catC);
		catC.setNext(null);
		categoryDao.update(catA);
		categoryDao.update(catB);
		categoryDao.update(catC);
		assertFalse(controller.updatePosition(catC.getId(), catB.getId(), false));
	}

	@Test
	@TransactionalReadWrite
	public void testUpdatePosition() {
		User user = testUtils.createAuthenticatedTestUser("AjaxControllerTest@testUpdatePosition");
		Category catA = categoryBo.addCategory(user, "Cat A");
		Category catB = categoryBo.addCategory(user, "Cat B");
		assertTrue(controller.updatePosition(catA.getId(), catB.getId(), false));
	}

	@Test
	@TransactionalReadWrite
	public void testAddQuickNote() {
		final String noteName = "Quick note";

		User user = testUtils.createAuthenticatedTestUser("AjaxControllerTest@testAddQuickNote");
		Note note = controller.addNote(noteName, 0L);
		assertEquals(noteName, noteBo.getNoteById(user, note.getId()).getName());
	}

	@Test
	@TransactionalReadWrite
	public void testCheckNote() {
		User user = testUtils.createAuthenticatedTestUser("AjaxControllerTest@testCheckNote");
		Note note = noteBo.addNote(user, "Name", 0L);
		Long[] todoHeaders = controller.checkNote(note.getId(), true, new MockHttpSession());
		assertEquals(3, todoHeaders.length);
		assertTrue(todoHeaders[0].equals(0L));
		assertTrue(todoHeaders[1].equals(0L));
		assertTrue(todoHeaders[2].equals(0L));
	}
}
