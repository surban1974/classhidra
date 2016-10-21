/**
* Name: jsTool.js
* Version: 1.5.2.3 (compatible classHidra 1.5.2)
* Creation date: (25/05/2015)
* @author: Svyatoslav Urbanovych svyatoslav.urbanovych@gmail.com
*/


var notClear=";$action;$action_from;$navigation;parent_pointOfLaunch;child_pointOfReturn;";
var prevKEYid = 0;
var KEYtime = 0;

var frame_dynamic = true;
var timeForOut = 250;
var varScrollLeftPrev=0;
var varScrollTopPrev=0;
var xmouse=0;
var ymouse=0;
var variables = new Array;
var value_variables = new Array;


var objPopup=function()
{
 this.width=0;
 this.height=0;
 this.maximize=false;
 this.position="";
 this.zIndex=-1;
 this.pscrollx="";
 this.pscrolly="";

}



var mPanel_Popup = new Map();

var vPanel_NotPopup = new objPopup();

var NS4=(document.layers);
var NS6=false;
var IE4=(document.all);
if (!IE4) {
	NS6=(document.getElementById);
}

((NS4) || (NS6))?window.captureEvents(Event.MOUSEMOVE):0;
((NS4) || (NS6))?window.captureEvents(Event.KEYDOWN):0;

function Mouse(evnt){
  xmouse = ((NS4) || (NS6))?(evnt.pageX):event.x;
  ymouse = ((NS4) || (NS6))?(evnt.pageY):event.y;
}
(NS4)?window.onMouseMove=Mouse:document.onmousemove=Mouse;

document.onkeydown=Key;


function isIE () {
	  var myNav = navigator.userAgent.toLowerCase();
	  return (myNav.indexOf('msie') != -1) ? parseInt(myNav.split('msie')[1]) : false;
	}

function runHelp(){
/*
	var helpId="";
	var helpSection="";
	var helpRedirect="";
	if(document.getElementById("$action")) helpId=document.getElementById("$action").value;
	startURL("$help?helpId="+helpId+"&helpSection="+helpSection+"&helpRedirect="+helpRedirect,"Help");
*/
}



function runSubmitOnEnter(){
	try{
		var go = false;
		try{
			if(runActionOnEnter())
				beforeClickJs();
		}catch(e){
			go=true;
		}
		if(go){
			document.getElementById("content_Panel_runSubmit").style.display="none";
//			document.forms[0].submit();
//			if(parent.frames["content"]) parent.frames["content"].beforeClickJs();
		}
	}catch(e){
	}
}

function beforeClickJs(){

//	var hasVScroll = document.body.scrollHeight > document.body.clientHeight;

	try{
		document.getElementById("content_Panel_runSubmit").style.top=document.body.scrollTop;
		document.getElementById("content_Panel_runSubmit").style.display="block";
	}catch(e){

	}
}

function beforeClick(){

	try{
//		document.body.style.overflowX="hidden";
//		document.body.style.overflowY="hidden";
	}catch(e){

	}
	var hasVScroll = document.body.scrollHeight > document.body.clientHeight;
	
	try{
		
		document.getElementById("content_Panel_runSubmit").style.top=document.body.scrollTop;
		document.getElementById("content_Panel_runSubmit").style.display="block";
	}catch(e){

	}
}


function runSubmitOnly(middle){
	try{

		document.forms[0].middleAction.value=middle;
		document.forms[0].submit();
	}catch(e){
	}
}

function runSubmitOnly(formName,middle){
	try{
		var form = document.getElementById(formName);
		if(form){
			form.middleAction.value=middle;
			form.submit();
		}
	}catch(e){
	}
}



function timeRunSubmit(middle){
	beforeClickJs();
	window.setTimeout("runSubmit('"+middle+"')", 1000);
}

function runSubmit(middle,formName,exec_type){
	try{
		var form = document.getElementById(formName);
		if(form){
		}else{
			form = document.forms[0];
		}
		if(form){
			form.middleAction.value=middle;
			if(exec_type){
				if(form.action.indexOf("?")>-1){
					form.action+="&$exec_type="+exec_type;
				}else{
					form.action+="?$exec_type="+exec_type;
				}
			}

			form.submit();
			beforeClickJs();
			if(parent.frames["content"])
				parent.frames["content"].beforeClickJs();

		}
	}catch(e){
	}
}

function runSubmitF(formName,middle){
	try{
		document.getElementById(formName).middleAction.value=middle;
		document.getElementById(formName).submit();
		beforeClickJs();
		if(parent.frames["content"])
			parent.frames["content"].beforeClickJs();
	}catch(e){
	}
}
function runSubmitAjax(formName,middle,target,afterJSFunction){
	try{
		document.getElementById(formName).middleAction.value=middle;
		ajax_submit(document.getElementById(formName),target,afterJSFunction);
	}catch(e){

	}
}

function runSubmitAjaxLoadScript(formName,middle){
	try{

		document.getElementById(formName).middleAction.value=middle;
		var url = ajax_makeParameters(document.getElementById(formName),document.getElementById(formName).action);
		if(url=="") url="?tmstamp="+new Date().getTime();
		else url+="&tmstamp="+new Date().getTime();
		dhtmlLoadScript(document.getElementById(formName).action+url,"text/javascript");
	}catch(e){

	}
}


function Key(evnt){

	var id=0;

	if(NS4 || NS6){
		id=(evnt.which);
	}
	if(IE4){
		id=(window.event.keyCode);
	}

	if( 	(id==17 && prevKEYid==16) ||
		(id==16 && prevKEYid==17)){
		KEYtime= new Date().getTime();
		return;
	}
	prevKEYid=id;

	var go = false;
	try{
		var retFun = runActionOnKey(id); 
		if(typeof retFun == "boolean"){	
			go = retFun;
		}
	}catch(e){
		go = true;
	}
	if(go){
		if(id==13) runSubmitOnEnter();
		if(id==113) runHelp();
		if(id==27){
			if(document.getElementById('panel_Popup')){
				try{
					JSBeforeClose_popup();
				}catch(e){};
				try{
					closePanel('panel_Popup');
				}catch(e){};
				document.getElementById('content_Panel_Popup').style.display='none';
			}
			if(document.getElementById('panel_IFrame')){
				try{
					JSBeforeClose_popup();
				}catch(e){};
				try{
					closePanel('panel_IFrame');
				}catch(e){};
				document.getElementById('content_IFrame_Popup').style.display='none';
			}
		}
	}
}



function startURL(URL, Descr){
	if(URL=='') return;
	screenPos = ",screenX=0"
		+",screenY=0"
		+",top=0"
		+",left=0"
		+",width=" + (screen.availWidth -10).toString()
		+",height=" + (screen.availHeight -30).toString();
	if (navigator.appName == 'Netscape')
		screenPos = screenPos + ",fullscreen=yes";

	aParams = "menubar=no"
		  +",status=no"
		  +",directories=no"
		  +",location=no"
		  +",titlebar=no"
		  +",toolbar=no"
		  +",resizable=yes"
		  +",scrollbars=yes"
		  +screenPos;
		  window.open(URL, Descr, aParams)
}

function startWin(URL){
	if(URL=='') return;
	screenPos = ",screenX=0"
		+",screenY=0"
		+",top=0"
		+",left=0"
		+",width=420"
		+",height=130"
	if (navigator.appName == 'Netscape')
		screenPos = screenPos + ",fullscreen=yes";
	aParams = "menubar=no"
		  +",status=no"
		  +",directories=no"
		  +",location=no"
		  +",titlebar=no"
		  +",toolbar=no"
		  +",resizable=yes"
		  +",scrollbars=no"
		  +screenPos;
	window.open(URL, "Tabelli", aParams);
}

function goReturn(action,wac){
	var ret=false;
	try{
		ret = runLocalControlReturn();
	}catch(e){
		ret=true;
	}

	if(ret==true) goAction(action,wac);
}

function actionExecMiddle(action){
	try{
		document.forms[0].middleAction.value=action;
		document.forms[0].submit();
		beforeClickJs();
	}catch(e){
	}
}

function getIdAction(action){
	if(action.indexOf(".bs")==-1) return action;
	var pos = action.indexOf(".bs");
	return action.substring(0,pos);
}

function getParametersAction(action){
	if(action.indexOf("?")==-1) return "";
	var pos = action.indexOf("?");
	return action.substring(pos+1);
}

function goActionMinimal(urlAction, formName){

	var form = document.forms[0];
	try{
		if(formName){
			form = document.getElementById(formName);
		}else{
			form = document.forms[0];
		}
	}catch(e){
	}



	if(form){
	}else form = document.forms[0];

	var url=prepareUrlString(urlAction);

		try{
			if(urlAction.indexOf("?")>-1){
				form.action = urlAction;
			}else{
				if(url.length>0) urlAction="?"+url;
				form.action = getIdAction(urlAction)+""+url;
			}

			form.submit();

		}catch(e){

			try{
				var add2url=url+((url=="")?"":"&")+getParametersAction(urlAction);

				if(add2url.length>0) add2url="?"+add2url;
				var _idaction = getIdAction(urlAction)+""+add2url;
				if(_idaction.length>0 && _idaction.lastIndexOf("&")==_idaction.length-1) _idaction=_idaction.substring(0,_idaction.length-1);
				location.replace(_idaction);
			}catch(e){
			}
		}
	try{

		beforeClickJs();


		if(parent.frames["content"])
			parent.frames["content"].beforeClickJs();
	}catch(e){
	}
	return;

}


function goAction(action,wac,service_parent,service_child,service_id){


		var url=prepareUrlString(action,wac,service_parent,service_child,service_id);
		if(getIdAction(action)=='$login' || getIdAction(action)=='login' || getIdAction(action)=='home'){
			try{
				var add2url=url+((url=="")?"":"&")+getParametersAction(action);

				if(add2url.length>0) add2url="?"+add2url;
				var _idaction = getIdAction(urlAction)+""+add2url;
				if(_idaction.length>0 && _idaction.lastIndexOf("&")==_idaction.length-1) _idaction=_idaction.substring(0,_idaction.length-1);
				if(parent)
					parent.location.replace(_idaction);
				else location.replace(_idaction);
			}catch(e){
			}
		}else{
			try{
				if(action.indexOf("?")>-1){
					var add2url=action+"&"+url;
					if(add2url.length>0 && add2url.lastIndexOf("&")==add2url.length-1) add2url=add2url.substring(0,add2url.length-1);
					document.forms[0].action = add2url;
				}else{
					if(url.length>0) url="?"+url;
					document.forms[0].action = getIdAction(action)+""+url;
				}
				document.forms[0].submit();

			}catch(e){
				try{
//					var add2url=url+((url=="")?"":"&")+getParametersAction(action);
//					if(add2url.length>0) add2url="?"+add2url;
//					location.replace(getIdAction(action)+""+add2url);

					var add2url=url+((url=="")?"":"&")+getParametersAction(action);
					var _idaction = getIdAction(action);
					if(_idaction.indexOf("?")==-1) _idaction+="?"+add2url;
					else _idaction+="&"+add2url;
					if(_idaction.length>0 && _idaction.lastIndexOf("&")==_idaction.length-1) _idaction=_idaction.substring(0,_idaction.length-1);
					location.replace(_idaction);
				}catch(e){
				}
			}
		}
		try{
			beforeClickJs();


			if(parent.frames["content"])
				parent.frames["content"].beforeClickJs();
		}catch(e){
		}
		return;

}

