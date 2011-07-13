/*** Settings ***/
/* Multi */
jQuery.fn.exists = function() {
	return jQuery(this).length > 0;
};
jQuery.fn.isBlank = function() {
	return (!jQuery(this) || jQuery(this).val().trim() === '');
};

//Once document is ready (dashboard)
jQuery(function($) {
	//Show/hide password container depending on radio button
	showHideEditPasswordForm();
});

//Show/Hide edit password form
$('#editPasswordRadios').find('input[type=radio]').live('click', function() {
	showHideEditPasswordForm();
});

//Show/Hide edit password form
function showHideEditPasswordForm() {
	if ($('#editPasswordRadios').find('input[name=editPassword]:checked').val() == 'yes')
		$('#password-container').show();
	else
		$('#password-container').hide();
}

//Display error or success message in the right of the screen
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

//Clear page removing eventual previous errors
function reinitErrors(springSpan, errorElem) {
	if ($(springSpan).exists())
		$(springSpan).remove();
	errorElem.removeClass();
	errorElem.show();
}

//Check if email is valid
$('#email').live('change', function() {
	var emailCheck = $('#email-check'),
		filter=/\S+@\S+/, //see signup.js:checkEmail() if this is changed
		emailVal = $('#email').val();

	reinitErrors('#emailError', emailCheck);
	//Check if email is valid, see signup.js:checkEmail() for more information
	if (!filter.test(emailVal)) {
		displayRightMsg(emailCheck, i18n['err.mail'], false);
		return ;
	}

	//Check if the email is available
	emailCheck.html('<img src="images/front/loading-circle.gif" />');
	$.post('settings_check_email', {
		emailToCheck : emailVal
	}, function(data) {
		if (data) {
			if (emailVal.toLowerCase() == $('#currentEmail').val().toLowerCase())
				emailCheck.html(''); //same email
			else
				displayRightMsg(emailCheck, i18n['ok.mail'], true);
		}
		else {
			displayRightMsg(emailCheck, i18n['err.mailRegist'], false);
		}
	});
});

//Check if current password is not empty.
$('#currentPassword').live('change', function() {
	checkPassword('#currentPassword', '#curpwd-check', '#newPasswordError');
});

//Check if new password is not empty.
$('#newPassword').live('change', function() {
	checkPassword('#newPassword', '#newpwd-check', '#currentPasswordError');
	if ($('#confirmpwd-check').is(':visible'))
		checkPasswordConfirmation();
});

//Check if password is not empty. If rules change for password: modify also front.js
function checkPassword(pwdId, checkId, errorId) {
	var passwordCheck = $(checkId)
	reinitErrors(errorId, passwordCheck);

	if ($(pwdId).isBlank())
		displayRightMsg(passwordCheck, i18n['err.pwd'], false);
	else
		displayRightMsg(passwordCheck, i18n['ok.pwd'], true);
}

//Check password confirmation
$('#confirmPassword').live('change', function() {
	checkPasswordConfirmation();
});

//Check password confirmation
function checkPasswordConfirmation() {
	var passwordConfirmCheck = $('#confirmpwd-check');
	reinitErrors('#confirmPasswordError', passwordConfirmCheck);

	if ($('#confirmPassword').isBlank() || $('#newPassword').val() != $('#confirmPassword').val())
		displayRightMsg(passwordConfirmCheck, i18n['err.pwdConf'], false);
	else
		displayRightMsg(passwordConfirmCheck, i18n['ok.pwdConf'], true);
}
