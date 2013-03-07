



function ajax_linkFromMenu(url,target,js_script){
	dhtmlLoadScript(js_script);
	ajax_makeRequest(url,target);
}

function ajax_submit(frm,target,afterJSFunction){
	
	if(document.getElementById(target) ){
		var parameters = ajax_makeParameters(frm,frm.action);
		
		ajax_makeRequest(frm.action+parameters,target,afterJSFunction);
	}else{	
		frm.submit();
	}		
}

function ajax_submitExt(frm,action,target,afterJSFunction){
	
	if(document.getElementById(target) ){
		var parameters = ajax_makeParameters(frm,action);
		
		ajax_makeRequest(action+parameters,target,afterJSFunction);
	}else{	
		frm.submit();
	}		
}

function ajax_loadMessage(target){
	ajax_loadAction("messages.action",target);
}


function ajax_loadAction(action,target, callBackJs){
	ajax_makeRequest(action,target,callBackJs);
}

function dhtmlLoadScript(url,type,rel){
	
   var e = document.createElement("script");
   e.src = url;
   if(type){
	   e.type=type;
   }else{
	   e.type="text/javascript";
   }
   if(rel){
	   e.rel=rel;
   }

	try{
	   document.getElementsByTagName("head")[0].appendChild(e);
	}catch(e){
	
	}

}

function dhtmlLoadScript_submit(url,frm,type,rel){
	
	   var e = document.createElement("script");
	   var parameters = ajax_makeParameters(frm,url);
  
	   e.src = url+parameters;
	   if(type){
		   e.type=type;
	   }else{
		   e.type="text/javascript";
	   }
	   if(rel){
		   e.rel=rel;
	   }


		try{
		   document.getElementsByTagName("head")[0].appendChild(e);
		}catch(e){
		
		}

	}


function ajax_makeParameters(frm,url) {

    var getstr = "?";
    if(url.indexOf("?")>-1) getstr="&";


    for (i=0; i<frm.elements.length; i++) {
   	
		var element = frm.elements[i];
	
		if(	url.indexOf("?"+element.name+"=")==-1 &&
				url.indexOf("&"+element.name+"=")==-1){
		
			if(element.name && element.name!=""){
				
				if	(element.type.toUpperCase() == "TEXT" ||
			         element.type.toUpperCase() == "HIDDEN" ||
			         element.type.toUpperCase() == "PASSWORD") {
		        	getstr += element.name + "=" + encodeURIComponent(element.value) + "&";
			    }
				if	(element.type.toUpperCase() == "TEXTAREA") {
			        	getstr += element.name + "=" + encodeURIComponent(element.value) + "&";
				}
				
			    if (element.type.toUpperCase() == "CHECKBOX") {
			    	if (element.checked) {
			        	getstr += element.name + "=" + encodeURIComponent(element.value) + "&";
			        } else {
			        	getstr += element.name + "=&";
			        }
			    }
			    if (element.type.toUpperCase() == "RADIO") {
			    	if (element.checked) {
			        	getstr += element.name + "=" + encodeURIComponent(element.value) + "&";
			        }
			    }
	  
				if (element.type.toUpperCase().indexOf("SELECT")==0) {
			    	var sel = element;
			    	try{
			    		getstr += sel.name + "=" + encodeURIComponent(sel.options[sel.selectedIndex].value) + "&";
			    	}catch(e){
			    	}
			    }
			}			    
		}
		        
    }
   
    return getstr;
 }

function ajax_makeRequest(urlWidthParameters,target,afterJSFunction, callBackJs,showImgBack) {



		var urlOnly="";
		var parametersOnly="";
		
		if(urlWidthParameters.indexOf("?")>-1){
			var pos = urlWidthParameters.indexOf("?");
			urlOnly = urlWidthParameters.substring(0,pos);
			parametersOnly = urlWidthParameters.substring(pos+1);
		}else{
			urlOnly=urlWidthParameters;			
		}
	
		if(parametersOnly=="") parametersOnly+="js4ajax=true";
		else parametersOnly+="&js4ajax=true";

	
		if(	target=="spacer"){
		}else{	
			try{
				var viewBack=true;
				if(showImgBack==false) viewBack=false;
			}catch(e){
				viewBack=true;
			}
			try{
				if(viewBack==true)
					document.getElementById(target).innerHTML="<table border='0' width='100%' height='100%'><tr><td align='center'><img src='images/wait.gif' border='0'></td></tr></table>";
			}catch(e){
			}
		}	
		
		var http_request = false;
		
		
		

	    if (window.ActiveXObject) { // IE
	       try {

	          http_request = new ActiveXObject("Msxml2.XMLHTTP");
	       } catch (e) {
	          try {
	             http_request = new ActiveXObject("Microsoft.XMLHTTP");
             
	          } catch (e) {}
	       }
	    }
	    if (window.XMLHttpRequest) { // Mozilla, Safari,...
		       http_request = new XMLHttpRequest();
		       if (http_request.overrideMimeType) {
		          //http_request.overrideMimeType('text/xml');
		          http_request.overrideMimeType('text/html');
		       }
		
	    }
	    if (!http_request) {
	       alert('Cannot create XMLHTTP instance');
	       return false;
	    }
	
	    http_request.onreadystatechange = function() { 
	    	try{
		    	if (http_request.readyState == 4) {
		            if (http_request.status == 200 ) {
		            	if(callBackJs && callBackJs!=""){
		            		eval(callBackJs + "(http_request,target)");
		            	}else{
		            		document.getElementById(target).innerHTML=http_request.responseText;	
		            	}
		            	if(afterJSFunction && afterJSFunction!=""){
		            		eval(afterJSFunction + "()");
		            	}	
		            } else {
		            	var error_mess = http_request.responseText;
//		            	ajax_makeRequest("messages.bs?error_mess="+encodeURIComponent(error_mess)+"&data_rif="+new Date().getTime(),"idDivMessage");

		            }
		        } 
	    	}catch(e){
//	    		alert('There was a generic problem with callback_function():'+e.toString());
	    	}

        };


        
	    http_request.open("POST", urlOnly, true);
	    
	    http_request.setRequestHeader("Content-length", parametersOnly.length);
	    http_request.setRequestHeader("Connection", "close");
	    http_request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");	    
	    http_request.setRequestHeader("Content-Encoding", "iso-8859-1");
//	    http_request.setRequestHeader("Content-Encoding", "utf-8");
   
	    http_request.send(parametersOnly);
	    
	   	
 }


function ajax_handleResponse(http_request,target) {
	try{
	    if (http_request.readyState == 4) {
	       if (http_request.status == 200) {
	          result = http_request.responseText;
	          var content_ajax = result;	
	          document.getElementById(target).innerHTML=content_ajax;
	          
//	          if(target!="idDivMessage") ajax_loadMessage("idDivMessage");
	          
	       } else {
	          alert('There was a problem with the request.');
	       }
	       
	    }
	    http_request.close();
	} catch (e) {
		alert(e);
    }
 }

