/**
* Name: clAjaxCompatibilityAdapter.js
* Version: 1.5.4 (compatible classHidra 1.5.4)
* Creation date: (16/07/2018)
* Last update: 
* @author: Svyatoslav Urbanovych svyatoslav.urbanovych@gmail.com
*/



function ajax_linkFromMenu(url,target,js_script){
	dhtmlLoadScript(js_script);
	ajax_makeRequest(url,target);
}



function ajax_loadMessage(target){
	ajax_loadAction("messages.action",target);
}


function ajax_loadAction(action,target, afterJSFunction){
	ajax_makeRequest(action,target,afterJSFunction);
}

function dhtmlLoadCss(href,type,rel,charset,media,inbase64,afterJSFunction){
	var current = new clajax();
	current.setAsCss(true);
	if(href)
		current.setUrl(href);
	if(type)
		current.setType(type);
	if(rel)
		current.setRel(rel);
	if(charset)
		current.setContentEncoding(charset);
	if(media)
		current.setMedia(media);
	if(inbase64)
		current.setBase64(inbase64);
	if(afterJSFunction)
		current.setSuccess(afterJSFunction);
	
	current.load();
}

// Javascript way
function dhtmlLoadScript(url,type,rel,charset,inbase64,afterJSFunction){
	var current = new clajax();
	current.setAsScript(true);
	if(href)
		current.setUrl(href);
	if(type)
		current.setType(type);
	if(rel)
		current.setRel(rel);
	if(charset)
		current.setContentEncoding(charset);
	if(media)
		current.setMedia(media);
	if(inbase64)
		current.setBase64(inbase64);
	if(afterJSFunction)
		current.setSuccess(afterJSFunction);
	
	current.load();
}

function dhtmlLoadScript_submit(url,frm,type,rel,charset,inbase64,afterJSFunction){

	var current = new clajax();
	if(frm)
		current.setForm(frm);
	if(href)
		current.setUrl(href);
	if(type)
		current.setType(type);
	if(rel)
		current.setRel(rel);
	if(charset)
		current.setContentEncoding(charset);
	if(media)
		current.setMedia(media);
	if(inbase64)
		current.setBase64(inbase64);
	if(afterJSFunction)
		current.setSuccess(afterJSFunction);
	
	current.load();
}

// Simple parameters string way

function ajax_submit(frm,target,afterJSFunction,inbase64,redrawTargetJSFunction,showImgBack,responseType){
	if(document.getElementById(target) ){
		var current = new clajax();
		if(frm)
			current.setForm(frm);
		if(target && document.getElementById(target))
			current.setTarget(document.getElementById(target));
		if(afterJSFunction)
			current.setSuccess(afterJSFunction);
		if(redrawTargetJSFunction)
			current.setReady(redrawTargetJSFunction);	
		if(showImgBack){
			try{
				var viewBack=true;
				if(showImgBack==false) viewBack=false;
			}catch(e){
				viewBack=true;
			}
			try{
				if(viewBack==true)
					current.setProgressWait("<table border='0' width='100%' height='100%'><tr><td align='center'><img id='img_ajax_makeRequest' src='images/wait.gif' border='0'></td></tr></table>");
			}catch(e){
			}
		}
		if(inbase64)
			current.setBase64(inbase64);
		if(responseType)
			current.setResponseType(responseType);
		
		current.submit();
	}else{
		if(frm)
			frm.submit();
	}
}

function ajax_submitExt(frm,action,target,afterJSFunction,inbase64,redrawTargetJSFunction,showImgBack,responseType){
	if(document.getElementById(target) ){
		var current = new clajax();
		if(frm)
			current.setForm(frm);
		if(action)
			current.setUrl(action);
		if(target && document.getElementById(target))
			current.setTarget(document.getElementById(target));
		if(afterJSFunction)
			current.setSuccess(afterJSFunction);
		if(redrawTargetJSFunction)
			current.setReady(redrawTargetJSFunction);	
		if(showImgBack){
			try{
				var viewBack=true;
				if(showImgBack==false) viewBack=false;
			}catch(e){
				viewBack=true;
			}
			try{
				if(viewBack==true)
					current.setProgressWait("<table border='0' width='100%' height='100%'><tr><td align='center'><img id='img_ajax_makeRequest' src='images/wait.gif' border='0'></td></tr></table>");
			}catch(e){
			}
		}
		if(inbase64)
			current.setBase64(inbase64);
		if(responseType)
			current.setResponseType(responseType);
		
		current.submit();
	}else{
		if(frm)
			frm.submit();
	}
}

