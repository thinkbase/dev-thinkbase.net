/* Sth. needed by r.js */
(function(){
    this.getLogger = function(category){
        return __CTX__.getLogger(category);
    }
    
    var _printlog = this.getLogger("r.js");
    
    this.print = function(msg){
        _printlog.info(msg);
    };
    
    this.load = function(resource){
        __CTX__.load(resource);
    }
    
    //arguments was initialized in net.thinkbase.shell.rhino.Main
})();
