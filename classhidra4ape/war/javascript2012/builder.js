var lastFocus={td:"", element:"", type:""};

function draw_stream(id,isOpened){
	var action="builder?middleAction=view_stream&id_selected_stream="+id;
	
	var img = document.getElementById("img_stream_"+id);
	var alwaysOpen = false;

	try{
		alwaysOpen=isOpened;
	}catch(e){
	}
	if(alwaysOpen){
		action+="&type_selected=open";

	}else{
		if(img.src.indexOf("_close")>-1){
			action+="&type_selected=open";

		} else{
			action+="&type_selected=close";
	
		}
	}
	ajax_makeRequest(action,"stream_"+id,"JSAfter_show_stream","",false);
}
function draw_action(id,isOpened){

	var action="builder?middleAction=view_action&id_selected_action="+id;
	var img = document.getElementById("img_action_"+id);
	
	try{
		alwaysOpen=isOpened;
	}catch(e){
	}
	if(alwaysOpen){
		action+="&type_selected=open";
	}else{
		if(img.src.indexOf("_close")>-1){
			action+="&type_selected=open";
		} else{
			action+="&type_selected=close";	
		}
	}

	ajax_makeRequest(action,"action_"+id,"JSAfter_show_stream","",false);
}
function draw_redirect1(id,isOpened){

	var action="builder?middleAction=view_redirect1&id_selected_redirect="+id;
	var img = document.getElementById("img_redirect_"+id);
	
	try{
		alwaysOpen=isOpened;
	}catch(e){
	}
	if(alwaysOpen){
		action+="&type_selected=open";
	}else{
		if(img.src.indexOf("_close")>-1){
			action+="&type_selected=open";
		} else{
			action+="&type_selected=close";	
		}
	}

	ajax_makeRequest(action,"redirect_"+id,"JSAfter_show_stream","",false);
}
function draw_bean(id,isOpened){

	var action="builder?middleAction=view_bean&id_selected_bean="+id;
	var img = document.getElementById("img_bean_"+id);
	
	try{
		alwaysOpen=isOpened;
	}catch(e){
	}
	if(alwaysOpen){
		action+="&type_selected=open";
	}else{
		if(img.src.indexOf("_close")>-1){
			action+="&type_selected=open";
		} else{
			action+="&type_selected=close";	
		}
	}

	ajax_makeRequest(action,"bean_"+id,"JSAfter_show_stream","",false);
}
function draw_apply_to_action(id_parent, id,isOpened){

	var action="builder?middleAction=view_apply_to_action&id_selected_stream="+escape(id_parent)+"&id_selected_apply_to_action="+escape(id);
	var img = document.getElementById("img_apply_to_action_"+id_parent+"_"+id);

	try{
		alwaysOpen=isOpened;
	}catch(e){
	}
	if(alwaysOpen){
		action+="&type_selected=open";
	}else{
		if(img.src.indexOf("_close")>-1){
			action+="&type_selected=open";
		} else{
			action+="&type_selected=close";	
		}
	}
	ajax_makeRequest(action,"apply_to_action_"+id_parent+"_"+id,"JSAfter_show_apply_to_action","",false);
}
function draw_redirect(id_parent, id,isOpened){

	var action="builder?middleAction=view_redirect&id_selected_action="+escape(id_parent)+"&id_selected_redirect="+escape(id);
	var img = document.getElementById("img_redirect_"+id_parent+"_"+id);

	try{
		alwaysOpen=isOpened;
	}catch(e){
	}
	if(alwaysOpen){
		action+="&type_selected=open";
	}else{
		if(img.src.indexOf("_close")>-1){
			action+="&type_selected=open";
		} else{
			action+="&type_selected=close";	
		}
	}
	ajax_makeRequest(action,"redirect_"+id_parent+"_"+id,"JSAfter_show_apply_to_action","",false);
}
function draw_transformationoutput(id_parent, id,isOpened){

	var action="builder?middleAction=view_transformationoutput&id_selected_action="+escape(id_parent)+"&id_selected_transformationoutput="+escape(id);
	var img = document.getElementById("img_transformationoutput_"+id_parent+"_"+id);

	try{
		alwaysOpen=isOpened;
	}catch(e){
	}
	if(alwaysOpen){
		action+="&type_selected=open";
	}else{
		if(img.src.indexOf("_close")>-1){
			action+="&type_selected=open";
		} else{
			action+="&type_selected=close";	
		}
	}

	ajax_makeRequest(action,"transformationoutput_"+id_parent+"_"+id,"JSAfter_show_apply_to_action","",false);
}
function draw_section(id_parent,id_parent1, id,isOpened){

	var action="builder?middleAction=view_section&id_selected_action="+escape(id_parent)+"&id_selected_redirect="+escape(id_parent1)+"&id_selected_section="+escape(id);
	var img = document.getElementById("img_section_"+id_parent+"_"+id_parent1+"_"+id);

	try{
		alwaysOpen=isOpened;
	}catch(e){
	}
	if(alwaysOpen){
		action+="&type_selected=open";
	}else{
		if(img.src.indexOf("_close")>-1){
			action+="&type_selected=open";
		} else{
			action+="&type_selected=close";	
		}
	}

	ajax_makeRequest(action,"section_"+id_parent+"_"+id_parent1+"_"+id,"JSAfter_show_section","",false);
}
function draw_transformationoutput1(id_parent,id_parent1, id,isOpened){

	var action="builder?middleAction=view_transformationoutput1&id_selected_action="+escape(id_parent)+"&id_selected_redirect="+escape(id_parent1)+"&id_selected_transformationoutput="+escape(id);
	var img = document.getElementById("img_transformationoutput_"+id_parent+"_"+id_parent1+"_"+id);
	try{
		alwaysOpen=isOpened;
	}catch(e){
	}
	if(alwaysOpen){
		action+="&type_selected=open";
	}else{
		if(img.src.indexOf("_close")>-1){
			action+="&type_selected=open";
		} else{
			action+="&type_selected=close";	
		}
	}


	ajax_makeRequest(action,"transformationoutput_"+id_parent+"_"+id_parent1+"_"+id,"JSAfter_show_transformationoutput1","",false);
}
function draw_item(id_parent, id,isOpened){

	var action="builder?middleAction=view_item&id_selected_bean="+escape(id_parent)+"&id_selected_item="+escape(id);
	var img = document.getElementById("img_item_"+id_parent+"_"+id);

	try{
		alwaysOpen=isOpened;
	}catch(e){
	}
	if(alwaysOpen){
		action+="&type_selected=open";
	}else{
		if(img.src.indexOf("_close")>-1){
			action+="&type_selected=open";
		} else{
			action+="&type_selected=close";	
		}
	}
	ajax_makeRequest(action,"item_"+id_parent+"_"+id,"JSAfter_show_item","",false);
}
function removeEntity(type,id_1,id_2,id_3){
	var action="builder?middleAction=remove_"+type;
	if(type=="stream"){
		action+="&id_selected_stream="+id_1;
		goAction(action);
	}	
	if(type=="apply_to_action"){
		action+="&id_selected_stream="+id_1+"&id_selected_apply_to_action="+id_2;
		ajax_makeRequest(action,"stream_"+id_1,"JSAfter_show_stream","",false);
		return;
	}
	if(type=="action"){
		action+="&id_selected_action="+id_1;
		goAction(action);
	}	
	if(type=="redirect1"){
		action+="&id_selected_redirect="+id_1;
		goAction(action);
	}	
	if(type=="bean"){
		action+="&id_selected_bean="+id_1;
		goAction(action);
	}		
	if(type=="item"){
		action+="&id_selected_bean="+id_1+"&id_selected_item="+id_2;
		ajax_makeRequest(action,"bean_"+id_1,"JSAfter_show_bean","",false);
		return;
	}	
	if(type=="redirect"){
		action+="&id_selected_action="+id_1+"&id_selected_redirect="+id_2;
		ajax_makeRequest(action,"action_"+id_1,"JSAfter_show_action","",false);
		return;
	}
	if(type=="transformationoutput"){
		action+="&id_selected_action="+id_1+"&id_selected_transformationoutput="+id_2;
		ajax_makeRequest(action,"action_"+id_1,"JSAfter_show_action","",false);
		return;
	}	
	if(type=="transformationoutput1"){
		action+="&id_selected_action="+id_1+"&id_selected_redirect="+id_2+"&id_selected_transformationoutput="+id_3;
		ajax_makeRequest(action,"redirwct_"+id_1+"_"+id_2,"JSAfter_show_redirect","",false);
		return;
	}			
	if(type=="section"){
		action+="&id_selected_action="+id_1+"&id_selected_redirect="+id_2+"&id_selected_section="+id_3;
		ajax_makeRequest(action,"redirwct_"+id_1+"_"+id_2,"JSAfter_show_redirect","",false);
		return;
	}			
			
//	dhtmlLoadScript(action);
	
}

