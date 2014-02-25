define(function () {
    /** Replace ${XXX} in a string */
    var replacePlaceHolder = function(string, dataOrProvider, thisObj){
        if (!string || !dataOrProvider){
            return string;
        };
        if (! thisObj){
            thisObj = this;
        }
        string = string + "";   //Force to javascript string
        var result = string.replace(
            /\$\{(.*?)\}/g,
            function(s0){
                s0 = s0.substr(2,s0.length-3);
                var result = null;
                if (typeof dataOrProvider == "function"){
                    //If argument 2 is a provider
                    result = dataOrProvider.call(thisObj, s0);
                }else{
                    //Just the data
                    result = dataOrProvider[s0];
                }
                if (null==result){
                    result = s0;
                }
                return result;
            }
        );
        return result;
    };
    
    /** Trim string or string array */
    var trim = function(str){
        if (! str){
            return str;
        }
        if (str.join){
            //If argument is an array, trim every element
            for(var i=0; i<str.length; i++){
                str[i] = trim(str[i]);
            }
            return str;
        }else{
            var jstr = new java.lang.String(str);
            jstr = jstr.trim();
            return jstr + "";
        }
    };
    
    /** Get every element of an array and process */
    var arrayForEach = function(array, fn, thisObj){
        if (!array || !array.join || !fn){
            return;
        }
        if (! thisObj){
            thisObj = this;
        }
        for(var i=0; i<array.length; i++){
            fn.call(thisObj, array[i], i);
        }
    }
    
    /** String escape for properties file key or value string; \=\\, :=\:, ==\= */
    var escapeProperty = function(keyOrValue){
        if (keyOrValue){
            return keyOrValue.replace(/\\/g, "\\\\").replace(/\:/g, "\\:").replace(/\=/g, "\\=");
        }else{
            return keyOrValue;
        }
    }
    
    return {
        replacePlaceHolder: replacePlaceHolder,
        trim: trim,
        arrayForEach: arrayForEach,
        escapeProperty: escapeProperty
    }
});