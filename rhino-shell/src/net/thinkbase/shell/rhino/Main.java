package net.thinkbase.shell.rhino;

import java.io.IOException;
import java.net.URL;

import net.thinkbase.js.rhino.JsEngine;
import net.thinkbase.js.rhino.ext.EngineManager;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * The JavasSript shell runner based on <a href="https://github.com/thinkbase/dev-thinkbase.net/tree/master/rhino-js">rhino-js-engine</a>;
 * And use <a href="https://github.com/jrburke/r.js">r.js</a> for <a href="http://requirejs.org">RequireJS</a> support.
 * <br/>
 * This application has no build-in command line arguments, because it should load "/loader.js" as the start point, so you should
 * put this file in your classpath then it should work.
 * 
 * @author root
 */
public class Main {
	private static final Logger log = Logger.getLogger(Main.class.getName());
	
	private JsEngine currentEngine = null;
	private int exitCode = 0;
	
	public static void main(String[] args) throws Exception {
		Main m = new Main();
		m.start(args);
	}
	public void start(String[] args) throws Exception {
		log.info("Rhino-shell starting ...");
		try{
			EngineManager.initEnv();
			currentEngine = EngineManager.getRootEngine();
			//Prepare shell build-in variables
			currentEngine.addObject("__ARGS__", args);	/*The command-line arguments*/
			currentEngine.addObject("__CTX__", this);	/*The context object*/
			//Prepare RequireJS
			currentEngine.addObject("arguments", args); //"arguments" object is needed by r.js
			doLoad("r/r-env.js");
			doLoad("r/r.js");
			//Run application
			currentEngine = EngineManager.buildEngine("App");
			doLoad("/app.js");
		}finally{
			EngineManager.cleanEnv();
		}
		if (exitCode !=0){
			log.warn("Rhino-shell exited with errorCode="+exitCode);
		}else{
			log.info("Rhino-shell finished.");
		}
		System.exit(exitCode);		//Force exit to avoid program hanging-up(always cause by Debugger thread)
	}

	public void load(String resource) throws IOException{
		log.debug("Loading ["+resource+"] ...");
		doLoad(resource);
		log.debug("Load ["+resource+"] successfully.");
	}
	public void setExitCode(int code){
		this.exitCode = code;
	}
	public Logger getLogger(String logger){
		return Logger.getLogger("_script_." + logger);
	}

	private void doLoad(String resource) throws IOException{
		URL url = Main.class.getResource(resource);
		String code = IOUtils.toString(Main.class.getResourceAsStream(resource), "UTF-8");
		currentEngine.eval(url.toExternalForm(), code);
	}
}
