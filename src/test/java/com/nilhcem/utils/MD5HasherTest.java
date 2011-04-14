package com.nilhcem.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml"})
public class MD5HasherTest {
	@Autowired
	@Qualifier(value = "Md5Hasher")
	private MD5Hasher md5Hasher;

	@Test
	public void shouldHashStringToMd5() throws Exception {
		String from = "ThisShouldBeHashedToMd5";
		String expectedResult = "84284076f9854a3dbf25c28e6714d183";
		assertEquals(expectedResult, md5Hasher.toMd5(from));

		from = "Yes, I think it's working great!";
		expectedResult = "ab2b094ac32d87e7d344f972161cff62";
		assertEquals(expectedResult, md5Hasher.toMd5(from));
	}
}