function ajax_makeParameters(frm,url) {
	return 
		new clajax()
			.getParametersAsUrl(frm,url);
 }

function ajax_makeParameters64(frm,url) {
	return 
		new clajax()
			.setBase64(true)
			.getParametersAsUrl(frm,url); }

function ajax_makeRequest(urlWidthParameters,target,afterJSFunction,redrawTargetJSFunction,showImgBack,responseType,requestMethod,anyServerStatus) {	
	var current = new clajax();
	if(urlWidthParameters)
		current.setUrl(urlWidthParameters);
	if(target && document.getElementById(target))
		current.setTarget(document.getElementById(target));
	if(afterJSFunction)
		current.setSuccess(afterJSFunction);
	if(redrawTargetJSFunction)
		current.setReady(redrawTargetJSFunction);	
	if(showImgBack){
		try{
			var viewBack=true;
			if(showImgBack==false) viewBack=false;
		}catch(e){
			viewBack=true;
		}
		try{
			if(viewBack==true)
				current.setProgressWait("<table border='0' width='100%' height='100%'><tr><td align='center'><img id='img_ajax_makeRequest' src='images/wait.gif' border='0'></td></tr></table>");
		}catch(e){
		}
	}
	if(anyServerStatus)
		current.setEnlargeServerStatus(anyServerStatus);
	if(responseType)
		current.setResponseType(responseType);
	
	if(requestMethod)
		current.setMethod(requestMethod);
	current.request();
 }



//JSON way
function ajax_submit_json(frm,target,afterJSFunction,inbase64,redrawTargetJSFunction,showImgBack,responseType){
	if(document.getElementById(target) ){
		var current = new clajax();
		current.setJson(true);
		if(frm)
			current.setForm(frm);
		if(target && document.getElementById(target))
			current.setTarget(document.getElementById(target));
		if(afterJSFunction)
			current.setSuccess(afterJSFunction);
		if(redrawTargetJSFunction)
			current.setReady(redrawTargetJSFunction);	
		if(showImgBack){
			try{
				var viewBack=true;
				if(showImgBack==false) viewBack=false;
			}catch(e){
				viewBack=true;
			}
			try{
				if(viewBack==true)
					current.setProgressWait("<table border='0' width='100%' height='100%'><tr><td align='center'><img id='img_ajax_makeRequest' src='images/wait.gif' border='0'></td></tr></table>");
			}catch(e){
			}
		}
		if(inbase64)
			current.setBase64(inbase64);
		if(responseType)
			current.setResponseType(responseType);
		
		current.submit();
	}else{
		if(frm)
			frm.submit();
	}

}

function ajax_submitExt_json(frm,action,target,afterJSFunction,inbase64,redrawTargetJSFunction,showImgBack,responseType){
	if(document.getElementById(target) ){
		var current = new clajax();
		current.setJson(true);
		if(frm)
			current.setForm(frm);
		if(action)
			current.setUrl(action);
		if(target && document.getElementById(target))
			current.setTarget(document.getElementById(target));
		if(afterJSFunction)
			current.setSuccess(afterJSFunction);
		if(redrawTargetJSFunction)
			current.setReady(redrawTargetJSFunction);	
		if(showImgBack){
			try{
				var viewBack=true;
				if(showImgBack==false) viewBack=false;
			}catch(e){
				viewBack=true;
			}
			try{
				if(viewBack==true)
					current.setProgressWait("<table border='0' width='100%' height='100%'><tr><td align='center'><img id='img_ajax_makeRequest' src='images/wait.gif' border='0'></td></tr></table>");
			}catch(e){
			}
		}
		if(inbase64)
			current.setBase64(inbase64);
		if(responseType)
			current.setResponseType(responseType);
		
		current.submit();
	}else{
		if(frm)
			frm.submit();
	}

}

function ajax_makeJSONParameters(frm,url) {
	return 
		new clajax()
			.getParametersAsJson(frm,url);
 }

