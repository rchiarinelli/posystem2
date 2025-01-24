if (!dojo._hasResource["brainyit.ui.pieces.TopMenu"]) {
	dojo._hasResource["brainyit.ui.pieces.TopMenu"] = true;
	dojo.require("brainyit.ui.POSystemUI");
	dojo.require("brainyit.ui.util.ComponentHelper");
	dojo.provide("brainyit.ui.pieces.TopMenu");
	dojo.require("brainyit.ui.customer.CustomerHomeUI");
	dojo.declare(
	    "brainyit.ui.pieces.TopMenu",brainyit.ui.POSystemUI,
	    {
	    	className : 'brainyit.ui.pieces.TopMenu'
			,constructor : function() {
				this.class='nav';
				this.baseClass='nav';
			}
			,startup : function() {
				if (this._started) {
					return;
				}

				this.inherited(arguments);
				console.debug('Building..:' + this.className);
				
				var divParams=new Array();
				
				var parentComp = dojo.byId(this.id);
				
				var componentHelper = new brainyit.ui.util.ComponentHelper(); 
				
				var ul = componentHelper.buildComponent(parentComp,'ul',null);

				var linkParams = new Array();
				linkParams[0]=['href','#'];

				var li = componentHelper.buildComponent(ul,'li',null);
				var linkHome = componentHelper.buildComponent(li,'a',linkParams);
				dojo.html.set(linkHome, 'In&iacute;cio');
				
				li = componentHelper.buildComponent(ul,'li',null);
				var linkCustomer = componentHelper.buildComponent(li,'a',linkParams);
				dojo.html.set(linkCustomer, 'Clientes');
                
				li = componentHelper.buildComponent(ul,'li',null);
				var linkOS  = componentHelper.buildComponent(li,'a',linkParams);
				dojo.html.set(linkOS, 'OS');
				
				li = componentHelper.buildComponent(ul,'li',null);
				var linkOrder = componentHelper.buildComponent(li,'a',linkParams); 
				dojo.html.set(linkOrder, 'Pedidos');
				
				li = componentHelper.buildComponent(ul,'li',null);
				var linkReport = componentHelper.buildComponent(li,'a',linkParams); 
				dojo.html.set(linkReport, 'Relat&oacute;rios');
				
				li = componentHelper.buildComponent(ul,'li',null);
				var linkExit = componentHelper.buildComponent(li,'a',linkParams); 
				dojo.html.set(linkExit, 'Sair');				

				//Bind actions
                var mainContent = dojo.byId('mainContent');
                var standby = new dojox.widget.Standby({
                    target: mainContent.id
                });
                dojo.place(standby.domNode,mainContent);
                
				dojo.connect(linkHome, "onclick", function() {
					var parentComp = dojo.getObject('_poSystemMainView').contentPanel;
					var newComp = new brainyit.ui.dashboard.DashboardUI();
					new brainyit.ui.switcher.WindowSwitcher().switchPanel(parentComp,newComp);
                });
                
                
				dojo.connect(linkCustomer, "onclick", function() {
					var parentComp = dojo.getObject('_poSystemMainView').contentPanel;
					var newComp = new brainyit.ui.customer.CustomerHomeUI();
					new brainyit.ui.switcher.WindowSwitcher().switchPanel(parentComp,newComp);
                });
				
				dojo.connect(linkOS, "onclick", function() {
                    standby.show();
					setTimeout(function(){
						standby.hide();	
					}, 3000);                    
                });

				
				dojo.connect(linkOrder, "onclick", function() {
                    standby.show();
					setTimeout(function(){
						standby.hide();	
					}, 3000);                    
                });

				dojo.connect(linkReport, "onclick", function() {
                    standby.show();
					setTimeout(function(){
						standby.hide();	
					}, 3000);                    
                });


			}
	    }
	);
}

