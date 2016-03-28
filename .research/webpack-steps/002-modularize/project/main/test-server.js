var app = require("../../supports/webpack-dev-web-test");

app.start({}, function(app){
    app.post('/test-data.json', function(req, resp) {
        resp.send('{"name": "thinkbase-ajax", "timestamp": '+ (new Date()).getTime() + '}');
    });
});
