print(" >> module [a] is loading ...");
define(function () {
    require (["libs/c"], function(c) {
        print(' >>> modules [c] loaded');
        print('  >> c.name = ' + c.name);
    });
    return {
        name: "A",
        notes: "It's just a test module"
    }
});