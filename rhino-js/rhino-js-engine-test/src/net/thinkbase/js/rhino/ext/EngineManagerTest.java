package net.thinkbase.js.rhino.ext;

import java.io.IOException;

import net.thinkbase.js.rhino.JsEngine;
import net.thinkbase.js.rhino.utils.Utils;

import org.junit.Test;

public class EngineManagerTest {
	private void doTest(String name) throws IOException {
		for(int i=0; i<5; i++){
			String srcName = "EngineManagerTest." + i + ".js";
			String src = Utils.readAsText(
					this.getClass().getResourceAsStream("/" + srcName));
			JsEngine jse = EngineManager.buildEngine(srcName);
			jse.addObject("_NAME_", name);
			jse.eval("rhino-js-engine-test-javascript/" + srcName, src);
		}
	}

	@Test
	public void testLocalDebugger() throws IOException {
		System.setProperty(EngineManager.DEBUGGER_SYS_PROP_NAME, "local://Test Debugger");
		EngineManager.initEnv();
		
		doTest("local");
		
		EngineManager.cleanEnv();
	}

	@Test
	public void testJSDTDebugger() throws Exception {
		System.setProperty(
				EngineManager.DEBUGGER_SYS_PROP_NAME, "jsdt://transport=socket,suspend=y,address=9999");
		EngineManager.initEnv();
		
		doTest("jsdt");
		
		EngineManager.cleanEnv();
	}

}
