var app = require("../../../supports/webpack-dev-web-test");

app.start({
    /** The default configuration parameters
      httpPort: 8080,
      wdsPort: 8081,
      wdsPubPath: "web-test",
      webpackCfg: require("./webpack.web-test-config.js")
    */
}, function(app){
    /** The response for ajax post testing */
    app.post('/test-data.json', function(req, resp) {
        resp.send('{"name": "thinkbase-ajax", "timestamp": '+ (new Date()).getTime() + '}');
    });
    app.post('/test-500.html', function(req, resp) {
        throw "500 error for testing";
    });
});
