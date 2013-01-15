package net.thinkbase.js.rhino.utils;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.UniqueTag;

/**
 * Helper based on rhino
 * @author thinkbase.net
 */
public class RhinoHelper {
	/**The system level Global context*/
	private static Scriptable rootStandardScope = null;
	
	/**
	 * Build the instances of the standard objects and their constructors
     * (Object, String, Number, Date, etc.), as the global object.
	 * @return
	 */
	public static synchronized final Scriptable buildStandScope(){
		return (Scriptable)runWithCtx(new ContextRunner(){
			public Object run(Context cx) {
				if (null==rootStandardScope){
					rootStandardScope = cx.initStandardObjects();
				}
				Scriptable blankScope = cx.newObject(rootStandardScope);
				blankScope.setPrototype(rootStandardScope);
				blankScope.setParentScope(null);
				return blankScope;
			}
		});
	}
	
	/**
	 * Run script-context based operation with Context enter and exit around.
	 * @param runner
	 * @return
	 */
	public static final Object runWithCtx(ContextRunner runner){
		Context cx = null;
		try{
			cx = Context.enter();
			Object res = runner.run(cx);
			return res;
		}finally{
			if (null!=cx){
				Context.exit();
			}
		}
	}

	/**
	 * Wrap java object to javascript object
	 * @param javaObj
	 * @param scope
	 * @return
	 */
	public static final Object wrapJavaObj(Object javaObj, Scriptable scope){
    	return Context.javaToJS(javaObj, scope);
    }
	/**
	 * Unwrap java object from javascript object
	 * @param jsObj
	 * @param scope
	 * @return
	 */
	public static final Object unwrapJsObj(Object jsObj, Scriptable scope){
        Object val = jsObj;
        if (val instanceof NativeJavaObject) {
            // Un-wrap native java object
            val = ((NativeJavaObject) val).unwrap();
        } else if (val instanceof NativeArray){
            //Un-wrap javascript array
            NativeArray na = ((NativeArray) val);
            long len = na.getLength();
            Object[] array = new Object[new Long(len).intValue()];
            for (int i = 0; i < array.length; i++) {
                Object obj = na.get(i, scope);
                array[i] = unwrapJsObj(obj, scope);
            }
            val = array;
        }else if (isNullNativeObject(val)) {
            val = null;
        }
        return val;
    }
    
    /**
     * Retrieve javascript native object, such as variable and functions
     * @param varName The variables's name to retrieve, for example: person.name
     * @param scope The scope where retrievement start from
     * @return
     */
    public static final RetrieveResult retrieveNative(String varName, Scriptable scope){
        String[] objList = varName.split("\\.");
        Scriptable thisObject = scope;	//Javascript "this" variable
        for (int i = 0; i < objList.length; i++) {
            if (i < objList.length-1){
                Object obj = ScriptableObject.getProperty(thisObject, objList[i]);
                if (isNullNativeObject(obj)){	//[Undefined] js object ...
                    return new RetrieveResult(null, null);
                }
                thisObject = (Scriptable) wrapJavaObj(obj, thisObject);
                if (isNullNativeObject(thisObject)){
                    return new RetrieveResult(null, null);
                }
            }else{
                Object jsObj = ScriptableObject.getProperty(thisObject, objList[i]);
                return new RetrieveResult(thisObject, jsObj);
            }
        }
        return null; //This line could not be executed for ever
    }

    private static boolean isNullNativeObject(Object val){
        if (val instanceof UniqueTag) {
            if (UniqueTag.NOT_FOUND.equals(val)){
                //Tag to mark non-existing values.
                return true;
            } else if (UniqueTag.NULL_VALUE.equals(val)){
                //Tag to distinguish between uninitialized and null values.
                return true;
            }
        } else if (val instanceof Undefined) {
            // Undefined --> null
            return true;
        }
        return false;
    }
    
    /**
     * The structure to store the result of javascript variable retrievement.
     * @author thinkbase.net
     */
    public static class RetrieveResult {
    	/**The javascript "this" object*/
    	private Scriptable thisObj;
    	/**The property to retrieve in "this" object*/
		private Object jsProp;
    	private RetrieveResult(Scriptable thisObj, Object jsProp){
    		this.thisObj = thisObj;
    		this.jsProp = jsProp;
    	}
    	public Scriptable getThisObj() {
			return thisObj;
		}
		public Object getJsProp() {
			return jsProp;
		}
    }
}
