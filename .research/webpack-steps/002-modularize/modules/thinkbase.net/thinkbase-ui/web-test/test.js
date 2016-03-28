var ui = require("../src/ui");

window.doTest = function(){
    ui.rotate("#box", 30,   500);          // 30 degrees more
    ui.rotate("#box", -60,  1000);         // 60 degrees less
    ui.rotate("#box", 30,   500);          // 30 degrees more
}
