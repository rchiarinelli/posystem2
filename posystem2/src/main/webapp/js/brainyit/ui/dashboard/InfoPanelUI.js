/**
 * 
 */
if (!dojo._hasResource["brainyit.ui.dashboard.InfoPanelUI"]) {
	dojo._hasResource["brainyit.ui.dashboard.InfoPanelUI"] = true;
	dojo.provide("brainyit.ui.dashboard.InfoPanelUI");
	dojo.require("dijit.form.DateTextBox");
	dojo.require("dojox.widget.Standby");
	dojo.require("dijit.Dialog");
	dojo.declare(
	    "brainyit.ui.dashboard.InfoPanelUI",brainyit.ui.POSystemUI,
	    {
	    	className : 'brainyit.ui.dashboard.InfoPanelUI'
			,constructor : function() {
				console.debug('Building ' + this.className);
				this.class='content';
			}
			,startup : function() {
				if (this._started) {
					return;
				}
				this.inherited(arguments);
				//Build the two main divs
	    		console.debug('Building..:' + this.className);
				var parentComp = dojo.byId(this.id);
				var componentHelper = new brainyit.ui.util.ComponentHelper();
				
				var divParams=new Array();
				//header
				dojo.html.set(componentHelper.buildComponent(parentComp,'h3',new Array()),'Painel de informa&ccedil;&otilde;es');
				
				//form
				divParams=new Array();
				divParams[0]=['id','internalForm']; 				
				divParams[1]=['method','post'];
				divParams[2]=['action','#'];
				var intervalForm = componentHelper.buildComponent(parentComp,'form',divParams);
				var paragraph = componentHelper.buildComponent(intervalForm,'p',new Array());
				dojo.html.set(componentHelper.buildComponent(paragraph,'label',[["for","fromDate"]]),'De:');
				var fromDate = new dijit.form.DateTextBox({
					id:'fromDate'
				    ,style:'width: 120px;'
				    ,constraints:{datePattern:'dd/MM/yyyy'}
				    ,value:new Date()
				});
				
				fromDate.placeAt(paragraph);
				//this.addChild(fromDate);
				//dojo.html.set(paragraph,'&nbsp;');
				dojo.html.set(componentHelper.buildComponent(paragraph,'label',[["for","toDate"]]),'At&eacute;:');				

				var toDate = new dijit.form.DateTextBox({
					id:'toDate'
				    ,style:'width: 120px;'
					,constraints:{datePattern:'dd/MM/yyyy'}
					,value:new Date()				    	
				});

				toDate.placeAt(paragraph);
				divParams=new Array();
				divParams[0]=['id','refresh']; 				
				divParams[1]=['type','button'];
				divParams[2]=['name','refresh'];
				divParams[3]=['value','Atualizar'];
				divParams[4]=['type','button'];
				divParams[4]=['class','formbutton'];
				
				var refreshButton = componentHelper.buildComponent(paragraph,'input',divParams);
                var standby = new dojox.widget.Standby({
                    target: this.id
                });
                dojo.place(standby.domNode,parentComp);
                standby.startup();
                dojo.connect(refreshButton, "onclick", function() {
                    standby.show();
					console.debug('Refresh button clicked');
					//Get the start and end date
					var fromDate = dojo.byId("fromDate").value;
					var toDate = dojo.byId("toDate").value;
					var fromDateStr = fromDate.replace("/","-").replace("/","-");
					var toDateStr = toDate.replace("/","-").replace("/","-");
					
					var url = '/posystem2/services/1/dashboard/po/'+fromDateStr+"&"+toDateStr;
					//invoke service
					dojo.xhrGet({
			            url: url
			            ,headers: {
			                "Content-Type": "text/plain"
			            }
			            ,handleAs: "json"
			            ,preventCache:true
			            ,load: function(response, ioArgs){
			            	console.info(response.orderValues);
			            	var openPO = response.orderValues.open.value + '';
			            	var execPO =  response.orderValues.executing.value + '';
			            	var billedPO = response.orderValues.billed.value + '';
			            	var closedPO = response.orderValues.closed.value + '';
			                dojo.html.set(dojo.byId("openPO"), openPO, {parseContent: true	});
			                dojo.html.set(dojo.byId("execPO"), execPO,{parseContent: true	});
			                dojo.html.set(dojo.byId("billedPO"), billedPO,{parseContent: true	});
			                dojo.html.set(dojo.byId("closedPO"), closedPO,{parseContent: true	});
			            	standby.hide();	
			            }
			            , error: function(response,ioArgs) {
			            	console.error(response);
			            	standby.hide();
			            	var dialog = new dijit.Dialog(
			            			{
			            				title:'Erro'
			            				,style:'width:400px'
			            				,closable:true
			            				,content:'Erro ao se comunicar com o servidor. Contate o admnistrador do sistema'
			            			});
			            	dialog.show();
			            }
			        });					
                });
				
				//table orders
				var tableOrders = componentHelper.buildComponent(parentComp,'table',new Array());
				var trOrderHeader = componentHelper.buildComponent(tableOrders,'tr',new Array());
				componentHelper.buildComponent(trOrderHeader,'td','');
				dojo.html.set(componentHelper.buildComponent(trOrderHeader,'td',''),'Pedidos');
				
				var trOpenOrder = componentHelper.buildComponent(tableOrders,'tr',new Array());
				dojo.html.set(componentHelper.buildComponent(trOpenOrder,'td',''),'Abertos');
				componentHelper.buildComponent(trOpenOrder,'td',[['id','openPO']]);
				
				var trExecOrder = componentHelper.buildComponent(tableOrders,'tr',new Array());
				dojo.html.set(componentHelper.buildComponent(trExecOrder,'td',''),'Em execu&ccedil;&atilde;o:');
				componentHelper.buildComponent(trExecOrder,'td',[['id','execPO']]);

				var trFinOrder = componentHelper.buildComponent(tableOrders,'tr',new Array());
				dojo.html.set(componentHelper.buildComponent(trFinOrder,'td',''),'Finalizados:');
				componentHelper.buildComponent(trFinOrder,'td',[['id','closedPO']]);
				
				var trBillOrder = componentHelper.buildComponent(tableOrders,'tr',new Array());
				dojo.html.set(componentHelper.buildComponent(trBillOrder,'td',''),'Faturados:');
				componentHelper.buildComponent(trBillOrder,'td',[['id','billedPO']]);
				
				//table POs
				var tablePOs = componentHelper.buildComponent(parentComp,'table',new Array());
				var trPOsHeader = componentHelper.buildComponent(tablePOs,'tr',new Array());
				componentHelper.buildComponent(trPOsHeader,'td','');
				dojo.html.set(componentHelper.buildComponent(trPOsHeader,'td',''),'Ordens de servi&ccedil;o');
				
				var trPOsPlan = componentHelper.buildComponent(tablePOs,'tr',new Array());
				dojo.html.set(componentHelper.buildComponent(trPOsPlan,'td',''),'Em planejamento:');
				componentHelper.buildComponent(trPOsPlan,'td',[['id','planningOS']]);

				var trPOsProd = componentHelper.buildComponent(tablePOs,'tr',new Array());
				dojo.html.set(componentHelper.buildComponent(trPOsProd,'td',''),'Em produ&ccedil;&atilde;o:');
				componentHelper.buildComponent(trPOsProd,'td',[['id','inProdOS']]);

				var trPOsDone = componentHelper.buildComponent(tablePOs,'tr',new Array());
				dojo.html.set(componentHelper.buildComponent(trPOsDone,'td',''),'Conclu&iacute;das:');
				componentHelper.buildComponent(trPOsDone,'td',[['id','concOS']]);
			}
	    }
	);
}