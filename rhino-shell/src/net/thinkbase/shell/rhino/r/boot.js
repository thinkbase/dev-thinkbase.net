/* Sth. needed by r.js */
(function(){
    this.print = function(string){
        java.lang.System.out.println(string);
    };
    
    this.load = function(resource){
        Packages.net.thinkbase.shell.rhino.Main.load(resource);
    }
    
    //arguments was initialized in net.thinkbase.shell.rhino.Main#injectArguments
})();