function ajax_makeJSONParameters64(frm,url) {
	return 
		new clajax()
			.setBase64(true)
			.getParametersAsJson(frm,url);
 }


function ajax_makeJSONRequest(urlWidthParameters,jsonParameters,target,afterJSFunction,redrawTargetJSFunction,showImgBack,responseType,requestMethod,anyServerStatus) {
	var current = new clajax();
	current.setAsJson(true);
	if(urlWidthParameters)
		current.setUrl(urlWidthParameters);
	if(jsonParameters)
		current.setJson(jsonParameters);
	if(target && document.getElementById(target))
		current.setTarget(document.getElementById(target));
	if(afterJSFunction)
		current.setSuccess(afterJSFunction);
	if(redrawTargetJSFunction)
		current.setReady(redrawTargetJSFunction);	
	if(showImgBack){
		try{
			var viewBack=true;
			if(showImgBack==false) viewBack=false;
		}catch(e){
			viewBack=true;
		}
		try{
			if(viewBack==true)
				current.setProgressWait("<table border='0' width='100%' height='100%'><tr><td align='center'><img id='img_ajax_makeRequest' src='images/wait.gif' border='0'></td></tr></table>");
		}catch(e){
		}
	}
	if(anyServerStatus)
		current.setEnlargeServerStatus(anyServerStatus);
	if(responseType)
		current.setResponseType(responseType);
	
	if(requestMethod)
		current.setMethod(requestMethod);
	current.request();
 }



// FormData way
function ajax_submit_mpart(frm,target,afterJSFunction,inbase64,redrawTargetJSFunction,showImgBack){
	if(window.FormData){
		if(document.getElementById(target) ){
			var current = new clajax();
			current.setMpart(true);
			if(frm)
				current.setForm(frm);
			if(target && document.getElementById(target))
				current.setTarget(document.getElementById(target));
			if(afterJSFunction)
				current.setSuccess(afterJSFunction);
			if(redrawTargetJSFunction)
				current.setReady(redrawTargetJSFunction);	
			if(showImgBack){
				try{
					var viewBack=true;
					if(showImgBack==false) viewBack=false;
				}catch(e){
					viewBack=true;
				}
				try{
					if(viewBack==true)
						current.setProgressWait("<table border='0' width='100%' height='100%'><tr><td align='center'><img id='img_ajax_makeRequest' src='images/wait.gif' border='0'></td></tr></table>");
				}catch(e){
				}
			}
			if(inbase64)
				current.setBase64(inbase64);
			if(responseType)
				current.setResponseType(responseType);
			
			current.submit();
		}else{
			if(frm)
				frm.submit();
		}
	}else{
		alert("Features supported for object FormData (). Available in Chrome, FireFox, Safari, IE9...");
	}
}

function ajax_submitExt_mpart(frm,action,target,afterJSFunction,inbase64,redrawTargetJSFunction,showImgBack){
	if(window.FormData){
		if(document.getElementById(target) ){
			var current = new clajax();
			current.setMpart(true);
			if(frm)
				current.setForm(frm);
			if(target && document.getElementById(target))
				current.setTarget(document.getElementById(target));
			if(afterJSFunction)
				current.setSuccess(afterJSFunction);
			if(redrawTargetJSFunction)
				current.setReady(redrawTargetJSFunction);	
			if(showImgBack){
				try{
					var viewBack=true;
					if(showImgBack==false) viewBack=false;
				}catch(e){
					viewBack=true;
				}
				try{
					if(viewBack==true)
						current.setProgressWait("<table border='0' width='100%' height='100%'><tr><td align='center'><img id='img_ajax_makeRequest' src='images/wait.gif' border='0'></td></tr></table>");
				}catch(e){
				}
			}
			if(inbase64)
				current.setBase64(inbase64);
			if(responseType)
				current.setResponseType(responseType);
			
			current.submit();
		}else{
			if(frm)
				frm.submit();
		}
	}else{
		alert("Features supported for object FormData (). Available in Chrome, FireFox, Safari, IE9...");
	}
}

function ajax_makeMPARTParameters(frm,url) {
	if(window.FormData){
		return 
			new clajax()
				.getParametersAsMpart(frm,url);
	}else{
		alert("Features supported for object FormData (). Available in Chrome, FireFox, Safari, IE9...");
	}
 }


