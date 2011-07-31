package com.nilhcem.business;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.core.test.AbstractDbTest;
import com.nilhcem.dao.UserDao;
import com.nilhcem.model.QuickMemo;
import com.nilhcem.model.User;

public class QuickMemoBoTest extends AbstractDbTest {
	private static final String DEFAULT_CONTENT = "";
	private static final String NEW_CONTENT = "Hey, this is my <br /> new content";
	private static final Logger logger = LoggerFactory.getLogger(QuickMemoBoTest.class);

	@Autowired
	private QuickMemoBo memoBo;
	@Autowired
	private LanguageBo langBo;
	@Autowired
	private UserDao userDao;

	@Test
	@TransactionalReadWrite
	public void testGetByUser() {
		//A quick memo is created when a user is created
		User user = testUtils.createTestUser("QuickMemoBoTest@testGetByUser");
		logger.debug("Get by user");
		QuickMemo memo = memoBo.getByUser(user);
		assertNotNull(memo);
	}

	@Test
	@TransactionalReadWrite
	public void testCreateQuickMemo() {
		logger.debug("Create quick memo: Create user manually (without memo)");
		User user = new User();
		user.setEmail("QuickMemoBoTest@UntakenEmail");
		user.setPassword("");
		user.setLanguage(langBo.findByLocale(new Locale("en", "US")));
		user.setRegistrationDate(Calendar.getInstance().getTime());
		userDao.save(user);

		logger.debug("Create quick memo: Create memo");
		memoBo.createQuickMemo(user);
		QuickMemo memo = memoBo.getByUser(user);
		assertEquals(QuickMemoBoTest.DEFAULT_CONTENT, memo.getContent());
		assertNull(memo.getSaveDate()); //save date should be null when a memo is created
		assertEquals(user.getId(), memo.getUser().getId());
	}

	@Test
	@TransactionalReadWrite
	public void testUpdateMemo() {
		User user = testUtils.createTestUser("QuickMemoBoTest@testUpdateMemo");
		logger.debug("Update memo: Get memo");
		QuickMemo memo = memoBo.getByUser(user);
		assertFalse(QuickMemoBoTest.NEW_CONTENT.equals(memo.getContent()));

		logger.debug("Update memo: Update memo");
		Date before = testUtils.getDateBeforeTest();
		memoBo.updateMemo(user,  QuickMemoBoTest.NEW_CONTENT);
		Date after = testUtils.getDateAfterTest();

		logger.debug("Update memo: Test updated values");
		QuickMemo updatedMemo = memoBo.getByUser(user);
		assertEquals(NEW_CONTENT, updatedMemo.getContent());
		assertEquals(memo.getId(), updatedMemo.getId());
		testUtils.checkDateBetween(updatedMemo.getSaveDate(), before, after);
	}
}
