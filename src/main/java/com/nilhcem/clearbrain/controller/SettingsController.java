package com.nilhcem.clearbrain.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.web.util.WebUtils;

import com.nilhcem.clearbrain.business.UserBo;
import com.nilhcem.clearbrain.form.SettingsForm;
import com.nilhcem.clearbrain.model.User;
import com.nilhcem.clearbrain.validator.SettingsValidator;

/**
 * Provides methods for accessing and modifying user's settings.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Controller
@PreAuthorize("hasRole('RIGHT_USER')")
public final class SettingsController extends AbstractController {
	@Autowired
	private UserBo userBo;
	@Autowired
	private SettingsValidator settingsValidator;

	/**
	 * Defines i18n keys which will be sent to JavaScript.
	 */
	public SettingsController() {
		super();
		final String[] i18nJs = {"settings.err.pwd", "settings.err.pwdConf", "settings.err.mailRegist", "settings.err.mail",
			"settings.ok.mail", "settings.ok.pwd", "settings.ok.pwdConf", "settings.cancel.confirm"};
		super.setI18nJsValues(i18nJs, "^settings\\.");
	}

	/**
	 * Initializes the settings form, and returns the "logged/settings" view.
	 *
	 * @param model the model which will contain the settings form.
	 * @return the "logged/settings" view.
	 * @see SettingsForm
	 */
	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public String initSettingsPage(ModelMap model) {
		User user = getCurrentUser();
		SettingsForm settingsForm = new SettingsForm();
		settingsForm.setEmail(user.getEmail());
		settingsForm.setCurrentEmail(user.getEmail());
		settingsForm.setEditPassword("no");
		settingsForm.setLang(user.getLanguage().getCode());
		model.addAttribute("settingsform", settingsForm);
		return "logged/settings";
	}

	/**
	 * Updates users profile information, or deletes a user. Happens when the user submits the form.
	 * <p>
	 * If the request contains the "{@code _action_delete}" parameter, then the user should be deleted.<br />
	 * Otherwise the profile information will be updated.
	 * </p>
	 *
	 * @param settingsForm a settings form containing updated settings data.
	 * @param result a binding result to contain some validation errors, if any.
	 * @param status the session status (should be set as "complete").
	 * @param request the user's HTTP request.
	 * @return <ul>
	 * <li>the "account-deleted" view if the user decided to delete his account.</li>
	 * <li>otherwise the "logged/settings" view</li>
	 * </ul>
	 */
	@RequestMapping(value = "/settings", method = RequestMethod.POST)
	public ModelAndView submitSettingsPage(@ModelAttribute("settingsform") SettingsForm settingsForm, BindingResult result,
		SessionStatus status, HttpServletRequest request) {
		ModelAndView modelAndView;
		HttpSession session = request.getSession();

		// Delete
		if (WebUtils.hasSubmitParameter(request, "_action_delete")) {
			// TODO: Prevent CSRF
			// Mark the user as deletable
			userBo.markAsDeletable(getCurrentUser());

			// Proceed logout
			SecurityContextHolder.clearContext();
			request.getSession().invalidate();

			// Drop cookies
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				cookie.setMaxAge(0);
			}

			// Redirect to confirmation page
			modelAndView = new ModelAndView("redirectWithoutModel:account-deleted");
		} else { // Update settings
			settingsValidator.validate(settingsForm, result);
			if (result.hasErrors()) {
				session.setAttribute("settings_ko", ""); // To display error message on client side
				modelAndView = new ModelAndView("logged/settings");
			} else {
				userBo.updateSettings(getCurrentUser(), settingsForm);
				status.setComplete();
				session.setAttribute("settings_ok", ""); // To display confirmation message on client side
				modelAndView = new ModelAndView("redirectWithoutModel:settings");
			}
		}
		return modelAndView;
	}

	/**
	 * Checks if the email sent in parameter is available, the email check is <b>case insensitive</b>.
	 *
	 * @param email the email we need to check.
	 * @return {@code true} if the email is available, or if the email entered by the user is the same as his current email. {@code false} if the email is not available.
	 */
	@RequestMapping(value = "/settings_check_email", method = RequestMethod.POST, params = { "emailToCheck" })
	@ResponseBody
	public boolean checkEmailAvailability(@RequestParam(value = "emailToCheck", required = true) String email) {
		User user = userBo.findByEmail(email);
		return ((user == null) || (user.getEmail().equalsIgnoreCase(getCurrentUser().getEmail())));
	}
}
