/* Categories */
var catPositions, //used to know the categories positions
	rmLock = false; //avoid updating category while removing one

jQuery(function($) { //same as $(document).ready(function()
	/***** Categories *****/
	var catName = $('#cat-name'),
		trash = $('#categories-container').find('.trash'),
		categories = $('#categories'),
		quickAdd = $('#quick-add-task'),
		quickAddContainer = $('#quick-add-task-container');

	fillCatPositionsArray();

	//Make categories sortable
	categories.sortable({
		axis: 'y',
		containment: '#categories-container',
		distance: 10,
		placeholder: 'highlight',
		update: function(event, ui) { 
			updateCategoryPosition(event, ui);
		}
	});
	categories.disableSelection();

	//When we click on "Add / Edit categories"
	$('#cat-edit').click(function() {
		editCategory();
	});

	//Add category when pressing 'Enter key'
	catName.live('keyup', function(e) {
		if (e.keyCode == 13 && catName.val() != '') {
			catName.attr('disabled', true);
			addCategory(catName);
		}
	    return false;
	});

	//Remove element when putting it in trash
	trash.droppable({
		over: function(event, ui){
			ui.draggable.hide();
			$(this).addClass('trashover');
			$(this).text('Remove ' + ui.draggable.text());
		},
		out: function(event, ui){
			trashOut(trash, event, ui);
		},
		drop: function(event, ui){
			removeCategory(trash, event, ui);
		}
	});

	//When we click on the checkbox of a category
	categories.delegate('li input', 'click', function(event) {
		showHideCategory(getCategoryId($(this).parent().attr('id')), $(this).attr('checked'));
	});


	/***** Notes *****/
	//When we click on "[+] Add task" button
	$('#header-addtask').click(function() {
		quickAddContainer.slideDown('slow');
	});

	//When we click on "Close" quick add task
	$('#hide-quick-add-task').click(function() {
		quickAddContainer.slideUp('slow');
	});

	//Enable clearField plugin on quick-add-task field
	quickAdd.clearField();

	//Add a note when pressing 'Enter key'
	quickAdd.live('keyup', function(e) {
		if (e.keyCode == 13 && quickAdd.val() != '') {
			quickAdd.attr('disabled', true);
			addNote(quickAdd);
	    }
	    return false;
	});
});

// Browse elements and fill a category position array
function fillCatPositionsArray() {
	catPositions = new Array()
	var idx = 0;
	$('#categories').find('li').each(function() {
		catPositions[idx++] = getCategoryId($(this).attr('id'));
	});
}

//When we cancel the trash operation
function trashOut(trash, event, ui) {
	ui.draggable.show();
	trash.removeClass('trashover');
	trash.text('Trash');
}

//Add a category
function addCategory(catName) {
	$.post('#', {
		addCat : catName.val()
	}, function(data) {
		//TODO: Clone 'unclassified' element and modify it instead of another HTML code in js
		//TODO: Insert element before unclassified
		$('#categories').append('<li id="cat-' + data.id + '"> <input type="checkbox" checked="checked" /> ' + data.name + '</li>');
		catName.val('');
		//Update catPositions
		catPositions[catPositions.length] = data.id;
		$('#categories-container').find('.trash').show(); //show trash
    	catName.attr('disabled', false);
	});
}

//Show category editor
function editCategory() {
	var editContainer = $('#cat-edit-container'),
		trash = $('#categories-container').find('.trash');

	if (editContainer.is(':visible')) {
		trash.hide();
		$('#cat-edit').html('Edit / Add');
	}
	else {
		if (catPositions.length > 0) //display trash only if we have at least one category
			trash.show();
		$('#cat-edit').html('Finish editing');
	}

	editContainer.toggle();
	$('#cat-unclassified').toggle();
	if (editContainer.is(':visible'))
		$('#cat-name').focus();
}

//Get category id by removing 'cat-' before
function getCategoryId(id) {
	return id.replace(/^cat-/, '');
}

//Remove a category when putting it in the trash
function removeCategory(trash, event, ui) {
	rmLock = true;
	trash.removeClass('trashover');
	if (confirm('Do you really want to remove' + ui.draggable.text() + '?')) {
		if (catPositions.length == 1) //hide trash if we don't have a category anymore
			trash.hide();

		$.post('#', {
			rmCat : getCategoryId(ui.draggable.attr('id'))
		}, function() {
			//TODO: Lots of things...
			trash.addClass('trashdeleted');
			trash.text(ui.draggable.text() + ' removed!');
			ui.draggable.remove();
			setTimeout(function() {
				trash.removeClass('trashdeleted'); trash.text('Trash');
			}, 1500);
			fillCatPositionsArray();
			rmLock = false;
		});
	}
	else
		trashOut(trash, event, ui);
}

//Update category position
function updateCategoryPosition(event, ui) {
	if (rmLock == false) {
		$.post('#', {
			updPos : getCategoryId(ui.item.attr('id')), //category's id
			prev : catPositions[ui.item.index()], //previous category's id in that position before update
			before : (ui.position.top < ui.originalPosition.top) //before or after
		}, function(data) {
			if (data == false) {//an error occured... refresh page
				alert('An error occured. Page will be refreshed...')
				window.location.reload();
			}
			else
				fillCatPositionsArray();
		});
	}
}

//Display or hide category
function showHideCategory(id, display) {
	$.post('#', {
		id : id, //category's id
		display : display //display (true / false)
	});
}


/* Notes */

//Add a note
function addNote(noteName) {
	$.post('#', {
		addNote : noteName.val()
	}, function(data) {
		//TODO: A lot of things (create a templateNote, clone it...)
		noteName.val('');
		$('#quick-add-task').clearField()
		noteName.attr('disabled', false);
	});
}
