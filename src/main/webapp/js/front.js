/* Multi */
jQuery.fn.exists = function() {
	return jQuery(this).length > 0;
};
jQuery.fn.isBlank = function() {
	return (!jQuery(this) || jQuery(this).val().trim() === '');
};

/* Header */
//Display/Hide login modal pop up
function toggleLogin() {
	$('#login-container').toggle();
}

//Hide login modal pop up if visible and if a user clicks outside this pop up
$(document).ready(function() {
	$('html').click(function() {
		var loginContainer = $('#login-container');
		if (loginContainer.is(':visible'))
			loginContainer.hide();
	});
	var stopPropagation = function(event){
	    event.stopPropagation();
	};
	$('#header').find('.login').click(stopPropagation);
	$('#login-container').click(stopPropagation);
});

/* Sign up */
//Display error or success message in the right of the screen
function displayRightMsg(elem, msg, success) {
	if (success) {
		elem.addClass('success');
		elem.addClass('successBlock');
	} else {
		elem.addClass('error');
		elem.addClass('errorBlock');
	}
	elem.html(msg);
}

//Clear page removing eventual previous errors
function reinitErrors(springSpan, errorElem) {
	if ($(springSpan).exists())
		$(springSpan).remove();
	errorElem.removeClass();
	errorElem.show();
}

//Check if email is valid
function checkEmail() {
	var emailCheck = $('#email-check'),
		filter=/\S+@\S+/;

	reinitErrors('#emailError', emailCheck);
	//Check if email is valid (see RFC822)
	//There is no 'advanced' email regex check there because it's most of the time not a good idea to use a regular expression to validate email.
	//Instead we should better proceed using an 'email confirmation' check
	if (!filter.test($('#email').val())) {
		displayRightMsg(emailCheck, i18n['err.mail'], false);
		return ;
	}

	//Use Ajax to check if email is available
	emailCheck.html('<img src="images/front/loading-circle.gif" />');
	$.post('signup', {
		emailToCheck : $('#email').val()
	}, function(data) {
		if (data == false) {
			displayRightMsg(emailCheck, i18n['err.mailRegist'], false);
		} else {
			displayRightMsg(emailCheck, i18n['ok.mail'], true);
		}
	});
}

//Check if password is not empty
function checkPassword() {
	var passwordCheck = $('#password-check')
	reinitErrors('#passwordError', passwordCheck);

	if ($('#password').isBlank())
		displayRightMsg(passwordCheck, i18n['err.pwd'], false);
	else
		displayRightMsg(passwordCheck, i18n['ok.pwd'], true);
	if ($('#password-confirm-check').is(':visible'))
		checkPasswordConfirmation();
}

//Check password confirmation
function checkPasswordConfirmation() {
	var passwordConfirmCheck = $('#password-confirm-check');
	reinitErrors('#passwordConfirmError', passwordConfirmCheck);

	if ($('#passwordConfirmation').isBlank() || $('#password').val() != $('#passwordConfirmation').val())
		displayRightMsg(passwordConfirmCheck, i18n['err.pwdConf'], false);
	else
		displayRightMsg(passwordConfirmCheck, i18n['ok.pwdConf'], true);
}
