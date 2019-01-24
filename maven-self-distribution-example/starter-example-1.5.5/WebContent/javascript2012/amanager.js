var lastFocus={td:"", element:"", type:""};

function enableEntity(type,id_0,id_1,id_2,id_3){
	var action="amanager?middleAction=enable_"+type+"&type_selected=open&id_selected_relation="+id_0;
	if(type=="action"){
		action+="&id_selected_action="+id_1;
	}	
	if(type=="redirect"){
		action+="&id_selected_action="+id_1+"&id_selected_redirect="+id_2;
	}	
	if(type=="section"){
		action+="&id_selected_action="+id_1+"&id_selected_redirect="+id_2+"&id_selected_section="+id_3;
	}	

	ajax_makeRequest(action,"action_"+id_0+"_"+id_1,"JSAfter_show_action","",false);
}

function draw_user(id,isOpened){

	var action="amanager?middleAction=view_user&id_selected_user="+id;
	var img = document.getElementById("img_user_"+id);
	
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

	ajax_makeRequest(action,"user_"+id,"JSAfter_show_user","",false);
}

function draw_group(id,isOpened){

	var action="amanager?middleAction=view_group&id_selected_group="+id;
	var img = document.getElementById("img_group_"+id);
	
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

	ajax_makeRequest(action,"group_"+id,"JSAfter_show_group","",false);
}

function draw_target(id,isOpened){

	var action="amanager?middleAction=view_target&id_selected_target="+id;
	var img = document.getElementById("img_target_"+id);
	
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

	ajax_makeRequest(action,"target_"+id,"JSAfter_show_target","",false);
}

function draw_relation(id,isOpened){

	var action="amanager?middleAction=view_relation&id_selected_relation="+id;
	var img = document.getElementById("img_relation_"+id);
	
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

	ajax_makeRequest(action,"relation_"+id,"JSAfter_show_relation","",false);
}

function draw_action(id_parent,id,isOpened){

	var action="amanager?middleAction=view_action&id_selected_relation="+id_parent+"&id_selected_action="+id;
	var img = document.getElementById("img_action_"+id_parent+"_"+id);
	
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

	ajax_makeRequest(action,"action_"+id_parent+"_"+id,"JSAfter_show_action","",false);
}

