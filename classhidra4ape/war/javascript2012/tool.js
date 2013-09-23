//---	30/07/2013 
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




function runHelp(){
	var helpId="";
	var helpSection="";
	var helpRedirect="";
	if(document.getElementById("$action")) helpId=document.getElementById("$action").value;
	startURL("$help?helpId="+helpId+"&helpSection="+helpSection+"&helpRedirect="+helpRedirect,"Help");
}



function runSubmitOnEnter(){
	try{
		var go = false;
		try{
			runActionOnEnter();
		}catch(e){
			go=true;
		}
		if(go){
//			document.forms[0].submit();
//			if(parent.frames["content"]) parent.frames["content"].beforeClick();
		}						
	}catch(e){
	}
}	

function beforeClick(){
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

function runSubmit(middle){
	try{

		document.forms[0].middleAction.value=middle;
		document.forms[0].submit();
		document.getElementById("content_Panel_runSubmit").style.top=document.body.scrollTop;
		document.getElementById("content_Panel_runSubmit").style.display="block";
		if(parent.frames["content"])
			parent.frames["content"].beforeClick();
	}catch(e){
	}
}	

function runSubmit(middle,formName){
	try{
		var form = document.getElementById(formName);
		if(form){			
		}else{
			form = document.forms[0]; 
		}
		if(form){
			
			form.middleAction.value=middle;
			form.submit();
			document.getElementById("content_Panel_runSubmit").style.top=document.body.scrollTop;
			document.getElementById("content_Panel_runSubmit").style.display="block";
			if(parent.frames["content"])
				parent.frames["content"].beforeClick();

		}
	}catch(e){
	}
}	

function runSubmitF(formName,middle){
	try{
		document.getElementById(formName).middleAction.value=middle;
		document.getElementById(formName).submit();
		document.getElementById("content_Panel_runSubmit").style.top=document.body.scrollTop;
		document.getElementById("content_Panel_runSubmit").style.display="block";
		if(parent.frames["content"])
			parent.frames["content"].beforeClick();
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
		runActionOnKey(id);
	}catch(e){
		go = true;
	}
	if(go){		
		if(id==13) runSubmitOnEnter();
	}	
	if(id==113) runHelp();
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
		beforeClick();
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

	var form = document.forms[0].action;
	try{
		if(formName){	
			form = document.getElementById(formName);
		}else{
			form = document.forms[0];
		}
	}catch(e){
	}
	
	
	
	if(form){
	}else form = document.forms[0].action;
	
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
				location.replace(getIdAction(urlAction)+""+add2url);
			}catch(e){
			}
		}
	try{
		
		document.getElementById("content_Panel_runSubmit").style.top=document.body.scrollTop;
		document.getElementById("content_Panel_runSubmit").style.display="block";

		if(parent.frames["content"])
			parent.frames["content"].beforeClick();
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
				if(parent)
					parent.location.replace(getIdAction(action)+""+add2url);
				else location.replace(getIdAction(action)+""+add2url);
			}catch(e){
			}	
		}else{
			try{
				if(action.indexOf("?")>-1)
					document.forms[0].action = action+"&"+url;
				else{
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
					location.replace(_idaction);
				}catch(e){
				}
			}
		}
		try{
			document.getElementById("content_Panel_runSubmit").style.top=document.body.scrollTop;
			document.getElementById("content_Panel_runSubmit").style.display="block";

			if(parent.frames["content"])
				parent.frames["content"].beforeClick();
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


function goPage(add){
	try{
		document.forms[0].middleAction.value="page";
		document.forms[0].current_page.value=(document.forms[0].current_page.value/1+add/1);
		document.forms[0].submit();
		document.getElementById("content_Panel_runSubmit").style.top=document.body.scrollTop;
		document.getElementById("content_Panel_runSubmit").style.display="block";
	}catch(e){
	}
	try{
		if(parent.frames["content"])
			parent.frames["content"].beforeClick();
	}catch(e){
	}
	
}	

function goPageN(page){
	try{
		document.forms[0].middleAction.value="page";
		document.forms[0].current_page.value=page;
		document.forms[0].submit();
		document.getElementById("content_Panel_runSubmit").style.top=document.body.scrollTop;
		document.getElementById("content_Panel_runSubmit").style.display="block";
	}catch(e){
	}
	try{
		if(parent.frames["content"])
			parent.frames["content"].beforeClick();
	}catch(e){
	}
	
}	

function goPage_ajax(frmName,add){
		document.getElementById(frmName).middleAction.value="page";
		document.getElementById(frmName).current_page.value=(document.getElementById(frmName).current_page.value/1+add/1);
		ajax_submit(document.getElementById(frmName),'content_body_Panel_Popup');
}	

function goPageN_ajax(frmName,page){
	document.getElementById(frmName).middleAction.value="page";
	document.getElementById(frmName).current_page.value=page;
	ajax_submit(document.getElementById(frmName),'content_body_Panel_Popup');
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

	document.getElementById("content_IFrame_Popup").style.top=document.body.scrollTop;
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
	vPanel_Popup = new objPopup();
	try{
		document.getElementById("panel_IFrame_img_maximize").style.display="block";
		document.getElementById("panel_IFrame_img_minimize").style.display="none";
	}catch(e){
	}
	
}

function ajustPopupOnScroll(){
	try{
		document.getElementById("content_Panel_Popup").style.top=document.body.scrollTop;
		document.getElementById("content_Panel_Popup").style.left=document.body.scrollLeft;
	}catch(e){
	}
}

function showAsPopup(action,panel_width,panel_height,scroll){
	
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

	document.getElementById("content_Panel_Popup").style.top=document.body.scrollTop;
	document.getElementById("content_Panel_Popup").style.left=document.body.scrollLeft;
	document.getElementById("content_Panel_Popup").style.display="block";
	

	try{
		if(vPanel_NotPopup.zIndex>-1){
			document.getElementById("content_Panel_Popup").style.zIndex=(vPanel_NotPopup.zIndex+1)/1;
			document.getElementById("content_Panel_Popup").style.position="absolute";
		}
	}catch(e){
	}


	
	ajustPanel();
	try{
		ajax_makeRequest(action,"content_body_Panel_Popup","JSAfter_showAsPopup");		
	}catch(e){
		alert(e);
	}

	try{
		document.getElementById("panel_Popup_img_maximize").style.display="block";
		document.getElementById("panel_Popup_img_minimize").style.display="none";
	}catch(e){
	}


	
}


function showAsPopupSubmit(formName,middle,panel_width,panel_height,scroll){
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
	document.getElementById("content_Panel_Popup").style.top=document.body.scrollTop;
	document.getElementById("content_Panel_Popup").style.left=document.body.scrollLeft;
	document.getElementById("content_Panel_Popup").style.display="block";
	
	try{
		if(vPanel_NotPopup.zIndex>-1){
			document.getElementById("content_Panel_Popup").style.zIndex=(vPanel_NotPopup.zIndex+1)/1;
			document.getElementById("content_Panel_Popup").style.position="absolute";
		}
	}catch(e){
	}

	
	ajustPanel();
	try{
		document.getElementById(formName).middleAction.value=middle;
		ajax_submit(document.getElementById(formName),"content_body_Panel_Popup","JSAfter_showAsPopup");
		
	}catch(e){
		alert(e);
	}
	

	
	var vPanel_Popup = mPanel_Popup.get("panel_Popup");
	if(vPanel_Popup==null){
	}else{
		if(vPanel_Popup.maximize==true){
			showAsPanelMax("panel_Popup");
		}else showAsPanelNormal("panel_Popup");
	}

}

function showAsPopupSubmitExt(formName,action,panel_width,panel_height,scroll){
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
	
	document.getElementById("content_Panel_Popup").style.top=document.body.scrollTop;
	document.getElementById("content_Panel_Popup").style.display="block";
	
	try{
		if(vPanel_NotPopup.zIndex>-1){
			document.getElementById("content_Panel_Popup").style.zIndex=(vPanel_NotPopup.zIndex+1)/1;
			document.getElementById("content_Panel_Popup").style.position="absolute";
		}
	}catch(e){
	}

	
	ajustPanel();
	try{
		ajax_submitExt(document.getElementById(formName),action,"content_body_Panel_Popup","JSAfter_showAsPopup");
		
	}catch(e){
		alert(e);
	}
	
	var vPanel_Popup = mPanel_Popup.get("panel_Popup");
	if(vPanel_Popup==null){
	}else{
		if(vPanel_Popup.maximize==true){
			showAsPanelMax("panel_Popup");
		}else showAsPanelNormal("panel_Popup");
	}

}


//JSON

function showAsPopup_json(action,panel_width,panel_height,scroll){
	
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

	document.getElementById("content_Panel_Popup").style.top=document.body.scrollTop;
	document.getElementById("content_Panel_Popup").style.left=document.body.scrollLeft;
	document.getElementById("content_Panel_Popup").style.display="block";
	

	try{
		if(vPanel_NotPopup.zIndex>-1){
			document.getElementById("content_Panel_Popup").style.zIndex=(vPanel_NotPopup.zIndex+1)/1;
			document.getElementById("content_Panel_Popup").style.position="absolute";
		}
	}catch(e){
	}


	
	ajustPanel();
	try{
		ajax_makeJSONRequest(action,{},"content_body_Panel_Popup","JSAfter_showAsPopup");	

	}catch(e){

	}

	try{
		document.getElementById("panel_Popup_img_maximize").style.display="block";
		document.getElementById("panel_Popup_img_minimize").style.display="none";
	}catch(e){
	}


	
}

function showAsPopupSubmit_json(formName,middle,panel_width,panel_height,scroll){
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
	document.getElementById("content_Panel_Popup").style.top=document.body.scrollTop;
	document.getElementById("content_Panel_Popup").style.left=document.body.scrollLeft;
	document.getElementById("content_Panel_Popup").style.display="block";
	
	try{
		if(vPanel_NotPopup.zIndex>-1){
			document.getElementById("content_Panel_Popup").style.zIndex=(vPanel_NotPopup.zIndex+1)/1;
			document.getElementById("content_Panel_Popup").style.position="absolute";
		}
	}catch(e){
	}

	
	ajustPanel();
	try{
		document.getElementById(formName).middleAction.value=middle;
		ajax_submit_json(document.getElementById(formName),"content_body_Panel_Popup","JSAfter_showAsPopup");
		
	}catch(e){
		alert(e);
	}
	

	
	var vPanel_Popup = mPanel_Popup.get("panel_Popup");
	if(vPanel_Popup==null){
	}else{
		if(vPanel_Popup.maximize==true){
			showAsPanelMax("panel_Popup");
		}else showAsPanelNormal("panel_Popup");
	}

}

function showAsPopupSubmitExt_json(formName,action,panel_width,panel_height,scroll){
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
	
	document.getElementById("content_Panel_Popup").style.top=document.body.scrollTop;
	document.getElementById("content_Panel_Popup").style.display="block";
	
	try{
		if(vPanel_NotPopup.zIndex>-1){
			document.getElementById("content_Panel_Popup").style.zIndex=(vPanel_NotPopup.zIndex+1)/1;
			document.getElementById("content_Panel_Popup").style.position="absolute";
		}
	}catch(e){
	}

	
	ajustPanel();
	try{
		ajax_submitExt_json(document.getElementById(formName),action,"content_body_Panel_Popup","JSAfter_showAsPopup");
		
	}catch(e){
		alert(e);
	}
	
	var vPanel_Popup = mPanel_Popup.get("panel_Popup");
	if(vPanel_Popup==null){
	}else{
		if(vPanel_Popup.maximize==true){
			showAsPanelMax("panel_Popup");
		}else showAsPanelNormal("panel_Popup");
	}

}

//---

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
		alert(e);
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
			vPanel_Popup.width=document.getElementById(id_body).style.width;
			vPanel_Popup.height=document.getElementById(id_body).style.height;

			vPanel_Popup.maximize=false;
	
	
	
			
			try{
				document.getElementById(id_body).style.width=document.body.clientWidth-40;
			}catch(e){		
			}
			try{
				document.getElementById(id_body).style.height=document.body.clientHeight-40;
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
		

		vPanel_NotPopup = new objPopup();
		vPanel_NotPopup.width=document.getElementById(panel_id).style.width;
		vPanel_NotPopup.height=document.getElementById(panel_id).style.height
		vPanel_NotPopup.position=document.getElementById(panel_id).style.position;
		vPanel_NotPopup.zIndex=document.getElementById(panel_id).style.zIndex;
		vPanel_NotPopup.maximize=false;
		
		document.getElementById(panel_id).style.position="absolute";

		document.getElementById(panel_id).style.top=5;
		document.getElementById(panel_id).style.left=5;
		document.getElementById(panel_id).style.width=document.body.clientWidth-5;
		document.getElementById(panel_id).style.height=document.body.clientHeight -5;
		
		vPanel_NotPopup.maximize=true;

		ajustPanelMinMaxNotPopup(panel_id);
//		ajustPanel();
		
		showInternalAsPanelMax(panel_id);
		
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
			document.getElementById(id_body).style.width=vPanel_Popup.width;
			document.getElementById(id_body).style.height=vPanel_Popup.height;

			vPanel_Popup.maximize=false;
			ajustPanelMinMax(panel_id);
	//		ajustPanel();
			return;
		}

	}

	
	try{
		
		document.getElementById(panel_id).style.position=vPanel_NotPopup.position;

		document.getElementById(panel_id).style.width=vPanel_NotPopup.width;
		document.getElementById(panel_id).style.height=vPanel_NotPopup.height;
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
	mPanel_Popup.remove(panel_id);
	

	
}




function JSAfter_showAsPopup(){

}

function ajustPanel(){
	
	var list = document.getElementsByTagName("DIV");

	for(var i=0;i<list.length;i++){
		try{

			if(list[i].id && list[i].id.indexOf("content_Panel_")==0){
				list[i].style.width = document.body.clientWidth;	
				list[i].style.height = document.body.clientHeight;	
			}
		}catch(e){			
		}
		
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
		if (n == 0) alert(str + " was not found on this page.");
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
			}else	alert(str + " was not found on this page.");
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
        this.keyArray = this.keyArray.removeAt(elementIndex);
        this.valArray = this.valArray.removeAt(elementIndex);
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