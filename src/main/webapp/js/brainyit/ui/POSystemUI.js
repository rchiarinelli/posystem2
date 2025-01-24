if (!dojo._hasResource["brainyit.ui.POSystemUI"]) {
	dojo._hasResource["brainyit.ui.POSystemUI"] = true;
	dojo.require("dijit.layout.BorderContainer");
	dojo.provide("brainyit.ui.POSystemUI");
	dojo.declare(
	    "brainyit.ui.POSystemUI",dijit.layout.BorderContainer,
	    {
	    	resourceName : null
	    	, operationType : null
			,constructor : function() {
			}
			,checkPermission: function(resource,operation) {
				console.debug('Checking permissions');
				var basePath = dojo.getObject("basePath");
				var subscriber = dojo.getObject("subscriber");
				var url = basePath + '/services/'+subscriber+'/permissions/'+resource +'/'+operation;
				dojo.xhrGet({
		            url: url
		            ,headers: {
		                "Content-Type": "text/plain"
		            }
		            ,handleAs: "json"
		            ,preventCache:true
		            ,load: function(data,args){
		            	console.debug('Logged user has authority to access this page');
		            	return true;
		            },
		            // if any error occurs, it goes here:
		            error: function(error,args){
		            	console.debug('Logged user does not has authority to access this page');
		            	return false;
		            }
		        });				
			}
	    }
	);
}

