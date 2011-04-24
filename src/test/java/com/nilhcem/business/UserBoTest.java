package com.nilhcem.business;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.nilhcem.business.UserBo;
import com.nilhcem.core.hibernate.WithTransaction;
import com.nilhcem.core.spring.UserDetailsAdapter;
import com.nilhcem.dao.RightDao;
import com.nilhcem.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/security/applicationContext-security.xml"})
public class UserBoTest {
	private final String EMAIL = "my.great@email.com";
	private final String PASSWORD = "myPassword";

	@Autowired
	@Qualifier(value = "userBo")
	private UserBo usersHandler;

	@Autowired
	private RightDao rightDao;

	@Autowired
	private ShaPasswordEncoder passwordEncoder;

	@Autowired
	private SaltSource saltSource;

	//Sequence won't be rolled-back: 
	//As explained in postgreSQL documentation:
	//To avoid blocking of concurrent transactions that obtain numbers from the same sequence, a nextval operation is never rolled back; that is, once a value has been fetched it is considered used, even if the transaction that did the nextval later aborts. This means that aborted transactions might leave unused "holes" in the sequence of assigned values. setval operations are never rolled back, either.
	@Test
	@WithTransaction
	@Rollback(true)
	public void aUserCanSignUp() {
		User user = new User();
		user.setEmail(EMAIL);
		user.setPassword(PASSWORD);
		user.setEnabled(true);
		usersHandler.signUpUser(user);
		checkIfUserIsSavedInDB();
	}

	private void checkIfUserIsSavedInDB() {
		User userNull = usersHandler.findByEmail("");
		assertNull(userNull);
		User user = usersHandler.findByEmail(EMAIL);
		assertNotNull(user);
		assertEquals(user.getEmail(), EMAIL);

		//Check password
		assertEquals(user.getPassword(), passwordEncoder.encodePassword(PASSWORD, saltSource.getSalt(new UserDetailsAdapter(user))));
		aUserWhoSignsUpShouldHaveUserRight(user);
	}

	private void aUserWhoSignsUpShouldHaveUserRight(User user) {
		assertNotNull(user.getRights());
		assertEquals(user.getRights().size(), 1);
		assertEquals(user.getRights().get(0), rightDao.findByName(RightDao.RIGHT_USER));
	}
}
