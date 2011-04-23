/* Multi */
jQuery.fn.exists = function() {
	return jQuery(this).length > 0;
}
jQuery.fn.isBlank = function() {
	return (!jQuery(this) || jQuery(this).val().trim() === "");
}

/* Sign up */
//Display error or success message in the right of the screen
function displayRightMsg(id, msg, success) {
	var img;
	if (success) {
		$(id).addClass("success");
		$(id).addClass("successBlock");
	} else {
		$(id).addClass("error");
		$(id).addClass("errorBlock");
	}
	$(id).html(msg);
}
//Clear page removing eventual previous errors
function reinitErrors(springSpan, error) {
	if ($(springSpan).exists())
		$(springSpan).remove();
	$(error).removeClass();
	$(error).show();
}
//Check if email is valid
function checkEmail() {
	reinitErrors("#emailError", "#email-check");

	//Check if email is valid (see RFC822)
	//There is no 'advanced' email regex check there because it's most of the time not a good idea to use a regular expression to validate email.
	//Instead we should better proceed using an 'email confirmation' check
	var filter=/\S+@\S+/
	if (!filter.test($("#email").val())) {
		displayRightMsg("#email-check", i18n["err.mail"], false);
		return ;
	}

	//Use Ajax to check if email is available
	$("#email-check").html("<img alt=\"loading...\" src=\"images/front/loading-circle.gif\" />");
	$.post("signup", {
		emailToCheck : $("#email").val()
	}, function(data) {
		if (data == false) {
			displayRightMsg("#email-check", i18n["err.mailRegist"], false);
		} else {
			displayRightMsg("#email-check", i18n["ok.mail"], true);
		}
	});
}
//Check if password is not empty
function checkPassword() {
	reinitErrors("#passwordError", "#password-check");

	if ($("#password").isBlank())
		displayRightMsg("#password-check", i18n["err.pwd"], false);
	else
		displayRightMsg("#password-check", i18n["ok.pwd"], true);
	if ($("#password-confirm-check").is(":visible"))
		checkPasswordConfirmation();
}
//Check password confirmation
function checkPasswordConfirmation() {
	reinitErrors("#passwordConfirmError", "#password-confirm-check");

	if ($("#passwordConfirmation").isBlank() || $("#password").val() != $("#passwordConfirmation").val())
		displayRightMsg("#password-confirm-check", i18n["err.pwdConf"], false);
	else
		displayRightMsg("#password-confirm-check", i18n["ok.pwdConf"], true);
}
