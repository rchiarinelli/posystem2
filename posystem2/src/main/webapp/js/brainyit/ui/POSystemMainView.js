/**
 * Main class of POSystem UI
 */
if (!dojo._hasResource["brainyit.ui.POSystemMainView"]) {
	dojo._hasResource["brainyit.ui.POSystemMainView"] = true;
	dojo.require("brainyit.ui.POSystemUI");
	dojo.require("brainyit.ui.pieces.TopMenu");
	dojo.require("brainyit.ui.pieces.Content");
	dojo.require("brainyit.ui.pieces.MessagePane");
	dojo.provide("brainyit.ui.POSystemMainView");
	dojo.declare(
	    "brainyit.ui.POSystemMainView",brainyit.ui.POSystemUI,
	    {
	    	className : 'brainyit.ui.POSystemMainView'
	    	//,topMenu : null
	    	,contentPanel : null
	    	,messagePanel : null
			,constructor : function() {
				this.gutters=false;
				this.style="width: 100%; height: 100%;";
				this.sppliter=false;
			}
			,startup : function() {
				if (this._started) {
					return;
				}
				this.inherited(arguments);
				console.debug('Building...:' + this.className);
				//var hasPermission = checkPermissiom('','');
				var hasPermission = true;
				if (hasPermission) {
					//Build UI
					this.buildUI();
				}				
			}
			,buildUI : function() {
				if (this.contentPanel==null) {
					console.debug('Building UI');
					//this.topMenu =      new brainyit.ui.pieces.TopMenu();
					this.messagePanel = new brainyit.ui.pieces.MessagePane();
					this.contentPanel = new brainyit.ui.pieces.Content();
					//Add top menu to main window
					//this.addChild(this.topMenu);
					this.addChild(this.messagePanel);
					this.addChild(this.contentPanel);
				}
			}
			//,getTopMenu : function() {
			//	return this.topMenu;
			//}
			,getMessagePanel : function() {
				return this.messagePanel;
			}
			,getContentPanel : function() {
				return this.contentPanel;
			}

	    }
	);
}

