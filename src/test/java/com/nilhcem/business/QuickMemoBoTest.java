package com.nilhcem.business;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.core.test.TestUtils;
import com.nilhcem.dao.QuickMemoDao;
import com.nilhcem.model.QuickMemo;
import com.nilhcem.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public class QuickMemoBoTest {
	private final static String DEFAULT_CONTENT = "";
	private final static String NEW_CONTENT = "Hey, this is my <br /> new content";

	@Autowired
	private TestUtils testUtils;
	@Autowired
	private QuickMemoBo service;
	@Autowired
	private QuickMemoDao dao;

	@Test
	public void aQuickMemoShouldBeCreatedWhenAUserIsCreated() {
		User user = testUtils.getTestUser();
		QuickMemo memo = service.getByUser(user);
		assertNotNull(memo);
		assertEquals(DEFAULT_CONTENT, memo.getContent());
		assertNull(memo.getSaveDate()); //save date should be null when a memo is created
	}

	@Test
	@TransactionalReadWrite
	public void testUpdateMemo() {
		//Get memo to check default values
		User user = testUtils.getTestUser();
		QuickMemo memo = service.getByUser(user);
		assertFalse(NEW_CONTENT.equals(memo.getContent()));

		//Update memo and check date
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, -1);
		Date before = cal.getTime();
		service.updateMemo(user, NEW_CONTENT);
		cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 1);
		Date after = cal.getTime();

		//Test updated memo values
		QuickMemo updatedMemo = service.getByUser(user);
		assertEquals(NEW_CONTENT, updatedMemo.getContent());
		assertEquals(memo.getId(), updatedMemo.getId());
		testSaveDate(before, updatedMemo.getSaveDate(), after);

		//Set back default content
		setBackDefaultValues(user);
	}

	private void testSaveDate(Date before, Date saveDate, Date after) {
		assertFalse(before.after(saveDate));
		assertFalse(after.before(saveDate));
	}

	private void setBackDefaultValues(User user) {
		QuickMemo memo = service.getByUser(user);
		memo.setSaveDate(null);
		memo.setContent(DEFAULT_CONTENT);
		dao.update(memo);
	}
}
