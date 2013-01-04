function init_dom(context) {
	
	// Multi-Select-Filter initialisieren
	$('.multiselect', context).each(function() {
		$(this).multiselect({
			selectedList: 2,
			checkAllText: "All",
			uncheckAllText: "None"
		}).multiselectfilter({label:"", width:"200px"});
	});
	// wenn es die filter_updater Komponente gibt, dann wird sie hier eingebunden
	if ($.fn.filterUpdater) {
		$('form.formular', context).filterUpdater({
			contentSelector: '#contentarea',
			init: init_dom
		});
	}
	
	// markierte Tabellenzeilen draggable machen 
	$('.issues tr[data-issueId]', context).draggable({
		containment: 'document',
		cursor: 'move',
		helper: function(event) {
			var issueId = $(this).attr('data-issueId');
			return '<div>' + issueId + '</div>';
		},
		cursorAt: {
			left: 30,
			top: 10
		}
	});
	
	// markierte Navigationspunkte droppable machen 
	$('.nav li[data-targetUriBase]', context).droppable({
		over: function(event, ui) {
			$(this).addClass('droppable-over');
		},
		out: function(event, ui) {
			$(this).removeClass('droppable-over');
		},
		drop: function(event, ui) {
			$(this).removeClass('droppable-over');

			// Ziel-URI aus Attribut des Navigationspunktes holen
			var targetUriBase = $(this).attr('data-targetUriBase');

			// Error-Task-Id aus Attribut des gezogenen Elements holen
			var issueId = ui.draggable.attr('data-issueId');

			// Query-String aus Attribut der umgebenden Tabelle holen
			var queryString = ui.draggable.closest('table').attr('data-queryString');
			
			var uri = targetUriBase + queryString + " #body-content"; 
			
			$("body").load(
					uri,
					{issueId: issueId},
					function() {
						// ersetzten DOM initialisieren
						init_dom($(this));
					});
		}
	});
	
	// Bootstrap-Rowlinks initialisieren
	$('tbody[data-provides]', context).each(function() {
		if($(this).rowlink != null){
		$(this).rowlink($(this).data());
		}
	});
}

$(document).ready(function() {
	init_dom($(document));
});
