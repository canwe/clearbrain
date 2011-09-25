package com.nilhcem.clearbrain.controller;

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
import com.nilhcem.clearbrain.business.UserBo;
import com.nilhcem.clearbrain.form.SignUpForm;
import com.nilhcem.clearbrain.validator.SignUpValidator;

/**
 * Displays and handles registration form.
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
	 * Defines i18n keys which will be sent to JavaScript.
	 */
	public SignUpController() {
		super();
		final String[] i18nJs = {"signup.err.pwd", "signup.err.pwdConf", "signup.err.mailRegist", "signup.err.mail",
			"signup.ok.mail", "signup.ok.pwd", "signup.ok.pwdConf"};
		super.setI18nJsValues(i18nJs, "^signup\\.");
	}

	/**
	 * Initializes the sign up form, and returns the "front/signup" view.
	 *
	 * @param model the model which will contain the search form.
	 * @return the "logged/signup" view.
	 * @see SignUpForm
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String initSignUpPage(ModelMap model) {
		SignUpForm signUpForm = new SignUpForm();
		model.addAttribute("signupform", signUpForm);
		return "front/signup";
	}

	/**
	 * Returns a confirmation page after signing up (Post/Redirect/Get pattern).
	 *
	 * @return the "front/signup-completed" view.
	 */
	@RequestMapping(value = "/signup-completed", method = RequestMethod.GET)
	public String getSignUpCompletedPage() {
		return "front/signup-completed";
	}

	/**
	 * Registers a user who has just signed up.
	 *
	 * @param signUpForm a signup form containing data of users who sign up.
	 * @param result a binding result to contain some validation errors, if any.
	 * @param status the session status (should be set as "complete").
	 * @param request the user's HTTP request.
	 * @return a redirection to the "signup-completed" view.
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
	 * Checks if the email sent in parameter is available, the email check is <b>case insensitive</b>.
	 *
	 * @param email the email we need to check.
	 * @return {@code true} if the email is available, or if the email entered by the user is the same as his current email. {@code false} if the email is not available.
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.POST, params = { "emailToCheck" })
	@ResponseBody
	public boolean checkEmailAvailability(@RequestParam(value = "emailToCheck", required = true) String email) {
		return (userBo.findByEmail(email) == null);
	}
}
