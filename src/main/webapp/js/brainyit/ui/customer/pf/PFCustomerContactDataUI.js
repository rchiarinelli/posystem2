if (!dojo._hasResource["brainyit.ui.customer.pf.PFCustomerContactDataUI"]) {
	dojo._hasResource["brainyit.ui.customer.pf.PFCustomerContactDataUI"] = true;
	dojo.provide("brainyit.ui.customer.pf.PFCustomerContactDataUI");
	dojo.require("dijit.form.Textarea");
	dojo.declare(
	    "brainyit.ui.customer.pf.PFCustomerContactDataUI",dijit.layout.BorderContainer,
	    {
	    	className : 'brainyit.ui.customer.pf.add.PFCustomerContactDataUI'
			,constructor : function() {
				this.title='Dados de contato';
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

				componentHelper.buildComponent(parentComp,'br',formParams)

				var formParams=new Array();
				formParams[0]=['action','#'];
				formParams[1]=['action','post'];
				var form = componentHelper.buildComponent(parentComp,'form',formParams);
				
				/*
				Email
				*/
				var p = componentHelper.buildComponent(form,'p',new Array());
				
				var labelParam = new Array();
				labelParam[0]=['for','email'];
				
				var label = componentHelper.buildComponent(p,'label',labelParam);
				dojo.html.set(label,'Email:');

				var txtParams = new Array();
				txtParams[0]=['id','email'];
				txtParams[1]=['name','email'];
				txtParams[2]=['type','text'];
				txtParams[3]=['size','20'];
				txtParams[4]=['maxlength','45'];
				
				componentHelper.buildComponent(p,'input',txtParams);
				
				/*
				Office number
				*/
				p = componentHelper.buildComponent(form,'p',new Array());
				
				labelParam = new Array();
				labelParam[0]=['for','officePhoneNumber'];
				
				label = componentHelper.buildComponent(p,'label',labelParam);
				dojo.html.set(label,'Telefone:');

				txtParams = new Array();
				txtParams[0]=['id','officePhoneNumber'];
				txtParams[1]=['name','officePhoneNumber'];
				txtParams[2]=['type','text'];
				txtParams[3]=['size','14'];
				txtParams[4]=['maxlength','20'];
				
				componentHelper.buildComponent(p,'input',txtParams);			
				
				/*
				Mobile number
				*/
				p = componentHelper.buildComponent(form,'p',new Array());
				
				labelParam = new Array();
				labelParam[0]=['for','mobilePhoneNumber'];
				
				label = componentHelper.buildComponent(p,'label',labelParam);
				dojo.html.set(label,'Celular:');

				txtParams = new Array();
				txtParams[0]=['id','mobilePhoneNumber'];
				txtParams[1]=['name','mobilePhoneNumber'];
				txtParams[2]=['type','text'];
				txtParams[3]=['size','14'];
				txtParams[4]=['maxlength','20'];
				
				componentHelper.buildComponent(p,'input',txtParams);				
				

			}
	    }
	);
}

