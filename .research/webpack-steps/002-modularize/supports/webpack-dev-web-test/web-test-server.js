/**
 * REF: http://www.boiajs.com/2015/08/25/webpack-dev-server-and-express-server/
 */
 var webpack = require('webpack');
 var WebpackDevServer = require('webpack-dev-server');
 var proxy = require('proxy-middleware');
 var url = require('url');
 var express = require('express');

module.exports = {
    start: function(cfg, appCallback) {
        //The dir of main js file
        var mainDir = require('path').dirname(require.main.filename);

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
                defWebpackCfg = mainDir+"/webpack.web-test-config.js";
                webpackCfg = require(defWebpackCfg);
                console.log(">>> Webpack config(For testing only): "+defWebpackCfg);
            }catch(ex){
                defWebpackCfg = __dirname+"/webpack.web-test-config.js";
                webpackCfg = require(defWebpackCfg);
                console.log(">>> Webpack config(For testing only): "+defWebpackCfg);
            }
        }

        var wdsCfg = cfg.wdsCfg || {
            contentBase: mainDir,
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
