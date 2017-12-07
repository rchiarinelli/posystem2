/**
 *Class to execute the post switch panel processment. 
 */
if (!dojo._hasResource["brainyit.ui.switcher.PosSwitchPanelProcess"]) {
	dojo._hasResource["brainyit.ui.switcher.PosSwitchPanelProcess"] = true;
	dojo.provide("brainyit.ui.switcher.PosSwitchPanelProcess");
	dojo.declare(
	    "brainyit.ui.switcher.PosSwitchPanelProcess",null,
	    {
	    	constructor : function(mainPane) {
			}
			,process:function(mainWindow,parent,newPanel,standbyWidget) {
				var d = new dojo.Deferred();
				try {
					standbyWidget.hide();
//					var messagePanel = mainWindow.getMessagePanel();
//					messagePanel.destroyDescendants(false);
//					var messagePanelId = messagePanel.id;
//					dojo.html.set(dojo.byId(messagePanelId), '');
					d.callback(true);
				} catch(e){
					console.error(e)
					d.errback(e);	
				}
				console.debug('Exiting Post processing switch');
				return d;
			}
	    }
	);
}