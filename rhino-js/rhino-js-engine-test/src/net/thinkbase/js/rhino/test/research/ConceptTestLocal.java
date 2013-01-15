package net.thinkbase.js.rhino.test.research;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.swing.JFrame;

import net.thinkbase.js.rhino.JsEngine;
import net.thinkbase.js.rhino.utils.Utils;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.tools.debugger.Main;

public class ConceptTestLocal {

	@BeforeClass
	public static void setUp() throws Exception {
		final Main debugger = new Main("调试窗口");
		
		debugger.getDebugFrame().setSize(800, 600);
		debugger.setVisible(true);
		debugger.getDebugFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);
		debugger.getDebugFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				
		debugger.attachTo(ContextFactory.getGlobal());
	}

	@Test
	public void firstTest() throws IOException {
		StringBuffer buf = new StringBuffer();
		
		JsEngine jse = new JsEngine(this.getClass().getName());
		jse.addObject("buf", buf);
		
		String srcFile = "/ConceptTest.firstTest.js";
		String src = Utils.readAsText(this.getClass().getResourceAsStream(srcFile));
		jse.eval(srcFile, src);
		
		assertEquals("1st line,2nd line,3rd line", buf.toString());
	}
	@Test
	public void secondTest() throws IOException {
		JsEngine jse = new JsEngine(this.getClass().getName());

		String srcFile = "/ConceptTest.secondTest-func.js";
		String src = Utils.readAsText(this.getClass().getResourceAsStream(srcFile));
		jse.eval(srcFile, src);

		srcFile = "/ConceptTest.secondTest.js";
		src = Utils.readAsText(this.getClass().getResourceAsStream(srcFile));
		jse.eval(srcFile, src);
	}

}
