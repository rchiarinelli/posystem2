/**
 * Class to hold the filter properties
 */
if (!dojo._hasResource["brainyit.ui.util.FilterProperties"]) {
	dojo._hasResource["brainyit.ui.util.FilterProperties"] = true;
	dojo.provide("brainyit.ui.util.FilterProperties");
	dojo.declare(
	    "brainyit.ui.util.FilterProperties",null,
	    {
	    	properties:null
			,constructor : function() {
			}
	    	,add:function(key, value) {
				if (this.properties==null) {
					this.properties = new Array();
				}
				if (key!=null && value!=null) {
					this.properties.push([key,value]);					
				}
			}
	    	,get:function(key){
				for (var i = 0; i < this.properties.length; i++) {
					if (this.properties[i][0]==key){
						return this.properties[i][1];
					}
				}
			}
			,getKeyValuePair:function(key){
				for (var i = 0; i < this.properties.length; i++) {
					if (this.properties[i][0]==key){
						return this.properties[i];
					}
				}
			}
			
	    }
	);
}