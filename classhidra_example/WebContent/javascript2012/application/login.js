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
	}catch(e){

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
