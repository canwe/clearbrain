package com.nilhcem.clearbrain.core.test.abstr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;

public abstract class AbstractValidatorTest extends AbstractDbTest {
	public abstract void testSupports();

	// Make sure the result contains the expected errors
	protected void checkErrors(BindingResult result, String[] expected) {
		if (expected == null) {
			assertFalse(result.hasErrors());
		}
		else {
			assertTrue(result.hasErrors());
			assertEquals(expected.length, result.getErrorCount());

			List<String> expectedList = Arrays.asList(expected);
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				assertTrue(expectedList.contains(error.getCode()));
			}
		}
	}

	// Get a BindingResult object
	protected BindingResult getBindingResult(Object target, MockHttpServletRequest request) {
		WebDataBinder binder = new WebDataBinder(target, "");
		binder.bind(new MutablePropertyValues(request.getParameterMap()));
		return binder.getBindingResult();
	}
}
