/**
* Name: List.js (support for tagList.java)
* Version: 1.4.11 (compatible classHidra 1.4.11)
* Creation date: (21/04/2015)
* @author: Svyatoslav Urbanovych svyatoslav.urbanovych@gmail.com
*/

function inListArray(arr,obj_name){
	var result = false;
	try{
		for(var i=0;i<arr.length;i++){	
			if(arr[i]==obj_name) return true;
		}
	}catch(e){
	}
	return result;
}

function initListArray(arr){
	for(var i=0;i<arr.length;i++)
		viewListValue(arr[i],true);
}

function viewListValue(obj_name,type){
	if(document.getElementById(obj_name)){
		if(type){
			document.getElementById(obj_name).className = "colTabSel";
//			document.getElementById(obj_name).className+"Dark";
//			document.getElementById(obj_name).onmouseover();
		}
		else document.getElementById(obj_name).onmouseout();
	}
}

function refreshListArray(arr,values_arr,obj_name,obj_value){

	var buf_arr = new Array();
	var values_buf_arr = new Array();
	var founded = false;
	var k=0;
	
	for(var i=0;i<arr.length;i++){	
		var tr = arr[i];
		if(tr==obj_name){
			arr[i]="";
			values_arr[i]="";
			viewListValue(obj_name,false);
			founded=true;
		}else{
			buf_arr[k]=arr[i];
			values_buf_arr[k]=values_arr[i];
			k++;
		}
	}

	if(!founded){
		buf_arr[k]=obj_name;
		values_buf_arr[k]=obj_value;
		viewListValue(obj_name,true);
	}
	arr.length=0;
	var return_obj_value="";
	var separator="";
	if(buf_arr.length>1) separator=";";
	for(var i=0;i<buf_arr.length;i++){
		arr[i] = buf_arr[i];
		values_arr[i] = values_buf_arr[i];
		return_obj_value+=values_arr[i]+separator;
	}
	return return_obj_value;
}

function refreshListArray4One(arr,values_arr,obj_name,obj_value){


	var buf_arr = new Array();
	var values_buf_arr = new Array();
	var founded = false;
	var k=0;
	
	for(var i=0;i<arr.length;i++){	
		var tr = arr[i];
		if(tr==obj_name){
			founded=true;
		}else{
			buf_arr[k]=arr[i];
			values_buf_arr[k]=values_arr[i];
			k++;
		}
		arr[i]="";
		values_arr[i]="";
		viewListValue(tr,false);
		
	}

	if(!founded){
		buf_arr[k]=obj_name;
		values_buf_arr[k]=obj_value;
		viewListValue(obj_name,true);
	}
	arr.length=0;
	var return_obj_value="";

	for(var i=0;i<buf_arr.length;i++){
		arr[i] = buf_arr[i];
		values_arr[i] = values_buf_arr[i];
		return_obj_value=values_arr[i];
	}

	return return_obj_value;
}

function catchKeyUpDown(_control, _key){
	try{
		var div = document.getElementById("div_formBean_"+_control.name);
		var aElement = document.getElementById("a_div_formBean_"+_control.name);
		var inFocus=false;
		try{
			if(aElement === document.activeElement) inFocus=true;
		}catch(e){
		}
		if(div && inFocus){
			
			var list_tr = div.getElementsByTagName("TR");
			var pos=-1;
			for(var i=0;i<list_tr.length;i++){
				if(list_tr[i].id=="tr_formBean_"+_control.name+"_"+_control.value) pos=i;
			}
			var pos_next=pos;
			if( _key==38) pos_next--;
			if( _key==40) pos_next++;
			if(pos_next<0 || pos_next>list_tr.length-1) return;
			var tr_pox = list_tr[pos];
			var tr_pox_next = list_tr[pos_next];
		
			tr_pox.onmouseover= 
				function(){
					if(!inListArray(array_formBean_usercode,this.id)) this.className='rowSignOnOver';
				}			
			tr_pox.onmouseout=
				function(){
					if(!inListArray(array_formBean_usercode,this.id)) this.className='rowSignOn';
				}	 
			tr_pox.className = "rowSignOn";
			
			tr_pox_next.onmouseover=
				function(){
				}
			tr_pox_next.onmouseout=
				function(){
				}			
			tr_pox_next.className = "colTabSel";
			
			_control.value = tr_pox_next.id.replace("tr_formBean_"+_control.name+"_","");
		}
	}catch(e){
	}
	
}
