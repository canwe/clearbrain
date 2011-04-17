package com.nilhcem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import com.nilhcem.business.UserBo;
import com.nilhcem.form.SignUpForm;
import com.nilhcem.validator.SignUpValidator;

/**
 * Business class for registrations.
 * 
 * @author Nilhcem
 * @since 1.0
 */
@Controller
public class SignUpController {
	@Autowired
	private SignUpValidator signUpValidator;

	@Autowired
	private UserBo userBo;

	/**
	 * Register a user who has just signed up.
	 * @param signUpForm
	 * @param result
	 * @param status
	 * @return a new view (SignUpCompleted)
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView submitSignUpPage(@ModelAttribute("signupform") SignUpForm signUpForm, BindingResult result, SessionStatus status) {
		signUpValidator.validate(signUpForm, result);
		if (result.hasErrors())
			return new ModelAndView("front/signup");

		userBo.signUpUser(signUpForm.getUser());
		status.setComplete();
		return new ModelAndView("redirectWithoutModel:signup-completed");
	}

	/**
	 * Init sign up form, giving it the SignUp model.
	 * @param model
	 * @return the SignUp view
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String initSignUpPage(ModelMap model) {
		SignUpForm signUpForm = new SignUpForm();
		model.addAttribute("signupform", signUpForm);
		return "front/signup";
	}

	/**
	 * Return a confirmation page after signing up (Post/Redirect/Get pattern).
	 * @return the SignUpCompleted view
	 */
	@RequestMapping(value = "/signup-completed", method = RequestMethod.GET)
	public String getSignUpCompletedPage() {
		return "front/signupCompleted";
	}
}
