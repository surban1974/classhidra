function runActionOnEnter(){
	enter();
}

function enter(){
	try{	
		runSubmit("groups");
	}catch(e){

	}	
}

function enterGuest(){
	try{	
		document.forms[0].user.value="guest";
		document.forms[0].password.value="guest";
		runSubmit("");
	}catch(e){

	}	
}
function enterUser(){
	try{	
		document.forms[0].user.value="user";
		document.forms[0].password.value="user";
		runSubmit("");
	
//		showAsPopupSubmitExt_json("formLogin","login?testjson=true",400,300);
		
//var jsonParameters = ajax_makeJSONParameters(document.getElementById("formLogin"),"action?user1=1&tmp=2");
//alert(JSON.stringify(jsonParameters));
		
	}catch(e){
alert(e);
	}	
}

function changeLang(value){
	try{
		document.forms[0].lang.value=value;
		runSubmit("lang");
	}catch(e){

	}	
}
function clear_l(){
	try{	
		runSubmit("clear");
	}catch(e){

	}	
}
function groups(){
	try{	
		runSubmit("groups");
	}catch(e){

	}	
}
