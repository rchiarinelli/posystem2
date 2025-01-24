/**
 *Class to execute the pre switch panel processment. 
 */
if (!dojo._hasResource["brainyit.ui.switcher.PreSwitchPanelProcess"]) {
	dojo._hasResource["brainyit.ui.switcher.PreSwitchPanelProcess"] = true;
	dojo.require("brainyit.ui.util.ComponentHelper");
	dojo.provide("brainyit.ui.switcher.PreSwitchPanelProcess");
	dojo.declare(
	    "brainyit.ui.switcher.PreSwitchPanelProcess",null,
	    {
	    	constructor : function(mainPane) {
			}
			,process:function(mainWindow,parent,newPanel,standbyWidget) {
				var d = new dojo.Deferred();
				try {
					standbyWidget.show();
//					console.debug('Pre-processing switch');
//					var divParams=new Array();
//					divParams[0]=['class','processingMessage'];
//					var componentHelper = new brainyit.ui.util.ComponentHelper();
//					var messagePanel = mainWindow.getMessagePanel();
//					var messagePanelId = messagePanel.id;
//					dojo.html.set(dojo.byId(messagePanelId), '');
//					console.debug('Adding processing message to message panel');
//					var divComp = componentHelper.buildComponent(dojo.byId(messagePanelId),'div',divParams);
//					dojo.html.set(divComp, 'Processando.....');
//					messagePanel.startup();
//					messagePanel.buildRendering();
//					messagePanel.layout();
					d.callback(true);
				} catch(e){
					console.error(e)
					d.errback(e);
				}
				console.debug('Exiting Pre processing');
				return d;
			}
	    }
	);
}