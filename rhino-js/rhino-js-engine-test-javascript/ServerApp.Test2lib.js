var getSystemProps = function(){
	var props = java.lang.System.getProperties();
	var names = props.stringPropertyNames();
	names = names.toArray();
	var result = {};
	for (var i=0; i<names.length; i++){
		var key = names[i];
		var val = props.getProperty(key);
		result[key+""] = val+"";
	}
	result = JSON.stringify(result);
	result = JSON_Tools.format(result);
	return result;
}
