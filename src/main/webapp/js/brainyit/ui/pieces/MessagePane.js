/**
 * 
 */
if (!dojo._hasResource["brainyit.ui.pieces.MessagePane"]) {
	dojo._hasResource["brainyit.ui.pieces.MessagePane"] = true;
	dojo.require("brainyit.ui.POSystemUI");
	dojo.require("brainyit.ui.util.ComponentHelper");
	dojo.provide("brainyit.ui.pieces.MessagePane");
	dojo.declare(
	    "brainyit.ui.pieces.MessagePane",brainyit.ui.POSystemUI,
	    {
	    	className : 'brainyit.ui.pieces.MessagePane'
			,constructor : function() {
				this.layoutPriority=2;
				this.sppliter=false;
				this.gutters=false;
				this.style="width: 100%; height: 50px;";

			}
			,startup : function() {
				if (this._started) {
					return;
				}
				this.inherited(arguments);
				console.debug('Building..:' + this.className);
			}
	    }
	);
}

