/* Multi */
jQuery.fn.exists = function() {
	return jQuery(this).length > 0;
}

/* Sign up */
function displayEmailMsg(msg, css) {
	$("#email-availability").addClass(css);
	$("#email-availability").css("height", "2em");
	$("#email-availability").html(msg);
}
function checkEmail() {
	if ($("#emailError").exists())
		$("#emailError").remove();
	$("#email-availability").removeClass();
	$("#email-availability").show();

	//Check if email is valid (see RFC822)
	//There is no 'advanced' email regex check there because it's most of the time not a good idea to use a regular expression to validate email.
	//Instead we should better proceed using an 'email confirmation' check
	var filter=/\S+@\S+/
	if (!filter.test($("#email").val())) {
		displayEmailMsg("This email doesn't seem to be valid!", "error");
		return ;
	}

	//Check if email is available
	$("#email-availability").html("<img alt=\"loading...\" src=\"images/loading-circle.gif\" />");

	$.post("signup", {
		emailToCheck : $("#email").val()
	}, function(data) {
		if (data == false) {
			displayEmailMsg("This email user has already registered!", "error");
		} else {
			displayEmailMsg("Email available", "success");
		}
	});
}
