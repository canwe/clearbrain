package com.nilhcem.clearbrain.business;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.nilhcem.clearbrain.core.hibernate.TransactionalReadWrite;
import com.nilhcem.clearbrain.core.test.abstr.AbstractDbTest;
import com.nilhcem.clearbrain.dao.UserDao;
import com.nilhcem.clearbrain.model.QuickMemo;
import com.nilhcem.clearbrain.model.User;

public class QuickMemoBoTest extends AbstractDbTest {
	private static final String DEFAULT_CONTENT = "";
	private static final String NEW_CONTENT = "Hey, this is my <br /> new content";

	@Autowired
	private QuickMemoBo memoBo;
	@Autowired
	private LanguageBo langBo;
	@Autowired
	private UserDao userDao;

	@Test
	@TransactionalReadWrite
	public void testGetByUser() {
		// A quick memo is created when a user is created
		User user = testUtils.createTestUser("QuickMemoBoTest@testGetByUser");
		QuickMemo memo = memoBo.getByUser(user);
		assertNotNull(memo);
	}

	@Test
	@TransactionalReadWrite
	public void testCreateQuickMemo() {
		// Create a user manually (without memo)
		User user = new User();
		user.setEmail("QuickMemoBoTest@UntakenEmail");
		user.setPassword("");
		user.setLanguage(langBo.findByLocale(new Locale("en", "US")));
		user.setRegistrationDate(Calendar.getInstance().getTime());
		userDao.save(user);

		// Create a quick memo
		memoBo.createQuickMemo(user);
		QuickMemo memo = memoBo.getByUser(user);
		assertEquals(QuickMemoBoTest.DEFAULT_CONTENT, memo.getContent());
		assertNull(memo.getSaveDate()); // Save date should be null when a memo is created
		assertEquals(user.getId(), memo.getUser().getId());
	}

	@Test
	@TransactionalReadWrite
	public void testUpdateMemo() {
		User user = testUtils.createTestUser("QuickMemoBoTest@testUpdateMemo");
		QuickMemo memo = memoBo.getByUser(user);
		assertFalse(QuickMemoBoTest.NEW_CONTENT.equals(memo.getContent()));

		// Update memo
		Date before = testUtils.getDateBeforeTest();
		memoBo.updateMemo(user,  QuickMemoBoTest.NEW_CONTENT);
		Date after = testUtils.getDateAfterTest();

		// Test updated values
		QuickMemo updatedMemo = memoBo.getByUser(user);
		assertEquals(NEW_CONTENT, updatedMemo.getContent());
		assertEquals(memo.getId(), updatedMemo.getId());
		testUtils.checkDateBetween(updatedMemo.getSaveDate(), before, after);
	}
}
