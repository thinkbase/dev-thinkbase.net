/* Sth. needed by r.js */
(function(){
    this.print = function(string){
        java.lang.System.out.println(string);
    };
    
    this.load = function(resource){
        __CTX__.load(resource);
    }
    
    //arguments was initialized in net.thinkbase.shell.rhino.Main
})();
