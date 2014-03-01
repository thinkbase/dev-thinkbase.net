define(function () {
    var log = getLogger("shell.js");
    var FileUtils = Packages.org.apache.commons.io.FileUtils;

    /** Makes a directory, including any necessary but non-existence parent directories;
      * support more then one directory(argument "path" as array). */
    var mkdir = function(path){
        if (! path){
            log.warn("Path is BLANK("+path+"), no directory created");
            return;
        }
        if (path.join){
            
        }else{
            if (! (path instanceof java.io.File) ){
                path = new java.io.File(new java.lang.String(path+""));
            }
            FileUtils.forceMkdir(path);
            log.info("Directory ["+path+"] created.");
        }
    }
    
    /** Copy file and replace place-holder variables, always use UTF-8 encoding */
    var copyTemplateFile = function(tmplPath, targetPath, dataOrProvider, thisObj){
        if (! thisObj){
            thisObj = this;
        }
        var template = FileUtils.readFileToString(new java.io.File(tmplPath), "UTF-8");
        require (["std/utils/misc"], function(replacer) {
            var result = replacer.replacePlaceHolder(template, dataOrProvider, thisObj);
            FileUtils.write(new java.io.File(targetPath), result, "UTF-8");
        });
    }
    
    /** Start a process with given environment variables */
    var startProcess = function(cmdLine, envVars, stamp4Kill){
        _startProcess(cmdLine, envVars, stamp4Kill, true);
    }
    var _startProcess = function(cmdLine, envVars, stamp4Kill, withShutdownHook){
        log.info("Start process [" + cmdLine + "], stamp4Kill=["+stamp4Kill+"], withShutdownHook=["+withShutdownHook+"]");
    
        var ProcessBuilder = Packages.java.lang.ProcessBuilder;
        var VerboseProcess = Packages.com.jcabi.log.VerboseProcess;
        require (["std/os"], function(os) {
            var killByStamp = function(stamp){
                if (os.isWindows){
                    _startProcess("wmic process where \"name='java.exe' and commandline like '%%"+stamp+"%%'\" call terminate", {}, null, false);
                }else{
                    _startProcess("pkill -f '"+stamp+"'", {}, null, false);
                    java.lang.Thread.sleep(1000);  //FIXME: in linux, pkill should not terminate process immediately
                }
            };
            //If given stamp4Kill, kill subprocess by stamp when shutdown
            if (stamp4Kill) {
                log.info("Clean existing subprocess with same stamp: ["+stamp4Kill+"]...");
                killByStamp(stamp4Kill);
            }
        
            var pb;
            if (os.isWindows){
                pb = new ProcessBuilder("cmd", "/c", cmdLine);
            }else{
                pb = new ProcessBuilder("/bin/bash", "-c", cmdLine);
            }
            var env = pb.environment();
            //Put env into environment variables
            for(var o in envVars){
                env.put(new java.lang.String(o+""), new java.lang.String(envVars[o]+""));
            }
            //Start process
            var p = pb.start();
            //Register shutdown hook to destory process
            if (withShutdownHook){
                var thread = new java.lang.Thread(new java.lang.Runnable({
                    run: function () {
                        log.info("Stop process when JVM shutdown: " + pb.command());
                        p.destroy();
                        //If given stamp4Kill, kill subprocess by stamp when shutdown
                        if (stamp4Kill) {
                            log.info("Clean subprocess with same stamp: ["+stamp4Kill+"]...");
                            killByStamp(stamp4Kill);
                        }
                    }
                }), "ShutdownHook-" + pb.command());
                java.lang.Runtime.getRuntime().addShutdownHook(thread);
            }
            //Echo it's stdout and stderr
            (new VerboseProcess(p)).stdoutQuietly();
        });
    }
    
    return {
        mkdir: mkdir,
        copyTemplateFile: copyTemplateFile,
        startProcess: startProcess
    }
});