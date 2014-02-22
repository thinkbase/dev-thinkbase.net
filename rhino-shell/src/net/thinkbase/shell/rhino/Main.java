package net.thinkbase.shell.rhino;

import java.io.IOException;
import java.net.URL;

import net.thinkbase.js.rhino.JsEngine;
import net.thinkbase.js.rhino.ext.EngineManager;

import org.apache.commons.io.IOUtils;

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
	private static JsEngine currentEngine = null;
	public static void main(String[] args) throws Exception {
		log("Rhino-shell starting ...");
		try{
			EngineManager.initEnv();
			currentEngine = EngineManager.getRootEngine();
			//Prepare RequireJS
			injectArguments(args);
			doLoad("r/env.js");
			doLoad("r/r.js");
			//Prepare shell build-in variables
			currentEngine.addObject("__ARGS__", args);	/*The command-line arguments*/
			//Run application
			currentEngine = EngineManager.buildEngine("App");
			doLoad("/app.js");
		}finally{
			EngineManager.cleanEnv();
		}
		log("Rhino-shell complete.");
	}

	private static void doLoad(String resource) throws IOException{
		URL url = Main.class.getResource(resource);
		String code = IOUtils.toString(Main.class.getResourceAsStream(resource), "UTF-8");
		currentEngine.eval(url.toExternalForm(), code);
	}

	public static void load(String resource) throws IOException{
		log("Loading ["+resource+"] ...");
		doLoad(resource);
		log("Load ["+resource+"] successfully.");
	}
	private static void injectArguments(String[] args){
		//"arguments" object is needed by r.js
		EngineManager.getRootEngine().addObject("arguments", args);
	}

	private static void log(String s){
		System.out.println("[Shell] " + s);
	}
}