function draw_redirect(id_parent, id,isOpened){

	var action="amanager?middleAction=view_redirect&id_selected_action="+escape(id_parent)+"&id_selected_redirect="+escape(id);
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

function draw_section(id_parent,id_parent1, id,isOpened){

	var action="amanager?middleAction=view_section&id_selected_action="+escape(id_parent)+"&id_selected_redirect="+escape(id_parent1)+"&id_selected_section="+escape(id);
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

function draw_auth(id_parent, id,isOpened){

	var action="amanager?middleAction=view_authentication&id_selected_relation="+escape(id_parent);
	var img = document.getElementById("img_relation_auth_"+id_parent);

	try{
		alwaysOpen=isOpened;
	}catch(e){
	}
	if(alwaysOpen){
		action+="&type_selected=open";
	}else{
		if(img.src.indexOf("_close")>-1){
			action+="&type_selected=open";
			img.src="images/menu/16_open.gif";
		} else{
			action+="&type_selected=close";	
			img.src="images/menu/16_close.gif"
		}
	}
	ajax_makeRequest(action,"relation_auth_"+id_parent,"JSAfter_show_authentication","",false);
}

function removeEntity(type,id_1,id_2,id_3){
	var action="amanager?middleAction=remove_"+type;
	if(type=="user"){
		action+="&id_selected_user="+id_1;
		goAction(action);
	}	
	if(type=="group"){
		action+="&id_selected_group="+id_1;
		goAction(action);
	}	
	if(type=="target"){
		action+="&id_selected_target="+id_1;
		goAction(action);
	}		
	if(type=="relation"){
		action+="&id_selected_relation="+id_1;
		goAction(action);
	}		
	if(type=="groupIntoUser"){
		action+="&id_selected_user="+id_1+"&id_selected_group="+id_2;
		ajax_makeRequest(action,"user_"+id_1,"JSAfter_show_user","",false);
		return;
	}	
	if(type=="groupIntoRelation"){
		action+="&id_selected_relation="+id_1+"&id_selected_group="+id_2;
		ajax_makeRequest(action,"relation_"+id_1,"JSAfter_show_relation","",false);
		return;
	}	
	if(type=="targetIntoRelation"){
		action+="&id_selected_relation="+id_1+"&id_selected_target="+id_2;
		ajax_makeRequest(action,"relation_"+id_1,"JSAfter_show_relation","",false);
		return;
	}	
	if(type=="userIntoGroup"){
		action+="&id_selected_group="+id_1+"&id_selected_user="+id_2;
		ajax_makeRequest(action,"group_"+id_1,"JSAfter_show_group","",false);
		return;
		
	}			
	if(type=="userIntoTarget"){
		action+="&id_selected_target="+id_1+"&id_selected_user="+id_2;
		ajax_makeRequest(action,"target_"+id_1,"JSAfter_show_target","",false);
		return;
	}			


	dhtmlLoadScript(action);
	
}

function addEntity(type,id_1,id_2){
	var action="amanager?middleAction=add_"+type+"&type_selected=open";
	if(type=="user"){
/*		
		var obj = document.getElementById("users");
		var newtr = document.createElement('tr');		
		var newtd = document.createElement('td');
		newtd.id="user_[new user]";
		newtd.valign="top";
		newtr.appendChild(newtd);
		obj.appendChild(newtr);	
*/		
		goAction(action);
//		ajax_makeRequest(action,"user_[new user]","JSAfter_show_user");
	}
	if(type=="group"){
/*		
		var obj = document.getElementById("groups");
		var newtr = document.createElement('tr');
		var newtd = document.createElement('td');
		newtd.id="group_[new group]";
		newtd.valign="top";
		newtr.appendChild(newtd);
		obj.appendChild(newtr);		
*/		
		goAction(action);
//		ajax_makeRequest(action,"group_[new group]","JSAfter_show_group");
	}
	if(type=="target"){
/*		
		var obj = document.getElementById("targets");
		var newtr = document.createElement('tr');
		var newtd = document.createElement('td');
		newtd.id="target_[new target]";
		newtd.valign="top";
		newtr.appendChild(newtd);
		obj.appendChild(newtr);	
*/			
		goAction(action);
//		ajax_makeRequest(action,"target_[new target]","JSAfter_show_target");
	}	
	if(type=="relation"){
/*		
		var obj = document.getElementById("relations");
		var newtr = document.createElement('tr');
		var newtd = document.createElement('td');
		newtd.id="relation_[new relation]";
		newtd.valign="top";
		newtr.appendChild(newtd);
		obj.appendChild(newtr);		
*/
		goAction(action);
//		ajax_makeRequest(action,"relation_[new relation]","JSAfter_show_relation");
	}	
	if(type=="groupIntoUser"){
		action+="&id_selected_user="+id_1+"&id_selected_group="+id_2;	
		ajax_makeRequest(action,"user_"+id_1,"JSAfter_show_user","",false);
	}	
	if(type=="userIntoGroup"){
		action+="&id_selected_group="+id_1+"&id_selected_user="+id_2;	
		ajax_makeRequest(action,"group_"+id_1,"JSAfter_show_group","",false);
	}		
	if(type=="userIntoTarget"){
		action+="&id_selected_target="+id_1+"&id_selected_user="+id_2;	
		ajax_makeRequest(action,"target_"+id_1,"JSAfter_show_target","",false);
	}		
	if(type=="groupIntoRelation"){
		action+="&id_selected_relation="+id_1+"&id_selected_group="+id_2;	
		ajax_makeRequest(action,"relation_"+id_1,"JSAfter_show_user","",false);
	}	
	if(type=="targetIntoRelation"){
		action+="&id_selected_relation="+id_1+"&id_selected_target="+id_2;	
		ajax_makeRequest(action,"relation_"+id_1,"JSAfter_show_user","",false);
	}	
	if(type=="targetIntoUser"){
		action+="&id_selected_user="+id_1+"&id_selected_target="+id_2;	
		ajax_makeRequest(action,"user_"+id_1,"JSAfter_show_user","",false);
//		dhtmlLoadScript(action);
	}	
	
	
	
}
function updateValue(obj, name){

	try{
		var spl = obj.offsetParent.id.split("|");

		var type=spl[0];
		var id_1=spl[1];
		var id_2=spl[2];
		var id_3=spl[3];
		
		
		var action="amanager?middleAction=update_"+type;

		if(type=="general"){
			action+="&name="+name+"&value="+escape(obj.value);
			ajax_makeRequest(action,"general","JSAfter_update","",false);
			return;
		}
		
		if(type=="user"){
			action+="&type_selected=open";
			action+="&id_selected_user="+id_1+"&name="+name+"&value="+escape(obj.value);
			if(name=="name"){
				var td = document.getElementById("user_"+id_1);
				td.id="user_"+obj.value;
				ajax_makeRequest(action,td.id,"JSAfter_update","",false);
			}
			else ajax_makeRequest(action,"user_"+id_1,"JSAfter_update","",false);
			return;
		}
		if(type=="group"){
			action+="&type_selected=open";
			action+="&id_selected_group="+id_1+"&name="+name+"&value="+escape(obj.value);
			if(name=="name"){
				var td = document.getElementById("group_"+id_1);
				td.id="group_"+obj.value;
				ajax_makeRequest(action,td.id,"JSAfter_update","",false);
			}
			else ajax_makeRequest(action,"group_"+id_1,"JSAfter_update","",false);
			return;
		}
		if(type=="target"){
			action+="&type_selected=open";
			action+="&id_selected_target="+id_1+"&name="+name+"&value="+escape(obj.value);
			if(name=="name"){
				var td = document.getElementById("target_"+id_1);
				td.id="target_"+obj.value;
				ajax_makeRequest(action,td.id,"JSAfter_update","",false);
			}
			else ajax_makeRequest(action,"target_"+id_1,"JSAfter_update","",false);
			return;
		}
		if(type=="relation"){
			action+="&type_selected=open";
			action+="&id_selected_relation="+id_1+"&name="+name+"&value="+escape(obj.value);
			if(name=="name"){
				var td = document.getElementById("relation_"+id_1);
				td.id="relation_"+obj.value;
				ajax_makeRequest(action,td.id,"JSAfter_update","",false);
			}
			else ajax_makeRequest(action,"relation_"+id_1,"JSAfter_update","",false);
			return;
		}
		
		if(type=="groupIntoUser"){
			action+="&id_selected_user="+id_1+"&id_selected_group="+id_2+"&name="+name+"&value="+escape(obj.value);
		}

		dhtmlLoadScript(action);
	}catch(e){
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
				if(td.childNodes[i].id==lastFocus.element){
					field4focus=td.childNodes[i];
					window.setTimeout("timeoutSetFocus();",100);
//					td.childNodes[i].focus();
					return;
				}
			}
		}	
	}catch(e){
	}
}

function timeoutSetFocus(){
	try{
		field4focus.focus();
	}catch(e){
	}
}