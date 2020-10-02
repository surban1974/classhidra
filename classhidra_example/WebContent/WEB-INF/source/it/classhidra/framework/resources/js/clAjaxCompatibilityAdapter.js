/**
* Name: clAjaxCompatibilityAdapter.js
* Version: 1.5.4 (compatible classHidra 1.5.4)
* Creation date: (28/09/2018)
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
	current.setCompatibility(true);
	current.setAsCss(true);
	if(href)
		current.setUrl(href);
	if(type)
		current.setContentType(type);
	if(rel)
		current.setRel(rel);
	if(charset)
		current.setContentEncoding(charset);
	if(media)
		current.setMedia(media);
	if(inbase64)
		current.setBase64(inbase64);
	if(afterJSFunction){
		current.setSuccess(afterJSFunction);
		current.setFail(afterJSFunction);
	}
	
	current.load();
}

// Javascript way
function dhtmlLoadScript(url,type,rel,charset,inbase64,afterJSFunction){
	var current = new clajax();
	current.setCompatibility(true);
	current.setAsScript(true);
	if(url)
		current.setUrl(url);
	if(type)
		current.setContentType(type);
	if(rel)
		current.setRel(rel);
	if(charset)
		current.setContentEncoding(charset);
	if(inbase64)
		current.setBase64(inbase64);
	if(afterJSFunction){
		current.setSuccess(afterJSFunction);
		current.setFail(afterJSFunction);
	}
	
	current.load();
}

function dhtmlLoadScript_submit(url,frm,type,rel,charset,inbase64,afterJSFunction){

	var current = new clajax();
	current.setCompatibility(true);
	current.setAsScript(true);
	if(frm)
		current.setForm(frm);
	if(url)
		current.setUrl(url);
	if(type)
		current.setContentType(type);
	if(rel)
		current.setRel(rel);
	if(charset)
		current.setContentEncoding(charset);
	if(inbase64)
		current.setBase64(inbase64);
	if(afterJSFunction){
		current.setSuccess(afterJSFunction);
		current.setFail(afterJSFunction);
	}
	
	current.load();
}

// Simple parameters string way

function ajax_submit(frm,target,afterJSFunction,inbase64,redrawTargetJSFunction,showImgBack,responseType){
	if(document.getElementById(target) ){
		var current = new clajax();
		current.setCompatibility(true);
		if(frm)
			current.setForm(frm);
		if(target && document.getElementById(target))
			current.setTarget(document.getElementById(target));
		if(afterJSFunction){
			current.setSuccess(afterJSFunction);
			current.setFail(afterJSFunction);
		}
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
		current.setCompatibility(true);
		if(frm)
			current.setForm(frm);
		if(action)
			current.setUrl(action);
		if(target && document.getElementById(target))
			current.setTarget(document.getElementById(target));
		if(afterJSFunction){
			current.setSuccess(afterJSFunction);
			current.setFail(afterJSFunction);
		}
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
	var current = new clajax();
	current.setCompatibility(true);
	var url = current.getParametersAsUrl(frm,url);
	return url;
 }

function ajax_makeParameters64(frm,url) {
	var current = new clajax();
	current.setCompatibility(true);
	current.setBase64(true);
	var url = current.getParametersAsUrl(frm,url);
	return url;
}

function ajax_makeRequest(urlWidthParameters,target,afterJSFunction,redrawTargetJSFunction,showImgBack,responseType,requestMethod,anyServerStatus,outer) {	
	var current = new clajax();
	current.setCompatibility(true);
	if(urlWidthParameters)
		current.setUrl(urlWidthParameters);
	if(target && document.getElementById(target))
		current.setTarget(document.getElementById(target));
	if(afterJSFunction){
		current.setSuccess(afterJSFunction);
		current.setFail(afterJSFunction);
	}
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
	else
		current.setMethod('GET');
	if(outer)
		current.setOuter(outer);
	current.request();
 }



//JSON way
function ajax_submit_json(frm,target,afterJSFunction,inbase64,redrawTargetJSFunction,showImgBack,responseType){
	if(document.getElementById(target) ){
		var current = new clajax();
		current.setCompatibility(true);
		current.setJson(true);
		if(frm)
			current.setForm(frm);
		if(target && document.getElementById(target))
			current.setTarget(document.getElementById(target));
		if(afterJSFunction){
			current.setSuccess(afterJSFunction);
			current.setFail(afterJSFunction);
		}
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
		current.setCompatibility(true);
		current.setJson(true);
		if(frm)
			current.setForm(frm);
		if(action)
			current.setUrl(action);
		if(target && document.getElementById(target))
			current.setTarget(document.getElementById(target));
		if(afterJSFunction){
			current.setSuccess(afterJSFunction);
			current.setFail(afterJSFunction);
		}
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
	var current = new clajax();
	current.setCompatibility(true);
	var json = current.getParametersAsJson(frm,url);
	return json;
 }

function ajax_makeJSONParameters64(frm,url) {
	var current = new clajax();
	current.setCompatibility(true);
	current.setBase64(true);
	var json = current.getParametersAsJson(frm,url);
	return json;
 }


function ajax_makeJSONRequest(urlWidthParameters,jsonParameters,target,afterJSFunction,redrawTargetJSFunction,showImgBack,responseType,requestMethod,anyServerStatus) {
	var current = new clajax();
	current.setCompatibility(true);
	current.setAsJson(true);
	if(urlWidthParameters)
		current.setUrl(urlWidthParameters);
	if(jsonParameters)
		current.setJson(jsonParameters);
	if(target && document.getElementById(target))
		current.setTarget(document.getElementById(target));
	if(afterJSFunction){
		current.setSuccess(afterJSFunction);
		current.setFail(afterJSFunction);
	}
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
			current.setCompatibility(true);
			current.setAsMpart(true);
			if(frm)
				current.setForm(frm);
			if(target && document.getElementById(target))
				current.setTarget(document.getElementById(target));
			if(afterJSFunction){
				current.setSuccess(afterJSFunction);
				current.setFail(afterJSFunction);
			}
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
			current.setCompatibility(true);
			current.setAsMpart(true);
			if(frm)
				current.setForm(frm);
			if(target && document.getElementById(target))
				current.setTarget(document.getElementById(target));
			if(afterJSFunction){
				current.setSuccess(afterJSFunction);
				current.setFail(afterJSFunction);
			}
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
		var current = new clajax();
		current.setCompatibility(true);
		var formdata = current.getParametersAsMpart(frm,url);
		return formdata;
	}else{
		alert("Features supported for object FormData (). Available in Chrome, FireFox, Safari, IE9...");
	}
 }


function ajax_makeMPARTParameters64(frm,url) {
	if(window.FormData){
		var current = new clajax();
		current.setCompatibility(true);
		current.setBase64(true);
		var formdata = current.getParametersAsMpart(frm,url);
		return formdata;			
	}else{
		alert("Features supported for object FormData (). Available in Chrome, FireFox, Safari, IE9...");
	}
 }


function ajax_makeMPARTRequest(urlWidthParameters,formdata,target,afterJSFunction,redrawTargetJSFunction,showImgBack,requestMethod,anyServerStatus) {
	if(window.FormData){
		
		var current = new clajax();
		current.setCompatibility(true);
		current.setAsMpart(true);
		if(urlWidthParameters)
			current.setUrl(urlWidthParameters);
		if(formdata)
			current.setMpart(formdata);
		if(target && document.getElementById(target))
			current.setTarget(document.getElementById(target));
		if(afterJSFunction){
			current.setSuccess(afterJSFunction);
			current.setFail(afterJSFunction);
		}
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

		
		if(requestMethod)
			current.setMethod(requestMethod);
		current.request();

		

	}else{
		alert("Features supported for object FormData (). Available in Chrome, FireFox, Safari.");
	}

 }




