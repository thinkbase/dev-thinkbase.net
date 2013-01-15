/**
 * The build-in functions
 */
(function() {               // Using a closure to keep global namespace clean.
    /**
     * print a message
     */
    this.println = function(message){
        java.lang.System.out.println(message);
    }
    
    /**
     * show message box
     */
    this.alert = function(message){
        Packages.javax.swing.JOptionPane.showMessageDialog(null, message);
    }
    
    /**
     * dump javascript object to string;
     * @param obj: javascript object to dump;
     * @param maxLevel: the depth to dump properties recursively, default is 1(no recursion);
     * @param showFunc: <code>true</code> means show both properties and function,
     *                  <code>false</code> show properties only; default is <code>false</code>;
     */
    this.dump = function(obj, maxLevel, showFunc){
        return _dumpDeep(obj, maxLevel, showFunc);
    }
    var _dumpDeep = function(obj, maxLevel, showFunc, space, isStart){
        if (null==maxLevel){
            maxLevel = 1;
        }
        if (null==showFunc){
            showFunc = false;
        }
        if (null==space){
            space = "    ";
        }
        if (null==isStart){
            isStart = true;
        }
        var stepSp = "    ";

        var s = "";
        if (isStart){
            s += ("\n"+ space + "DUMP Object = [" + obj + "]");
        }
        for( var i in obj ){
            var o;
            try{
                o = obj[i];
            }catch(ex){
                o = "ERROR: " + ex;
            }
            if ( (! showFunc)&&(typeof(o)=="function") ){
                continue;
            }
            s += ("\n"+ space + stepSp + i + " = [" + o + "]");
            if (maxLevel>1){
                s += _dumpDeep(o, maxLevel-1, showFunc, space + stepSp, false);
            }
        }
        return s;
    }
}) ();