function prepareUrlString(action,wac,service_parent,service_child,service_id){
	var url="";
	if(wac){
		if(wac!="null") url+="wac="+wac;
	}
	if(service_parent){
		if(service_parent!="null") url+= ((url=="")?"":"&")+ "$parent_pointOfLaunch="+service_parent;
	}
	if(service_child){
		if(service_child!="null") url+= ((url=="")?"":"&")+ "$child_pointOfReturn="+service_child;
	}
	if(service_id){
		if(service_id!="null") url+= ((url=="")?"":"&")+ "$id_pointOfService="+service_id;
	}
	return url;
}

function enable_disable_element(id_element,value){
//document.getElementById(id_element).type.toUpperCase().indexOf("SELECT")==0
	try{
		if(value==true){

			if(document.getElementById(id_element)){

				if(	document.getElementById(id_element).type.toUpperCase()=="CHECKBOX" ||
					document.getElementById(id_element).type.toUpperCase()=="RADIO" ||
					document.getElementById(id_element).type.toUpperCase()=="BUTTON" ||
					document.getElementById(id_element).type.toUpperCase().indexOf("SELECT")==0

				){

					document.getElementById(id_element).disabled=true;
					if(document.getElementById(id_element).type.toUpperCase()=="RADIO"){
						var radioGroup = document.getElementById(id_element);
						if(radioGroup.length){
							for (var b = 0; b < radioGroup.length; b++)
								radioGroup[b].disabled = true;

						}
					}
				}else{
					document.getElementById(id_element).readOnly=true;
					document.getElementById(id_element).value="";

				}

				if(document.getElementById(id_element).type.toUpperCase()!="BUTTON"){
					if(document.getElementById(id_element).className.indexOf("dis")==-1)
						document.getElementById(id_element).className = document.getElementById(id_element).className + "dis";
				}
			}
		}
		if(value==false){
			if(document.getElementById(id_element)){
				if(	document.getElementById(id_element).type.toUpperCase()=="CHECKBOX" ||
					document.getElementById(id_element).type.toUpperCase()=="RADIO" ||
					document.getElementById(id_element).type.toUpperCase()=="BUTTON"||
					document.getElementById(id_element).type.toUpperCase().indexOf("SELECT")==0
				){
					document.getElementById(id_element).disabled=false;
					if(document.getElementById(id_element).type.toUpperCase()=="RADIO"){
					}
				}else{
					document.getElementById(id_element).readOnly=false;
				}
				if(document.getElementById(id_element).type.toUpperCase()!="BUTTON")
					document.getElementById(id_element).className = document.getElementById(id_element).className.replace("dis","");

			}
		}
	}catch(e){
	}
}

function enable_disable_elementNotBlank(id_element,value){
//document.getElementById(id_element).type.toUpperCase().indexOf("SELECT")==0
	try{
		if(value==true){

			if(document.getElementById(id_element)){

				if(	document.getElementById(id_element).type.toUpperCase()=="CHECKBOX" ||
					document.getElementById(id_element).type.toUpperCase()=="RADIO" ||
					document.getElementById(id_element).type.toUpperCase()=="BUTTON" ||
					document.getElementById(id_element).type.toUpperCase().indexOf("SELECT")==0

				){

					document.getElementById(id_element).disabled=true;
					if(document.getElementById(id_element).type.toUpperCase()=="RADIO"){
						var radioGroup = document.getElementById(id_element);
						if(radioGroup.length){
							for (var b = 0; b < radioGroup.length; b++)
								radioGroup[b].disabled = true;

						}
					}
				}else{
					document.getElementById(id_element).readOnly=true;
				}

				if(document.getElementById(id_element).type.toUpperCase()!="BUTTON"){
					var curClassName = document.getElementById(id_element).className;
					if(curClassName.indexOf("red")>0 && (curClassName.indexOf("red")+3)==curClassName.length){
						curClassName=curClassName.replace("red","");
					}
					if(curClassName.indexOf("dis")==-1){
						document.getElementById(id_element).className = curClassName + "dis";
					}
				}
			}
		}
		if(value==false){
			if(document.getElementById(id_element)){
				if(	document.getElementById(id_element).type.toUpperCase()=="CHECKBOX" ||
					document.getElementById(id_element).type.toUpperCase()=="RADIO" ||
					document.getElementById(id_element).type.toUpperCase()=="BUTTON"||
					document.getElementById(id_element).type.toUpperCase().indexOf("SELECT")==0
				){
					document.getElementById(id_element).disabled=false;
					if(document.getElementById(id_element).type.toUpperCase()=="RADIO"){
					}
				}else{
					document.getElementById(id_element).readOnly=false;
				}
				if(document.getElementById(id_element).type.toUpperCase()!="BUTTON")
					document.getElementById(id_element).className = document.getElementById(id_element).className.replace("dis","");

			}
		}
	}catch(e){
	}
}


function actionClearAll(){
	var frm = document.forms[0];
	for(i=0;i<frm.elements.length;i++){
		if(frm.elements[i].name){
			if(notClear.indexOf(";"+frm.elements[i].name+";")==-1){
				if(frm.elements[i].selectedIndex){
					try{
						frm.elements[i].selectedIndex=0;
						frm.elements[i].value=frm.elements[i][frm.elements[i].selectedIndex].value;
					}catch(e){
					}
				}else{
					try{
						if(	frm.elements[i].type.toUpperCase()=="TEXT" ||
							frm.elements[i].type.toUpperCase()=="CHECKBOX" ||
							frm.elements[i].type.toUpperCase()=="RADIO")
								frm.elements[i].value="";

						if(	frm.elements[i].type.toUpperCase()=="CHECKBOX" ||
							frm.elements[i].type.toUpperCase()=="RADIO")
							frm.elements[i].checked=false;

						if(	frm.elements[i].type.toUpperCase()=="HIDDEN" &&
							frm.elements[i].name.indexOf("type_search_")==0)
								frm.elements[i].value="-1";

					}catch(e){
					}
				}
			}
		}
	}
}

function actionClearAllInForm(formname){
	var frm = document.getElementById(formname);
	try{
		for(i=0;i<frm.elements.length;i++){
			if(frm.elements[i].name){
				if(notClear.indexOf(";"+frm.elements[i].name+";")==-1){
					if(frm.elements[i].selectedIndex){
						try{
							frm.elements[i].selectedIndex=0;
							frm.elements[i].value=frm.elements[i][frm.elements[i].selectedIndex].value;
						}catch(e){
						}
					}else{
						try{
							if(	frm.elements[i].type.toUpperCase()=="TEXT" ||
								frm.elements[i].type.toUpperCase()=="CHECKBOX" ||
								frm.elements[i].type.toUpperCase()=="RADIO")
									frm.elements[i].value="";

							if(	frm.elements[i].type.toUpperCase()=="CHECKBOX" ||
								frm.elements[i].type.toUpperCase()=="RADIO")
								frm.elements[i].checked=false;

							if(	frm.elements[i].type.toUpperCase()=="HIDDEN" &&
								frm.elements[i].name.indexOf("type_search_")==0)
									frm.elements[i].value="-1";

						}catch(e){
						}
					}
				}
			}
		}
	}catch(e){
	}
}

function goRowsOnPage(row){
	try{


		document.forms[0].middleAction.value="reload";
		var url_action = document.forms[0].action;
		if(url_action.indexOf('?')==-1)
			url_action=url_action+"?rows_on_page="+row;
		else
			url_action=url_action+"&rows_on_page="+row;

		document.forms[0].action = url_action;
		if(document.forms[0].current_page){
			document.forms[0].current_page.value=1;
			document.forms[0].submit();
		}else{
			document.getElementById("current_page").value=1;
			if(document.forms[0].action.indexOf("?")>-1)
				document.forms[0].action=document.forms[0].action+"&current_page="+document.getElementById("current_page").value;
			else
				document.forms[0].action=document.forms[0].action+"?current_page="+document.getElementById("current_page").value;
			document.forms[0].submit();
		}

		beforeClickJs();

	}catch(e){
	}
	try{
		if(parent.frames["content"])
			parent.frames["content"].beforeClickJs();
	}catch(e){
	}

}

function goPage(add){
	try{
		document.forms[0].middleAction.value="page";
		
		if(document.forms[0].current_page){
			document.forms[0].current_page.value=document.forms[0].current_page.value/1+add/1;
			document.forms[0].submit();
		}else{	
			document.getElementById("current_page").value=(document.getElementById("current_page").value/1+add/1);
			if(document.forms[0].action.indexOf("?")>-1)
				document.forms[0].action=document.forms[0].action+"&current_page="+document.getElementById("current_page").value;
			else
				document.forms[0].action=document.forms[0].action+"?current_page="+document.getElementById("current_page").value;
			document.forms[0].submit();
		}
		beforeClickJs();

	}catch(e){
	}
	try{
		if(parent.frames["content"])
			parent.frames["content"].beforeClickJs();
	}catch(e){
	}

}

function goPageN(page){
	try{
		document.forms[0].middleAction.value="page";
		
		if(document.forms[0].current_page){
			document.forms[0].current_page.value=page;

			document.forms[0].submit();
		}else{
			document.getElementById("current_page").value=page;
			if(document.forms[0].action.indexOf("?")>-1)
				document.forms[0].action=document.forms[0].action+"&current_page="+document.getElementById("current_page").value;
			else
				document.forms[0].action=document.forms[0].action+"?current_page="+document.getElementById("current_page").value;
			document.forms[0].submit();
		}
		beforeClickJs();

	}catch(e){
	}
	try{
		if(parent.frames["content"])
			parent.frames["content"].beforeClickJs();
	}catch(e){
	}

}

function goPage_ajax(frmName,add,afterJSFunction,target){
	document.getElementById(frmName).middleAction.value="page";
	document.getElementById(frmName).current_page.value=(document.getElementById(frmName).current_page.value/1+add/1);
	if(target){
		if(target!="")
			ajax_submit(document.getElementById(frmName),target,afterJSFunction);
		else	
			ajax_submit(document.getElementById(frmName),'content_body_Panel_Popup',afterJSFunction);
	}else	
		ajax_submit(document.getElementById(frmName),'content_body_Panel_Popup',afterJSFunction);
}

function goPageN_ajax(frmName,page,afterJSFunction,target){
	document.getElementById(frmName).middleAction.value="page";
	document.getElementById(frmName).current_page.value=page;
	if(target){
		if(target!="")
			ajax_submit(document.getElementById(frmName),target,afterJSFunction);
		else ajax_submit(document.getElementById(frmName),'content_body_Panel_Popup',afterJSFunction);
	}else	
		ajax_submit(document.getElementById(frmName),'content_body_Panel_Popup',afterJSFunction);
}



function goViewExtend(){
	var list_of_div = document.getElementsByTagName("div");
	var obj_div;
	for(var i=0;i<list_of_div.length;i++){
		if(list_of_div[i].id.indexOf("div_formBean_")==0)
			obj_div=list_of_div[i];
	}
	if(obj_div){
		try{
			
			if(document.forms[0].current_div_width){
				document.forms[0].current_div_width.value=obj_div.style.width;
			}else{
				document.getElementById("current_div_width").value=obj_div.style.width;
				if(document.forms[0].action.indexOf("?")>-1){
					if(document.forms[0].action.indexOf("current_div_width")>-1)
						document.forms[0].action = document.forms[0].action.replace(/(current_div_width=)[^\&]+/, '$1' + document.getElementById("current_div_width").value)
					else					
						document.forms[0].action=document.forms[0].action+"&current_div_width="+document.getElementById("current_div_width").value;
				}else
					document.forms[0].action=document.forms[0].action+"?current_div_width="+document.getElementById("current_div_width").value;	
				
			}

			obj_div.style.width="100%";
			document.getElementById("button_view_extend").style.display="none";
			document.getElementById("button_view_reduce").style.display="block";
		}catch(e){
		}
	}

}

