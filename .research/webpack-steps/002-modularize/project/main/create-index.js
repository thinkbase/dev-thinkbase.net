var creater = require("../../supports/webpack-dyn-index-creater");

creater.build({
    chunkArrays: [
        ["jquery", "../../modules/bizobj.org/bizobj-utils"],
        ["../../modules/thinkbase.net/thinkbase-ajax", "../../modules/thinkbase.net/thinkbase-ui"],
        ["../modules/error-msg", "../modules/string-utils"]
    ]
});
