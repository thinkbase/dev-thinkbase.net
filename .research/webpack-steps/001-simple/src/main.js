var __modules = {
    "jquery": require("jquery"),
    "date-format": require("date-format")
}

window._require = function(name){
    return __modules[name];
}
