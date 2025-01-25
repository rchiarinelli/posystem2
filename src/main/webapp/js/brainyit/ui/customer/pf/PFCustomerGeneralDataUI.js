if (!dojo._hasResource["brainyit.ui.customer.pf.PFCustomerGeneralDataUI"]) {
	dojo._hasResource["brainyit.ui.customer.pf.PFCustomerGeneralDataUI"] = true;
	dojo.provide("brainyit.ui.customer.pf.PFCustomerGeneralDataUI");
	dojo.require("dijit.form.Textarea");
	dojo.declare(
	    "brainyit.ui.customer.pf.PFCustomerGeneralDataUI",dijit.layout.BorderContainer,
	    {
	    	className : 'brainyit.ui.customer.pf.add.PFCustomerGeneralDataUI'
			,constructor : function() {
				this.title='Dados gerais';
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
				Name
				*/
				var p = componentHelper.buildComponent(form,'p',new Array());
				var spanParam = new Array();
				spanParam[0]=['class','errorSign'];
				
				var span = componentHelper.buildComponent(p,'span',spanParam);
				dojo.html.set(span,'*');
				
				var labelParam = new Array();
				labelParam[0]=['for','name'];
				
				var label = componentHelper.buildComponent(p,'label',labelParam);
				dojo.html.set(label,'Nome:');

				var txtParams = new Array();
				txtParams[0]=['id','customerName'];
				txtParams[1]=['name','customerName'];
				txtParams[2]=['type','text'];
				txtParams[3]=['size','20'];
				txtParams[4]=['maxlength','30'];
				
				componentHelper.buildComponent(p,'input',txtParams);
				
				/*
				Cpf
				*/
				p = componentHelper.buildComponent(form,'p',new Array());
				spanParam = new Array();
				spanParam[0]=['class','errorSign'];
				
				span = componentHelper.buildComponent(p,'span',spanParam);
				dojo.html.set(span,'*');
				
				labelParam = new Array();
				labelParam[0]=['for','cpf'];
				
				label = componentHelper.buildComponent(p,'label',labelParam);
				dojo.html.set(label,'CPF:');

				txtParams = new Array();
				txtParams[0]=['id','cpf'];
				txtParams[1]=['name','cpf'];
				txtParams[2]=['type','text'];
				txtParams[3]=['size','14'];
				txtParams[4]=['maxlength','14'];
				
				componentHelper.buildComponent(p,'input',txtParams);			
				
				/*
				RG
				*/
				p = componentHelper.buildComponent(form,'p',new Array());
				spanParam = new Array();
				spanParam[0]=['class','errorSign'];
				
				span = componentHelper.buildComponent(p,'span',spanParam);
				dojo.html.set(span,'*');
				
				labelParam = new Array();
				labelParam[0]=['for','rg'];
				
				label = componentHelper.buildComponent(p,'label',labelParam);
				dojo.html.set(label,'RG:');

				txtParams = new Array();
				txtParams[0]=['id','rg'];
				txtParams[1]=['name','rg'];
				txtParams[2]=['type','text'];
				txtParams[3]=['size','14'];
				txtParams[4]=['maxlength','14'];
				
				componentHelper.buildComponent(p,'input',txtParams);				
				
				/*
				Details
				*/
				p = componentHelper.buildComponent(form,'p',new Array());
				spanParam = new Array();
				spanParam[0]=['class','errorSign'];
				
				labelParam = new Array();
				labelParam[0]=['for','details'];
				
				label = componentHelper.buildComponent(p,'details',labelParam);
				dojo.html.set(label,'Informa&ccedil;&otilde;es:');

				componentHelper.buildComponent(p,'br',new Array());
				
				txtParams = new Array();
				txtParams[0]=['id','details'];
				txtParams[1]=['name','details'];
				txtParams[2]=['cols','50'];
				txtParams[3]=['rows','10'];
				txtParams[4]=['style','height: 70px;'];
				txtParams[4]=['title','Dados gerais. Aceita mais de uma linha.'];
				
				componentHelper.buildComponent(p,'textarea',txtParams);	

			}
	    }
	);
}

