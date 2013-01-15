var now = new java.util.Date();
println(">>> Test2: start ["+now+"] ...");

var getThreadInfo = function(){
	var threadName = java.lang.Thread.currentThread().getName();
	buf.append('Current Thread: ['+threadName+']<br/>');
}

buf.append("==== Test2 ====<br/>");
buf.append("Test Time: ["+now+"]<br/>");
getThreadInfo();

buf.append("==== System properties ====</br>");
buf.append(getSystemProps());

now = new java.util.Date();
println(">>> Test2: end ["+now+"] .");
