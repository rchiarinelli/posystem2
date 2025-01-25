/**
 *Class to controle the swith between in the window panels 
 */
if (!dojo._hasResource["brainyit.ui.switcher.WindowSwitcher"]) {
	dojo._hasResource["brainyit.ui.switcher.WindowSwitcher"] = true;
	dojo.require("brainyit.ui.switcher.PosSwitchPanelProcess");
	dojo.require("brainyit.ui.switcher.PreSwitchPanelProcess");
	dojo.require("brainyit.ui.util.ComponentHelper");
	dojo.provide("brainyit.ui.switcher.WindowSwitcher");
	dojo.declare(
	    "brainyit.ui.switcher.WindowSwitcher",null,
	    {
	    	constructor : function(mainPane) {
			}
			,switchPanel:function(parent,newPanel) {
				this.clearMessages();
				//recover main window instance
				var mainWindow = dojo.getObject("_poSystemMainView");
				if (mainWindow==null) {
					console.error('There is no mainWindow instance to handle the panels switch.');
				} else {
					//clean the previous content
					parent.destroyDescendants(false);
					var parentDOMId = parent.id;
					dojo.html.set(dojo.byId(parentDOMId), '');
					//build standby icon
					//Bind actions
					var mainContent = dojo.byId('mainContent');
	                var standby = new dojox.widget.Standby({
	                    target: mainContent.id
	                });
	                dojo.place(standby.domNode,mainContent);					
	                
					//pre process before the panel switch
					var preProc = new brainyit.ui.switcher.PreSwitchPanelProcess();
					var preProcRes = preProc.process(mainWindow, parent, newPanel,standby);
					if (preProcRes.results[1]!=null) {
						//this.showErrorMessage(mainWindow, preProcRes.results[1]);
						standby.hide();
						var dialog = new dijit.Dialog(
	        			{
	        				title:'Erro'
	        				,style:'width:400px'
	        				,closable:true
	        				,content:'Erro ao tentar abrir a p&aacute;gina. Por favor, contate o administrdor do sistema.Erro:' + preProcRes.results[1]
	        			});
		            	dialog.show();					
						
					} else {
						preProcRes.then(function(){
//							setTimeout(function(){
//								//process the panel switchswithStatus)
//								var switcher = new brainyit.ui.switcher.WindowSwitcher();
//								var switchRes = switcher.executeSwitch(mainWindow, parent, newPanel);
//								if (switchRes.results[1]!=null) {
//									switcher.showErrorMessage(mainWindow, switchRes.results[1]);
//								} else {
//									switchRes.then(function(){
//										//pos process after the panel switch
//										var posProc = new brainyit.ui.switcher.PosSwitchPanelProcess();
//										posProc.process(mainWindow, parent, newPanel,standby);
//									});
//								}
//							}, 1000);
							//process the panel switchswithStatus)
							var switcher = new brainyit.ui.switcher.WindowSwitcher();
							var switchRes = switcher.executeSwitch(mainWindow, parent, newPanel);
							if (switchRes.results[1]!=null) {
								//switcher.showErrorMessage(mainWindow, switchRes.results[1]);
								standby.hide();
								var dialog = new dijit.Dialog(
			        			{
			        				title:'Erro'
			        				,style:'width:400px'
			        				,closable:true
			        				,content:'Erro ao tentar abrir a p&aacute;gina. Por favor, contate o administrdor do sistema.Erro:' + switchRes.results[1] 
			        			});
				            	dialog.show();								
							} else {
								switchRes.then(function(){
									//pos process after the panel switch
									var posProc = new brainyit.ui.switcher.PosSwitchPanelProcess();
									posProc.process(mainWindow, parent, newPanel,standby);
								});
							}

						});
					}
				}
			}
			,switchPanelWithMessage:function(parent,newPanel,message) {
				this.clearMessages();
				this.switchPanel(parent, newPanel);
				this.showNotificationMessage(message);
			}
			,showNotificationMessage:function(message) {
				var mainWindow = dojo.getObject("_poSystemMainView");
				//add the message to messagePanel
				var messagePanel = mainWindow.getMessagePanel();
				var messagePanelId = messagePanel.id;
				dojo.html.set(dojo.byId(messagePanelId), '');
				var divParams=new Array();
				divParams[0]=['class','notificationMessage'];
				var componentHelper = new brainyit.ui.util.ComponentHelper();				
				var divComp = componentHelper.buildComponent(dojo.byId(messagePanelId),'div',divParams);
				dojo.html.set(divComp, message)
			}
			,executeSwitch:function(mainWindow,parent,newPanel) {
				var d = new dojo.Deferred();
				try {
					console.debug('Executing switch on parent');
					console.debug('Adding new panel..:' + newPanel);
					parent.addChild(newPanel);
					console.debug('New panel added. Startup on parent');
					newPanel.startup();
					newPanel.buildRendering();
					parent.startup();
					parent.layout();
					console.debug('Parent and children initialized.');
					d.callback(true);
				} catch(e){
					console.error(e)
					d.errback(e);
				}
				return d;
			}
			,showErrorMessage:function(mainWindow,errMsg) {
				//Show an error message if there is an error
				console.error(errMsg);
				var divParams=new Array();
				divParams[0]=['class','systemMessages'];
				var componentHelper = new brainyit.ui.util.ComponentHelper();
				var messagePanel = mainWindow.getMessagePanel();
				var messagePanelId = messagePanel.id;
				messagePanel.destroyDescendants(false);
				dojo.html.set(dojo.byId(messagePanelId), '');
				var divComp = componentHelper.buildComponent(dojo.byId(messagePanelId),'div',divParams);
				dojo.html.set(divComp, 'Ocorreu um problema durante o processamento.Por favor, contate o administrador do sistema.Erro:' + errMsg);
				messagePanel.startup();
				messagePanel.buildRendering();
				messagePanel.layout();
			}
			,clearMessages:function(){
				var mainWindow = dojo.getObject("_poSystemMainView");
				//add the message to messagePanel
				var messagePanel = mainWindow.getMessagePanel();
				var messagePanelId = messagePanel.id;
				dojo.html.set(dojo.byId(messagePanelId), '');
			}
	    }
	);
}