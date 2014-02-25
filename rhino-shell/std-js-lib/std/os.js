define(function () {
    var _export = {};
    require (["std/sys"], function(sys) {
        /** OS name in lower case (linux, windows, ...) */
        var name = java.lang.System.getProperty("os.name").toLowerCase();
        var isWindows = function(){
            return (name.indexOf("win") >= 0);
        };
        var isLinux = function(){
            return (name.indexOf("linux") >= 0);
        };
        var isMac = function() {
            return (name.indexOf("mac") >= 0);
        };
        var isUnix = function() {
            return (name.indexOf("nix") >= 0 || name.indexOf("nux") >= 0 || name.indexOf("aix") > 0 );
        };
        var isSolaris = function() {
            return (name.indexOf("sunos") >= 0);
        };
        
        /** Get java system property or environment variable */
        var getProp = function(name, defaultValue){
            var tmp = sys.appProperties[name];
            if (null == tmp){
                tmp = java.lang.System.getProperty(name);
            }
            if (null == tmp){
                tmp = java.lang.System.getenv(name);
            }
            if (null == tmp){
                tmp = defaultValue;
            }
            //If tmp is java string, convert it to js string, but other object shouldn't convert automatically
            if (null!=tmp && tmp instanceof java.lang.String){
                tmp = tmp + "";
            }
            return tmp;
        }
        
        var _replaceWithSystemProp = function(string){
            var result;
            require (["std/utils/misc"], function(replacer) {
                result = replacer.replacePlaceHolder(string, function(key){
                    return getProp(key);
                });
            });
            return result;
        }
        
        /** The root folder, default is the path of app.js */
        var root = sys.home;
        
        /** Change root folder */
        var chroot = function(newRoot){
            var newRoot = normalizePath(newRoot);
            root = newRoot;
        }
        /** Normalize a path with the specified basePath(parnet folder), or based on the AppRoot */
        var normalizePath = function(path, basePath){
            if (null==path) path="";
            if (path.join){ //If path is Array, join them
                for (var i=0; i<path.length; i++){
                    path[i] = path[i]+"";   //Force to javascript string
                    if (i>0){   //Remain heading "/" for first path element 
                        path[i] = path[i].replace(/^\//, "");   //The start "/"
                        path[i] = path[i].replace(/^\\/, "");   //The start "\"
                    }
                    path[i] = path[i].replace(/\/$/, "");   //The end "/"
                    path[i] = path[i].replace(/\\$/, "");   //The end "\"
                }
                path = path.join(Packages.java.io.File.separator);
            }
            //Replace ${XXX} in path with java system property or environment variable
            path = _replaceWithSystemProp(path);
            //Detect the "path" is absolute or not
            path = new java.lang.String(path);
            var isAbs = false;
            if (path.startsWith("/")) isAbs = true;
            if (path.startsWith("\\")) isAbs = true;
            if ( isWindows() && (path.length()>1) && (path.substr(1,1)==":") ) isAbs = true;
            //Calculate the full path
            if (! isAbs){
                if (! basePath) {
                    basePath = root;
                }
                path = basePath + java.io.File.separator + path;
            }
            //Get normalized Path string
            var file = new java.io.File(path);
            return file.getCanonicalPath()+"";  //Force convert to javascript string
        }
        
        /** Check file/folder exists or not; support wildcard in file name part */
        var fileExists = function(path){
            if (! path) return false;
            
            if (path.join){ //If path is Array...
                var totalResult = false;
                for (var i=0; i<path.length; i++){
                    path[i] = path[i]+"";   //Force to javascript string
                    totalResult = fileExists(path[i]);
                    if (totalResult==tmp){
                        return false;
                    }
                }
                return totalResult;
            }else{
                path = _replaceWithSystemProp(path);
                //Find the folder part and file part of path 
                path = path.replace(/\\/g, "/");
                var tmp = path.split("/");
                var folder = [];
                var file = "";
                for(var i=0; i<tmp.length; i++){
                    if (i==(tmp.length-1)){
                        file = tmp[i];
                    }else{
                        folder[folder.length] = tmp[i];
                    }
                }
                folder = folder.join(java.io.File.separator);
                //Check folder exists or not
                folder = new java.io.File(normalizePath(folder));
                if (! folder.exists()){
                    return false;
                }
                //Check file exists, support wildcard
                var fileFilter = new Packages.org.apache.commons.io.filefilter.WildcardFileFilter(file);
                var files = folder.list(fileFilter);
                return (files.length > 0);
            }
        }
        
        /** Same as fileExists but return the missing file/folders as an array */
        var findMissingFiles = function(path){
            if (! path) return [];
            if (! path.join) path = [path];
            var result = [];
            require (["std/utils/misc"], function(misc) {
                misc.arrayForEach(path, function(f){
                    if (! fileExists(f)){
                        result[result.length] = f;
                    }
                });
            });
            return result;
        }
        
        /** Read text file, return null if file not found */
        var readTextFile = function(file){
            var FileUtils = Packages.org.apache.commons.io.FileUtils;
            var f = new Packages.java.io.File(file);
            if (f.exists()){
                var s = FileUtils.readFileToString(f, "UTF-8");
                return s + "";
            }else{
                return null;
            }
        }
        
        /** Write text file */
        var writeTextFile = function(file, text){
            var FileUtils = Packages.org.apache.commons.io.FileUtils;
            var f = new Packages.java.io.File(file);
            FileUtils.writeStringToFile(f, new java.lang.String(text+""), "UTF-8");
        }
        
        _export = {
            isWindows:isWindows(), isLinux:isLinux(), isMac:isMac(), isUnix:isUnix(), isSolaris:isSolaris(),
            normalizePath: normalizePath,
            chroot: chroot,
            getProp: getProp,
            fileExists: fileExists,
            findMissingFiles: findMissingFiles,
            readTextFile: readTextFile,
            writeTextFile: writeTextFile
        }
    });
    return _export;
});