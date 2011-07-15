//Once document is ready
jQuery(function($) {
	//Show/hide password container depending on radio button
	showHideEditPasswordForm();
});

//Show/Hide edit password form
$('#edit-password-radios').find('input[type=radio]').live('click', function() {
	showHideEditPasswordForm();
});

//Show/Hide edit password form
function showHideEditPasswordForm() {
	if ($('#edit-password-radios').find('input[name=editPassword]:checked').val() == 'yes')
		$('#password-container').show();
	else
		$('#password-container').hide();
}

//Check if email is valid
$('#email').live('change', function() {
	var emailCheck = $('#email-check'),
		emailVal = $('#email').val();

	reinitErrors('#email-error', emailCheck);
	if (!emailIsValid(emailVal)) {
		displayRightMsg(emailCheck, i18n['err.mail'], false);
		return ;
	}

	//Check if the email is available
	emailCheck.html('<img src="images/front/loading-circle.gif" />');
	$.post('settings_check_email', {
		emailToCheck : emailVal
	}, function(data) {
		if (data) {
			if (emailVal.toLowerCase() == $('#current-email').val().toLowerCase())
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
$('#current-password').live('change', function() {
	checkPassword('#current-password', '#curpwd-check', '#current-password-error');
});

//Check if new password is not empty.
$('#new-password').live('change', function() {
	checkPassword('#new-password', '#newpwd-check', '#new-password-error');
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
$('#confirm-password').live('change', function() {
	checkPasswordConfirmation();
});

//Check password confirmation
function checkPasswordConfirmation() {
	var passwordConfirmCheck = $('#confirmpwd-check');
	reinitErrors('#confirm-password-error', passwordConfirmCheck);

	if ($('#confirm-password').isBlank() || $('#new-password').val() != $('#confirm-password').val())
		displayRightMsg(passwordConfirmCheck, i18n['err.pwdConf'], false);
	else
		displayRightMsg(passwordConfirmCheck, i18n['ok.pwdConf'], true);
}

//Display confirmation message if user clicks on 'Remove account'
$('#delete-account').live('click', function() {
	return (confirm(i18n['cancel.confirm']));
});