function goViewReduce(){
	var list_of_div = document.getElementsByTagName("div");
	var obj_div;
	for(var i=0;i<list_of_div.length;i++){
		if(list_of_div[i].id.indexOf("div_formBean_")==0)
			obj_div=list_of_div[i];
	}
	if(obj_div){
		try{
			
			if(document.forms[0].current_div_width){
				obj_div.style.width=document.forms[0].current_div_width.value;				
				document.forms[0].current_div_width.value="";			
			}else{
				obj_div.style.width=document.getElementById("current_div_width").value;
				document.getElementById("current_div_width").value="";				
				if(document.forms[0].action.indexOf("?")>-1){
					if(document.forms[0].action.indexOf("current_div_width")>-1)
						document.forms[0].action = document.forms[0].action.replace(/(current_div_width=)[^\&]+/, '$1' + document.getElementById("current_div_width").value)
					else					
						document.forms[0].action=document.forms[0].action+"&current_div_width="+document.getElementById("current_div_width").value;
				}else
					document.forms[0].action=document.forms[0].action+"?current_div_width=";		
			}				

			document.getElementById("button_view_extend").style.display="block";
			document.getElementById("button_view_reduce").style.display="none";
		}catch(e){
		}
	}

}


function ShowHideSections(secname){
	try{
		var section = document.getElementById("section_"+secname);
		var obj_img = document.getElementById("img_"+secname);
		if(section.style.display=='block'){
			section.style.display="none";
			obj_img.src="images/menu/13_close.gif"
		}else{
			section.style.display="block";
			obj_img.src="images/menu/13_open.gif"
		}

	}catch(e){
	}
}

function ShowHidePageTab(selid, ptabname){



	var goNext=true;
	var currentid=0;
	while(goNext){
		try{
			var obj = document.getElementById("page_tab_"+currentid+"_"+ptabname);
			var obj_menu = document.getElementById("page_tab_menu_"+currentid+"_"+ptabname);
			if(currentid==selid){
				obj.style.display="block";
				obj_menu.className="page_tab_section";
			}else{
				obj.style.display="none";
				obj_menu.className="page_tab_sectiondis";
			}
			currentid++;
		}catch(e){
			goNext=false;
		}
	}


}


function draw_div(id){

	try{
		if (document.getElementById(id).style.display=="block" ||
		    document.getElementById(id).style.display==""){
			document.getElementById(id).style.display="none";
			document.getElementById(id+"_img").src="images/menu/closed.gif";
			document.getElementById("show_"+id).value="0";

		}else{
			document.getElementById(id).style.display="block";
			document.getElementById(id+"_img").src="images/menu/opened.gif";
			document.getElementById("show_"+id).value="1";
		}
	}catch(e){
	}
}

function draw_div_p(id,type){
	try{
		document.getElementById(id).style.display=type;
		if(type=="none"){
			document.getElementById(id+"_img").src="images/menu/closed.gif";
			document.getElementById("show_"+id).value="0";
		}
		if(type=="block"){
			document.getElementById(id+"_img").src="images/menu/opened.gif";
			document.getElementById("show_"+id).value="1";
		}
	}catch(e){
	}
}

function draw_tabs_div(id,bool,height,prefix){

	var pref="";
	if(prefix)
		pref=prefix;
	var go=true;
	var i=0;
	while(go){
		var tab = document.getElementById(pref+"page_tab_"+i);
		if(tab){
			if(i==id){
				if(document.getElementById(pref+"div_page_tab_"+id)){
					if (bool==false){
						document.getElementById(pref+"div_page_tab_"+id).style.display="none";
						tab.className="page_tab_sectiondis";
					}else{
						if(height){
							if(height>-1)
								document.getElementById(pref+"div_page_tab_"+id).style.height=height;
						}
						document.getElementById(pref+"div_page_tab_"+id).style.display="block";
						try{
							if(document.getElementById("page_tab"))
								document.getElementById("page_tab").value=id;
						}catch(e){
						}
						tab.className="page_tab_section";
					}
				}
			}else{
				document.getElementById(pref+"div_page_tab_"+i).style.display="none";
				tab.className="page_tab_sectiondis";
			}
			i++;
		}else go=false;
	}

}

var MONTH_NAMES=new Array('January','February','March','April','May','June','July','August','September','October','November','December','Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec');
var DAY_NAMES=new Array('Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sun','Mon','Tue','Wed','Thu','Fri','Sat');
function LZ(x) {return(x<0||x>9?"":"0")+x}

function isDate(val,format) {
	var date=getDateFromFormat(val,format);
	if (date==0) { return false; }
	return true;
	}


function formatDate(date,format) {
	format=format+"";
	var result="";
	var i_format=0;
	var c="";
	var token="";
	var y=date.getYear()+"";
	var M=date.getMonth()+1;
	var d=date.getDate();
	var E=date.getDay();
	var H=date.getHours();
	var m=date.getMinutes();
	var s=date.getSeconds();
	var yyyy,yy,MMM,MM,dd,hh,h,mm,ss,ampm,HH,H,KK,K,kk,k;
	// Convert real date parts into formatted versions
	var value=new Object();
	if (y.length < 4) {y=""+(y-0+1900);}
	value["y"]=""+y;
	value["yyyy"]=y;
	value["yy"]=y.substring(2,4);
	value["M"]=M;
	value["MM"]=LZ(M);
	value["MMM"]=MONTH_NAMES[M-1];
	value["NNN"]=MONTH_NAMES[M+11];
	value["d"]=d;
	value["dd"]=LZ(d);
	value["E"]=DAY_NAMES[E+7];
	value["EE"]=DAY_NAMES[E];
	value["H"]=H;
	value["HH"]=LZ(H);
	if (H==0){value["h"]=12;}
	else if (H>12){value["h"]=H-12;}
	else {value["h"]=H;}
	value["hh"]=LZ(value["h"]);
	if (H>11){value["K"]=H-12;} else {value["K"]=H;}
	value["k"]=H+1;
	value["KK"]=LZ(value["K"]);
	value["kk"]=LZ(value["k"]);
	if (H > 11) { value["a"]="PM"; }
	else { value["a"]="AM"; }
	value["m"]=m;
	value["mm"]=LZ(m);
	value["s"]=s;
	value["ss"]=LZ(s);
	while (i_format < format.length) {
		c=format.charAt(i_format);
		token="";
		while ((format.charAt(i_format)==c) && (i_format < format.length)) {
			token += format.charAt(i_format++);
			}
		if (value[token] != null) { result=result + value[token]; }
		else { result=result + token; }
		}
	return result;
}

function getDateFromFormat(val,format) {

	val=val+"";
	format=format+"";
	var i_val=0;
	var i_format=0;
	var c="";
	var token="";
	var token2="";
	var x,y;
	var now=new Date();
	var year=now.getYear();
	var month=now.getMonth()+1;
	var date=1;
	var hh=now.getHours();
	var mm=now.getMinutes();
	var ss=now.getSeconds();
	var ampm="";

	while (i_format < format.length) {
		// Get next token from format string
		c=format.charAt(i_format);
		token="";
		while ((format.charAt(i_format)==c) && (i_format < format.length)) {
			token += format.charAt(i_format++);
			}
		// Extract contents of value based on format token
		if (token=="yyyy" || token=="yy" || token=="y") {
			if (token=="yyyy") { x=4;y=4; }
			if (token=="yy")   { x=2;y=2; }
			if (token=="y")    { x=2;y=4; }
			year=_getInt(val,i_val,x,y);
			if (year==null) { return 0; }
			i_val += year.length;
			if (year.length==2) {
				if (year > 70) { year=1900+(year-0); }
				else { year=2000+(year-0); }
				}
			}
		else if (token=="MMM"||token=="NNN"){
			month=0;
			for (var i=0; i<MONTH_NAMES.length; i++) {
				var month_name=MONTH_NAMES[i];
				if (val.substring(i_val,i_val+month_name.length).toLowerCase()==month_name.toLowerCase()) {
					if (token=="MMM"||(token=="NNN"&&i>11)) {
						month=i+1;
						if (month>12) { month -= 12; }
						i_val += month_name.length;
						break;
						}
					}
				}
			if ((month < 1)||(month>12)){return 0;}
			}
		else if (token=="EE"||token=="E"){
			for (var i=0; i<DAY_NAMES.length; i++) {
				var day_name=DAY_NAMES[i];
				if (val.substring(i_val,i_val+day_name.length).toLowerCase()==day_name.toLowerCase()) {
					i_val += day_name.length;
					break;
					}
				}
			}
		else if (token=="MM"||token=="M") {
			month=_getInt(val,i_val,token.length,2);
			if(month==null||(month<1)||(month>12)){return 0;}
			i_val+=month.length;}
		else if (token=="dd"||token=="d") {
			date=_getInt(val,i_val,token.length,2);
			if(date==null||(date<1)||(date>31)){return 0;}
			i_val+=date.length;}
		else if (token=="hh"||token=="h") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<1)||(hh>12)){return 0;}
			i_val+=hh.length;}
		else if (token=="HH"||token=="H") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<0)||(hh>23)){return 0;}
			i_val+=hh.length;}
		else if (token=="KK"||token=="K") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<0)||(hh>11)){return 0;}
			i_val+=hh.length;}
		else if (token=="kk"||token=="k") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<1)||(hh>24)){return 0;}
			i_val+=hh.length;hh--;}
		else if (token=="mm"||token=="m") {
			mm=_getInt(val,i_val,token.length,2);
			if(mm==null||(mm<0)||(mm>59)){return 0;}
			i_val+=mm.length;}
		else if (token=="ss"||token=="s") {
			ss=_getInt(val,i_val,token.length,2);
			if(ss==null||(ss<0)||(ss>59)){return 0;}
			i_val+=ss.length;}
		else if (token=="a") {
			if (val.substring(i_val,i_val+2).toLowerCase()=="am") {ampm="AM";}
			else if (val.substring(i_val,i_val+2).toLowerCase()=="pm") {ampm="PM";}
			else {return 0;}
			i_val+=2;}
		else {
			if (val.substring(i_val,i_val+token.length)!=token) {return 0;}
			else {i_val+=token.length;}
			}
		}
	// If there are any trailing characters left in the value, it doesn't match
	if (i_val != val.length) { return 0; }
	// Is date valid for month?
	if (month==2) {
		// Check for leap year
		if ( ( (year%4==0)&&(year%100 != 0) ) || (year%400==0) ) { // leap year
			if (date > 29){ return 0; }
			}
		else { if (date > 28) { return 0; } }
		}
	if ((month==4)||(month==6)||(month==9)||(month==11)) {
		if (date > 30) { return 0; }
		}
	// Correct hours value
	if (hh<12 && ampm=="PM") { hh=hh-0+12; }
	else if (hh>11 && ampm=="AM") { hh-=12; }
	var newdate=new Date(year,month-1,date,hh,mm,ss);
	return newdate.getTime();
}
function _isInteger(val) {
	var digits="1234567890";
	for (var i=0; i < val.length; i++) {
		if (digits.indexOf(val.charAt(i))==-1) { return false; }
		}
	return true;
}
function _getInt(str,i,minlength,maxlength) {
	for (var x=maxlength; x>=minlength; x--) {
		var token=str.substring(i,i+x);
		if (token.length < minlength) { return null; }
		if (_isInteger(token)) { return token; }
		}
	return null;
}

