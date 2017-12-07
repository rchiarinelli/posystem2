if (!dojo._hasResource["brainyit.ui.customer.CustomerFilterUI"]) {
	dojo._hasResource["brainyit.ui.customer.CustomerFilterUI"] = true;
	dojo.provide("brainyit.ui.customer.CustomerFilterUI");
	dojo.require("dijit.Dialog");
	dojo.require("brainyit.ui.customer.CustomerFilterResultsUI");
	dojo.require("brainyit.ui.util.FilterProperties"); 
	dojo.declare(
	    "brainyit.ui.customer.CustomerFilterUI",dijit.layout.BorderContainer,
	    {
	    	className : 'brainyit.ui.customer.CustomerFilterUI'
	    	,isNew : false
    		,constructor : function() {
				console.debug('Building ' + this.className);
				this.style="height:300px;";
			}
	    	,setIsNew:function(isNewFilter){
				this.isNew = isNewFilter;
	    	}
			,startup : function() {
				if (this._started) {
					return;
				}
				this.inherited(arguments);
				//Build add costumer screen
				this.inherited(arguments);
				console.debug('Building..:' + this.className);
				console.debug('Is new filter..:' + this.isNew);
				
				var basePath = dojo.getObject("basePath");
				
				var parentComp = dojo.byId(this.id);
				
				var componentHelper = new brainyit.ui.util.ComponentHelper(); 

				dojo.html.set(componentHelper.buildComponent(parentComp,'h4',new Array()), 'Busca de clientes');
				componentHelper.buildComponent(parentComp,'br',new Array())
				
				var formParams=new Array();
				formParams[0]=['action','#'];
				formParams[1]=['action','post'];
				var form = componentHelper.buildComponent(parentComp,'form',formParams);
				
				/* Name */
				var p = componentHelper.buildComponent(form,'p',new Array());
				
				var labelParam = new Array();
				labelParam[0]=['for','name'];
				
				var label = componentHelper.buildComponent(p,'label',labelParam);
				dojo.html.set(label,'Nome:');

				var txtParams = new Array();
				txtParams[0]=['id','name'];
				txtParams[1]=['name','name'];
				txtParams[2]=['type','text'];
				txtParams[3]=['size','20'];
				txtParams[4]=['maxlength','30'];
				
				var txtName = componentHelper.buildComponent(p,'input',txtParams);
				
				/* CPF/CNPJ */
				p = componentHelper.buildComponent(form,'p',new Array());
				
				labelParam = new Array();
				labelParam[0]=['for','cpf_cnpj'];
				
				label = componentHelper.buildComponent(p,'label',labelParam);
				dojo.html.set(label,'CPF/CNPJ:');

				txtParams = new Array();
				txtParams[0]=['id','cpf_cnpj'];
				txtParams[1]=['name','cpf_cnpj'];
				txtParams[2]=['type','text'];
				txtParams[3]=['size','20'];
				txtParams[4]=['maxlength','30'];
				
				var txtCpfCnpf = componentHelper.buildComponent(p,'input',txtParams);				
				
				/*search image button*/
				p = componentHelper.buildComponent(form,'p',new Array());				
				
				var buttonParams = new Array();
				buttonParams[0]=['id','search'];
				buttonParams[1]=['name','search'];
				buttonParams[2]=['type','button'];
				buttonParams[3]=['class','formbutton'];
				buttonParams[4]=['value','Buscar'];
				
				var searchButton = componentHelper.buildComponent(p,'input',buttonParams);
				
				var currentInstance = this;
				
				var filterProperties = dojo.getObject('customerFilterProperites');
				if (filterProperties!=null && this.isNew==false) {
					txtName.value = filterProperties.get('customerName');
					txtCpfCnpf.value = filterProperties.get('customerIdent');
				}
				dojo.connect(searchButton, "onclick", function() {
					var parentComp = dojo.getObject('_poSystemMainView').contentPanel;
					var filterResUI = new brainyit.ui.customer.CustomerFilterResultsUI();
					//build filter
					var filterProps = new brainyit.ui.util.FilterProperties();
					filterProps.add('customerName',dojo.byId('name').value);
					filterProps.add('customerIdent',dojo.byId('cpf_cnpj').value);
					
					dojo.setObject('customerFilterProperites',filterProps);
					
					new brainyit.ui.switcher.WindowSwitcher().switchPanel(parentComp,filterResUI);
                });
			}
	    }
	);
}

