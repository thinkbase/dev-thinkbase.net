package net.thinkbase.js.rhino.ext;

import net.thinkbase.js.rhino.JsEngine;
import net.thinkbase.js.rhino.ext.impl.DebuggerTools;
import net.thinkbase.js.rhino.utils.Utils;

/**
 * The JsEngine manager
 * @author thinkbase.net
 */
public class EngineManager {
	/**
	 * The environment variable name of debugger setting string, support 2 kind of debuggers:
	 * <ul>
	 * 	<li>1. Rhino debugger, setting string is :
	 * 		   <code>local://{debugger name}</code></li>
	 *  <li>2. eclipse JSDT, setting string is :
	 *         <code>jsdt://transport=socket,suspend={y|n},address={tcp port}</code></li>
	 * </ul> 
	 */
	public static final String DEBUGGER_ENV_VAR_NAME = "THINKBASE_NET_RHINO_DEBUGGER";
	/**
	 * The java system property name of debugger setting string,
	 * should overwrite the environment variable setting
	 */
	public static final String DEBUGGER_SYS_PROP_NAME =
			EngineManager.class.getName() + "." + DEBUGGER_ENV_VAR_NAME;
	
	/**
	 * The js file to init root engine
	 */
	private static final String ROOT_JS_FILE = "root.js";
	
	/**
	 * The root engine, it's the root scope of all javascript engines
	 */
	private static JsEngine rootEngine;
	
	/**
	 * Initialize the javascript execution environment, include:
	 * <ul>
	 * 	<li>1. Setup the debugger</li>
	 * 	<li>2. Initialize the root engine</li>
	 * </ul>
	 */
	public synchronized static final void initEnv(){
		try {
			//Init debugger
			DebuggerTools.resetDebugger();
			DebuggerTools.initDebugger(DEBUGGER_SYS_PROP_NAME, DEBUGGER_ENV_VAR_NAME);
			
			//Create root engine
			rootEngine = new JsEngine(EngineManager.class.getName());
			//Init rootEngine
			String src = Utils.readAsText(EngineManager.class.getResourceAsStream(ROOT_JS_FILE));
			rootEngine.eval(ROOT_JS_FILE, src);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Clean the current javascript execution environment
	 */
	public synchronized static final void cleanEnv(){
		DebuggerTools.resetDebugger();
		rootEngine = null;
	}
	
	/**
	 * Get the root javascript engine
	 * @return
	 */
	public static JsEngine getRootEngine(){
		return rootEngine;
	}
	
	/**
	 * Build a javascript engine, with specified engine name, and a prototype engine to inherit
	 * @param name
	 * @param prototype
	 * @return
	 */
	public static JsEngine buildEngine(String name, JsEngine prototype){
		if (null==prototype){
			prototype = rootEngine;
		}
		return new JsEngine(name, prototype);
	}
	/**
	 * Build a javascript engine, with specified engine name
	 * @param name
	 * @return
	 */
	public static JsEngine buildEngine(String name){
		return buildEngine(name, null);
	}
}
