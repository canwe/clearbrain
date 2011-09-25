// Gets note id by removing 'note-' before.
function getNoteId(id) {
	return id.replace(/^note-/, '');
}

// Goes to note edit when clicking on a note.
$('#notes-container').find('div[id^=note-]').live('click', function() {
	window.location = ('note?id=' + getNoteId($(this).attr('id')));
});

// Displays note edit button when hovering "in" note.
$('#notes-container').find('div[id^=note-]').live('mouseenter', function() {
	$('#noteedit-' + getNoteId($(this).attr('id'))).show();
});

// Hides note edit button when hovering "out" note.
$('#notes-container').find('div[id^=note-]').live('mouseleave', function() {
	$('#notes-container').find('span[id^=noteedit-]').hide();
});

// Checks / unchecks todo.
$('#notes-container').find('input[type=checkbox]').live('click', function(event) {
	$.post('dashboard-js', {
		noteId : getNoteId($(this).parent().attr('id')),
		checked : $(this).is(':checked')
	}, function(data) {
		refreshCountTodoHeader(data[0], data[1], data[2]);
	});

	event.stopPropagation(); // to avoid a redirection to the edit note page while clicking on the note element.
});

// Refreshes in JS the todo header counter.
function refreshCountTodoHeaderUpdate(value, spanId, hidden) {
	var span = $(spanId).find('span');

	if (value == '0') {
		if (!span.hasClass(hidden))
			span.addClass(hidden);
	}
	else {
		span.html(value);
		if (span.hasClass(hidden))
			span.removeClass(hidden);
	}
}
function refreshCountTodoHeader(today, tomorrow, week) {
	var hidden = 'hide-forced';

	refreshCountTodoHeaderUpdate(today, '#m-today', hidden);
	refreshCountTodoHeaderUpdate(tomorrow, '#m-tomorrow', hidden);
	refreshCountTodoHeaderUpdate(week, '#m-week', hidden);
}
