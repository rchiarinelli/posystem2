if (!dojo._hasResource["brainyit.ui.customer.CustomerFilterResultsUI"]) {
	dojo._hasResource["brainyit.ui.customer.CustomerFilterResultsUI"] = true;
	dojo.provide("brainyit.ui.customer.CustomerFilterResultsUI");
    dojo.require("dojox.grid.DataGrid");
    dojo.require("dojo.data.ItemFileReadStore");
    dojo.require("dojox.data.JsonRestStore");
    dojo.require("dojox.rpc.Service");
    dojo.require("brainyit.ui.customer.ViewCustomerDataUI");
    dojo.require("brainyit.ui.customer.pf.edit.EditPFCustomerUI");
    dojo.require("brainyit.ui.customer.pf.delete.DeletePFCustomerUI");
    dojo.require("brainyit.ui.customer.pj.delete.DeletePJCustomerUI");
	dojo.declare(
	    "brainyit.ui.customer.CustomerFilterResultsUI",dijit.layout.BorderContainer,
	    {
	    	className : 'brainyit.ui.customer.CustomerFilterResultsUI'
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

				dojo.html.set(componentHelper.buildComponent(parentComp,'h4',new Array()), 'Resultado da busca de clientes');
				componentHelper.buildComponent(parentComp,'br',new Array());
				console.debug('Building grid layout');
				
				var p = componentHelper.buildComponent(parentComp,'p',new Array());
				
				var linkBackProps = new Array();
				linkBackProps.push(['href','#']);
				
				var linkBack = componentHelper.buildComponent(p,'a',linkBackProps);
				
				dojo.html.set(linkBack,'Voltar');
				
				//add action
				dojo.connect(linkBack, "onclick", function() {
					var parentComp = dojo.getObject('_poSystemMainView').contentPanel;
					var custFilterUI = new brainyit.ui.customer.CustomerFilterUI();
					custFilterUI.setIsNew(false);
					new brainyit.ui.switcher.WindowSwitcher().switchPanel(parentComp,custFilterUI);
                });				
				
				p = componentHelper.buildComponent(parentComp,'p',new Array());
				
				var formParams = new Array();
				formParams.push(['id','filterForm']);
				formParams.push(['action','#']);
				formParams.push(['method','post']);
				
				var filterForm = componentHelper.buildComponent(p,'form',formParams);
				
				var buttonParams = new Array();
				buttonParams.push(['id','editButton']);
				buttonParams.push(['type','button']);
				buttonParams.push(['value','Editar']);
				buttonParams.push(['class','formbutton']);
				buttonParams.push(['name','editButton']);
				var editButton = componentHelper.buildComponent(filterForm,'input',buttonParams);
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
				var deleteButton = componentHelper.buildComponent(filterForm,'input',buttonParams);
				dojo.connect(deleteButton, "onclick", function() {
					 var g = dijit.byId("filterCustomerGrid");
					 var selectedItem =  g.selection.getSelected()[0];
					 console.log(selectedItem);
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
                });
				
				buttonParams = new Array();
				buttonParams.push(['id','viewButton']);
				buttonParams.push(['type','button']);
				buttonParams.push(['value','Ver detalhes']);
				buttonParams.push(['class','formbutton']);
				buttonParams.push(['name','viewButton']);
				var viewButton = componentHelper.buildComponent(filterForm,'input',buttonParams);
				
        		//view customer event
				dojo.connect(viewButton, "onclick", function() {
					 var g = dijit.byId("filterCustomerGrid");
					 var selectedItem =  g.selection.getSelected()[0];
					 var parentComp = dojo.byId(this.id);
					 var viewCustUI = new brainyit.ui.customer.ViewCustomerDataUI();
					 var viewCustCont = viewCustUI.buildViewUI(parentComp,selectedItem);
	        		 if (dijit.byId('viewCustomerUI')!=null){
		        		dijit.byId('viewCustomerUI').destroy();
	        		 }					 
					 var dialog = new dijit.Dialog({
						title:'Dados do cliente'
						,style:'width: 400px; height: 350px; overflow: auto;'
						,closable:true
						,id:'viewCustomerUI'
					 });
					 dialog.set('content',viewCustCont);
					 dialog.show();					 
                });
				
				//datastore
				var basePath = dojo.getObject("basePath");
				
        		var jsonStore = new dojox.data.JsonRestStore({
        				target:basePath + '/services/1/customers/'
        		});
				
        		
        		var tempGrid = dijit.byId('filterCustomerGrid');
        		if (tempGrid!=null){
        			tempGrid.destroy();
        		}
                
				// set the layout structure:
                var layout = [{
                    field: 'name',
                    name: 'Nome',
                    width: 'auto'},
                {   field: 'custIdent',
                    name: 'CPF/CNPJ',
                    width: 'auto'}];
				
                var grid = new dojox.grid.DataGrid({
                    style:'height:150px;color: #000000;',
                    id: 'filterCustomerGrid', 
                    store: jsonStore, 
                    structure: layout,
                    errorMessage:'Ocorreu um erro durante a carga dos dados.',
                    loadingMessage:'Carregando dados...',
                    selectionMode:'single',
                    queryOptions: {cache: true}
                });
                
                this.addChild(grid);
	
				grid.onFetchError = function(err, req) { console.error('Error..: '+err + '. Req..: ' + req.toString()); }
        		grid.startup();
			}
	    }
	);
}

