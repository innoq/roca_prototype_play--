/*jslint vars: true, unparam: true, browser: true, white: true */
/*global jQuery */
(function($) {
    'use strict';
     $.fn.filterUpdater = function(options) {
       var config = {
           contentSelector : '#content'
       }
       if (options) {
           $.extend(config, options);
       }
       return $(this).each(function() {
             var self = $(this);
 
             self.submit(function(event) {
                var queryString = $(this).serialize();
                var host = $(this).attr('action');
                var uri = host + '?' + queryString; 
                var contentarea = $(config.contentSelector);
                $.get(uri + '&ajax=ajax', function(data) {
                	contentarea.html(data);
                    if (options.init) {
                    	options.init(contentarea);
                    }
                });
                history.pushState(null, null, uri);
                event.preventDefault();
            });
             
         });
     };
}(jQuery));