function ajax_makeMPARTParameters64(frm,url) {
	if(window.FormData){
		return 
			new clajax()
				.setBase64(true)
				.getParametersAsMpart(frm,url);
	}else{
		alert("Features supported for object FormData (). Available in Chrome, FireFox, Safari, IE9...");
	}
 }


function ajax_makeMPARTRequest(urlWidthParameters,formdata,target,afterJSFunction,redrawTargetJSFunction,showImgBack,requestMethod,anyServerStatus) {
	if(window.FormData){
		
		var current = new clajax();
		current.setAsMpart(true);
		if(urlWidthParameters)
			current.setUrl(urlWidthParameters);
		if(formdata)
			current.setMpart(formdata);
		if(target && document.getElementById(target))
			current.setTarget(document.getElementById(target));
		if(afterJSFunction)
			current.setSuccess(afterJSFunction);
		if(redrawTargetJSFunction)
			current.setReady(redrawTargetJSFunction);	
		if(showImgBack){
			try{
				var viewBack=true;
				if(showImgBack==false) viewBack=false;
			}catch(e){
				viewBack=true;
			}
			try{
				if(viewBack==true)
					current.setProgressWait("<table border='0' width='100%' height='100%'><tr><td align='center'><img id='img_ajax_makeRequest' src='images/wait.gif' border='0'></td></tr></table>");
			}catch(e){
			}
		}
		if(anyServerStatus)
			current.setEnlargeServerStatus(anyServerStatus);
		if(responseType)
			current.setResponseType(responseType);
		
		if(requestMethod)
			current.setMethod(requestMethod);
		current.request();

		
		var urlOnly="";
		var method="POST";
		var enlargeServerStatus = false;
		
		if(requestMethod && requestMethod!=""){
			method = requestMethod;
		}
		
		if(anyServerStatus)
			enlargeServerStatus = anyServerStatus;

		if(urlWidthParameters.indexOf("?")>-1){
			var pos = urlWidthParameters.indexOf("?");
			urlOnly = urlWidthParameters.substring(0,pos);
	    	var urlParameters=urlWidthParameters.substring(pos+1,urlWidthParameters.length);
	    	if(urlParameters.length>0){
	    		urlParameters = decodeURIComponent(urlParameters);
	    		var chunks = urlParameters.split('&');
	    		for(var c=0; c < chunks.length; c++){
	    			var split = chunks[c].split('=', 2);
	    			formdata.append(split[0], (split[1]));
	    		}
	    	}
		}else{
			urlOnly=urlWidthParameters;
		}

		formdata.append("js4ajax","true");



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

	    http_request.onload = function(e) {
	    	try{
	            if (	(enlargeServerStatus==false && http_request.status == 200)
	            		||
	            		(enlargeServerStatus==true && (http_request.status == 200 || http_request.status == 201 || http_request.status == 400 || http_request.status == 401 || http_request.status == 404 || http_request.status == 405))
	            ) {
		            	if(redrawTargetJSFunction && redrawTargetJSFunction!=""){
		            		if (typeof redrawTargetJSFunction === "function") {
		            			redrawTargetJSFunction(http_request,target);
		            		}else{
		            			eval(redrawTargetJSFunction + "(http_request,target)");
		            		}
		            	}else{
		            		document.getElementById(target).innerHTML=http_request.responseText;
		            	}
		            	if(afterJSFunction && afterJSFunction!=""){
		            		if (typeof afterJSFunction === "function") {
		            			afterJSFunction(http_request,target);
		            		}
		            		else{
		            			eval(afterJSFunction + "(http_request,target)");
		            		}
		            	}
		            	http_request.close();
		            }else{
		            	if(afterJSFunction && afterJSFunction!=""){
		            		if (typeof afterJSFunction === "function") {
		            			afterJSFunction(http_request,target);
		            		}else{
		            			eval(afterJSFunction + "(http_request,target)");
		            		}
		            	}		            	
		            }
	    	}catch(e){

	    	}

        };

        http_request.open(method,urlOnly, true);
        http_request.send(formdata);
	}else{
		alert("Features supported for object FormData (). Available in Chrome, FireFox, Safari.");
	}

 }




