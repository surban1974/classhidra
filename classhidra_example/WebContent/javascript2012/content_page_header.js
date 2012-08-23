var array_formBean_cd_point = new Array();
function elaborate_cd_point(objid,code){
	document.getElementById("cd_point").value = code;
	document.getElementById("scroll").value=document.getElementById("div_formBean_cd_point").scrollTop;
}

function menu(){
	try{
		var div = document.getElementById("menu_operation");
		var img = document.getElementById("menu_img");
		var disp = div.style.display;
		if(disp=="block"){
			div.style.display="none";
			img.src="images/menu/16_close.gif";
		}else{
			div.style.display="block";
			img.src="images/menu/16_open.gif";
		}
	}catch(e){
	}
}


function actionMenuVisual(value,id){
	try{
		ajax_makeRequest("menuCreator?menu_id="+id+"&menu_action=visual_"+value,id,"JSAfter_showAsPopup","",false);
	}catch(e){
	}	
	
}
function actionContent(action,type){

	if(type=="") type="PUT";
	try{
		if(type=="GET"){
			if(action.indexOf("?")>-1) 
				document.forms[0].action = action;
			else document.forms[0].action = action;
			document.forms[0].submit();
		}
		if(type=="PUT"){
			if(action=="exit"){
				window.close();
				return;
			}
			if(action=="login" || action=="home"){
				location ="content?middleAction=&menuSource="+action;
				return;
			}	
			location ="content?middleAction=&menuSource="+action;
			
		}
		if(type=="SCRIPT"){		
			eval(action);
		}	
		try{
				beforeClick();
		}catch(e){
		}		
		
	}catch(e){

	}
	try{
		document.getElementById("content_Panel_runSubmit").style.display="block";
		if(parent.frames["content"])
			parent.frames["content"].beforeClick();
	}catch(e){
	}

}

