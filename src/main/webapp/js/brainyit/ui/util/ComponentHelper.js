/**
 * Base class for UI component helpers
 */
if (!dojo._hasResource["brainyit.ui.util.ComponentHelper"]) {
	dojo._hasResource["brainyit.ui.util.ComponentHelper"] = true;
	dojo.provide("brainyit.ui.util.ComponentHelper");
	dojo.declare(
	    "brainyit.ui.util.ComponentHelper",null,
	    {
			constructor : function() {
			}
			,buildComponent : function(parent,compType,params) {
				var component = dojo.create(compType,null,parent);
				if (params!=null) {
					for(var i = 0; i <params.length;i++){
						dojo.attr(component,params[i][0],params[i][1]);
					}
				}
				return component;
			}
			,getSelectedItem:function(grid){
				
			}
	    }
	);
}