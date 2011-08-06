package com.nilhcem.controller.dashboard;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nilhcem.business.CategoryBo;
import com.nilhcem.business.NoteBo;
import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.core.test.abstr.AbstractControllerTest;
import com.nilhcem.enums.DashboardDateEnum;
import com.nilhcem.model.Note;
import com.nilhcem.model.User;

public class GlobalControllerTest extends AbstractControllerTest {
	@Autowired
	private GlobalController controller;
	@Autowired
	private NoteBo noteBo;
	@Autowired
	private CategoryBo categoryBo;

	@Test
	public void testPopulateCategoryList() {
		User user = testUtils.createTestUserNewPropagation("GlobalControllerTest@testPopulateCategoryList");
		testUtils.authenticate(user);
		categoryBo.addCategory(user, "Cat A");
		categoryBo.addCategory(user, "Cat B");
		assertEquals(2, controller.populateCategoriesList().size());
	}

	@Test
	@TransactionalReadWrite
	public void testPopulateUndoneNotesList() {
		User user = testUtils.createAuthenticatedTestUser("GlobalControllerTest@testPopulateUndoneNotesList");
		noteBo.addNote(user, "Note 1", 0L);
		noteBo.addNote(user, "Note 2", 0L);
		assertEquals(2, controller.populateUndoneNotesList().size());
	}

	@Test
	@TransactionalReadWrite
	public void testPopulateDoneNotesList() {
		User user = testUtils.createAuthenticatedTestUser("GlobalControllerTest@testPopulateUndoneNotesList");
		Note note = noteBo.addNote(user, "Note 1", 0L);
		noteBo.addNote(user, "Note 2", 0L);
		noteBo.checkUncheckNote(user, note.getId(), true);
		assertEquals(1, controller.populateDoneNotesList().size());
	}

	@Test
	@TransactionalReadWrite
	public void testPopulateNotesCatList() {
		User user = testUtils.createAuthenticatedTestUser("GlobalControllerTest@testPopulateNotesCatList");
		Note note = noteBo.addNote(user, "Name", 0L);
		Map<Long, Long> map = controller.populateNotesCatList();
		assertEquals(1, map.size());
		assertTrue(map.get(note.getId()).equals(0L));
	}

	@Test
	public void testPopulateDashboardType() {
		assertEquals(DashboardDateEnum.GLOBAL, controller.populateDashboardType());
	}
}
