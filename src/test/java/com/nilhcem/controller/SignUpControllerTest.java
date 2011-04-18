package com.nilhcem.controller;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.SimpleSessionStatus;
import org.springframework.web.servlet.ModelAndView;
import com.nilhcem.form.SignUpForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/mvc/mvc-dispatcher-servlet.xml"})
public class SignUpControllerTest {
	@Autowired
	private SignUpController controller;

	@Test
	public void signUpPageShouldBeCorrectlyInitialized() {
		ModelMap model = new ModelMap();
		assertEquals(controller.initSignUpPage(model), "front/signup");
		assertTrue(model.containsAttribute("signupform"));
	}

	private ModelAndView getModelAndViewSignUpPage(String email, String password, String passwordConfirmation) {
        MockHttpServletRequest request = new MockHttpServletRequest("post", "/signup");
        request.setParameter("user.email", email);
        request.setParameter("user.password", password);
        request.setParameter("passwordConfirmation", passwordConfirmation);

        SignUpForm signUpForm = new SignUpForm();
        WebDataBinder binder = new WebDataBinder(signUpForm, "signupform");
        binder.bind(new MutablePropertyValues(request.getParameterMap()));
        SessionStatus status = new SimpleSessionStatus();

        return controller.submitSignUpPage(signUpForm, binder.getBindingResult(), status);
	}

	@Test
	public void submitSignUpPageWithBadEmailShouldRedirectToInitialForm() {
		ModelAndView modelAndView = getModelAndViewSignUpPage("", "myP#ssW0Rd", "myP#ssW0Rd");
		assertEquals(modelAndView.getViewName(), "front/signup");
	    assertNotNull(modelAndView.getModel());
	}

	@Test
	public void submitSignUpPageWithEmptyPwdShouldRedirectToInitialForm() {
		ModelAndView modelAndView = getModelAndViewSignUpPage("my.email@company.com", "", "");
		assertEquals(modelAndView.getViewName(), "front/signup");
	    assertNotNull(modelAndView.getModel());
	}

	@Test
	public void submitSignUpPageWithBadPwdConfirmationShouldRedirectToInitialForm() {
		ModelAndView modelAndView = getModelAndViewSignUpPage("my.email@company.com", "myP#ssW0Rd", "notTheSame");
		assertEquals(modelAndView.getViewName(), "front/signup");
	    assertNotNull(modelAndView.getModel());
	}

	@Test
	@Transactional
	@Rollback(true)
	public void submitSignUpPageWithValidObjectShouldRedirectToCompletedView() {
		String email = "my.email@company.com";
		ModelAndView modelAndView = getModelAndViewSignUpPage(email, "myP#ssW0Rd", "myP#ssW0Rd");
		assertEquals(modelAndView.getViewName(), "redirectWithoutModel:signup-completed");
	    assertNotNull(modelAndView.getModel());
	    checkEmailAvailabilityShouldReturnFalse(email);
	}

	@Test
	public void shouldGetCompletedSignUpPage() {
		assertEquals(controller.getSignUpCompletedPage(), "front/signupCompleted");
	}

	@Test
	public void checkEmailAvailabilityShouldReturnTrue() {
		assertTrue(controller.checkEmailAvailability("###@###.###")); //I'm sure nobody has taken this email (anyway, it's not possible...)
	}

	public void checkEmailAvailabilityShouldReturnFalse(String email) {
		assertFalse(controller.checkEmailAvailability(email));
	}
}
