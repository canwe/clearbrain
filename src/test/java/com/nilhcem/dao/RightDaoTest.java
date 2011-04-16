package com.nilhcem.dao;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.nilhcem.model.Right;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml"})
public class RightDaoTest {
	@Autowired
	private RightDao dao;

	@Test
	public void shouldGetUserRight() {
		Right userRight = dao.findByName(RightDao.RIGHT_USER);
		assertNotNull(userRight);
		assertEquals(userRight.getName(), RightDao.RIGHT_USER);
	}

	@Test
	public void shouldGetAdminRight() {
		Right adminRight = dao.findByName(RightDao.RIGHT_ADMIN);
		assertNotNull(adminRight);
		assertEquals(adminRight.getName(), RightDao.RIGHT_ADMIN);
	}

	@Test
	public void makeSureThatAdminAndUserAreNotEqual() {
		Right user = dao.findByName(RightDao.RIGHT_USER);
		Right admin = dao.findByName(RightDao.RIGHT_ADMIN);
		assertNotSame(user.getId(), admin.getId());
	}
}
