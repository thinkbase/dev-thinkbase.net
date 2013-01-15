package net.thinkbase.js.rhino.test.jetty;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.thinkbase.js.rhino.JsEngine;
import net.thinkbase.js.rhino.ext.EngineManager;
import net.thinkbase.js.rhino.utils.Utils;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public class ServerApp {
    private static final Logger log = Log.getLogger(ServerApp.class);
    
    private static JsEngine TEST2_LIB;
    
    public static void main(String[] args) throws Exception {
        initSystem(null);
        
        Server server = new Server(8080);
        server.setHandler(new AbstractHandler() {
            public void handle(String target, Request baseRequest,
                    HttpServletRequest request, HttpServletResponse response)
                    throws IOException, ServletException {
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
                
                String uri = request.getRequestURI();
                log.info("Handler URI ["+uri+"] ...");
                String result = dispatch(uri);
                
                String html = Utils.readAsText(
                        ServerApp.class.getResourceAsStream("template.html"));
                html = html.replace("${RESULT}", result);
                
                response.getWriter().println(html);
            }
        });
        server.start();
        server.join();
    }
    
    private static final String dispatch(String uri)throws IOException{
        if ("/T1".equals(uri)){
            return doTest1();
        }else if ("/T2".equals(uri)){
            return doTest2();
        }else if ("/LOCAL".equals(uri)){
            String cfg = "local://Test Debugger";
            initSystem(cfg);
            return "Switch to local javascript debugger: "+cfg;
        }else if ("/JSDT".equals(uri)){
            String cfg = "jsdt://transport=socket,suspend=n,address=9999";
            initSystem(cfg);
            return "Switch to JSDT javascript debugger: "+cfg+
            		"<br/><br/>---- <b>Attach to localhost:9999 for debugging ...</b>";
        }else if ("/".equals(uri)){
            initSystem(null);
            return "No javascript debugger attched";
        }else {
            return "Unknown uri: ["+uri+"]";
        }
    }
    
    private static final void initSystem(String cfg) throws IOException{
        if (null==cfg) cfg = "";
        System.setProperty(EngineManager.DEBUGGER_SYS_PROP_NAME, cfg);
        EngineManager.initEnv();
        
        //Init the prototype Engine for Test2
        TEST2_LIB = EngineManager.buildEngine("Test2Lib");
        
        TEST2_LIB.eval(
            "rhino-js-engine-test/server-src/net/thinkbase/js/rhino/test/jetty/json_tools.js",
            Utils.readAsText(ServerApp.class.getResourceAsStream("json_tools.js"))
        );        

        String libName = "ServerApp.Test2lib.js";
        String libSrc = Utils.readAsText(
                ServerApp.class.getResourceAsStream("/" + libName));
        TEST2_LIB.eval("rhino-js-engine-test-javascript/" + libName, libSrc);        
    }
    
    private static final String doTest1() throws IOException{
        StringBuffer buf = new StringBuffer();

        JsEngine js = EngineManager.buildEngine("Test1");
        js.addObject("buf", buf);
        
        String srcName = "ServerApp.Test1.js";
        String src = Utils.readAsText(
                ServerApp.class.getResourceAsStream("/" + srcName));
        js.eval("/rhino-js-engine-test-javascript/" + srcName, src);
        
        return buf.toString();
    }
    private static final String doTest2() throws IOException{
        StringBuffer buf = new StringBuffer();

        JsEngine js = EngineManager.buildEngine("Test2", TEST2_LIB);
        js.addObject("buf", buf);
        
        String srcName = "ServerApp.Test2.js";
        String src = Utils.readAsText(
                ServerApp.class.getResourceAsStream("/" + srcName));
        js.eval("rhino-js-engine-test-javascript/" + srcName, src);
        
        return buf.toString();
    }
}
