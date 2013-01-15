package net.thinkbase.js.rhino;

import net.thinkbase.js.rhino.utils.ContextRunner;
import net.thinkbase.js.rhino.utils.RhinoHelper;
import net.thinkbase.js.rhino.utils.RhinoHelper.RetrieveResult;
import net.thinkbase.js.rhino.utils.Utils;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * The Javascript engine based on Rhino
 * @author thinkbase.net
 */
public class JsEngine {
	/**The name of current engine*/
    private String engineName;
	/**Scope of current engine*/
    private Scriptable scope;
    
    /**
     * New javascript engine
     * @param name
     */
    public JsEngine(String name) {
        this(name, null);
    }
    /**
     * New javascript engine, use another javascript engine as prototype
     * @param name
     * @param prototype
     */
    public JsEngine(String name, JsEngine prototype) {
        if (null==name){
        	engineName = Utils.buildUUID();
        }else{
        	engineName = name;
        }
        this.scope = RhinoHelper.buildStandScope();
        if (null!=prototype && this.scope!=prototype.scope){
            this.scope.setPrototype(prototype.scope);
        }
    }
    
    /**
     * Get the name of current javascript engine
     * @return
     */
    public String getEngineName(){
    	return engineName;
    }
    
    /**
     * Add a java object, as a variable
     * @param varName The variable's name
     * @param object The java object
     */
    public void addObject(final String varName, final Object object) {
    	RhinoHelper.runWithCtx(new ContextRunner() {
			public Object run(Context cx) {
		    	Object wrapper = RhinoHelper.wrapJavaObj(object, scope);
		        ScriptableObject.putProperty(scope, varName, wrapper);
				return null;
			}
		});
    }

    /**
     * Remove a java object from context
     * @param varName
     */
    public void removeObject(final String varName) {
    	RhinoHelper.runWithCtx(new ContextRunner() {
			public Object run(Context cx) {
		        ScriptableObject.deleteProperty(scope, varName);
				return null;
			}
		});
    }
    
    /**
	 * Retrieve the value of a variable in context
	 * @param valName
	 * @return
	 */
	public Object retrieve(final String valName) {
		return RhinoHelper.runWithCtx(new ContextRunner() {
			public Object run(Context cx) {
			    Object jsObj = RhinoHelper.retrieveNative(valName, scope).getJsProp();
			    return RhinoHelper.unwrapJsObj(jsObj, scope);
			}
		});
	}
	/**
	 * Evaluate the javascript code
	 * @param srcName The name of current source, for example, the file name of source
	 * @param src The source code
	 * @return
	 */
	public Object eval(final String srcName, final String src){
		Object jsObj = RhinoHelper.runWithCtx(new ContextRunner() {
			public Object run(Context cx) {
				String name = srcName;
				if (null==name){
					name = Utils.MD5(src);
				}
				return cx.evaluateString(scope, src, name, 1, null);
			}
		});
		return RhinoHelper.unwrapJsObj(jsObj, scope);
	}
	/**
	 * Evaluate the javascript code
	 * @param src The source code
	 * @return
	 */
	public Object eval(String src){
		return eval(null, src);
	}
	
	/**
	 * Call a javascript function
	 * @param functionName The name of javascript function
	 * @param args arguments to call the function
	 * @return
	 */
	public Object invoke(final String functionName, final Object[] args){
		Object jsObj = RhinoHelper.runWithCtx(new ContextRunner() {
			public Object run(Context cx) {
				Object[] funcParams = args;
				if (null==funcParams) funcParams = new Object[]{};
				
				RetrieveResult thisAndFunc = RhinoHelper.retrieveNative(functionName, scope);
				Function f = (Function)thisAndFunc.getJsProp();
				for (int i = 0; i < funcParams.length; i++) {
					//Note: wrap java object to js object is optional, but sometime Function.call should report error:
					//RHINO USAGE WARNING: Missed Context.javaToJS() conversion:
					//Rhino runtime detected object java.io.PrintStream@182f0db of class java.io.PrintStream where it expected String, Number, Boolean or Scriptable instance. Please check your code for missig Context.javaToJS() call.
					funcParams[i]=RhinoHelper.wrapJavaObj(funcParams[i], scope);
				}
				Object o = f.call( cx, scope, thisAndFunc.getThisObj(), funcParams );
				return o;
			}
		});
		return RhinoHelper.unwrapJsObj(jsObj, scope);
	}
	
}

