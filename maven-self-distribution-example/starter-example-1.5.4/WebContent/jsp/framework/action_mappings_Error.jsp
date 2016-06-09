<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<%@page import="it.classhidra.core.tool.exception.message"%><%@ page import="java.util.*" %>
<%@ page import="it.classhidra.core.tool.log.stubs.iStub" %>
<%@ page import="it.classhidra.core.controller.bsController" %>
<%@ page import="it.classhidra.core.tool.exception.bsControllerException" %>
<%@ include file="../framework/header.jsp" %> 


<bs:link />
<%  

	String erroraction = "";
	try{
		erroraction = (String)request.getAttribute("$erroraction");
	}catch(Exception e){
	}	

	
	Vector $listmessages = new Vector();
	try{
		$listmessages = (Vector)request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE);
	}catch(Exception e){
	}
	if($listmessages==null) $listmessages = new Vector();	
	
	if((erroraction==null || erroraction.trim().equals("")) && $listmessages.size()==0)
		erroraction = "Generic Controller ERROR";
%> 



<center>
<bs:equal  bean="REQUEST.PARAMETER" name="js4ajax" value="true">
<table   height="100%" >
<tr>
<td align="center" valign="middle" >
	<table style="border-style: outset;border-width: 1px;">
<%if(erroraction!=null || $listmessages.size()==0){ %>
	<tr>
		<td width="1%"><img src="images/decor/64_mess_error.gif" border="0"></td>
		<td align="left">
			<font color="#B03C3C">
				<b><%@ include file="../included/show_navigate_story.jsp" %></b>
				<br><br>
				<b><%=erroraction%></b>
				</font>
			</td>
		</tr>	
<%}
for(int i=0;i<$listmessages.size();i++){
	message mess = (message)$listmessages.get(i);
	String color = "#B03C3C";
	String img = "images/decor/64_mess_error.gif";
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

<%} %>
			<tr>
				<td colspan="2" align="center" style="cursor: pointer;" onclick="goAction('login?middleAction=clear')">
					<table cellpadding="0" cellspacing="0" >
					<tr>
					<td>
					<img src="images/decor/32_profile.gif" border="0">
					</td>
					<td><font color="#54AA4C"><b><bs:message code="ajax_carrello_62" defaultValue="ReLogon"/></b></font></td>
					</tr>
					</table>
				</td>
			</tr>
	</table>		
</td>
</tr>
</table>		
</bs:equal>

<bs:notEqual  bean="REQUEST.PARAMETER" name="js4ajax" value="true">
<div id="content_Panel_Messages_AME" 
style="position: absolute; top: 0; left: 0; width: 100%; height: 100%;  "
>

<table border="0" width="100%" height="100%">
<tr>

<td align="center">



<jsp:include page="../included/panel_top.jsp">
	<jsp:param name="panel-show-header" value="true" />
	<jsp:param name="panel-show-close-button" value="false" /> 
	<jsp:param name="panel-description" value="Messaggi" />	
	<jsp:param name="panel-id" value="panel_Messages" />
	<jsp:param name="panel-onclose" value="document.getElementById('content_Panel_Messages_AME').style.display='none'" />	
</jsp:include>	


<table width="500" bgcolor="white">
<%if(erroraction!=null || $listmessages.size()==0){ %>
			<tr>
				<td width="1%">
				<img src="images/decor/64_mess_error.gif" border="0">
				</td>
				<td>
					<font color="#B03C3C">
					<b><%@ include file="../included/show_navigate_story.jsp" %></b>
					<br><br>
					<b><%=erroraction%></b>
					</font>
				</td>
			</tr>
<%}
for(int i=0;i<$listmessages.size();i++){
	message mess = (message)$listmessages.get(i);
	String color = "#B03C3C";
	String img = "images/decor/64_mess_error.gif";
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
%>		
	<tr>
		<td colspan="2" align="center" style="cursor: pointer;" onclick="goAction('login?')">
			<table cellpadding="0" cellspacing="0">
			<tr>
			<td>
			<img src="images/decor/32_profile.gif" border="0">
			</td>
			<td><font color="#54AA4C"><b><bs:message code="ajax_carrello_62" defaultValue="ReLogon"/></b></font></td>
			</tr>
			</table>
		</td>
	</tr>
</table>		

	
<jsp:include page="../included/panel_bottom.jsp"/>

</td>

</tr>
</table>


</div>
</bs:notEqual>
</center>

<script>

ajustPanel();
window.onresize = function() {ajustPanel();}


</script>

<%

if($listmessages.size()>0){

try{
	if(request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE)!=null)
		((Vector)request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE)).clear();
}catch(Exception e){
	new bsControllerException("Inpossible to clear the messages bean.",request,iStub.log_DEBUG);
}

}
%>	