function addEntity(type,id_1,id_2){
	var action="builder?middleAction=add_"+type+"&type_selected=open";
	if(type=="stream"){
/*		
		var obj = document.getElementById("action-streams");
		var newtd = document.createElement('td');
		newtd.id="stream_[new stream]";
		newtd.valign="top";
		obj.appendChild(newtd);
		if (document.getElementById('action-streams').style.display=="none")
			draw_div_section('action-streams','display_streams');	
		
		ajax_makeRequest(action,"stream_[new stream]","JSAfter_show_stream");
*/
		goAction(action);
	}
	if(type=="action"){
/*		
		var obj = document.getElementById("action-mappings");
		var newtr = document.createElement('tr');		
		var newtd = document.createElement('td');
		newtd.id="action_[new action]";
		newtd.valign="top";
		newtr.appendChild(newtd);
		obj.appendChild(newtr);
		if (document.getElementById('action-mappings').style.display=="none")
			draw_div_section('action-mappings','display_actions');		
		
		ajax_makeRequest(action,"action_[new action]","JSAfter_show_stream");
*/
		goAction(action);
	}
	if(type=="bean"){
/*		
		var obj = document.getElementById("form-beans");
		var newtr = document.createElement('tr');		
		var newtd = document.createElement('td');
		newtd.id="bean_[new bean]";
		newtd.valign="top";
		newtr.appendChild(newtd);
		obj.appendChild(newtr);
		if (document.getElementById('form-beans').style.display=="none")
			draw_div_section('form-beans','display_beans');		
		
		ajax_makeRequest(action,"bean_[new bean]","JSAfter_show_bean");
*/
		goAction(action);
	}	
	if(type=="item"){
		var obj = document.getElementById("items_"+id_1);
		var newtr = document.createElement('tr');		
		var newtd = document.createElement('td');
		newtd.id="item_[new item]";
		newtd.valign="top";
		newtr.appendChild(newtd);
		obj.appendChild(newtr);
		action+="&id_selected_bean="+id_1;
		ajax_makeRequest(action,"bean_"+id_1,"JSAfter_show_bean","",false);
	}
	
	if(type=="redirect1"){
/*		
		var obj = document.getElementById("form-redirects");
		var newtr = document.createElement('tr');		
		var newtd = document.createElement('td');
		newtd.id="redirect_[new redirect]";
		newtd.valign="top";
		newtr.appendChild(newtd);
		obj.appendChild(newtr);
		if (document.getElementById('form-redirects').style.display=="none")
			draw_div_section('form-redirects','display_redirects');	
		
		ajax_makeRequest(action,"redirect_[new redirect]","JSAfter_show_bean");
*/
		goAction(action);
	}			
			
				
	if(type=="apply_to_action"){
/*		
		var obj = document.getElementById("applys-to-action_"+id_1);
		var newtr = document.createElement('tr');		
		var newtd = document.createElement('td');
		newtd.id="apply_to_action_[new action]";
		newtd.valign="top";
		newtr.appendChild(newtd);
		obj.appendChild(newtr);
*/		
		action+="&id_selected_stream="+id_1;
		ajax_makeRequest(action,"stream_"+id_1,"JSAfter_show_stream","",false);
	}	
	if(type=="redirect"){
/*		
		var obj = document.getElementById("redirects_"+id_1);
		var newtr = document.createElement('tr');		
		var newtd = document.createElement('td');
		newtd.id="redirect_[new redirect]";
		newtd.valign="top";
		newtr.appendChild(newtd);
		obj.appendChild(newtr);
*/		
		action+="&id_selected_action="+id_1;
		ajax_makeRequest(action,"action_"+id_1,"JSAfter_show_action","",false);
	}
	if(type=="transformationoutput"){
/*		
		var obj = document.getElementById("transformationoutputs_"+id_1);
		var newtr = document.createElement('tr');		
		var newtd = document.createElement('td');
		newtd.id="transformationoutput_[new transformationoutput]";
		newtd.valign="top";
		newtr.appendChild(newtd);
		obj.appendChild(newtr);
*/		
		action+="&id_selected_action="+id_1;
		ajax_makeRequest(action,"action_"+id_1,"JSAfter_show_action","",false);
	}	
	if(type=="transformationoutput1"){
/*		
		var obj = document.getElementById("transformationoutputs_"+id_1+"_"+id_2);
		var newtr = document.createElement('tr');		
		var newtd = document.createElement('td');
		newtd.id="transformationoutput_"+id_1+"_[new transformationoutput]";
		newtd.valign="top";
		newtr.appendChild(newtd);
		obj.appendChild(newtr);
*/		
		action+="&id_selected_action="+id_1+"&id_selected_redirect="+id_2;
		ajax_makeRequest(action,"redirect_"+id_1+"_"+id_2,"JSAfter_show_redirect","",false);
	}		
	if(type=="section"){
/*		
		var obj = document.getElementById("sections_"+id_1+"_"+id_2);
		var newtr = document.createElement('tr');		
		var newtd = document.createElement('td');
		newtd.id="section_"+id_1+"_[new section]";
		newtd.valign="top";
		newtr.appendChild(newtd);
		obj.appendChild(newtr);
*/		
		action+="&id_selected_action="+id_1+"&id_selected_redirect="+id_2;
		ajax_makeRequest(action,"redirect_"+id_1+"_"+id_2,"JSAfter_show_redirect","",false);
	}			
}
function updateValue(obj, name){

		
	
		var spl = obj.offsetParent.id.split("|");

		var type=spl[0];
		var id_1=spl[1];
		var id_2=spl[2];
		var id_3=spl[3];
		
		
		var action="builder?middleAction=update_"+type;

//		makeFocus(obj);
		
		if(type=="general"){
			action+="&name="+name+"&value="+escape(obj.value);
			ajax_makeRequest(action,"general","JSAfter_update","",false);
			return;

		}
		
		if(type=="stream"){
			action+="&type_selected=open";
			action+="&id_selected_stream="+id_1+"&name="+name+"&value="+escape(obj.value);
			if(name=="name"){
				var td = document.getElementById("stream_"+id_1);
				td.id="stream_"+obj.value;
				ajax_makeRequest(action,td.id,"JSAfter_update","",false);
			}
			else ajax_makeRequest(action,"stream_"+id_1,"JSAfter_update","",false);
			return;

		}
		if(type=="action"){
			action+="&type_selected=open";
			action+="&id_selected_action="+id_1+"&name="+name+"&value="+escape(obj.value);
			if(name=="path"){
				var td = document.getElementById("action_"+id_1);
				td.id="action_"+obj.value;
				ajax_makeRequest(action,td.id,"JSAfter_update","",false);
			}
			else ajax_makeRequest(action,"action_"+id_1,"JSAfter_update","",false);
			return;
			
		}
		if(type=="bean"){
			action+="&type_selected=open";
			action+="&id_selected_bean="+id_1+"&name="+name+"&value="+escape(obj.value);
			if(name=="name"){
				var td = document.getElementById("bean_"+id_1);
				td.id="bean_"+obj.value;
				ajax_makeRequest(action,td.id,"JSAfter_update","",false);
			}
			else ajax_makeRequest(action,"bean_"+id_1,"JSAfter_update","",false);
			return;
			
		}
		if(type=="item"){
			action+="&type_selected=open";
			action+="&id_selected_bean="+id_1+"&id_selected_item="+id_2+"&name="+name+"&value="+escape(obj.value);
			if(name=="name"){
				var td = document.getElementById("item_"+id_1+"_"+id_2);
				td.id="item_"+id_1+"_"+obj.value;
				ajax_makeRequest(action,td.id,"JSAfter_update","",false);
			}
			else ajax_makeRequest(action,"item_"+id_1+"_"+id_2,"JSAfter_update","",false);
			return;

		}
		
		if(type=="redirect1"){
			action+="&type_selected=open";
			action+="&id_selected_redirect="+id_1+"&name="+name+"&value="+escape(obj.value);
			if(name=="path"){
				var td = document.getElementById("redirect_"+id_1);
				td.id="redirect_"+obj.value;
				ajax_makeRequest(action,td.id,"JSAfter_update","",false);
			}
			else ajax_makeRequest(action,"redirect_"+id_1,"JSAfter_update","",false);
			return;
		}
		
		if(type=="apply_to_action"){
			action+="&type_selected=open";
			action+="&id_selected_stream="+id_1+"&id_selected_apply_to_action="+id_2+"&name="+name+"&value="+escape(obj.value);
			if(name=="action"){
				var td = document.getElementById("apply_to_action_"+id_1+"_"+id_2);
				td.id="apply_to_action_"+id_1+"_"+obj.value;
				ajax_makeRequest(action,td.id,"JSAfter_update","",false);
			}
			else ajax_makeRequest(action,"apply_to_action_"+id_1+"_"+id_2,"JSAfter_update","",false);
			return;


		}
		if(type=="redirect"){
			action+="&type_selected=open";
			action+="&id_selected_action="+id_1+"&id_selected_redirect="+id_2+"&name="+name+"&value="+escape(obj.value);
			if(name=="path"){
				var td = document.getElementById("redirect_"+id_1+"_"+id_2);
				td.id="redirect_"+id_1+"_"+obj.value;
				ajax_makeRequest(action,td.id,"JSAfter_update","",false);
			}
			else ajax_makeRequest(action,"redirect_"+id_1+"_"+id_2,"JSAfter_update","",false);
			return;

		}
		if(type=="transformationoutput"){
			action+="&type_selected=open";
			action+="&id_selected_action="+id_1+"&id_selected_transformationoutput="+id_2+"&name="+name+"&value="+escape(obj.value);
			if(name=="name"){
				var td = document.getElementById("transformationoutput_"+id_1+"_"+id_2);
				td.id="transformationoutput_"+id_1+"_"+obj.value;
				ajax_makeRequest(action,td.id,"JSAfter_update","",false);
			}
			else ajax_makeRequest(action,"transformationoutput_"+id_1+"_"+id_2,"JSAfter_update","",false);
			return;
		}
		if(type=="transformationoutput1"){
			action+="&type_selected=open";
			action+="&id_selected_action="+id_1+"&id_selected_redirect="+id_2+"&id_selected_transformationoutput="+id_3+"&name="+name+"&value="+escape(obj.value);
			if(name=="name"){
				var td = document.getElementById("transformationoutput_"+id_1+"_"+id_2+"_"+id_3);
				td.id="transformationoutput_"+id_1+"_"+id_2+"_"+obj.value;
				ajax_makeRequest(action,td.id,"JSAfter_update","",false);
			}
			else ajax_makeRequest(action,"transformationoutput_"+id_1+"_"+id_2+"_"+id_3,"JSAfter_update","",false);
			return;

		}
		if(type=="section"){
			action+="&type_selected=open";
			action+="&id_selected_action="+id_1+"&id_selected_redirect="+id_2+"&id_selected_section="+id_3+"&name="+name+"&value="+escape(obj.value);
			if(name=="name"){
				var td = document.getElementById("section_"+id_1+"_"+id_2+"_"+id_3);
				td.id="section_"+id_1+"_"+id_2+"_"+obj.value;
				ajax_makeRequest(action,td.id,"JSAfter_update","",false);
			}
			else ajax_makeRequest(action,"section_"+id_1+"_"+id_2+"_"+id_3,"JSAfter_update","",false);
			return;

		}

//		dhtmlLoadScript(action);

}



