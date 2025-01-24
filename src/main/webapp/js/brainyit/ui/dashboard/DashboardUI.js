/**
 * 
 */
if (!dojo._hasResource["brainyit.ui.dashboard.DashboardUI"]) {
	dojo._hasResource["brainyit.ui.dashboard.DashboardUI"] = true;
	dojo.provide("brainyit.ui.dashboard.DashboardUI");
	dojo.require("brainyit.ui.dashboard.PendingInstallmentsUI");
	dojo.require("brainyit.ui.dashboard.InfoPanelUI");
	dojo.declare(
	    "brainyit.ui.dashboard.DashboardUI",brainyit.ui.POSystemUI,
	    {
	    	className : 'brainyit.ui.dashboard.DashboardUI'
			,constructor : function() {
				console.debug('Building ' + this.className);
			}
			,startup : function() {
				if (this._started) {
					return;
				}
				this.inherited(arguments);
				//Build the two main divs
	    		console.debug('Building..:' + this.className);
	    		var infoPanel = new brainyit.ui.dashboard.InfoPanelUI();
	    		this.addChild(infoPanel);
	    		infoPanel.startup();
				var pendingInstallments = new brainyit.ui.dashboard.PendingInstallmentsUI();
				this.addChild(pendingInstallments);
				pendingInstallments.startup();
			}
	    }
	);
}

