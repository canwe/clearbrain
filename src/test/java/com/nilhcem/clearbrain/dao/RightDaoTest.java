package com.nilhcem.clearbrain.dao;

import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nilhcem.clearbrain.core.hibernate.TransactionalReadOnly;
import com.nilhcem.clearbrain.core.test.abstr.AbstractDbTest;
import com.nilhcem.clearbrain.model.Right;

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
