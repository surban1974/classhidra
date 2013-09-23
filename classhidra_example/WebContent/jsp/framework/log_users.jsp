<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 

<%@ include file="../framework/header.jsp" %>  
<body >

<div id="content_Panel_canvas" 
style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; overflow:hidden;"
>
<table border="0" width="100%" height="100%" cellpadding="0" cellspacing="0">
<tr>
<td align="center" bgcolor="">

<jsp:include page="/jsp/included/panel_top.jsp" >
	<jsp:param name="panel-id" value="panel_Login" />	
	<jsp:param name="panel-onclose" value="window.close();" />

</jsp:include>	

<div id="page" style=" width: 900; height: 530; background-color: white">
<%@ include file="../included/content_page_header.jsp" %>

<table>
<tr>

	<td align="center"><script>ObjectDraw("page1","button",1,"show stack activity","showAsPopup('statistisc_json?middleAction=showasxml',800,500)","page_section","","images/menu/","",true,24,"35");</script></td>
</tr>
</table>
<center>


<bs:form onhistory="" method="post" >

<table >

<bs:sequence bean="elements">
	<bs:bean name="current_auth" source="elements" index="sequence"/>
		<tr >	
			<td><img src="images/decor/16_users.gif" width="13"/></td>
			<td><b><bs:formelement bean="current_auth" name="_matricola"/></b></td>
			<td style="border: outset 1px;">&nbsp;<bs:formelement bean="current_auth" name="_ruolo"/>&#8594;<bs:formelement bean="current_auth" name="_userDesc"/>&nbsp;</td>
			<td style="border: outset 1px;">&nbsp;<bs:formelement bean="current_auth" name="_language"/>&nbsp;</td>
			<td style="border: outset 1px;">&nbsp;<bs:formelement bean="current_auth" name="_user_ip"/>&nbsp;</td>
			<td style="border: outset 1px;">&nbsp;<bs:formelement bean="current_auth" name="_user_property.LOGIN_TIME" formatOutput="dd/MM/yyyy HH:mm:ss"/>&nbsp;</td>
			<td style="border: outset 1px;">&nbsp;<bs:formelement bean="current_auth" name="_user_property.LASTUSE_TIME" formatOutput="dd/MM/yyyy HH:mm:ss"/>&nbsp;</td>
			<td style="border: outset 1px;">&nbsp;<bs:formelement bean="current_auth" name="_user_property.LASTUSE_ACTION"/>&nbsp;</td>
			<td><img src="images/menu/Delete.gif" style="cursor: pointer;" 
				onclick="goAction('log_users?middleAction=remove&session_id=<bs:formelement bean="current_auth" name="_user_property.LOGIN_SESSION_ID"/>')"
			></img></td>
			<td><img src="images/menu/13_help_about.gif"  style="cursor: pointer;" 
				onclick="showAsPopup('log_users?middleAction=view_mess&session_id=<bs:formelement bean="current_auth" name="_user_property.LOGIN_SESSION_ID"/>',600,400)"
			/> </td>			
		</tr>
	

</bs:sequence>
</table>
<br></br>

<div style="width: 700px; height:300px;border: none; " >
        <canvas id="graph" style="" width="700" height="300"

        ></canvas>
</div>

</bs:form> 
</center>



</div>
<jsp:include  page="/jsp/included/panel_bottom.jsp" />


</td>
</tr>
</table>
</div>
</body>
<%@ include file="../framework/footer.jsp" %>

<script>


	function loadJsonList_afterJSFunction(){
	}

	function loadJsonList_callBackJs(http_request,target){

		try{

			var canvas = document.getElementById('graph');
			var ctx = canvas.getContext('2d');

			ctx.clearRect(0, 0, canvas.width, canvas.height);


			ctx.strokeStyle = "rgb(0,102,204)";
				ctx.beginPath();
				ctx.moveTo(49, canvas.height-19);
				ctx.lineTo(49, 0);
				ctx.stroke();
	
				ctx.beginPath();
				ctx.moveTo(49, canvas.height-19);
				ctx.lineTo(canvas.width, canvas.height-19);
				ctx.stroke();


					
			var json_object;
			try{
				json_object = JSON.parse(http_request.responseText);
			}catch(e){
			}

			var maxY=0;
			var vmaxY="0";
			var startX="";
			var finishX="";
	
			for(i=0;i<json_object.data.length;i++){
				ctx.strokeStyle    = 'rgb(255, 0, 0)';
				ctx.beginPath();
				ctx.moveTo(json_object.data[i].x*0.65+50, canvas.height - json_object.data[i].y*0.28 -20);
				ctx.lineTo(json_object.data[i].x*0.65+50, canvas.height -20);
				ctx.stroke();

				if(startX=="") startX=json_object.data[i].vx;
				finishX=json_object.data[i].vx;

				if(maxY<json_object.data[i].y){
					maxY = json_object.data[i].y;
					vmaxY=json_object.data[i].vy;
				}
					
				
			}
			ctx.fillStyle    = 'rgb(255, 0, 0)';
			ctx.font         = 'normal 8px sans-serif';
			ctx.textBaseline = 'top';
			ctx.fillText  ("0 ms", 0,  canvas.height -25);
			ctx.fillText  (vmaxY, 0,  0);

			ctx.fillText  (startX, 50,  canvas.height-10);
			ctx.fillText  (finishX, canvas.width-85,  canvas.height-10);
	
		}catch(e){
		
		}

		
		
	}

	ajax_makeRequest("statistisc_json","","loadJsonList_afterJSFunction", "loadJsonList_callBackJs",false);

</script>

