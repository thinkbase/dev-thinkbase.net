print("module [b] is loading ...");

define(function () {
    require (["libs/c", "libs/d"], function(c,  d) {
        getLogger("b.js").info('modules [c,d] loaded');
        getLogger("b.js-info").info('c.name = ' + c.name);
        getLogger("b.js-info").info('d.name = ' + d.name);
    });
    return {
        name: "B",
        notes: "It's just a test module"
    }
});