function nlist_over(obj){

	try{

		var div_obj = document.getElementById("navigation_label");

		if(div_obj.style.visibility=="visible") return;

		var img_obj = document.getElementById("navigation_label_img");
		var span_obj = document.getElementById("navigation_label_span");
		var docPos    = getPosition(obj);
		div_obj.style.visibility="visible";
		div_obj.style_width=obj.width;
		span_obj.innerHTML = obj.getAttribute("desc");
		img_obj.src = obj.src;
		div_obj.onclick = obj.onclick;
		div_obj.style.top = (docPos.y)-6;
		div_obj.style.left	= docPos.x-5;
	}catch(e){
	}

}

function nlist_out(obj){
	try{
		var div_obj = document.getElementById("navigation_label");

		div_obj.style.visibility="hidden";
	}catch(e){
	}

}


function getPosition(e){
	var left = 0;
	var top  = 0;

	while (e.offsetParent){
		left += e.offsetLeft;
		top  += e.offsetTop;
		e     = e.offsetParent;
	}
	left += e.offsetLeft;
	top  += e.offsetTop;
	return {x:left, y:top};
}


function showAsIFrame(action,panel_width,panel_height,scroll){



	if(document.getElementById("content_body_IFrame_Popup")){
	}else{
		var tmp_container = document.createElement("div");
		tmp_container.id="tmp_container";
		document.body.appendChild(tmp_container);

		if(document.getElementById("tmp_container")){
			ajax_makeRequest(	"popup?type=iframe",
								"tmp_container",
								function(http_request,target){
									if(http_request.status == 200){
										showAsIFrame(action,panel_width,panel_height,scroll);
									}else{
										ajax_makeRequest(	
												"jsp/included/iframe_popup.jsp",
												"tmp_container",
												function(){
													showAsIFrame(action,panel_width,panel_height,scroll);
												},
												"",
												false		
										);										
									}
								},
								"",
								false
			);
		}
		return;
	}

	try{
		document.getElementById("content_body_IFrame_Popup").style.width=panel_width;
	}catch(e){
	}
	try{
		document.getElementById("content_body_IFrame_Popup").style.height=panel_height;
	}catch(e){
	}

	try{
		if(scroll==true){
			document.getElementById("content_body_IFrame_Popup").style.overflowY="scroll";
		}
	}catch(e){
	}
	if(IE4){
		document.getElementById("content_IFrame_Popup").style.top=document.body.scrollTop;
		document.getElementById("content_IFrame_Popup").style.display="block";
	}else{
		document.getElementById("content_IFrame_Popup").style.position="fixed";
	}
	document.getElementById("content_IFrame_Popup").style.display="block";	



	ajustPanel();

	try{
		var iframe_element = window.frames['content_body_IFrame_Popup'];
		iframe_element.document.open();
		iframe_element.document.close();
	}catch(e){
	}
	document.getElementById("content_body_IFrame_Popup").src="jsp/scroll.html";
	try{

		document.getElementById('content_body_IFrame_Popup').src=action;

	}catch(e){

	}

	try{
		document.getElementById("panel_IFrame_img_maximize").style.display="block";
		document.getElementById("panel_IFrame_img_minimize").style.display="none";
	}catch(e){
	}
	var vPanel_Popup = mPanel_Popup.get("panel_IFrame");
	if(vPanel_Popup==null){

		var vPanel_Popup = new objPopup();

		vPanel_Popup.width=document.body.offsetWidth;
		vPanel_Popup.height=document.body.offsetHeight;
		vPanel_Popup.maximize=false;
		
		hideScrollPanel(vPanel_Popup);

		mPanel_Popup.put("panel_IFrame", vPanel_Popup);

	}else{
		if(vPanel_Popup.maximize==true){
			showAsPanelMax("panel_IFrame");
		}
//		else showAsPanelNormal("panel_IFrame");
	}

}

function ajustPopupOnScroll(){
	try{
		document.getElementById("content_Panel_Popup").style.top=document.body.scrollTop;
		document.getElementById("content_Panel_Popup").style.left=document.body.scrollLeft;
	}catch(e){
	}
}


function downloadAsPopupSimple(action,img_wait){
	try{
		if(Blob && isIE()!=8 && isIE()!=9 && Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor')==-1){
			downloadAsPopup(action, 400,300,false,
				function(http_request,target){
					try{
						closePanel('panel_Popup');
						document.getElementById('content_Panel_Popup').style.display='none';
					}catch(e){};
				},
				img_wait
			);
		}else{
			location.replace(action, "report");
		}
	}catch(e){
		location.replace(action, "report");
	}	

	return;	
}

