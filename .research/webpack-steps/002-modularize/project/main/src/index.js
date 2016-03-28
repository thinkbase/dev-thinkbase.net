"use strict";

window._require = function(moduleName, callback){
    if ("bizobj-utils"==moduleName){
        require.ensure(["../../../modules/bizobj.org/bizobj-utils"], function(require) {
            callback(require("../../../modules/bizobj.org/bizobj-utils"));
        });
    }

    if ("thinkbase-ajax"==moduleName){
        require.ensure(["../../../modules/thinkbase.net/thinkbase-ajax", "../../../modules/thinkbase.net/thinkbase-ui"], function(require) {
            callback(require("../../../modules/thinkbase.net/thinkbase-ajax"));
        });
    }
    if ("thinkbase-ui"==moduleName){
        require.ensure(["../../../modules/thinkbase.net/thinkbase-ajax", "../../../modules/thinkbase.net/thinkbase-ui"], function(require) {
            callback(require("../../../modules/thinkbase.net/thinkbase-ui"));
        });
    }

    if ("error-msg"==moduleName){
        require.ensure(["../../modules/error-msg", "../../modules/string-utils"], function(require) {
            callback(require("../../modules/error-msg"));
        });
    }
    if ("string-utils"==moduleName){
        require.ensure(["../../modules/error-msg", "../../modules/string-utils"], function(require) {
            callback(require("../../modules/string-utils"));
        });
    }
}
