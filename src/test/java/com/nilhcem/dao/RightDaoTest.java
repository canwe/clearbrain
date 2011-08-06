package com.nilhcem.dao;

import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nilhcem.core.hibernate.TransactionalReadOnly;
import com.nilhcem.core.test.abstr.AbstractDbTest;
import com.nilhcem.model.Right;

@TransactionalReadOnly
public class RightDaoTest extends AbstractDbTest {
	@Autowired
	private RightDao dao;

	@Test
	public void testGetUserRight() {
		Right userRight = dao.findByName(RightDao.RIGHT_USER);
		assertNotNull(userRight);
		assertEquals(RightDao.RIGHT_USER, userRight.getName());
	}

	@Test
	public void testGetAdminRight() {
		Right adminRight = dao.findByName(RightDao.RIGHT_ADMIN);
		assertNotNull(adminRight);
		assertEquals(RightDao.RIGHT_ADMIN, adminRight.getName());
	}

	@Test
	public void testMakeSureThatAdminAndUserAreNotEqual() {
		Right user = dao.findByName(RightDao.RIGHT_USER);
		Right admin = dao.findByName(RightDao.RIGHT_ADMIN);
		assertFalse(admin.getId().equals(user.getId()));
	}
}
