/*** Categories ***/
var catPositions, //used to know the categories positions
	catNA = new Array(); //CatNoteArray: to know which note belong to which category. key: noteId - value: categoryId

//Once document is ready
jQuery(function($) {
	var categories = $('#categories');

	//Display categories names and #nb of notes per category.
	displayCategoryNameInNotes();
	countNbNotesPerCategory();

	//Enable clearField plugin on quick-add-task field
	$('#quick-add-task').clearField();

	//Make categories sortable
	fillCatPositionsArray();
	categories.sortable({
		axis: 'y',
		containment: '#content',
		distance: 10,
		opacity: 0.95,
		placeholder: 'highlight',
		update: function(event, ui) {
			updateCategoryPosition(event, ui);
		}
	});
	categories.disableSelection();
	$('#cat-unclassified').disableSelection();
});

//Change style when selecting a category (starting by 'cat-' by clicking on it)
$('#categories-container').find('*[id^=cat-]').live('click', function() {
	if ($('#categories-edit-link').is(':visible')) //only if we are not updating categories
		selectCategory($(this));
});

//Get category id by removing 'cat-' before
function getCategoryId(id) {
	return id.replace(/^cat-/, '');
}

//Browse elements and fill a category position array
function fillCatPositionsArray() {
	catPositions = new Array()
	var idx = 0;
	$('#categories').find('li').each(function() {
		catPositions[idx++] = getCategoryId($(this).attr('id'));
	});
}

//Select a category (display a green arrow)
function selectCategory(category) {
	$('#categories-container').find('.category-selected').removeClass('category-selected');
	category.addClass('category-selected');
}

//Get the current selected category
function getSelectedCategoryId() {
	var selectedCat =$('#categories').find('.category-selected');
	if (selectedCat.length == 0)
		return 0;
	return getCategoryId(selectedCat.attr('id'));
}

//Update category position
function updateCategoryPosition(event, ui) {
	$.post('dashboard-js', {
		updPos : getCategoryId(ui.item.attr('id')), //category's id
		prev : catPositions[ui.item.index()], //previous category's id in that position before update
		before : (ui.position.top < ui.originalPosition.top) //before or after
	}, function(data) {
		if (data == false) { //detect error... refresh page if any
			alert(i18n['cat.updErr']);
			window.location.reload();
		}
		else
			fillCatPositionsArray();
	});
}

//When clicking on the "Add / Edit categories" link
$('#categories-edit').live('click', function() {
	$('#categories-edit-link').hide();
	$('#categories-edit-container').show();
	$('#cat-unclassified').hide();
	$('#categories').find('span[id^=catcount-]').hide();
	$('#categories').find('span[id^=catmenu-]').show();
	selectCategory($('#cat-unclassified'));
	displayMsgIfNoCategory();

	//Stop elements to be draggable
	$('#notes-container').find('div[id^=note-]').draggable('disable');
	$('#categories').sortable('disable');

	//Specify elements can't be moved
	$('#categories').find('li').addClass('undraggable');
});

//When clicking on the "Finish editing" link
$('#categories-endedit').live('click', function() {
	$('#categories-edit-link').show();
	$('#categories-edit-container').hide();
	$('#cat-unclassified').show();
	$('#categories').find('span[id^=catcount-]').show();
	$('#categories').find('span[id^=catmenu-]').hide();
	$('#no-category').hide();
	countNbNotesPerCategory(); //in case a category was removed or notes were assigned to new categories

	//Enable draggable elements again
	$('#notes-container').find('div[id^=note-]').draggable('enable');
	$('#categories').sortable('enable');

	//Specify elements can't be moved
	$('#categories').find('li').removeClass('undraggable');
});

//Add a category when pressing 'Enter key'
$('#catadd-name').live('keyup', function(e) {
	if (e.keyCode == 13 && $(this).val() != '') {
		$.post('dashboard-js', {
			addCat : $(this).val()
		}, function() {
			window.location.reload();
		});
	}
});

//Remove a category when clicking on the 'cross' icon
$('#categories').find('img[id^=catrmv-]').live('click', function() {
	var catId = $(this).attr('id').replace(/^catrmv-/, '');

	if (confirm(i18n['cat.confRm'] + ' ' + $('#catname-' + catId).text() + i18n['cat.confRmQ'])) {
		$.post('dashboard-js', {
			rmCat : catId
		}, function() {
			//Check if this category was selected, if yes, select the default category
			if ($('#categories').find('.selected-category').attr('id') == ('cat-' + catId))
				selectCategory($('#cat-unclassified'));

			//Update each note whose category was the deleted one to set the new category as Unclassified
			var undefinedCategoryName = $('#catname-0').html()
			for (var noteId in catNA) {
				if (catNA[noteId] == catId) {
					$('#notecat-' + noteId).html(undefinedCategoryName);
					catNA[noteId] = 0;
				}
			}

			//Remove category
			$('#cat-' + catId).remove();
			fillCatPositionsArray();
			displayMsgIfNoCategory();
		});
	}
});

