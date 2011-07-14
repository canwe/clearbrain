//Check if note's name is not empty.
$('#name').live('change', function() {
	var nameCheck = $('#name-check');
	reinitErrors('#name-error', nameCheck);

	if ($(this).isBlank())
		displayRightMsg(nameCheck, i18n['err.name'], false);
});

//Display confirmation message if user clicks on 'Remove note'
$('#delete-note').live('click', function() {
	return (confirm(i18n['delete.confirm']));
});
