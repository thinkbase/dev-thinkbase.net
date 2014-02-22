require.config({
    baseUrl: "/app"
});

require (["a", "b"], function(a,  b) {
    print(' > modules [a,b] loaded');
});
