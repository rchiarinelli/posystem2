if (!dojo._hasResource["brainyit.ui.customer.CustomerHomeUI"]) {
	dojo._hasResource["brainyit.ui.customer.CustomerHomeUI"] = true;
	dojo.provide("brainyit.ui.customer.CustomerHomeUI");
	dojo.require("brainyit.ui.customer.pf.add.AddPFCustomerUI");
	dojo.require("brainyit.ui.customer.CustomerFilterUI");
	dojo.declare(
	    "brainyit.ui.customer.CustomerHomeUI",dijit.layout.BorderContainer,
	    {
	    	className : 'brainyit.ui.customer.CustomerHomeUI'
			,constructor : function() {
				console.debug('Building ' + this.className);
			}
			,startup : function() {
				if (this._started) {
					return;
				}
				this.inherited(arguments);
				//Build add costumer screen
				this.inherited(arguments);
				console.debug('Building..:' + this.className);
				
				var parentComp = dojo.byId(this.id);
				
				var componentHelper = new brainyit.ui.util.ComponentHelper(); 


				dojo.html.set(componentHelper.buildComponent(parentComp,'h4',new Array()), 'Aqui, voc&ecirc; pode:');
				componentHelper.buildComponent(parentComp,'br',new Array())
				//Home links
				var linkParams = new Array();
				linkParams[0]=['href','#'];				
				var ul = componentHelper.buildComponent(parentComp,'ul',new Array());
				var li = componentHelper.buildComponent(ul,'li',new Array());
				var linkSearch = componentHelper.buildComponent(li,'a',linkParams);
				dojo.html.set(linkSearch,'Procurar e modificar dados dos clientes');
				

				li = componentHelper.buildComponent(ul,'li',new Array());
				var linkAddPf = componentHelper.buildComponent(li,'a',linkParams);
				dojo.html.set(linkAddPf,'Adicionar um novo cliente pessoa f&iacute;sica');

				
				li = componentHelper.buildComponent(ul,'li',new Array());
				var linkAddPj = componentHelper.buildComponent(li,'a',linkParams);
				dojo.html.set(linkAddPj,'Adicionar um novo cliente pessoa jur&iacute;dica');
				
				//Bind actions
				dojo.connect(linkAddPf, "onclick", function() {
					var parentComp = dojo.getObject('_poSystemMainView').contentPanel;
					var newComp = new brainyit.ui.customer.pf.add.AddPFCustomerUI();
					new brainyit.ui.switcher.WindowSwitcher().switchPanel(parentComp,newComp);
                });
				dojo.connect(linkSearch, "onclick", function() {
					var parentComp = dojo.getObject('_poSystemMainView').contentPanel;
					var newComp = new brainyit.ui.customer.CustomerFilterUI();
					newComp.setIsNew(true);
					new brainyit.ui.switcher.WindowSwitcher().switchPanel(parentComp,newComp);
                });

				
			}
	    }
	);
}

