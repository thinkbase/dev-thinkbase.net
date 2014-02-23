print("module [a] is loading ...");
define(function () {
    var log = getLogger("a.js");
    require (["libs/c"], function(c) {
        log.info('modules [c] loaded');
        getLogger("a.js-info").info('c.name = ' + c.name);
    });
    return {
        name: "A",
        notes: "It's just a test module"
    }
});