/**
 * 
 */
if (!dojo._hasResource["brainyit.ui.dashboard.PendingInstallmentsUI"]) {
	dojo._hasResource["brainyit.ui.dashboard.PendingInstallmentsUI"] = true;
	dojo.provide("brainyit.ui.dashboard.PendingInstallmentsUI");
	dojo.require("dojox.widget.Standby");
	dojo.declare(
	    "brainyit.ui.dashboard.PendingInstallmentsUI",brainyit.ui.POSystemUI,
	    {
	    	className : 'brainyit.ui.dashboard.PendingInstallmentsUI'
			,constructor : function() {
				console.debug('Building ' + this.className);
				this.class='sidebar';
			}
			,startup : function() {
				if (this._started) {
					return;
				}
				this.inherited(arguments);
				//Build the two main divs
	    		console.debug('Building..:' + this.className);
	    		var componentHelper = new brainyit.ui.util.ComponentHelper();
				var parentComp = dojo.byId(this.id);
				var divParams=new Array();
				dojo.html.set(componentHelper.buildComponent(parentComp,'h4',new Array()), 'Pagamentos a receber');
				//installmentForm
				divParams=new Array();
				divParams[0]=['id','installmentsForm'];
				divParams[1]=['action','#'];
				var installmentForm = componentHelper.buildComponent(parentComp,'form',divParams);
				var paragraph = componentHelper.buildComponent(installmentForm,'p',new Array());
				divParams=new Array();
				divParams[0]=['id','daysGapCb'];
				divParams[1]=['name','daysGapCb'];
				var daysGapCombo = componentHelper.buildComponent(paragraph,'select',divParams);
				dojo.html.set(componentHelper.buildComponent(daysGapCombo,'option',[['value','10']]), '10 dias');
				dojo.html.set(componentHelper.buildComponent(daysGapCombo,'option',[['value','20']]), '20 dias');
				dojo.html.set(componentHelper.buildComponent(daysGapCombo,'option',[['value','30']]), '30 dias');
				dojo.html.set(componentHelper.buildComponent(daysGapCombo,'option',[['value','40']]), '40 dias');
                var standby = new dojox.widget.Standby({
                    target: this.id
                });
                dojo.place(standby.domNode,parentComp);
                standby.startup();
                dojo.connect(daysGapCombo, "onchange", function() {
                    standby.show();
					var days = dojo.byId("daysGapCb").value;
					var url = '/posystem2/services/1/dashboard/installments/'+days;
					//invoke service
					dojo.xhrGet({
			            url: url
			            ,headers: {
			                "Content-Type": "text/plain"
			            }
			            ,handleAs: "json"
			            ,preventCache:true
			            ,load: function(response, ioArgs){
			            	var installments = response.pendingInstallments.installments;
			            	dojo.html.set(dojo.byId("installments"), '', {parseContent: true});
		                	var ul = dojo.create("ul", null, "installments");
		                   	var items = new Array();
		                   	var item = null;
		                	if (installments.length==null) {
		            			item = installments.date + "&nbsp;-&nbsp;" + installments.value;
		            			items.push(item);
		                	} else {
		                		for (var i = 0; i < installments.length; i++) {
		                			item = installments[i].date.$ + "&nbsp;-&nbsp;" + installments[i].value.$;
		                			items.push(item);
		                		}
		                	}
		                   	dojo.forEach(items, function(data){
		                     	  dojo.create("li", { innerHTML: data }, ul);
							});
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
				
				//Installments div
				divParams=new Array();
				divParams[0]=['id','installments'];
				divParams[1]=['style','overflow: auto;height: 250px;'];
				var installmentDiv = componentHelper.buildComponent(parentComp,'div',divParams);
				//Load installements
				var days = dojo.byId("daysGapCb").value;
				var url = '/posystem2/services/1/dashboard/installments/'+days;				
				dojo.xhrGet({
		            url: url
		            ,headers: {
		                "Content-Type": "text/plain"
		            }
		            ,handleAs: "json"
		            ,preventCache:true
		            ,load: function(response, ioArgs){
		            	var installments = response.pendingInstallments.installments;
		            	dojo.html.set(dojo.byId("installments"), '', {parseContent: true});
	                	var ul = dojo.create("ul", null, "installments");
	                   	var items = new Array();
	                   	var item = null;
	                	if (installments.length==null) {
	            			item = installments.date + "&nbsp;-&nbsp;" + installments.value;
	            			items.push(item);
	                	} else {
	                		for (var i = 0; i < installments.length; i++) {
	                			item = installments[i].date.$ + "&nbsp;-&nbsp;" + installments[i].value.$;
	                			items.push(item);
	                		}
	                	}
	                   	dojo.forEach(items, function(data){
	                     	  dojo.create("li", { innerHTML: data }, ul);
						});
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
				
			}
	    }
	);
}