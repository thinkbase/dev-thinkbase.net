var ajax = require("../src/index");
var $ = require("jquery");
window.doTest = function(){
    ajax.post("/test-data.json", {}, function(json){
        $("#console").text("Name: "+json.name+"; timestamp: "+json.timestamp);
    });
}
window.do500Error = function(){
    ajax.post("/test-500.html", {}, function(json){
        $("#console").text("Error NOT raised!");
    });
}
window.do404Error = function(){
    ajax.post("/test-404.html", {}, function(json){
        $("#console").text("Error NOT raised!");
    });
}
