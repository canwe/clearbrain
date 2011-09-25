jQuery(function($) {
	$('#datepicker').datepicker({
		altField: '#alt-datepicker',
		altFormat: 'mm/dd/yy',
		changeMonth: true,
		changeYear: true,
		dateFormat: i18n['caldate.format'],
		minDate: 0,
		onSelect: function(dateText, inst) {
			reinitErrors('#duedate-error', $('#duedate-check'));
		}
	});
});

// Checks if note's name is not empty.
$('#name').live('change', function() {
	var nameCheck = $('#name-check');
	reinitErrors('#name-error', nameCheck);

	if ($(this).isBlank())
		displayRightMsg(nameCheck, i18n['err.name'], false);
});

// Displays confirmation message if a user clicks on 'Remove note'.
$('#delete-note').live('click', function() {
	return (confirm(i18n['delete.confirm']));
});

// Removes due-date value when clicking on "no due date".
$('#edit-duedate-1').live('click', function() {
	$('#datepicker').val('');
	$('#alt-datepicker').val('');
});

// Displays calendar when clicking on "due date" or date picker.
$('#edit-duedate-2').live('click', function() {
	$('#datepicker').datepicker('show');
});
