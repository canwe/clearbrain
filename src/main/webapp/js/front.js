/* Multi */
jQuery.fn.exists = function(){return jQuery(this).length>0;}


/* Sign up */
//Check if email is available
function checkEmail() {
	if ($("#emailError").exists())
		$("#emailError").remove();

	$("#email-availability").removeClass();
	$("#email-availability").html("<img alt=\"loading...\" src=\"images/loading-circle.gif\" />");
	$("#email-availability").show();

	$.post("signup", {
		emailToCheck : $("#email").val()
	}, function(data) {
		if (data == false) {
			$("#email-availability").addClass("error");
			$("#email-availability").css("height", "2em");
			$("#email-availability").html("This email user has already registered!");
		}
		else {
			$("#email-availability").addClass("success");
			$("#email-availability").css("height", "2em");
			$("#email-availability").html("Email available");
		}
	});
}
