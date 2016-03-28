"use strict";

/* The implementations */
/**
 * Post json data to server, then run success callback (and with the standard error handler)；
 * @param url post url
 * @param data data object
 * @param successCallback callback when success; with parameter: {1.the return json object}
 */
var _postJson = function(url, dataObj, successCallback){
    var $ = require("jquery");
    var JSON = require("JSON2");
    $.ajax({
        type: 'POST',
        url: url,
        data: dataObj,
        dataType: "json",
        success: function(json){
            successCallback(json);
        },
        error: function(xhr/*XMLHttpRequest*/, err/*error message*/, errStatus/*(optional)The catched exception*/){
            var msg = "Server data process error";
            //Keep slience for canceled request
            if (xhr){
                if (0==xhr.status && "rejected"==xhr.state()){
                    return;
                }
            }

            //Error handler
            var fail;
            if ( xhr && xhr.status && xhr.status>=400 ){
                var status = xhr.status;
                var statusText = xhr.statusText;
                msg += "("+status+"/"+statusText+")";
                var respObj = xhr.responseJSON;
                if (respObj){
                    fail = msg + "\n\n" + JSON.stringify(respObj, null, 4);
                    //msg should not include response JSON object
                }
                var respTxt = xhr.responseText;
                if (respTxt){
                    fail = msg + "\n\n" + respTxt;
                    msg += ": "+respTxt;
                }
            }else{
                if (err) {
                    msg += "("+err+")";
                }
                if (errStatus) {
                    msg += ": " + errStatus;
                }
                fail = msg;
            }

            alert(msg);
            throw fail;
        }
    });
};

/** Define the export point for module */
module.exports = {
    post: _postJson
}
