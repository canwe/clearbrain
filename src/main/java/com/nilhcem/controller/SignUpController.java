package com.nilhcem.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import com.nilhcem.business.UserBo;
import com.nilhcem.form.SignUpForm;
import com.nilhcem.validator.SignUpValidator;

/**
 * Spring MVC Controller class for registration.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Controller
public final class SignUpController extends AbstractController {
	@Autowired
	private SignUpValidator signUpValidator;
	@Autowired
	private UserBo userBo;

	/**
	 * Define JS i18n keys.
	 */
	public SignUpController() {
		super();
		final String[] i18nJs = {"signup.err.pwd", "signup.err.pwdConf", "signup.err.mailRegist", "signup.err.mail", 
			"signup.ok.mail", "signup.ok.pwd", "signup.ok.pwdConf"};
		super.setI18nJsValues(i18nJs, "^signup\\.");
	}

	/**
	 * Initialize sign up form, giving it the SignUp model.
	 *
	 * @param model Model map.
	 * @return the SignUp view.
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String initSignUpPage(ModelMap model) {
		SignUpForm signUpForm = new SignUpForm();
		model.addAttribute("signupform", signUpForm);
		return "front/signup";
	}

	/**
	 * Return a confirmation page after signing up (Post/Redirect/Get pattern).
	 *
	 * @return the front/signupCompleted view.
	 */
	@RequestMapping(value = "/signup-completed", method = RequestMethod.GET)
	public String getSignUpCompletedPage() {
		return "front/signupCompleted";
	}

	/**
	 * Register a user who has just signed up.
	 *
	 * @param signUpForm The signup form.
	 * @param result Binding result.
	 * @param status Session status.
	 * @param request HTTP request.
	 * @return A new view (front/signupCompleted).
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView submitSignUpPage(@ModelAttribute("signupform") SignUpForm signUpForm, BindingResult result,
		SessionStatus status, HttpServletRequest request) {
		signUpValidator.validate(signUpForm, result);
		if (result.hasErrors()) {
			return new ModelAndView("front/signup");
		}

		userBo.signUpUser(signUpForm.getUser(), RequestContextUtils.getLocale(request));
		status.setComplete();
		userBo.autoLoginAfterSignup(signUpForm.getUser().getEmail(), signUpForm.getPasswordConfirmation(), request);
		return new ModelAndView("redirectWithoutModel:signup-completed");
	}

	/**
	 * Check if email is available.
	 *
	 * @param email The email we need to check.
	 * @return true if available, false is not available.
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.POST, params = { "emailToCheck" })
	public @ResponseBody boolean checkEmailAvailability(@RequestParam(value = "emailToCheck", required = true) String email) {
		return (userBo.findByEmail(email) == null);
	}
}
