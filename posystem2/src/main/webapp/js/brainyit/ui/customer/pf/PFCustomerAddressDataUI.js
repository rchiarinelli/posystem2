if (!dojo._hasResource["brainyit.ui.customer.pf.PFCustomerAddressDataUI"]) {
	dojo._hasResource["brainyit.ui.customer.pf.PFCustomerAddressDataUI"] = true;
	dojo.provide("brainyit.ui.customer.pf.PFCustomerAddressDataUI");
	dojo.declare(
	    "brainyit.ui.customer.pf.PFCustomerAddressDataUI",dijit.layout.BorderContainer,
	    {
	    	className : 'brainyit.ui.customer.pf.PFCustomerAddressDataUI'
			,constructor : function() {
				this.title='Endere&ccedil;o';
				this.style='overflow:auto;';
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

				componentHelper.buildComponent(parentComp,'br',new Array());
				
				var formParams=new Array();
				formParams[0]=['action','#'];
				formParams[1]=['action','post'];
				var form = componentHelper.buildComponent(parentComp,'form',formParams);
				
				/*
				Street
				*/
				var p = componentHelper.buildComponent(form,'p',new Array());
				
				var labelParam = new Array();
				labelParam[0]=['for','street'];
				
				var label = componentHelper.buildComponent(p,'label',labelParam);
				dojo.html.set(label,'Rua:');

				var txtParams = new Array();
				txtParams[0]=['id','street'];
				txtParams[1]=['name','street'];
				txtParams[2]=['type','text'];
				txtParams[3]=['size','20'];
				txtParams[4]=['maxlength','60'];
				
				componentHelper.buildComponent(p,'input',txtParams);
				
				
				/*
				Number
				*/
				p = componentHelper.buildComponent(form,'p',new Array());
				
				labelParam = new Array();
				labelParam[0]=['for','number'];
				
				label = componentHelper.buildComponent(p,'label',labelParam);
				dojo.html.set(label,'N&uacute;mero:');

				txtParams = new Array();
				txtParams[0]=['id','number'];
				txtParams[1]=['name','number'];
				txtParams[2]=['type','text'];
				txtParams[3]=['size','5'];
				txtParams[4]=['maxlength','5'];
				
				componentHelper.buildComponent(p,'input',txtParams);
				
				/*
				Additional info
				*/
				p = componentHelper.buildComponent(form,'p',new Array());
				
				labelParam = new Array();
				labelParam[0]=['for','addInfo'];
				
				label = componentHelper.buildComponent(p,'label',labelParam);
				dojo.html.set(label,'Complemento:');

				txtParams = new Array();
				txtParams[0]=['id','addInfo'];
				txtParams[1]=['name','addInfo'];
				txtParams[2]=['type','text'];
				txtParams[3]=['size','30'];
				txtParams[4]=['maxlength','45'];
				
				componentHelper.buildComponent(p,'input',txtParams);				
				
				/*
				City
				*/
				p = componentHelper.buildComponent(form,'p',new Array());
				
				labelParam = new Array();
				labelParam[0]=['for','city'];
				
				label = componentHelper.buildComponent(p,'label',labelParam);
				dojo.html.set(label,'Cidade:');

				txtParams = new Array();
				txtParams[0]=['id','city'];
				txtParams[1]=['name','city'];
				txtParams[2]=['type','text'];
				txtParams[3]=['size','30'];
				txtParams[4]=['maxlength','45'];
				
				componentHelper.buildComponent(p,'input',txtParams);
				
				/*
				CEP
				*/
				p = componentHelper.buildComponent(form,'p',new Array());
				
				labelParam = new Array();
				labelParam[0]=['for','cep'];
				
				label = componentHelper.buildComponent(p,'label',labelParam);
				dojo.html.set(label,'CEP:');

				txtParams = new Array();
				txtParams[0]=['id','cep'];
				txtParams[1]=['name','cep'];
				txtParams[2]=['type','text'];
				txtParams[3]=['size','20'];
				txtParams[4]=['maxlength','15'];
				
				componentHelper.buildComponent(p,'input',txtParams);				
				
				/*
				State
				*/
				p = componentHelper.buildComponent(form,'p',new Array());
				
				labelParam = new Array();
				labelParam[0]=['for','state'];
				
				label = componentHelper.buildComponent(p,'label',labelParam);
				dojo.html.set(label,'Estado:');

				var cbParams=new Array();
				cbParams[0]=['id','state'];
				cbParams[1]=['name','state'];
				var daysGapCombo = componentHelper.buildComponent(p,'select',cbParams);
				dojo.html.set(componentHelper.buildComponent(daysGapCombo,'option',[['value','SP']]), 'SP');
			}
	    }
	);
}

