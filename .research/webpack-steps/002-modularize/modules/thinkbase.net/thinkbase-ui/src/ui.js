var $ = require("jquery");

require("jquery.transit");
require("../src/ui.css");

/** Define the export point for module */
module.exports = {
    rotate: function($elm, degree, timeoutMs){
        if (! timeoutMs){
            timeoutMs=500;    //500 ms
        }
        $($elm).transition({ rotate: '+='+degree },  timeoutMs);
    }
}
