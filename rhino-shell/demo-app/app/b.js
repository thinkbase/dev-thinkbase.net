print(" >> module [b] is loading ...");

define(function () {
    require (["libs/c", "libs/d"], function(c,  d) {
        print(' >>> modules [c,d] loaded');
        print('  >> c.name = ' + c.name);
        print('  >> d.name = ' + d.name);
    });
    return {
        name: "B",
        notes: "It's just a test module"
    }
});