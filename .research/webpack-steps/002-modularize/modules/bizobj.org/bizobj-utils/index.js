"use strict";

/* The implementations */
var STD_FORMAT="yyyy-MM-dd";
var STD_FORMAT_READ="%Y-%m-%d";
var _stdDate2String = function(d){
    var formatter=require("date-format");
    var result = formatter.asString(STD_FORMAT, d);
    return result;
}
var _stdString2Date = function(s){
    var tempus=require('tempusjs');
    var tDate = tempus(s, STD_FORMAT_READ);
    return tDate.get("DateUTC");
}

/** Define the export point for module */
module.exports = {
    stdDate2String: _stdDate2String,
    stdString2Date: _stdString2Date
}
