package com.nilhcem.clearbrain.controller;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import com.nilhcem.clearbrain.business.QuickMemoBo;
import com.nilhcem.clearbrain.form.QuickMemoForm;
import com.nilhcem.clearbrain.model.QuickMemo;

/**
 * Provides methods for dealing with quick memos.
 *
 * @author Nilhcem
 * @since 1.0
 * @see QuickMemo
 */
@Controller
@PreAuthorize("hasRole('RIGHT_USER')")
@RequestMapping("/quick_memo")
public final class QuickMemoController extends AbstractController {
	@Autowired
	private QuickMemoBo quickMemoBo;

	/**
	 * Initializes the quick memo form, and returns the "logged/quick-memo" view.
	 * <p>
	 * The method will set in the model the content of the quick memo.<br />
	 * If the user has never modified his quick memo, a localized default text will be sent to the model.
	 * </p>
	 *
	 * @param model the model which will contain the quick memo form.
	 * @param locale the user's locale to display a localized default text if the user goes to his quick memo for the first time.
	 * @return the "logged/quick-memo" view.
	 * @see QuickMemoForm
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String getQuickMemoView(ModelMap model, Locale locale) {
		QuickMemoForm memoForm = new QuickMemoForm();
		QuickMemo memo = quickMemoBo.getByUser(getCurrentUser());

		// If this is the first time a quick memo is opened, displays default text
		if (memo.getSaveDate() == null) {
			memoForm.setInput(getMessageSource().getMessage("memo.default.text", null, locale));
		} else {
			memoForm.setInput(memo.getContent());
		}
		memoForm.setSaveDate(memo.getSaveDate());
		model.addAttribute("memoform", memoForm);
		return "logged/quick-memo";
	}

	/**
	 * Saves a quick memo. Happens when the user submits the form.
	 *
	 * @param memoForm the quick memo form which will be saved.
	 * @param status the session status (should be set as "complete").
	 * @return a redirection to the "logged/quick-memo" view.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView submitQuickMemoPage(@ModelAttribute("memoform") QuickMemoForm memoForm, SessionStatus status) {
		status.setComplete();
		quickMemoBo.updateMemo(getCurrentUser(), memoForm.getInput());
		return new ModelAndView("redirectWithoutModel:quick_memo");
	}
}
