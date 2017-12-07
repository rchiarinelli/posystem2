dojo.provide("brainyit-dojo.command.SimpleGetCommand");
dojo.declare(
    "brainyit-dojo.command.SimpleGetCommand",
    null,
    {
        target: ""
        , constructor : function(arg1) {
        	this.target = arg1;
        }
    }
    ,
    function execute() {
		var response = null;
		dojo.xhrGet({
            url: this.target
            ,headers: {
                "Content-Type": "text/plain"
            }
            ,handleAs: "json"
            ,preventCache:true
            ,load: function(data,args){
            	response = data;
            },
            // if any error occurs, it goes here:
            error: function(error,args){
                
            }
        });
		return response;
    	
    }
);
