/*** Extends Jquery's features ***/
jQuery.fn.exists = function() {
	return jQuery(this).length > 0;
};
jQuery.fn.isBlank = function() {
	return (!jQuery(this) || jQuery(this).val().trim() === '');
};


/*** Form validation ***/
// Checks if a email is valid (see RFC822).
// There is no 'advanced' email regex check there because it's most of the time not a good idea to use a regular expression to validate email.
// Instead we should better proceed using an 'email confirmation' check.
function emailIsValid(email) {
	var filter=/\S+@\S+/;
	return filter.test(email);
}

// Displays error or success message in the right of the screen.
function displayRightMsg(elem, msg, success) {
	if (success) {
		elem.addClass('success');
		elem.addClass('success-block');
	} else {
		elem.addClass('error');
		elem.addClass('error-block');
	}
	elem.html(msg);
}

// Clears page removing eventual previous errors.
function reinitErrors(springSpan, errorElem) {
	if ($(springSpan).exists())
		$(springSpan).remove();
	errorElem.removeClass();
	errorElem.html('');
	errorElem.show();
}
