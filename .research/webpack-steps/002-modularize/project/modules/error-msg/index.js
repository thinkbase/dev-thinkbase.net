"use strict";

/* The implementations */
var dialog = require("dialog-component");
require("dialog-component/dialog.css");

require("./index.css");

var msgs = {
    "1": "The first error",
    "2": "The second error"
}
var defMsg = "Unknown error";
var _raiseErr = function(code){
    code = "" + code;
    var msg = msgs[code];
    if (!msg){
        msg = defMsg;
    }
    dialog("ERROR", msg).closable().modal().show();
};

/** Define the export point for module */
module.exports = {
    raiseErr: _raiseErr
}
