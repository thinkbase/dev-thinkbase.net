package net.thinkbase.dev.gittools.xls.utils;

import java.io.File;
import java.io.IOException;

import org.codehaus.groovy.control.CompilationFailedException;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;
import net.thinkbase.dev.gittools.service.vo.CommitStatInfo;
import net.thinkbase.dev.gittools.xls.vo.StatDetailBean;

public class ExtScriptUtil {
	/** The class name of extension groovy class */
	private static final String GIT_TOOLS_EXT_METHOD = "run"; 
	
	/**
	 * Build groovy instance with {@link #GIT_TOOLS_EXT_METHOD} function defined
	 * @param groovySrc
	 * @return
	 */
	public static GroovyObject buildExtInstance(File groovySrc) {
		try {
			GroovyClassLoader classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());  
			Class<?> groovyClass = classLoader.parseClass(new GroovyCodeSource(groovySrc, "utf-8"));  
			GroovyObject instance = (GroovyObject)groovyClass.getDeclaredConstructor().newInstance();
			return instance;
		} catch (CompilationFailedException | ReflectiveOperationException | SecurityException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Call "run" function with {@link CommitStatInfo} and {@link StatDetailBean}, so extension fields in {@link StatDetailBean}
	 * could be filled by groovy script
	 * @param instance
	 * @param ci
	 * @param si
	 */
	public static void processExtension(GroovyObject instance, CommitStatInfo ci, StatDetailBean si) {
		instance.invokeMethod(GIT_TOOLS_EXT_METHOD, new Object[] {ci, si});
	}
}
