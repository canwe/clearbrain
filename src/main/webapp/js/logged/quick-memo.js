/*** Quick memo ***/
var editor, //CLEditor
	saved; //Flag to know if memo was saved.

jQuery(function($) {
	editor = $('#input').cleditor({width: '946px', height: '100%'})[0].focus();
    $(window).resize();
    $('#cleditor-container').removeClass('hiddenClass');
    saved = true;

    //Set flag to specify the memo content has changed, to prevent exit without saving
    editor.change(function() {
		saved = false;
    });

    //Set flag to specify when the form is submit, content is saved
    $('#memoform').submit(function() {
		saved = true;
    });
});

//Auto resize
$(window).resize(function() {
	var $win = $(window);
	$('#cleditor-container')
		.width('946px')
		.height($win.height() - 120)
		.offset({top:50});
	editor.refresh();
});

//Alert when leaving if memo was modified
$(window).bind('beforeunload', function(){
	if (saved == false) {
		return false;
	}
});
