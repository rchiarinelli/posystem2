if (!dojo._hasResource["brainyit.ui.customer.pf.add.AddPFCustomerUI"]) {
	dojo._hasResource["brainyit.ui.customer.pf.add.AddPFCustomerUI"] = true;
	dojo.provide("brainyit.ui.customer.pf.add.AddPFCustomerUI");
	dojo.require("dijit.layout.TabContainer");
	dojo.require("brainyit.ui.customer.pf.PFCustomerGeneralDataUI");
	dojo.require("brainyit.ui.customer.pf.PFCustomerAddressDataUI");
	dojo.require("brainyit.ui.customer.pf.PFCustomerContactDataUI");
	
	dojo.declare(
	    "brainyit.ui.customer.pf.add.AddPFCustomerUI",dijit.layout.BorderContainer,
	    {
	    	className : 'brainyit.ui.customer.pf.add.AddPFCustomerUI'
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

				var divParams=new Array();
				dojo.html.set(componentHelper.buildComponent(parentComp,'h4',new Array()), 'Cadastro de cliente pessoa f&iacute;sica');
				
				componentHelper.buildComponent(parentComp,'br',new Array());
				
				//Add Customer form
				var tc = new dijit.layout.TabContainer({
					style: "width: 100%;height:300px;overflow:auto;"
					,useMenu:false
					,useSlider:false
				});
				
                var generalData = new brainyit.ui.customer.pf.PFCustomerGeneralDataUI();
                tc.addChild(generalData);

                var addressData = new brainyit.ui.customer.pf.PFCustomerAddressDataUI();
                tc.addChild(addressData);
                
                var contactData = new brainyit.ui.customer.pf.PFCustomerContactDataUI();
                tc.addChild(contactData);
                                
                this.addChild(tc);

                generalData.startup();
                
                tc.selectChild(generalData,false);
                tc.startup();
                //Workaround to force the tabContaier start the child container
                tc.forward();
                tc.back();
                
                //add buttons
				var buttonParams = new Array();
				buttonParams[0]=['id','save'];
				buttonParams[1]=['name','save'];
				buttonParams[2]=['type','button'];
				buttonParams[3]=['class','formbutton'];
				buttonParams[4]=['value','Salvar'];
				
				var saveButton = componentHelper.buildComponent(parentComp,'input',buttonParams);
				
				dojo.connect(saveButton, "onclick", function() {
					var parentComp = dojo.getObject('_poSystemMainView').contentPanel;
					var newComp = new brainyit.ui.customer.CustomerHomeUI();
					var txtCustName =  dojo.byId('customerName');
					
					var msg = 'O dados do cliente ' + txtCustName.value + ' foram salvos com sucesso.';
					new brainyit.ui.switcher.WindowSwitcher().switchPanelWithMessage(parentComp,newComp,msg);
                });
				
				
				buttonParams = new Array();
				buttonParams[0]=['id','cancel'];
				buttonParams[1]=['name','cancel'];
				buttonParams[2]=['type','button'];
				buttonParams[3]=['class','formbutton'];
				buttonParams[4]=['value','Cancelar'];
				
				var cancelButton = componentHelper.buildComponent(parentComp,'input',buttonParams);
				
				dojo.connect(cancelButton, "onclick", function() {
					var parentComp = dojo.getObject('_poSystemMainView').contentPanel;
					var newComp = new brainyit.ui.customer.CustomerHomeUI();
					new brainyit.ui.switcher.WindowSwitcher().switchPanel(parentComp,newComp);
                });
			}
	    }
	);
}

