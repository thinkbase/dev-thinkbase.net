var now = new java.util.Date();
println(">>> Test1: start ["+now+"] ...");

var getThreadInfo = function(){
	var threadName = java.lang.Thread.currentThread().getName();
	buf.append('Current Thread: ['+threadName+']<br/>');
}

buf.append("==== Test1 ====<br/>");
buf.append("Test Time: ["+now+"]<br/>");
getThreadInfo();

now = new java.util.Date();
println(">>> Test1: end ["+now+"] .");
