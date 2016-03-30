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
            var allDeps = [];   //Store all dependencies for modify package.json on the fly

            var buffer = '"use strict";\n'
                       + '\n'
                       + '/*Created by `webpack-dyn-index-creater, `, '+new Date()+'*/\n'
                       + `window._require = function(moduleName, callback){\n`;
            for (var i=0; i<chunkArrays.length; i++){
                var chunk = chunkArrays[i];

                //Because index.js was created in "src" folder, so the relative path must add ".."
                var srcChunks = [];
                for (var j=0; j<chunk.length; j++){
                    var module = chunk[j];
                    if (module.startsWith("../")){
                        module = "../" + module;
                    }
                    srcChunks[srcChunks.length] = module;
                }
                var chunkStr = JSON.stringify(srcChunks);

                //Create code to refer every module
                for (var j=0; j<chunk.length; j++){
                    var module = chunk[j];
                    allDeps[allDeps.length] = module;
                    console.log("  >> Chunk: "+chunkStr+", Module="+module);
                    var moduleName = splitNameFromPath(module);
                    if (module.startsWith("../")){
                        module = "../" + module;
                    }
                    buffer = buffer + "\n"
                           + '    if ("'+moduleName+'"==moduleName){\n'
                           + '        require.ensure('+chunkStr+', function(require) {\n'
                           + '            if(callback) callback(require("'+module+'"));\n'
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

            //Modify dependencies in package.json
            var json = fs.readFileSync('package.json', 'utf-8');
            var packageObj = JSON.parse(json);
            var dependencies = packageObj.dependencies;
            if (!dependencies){
                dependencies = {};
                packageObj.dependencies = dependencies;
            }
            for (var i=0; i<allDeps.length; i++){
                var dep = allDeps[i];
                if (dep.startsWith("../")){
                    //The module from relative path
                    var moduleName = splitNameFromPath(dep);
                    dependencies[moduleName] = dep;
                }else{
                    //The module from npm repo - must defined in package.json
                    if (! dependencies[dep]){
                        throw "Module '"+dep+"' MUST define in package.json as 'dependencies'";
                    }
                }
            }
            json = JSON.stringify(packageObj, null, "  ");
            fs.writeFileSync('package.json', json, {encoding: 'utf-8'});
            console.log(">>> package.json updated.");
        });
    }
};
