package com.nilhcem.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.nilhcem.business.CategoryBo;
import com.nilhcem.core.exception.CategoriesOrderException;
import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.core.test.AbstractDbTest;
import com.nilhcem.model.Category;
import com.nilhcem.model.User;

public class CategoryDaoTest extends AbstractDbTest {
	@Autowired
	private CategoryDao dao;
	@Autowired
	private CategoryBo categoryBo;

	@Test(expected = CategoriesOrderException.class)
	@TransactionalReadWrite
	public void testCheckIfCategoriesAreProperlyOrderedWithErrorsInCategories() throws CategoriesOrderException {
		//Create user and set 2 categories with the same "next" value.
		User user = testUtils.createTestUser("CategoryDaoTest@testCheckIfCategoriesAreProperlyOrderedWithEmptyCategories");
		Category catA = categoryBo.addCategory(user, "catA");
		Category catB = categoryBo.addCategory(user, "catB");
		Category catC = categoryBo.addCategory(user, "catC");
		catA.setNext(catC);
		catB.setNext(catC);
		catC.setNext(null);
		dao.update(catA);
		dao.update(catB);
		dao.update(catC);
		dao.checkIfCategoriesAreProperlyOrdered(user);
	}
}
