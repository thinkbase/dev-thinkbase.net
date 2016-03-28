/**
 * Build index.js, define `window._require` to make modules require dynamicly.
 */
 var fs = require('fs');
 var path = require('path');

var mkdirs = function(dirpath, mode, callback) {
    fs.exists(dirpath, function(exists) {
        if(exists) {
            callback(dirpath);
        } else {
            //try to make parent first, the current level
            mkdirs(path.dirname(dirpath), mode, function(){
                fs.mkdir(dirpath, mode, callback);
            });
        }
    });
};

var splitNameFromPath = function(pathStr){
    var lastSP = pathStr.lastIndexOf("/");
    if (lastSP<0){
        //only name
        return pathStr;
    }else if (lastSP==module.length-1){
        //the last is "/"
        pathStr = pathStr.substring(0, pathStr.length-1);
        return splitNameFromPath(pathStr);
    }else{
        return pathStr.substring(lastSP+1, pathStr.length);
    }

}

module.exports = {
    /**
     * Build the index.js for dynamic _require() modules loading;
     * @param: cfg: The configuration:
     *      - indexJsPath: Where is the index.js, default "src/index.js"
     *      - chunkArrays: Array of String[] to define the chunks,
     *                     for example: [ ["jquery", "../../modules/error-msg"], [...], [...] ];
     *                     NOTE - the relatice path is recommanded ton refer local modules,
     *                            and the relative path is based `indexJsPath`.
     */
    build: function(cfg) {
        //The dir of main js file
        var mainDir = path.dirname(require.main.filename);

        if (! cfg){
            cfg = {};
        }

        var indexJsPath = cfg.indexJsPath || "src/index.js";
        var chunkArrays = cfg.chunkArrays || [];

        var dir2mk = path.dirname(mainDir + "/" + indexJsPath);
        mkdirs(dir2mk, 0777, function(folder){
            console.log(">>> Folder created: "+dir2mk);

            var buffer = '"use strict";\n'
                       + '\n'
                       + '/*Created by `webpack-dyn-index-creater, `, '+new Date()+'*/\n'
                       + `window._require = function(moduleName, callback){\n`;
            for (var i=0; i<chunkArrays.length; i++){
                var chunk = chunkArrays[i];
                var chunkStr = JSON.stringify(chunk);
                for (var j=0; j<chunk.length; j++){
                    var module = chunk[j];
                    console.log("  >> Chunk: "+chunkStr+", Module="+module);
                    var moduleName = splitNameFromPath(module);
                    buffer = buffer + "\n"
                           + '    if ("'+moduleName+'"==moduleName){\n'
                           + '        require.ensure('+chunkStr+', function(require) {\n'
                           + '            callback(require("'+module+'"));\n'
                           + '        });\n'
                           + '    }'
                }
                buffer = buffer + "\n";
            }
            buffer = buffer + '\n}\n';
            fs.writeFile(indexJsPath, buffer, function(e){
                if(e) {
                    throw e;
                }else{
                    console.log(">>> Index created: "+indexJsPath);
                }
            });
        });
    }
};
