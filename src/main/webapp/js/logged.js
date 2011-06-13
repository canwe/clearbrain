/*** Categories ***/
var catPositions, //used to know the categories positions
	catNoteStack = new Array(), //stack to know which are the current selected categories to be the new note's category
	flagNoteDragged = false; //flag to know if a note is currently dragged or not

//Once document is ready (dashboard)
jQuery(function($) { //same as $(document).ready(function()
	var categories = $('#categories');

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
});

//When clicking on the "Finish editing" link
$('#categories-endedit').live('click', function() {
	$('#categories-edit-link').show();
	$('#categories-edit-container').hide();
	$('#cat-unclassified').show();
	$('#categories').find('span[id^=catcount-]').show();
	$('#categories').find('span[id^=catmenu-]').hide();
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
$('#categories').find('a[id^=catrmv-]').live('click', function(e) {
	var catId = $(this).attr('id').replace(/^catrmv-/, '');

	if (confirm(i18n['cat.confRm'] + ' ' + $('#catname-' + catId).text() + i18n['cat.confRmQ'])) {
		$.post('dashboard-js', {
			rmCat : catId
		}, function() {
			//Check if this category was selected, if yes, select the default category
			if ($('#categories').find('.selected-category').attr('id') == ('cat-' + catId))
				selectCategory($('#cat-unclassified'));
			$('#cat-' + catId).remove();
			fillCatPositionsArray();
		});
	}
});


/*** Notes ***/
//Get note id by removing 'note-' before
function getNoteId(id) {
	return id.replace(/^note-/, '');
}

//Display note edit button when hovering "in" note
$('#notes-container').find('div[id^=note-]').live('mouseenter', function(e) {
	if (!flagNoteDragged)
		$('#noteedit-' + getNoteId($(this).attr('id'))).show();
});

//Display note edit button when hovering "out" note
$('#notes-container').find('div[id^=note-]').live('mouseleave', function(e) {
	if (!flagNoteDragged)
		$('#notes-container').find('span[id^=noteedit-]').hide();
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







///* Categories */
////When we click on the checkbox of a category
//$('#categories').delegate('li input', 'click', function(event) {
//	showHideCategory(getCategoryId($(this).parent().attr('id')), $(this).attr('checked'));
//});
//
////Display or hide category
//function showHideCategory(id, display) {
//	$.post('dashboard-js', {
//		id : id, //category's id
//		display : display //display (true / false)
//	});
//}
//