function makeUnselectable(node) {
    if (node.nodeType == 1) {
        node.unselectable = true;
    }
    var child = node.firstChild;
    while (child) {
        makeUnselectable(child);
        child = child.nextSibling;
    }
}


function draw_div_section(id,disp){

	try{
		if (document.getElementById(id).style.display=="block" ||
		    document.getElementById(id).style.display==""){
			document.getElementById(id).style.display="none";
			document.getElementById(disp).value="false";
			document.getElementById(id+"_img").src="images/menu/16_close.gif";
			document.getElementById("show_"+id).value="0";
			
		}else{
			document.getElementById(id).style.display="block";
			document.getElementById(disp).value="true";
			document.getElementById(id+"_img").src="images/menu/16_open.gif";
			document.getElementById("show_"+id).value="1";
		}	
	}catch(e){
	}
}

function makeFocus(obj){
	try{
		var spl = obj.offsetParent.id.split("|");
		var type=spl[0];
		var td_id = obj.offsetParent.id;
		var element_id = obj.id;
		lastFocus.td=td_id;
		lastFocus.element=element_id;
		lastFocus.type=type;
	}catch(e){
	}
}

function JSAfter_update(){
	try{
		if(lastFocus.type=="general"){
			var element = document.getElementById(lastFocus.element);
			element.focus();
		}else{	
			var td = document.getElementById(lastFocus.td);
			for(var i=0;i<td.childNodes.length;i++){
				if(td.childNodes[i].id=lastFocus.element)
					td.childNodes[i].focus();
					return;
			}
		}	
	}catch(e){
	}
}
