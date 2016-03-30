/**
 * REF: http://www.boiajs.com/2015/08/25/webpack-dev-server-and-express-server/
 */
 //The dir of main js file
var MAIN_DIR = require('path').dirname(require.main.filename);

/* NOTE: Because the webpack config file (webpack.web-test-config.js) was not loaded at this
         point, so we must require mudules with absolute path.
 */
var webpack = require(MAIN_DIR+'/node_modules/webpack');
var WebpackDevServer = require(MAIN_DIR+'/node_modules/webpack-dev-server');
var proxy = require(MAIN_DIR+'/node_modules/proxy-middleware');
var url = require(MAIN_DIR+'/node_modules/url');
var express = require(MAIN_DIR+'/node_modules/express');

module.exports = {
    start: function(cfg, appCallback) {

        if (! cfg){
            cfg = {};
        }

        var wdsPort = cfg.wdsPort || 8081;
        var httpPort = cfg.httpPort || 8080;
        var wdsPubPath = cfg.wdsPubPath || "web-test";
        if (wdsPubPath.startsWith("/")){
            wdsPubPath = wdsPubPath.substring(1);
        }
        if (wdsPubPath.endsWith("/")){
            wdsPubPath = wdsPubPath.substring(0, wdsPubPath.length-1);
        }

        var defWebpackCfg;
        var webpackCfg = cfg.webpackCfg;
        if (! webpackCfg){
            try{
                defWebpackCfg = MAIN_DIR+"/webpack.web-test-config.js";
                webpackCfg = require(defWebpackCfg);
                console.log(">>> Webpack config(For testing only): "+defWebpackCfg);
            }catch(ex){
                defWebpackCfg = __dirname+"/webpack.web-test-config.js";
                webpackCfg = require(defWebpackCfg);
                console.log(">>> Webpack config(For testing only): "+defWebpackCfg);
            }
        }

        var wdsCfg = cfg.wdsCfg || {
            contentBase: MAIN_DIR,
            hot: true,
            quiet: false,
            noInfo: false,
            publicPath: '/'+wdsPubPath+'/',
            stats: { colors: true }
        };
        console.log(">>> ContentBase: "+wdsCfg.contentBase);

        var app = express();
        app.get('/', function (req, resp) {
            resp.send('[webpack-dev-support] It works!')
        });
        app.use('/'+wdsPubPath, proxy(url.parse('http://localhost:8081/'+wdsPubPath)));
        if (appCallback) {
            appCallback(app);
        }
        app.listen(httpPort);
        console.log('>>> WebServer listen: ' + httpPort)

        var server = new WebpackDevServer(webpack(webpackCfg), wdsCfg)
            .listen(wdsPort, 'localhost', function() {
                console.log('>>> WebpackDevServer socketio listen: ' + wdsPort)
        });
    }
};
