/*** Header ***/
//Display/Hide login modal pop up
function toggleLogin() {
	var loginContainer = $('#login-container');

	loginContainer.toggle();
	if (loginContainer.is(':visible')) // focus on email when clicking.
		$('#j_username_t').focus();
}

// Hides login modal pop up if visible and if a user clicks outside this pop up.
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

	// When clicking on the "Login" link in the top right area
	$('#login-bt').click(function() {
		toggleLogin();
		return false;
	});

	// Image slider
	$('#slider').nivoSlider({
		effect: 'fade',
		pauseTime: 8000
	});
});


/*** Signup ***/
// Checks if email is valid.
function checkEmail() {
	var emailCheck = $('#email-check');

	reinitErrors('#email-error', emailCheck);
	if (!emailIsValid($('#email').val())) {
		displayRightMsg(emailCheck, i18n['err.mail'], false);
		return ;
	}

	// Uses Ajax to check if email is available.
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

// Checks if password is not empty. If rules change for password: modify also logged/settings.js.
function checkPassword() {
	var passwordCheck = $('#password-check')
	reinitErrors('#password-error', passwordCheck);

	if ($('#password').isBlank())
		displayRightMsg(passwordCheck, i18n['err.pwd'], false);
	else
		displayRightMsg(passwordCheck, i18n['ok.pwd'], true);
	if ($('#password-confirm-check').is(':visible'))
		checkPasswordConfirmation();
}

// Checks password confirmation.
function checkPasswordConfirmation() {
	var passwordConfirmCheck = $('#password-confirm-check');
	reinitErrors('#password-confirm-error', passwordConfirmCheck);

	if ($('#passwordConfirmation').isBlank() || $('#password').val() != $('#passwordConfirmation').val())
		displayRightMsg(passwordConfirmCheck, i18n['err.pwdConf'], false);
	else
		displayRightMsg(passwordConfirmCheck, i18n['ok.pwdConf'], true);
}
