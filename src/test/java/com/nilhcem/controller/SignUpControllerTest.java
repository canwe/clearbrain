package com.nilhcem.controller;

import static org.junit.Assert.*;
import java.util.HashMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.servlet.ModelAndView;
import com.nilhcem.form.SignUp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/springmvc/mvc-dispatcher-servlet.xml"})
public class SignUpControllerTest {
	@Autowired
	private SignUpController controller;

	@Test
	public void submitSignUpPageWithWrongObjectShouldRedirectToInitialForm() {
		SignUp signup = new SignUp();
		signup.setEmail("");
		signup.setPassword("");

		@SuppressWarnings("rawtypes")
		BindingResult br = new MapBindingResult(new HashMap(), "objectName");

		ModelAndView modelAndView = controller.submitSignUpPage(signup, br, null);
		assertEquals(modelAndView.getViewName(), "SignUp");
	    assertNotNull(modelAndView.getModel());
	}

	@Test
	public void signUpPageShouldBeCorrectlyInitialized() {
		ModelMap model = new ModelMap();
		assertEquals(controller.initSignUpPage(model), "SignUp");
		assertTrue(model.containsAttribute("signupForm"));
	}

	@Test
	public void shouldGetCompletedSignUpPage() {
		assertEquals(controller.getSignUpCompletedPage(), "SignUpCompleted");
	}
}
