package net.thinkbase.js.rhino.test.research;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import net.thinkbase.js.rhino.JsEngine;
import net.thinkbase.js.rhino.utils.Utils;

import org.eclipse.wst.jsdt.debug.rhino.debugger.RhinoDebugger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mozilla.javascript.ContextFactory;

public class ConceptTestJSDT {

	@BeforeClass
	public static void setUp() throws Exception {
		String jpda = "transport=socket,suspend=y,address=9999";
		
		RhinoDebugger debugger = new RhinoDebugger(jpda);
		debugger.start();
		ContextFactory.getGlobal().addListener(debugger);
	}

	@Test
	public void firstTest() throws IOException {
		StringBuffer buf = new StringBuffer();
		
		JsEngine jse = new JsEngine(this.getClass().getName());
		jse.addObject("buf", buf);
		
		String srcFile = "ConceptTest.firstTest.js";
		String src = Utils.readAsText(this.getClass().getResourceAsStream("/" + srcFile));
		jse.eval("rhino-js-engine-test-javascript/" + srcFile, src);
		
		assertEquals("1st line,2nd line,3rd line", buf.toString());
	}
	@Test
	public void secondTest() throws IOException {
		JsEngine jse = new JsEngine(this.getClass().getName());

		String srcFile = "ConceptTest.secondTest-func.js";
		String src = Utils.readAsText(this.getClass().getResourceAsStream("/" + srcFile));
		jse.eval("rhino-js-engine-test-javascript/" + srcFile, src);

		srcFile = "ConceptTest.secondTest.js";
		src = Utils.readAsText(this.getClass().getResourceAsStream("/" + srcFile));
		jse.eval("rhino-js-engine-test-javascript/" + srcFile, src);
	}

}