//Rename a category when clicking on the 'rename' icon
$('#categories').find('img[id^=catrnm-]').live('click', function() {
	var catId = $(this).attr('id').replace(/^catrnm-/, ''),
		catName = $('#catname-' + catId),
		previousName = catName.html();

	//Replace name by Input text
	$('#categories').enableSelection();
	$('#catmenu-' + catId).hide();
	catName.html('<input type="text" value="' + previousName.replace(/"/g, '&quot;') + '" />');
	catName.find('input').focus();

	//When pressing the Enter key
	catName.find('input').live('keyup', function(e) {
		if (e.keyCode == 13)
			$('#catadd-name').trigger('focus'); //to make element loose focus and enable blur
	});

	//When loosing focus on input
	catName.find('input').blur(function() {
		$('#categories').disableSelection();
		var newName = $('#catname-' + catId).find('input').val();

		//Update name (only if it has changed)
		if (previousName != newName) {
			if (confirm(i18n['cat.confRn1'] + previousName + i18n['cat.confRn2'] + newName + i18n['cat.confRn3'])) {
				$.post('dashboard-js', {
					rnmCat : catId,
					newName : newName
				}, function() {});

				//Loop notes to change category's name
				for (var noteId in catNA)
					if (catNA[noteId] == catId)
						$('#notecat-' + noteId).html(newName);
			}
			else {
				newName = previousName;
			}
		}
		catName.html(newName);
		$('#catmenu-' + catId).show();
	});
});

//If there is no category currently, display a special message and focus on insert
function displayMsgIfNoCategory() {
	if ($('#categories').find('li').length == 0) {
		$('#no-category').show();
		$('#catadd-name').focus();
	}
}

/*** Notes ***/
//Get note id by removing 'note-' before
function getNoteId(id) {
	return id.replace(/^note-/, '');
}

//Go to note edit when clicking on a note
$('#notes-container').find('div[id^=note-]').live('click', function() {
	window.location = ('note?id=' + getNoteId($(this).attr('id')));
});

//Display note edit button when hovering "in" note
$('#notes-container').find('div[id^=note-]').live('mouseenter', function() {
	$('#noteedit-' + getNoteId($(this).attr('id'))).show();
});

//Display note edit button when hovering "out" note
$('#notes-container').find('div[id^=note-]').live('mouseleave', function() {
	$('#notes-container').find('span[id^=noteedit-]').hide();
});

//Add a note when pressing 'Enter key'
$('#quick-add-task').live('keyup', function(e) {
	if (e.keyCode == 13 && $(this).val() != '') {
		$.post('dashboard-js', {
			addNote : $(this).val(),
			catId : getSelectedCategoryId()
		}, function() {
			$(this).val('');
			$('#quick-add-task').clearField()
			window.location.reload();
		});
    }
    return false;
});

//Display category name in notes
function displayCategoryNameInNotes() {
	for (var noteId in catNA) {
		$('#notecat-' + noteId).html($('#catname-' + catNA[noteId]).html());
	}
}

//Display Nb of notes per category
function countNbNotesPerCategory() {
	var nbNotesPerCategory = new Array();

	//Fill nbNotesPerCategory Array
	for (var key in catNA) {
		var catId = catNA[key];
		if (nbNotesPerCategory[catId])
			nbNotesPerCategory[catId]++;
		else
			nbNotesPerCategory[catId] = 1;
	}

	//Loop each category and update #nb of notes per category
	$('#categories-container').find('span[id^=catcount-]').each(function() {
		var catId = $(this).attr('id').replace(/^catcount-/, '');
		$(this).html('(' + (nbNotesPerCategory[catId] ? nbNotesPerCategory[catId] : 0) + ')');
	});
}

//Check/uncheck todo
$('#notes-container').find('input[type=checkbox]').live('click', function(event) {
	$.post('dashboard-js', {
		noteId : getNoteId($(this).parent().attr('id')),
		checked : $(this).is(':checked')
	}, function(data) {
		refreshCountTodoHeader(data[0], data[1], data[2]);
	});

	event.stopPropagation(); //to avoid a redirection to the edit note page while clicking on the note element
});

//Refresh in JS the count todo header
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
