"use strict";

/* The implementations */
// Nothing

/** Define the export point for module */
module.exports = {
    upper: function(str){
        if (! str) return str;
        return str.toUpperCase();
    },
    lower: function(str){
        if (! str) return str;
        return str.toLowerCase();
    }
}
