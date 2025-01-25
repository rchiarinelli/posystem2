/**
 * 
 */
if (!dojo._hasResource["brainyit.ui.customer.ViewCustomerDataUI"]) {
	dojo._hasResource["brainyit.ui.customer.ViewCustomerDataUI"] = true;
	dojo.provide("brainyit.ui.customer.ViewCustomerDataUI");
	dojo.require("brainyit.ui.customer.pf.edit.EditPFCustomerUI"); 
	dojo.declare(
	    "brainyit.ui.customer.ViewCustomerDataUI",null,
	    {
			constructor : function() {
			}
			,buildViewUI:function(parentComp,selectedItem) {
				 var componentHelper = new brainyit.ui.util.ComponentHelper();
				 var div = componentHelper.buildComponent(parentComp,'div',new Array());
				 
				 var formParams = new Array();
				 formParams.push(['id','viewCustomerForm']);
				 formParams.push(['method','post']);
				 formParams.push(['action','#']);
				 
				 var form = componentHelper.buildComponent(div,'form',formParams);
				 
				 //name
				 var p = componentHelper.buildComponent(form,'p',new Array());
				 
				 dojo.html.set(p, 'Nome: ' + selectedItem.name);
				 //type
				 p = componentHelper.buildComponent(form,'p',new Array());
				 dojo.html.set(p, 'Tipo: ' + selectedItem.typeName);
				 //CPF/CNPJ
				 p = componentHelper.buildComponent(form,'p',new Array());
				 dojo.html.set(p, 'CPF/CPNJ: ' + selectedItem.custIdent);
				 //Buttons
				 p = componentHelper.buildComponent(form,'p',new Array());
				 var buttonParams = new Array();
				 buttonParams.push(['id','editButton']);
				 buttonParams.push(['type','button']);
				 buttonParams.push(['value','Editar']);
				 buttonParams.push(['class','formbutton']);
				 buttonParams.push(['name','editButton']);
				 var editButton = componentHelper.buildComponent(p,'input',buttonParams);
				 dojo.connect(editButton, "onclick", function() {
					 var g = dijit.byId("filterCustomerGrid");
					 var selectedItem =  g.selection.getSelected()[0];
					 var parentComp = dojo.byId(this.id);
					 if (dijit.byId('viewCustomerUI')!=null){
			        	 dijit.byId('viewCustomerUI').hide();
		        	 }
					 var parentComp = dojo.getObject('_poSystemMainView').contentPanel;
					 var newComp = new brainyit.ui.customer.pf.edit.EditPFCustomerUI();
					 new brainyit.ui.switcher.WindowSwitcher().switchPanel(parentComp,newComp);					 
                 });
				 
				 buttonParams = new Array();
				 buttonParams.push(['id','deleteButton']);
				 buttonParams.push(['type','button']);
				 buttonParams.push(['value','Apagar']);
				 buttonParams.push(['class','formbutton']);
				 buttonParams.push(['name','deleteButton']);
				 var deleteButton = componentHelper.buildComponent(p,'input',buttonParams);
				 dojo.connect(deleteButton, "onclick", function() {
					 var g = dijit.byId("filterCustomerGrid");
					 var selectedItem =  g.selection.getSelected()[0];
					 var parentComp = dojo.byId(this.id);
					 var viewCustUI = null;
					 var viewCustCont = null;
					 if (selectedItem.type.toLowerCase()=='pf') {
						 viewCustUI = new brainyit.ui.customer.pf.delete.DeletePFCustomerUI();
						 viewCustCont = viewCustUI.buildViewUI(parentComp,selectedItem);	 
					 }
					 if (selectedItem.type.toLowerCase()=='pj') {
						 viewCustUI = new brainyit.ui.customer.pj.delete.DeletePJCustomerUI();
						 viewCustCont = viewCustUI.buildViewUI(parentComp,selectedItem);	 
					 }
	        		 if (dijit.byId('deleteConfirmDialog')!=null){
	        			dijit.byId('deleteConfirmDialog').destroy();
	        		 }					 
					 var dialog = new dijit.Dialog({
						title:'Dados do cliente'
						,style:'width: 600px; height: 350px; overflow: auto;'
						,closable:true
						,id:'deleteConfirmDialog'
					 });
					 dialog.set('content',viewCustCont);
					 dialog.show();
					 
					 if (dijit.byId('viewCustomerUI')!=null){
			        	 dijit.byId('viewCustomerUI').hide();
		        	 }		
                 });				 

				 return div;
			}
	    }
	);
}