function downloadAsPopup(action,panel_width,panel_height,scroll,afterJSFunction,img_wait){
	showAsPopup(action, panel_width,panel_height,scroll,
			afterJSFunction,
			function(http_request,target){

				if(document.getElementById('content_Panel_Popup').style.display!="none"){
					try{
						var filename = "";
				        var disposition = http_request.getResponseHeader('Content-Disposition');
				        if (disposition && disposition.indexOf('attachment') !== -1) {
				            var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
				            var matches = filenameRegex.exec(disposition);
				            if (matches != null && matches[1]) filename = matches[1].replace(/['"]/g, '');
				        }
		
				        var type = http_request.getResponseHeader('Content-Type');
				        var blob;
				        if(http_request.response)
				        	blob = new Blob([http_request.response], { type: type });
				        else 
				        	blob = new Blob([http_request.responseText], { type: type });
				        
				        if (typeof window.navigator.msSaveOrOpenBlob !== 'undefined') {
				            // IE workaround for "HTML7007: One or more blob URLs were revoked by closing the blob for which they were created. These URLs will no longer resolve as the data backing the URL has been freed."
	//			            window.navigator.msSaveBlob(blob, filename);
				            window.navigator.msSaveOrOpenBlob(blob, filename);
				        }else if (typeof window.navigator.msSaveBlob !== 'undefined') {
				        	window.navigator.msSaveBlob(blob, filename);
				        } else {
				            var URL = window.URL || window.webkitURL;
				            var downloadUrl = URL.createObjectURL(blob);
		
				            if (filename) {
				                // use HTML5 a[download] attribute to specify filename
				                var a = document.createElement("a");
				                // safari doesn't support this yet
				                if (typeof a.download === 'undefined') {
				                    window.location = downloadUrl;
				                } else {
				                    a.href = downloadUrl;
				                    a.download = filename;
				                    document.body.appendChild(a);
				                    a.click();
				                }
				            } else {
				                window.location = downloadUrl;
				            }
		
				            setTimeout(function () { URL.revokeObjectURL(downloadUrl); }, 100); // cleanup
				        }
					}catch(e){
						location.replace(action);
						try{
							closePanel('panel_Popup');
							document.getElementById('content_Panel_Popup').style.display='none';
						}catch(e){};
					}
				}
			},
			"arraybuffer"
		);
	if(document.getElementById("content_body_Panel_Popup")){
		if(img_wait){
			try{
				document.getElementById("img_ajax_makeRequest").src=img_wait;
			}catch(e){
			}
		}
	}else{
		window.setTimeout(
				function(){
					if(img_wait){
						try{
							document.getElementById("img_ajax_makeRequest").src=img_wait;
						}catch(e){
						}
					}
				},
				1000
			);		
	}
}

function showAsPopup(action,panel_width,panel_height,scroll,afterJSFunction,redrawTargetJSFunction,responseType){


	if(document.getElementById("content_body_Panel_Popup")){
	}else{
		var tmp_container = document.createElement("div");
		tmp_container.id="tmp_container";
		document.body.appendChild(tmp_container);

		if(document.getElementById("tmp_container")){
			ajax_makeRequest(	"popup?type=page",
								"tmp_container",
								function(http_request,target){
									if(http_request.status == 200){
										showAsPopup(action,panel_width,panel_height,scroll,afterJSFunction,redrawTargetJSFunction,responseType);
									}else{
										ajax_makeRequest(	
												"jsp/included/page_popup.jsp",
												"tmp_container",
												function(http_request,target){
													showAsPopup(action,panel_width,panel_height,scroll,afterJSFunction,redrawTargetJSFunction,responseType);
												},
												"",
												false		
										);
									}								
								},
								"",
								false
			);
		}
		return;
	}

	try{
		document.getElementById("content_body_Panel_Popup").style.width=panel_width;
	}catch(e){
	}
	try{
		document.getElementById("content_body_Panel_Popup").style.height=panel_height;
	}catch(e){
	}

	try{
		if(scroll==true){
			document.getElementById("content_body_Panel_Popup").style.overflowY="scroll";
		}
	}catch(e){
	}

	if(IE4){
		document.getElementById("content_Panel_Popup").style.top=document.body.scrollTop;
		document.getElementById("content_Panel_Popup").style.left=document.body.scrollLeft;
	}else{
		document.getElementById("content_Panel_Popup").style.position="fixed";
	}
	
	document.getElementById("content_Panel_Popup").style.display="block";


	try{
		if(vPanel_NotPopup.zIndex>-1){
			document.getElementById("content_Panel_Popup").style.zIndex=(vPanel_NotPopup.zIndex+1)/1;
//			document.getElementById("content_Panel_Popup").style.position="absolute";
		}
	}catch(e){
	}



	ajustPanel();
	if(action!=""){
		try{
			if(afterJSFunction){
				ajax_makeRequest(action,"content_body_Panel_Popup",afterJSFunction,redrawTargetJSFunction,true,responseType);
			}else{	
				ajax_makeRequest(action,"content_body_Panel_Popup","JSAfter_showAsPopup",redrawTargetJSFunction,true,responseType);
			}
		}catch(e){
		}
	}

	try{
		document.getElementById("panel_Popup_img_maximize").style.display="block";
		document.getElementById("panel_Popup_img_minimize").style.display="none";
	}catch(e){
	}

	var vPanel_Popup = mPanel_Popup.get("panel_Popup");

	if(vPanel_Popup==null){

		var vPanel_Popup = new objPopup();
		vPanel_Popup.width=document.body.offsetWidth;
		vPanel_Popup.height=document.body.offsetHeight;
		vPanel_Popup.maximize=false;
		
		hideScrollPanel(vPanel_Popup);

		mPanel_Popup.put("panel_Popup", vPanel_Popup);

		ajustPanel();

	}else{
		if(vPanel_Popup.maximize==true){
			showAsPanelMax("panel_Popup");
		}
//		else showAsPanelNormal("panel_Popup");
		ajustPanel();
	}


}


function showAsPopupSubmit(formName,middle,panel_width,panel_height,scroll,afterJSFunction,redrawTargetJSFunction,responseType){

	if(document.getElementById("content_body_Panel_Popup")){
	}else{
		var tmp_container = document.createElement("div");
		tmp_container.id="tmp_container";
		document.body.appendChild(tmp_container);

		if(document.getElementById("tmp_container")){
			ajax_makeRequest(	"popup?type=page",
								"tmp_container",
								function(http_request,target){
									if(http_request.status == 200){
										showAsPopupSubmit(formName,middle,panel_width,panel_height,scroll,afterJSFunction,redrawTargetJSFunction,responseType);
									}else{
										ajax_makeRequest(	
												"jsp/included/page_popup.jsp",
												"tmp_container",
												function(){
													showAsPopupSubmit(formName,middle,panel_width,panel_height,scroll,afterJSFunction,redrawTargetJSFunction,responseType);
												},
												"",
												false		
										);
									}
								},
								"",
								false
			);
		}
		return;
	}

	try{
		document.getElementById("content_body_Panel_Popup").style.width=panel_width;
	}catch(e){
	}
	try{
		document.getElementById("content_body_Panel_Popup").style.height=panel_height;
	}catch(e){
	}
	try{
		if(scroll==true){
			document.getElementById("content_body_Panel_Popup").style.overflowY="scroll";
		}
	}catch(e){
	}
	if(IE4){
		document.getElementById("content_Panel_Popup").style.top=document.body.scrollTop;
		document.getElementById("content_Panel_Popup").style.left=document.body.scrollLeft;
	}else{
		document.getElementById("content_Panel_Popup").style.position="fixed";
	}
	document.getElementById("content_Panel_Popup").style.display="block";

	try{
		if(vPanel_NotPopup.zIndex>-1){
			document.getElementById("content_Panel_Popup").style.zIndex=(vPanel_NotPopup.zIndex+1)/1;
//			document.getElementById("content_Panel_Popup").style.position="absolute";
		}
	}catch(e){
	}


	ajustPanel();
	try{
		document.getElementById(formName).middleAction.value=middle;
		if(afterJSFunction){
			ajax_submit(document.getElementById(formName),"content_body_Panel_Popup",afterJSFunction,redrawTargetJSFunction,responseType);
		}else{	
			ajax_submit(document.getElementById(formName),"content_body_Panel_Popup","JSAfter_showAsPopup",redrawTargetJSFunction,responseType);
		}
	}catch(e){
//		alert(e);
	}




	var vPanel_Popup = mPanel_Popup.get("panel_Popup");

	if(vPanel_Popup==null){

		var vPanel_Popup = new objPopup();
		vPanel_Popup.width=document.body.offsetWidth;
		vPanel_Popup.height=document.body.offsetHeight;
		vPanel_Popup.maximize=false;
		
		hideScrollPanel(vPanel_Popup);

		mPanel_Popup.put("panel_Popup", vPanel_Popup);

		ajustPanel();

	}else{
		if(vPanel_Popup.maximize==true){
			showAsPanelMax("panel_Popup");
		}
//		else showAsPanelNormal("panel_Popup");
		ajustPanel();
	}

}

function showAsPopupSubmitExt(formName,action,panel_width,panel_height,scroll,afterJSFunction,redrawTargetJSFunction,responseType){

	if(document.getElementById("content_body_Panel_Popup")){
	}else{
		var tmp_container = document.createElement("div");
		tmp_container.id="tmp_container";
		document.body.appendChild(tmp_container);

		if(document.getElementById("tmp_container")){
			ajax_makeRequest(	"popup?type=page",
								"tmp_container",
								function(http_request,target){
									if(http_request.status == 200){
										showAsPopupSubmitExt(formName,action,panel_width,panel_height,scroll,afterJSFunction,redrawTargetJSFunction,responseType);
									}else{
										ajax_makeRequest(	
												"jsp/included/page_popup.jsp",
												"tmp_container",
												function(){
													showAsPopupSubmitExt(formName,action,panel_width,panel_height,scroll,afterJSFunction,redrawTargetJSFunction,responseType);
												},
												"",
												false		
										);
									}
								},
								"",
								false
			);
		}
		return;
	}

	try{
		document.getElementById("content_body_Panel_Popup").style.width=panel_width;
	}catch(e){
	}
	try{
		document.getElementById("content_body_Panel_Popup").style.height=panel_height;
	}catch(e){
	}
	try{
		if(scroll==true){
			document.getElementById("content_body_Panel_Popup").style.overflowY="scroll";
		}
	}catch(e){
	}
	if(IE4){
		document.getElementById("content_Panel_Popup").style.top=document.body.scrollTop;
		document.getElementById("content_Panel_Popup").style.left=document.body.scrollLeft;
	}else{
		document.getElementById("content_Panel_Popup").style.position="fixed";
	}
	document.getElementById("content_Panel_Popup").style.display="block";

	try{
		if(vPanel_NotPopup.zIndex>-1){
			document.getElementById("content_Panel_Popup").style.zIndex=(vPanel_NotPopup.zIndex+1)/1;
//			document.getElementById("content_Panel_Popup").style.position="absolute";
		}
	}catch(e){
	}


	ajustPanel();
	try{
		if(afterJSFunction){
			ajax_submitExt(document.getElementById(formName),action,"content_body_Panel_Popup",afterJSFunction,redrawTargetJSFunction,responseType);
		}else{
			ajax_submitExt(document.getElementById(formName),action,"content_body_Panel_Popup","JSAfter_showAsPopup",redrawTargetJSFunction,responseType);
		}
	}catch(e){
//		alert(e);
	}

	var vPanel_Popup = mPanel_Popup.get("panel_Popup");

	if(vPanel_Popup==null){

		var vPanel_Popup = new objPopup();
		vPanel_Popup.width=document.body.offsetWidth;
		vPanel_Popup.height=document.body.offsetHeight;
		vPanel_Popup.maximize=false;
		
		hideScrollPanel(vPanel_Popup);

		mPanel_Popup.put("panel_Popup", vPanel_Popup);

		ajustPanel();

	}else{
		if(vPanel_Popup.maximize==true){
			showAsPanelMax("panel_Popup");
		}
//		else showAsPanelNormal("panel_Popup");
		ajustPanel();
	}

}


//JSON

function downloadAsPopup_jsonSimple(action,img_wait){
	try{
		if(Blob && isIE()!=8 && isIE()!=9 && Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor')==-1){
			downloadAsPopup_json(action, 400,300,false,
				function(http_request,target){
					try{
						closePanel('panel_Popup');
						document.getElementById('content_Panel_Popup').style.display='none';
					}catch(e){};
				},
				img_wait
			);
		}else{
			location.replace(action, "report");
		}
	}catch(e){
		location.replace(action, "report");
	}	

	return;	
}

function downloadAsPopup_json(action,panel_width,panel_height,scroll,afterJSFunction,img_wait){
	showAsPopup_json(action, panel_width,panel_height,scroll,
			afterJSFunction,
			function(http_request,target){

				if(document.getElementById('content_Panel_Popup').style.display!="none"){
					try{
						var filename = "";
				        var disposition = http_request.getResponseHeader('Content-Disposition');
				        if (disposition && disposition.indexOf('attachment') !== -1) {
				            var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
				            var matches = filenameRegex.exec(disposition);
				            if (matches != null && matches[1]) filename = matches[1].replace(/['"]/g, '');
				        }
		
				        var type = http_request.getResponseHeader('Content-Type');
				        if(http_request.response)
				        	blob = new Blob([http_request.response], { type: type });
				        else 
				        	blob = new Blob([http_request.responseText], { type: type });
				        
				        if (typeof window.navigator.msSaveOrOpenBlob !== 'undefined') {
				            // IE workaround for "HTML7007: One or more blob URLs were revoked by closing the blob for which they were created. These URLs will no longer resolve as the data backing the URL has been freed."
	//			            window.navigator.msSaveBlob(blob, filename);
				            window.navigator.msSaveOrOpenBlob(blob, filename);
				        }else if (typeof window.navigator.msSaveBlob !== 'undefined') {
				        	window.navigator.msSaveBlob(blob, filename);
				        } else {
				            var URL = window.URL || window.webkitURL;
				            var downloadUrl = URL.createObjectURL(blob);
		
				            if (filename) {
				                // use HTML5 a[download] attribute to specify filename
				                var a = document.createElement("a");
				                // safari doesn't support this yet
				                if (typeof a.download === 'undefined') {
				                    window.location = downloadUrl;
				                } else {
				                    a.href = downloadUrl;
				                    a.download = filename;
				                    document.body.appendChild(a);
				                    a.click();
				                }
				            } else {
				                window.location = downloadUrl;
				            }
		
				            setTimeout(function () { URL.revokeObjectURL(downloadUrl); }, 100); // cleanup
				        }	
					}catch(e){
						location.replace(action);
						try{
							closePanel('panel_Popup');
							document.getElementById('content_Panel_Popup').style.display='none';
						}catch(e){};
					}				        
				}
			},
			"arraybuffer"
		);
	if(document.getElementById("content_body_Panel_Popup")){
		if(img_wait){
			try{
				document.getElementById("img_ajax_makeJSONRequest").src=img_wait;
			}catch(e){
			}
		}
	}else{
		window.setTimeout(
				function(){
					if(img_wait){
						try{
							document.getElementById("img_ajax_makeJSONRequest").src=img_wait;
						}catch(e){
						}
					}
				},
				1000
			);		
	}


}


function showAsPopup_json(action,panel_width,panel_height,scroll,afterJSFunction,redrawTargetJSFunction,responseType){

	if(document.getElementById("content_body_Panel_Popup")){
	}else{
		var tmp_container = document.createElement("div");
		tmp_container.id="tmp_container";
		document.body.appendChild(tmp_container);

		if(document.getElementById("tmp_container")){ 
			ajax_makeRequest(	"popup?type=page",
								"tmp_container",
								function(http_request,target){
									if(http_request.status == 200){
										showAsPopup_json(action,panel_width,panel_height,scroll,afterJSFunction,redrawTargetJSFunction,responseType);
									}else{
										ajax_makeRequest(	
												"jsp/included/page_popup.jsp",
												"tmp_container",
												function(){
													showAsPopup_json(action,panel_width,panel_height,scroll,afterJSFunction,redrawTargetJSFunction,responseType);
												},
												"",
												false		
										);
									}
								},
								"",
								false
			);
		}
		return;
	}

	try{
		document.getElementById("content_body_Panel_Popup").style.width=panel_width;
	}catch(e){
	}
	try{
		document.getElementById("content_body_Panel_Popup").style.height=panel_height;
	}catch(e){
	}

	try{
		if(scroll==true){
			document.getElementById("content_body_Panel_Popup").style.overflowY="scroll";
		}
	}catch(e){
	}
	if(IE4){
		document.getElementById("content_Panel_Popup").style.top=document.body.scrollTop;
		document.getElementById("content_Panel_Popup").style.left=document.body.scrollLeft;
	}else{
		document.getElementById("content_Panel_Popup").style.position="fixed";
	}
	document.getElementById("content_Panel_Popup").style.display="block";


	try{
		if(vPanel_NotPopup.zIndex>-1){
			document.getElementById("content_Panel_Popup").style.zIndex=(vPanel_NotPopup.zIndex+1)/1;
//			document.getElementById("content_Panel_Popup").style.position="absolute";
		}
	}catch(e){
	}



	ajustPanel();
	if(action!=""){
		try{		
			if(afterJSFunction){
				ajax_makeJSONRequest(action,{},"content_body_Panel_Popup",afterJSFunction,redrawTargetJSFunction,true,responseType);
			}else{
				ajax_makeJSONRequest(action,{},"content_body_Panel_Popup","JSAfter_showAsPopup",redrawTargetJSFunction,true,responseType);
			}
	
		}catch(e){
		}
	}

	
	try{
		document.getElementById("panel_Popup_img_maximize").style.display="block";
		document.getElementById("panel_Popup_img_minimize").style.display="none";
	}catch(e){
	}

	
// Added 20150423 ---
	var vPanel_Popup = mPanel_Popup.get("panel_Popup");

	if(vPanel_Popup==null){

		var vPanel_Popup = new objPopup();
		vPanel_Popup.width=document.body.offsetWidth;
		vPanel_Popup.height=document.body.offsetHeight;
		vPanel_Popup.maximize=false;
		
		hideScrollPanel(vPanel_Popup);

		mPanel_Popup.put("panel_Popup", vPanel_Popup);

		ajustPanel();

	}else{
		if(vPanel_Popup.maximize==true){
			showAsPanelMax("panel_Popup");
		}
//		else showAsPanelNormal("panel_Popup");
		ajustPanel();
	}
// ---

}

function showAsPopupSubmit_json(formName,middle,panel_width,panel_height,scroll,afterJSFunction,redrawTargetJSFunction,responseType){

	if(document.getElementById("content_body_Panel_Popup")){
	}else{
		var tmp_container = document.createElement("div");
		tmp_container.id="tmp_container";
		document.body.appendChild(tmp_container);

		if(document.getElementById("tmp_container")){
			ajax_makeRequest(	"popup?type=page",
								"tmp_container",
								function(http_request,target){
									if(http_request.status == 200){
										showAsPopupSubmit_json(formName,middle,panel_width,panel_height,scroll,afterJSFunction,redrawTargetJSFunction,responseType);
									}else{
										ajax_makeRequest(	
												"jsp/included/page_popup.jsp",
												"tmp_container",
												function(){
													showAsPopupSubmit_json(formName,middle,panel_width,panel_height,scroll,afterJSFunction,redrawTargetJSFunction,responseType);
												},
												"",
												false		
										);
									}
								},
								"",
								false
			);
		}
		return;
	}

	try{
		document.getElementById("content_body_Panel_Popup").style.width=panel_width;
	}catch(e){
	}
	try{
		document.getElementById("content_body_Panel_Popup").style.height=panel_height;
	}catch(e){
	}
	try{
		if(scroll==true){
			document.getElementById("content_body_Panel_Popup").style.overflowY="scroll";
		}
	}catch(e){
	}
	if(IE4){
		document.getElementById("content_Panel_Popup").style.top=document.body.scrollTop;
		document.getElementById("content_Panel_Popup").style.left=document.body.scrollLeft;
	}else{
		document.getElementById("content_Panel_Popup").style.position="fixed";
	}
	document.getElementById("content_Panel_Popup").style.display="block";

	try{
		if(vPanel_NotPopup.zIndex>-1){
			document.getElementById("content_Panel_Popup").style.zIndex=(vPanel_NotPopup.zIndex+1)/1;
//			document.getElementById("content_Panel_Popup").style.position="absolute";
		}
	}catch(e){
	}


	ajustPanel();
	try{
		document.getElementById(formName).middleAction.value=middle;
		if(afterJSFunction){
			ajax_submit_json(document.getElementById(formName),"content_body_Panel_Popup",afterJSFunction,redrawTargetJSFunction,responseType);
		}else{
			ajax_submit_json(document.getElementById(formName),"content_body_Panel_Popup","JSAfter_showAsPopup",redrawTargetJSFunction,responseType);
		}

	}catch(e){
//		alert(e);
	}



	var vPanel_Popup = mPanel_Popup.get("panel_Popup");

	if(vPanel_Popup==null){

		var vPanel_Popup = new objPopup();
		vPanel_Popup.width=document.body.offsetWidth;
		vPanel_Popup.height=document.body.offsetHeight;
		vPanel_Popup.maximize=false;
		
		hideScrollPanel(vPanel_Popup);

		mPanel_Popup.put("panel_Popup", vPanel_Popup);

		ajustPanel();

	}else{
		if(vPanel_Popup.maximize==true){
			showAsPanelMax("panel_Popup");
		}
//		else showAsPanelNormal("panel_Popup");
		ajustPanel();
	}

}

function showAsPopupSubmitExt_json(formName,action,panel_width,panel_height,scroll,afterJSFunction,redrawTargetJSFunction,responseType){

	if(document.getElementById("content_body_Panel_Popup")){
	}else{
		var tmp_container = document.createElement("div");
		tmp_container.id="tmp_container";
		document.body.appendChild(tmp_container);

		if(document.getElementById("tmp_container")){
			ajax_makeRequest(	"popup?type=page",
								"tmp_container",
								function(http_request,target){
									if(http_request.status == 200){
										showAsPopupSubmitExt_json(formName,action,panel_width,panel_height,scroll,afterJSFunction,redrawTargetJSFunction,responseType);
									}else{
										ajax_makeRequest(	
												"jsp/included/page_popup.jsp",
												"tmp_container",
												function(){
													showAsPopupSubmitExt_json(formName,action,panel_width,panel_height,scroll,afterJSFunction,redrawTargetJSFunction,responseType);
												},
												"",
												false		
										);
									}
								},
								"",
								false
			);
		}
		return;
	}

	try{
		document.getElementById("content_body_Panel_Popup").style.width=panel_width;
	}catch(e){
	}
	try{
		document.getElementById("content_body_Panel_Popup").style.height=panel_height;
	}catch(e){
	}
	try{
		if(scroll==true){
			document.getElementById("content_body_Panel_Popup").style.overflowY="scroll";
		}
	}catch(e){
	}
	if(IE4){
		document.getElementById("content_Panel_Popup").style.top=document.body.scrollTop;
		document.getElementById("content_Panel_Popup").style.left=document.body.scrollLeft;
	}else{
		document.getElementById("content_Panel_Popup").style.position="fixed";
	}
	document.getElementById("content_Panel_Popup").style.display="block";

	try{
		if(vPanel_NotPopup.zIndex>-1){
			document.getElementById("content_Panel_Popup").style.zIndex=(vPanel_NotPopup.zIndex+1)/1;
//			document.getElementById("content_Panel_Popup").style.position="absolute";
		}
	}catch(e){
	}


	ajustPanel();
	try{
		if(afterJSFunction){
			ajax_submitExt_json(document.getElementById(formName),action,"content_body_Panel_Popup",afterJSFunction,redrawTargetJSFunction,responseType);
		}else{
			ajax_submitExt_json(document.getElementById(formName),action,"content_body_Panel_Popup","JSAfter_showAsPopup",redrawTargetJSFunction,responseType);
		}

	}catch(e){
//		alert(e);
	}

	var vPanel_Popup = mPanel_Popup.get("panel_Popup");

	if(vPanel_Popup==null){

		var vPanel_Popup = new objPopup();
		vPanel_Popup.width=document.body.offsetWidth;
		vPanel_Popup.height=document.body.offsetHeight;
		vPanel_Popup.maximize=false;
		
		hideScrollPanel(vPanel_Popup);

		mPanel_Popup.put("panel_Popup", vPanel_Popup);

		ajustPanel();

	}else{
		if(vPanel_Popup.maximize==true){
			showAsPanelMax("panel_Popup");
		}
//		else showAsPanelNormal("panel_Popup");
		ajustPanel();
	}

}

//---

function showAlertAsPopup(label){
	if(document.getElementById("content_body_Panel_Popup")){
	}else{
		var tmp_container = document.createElement("div");
		tmp_container.id="tmp_container";
		document.body.appendChild(tmp_container);

		if(document.getElementById("tmp_container")){
			ajax_makeRequest(	"popup?type=page",
								"tmp_container",
								function(http_request,target){
									if(http_request.status == 200){
										showAlertAsPopup(label);
									}else{
										ajax_makeRequest(	
												"jsp/included/page_popup.jsp",
												"tmp_container",
												function(){
													showAlertAsPopup(label);
												},
												"",
												false		
										);
									}
								},
								"",
								false
			);
		}
		return;
	}


	try{
		document.getElementById("content_body_Panel_Popup").style.width=505;
	}catch(e){
	}
	try{
//		document.getElementById("content_body_Panel_Popup").style.height=100;
	}catch(e){
	}


	if(IE4){
		document.getElementById("content_Panel_Popup").style.top=document.body.scrollTop;
		document.getElementById("content_Panel_Popup").style.left=document.body.scrollLeft;
	}else{
		document.getElementById("content_Panel_Popup").style.position="fixed";
	}
	document.getElementById("content_Panel_Popup").style.display="block";


	try{
		if(vPanel_NotPopup.zIndex>-1){
			document.getElementById("content_Panel_Popup").style.zIndex=(vPanel_NotPopup.zIndex+1)/1;
//			document.getElementById("content_Panel_Popup").style.position="absolute";
		}
	}catch(e){
	}



	ajustPanel();

		try{
			
			var f_exec_alert = function(http_request,target){
				document.getElementById(target).innerHTML=http_request.responseText;
				if (typeof label === "function") {
					label("confirm_label");
	    		}else{
	    			document.getElementById("confirm_label").innerHTML = label;
	    		}
				
			}	
			
			ajax_makeRequest(	
					"popup?type=confirm",
					"content_body_Panel_Popup",
					function(http_request,target){
						if(http_request.status == 200){
							if (typeof f_exec_alert === "function") {
								f_exec_alert(http_request,target);
		            		}else{
		            			eval(f_exec_alert + "(http_request,target)");
		            		}
						}else{
							ajax_makeRequest(
									"jsp/framework/viewalert.jsp",
									"content_body_Panel_Popup",
									"",
									f_exec_alert,
									false);
						}
					},
					"",
					false
			);		
			
		}catch(e){
		}


	try{
		document.getElementById("panel_Popup_img_maximize").style.display="block";
		document.getElementById("panel_Popup_img_minimize").style.display="none";
	}catch(e){
	}

	var vPanel_Popup = mPanel_Popup.get("panel_Popup");

	if(vPanel_Popup==null){

		var vPanel_Popup = new objPopup();
		vPanel_Popup.width=document.body.offsetWidth;
		vPanel_Popup.height=document.body.offsetHeight;
		vPanel_Popup.maximize=false;

		hideScrollPanel(vPanel_Popup);

		mPanel_Popup.put("panel_Popup", vPanel_Popup);

		ajustPanel();

	}else{
		if(vPanel_Popup.maximize==true){
			showAsPanelMax("panel_Popup");
		}
//		else showAsPanelNormal("panel_Popup");
		ajustPanel();
	}


}

function showConfirmAsPopup(label,callOk, callKo, labelBtnOk, labelBtnKo){

	if(document.getElementById("content_body_Panel_Popup")){
	}else{
		var tmp_container = document.createElement("div");
		tmp_container.id="tmp_container";
		document.body.appendChild(tmp_container);

		if(document.getElementById("tmp_container")){
			ajax_makeRequest(	"popup?type=page",
								"tmp_container",
								function(http_request,target){
									if(http_request.status == 200){
										showConfirmAsPopup(label,callOk, callKo, labelBtnOk, labelBtnKo);
									}else{
										ajax_makeRequest(	
												"jsp/included/page_popup.jsp",
												"tmp_container",
												function(){
													showConfirmAsPopup(label,callOk, callKo, labelBtnOk, labelBtnKo);
												},
												"",
												false		
										);
									}
								},
								"",
								false
			);
		}
		return;
	}



	try{
		document.getElementById("content_body_Panel_Popup").style.width=505;
	}catch(e){
	}
	try{
//		document.getElementById("content_body_Panel_Popup").style.height=120;
	}catch(e){
	}

	if(IE4){
		document.getElementById("content_Panel_Popup").style.top=document.body.scrollTop;
		document.getElementById("content_Panel_Popup").style.left=document.body.scrollLeft;
	}else{
		document.getElementById("content_Panel_Popup").style.position="fixed";
	}
	document.getElementById("content_Panel_Popup").style.display="block";


	try{
		if(vPanel_NotPopup.zIndex>-1){
			document.getElementById("content_Panel_Popup").style.zIndex=(vPanel_NotPopup.zIndex+1)/1;
			if(IE4)
				document.getElementById("content_Panel_Popup").style.position="absolute";
		}
	}catch(e){
	}



	ajustPanel();

	try{

		var f_exec_confirm = function(http_request,target){

			document.getElementById(target).innerHTML=http_request.responseText;
			if (typeof label === "function") {
				label("confirm_label");
    		}else{
    			document.getElementById("confirm_label").innerHTML = label;
    		}
			
			if(document.getElementById("action_ok")){
				document.getElementById("action_ok").onclick = callOk;
				if(labelBtnOk){
					if (typeof labelBtnOk === "function") {
						labelBtnOk("action_ok");
		    		}else{
		    			document.getElementById("action_ok").innerHTML = labelBtnOk;
		    		}
					
					document.getElementById("action_ok").style.width=labelBtnOk.length*10;
				}
			}
			if(document.getElementById("action_confirm")){
				document.getElementById("action_confirm").onclick = callOk;
				if(labelBtnOk){
					if (typeof labelBtnOk === "function") {
						labelBtnOk("action_confirm");
		    		}else{
		    			document.getElementById("action_confirm").innerHTML = labelBtnOk;
		    		}
					document.getElementById("action_confirm").style.width=labelBtnOk.length*10;
				}
			}	
			if(document.getElementById("action_ko")){
				if(labelBtnKo){
					if (typeof labelBtnKo === "function") {
						labelBtnKo("action_ko");
		    		}else{
		    			document.getElementById("action_ko").innerHTML = labelBtnKo;
		    		}
					document.getElementById("action_ko").style.width=labelBtnKo.length*10;
					document.getElementById("action_ko").onclick = callKo;

				}else{
					try{
						var element = document.getElementById('action_ko');
						element.outerHTML = '';
						delete element;
					}catch(e){
					}							
				}
			}
		}
		
		ajax_makeRequest(	
				"popup?type=confirm",
				"content_body_Panel_Popup",
				function(http_request,target){
					if(http_request.status == 200){
						if (typeof f_exec_confirm === "function") {
							f_exec_confirm(http_request,target);
	            		}else{
	            			eval(f_exec_confirm + "(http_request,target)");
	            		}
					}else{
						ajax_makeRequest(
								"jsp/framework/viewconfirm.jsp",
								"content_body_Panel_Popup",
								"",
								f_exec_confirm,
								false
							);	
					}
				},
				"",
				false
		);		
		
	}catch(e){
	}

	



	try{
		document.getElementById("panel_Popup_img_maximize").style.display="block";
		document.getElementById("panel_Popup_img_minimize").style.display="none";
	}catch(e){
	}

	var vPanel_Popup = mPanel_Popup.get("panel_Popup");

	if(vPanel_Popup==null){

		var vPanel_Popup = new objPopup();
		vPanel_Popup.width=document.body.offsetWidth;
		vPanel_Popup.height=document.body.offsetHeight;
		vPanel_Popup.maximize=false;
		
		hideScrollPanel(vPanel_Popup);

		mPanel_Popup.put("panel_Popup", vPanel_Popup);

		ajustPanel();

	}else{
		if(vPanel_Popup.maximize==true){
			showAsPanelMax("panel_Popup");
		}
//		else showAsPanelNormal("panel_Popup");
		ajustPanel();
	}


}



function showMessAsPopup(panel_width,panel_height){

	try{
		document.getElementById("div_messages").style.width=panel_width;
	}catch(e){
	}
	try{
		document.getElementById("div_messages").style.height=panel_height;
	}catch(e){
	}

	document.getElementById("div_messages").style.display="block";

	try{
		if(vPanel_NotPopup.zIndex>-1){
			document.getElementById("div_messages").style.zIndex=(vPanel_NotPopup.zIndex+1)/1;
			document.getElementById("div_messages").style.position="absolute";
		}
	}catch(e){
	}


	ajustPanel();
	try{
		ajax_makeRequest("messages","div_messages");
	}catch(e){
//		alert(e);
	}
	ajustPanelMinMaxDef();
}

function showAsPanelMax(panel_id){


	var obj_panel=document.getElementById(panel_id);
	var obj_parent = obj_panel.offsetParent;
	var isObjParent=false;
	try{
		while(obj_parent.tagName.toUpperCase()!="DIV"){
			obj_parent=obj_parent.offsetParent;
		}
		isObjParent=true;
	}catch(e){
		isObjParent=false;
	}



	if(isObjParent==true){

		var id_body=obj_parent.id;
		
		
		if(id_body.indexOf("content_")==0)
			id_body=id_body.replace("content_", "content_body_");
		
		var vPanel_Popup = new objPopup();
		if(document.getElementById(id_body)){			
			var current_obj = document.getElementById(id_body);
			
			vPanel_Popup.width=current_obj.style.width;
			vPanel_Popup.height=current_obj.style.height;

			vPanel_Popup.maximize=false;

			try{
				current_obj.style.width=document.body.clientWidth-40;
			}catch(e){
			}
			try{
				current_obj.style.height=document.body.clientHeight-40;
			}catch(e){
			}

			vPanel_Popup.maximize=true;

			mPanel_Popup.put(panel_id, vPanel_Popup);

			ajustPanelMinMax(panel_id);
	//		ajustPanel();
			return;
		}
	}


	try{
		var current_npobj = document.getElementById(panel_id);

		vPanel_NotPopup = new objPopup();
		vPanel_NotPopup.width=current_npobj.style.width;
		vPanel_NotPopup.height=current_npobj.style.height
		vPanel_NotPopup.position=current_npobj.style.position;
		vPanel_NotPopup.zIndex=current_npobj.style.zIndex;
		vPanel_NotPopup.maximize=false;

		current_npobj.style.position="absolute";

		current_npobj.style.top=5;
		current_npobj.style.left=5;
		current_npobj.style.width=document.body.clientWidth-5;
		current_npobj.style.height=document.body.clientHeight -5;
		
		vPanel_NotPopup.maximize=true;

		ajustPanelMinMaxNotPopup(panel_id);
//		ajustPanel();

		showInternalAsPanelMax(panel_id);

	}catch(e){
	}


}

function resizeAsPanelMax(panel_id){


	var obj_panel=document.getElementById(panel_id);
	var obj_parent = obj_panel.offsetParent;
	var isObjParent=false;
	try{
		while(obj_parent.tagName.toUpperCase()!="DIV"){
			obj_parent=obj_parent.offsetParent;
		}
		isObjParent=true;
	}catch(e){
		isObjParent=false;
	}



	if(isObjParent==true){

		var id_body=obj_parent.id;
		
		
		if(id_body.indexOf("content_")==0)
			id_body=id_body.replace("content_", "content_body_");
		
		var vPanel_Popup = mPanel_Popup.get(panel_id);
		if(document.getElementById(id_body) && vPanel_Popup ){			
			var current_obj = document.getElementById(id_body);
			
			vPanel_Popup.width=current_obj.style.width;
			vPanel_Popup.height=current_obj.style.height;

			vPanel_Popup.maximize=false;

			try{
				current_obj.style.width=document.body.clientWidth-40;
			}catch(e){
			}
			try{
				current_obj.style.height=document.body.clientHeight-40;
			}catch(e){
			}

			vPanel_Popup.maximize=true;

			mPanel_Popup.put(panel_id, vPanel_Popup);

			ajustPanelMinMax(panel_id);

			return;
		}
	}


	try{
		var current_npobj = document.getElementById(panel_id);

		current_npobj.style.position="absolute";

		current_npobj.style.top=5;
		current_npobj.style.left=5;
		current_npobj.style.width=document.body.clientWidth-5;
		current_npobj.style.height=document.body.clientHeight -5;
		

	}catch(e){
	}


}


function showAsPanelNormal(panel_id){

	var obj_panel=document.getElementById(panel_id);
	var obj_parent = obj_panel.offsetParent;
	var isObjParent=false;
	try{
		while(obj_parent.tagName.toUpperCase()!="DIV"){
			obj_parent=obj_parent.offsetParent;
		}
		isObjParent=true;
	}catch(e){
		isObjParent=false;
	}


	if(isObjParent==true){
		var id_body=obj_parent.id;
		if(id_body.indexOf("content_")==0)
			id_body=id_body.replace("content_", "content_body_");


		var vPanel_Popup = mPanel_Popup.get(panel_id);

		if(document.getElementById(id_body)){
			var current_obj = document.getElementById(id_body);
			current_obj.style.width=vPanel_Popup.width;
			current_obj.style.height=vPanel_Popup.height;

			vPanel_Popup.maximize=false;
			ajustPanelMinMax(panel_id);
	//		ajustPanel();
			return;
		}

	}


	try{

		var current_npobj = document.getElementById(panel_id);
		
		current_npobj.style.position=vPanel_NotPopup.position;

		current_npobj.style.width=vPanel_NotPopup.width;
		current_npobj.style.height=vPanel_NotPopup.height;
		vPanel_NotPopup.maximize=false;

		ajustPanelMinMaxNotPopup(panel_id);
//		ajustPanel();

		showInternalAsPanelNormal(panel_id);

	}catch(e){
	}

}

function ajustPanelMinMax(panel_id){

		try{
			var vPanel_Popup = mPanel_Popup.get(panel_id);
			if(vPanel_Popup.maximize==false){
				document.getElementById(panel_id+"_img_maximize").style.display="block";
				document.getElementById(panel_id+"_img_minimize").style.display="none";
			}else{

				document.getElementById(panel_id+"_img_maximize").style.display="none";
				document.getElementById(panel_id+"_img_minimize").style.display="block";
			}
		}catch(e){
		}
}

function ajustPanelMinMaxNotPopup(panel_id){
	try{
		if(vPanel_NotPopup.maximize==false){
			document.getElementById(panel_id+"_img_maximize").style.display="block";
			document.getElementById(panel_id+"_img_minimize").style.display="none";
		}else{

			document.getElementById(panel_id+"_img_maximize").style.display="none";
			document.getElementById(panel_id+"_img_minimize").style.display="block";
		}
	}catch(e){
	}
}

function ajustPanelMinMaxDef(){

		try{

//				document.getElementById("panel_Popup_img_maximize").style.display="block";
//				document.getElementById("panel_Popup_img_minimize").style.display="none";
		}catch(e){
		}

}

function closePanelPopup(){

	try{
		JSBeforeClose_popup();
	}catch(e){};
	try{
		document.getElementById('content_Panel_Popup').style.display='none';
	}catch(e){};
	try{
		closePanel('panel_Popup');
	}catch(e){};
}

function closePanel(panel_id){

	try{
		document.getElementById(panel_id+"_img_maximize").style.display="block";
		document.getElementById(panel_id+"_img_minimize").style.display="none";
	}catch(e){
	}


	var obj_panel=document.getElementById(panel_id);
	var obj_parent = obj_panel.offsetParent;
	var isObjParent=false;
	try{
		while(obj_parent.tagName.toUpperCase()!="DIV"){
			obj_parent=obj_parent.offsetParent;
		}
		isObjParent=true;
	}catch(e){
		isObjParent=false;
	}



	if(isObjParent==true){

		var id_body=obj_parent.id;
		if(id_body.indexOf("content_")==0)
			id_body=id_body.replace("content_", "content_body_");

		document.getElementById(id_body).innerHTML='';

	}
	if(IE4){
		if(panel_id=="panel_Popup" || panel_id=="panel_IFrame"){
			var vPanel_Popup = mPanel_Popup.get(panel_id);
			if(vPanel_Popup!=null){
				document.body.style.overflowX=vPanel_Popup.pscrollx;
				document.body.style.overflowY=vPanel_Popup.pscrolly;
			}
		}
	}
	try{
		mPanel_Popup.remove(panel_id);
	}catch(e){

	}




}

function hideScrollPanel(panel_Popup){
	if(IE4){
		panel_Popup.pscrollx=document.body.style.overflowX;
		panel_Popup.pscrolly=document.body.style.overflowY;
		document.body.style.overflowX="hidden";
		document.body.style.overflowY="hidden";
	}
}



function JSAfter_showAsPopup(){

}

function ajustPanel(){

	var list = document.getElementsByTagName("DIV");

	for(var i=0;i<list.length;i++){
		try{

			if(list[i].id && list[i].id.indexOf("content_Panel_")==0){
				if(list[i].offsetWidth>document.body.clientWidth)
					list[i].style.width = document.body.clientWidth;
				if(list[i].offsetHeight>document.body.clientHeight)
					list[i].style.height = document.body.clientHeight;
			}
		}catch(e){
		}

	}

}

function ajustPageInHeader(){


	try{

		var div = document.getElementById("page1");
		var div_mc = document.getElementById("menu_command");
		var tr_mc = document.getElementById("tr_menu_command");
		var div_fb2 = document.getElementById("fixedbox2");

		if(div){
			if(div_mc) div_mc.innerHTML = div.innerHTML;
		}
		if(tr_mc) tr_mc.style.display="block";
		if(div_fb2) div_fb2.style.display="block";

	}catch(e){

	}
}

function ajustMenuPanel(){
	if(IE4){
		try{
			window.innerHeight = window.innerHeight || document.documentElement.clientHeight;
			window.innerWidth = window.innerWidth || document.documentElement.clientWidth;
		}catch(e){
			
		}			
	}
	var div = document.getElementById("menu_left");
	if(div){
		var hasHScroll = document.body.scrollWidth > document.body.clientWidth;
		if(hasHScroll)
			div.style.height = window.innerHeight-65;
		else
			div.style.height = window.innerHeight-55;
	}

}

function ajustMenuPanel_ar(){
	if(IE4){
		try{
			window.innerHeight = window.innerHeight || document.documentElement.clientHeight;
			window.innerWidth = window.innerWidth || document.documentElement.clientWidth;
		}catch(e){
			
		}			
	}	
	var div = document.getElementById("menu_left");
	if(div){
		var hasHScroll = document.body.scrollWidth > document.body.clientWidth;
		if(hasHScroll)
			div.style.height = window.innerHeight-50;
		else
			div.style.height = window.innerHeight-55;

		var hasVScroll = document.body.scrollHeight > document.body.clientHeight;
		if(hasVScroll)
			div.style.left = window.innerWidth-265;
		else
			div.style.left = window.innerWidth-250;
	}
}

function changeOnSelTr(obj){

	var obj_s = obj;
	try{
		var clname = obj_s.className;
		clname=clname+"Sel";
		obj_s.className=clname;
	}catch(e){
	}
}



function changeOnNormTr(obj){


	var obj_s = obj;
	try{
		var clname = obj_s.className;
		clname=clname.replace("Sel","");
		obj_s.className=clname;
	}catch(e){
	}
}

function printdiv(printpage){
var headstr = "<html><head><title></title></head><body>";
var footstr = "</body>";
var newstr = document.getElementById(printpage).innerHTML;
var oldstr = document.body.innerHTML;
document.body.innerHTML = headstr+newstr+footstr;
window.print();
document.body.innerHTML = oldstr;
return false;
}

function clearNobr(node){
	if(node.innerHTML.toUpperCase().indexOf("<NOBR>")==0){
		var list = node.childNodes;
		for(var i=0;i<list.length;i++){
			if(list[i].nodeName=="NOBR") return list[i].innerHTML;
		}
		return node.innerHTML;
	}else{
		return node.innerHTML;
	}

}
function filterIntoTab(str,obj){
	var list = obj.childNodes;
	for(var i=0;i<list.length;i++){
		if(list[i].nodeName=="TBODY"){
			var listTR = list[i].childNodes;
			for(var j=0;j<listTR.length;j++){
				if(listTR[j].nodeName=="TR"){
					var isFound=false;
					var listTD = listTR[j].childNodes;
					for(var k=0;k<listTD.length;k++){
						if(listTD[k].nodeName=="TD"){
							var cnt = clearNobr(listTD[k]).toUpperCase();
							if(cnt.indexOf(str.toUpperCase())>-1) isFound=true;
						}
					}
					if(isFound){
						listTR[j].style.visibility="visible";
						listTR[j].style.display="block";

					}else{
						listTR[j].style.visibility="hidden";
						listTR[j].style.display="none";
					}
				}
			}
		}
	}
}

function findInDiv(str,div) {
	var win = this;
	var n   = 0;
	var txt, i, found;

	if (str == "") return false;

	if (NS4 || NS6) {

		if (!win.find(str))
		while(win.find(str, false, true))
			n++;
		else
			n++;
		if (n == 0) 
			alert(str + " was not found on this page.");
	}
	if (IE4) {

		if( win.document.selection && win.document.selection.type != 'None' ) {
			txt = win.document.selection.createRange();
			txt.collapse( false );
		} else {
			txt = win.document.body.createTextRange();
		}

		for (i = 0; i <= n && (found = txt.findText(str)) != false; i++) {
			txt.moveStart("character", 1);
			txt.moveEnd("textedit");
		}
		if (found) {
			txt.moveStart("character", -1);
			txt.findText(str);
			txt.select();
			txt.scrollIntoView();
			n++;
		}else {
			if (n > 0) {
				n = 0;
				findInPage(str);
			}else	
				alert(str + " was not found on this page.");
		}
	}
	return false;
}
function setActiveTabs_ajax(index){
	try{
		var page_tabs = document.getElementById("page_tabs").value;
		var buf="";
		for(var i=0;i<page_tabs.length;i++){
			if(index==i){
				 buf+="v";
				 document.getElementById("div_page_tab_"+i).style.display="block";
				 document.getElementById("page_tab_"+i).className = "page_tab_section";
			}
			else{
				 buf+="h";
				 document.getElementById("div_page_tab_"+i).style.display="none";
				 document.getElementById("page_tab_"+i).className = "page_tab_sectiondis";
			}
		}
		page_tabs=buf;
		document.getElementById("page_tabs").value=page_tabs;
	}catch(e){
	}

}


function Map(){
    // members
    this.keyArray = new Array(); // Keys
    this.valArray = new Array(); // Values

    // methods
    this.put = put;
    this.get = get;
    this.size = size;
    this.clear = clear;
    this.keySet = keySet;
    this.valSet = valSet;
    this.showMe = showMe;   // returns a string with all keys and values in map.
    this.findIt = findIt;
    this.remove = remove;
}

function put( key, val ){
    var elementIndex = this.findIt( key );

    if( elementIndex == (-1) )
    {
        this.keyArray.push( key );
        this.valArray.push( val );
    }
    else
    {
        this.valArray[ elementIndex ] = val;
    }
}

function get( key ){
    var result = null;
    var elementIndex = this.findIt( key );

    if( elementIndex != (-1) )
    {
        result = this.valArray[ elementIndex ];
    }

    return result;
}

function remove( key ){
    var result = null;
    var elementIndex = this.findIt( key );

    if( elementIndex != (-1) )
    {
//        this.keyArray = this.keyArray.removeAt(elementIndex);
//        this.valArray = this.valArray.removeAt(elementIndex);
        this.keyArray = removeAtArray(this.keyArray,elementIndex);
        this.valArray = removeAtArray(this.valArray,elementIndex);


    }

    return ;
}

function size(){
    return (this.keyArray.length);
}

function clear(){
    for( var i = 0; i < this.keyArray.length; i++ )
    {
        this.keyArray.pop(); this.valArray.pop();
    }
}

function keySet(){
    return (this.keyArray);
}

function valSet(){
    return (this.valArray);
}

function showMe(){
    var result = "";

    for( var i = 0; i < this.keyArray.length; i++ )
    {
        result += "Key: " + this.keyArray[ i ] + "\tValues: " + this.valArray[ i ] + "\n";
    }
    return result;
}

function findIt( key ){
    var result = (-1);

    for( var i = 0; i < this.keyArray.length; i++ )
    {
        if( this.keyArray[ i ] == key )
        {
            result = i;
            break;
        }
    }
    return result;
}

function removeAt( index ){
  var part1 = this.slice( 0, index);
  var part2 = this.slice( index+1 );

  return( part1.concat( part2 ) );
}

function removeAtArray(array, index ){
	  var part1 = array.slice( 0, index);
	  var part2 = array.slice( index+1 );

	  return( part1.concat( part2 ) );
	}

//******************


function canceOnClicklBubble(event){
	try{
		event.stopPropagation();
		window.event.cancelBubble = true;
	}catch(e){
		window.event.cancelBubble = true;
	}
}

function getFileName (inp){

	var div = inp.offsetParent;
	var str = inp.value;
    if (str.lastIndexOf('\\')){
        var i = str.lastIndexOf('\\')+1;
    }
    else{
        var i = str.lastIndexOf('/')+1;
    }
    var filename = str.slice(i);
    var list = div.childNodes;
	for(var i=0;i<list.length;i++){
		if(list[i].nodeName=="DIV"){
			list[i].innerHTML = filename;
			return;
		}
	}
//    var uploaded = document.getElementById("fileformlabel");
//    uploaded.innerHTML = filename;
}


// Cookies
function reldate(days) {
	var d;
	d = new Date();

	/* We need to add a relative amount of time to
	the current date. The basic unit of JavaScript
	time is milliseconds, so we need to convert the
	days value to ms. Thus we have
	ms/day
	= 1000 ms/sec *  60 sec/min * 60 min/hr * 24 hrs/day
	= 86,400,000. */

	d.setTime(d.getTime() + days*86400000);
	return d.toGMTString();
}

function readCookie(name) {
	var s = document.cookie, i;
	if (s)
	for (i=0, s=s.split('; '); i<s.length; i++) {
		s[i] = s[i].split('=', 2);
		if (unescape(s[i][0]) == name)
		return unescape(s[i][1]);
	}
	return null;
}

function makeCookie(name, value, p) {
	var s, k;
	s = escape(name) + '=' + escape(value);
	if (p) for (k in p) {

		/* convert a numeric expires value to a relative date */
	
		if (k == 'expires')
		p[k] = isNaN(p[k]) ? p[k] : reldate(p[k]);
	
		/* The secure property is the only special case
		here, and it causes two problems. Rather than
		being '; protocol=secure' like all other
		properties, the secure property is set by
		appending '; secure', so we have to use a
		ternary statement to format the string.
	
		The second problem is that secure doesn't have
		any value associated with it, so whatever value
		people use doesn't matter. However, we don't
		want to surprise people who set { secure: false }.
		For this reason, we actually do have to check
		the value of the secure property so that someone
		won't end up with a secure cookie when
		they didn't want one. */
	
		if (p[k])
			s += '; ' + (k != 'secure' ? k + '=' + p[k] : k);
	}
	document.cookie = s;
	return readCookie(name) == value;
}

function rmCookie(name) {
	return !makeCookie(name, '', { expires: -1 });
}