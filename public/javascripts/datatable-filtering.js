$(document).ready(function() {
			/* Table initialisation */
			$('#datatable').dataTable( {
				"sDom": "<'#search'f>t<'#pagination'p>",
				"sPaginationType": "bootstrap",
				"bLengthChange": false,
				"bInfo": false,
				"oLanguage": {
					"sLengthMenu": "_MENU_ records per page"
				}
			});
		});