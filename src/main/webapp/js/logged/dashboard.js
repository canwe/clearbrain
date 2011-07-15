/*** Categories ***/
var catPositions, //used to know the categories positions
	catNoteArray = new Array(), //to know which note belong to which category. key: noteId - value: categoryId
	catNoteStack = new Array(), //stack to know which are the current selected categories to be the new note's category
	flagNoteDragged = false; //flag to know if a note is currently dragged or not

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

	//Make notes draggable
	$('#notes-container').find('div[id^=note-]').draggable({
		containment: '#content',
		distance: 10,
		opacity: 0.8,
		revert: true,
		revertDuration: 200,
		start: function(event, ui) {
			flagNoteDragged = true;
			$('#notes-container').find('span[id^=noteedit-]').hide();
		},
		stop: function(event, ui) {
			flagNoteDragged = false;
		}
	});
	$('#notes-container').find('div[id^=note-]').disableSelection();

	//Assign a category to a note
	$('#categories-container').find('*[id^=cat-]').droppable({
		accept: '#notes-container div',
		tolerance: 'touch',
		greedy: true,
		over: function(event, ui) {
			assignCatToNoteOver($(this));
		},
		out: function(event, ui) {
			assignCatToNoteOut($(this));
		},
		drop: function(event, ui) {
			assignCatToNoteDrop(categories, ui);
		}
	});
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

	//Stop elements to be draggable
	$('#notes-container').find('div[id^=note-]').draggable('disable');
	$('#categories').sortable('disable');

	//Dotted borders of elements to specify they can't be draggabled
	$('#categories').find('li[id^=cat-]').addClass('dotted-border');
	$('#notes-container').find('div[id^=note-]').addClass('dotted-border');
});

//When clicking on the "Finish editing" link
$('#categories-endedit').live('click', function() {
	$('#categories-edit-link').show();
	$('#categories-edit-container').hide();
	$('#cat-unclassified').show();
	$('#categories').find('span[id^=catcount-]').show();
	$('#categories').find('span[id^=catmenu-]').hide();
	countNbNotesPerCategory(); //in case a category was removed or notes were assigned to new categories

	//Enable draggable elements again
	$('#notes-container').find('div[id^=note-]').draggable('enable');
	$('#categories').sortable('enable');

	//Dotted borders of elements to specify they can't be draggabled
	$('#categories').find('li[id^=cat-]').removeClass('dotted-border');
	$('#notes-container').find('div[id^=note-]').removeClass('dotted-border');

});

//Add a category when pressing 'Enter key'
$('#catadd-name').live('keyup', function(e) {
	if (e.keyCode == 13 && $(this).val() != '') {
//		$(this).attr('disabled', true);
		$.post('dashboard-js', {
			addCat : $(this).val()
		}, function(data) {
//			//TODO: Clone element and modify it
//			$(this).val('');
//			//Update catPositions
//			catPositions[catPositions.length] = data.id;
//			$(this).attr('disabled', false);

			//Easy way: reload page, TODO: remove this later
			window.location.reload();
		});
	}
});

//Remove a category when clicking on the 'cross' icon
$('#categories').find('img[id^=catrmv-]').live('click', function(e) {
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
			for (var noteId in catNoteArray) {
				if (catNoteArray[noteId] == catId) {
					$('#notecat-' + noteId).html(undefinedCategoryName);
					catNoteArray[noteId] = 0;
				}
			}

			//Remove category
			$('#cat-' + catId).remove();
			fillCatPositionsArray();
		});
	}
});

//Rename a category when clicking on the 'rename' icon
$('#categories').find('img[id^=catrnm-]').live('click', function(e) {
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
				}, function() {
					//TODO: Notification
				});

				//Loop notes to change category's name
				for (var noteId in catNoteArray)
					if (catNoteArray[noteId] == catId)
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


/*** Notes ***/
//Get note id by removing 'note-' before
function getNoteId(id) {
	return id.replace(/^note-/, '');
}

//Display note edit button when hovering "in" note
$('#notes-container').find('div[id^=note-]').live('mouseenter', function(e) {
	if (!flagNoteDragged) {
		$('#noteedit-' + getNoteId($(this).attr('id'))).show();
	}
});

//Display note edit button when hovering "out" note
$('#notes-container').find('div[id^=note-]').live('mouseleave', function(e) {
	if (!flagNoteDragged) {
		$('#notes-container').find('span[id^=noteedit-]').hide();
	}
});

//when a note is over a category
function assignCatToNoteOver(elem) {
	var insertToStack = true;
	if (catNoteStack.length < 2) {
		elem.addClass('category-to-note');
		//Do not touch to array but remove class of everyone except current
		for (var i = 0; i < catNoteStack.length; i++) {
			if (catNoteStack[i].attr('id') != elem.attr('id'))
				catNoteStack[i].removeClass('category-to-note');
			else
				insertToStack = false;
		}
		if (insertToStack)
			catNoteStack[catNoteStack.length] = elem;
	}
}

//when a note is out from a category
function assignCatToNoteOut(elem) {
	elem.removeClass('category-to-note');
	var curElem = null;

	//Remove element from stack and reset colors to already existing stack elements
	for (var i = 0; i < catNoteStack.length; i++)
		if (catNoteStack[i].attr('id') != elem.attr('id')) {
			catNoteStack[i].addClass('category-to-note');
			curElem = catNoteStack[i];
			break;
		}

	catNoteStack = new Array();
	if (curElem != null)
		catNoteStack[0] = curElem;
}

//When a note is dropped in a category
function assignCatToNoteDrop(categories, ui) {
	var noteId = getNoteId(ui.draggable.attr('id')),
		selectedCat = categories.find('.category-to-note'),
		catId = (selectedCat.length == 0 ? 0 : getCategoryId(selectedCat.attr('id')));

	//Remove border for all
	for (var i = 0; i < catNoteStack.length; i++)
		catNoteStack[i].removeClass('category-to-note');
	catNoteStack = new Array();

	//Send request to server
	$.post('dashboard-js', {
		assignCat : catId,
		noteId : noteId
	}, function(data) {
		//Change category name in Note
		$('#notecat-' + noteId).html($('#catname-' + catId).html());
		catNoteArray[noteId] = catId;
		countNbNotesPerCategory();
		//TODO: Notification
	});
}

//Add a note when pressing 'Enter key'
$('#quick-add-task').live('keyup', function(e) {
	if (e.keyCode == 13 && $(this).val() != '') {
//		$(this).attr('disabled', true);
		$.post('dashboard-js', {
			addNote : $(this).val(),
			catId : getSelectedCategoryId()
		}, function(data) {
			//TODO: A lot of things (create a templateNote, clone it...)
//			$(this).attr('disabled', false);
			$(this).val('');
			$('#quick-add-task').clearField()
			//Easy way: reload page, TODO: remove this later
			window.location.reload();
		});
    }
    return false;
});

//Display category name in notes
function displayCategoryNameInNotes() {
	for (var noteId in catNoteArray) {
		$('#notecat-' + noteId).html($('#catname-' + catNoteArray[noteId]).html());
	}
}

//Display Nb of notes per category
function countNbNotesPerCategory() {
	var nbNotesPerCategory = new Array();

	//Fill nbNotesPerCategory Array
	for (var key in catNoteArray) {
		var catId = catNoteArray[key];
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
