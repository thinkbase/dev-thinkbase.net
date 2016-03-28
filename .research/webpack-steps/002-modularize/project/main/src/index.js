"use strict";

/*Created by `webpack-dyn-index-creater, `, Tue Mar 29 2016 04:36:53 GMT+0800 (CST)*/
window._require = function(moduleName, callback){

    if ("jquery"==moduleName){
        require.ensure(["jquery","../../../modules/bizobj.org/bizobj-utils"], function(require) {
            callback(require("jquery"));
        });
    }
    if ("bizobj-utils"==moduleName){
        require.ensure(["jquery","../../../modules/bizobj.org/bizobj-utils"], function(require) {
            callback(require("../../../modules/bizobj.org/bizobj-utils"));
        });
    }

    if ("thinkbase-ajax"==moduleName){
        require.ensure(["../../../modules/thinkbase.net/thinkbase-ajax","../../../modules/thinkbase.net/thinkbase-ui"], function(require) {
            callback(require("../../../modules/thinkbase.net/thinkbase-ajax"));
        });
    }
    if ("thinkbase-ui"==moduleName){
        require.ensure(["../../../modules/thinkbase.net/thinkbase-ajax","../../../modules/thinkbase.net/thinkbase-ui"], function(require) {
            callback(require("../../../modules/thinkbase.net/thinkbase-ui"));
        });
    }

    if ("error-msg"==moduleName){
        require.ensure(["../../modules/error-msg","../../modules/string-utils"], function(require) {
            callback(require("../../modules/error-msg"));
        });
    }
    if ("string-utils"==moduleName){
        require.ensure(["../../modules/error-msg","../../modules/string-utils"], function(require) {
            callback(require("../../modules/string-utils"));
        });
    }

}
