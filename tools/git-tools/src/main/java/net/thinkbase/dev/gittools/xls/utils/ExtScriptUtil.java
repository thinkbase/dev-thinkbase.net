package net.thinkbase.dev.gittools.xls.utils;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.thinkbase.dev.gittools.service.vo.CommitStatInfo;
import net.thinkbase.dev.gittools.xls.vo.StatDetailBean;

public class ExtScriptUtil {
	/**
	 * Get groovy engine with "run" function defined
	 * @param groovySrc
	 * @return
	 * @throws ScriptException
	 */
	public static ScriptEngine getScriptEngine(String groovySrc) throws ScriptException {
		ScriptEngineManager factory = new ScriptEngineManager();
	    ScriptEngine engine = factory.getEngineByName("groovy");

	    //Assign a resource loader for scripting
	    Bindings binding = engine.createBindings();
	    binding.put("engine", engine);
	    
	    //eval source
	    engine.eval(groovySrc, binding);
	    
	    return engine;
	}
	
	/**
	 * Call "run" function with {@link CommitStatInfo} and {@link StatDetailBean}, so extension fields in {@link StatDetailBean}
	 * could be filled by groovy script
	 * @param engine
	 * @param ci
	 * @param si
	 * @throws NoSuchMethodException
	 * @throws ScriptException
	 */
	public static void processExtension(ScriptEngine engine, CommitStatInfo ci, StatDetailBean si) throws NoSuchMethodException, ScriptException {
		((Invocable)engine).invokeFunction("run", ci, si);
	}
}
