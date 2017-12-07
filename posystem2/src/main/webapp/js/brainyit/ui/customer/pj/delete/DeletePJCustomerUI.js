/**
 * 
 */
if (!dojo._hasResource["brainyit.ui.customer.pj.delete.DeletePJCustomerUI"]) {
	dojo._hasResource["brainyit.ui.customer.pj.delete.DeletePJCustomerUI"] = true;
	dojo.provide("brainyit.ui.customer.pj.delete.DeletePJCustomerUI");
	dojo.declare(
	    "brainyit.ui.customer.pj.delete.DeletePJCustomerUI",null,
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
				 
				 //message
				 var p = componentHelper.buildComponent(form,'p',new Array());
				 
				 dojo.html.set(p, 'Você deseja realmente apagar os dados do cliente ' + selectedItem.name + ' ?');
				 
				 //name
				 p = componentHelper.buildComponent(form,'p',new Array());
				 
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
				 buttonParams.push(['id','confirm']);
				 buttonParams.push(['type','button']);
				 buttonParams.push(['value','Sim']);
				 buttonParams.push(['class','formbutton']);
				 buttonParams.push(['name','confirm']);
				 var confirmButton = componentHelper.buildComponent(p,'input',buttonParams);
				 dojo.connect(confirmButton,'onclick',function(){
					 //Invoke remove
					 dijit.byId('deleteConfirmDialog').hide();
					 //Put the message on the message panel
					 var msg = 'O dados do cliente ' + selectedItem.name[0] + ' foram removidos com sucesso';
					 new brainyit.ui.switcher.WindowSwitcher().showNotificationMessage(msg);
				 });				 
				 
				 buttonParams = new Array();
				 buttonParams.push(['id','cancel']);
				 buttonParams.push(['type','button']);
				 buttonParams.push(['value','Não']);
				 buttonParams.push(['class','formbutton']);
				 buttonParams.push(['name','cancel']);
				 var cancelButton = componentHelper.buildComponent(p,'input',buttonParams);
				 dojo.connect(cancelButton,'onclick',function(){
					 dijit.byId('deleteConfirmDialog').hide();
				 });

				 return div;
			}
	    }
	);
}