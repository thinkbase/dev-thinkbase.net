package net.thinkbase.shell.rhino;

import java.io.IOException;

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
	public static void main(String[] args) throws Exception {
		try{
			EngineManager.initEnv();
			injectArguments(args);
			load("boot.js");
			load("r/r.js");
			load("/loader.js");
		}finally{
			EngineManager.cleanEnv();
		}
	}

	public static void load(String resource) throws IOException{
		log("Loading ["+resource+"] ...");
		String code = readResource(resource);
		EngineManager.getRootEngine().eval(resource, code);
		log("Load ["+resource+"] successfully.");
	}

	private static void injectArguments(String[] args){
		//arguments is needed by r.js
		EngineManager.getRootEngine().addObject("arguments", args);
	}
	private static void log(String s){
		System.out.println("[Main] " + s);
	}
	private static String readResource(String resource) throws IOException {
		String code = IOUtils.toString(Main.class.getResourceAsStream(resource), "UTF-8");
		return code;
	}
}
