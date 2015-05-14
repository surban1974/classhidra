/**
* Name: js4ajax.js
* Version: 1.4.11 (compatible classHidra 1.4.11)
* Creation date: (21/04/2015)
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

	var e = document.createElement("link");

	if(inbase64){
		var parameters = ajax_makeParameters64(null,href);
		if(href.indexOf("?")>-1)
			e.href = href.substring(0,href.indexOf("?"))+parameters;
		else
			e.href = href+parameters;
	}else
		e.href = href;

	if(type){
		if(type!="") e.type=type;
	}else
		e.type="text/css";


	if(rel){
		if(rel!="") e.rel=rel;
	}else
		e.rel="stylesheet";

	if(media){
		   if(media!="") e.media=media;
	}
	if(charset){
		   if(charset!="") e.charset=charset;
	}
	try{
		if(afterJSFunction && afterJSFunction!=""){
			if (typeof afterJSFunction === "function") {
				//real browsers
			    e.onload=afterJSFunction;
			    //Internet explorer
			    e.onreadystatechange = function() {
			        if (this.readyState == 'complete') {
			        	afterJSFunction();
			        }
			    }

			}else{
				//real browsers
			    	e.onload=eval(afterJSFunction + "()");
			    //Internet explorer
				    e.onreadystatechange = function() {
				        if (this.readyState == 'complete') {
				        	eval(afterJSFunction + "()");
				        }
				    }
			}
		}
	}catch(e){
	}
	try{
		document.getElementsByTagName("head")[0].appendChild(e);
	}catch(e){
	}

}

// Javascript way
function dhtmlLoadScript(url,type,rel,charset,inbase64,afterJSFunction){

	var e = document.createElement("script");

	if(inbase64){
		var parameters = ajax_makeParameters64(null,url);
		if(url.indexOf("?")>-1)
			e.src = url.substring(0,url.indexOf("?"))+parameters;
		else
			e.src = url+parameters;
	}else
		e.src = url;

	if(type){
		if(type!="") e.type=type;
	}else
		e.type="text/javascript";

	if(rel){
		if(rel!="") e.rel=rel;
	}
	if(charset){
		   if(charset!="") e.charset=charset;
	}
	try{
		if(afterJSFunction && afterJSFunction!=""){
			if (typeof afterJSFunction === "function") {
				//real browsers
			    e.onload=afterJSFunction;
			    //Internet explorer
			    e.onreadystatechange = function() {
			        if (this.readyState == 'complete') {
			        	afterJSFunction();
			        }
			    }

			}else{
				//real browsers
			    	e.onload=eval(afterJSFunction + "()");
			    //Internet explorer
				    e.onreadystatechange = function() {
				        if (this.readyState == 'complete') {
				        	eval(afterJSFunction + "()");
				        }
				    }
			}
		}
	}catch(e){
	}
	try{
		document.getElementsByTagName("head")[0].appendChild(e);
	}catch(e){
	}

}

function dhtmlLoadScript_submit(url,frm,type,rel,charset,inbase64,afterJSFunction){
	var e = document.createElement("script");
	var parameters;
	if(inbase64){
		parameters = ajax_makeParameters64(frm,url);
		if(url.indexOf("?")>-1)
			e.src = url.substring(0,url.indexOf("?"))+parameters;
		else
			e.src = url+parameters;
	}else{
		parameters = ajax_makeParameters(frm,url);
		e.src = url+parameters;
	}


	if(type){
		if(type!="") e.type=type;
	}else
		e.type="text/javascript";

	if(rel){
		if(rel!="") e.rel=rel;
	}
	if(charset){
		   if(charset!="") e.charset=charset;
	}
	try{
		if(afterJSFunction && afterJSFunction!=""){
			if (typeof afterJSFunction === "function") {
				//real browsers
			    e.onload=afterJSFunction;
			    //Internet explorer
			    e.onreadystatechange = function() {
			        if (this.readyState == 'complete') {
			        	afterJSFunction();
			        }
			    }

			}else{
				//real browsers
			    	e.onload=eval(afterJSFunction + "()");
			    //Internet explorer
				    e.onreadystatechange = function() {
				        if (this.readyState == 'complete') {
				        	eval(afterJSFunction + "()");
				        }
				    }
			}
		}
	}catch(e){
	}
	try{
		document.getElementsByTagName("head")[0].appendChild(e);
	}catch(e){
	}

}

// Simple parameters string way

function ajax_submit(frm,target,afterJSFunction,inbase64,redrawTargetJSFunction,showImgBack){

	if(document.getElementById(target) ){
		var parameters;
		   if(inbase64)
			   parameters = ajax_makeParameters64(frm,frm.action);
		   else parameters = ajax_makeParameters(frm,frm.action);


		ajax_makeRequest(frm.action+parameters,target,afterJSFunction,redrawTargetJSFunction,showImgBack);
	}else{
		frm.submit();
	}
}

function ajax_submitExt(frm,action,target,afterJSFunction,inbase64,redrawTargetJSFunction,showImgBack){

	if(document.getElementById(target) ){
		var parameters;
		   if(inbase64)
			   parameters = ajax_makeParameters64(frm,action);
		   else parameters = ajax_makeParameters(frm,action);


		ajax_makeRequest(action+parameters,target,afterJSFunction,redrawTargetJSFunction,showImgBack);
	}else{
		frm.submit();
	}
}

function ajax_makeParameters(frm,url) {

    var getstr = "?";
    if(url.indexOf("?")>-1) getstr="&";

    if(frm){
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
    }
    return getstr;
 }

function ajax_makeParameters64(frm,url) {

    var getstr = "?$inputBase64=true&";

    if(url.indexOf("?")>-1){
    	var urlParameters=url.substring(url.indexOf("?")+1,url.length);
    	if(urlParameters.length>0){
    		urlParameters = decodeURIComponent(urlParameters);
    		var chunks = urlParameters.split('&');
    		for(var c=0; c < chunks.length; c++){
    			var split = chunks[c].split('=', 2);
    			getstr+=split[0]+"="+encodeURIComponent(base64_encode(split[1]))+"&";
    		}
    	}
    }


    if(frm){
	    for (i=0; i<frm.elements.length; i++) {
			var element = frm.elements[i];

			if(	url.indexOf("?"+element.name+"=")==-1 &&
					url.indexOf("&"+element.name+"=")==-1){
				if(element.name && element.name!=""){
					if	(element.type.toUpperCase() == "TEXT" ||
				         element.type.toUpperCase() == "HIDDEN" ||
				         element.type.toUpperCase() == "PASSWORD") {
			        	getstr += element.name + "=" + encodeURIComponent(base64_encode(element.value)) + "&";
				    }
					if	(element.type.toUpperCase() == "TEXTAREA") {
				        	getstr += element.name + "=" + encodeURIComponent(base64_encode(element.value)) + "&";
					}

				    if (element.type.toUpperCase() == "CHECKBOX") {
				    	if (element.checked) {
				        	getstr += element.name + "=" + encodeURIComponent(base64_encode(element.value)) + "&";
				        } else {
				        	getstr += element.name + "=&";
				        }
				    }
				    if (element.type.toUpperCase() == "RADIO") {
				    	if (element.checked) {
				        	getstr += element.name + "=" + encodeURIComponent(base64_encode(element.value)) + "&";
				        }
				    }

					if (element.type.toUpperCase().indexOf("SELECT")==0) {
				    	var sel = element;
				    	try{
				    		getstr += sel.name + "=" + encodeURIComponent(base64_encode(sel.options[sel.selectedIndex].value)) + "&";
				    	}catch(e){
				    	}
				    }
				}
			}
	    }
    }
    return getstr;
 }

function ajax_makeRequest(urlWidthParameters,target,afterJSFunction,redrawTargetJSFunction,showImgBack) {
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
		            }else{
		            	if(afterJSFunction && afterJSFunction!=""){
		            		if (typeof afterJSFunction === "function") {
		            			afterJSFunction(http_request,target);
		            		}else{
		            			eval(afterJSFunction + "(http_request,target)");
		            		}
		            	}		            	
		            }
		            http_request.close();
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



//JSON way
function ajax_submit_json(frm,target,afterJSFunction,inbase64,redrawTargetJSFunction,showImgBack){

	if(document.getElementById(target) ){
		var json;
		if(inbase64)
			json = ajax_makeJSONParameters64(frm,url);
		else json = ajax_makeJSONParameters(frm,frm.action);

		ajax_makeJSONRequest(frm.action,json,target,afterJSFunction,redrawTargetJSFunction,showImgBack);
	}else{
		frm.submit();
	}
}

function ajax_submitExt_json(frm,action,target,afterJSFunction,inbase64,redrawTargetJSFunction,showImgBack){

	if(document.getElementById(target) ){
		var json;
		if(inbase64)
			json = ajax_makeJSONParameters64(frm,url);
		else json = ajax_makeJSONParameters(frm,frm.action);
		ajax_makeJSONRequest(action,json,target,afterJSFunction,redrawTargetJSFunction,showImgBack);
	}else{
		frm.submit();
	}
}

function ajax_makeJSONParameters(frm,url) {

    var issue = {};


    if(url.indexOf("?")>-1){
    	var urlParameters=url.substring(url.indexOf("?")+1,url.length);
    	if(urlParameters.length>0){
    		urlParameters = decodeURIComponent(urlParameters);
    		var chunks = urlParameters.split('&');
    		for(var c=0; c < chunks.length; c++){
    			var split = chunks[c].split('=', 2);
    			issue[split[0]] = split[1];
    		}
    	}
    }

    try{
	    for (i=0; i<frm.elements.length; i++) {
	 		var element = frm.elements[i];
			if(	url.indexOf("?"+element.name+"=")==-1 &&
				url.indexOf("&"+element.name+"=")==-1){

				if(element.name && element.name!=""){

					if	(element.type.toUpperCase() == "TEXT" ||
				         element.type.toUpperCase() == "HIDDEN" ||
				         element.type.toUpperCase() == "PASSWORD") {
						issue[element.name ] = (element.value);
				    }
					if	(element.type.toUpperCase() == "TEXTAREA") {
						issue[element.name ] = (element.value);
					}

				    if (element.type.toUpperCase() == "CHECKBOX") {
				    	if (element.checked) {
							issue[element.name ] = (element.value);
				        } else {
							issue[element.name ] = "";

				        }
				    }
				    if (element.type.toUpperCase() == "RADIO") {
				    	if (element.checked) {
							issue[element.name ] = (element.value);
				        }
				    }

					if (element.type.toUpperCase().indexOf("SELECT")==0) {
				    	var sel = element;
				    	try{
							issue[sel.name ] = (sel.options[sel.selectedIndex].value);
				    	}catch(e){
				    	}
				    }
				}
			}

	    }
    }catch(e){
    }


    return issue;
 }

function ajax_makeJSONParameters64(frm,url) {

    var issue = {};

    issue[$inputBase64]="true";

    if(url.indexOf("?")>-1){
    	var urlParameters=url.substring(url.indexOf("?")+1,url.length);
    	if(urlParameters.length>0){
    		urlParameters = decodeURIComponent(urlParameters);
    		var chunks = urlParameters.split('&');
    		for(var c=0; c < chunks.length; c++){
    			var split = chunks[c].split('=', 2);
    			issue[split[0]] = base64_encode(split[1]);
    		}
    	}
    }

    try{
	    for (i=0; i<frm.elements.length; i++) {
	 		var element = frm.elements[i];
			if(	url.indexOf("?"+element.name+"=")==-1 &&
				url.indexOf("&"+element.name+"=")==-1){

				if(element.name && element.name!=""){

					if	(element.type.toUpperCase() == "TEXT" ||
				         element.type.toUpperCase() == "HIDDEN" ||
				         element.type.toUpperCase() == "PASSWORD") {
						issue[element.name ] = base64_encode(element.value);
				    }
					if	(element.type.toUpperCase() == "TEXTAREA") {
						issue[element.name ] = base64_encode(element.value);
					}

				    if (element.type.toUpperCase() == "CHECKBOX") {
				    	if (element.checked) {
							issue[element.name ] = base64_encode(element.value);
				        } else {
							issue[element.name ] = "";

				        }
				    }
				    if (element.type.toUpperCase() == "RADIO") {
				    	if (element.checked) {
							issue[element.name ] = base64_encode(element.value);
				        }
				    }

					if (element.type.toUpperCase().indexOf("SELECT")==0) {
				    	var sel = element;
				    	try{
							issue[sel.name ] = base64_encode(sel.options[sel.selectedIndex].value);
				    	}catch(e){
				    	}
				    }
				}
			}

	    }
    }catch(e){
    }


    return issue;
 }


function ajax_makeJSONRequest(urlWidthParameters,jsonParameters,target,afterJSFunction,redrawTargetJSFunction,showImgBack) {

		var urlOnly="";

		if(urlWidthParameters.indexOf("?")>-1){
			var pos = urlWidthParameters.indexOf("?");
			urlOnly = urlWidthParameters.substring(0,pos);
	    	var urlParameters=urlWidthParameters.substring(pos+1,urlWidthParameters.length);
	    	if(urlParameters.length>0){
	    		urlParameters = decodeURIComponent(urlParameters);
	    		var chunks = urlParameters.split('&');
	    		for(var c=0; c < chunks.length; c++){
	    			var split = chunks[c].split('=', 2);
	    			jsonParameters[split[0]] = split[1];
	    		}
	    	}
		}else{
			urlOnly=urlWidthParameters;
		}

		jsonParameters["js4ajax"] = "true";



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
		            http_request.close();
		        }
	    	}catch(e){

	    	}

        };

        var json_string = JSON.stringify(jsonParameters);

	    http_request.open("POST", urlOnly, true);

	    http_request.setRequestHeader("Content-length", json_string.length);
	    http_request.setRequestHeader("Connection", "close");
	    http_request.setRequestHeader("Content-type", "application/json");
	    http_request.setRequestHeader("Content-Encoding", "iso-8859-1");
//	    http_request.setRequestHeader("Content-Encoding", "utf-8");


	    http_request.send(json_string);

 }



// FormData way
function ajax_submit_mpart(frm,target,afterJSFunction,inbase64,redrawTargetJSFunction,showImgBack){
	if(window.FormData){

		if(document.getElementById(target) ){
			var formdata;
			if(inbase64)
				formdata = ajax_makeMPARTParameters64(frm,url);
			else formdata = ajax_makeMPARTParameters(frm,frm.action);

			ajax_makeMPARTRequest(frm.action,formdata,target,afterJSFunction,redrawTargetJSFunction,showImgBack);
		}else{
			frm.submit();
		}
	}else{
		alert("Features supported for object FormData (). Available in Chrome, FireFox, Safari, IE9...");
	}
}

function ajax_submitExt_mpart(frm,action,target,afterJSFunction,inbase64,redrawTargetJSFunction,showImgBack){
	if(window.FormData){
		if(document.getElementById(target) ){
			var formdata;
			if(inbase64)
				formdata = ajax_makeMPARTParameters64(frm,url);
			else formdata = ajax_makeMPARTParameters(frm,frm.action);
			ajax_makeMPARTRequest(action,formdata,target,afterJSFunction,redrawTargetJSFunction,showImgBack);
		}else{
			frm.submit();
		}
	}else{
		alert("Features supported for object FormData (). Available in Chrome, FireFox, Safari, IE9...");
	}
}

function ajax_makeMPARTParameters(frm,url) {
	if(window.FormData){

		var formdata = new FormData();


	    if(url.indexOf("?")>-1){
	    	var urlParameters=url.substring(url.indexOf("?")+1,url.length);
	    	if(urlParameters.length>0){
	    		urlParameters = decodeURIComponent(urlParameters);
	    		var chunks = urlParameters.split('&');
	    		for(var c=0; c < chunks.length; c++){
	    			var split = chunks[c].split('=', 2);
	    			formdata.append(split[0], split[1]);
	    		}
	    	}
	    }

	    try{
		    for (i=0; i<frm.elements.length; i++) {
		 		var element = frm.elements[i];
				if(	url.indexOf("?"+element.name+"=")==-1 &&
					url.indexOf("&"+element.name+"=")==-1){

					if(element.name && element.name!=""){

						if	(element.type.toUpperCase() == "TEXT" ||
					         element.type.toUpperCase() == "HIDDEN" ||
					         element.type.toUpperCase() == "PASSWORD") {
								formdata.append(element.name, element.value);
					    }
						if	(element.type.toUpperCase() == "TEXTAREA") {
							formdata.append(element.name, element.value);
						}

					    if (element.type.toUpperCase() == "CHECKBOX") {
					    	if (element.checked) {
					    		formdata.append(element.name, element.value);
					        } else {
					        	formdata.append(element.name, "");
					        }
					    }
					    if (element.type.toUpperCase() == "RADIO") {
					    	if (element.checked) {
					    		formdata.append(element.name, element.value);
					        }
					    }

						if (element.type.toUpperCase().indexOf("SELECT")==0) {
					    	var sel = element;
					    	try{
					    		formdata.append(sel.name, (sel.options[sel.selectedIndex].value));
					    	}catch(e){
					    	}
					    }
					    if (element.type.toUpperCase() == "FILE") {
					    	if (element.checked) {
					    		formdata.append(element.name, element.files[0]);
					        }
					    }

					}
				}

		    }
	    }catch(e){
	    }


	    return formdata;
	}else{
		alert("Features supported for object FormData (). Available in Chrome, FireFox, Safari, IE9...");
	}
 }


function ajax_makeMPARTParameters64(frm,url) {
	if(window.FormData){
		var formdata = new FormData();


	    if(url.indexOf("?")>-1){
	    	var urlParameters=url.substring(url.indexOf("?")+1,url.length);
	    	if(urlParameters.length>0){
	    		urlParameters = decodeURIComponent(urlParameters);
	    		var chunks = urlParameters.split('&');
	    		for(var c=0; c < chunks.length; c++){
	    			var split = chunks[c].split('=', 2);
	    			formdata.append(split[0], base64_encode(split[1]));
	    		}
	    	}
	    }

	    try{
		    for (i=0; i<frm.elements.length; i++) {
		 		var element = frm.elements[i];
				if(	url.indexOf("?"+element.name+"=")==-1 &&
					url.indexOf("&"+element.name+"=")==-1){

					if(element.name && element.name!=""){

						if	(element.type.toUpperCase() == "TEXT" ||
					         element.type.toUpperCase() == "HIDDEN" ||
					         element.type.toUpperCase() == "PASSWORD") {
								formdata.append(element.name, base64_encode(element.value));
					    }
						if	(element.type.toUpperCase() == "TEXTAREA") {
							formdata.append(element.name, base64_encode(element.value));
						}

					    if (element.type.toUpperCase() == "CHECKBOX") {
					    	if (element.checked) {
					    		formdata.append(element.name, base64_encode(element.value));
					        } else {
					        	formdata.append(element.name, "");
					        }
					    }
					    if (element.type.toUpperCase() == "RADIO") {
					    	if (element.checked) {
					    		formdata.append(element.name, base64_encode(element.value));
					        }
					    }

						if (element.type.toUpperCase().indexOf("SELECT")==0) {
					    	var sel = element;
					    	try{
					    		formdata.append(sel.name, base64_encode(sel.options[sel.selectedIndex].value));
					    	}catch(e){
					    	}
					    }
					    if (element.type.toUpperCase() == "FILE") {
				    		formdata.append(element.name, element.files[0]);
					    }

					}
				}

		    }
	    }catch(e){
	    }


	    return formdata;
	}else{
		alert("Features supported for object FormData (). Available in Chrome, FireFox, Safari, IE9...");
	}
 }


function ajax_makeMPARTRequest(urlWidthParameters,formdata,target,afterJSFunction,redrawTargetJSFunction,showImgBack) {
	if(window.FormData){
		var urlOnly="";

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
		            if (http_request.status == 200 ) {
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

        http_request.open("POST",urlOnly, true);
        http_request.send(formdata);
	}else{
		alert("Features supported for object FormData (). Available in Chrome, FireFox, Safari.");
	}

 }


// --------
/*
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
*/
// -------
function base64_encode(data) {

	  // From: http://phpjs.org/functions
	  // +   original by: Tyler Akins (http://rumkin.com)
	  // +   improved by: Bayron Guevara
	  // +   improved by: Thunder.m
	  // +   improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
	  // +   bugfixed by: Pellentesque Malesuada
	  // +   improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
	  // +   improved by: Rafal Kukawski (http://kukawski.pl)

	  // mozilla has this native
	  // - but breaks in 2.0.0.12!

	  if (typeof this.window['btoa'] === 'function') {
//	      return btoa(data);
		  return btoa(unescape(encodeURIComponent(data)));
	  }
	  var b64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
	  var o1, o2, o3, h1, h2, h3, h4, bits, i = 0,
	    ac = 0,
	    enc = "",
	    tmp_arr = [];

	  if (!data) {
	    return data;
	  }

	  do { // pack three octets into four hexets
	    o1 = data.charCodeAt(i++);
	    o2 = data.charCodeAt(i++);
	    o3 = data.charCodeAt(i++);

	    bits = o1 << 16 | o2 << 8 | o3;

	    h1 = bits >> 18 & 0x3f;
	    h2 = bits >> 12 & 0x3f;
	    h3 = bits >> 6 & 0x3f;
	    h4 = bits & 0x3f;

	    // use hexets to index into b64, and append result to encoded string
	    tmp_arr[ac++] = b64.charAt(h1) + b64.charAt(h2) + b64.charAt(h3) + b64.charAt(h4);
	  } while (i < data.length);

	  enc = tmp_arr.join('');

	  var r = data.length % 3;

	  return (r ? enc.slice(0, r - 3) : enc) + '==='.slice(r || 3);

	}

