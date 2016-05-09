<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<%@ include file="../framework/header.jsp" %> 

<script language="javascript" src="javascript2012/application/login.js"></script>

<body >

<div id="content_Panel_canvas" 
style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; overflow:hidden;"
>
<table border="0" width="100%" height="100%" cellpadding="0" cellspacing="0">
<tr>
<td align="center" bgcolor="">

<jsp:include page="/jsp/included/panel_top.jsp" >
	<jsp:param name="panel-id" value="panel_About" />	
	<jsp:param name="panel-onclose" value="window.close();" />
</jsp:include>	

<div id="page" style=" width: 900; height: 530; background-color: white">
<%@ include file="../included/content_page_header.jsp" %>

<center>
	<bs:form method="post" onhistory="">

<table border="0" width="100%"  height="95%" >
<tr>
	<td align="left" style="border-bottom-style: solid 1px; min-height: 50px" valign="middle" height="10%">	
		<table cellspacing="0" >	
		<tr>
			<td align="right" class="labelLeft"><nobr><b>Item:</b></nobr></td>
			<td align="right" class_="labelLeft" style="border: 1px solid silver; border-right: none"><nobr>Id</nobr></td>
			<td width="30"  style="border: 1px solid silver; border-left: none; border-right: none;"><bs:input name="item.id" size="30" toUpperCase="no" styleClass ="input200" style="width:30px"/></td>
			<td align="right" class_="labelLeft" style="border: 1px solid silver; border-left: none; border-right: none;"><nobr>Name</nobr></td>
			<td width="100"  style="border: 1px solid silver; border-left: none; border-right: none;"><bs:input name="item.name" size="30" toUpperCase="no" styleClass ="input200" style="width:100px"/></td>
			<td align="right" class_="labelLeft" style="border: 1px solid silver; border-left: none; border-right: none;"><nobr>Surname</nobr></td>
			<td width="100" style="border: 1px solid silver; border-left: none"><bs:input name="item.surname" size="30" toUpperCase="no" styleClass ="input200" style="width:100px"/></td>
			
			<td>&nbsp;</td>
			<td width="50" style="width:50px"><script>ObjectDraw("page1","button",10,"GET","getItem(false)","page_section","","images/menu/","",true,24,"50");</script></td>
			<td>&nbsp;</td>
			<td width="100" style="width:100px"><script>ObjectDraw("page1","button",11,"POST (new)","postItem(false)","page_section","","images/menu/","",true,24,"100");</script></td>
			<td>&nbsp;</td>
			<td width="100" style="width:100px"><script>ObjectDraw("page1","button",12,"PUT (update)","putItem(false)","page_section","","images/menu/","",true,24,"100");</script></td>
			<td>&nbsp;</td>
			<td width="50" style="width:50px"><script>ObjectDraw("page1","button",13,"DELETE","deleteItem(false)","page_section","","images/menu/","",true,24,"50");</script></td>
		</tr>
		<tr>
			<td align="right" class="labelLeft"><nobr><b>Link:</b></nobr></td>
			<td colspan="6"><bs:input name="link" size="370" toUpperCase="no" styleClass ="input200" style="width:370px" value="rest/examples/"/></td>
			
			<td>&nbsp;</td>
			<td width="50" style="width:50px"><script>ObjectDraw("page1","button",14,"GET","getItems(true)","page_section","","images/menu/","",true,24,"50");</script></td>
			<td>&nbsp;</td>
			<td width="100" style="width:100px"></td>
			<td>&nbsp;</td>
			<td width="100" style="width:100px"></td>
			<td>&nbsp;</td>
			<td width="50" style="width:50px"></td>
		</tr>
		</table>	
	</td>
</tr>
<tr>
	<td align="center" style="border-bottom-style: solid 1px; min-height: 400px" valign="middle" height="90%">	
	
		<textarea id="rest_canvas" style="width: 100%; height: 100%" readonly="readonly"></textarea>
	
	</td>
</tr>
</table>
	

	
	
	</bs:form>
	
<script>
function getItems(fromField){
	var url = "rest/examples/";
	if(fromField==true)
		url = document.getElementById("link").value;
	ajax_makeRequest(
			url,
			"rest_canvas",
			"",
			"",
			false,
			"",
			"GET",
			true
			);
}	
function getItem(fromField){	
	var url = "rest/examples/";
	if(fromField==true)
		url = document.getElementById("link").value;
	else{
		url = "rest/examples/"+document.getElementById("item.id").value;
		document.getElementById("link").value = url;
	}
		
	ajax_makeRequest(
			url,
			"rest_canvas",
			"",
			"",
			false,
			"",
			"GET",
			true
			);
}




/*
function putItem(fromField){	
	var url = "rest/examples/";
	if(fromField==true)
		url = document.getElementById("link").value;
	else{
		url = "rest/examples/"+document.getElementById("item.id").value;
		document.getElementById("link").value = url;
	}
	var formdata = new FormData();
	formdata.append("item.name", document.getElementById("item.name").value);
	formdata.append("item.surname", document.getElementById("item.surname").value);

	ajax_makeMPARTRequest(
			url,
			formdata,
			"rest_canvas",
			"",
			"",
			false,
			"PUT"
			);
}

function postItem(fromField){	
	var url = "rest/examples/";
	if(fromField==true)
		url = document.getElementById("link").value;
	else{
		url = "rest/examples/";
		document.getElementById("link").value = url;
	}
	var formdata = new FormData();
	formdata.append("item.id", document.getElementById("item.id").value);
	formdata.append("item.name", document.getElementById("item.name").value);
	formdata.append("item.surname", document.getElementById("item.surname").value);

	ajax_makeMPARTRequest(
			url,
			formdata,
			"rest_canvas",
			"",
			"",
			false,
			"POST"
			);
}
*/

function putItem(fromField){	
	var url = "rest/examples/";
	if(fromField==true)
		url = document.getElementById("link").value;
	else{
		url = "rest/examples/"+document.getElementById("item.id").value;
		document.getElementById("link").value = url;
	}

	ajax_makeJSONRequest(
			url,			
			{
				"item":{
					"name":document.getElementById("item.name").value,
					"surname":document.getElementById("item.surname").value
				}
			},
			"rest_canvas",
			"",
			"",
			false,
			"",
			"PUT",
			true
			);
}

function postItem(fromField){	
	var url = "rest/examples/";
	if(fromField==true)
		url = document.getElementById("link").value;
	else{
		url = "rest/examples/";
		document.getElementById("link").value = url;
	}

	ajax_makeJSONRequest(
			url,
			{
				"item":{
					"id":document.getElementById("item.id").value,
					"name":document.getElementById("item.name").value,
					"surname":document.getElementById("item.surname").value
				}
			},
			"rest_canvas",
			"",
			"",
			false,
			"",
			"POST",
			true
			);
}

function deleteItem(fromField){	
	var url = "rest/examples/";
	if(fromField==true)
		url = document.getElementById("link").value;
	else{
		url = "rest/examples/"+document.getElementById("item.id").value;
		document.getElementById("link").value = url;
	}
		
	ajax_makeRequest(
			url,
			"rest_canvas",
			"",
			"",
			false,
			"",
			"DELETE",
			true
			);
}

getItems(false);
</script>	

</center>



</div>
<jsp:include  page="/jsp/included/panel_bottom.jsp" />


</td>
</tr>
</table>
</div>
</body>
<%@ include file="../framework/footer.jsp" %>

