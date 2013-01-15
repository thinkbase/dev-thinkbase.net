package net.thinkbase.js.rhino.ext.impl;

import javax.swing.JFrame;

import org.eclipse.wst.jsdt.debug.rhino.debugger.RhinoDebugger;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.tools.debugger.Main;

public class DebuggerTools {
	private static final String TYPE_PREFIX_LOCAL = "local://";
	private static final String TYPE_PREFIX_JSDT = "jsdt://";
	
	private static RhinoDebugger jsdtDebugger;
	private static Main localDebugger;
	
	public static void initDebugger(String sysPropName, String envVarName){
		String setting = getDebuggerSetting(sysPropName, envVarName);
		if (null==setting){
			//Do nothing
		}else if (setting.startsWith(TYPE_PREFIX_LOCAL)){
			initLocalDebugger(setting);
		}else if (setting.startsWith(TYPE_PREFIX_JSDT)){
			initJSDTDebugger(setting);
		}
	}
	
	public static void resetDebugger(){
		try {
			if (null!=jsdtDebugger){
				jsdtDebugger.stop();
				ContextFactory.getGlobal().removeListener(jsdtDebugger);
				jsdtDebugger = null;
			}
			if (null!=localDebugger){
				localDebugger.detach();
				localDebugger.dispose();
				localDebugger = null;
			}
		} catch (RuntimeException re){
			throw re;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}
	
	private static String getDebuggerSetting(String sysPropName, String envVarName){
		String sysProp = System.getProperty(sysPropName);
		String envVar = System.getenv(envVarName);
		
		String setting = null;
		
		if ( (null!=sysProp) && (sysProp.trim().length()>0) ){
			setting = sysProp.trim();
		}else if ( (null!=envVar) && (envVar.trim().length()>0) ){
			setting = envVar.trim();
		}
		
		if ( null==setting ){
			return null;
		}else if( setting.startsWith(TYPE_PREFIX_LOCAL) || setting.startsWith(TYPE_PREFIX_JSDT) ){
			return setting;
		}else{
			throw new RuntimeException("Unexcepted debugger setting: " + setting);
		}
	}
	
	private static void initJSDTDebugger(String setting){
		try {
			resetDebugger();
			String jsdt = setting.substring(TYPE_PREFIX_JSDT.length());
			jsdtDebugger = new RhinoDebugger(jsdt);
			jsdtDebugger.start();
			ContextFactory.getGlobal().addListener(jsdtDebugger);
		} catch (RuntimeException re){
			throw re;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void initLocalDebugger(String setting){
		resetDebugger();
		String title = setting.substring(TYPE_PREFIX_LOCAL.length());
		if (title.length() <=0) title = "Rhino debugger";
		localDebugger = new Main(title);
		
		localDebugger.getDebugFrame().setSize(800, 600);
		localDebugger.setVisible(true);
		localDebugger.getDebugFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);
		localDebugger.getDebugFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		localDebugger.attachTo(ContextFactory.getGlobal());
	}
}
