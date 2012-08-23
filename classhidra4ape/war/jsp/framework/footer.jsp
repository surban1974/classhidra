
<%@page import="it.classhidra.core.tool.exception.message"%>
<%@ page import="java.util.*" %>
<%@ page import="it.classhidra.core.tool.log.stubs.iStub" %>
<%@ page import="it.classhidra.core.controller.bsController" %>
<%@ page import="it.classhidra.core.tool.exception.bsControllerException" %>

<jsp:include page="/jsp/included/page_popup.jsp"/>

<table width="100%" >
			<tr>
				<td align="center">		
					<img src="images/copyright.gif" border="0"/>
				</td>
			</tr>
</table>					

<div id="content_Panel_runSubmit" name="cp_popup"
style="z-index: 9999; position: absolute; top: 0; left: 0; width: 100%; height: 100%; display: none; background-image: url('images/decor/blank.gif'); filter: alpha(opacity=50);opacity: 0.5;  "
>
<table border="0" width="100%" height="100%" style="z-index:99999">
<tr>
<td align="center"><img src="images/wait.gif" border="0"></td>
</tr>
</table>
</div>
<%

Vector $listmessages = new Vector();
try{
	$listmessages = (Vector)request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE);
}catch(Exception e){
}
if($listmessages==null) $listmessages = new Vector();
if($listmessages.size()>0){
%>

<center>

<div id="content_Panel_Messages" name="cp_popup"
style="z-index: 9999; position: absolute; top: 0; left: 0; width: 100%; height: 100%;  "
>

<table border="0" width="100%" height="100%" style="z-index:99999">
<tr>

<td align="center">

<jsp:include page="/jsp/included/panel_top.jsp">
	<jsp:param name="panel-show-header" value="true" />
	<jsp:param name="panel-show-close-button" value="true" /> 
	<jsp:param name="panel-description" value="Messaggi" />	
	<jsp:param name="panel-id" value="panel_Messages" />
	<jsp:param name="panel-onclose" value="document.getElementById('content_Panel_Messages').style.display='none'" />	

</jsp:include>	


<div style="width: 500px; <%=(($listmessages.size()>4)?"height: 285px; overflow:auto;":"") %>vertical-align:middle;background-color:white">
<table  bgcolor="white" style="z-index:99999">
<%
}
for(int i=0;i<$listmessages.size();i++){
	message mess = (message)$listmessages.get(i);
	String color = "#DCC434";
	String img = "images/decor/64_mess_warning.gif";
	if(mess.getTYPE().equals("E")){
		color="#B03C3C";
		img="images/decor/64_mess_error.gif";
	}
	if(mess.getTYPE().equals("W")){
		color="#DCC434";
		img="images/decor/64_mess_warning.gif";
	}
	if(mess.getTYPE().equals("I")){
		color="#54AA4C";
		img="images/decor/64_mess_ok.gif";
	}
%>
			<tr>
				<td width="1%">
				<img src="<%=img %>" border="0">
				</td>
				<td>
					<font color="<%=color %>"><b>
					<%=mess.getDESC_MESS() %>
					</b></font>
				</td>
			</tr>
	
<%
}
if($listmessages.size()>0){
%>
</table>		
</div>
		
<jsp:include page="/jsp/included/panel_bottom.jsp"/>
</td>

</tr>
</table>

<div id="content_Panel_Messages_opacity" 
style="z-index: -1; position: absolute; top: 0; left: 0; width: 50%; height: 50%; background-image: url('images/decor/blank.gif'); filter: alpha(opacity=50);opacity: 0.5; "
></div>

</div>

</center>



<%
}
try{
	if(request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE)!=null)
		((Vector)request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE)).clear();
}catch(Exception e){
	new bsControllerException("Inpossible to clear the messages bean.",request,iStub.log_DEBUG);
}
%>

<div id="footer_fixedbox">
<jsp:include page="minimizer?middleAction=view"/>
</div>

<script>
ajustPanel();

window.onresize = function() { ajustPanel();}

function ajustPage1(){
	try{
//		var pos = getPosition(document.getElementById("fixedbox_show_navigate_story"));
		var div = document.getElementById("page1");
		var div_mc = document.getElementById("menu_command");
		var tr_mc = document.getElementById("tr_menu_command");
		var div_innerHTML = div.innerHTML;	
//		if(div_innerHTML.toLowerCase().indexOf("</td>")>-1){	
			div_mc.innerHTML = div_innerHTML;
			tr_mc.style.display="block";
//		}
//		div.style.top=pos.y+22;
//		div.style.left=pos.x;
		
//		div.style.visibility="visible";
	}catch(e){

	}	
}
ajustPage1();

</script>



