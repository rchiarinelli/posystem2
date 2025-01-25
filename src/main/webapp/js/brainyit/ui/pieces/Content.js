if (!dojo._hasResource["brainyit.ui.pieces.Content"]) {
	dojo._hasResource["brainyit.ui.pieces.Content"] = true;
	dojo.require("brainyit.ui.POSystemUI");
	dojo.require("brainyit.ui.util.ComponentHelper");
	dojo.require("brainyit.ui.switcher.WindowSwitcher");
	dojo.require("brainyit.ui.dashboard.DashboardUI");
	dojo.provide("brainyit.ui.pieces.Content");
	dojo.declare(
	    "brainyit.ui.pieces.Content",brainyit.ui.POSystemUI,
	    {
	    	className : 'brainyit.ui.pieces.Content'
			,constructor : function() {
				this.layoutPriority=3;
				this.sppliter=false;
				this.gutters=false;
				this.style="width: 100%; height: 100%;";
			}
			,startup : function() {
				if (this._started) {
					return;
				}
				this.inherited(arguments);
				this.buildUI();
			}
			, buildUI:function() {
	    		console.debug('Building..:' + this.className);
				var dashboard = new brainyit.ui.dashboard.DashboardUI();
				this.addChild(dashboard);
				dashboard.startup();
				/*
				var parent = dojo.byId(this.id);
	    		console.debug('Building..:' + this.className);
				var parentDiv = dojo.create("div",null,parent);
				dojo.attr(parentDiv,"height","100px");
				dojo.attr(parentDiv,"width","200px");
				
				var form = dojo.create("form",null,parentDiv);
				dojo.attr(form,"action","#");
				dojo.attr(form,"method","post");

				var pTag = dojo.create("p",null,form);

				var span  = dojo.create("span",{innerHTML: "*"},pTag);
				dojo.attr(span,"class","errorSign");
				
				var label = dojo.create("label",{innerHTML: "Nome:"},pTag);
				dojo.attr(label,"for","nameTxt");
				
				var nameTxt = dojo.create("input",null,pTag);
				dojo.attr(nameTxt,"id","nameTxt");
				dojo.attr(nameTxt,"name","nameTxt");
				dojo.attr(nameTxt,"type","text");

				//Botao
				pTag = dojo.create("p",null,form);

				var saveBt = dojo.create("input", null, pTag);
				dojo.attr(saveBt,"type","button");
				dojo.attr(saveBt,"id","save");
				dojo.attr(saveBt,"name","save");
				dojo.attr(saveBt,"class","formbutton");
				dojo.attr(saveBt,"value","Salvar");
				
				
				dojo.connect(saveBt, "onclick", function(evt){
					console.debug('Connecting event to save button');
					var parentComp = dojo.getObject('_poSystemMainView').contentPanel;
					var newComp = new brainyit.ui.customer.add.AddPFCustomerUI();
					new brainyit.ui.switcher.WindowSwitcher().switchPanel(parentComp,newComp);
				});
				*/
			}
	    }
	);
}

