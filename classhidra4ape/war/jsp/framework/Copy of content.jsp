<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<%@ include file="../framework/header.jsp" %> 

<script language="javascript" src="javascript/application/login.js"></script>

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

<center>
	<bs:form method="post" onhistory="">

<table border="0" width="100%" height="450" >
<tr>
<td align="center" style="border-bottom-style: solid 1px;" valign="middle">	


	<table border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="150" height="5" align="center" valign="middle">&nbsp;</td>
		<td width="150" height="5" align="center" valign="middle">&nbsp;</td>
		<td width="150" height="5" align="center" valign="middle">&nbsp;</td>
		<td width="150" height="5" align="center" valign="middle">&nbsp;</td>
		<td width="150" height="5" align="center" valign="middle">&nbsp;</td>
		<td width="150" height="5" align="center" valign="middle">&nbsp;</td>
	</tr>
	<tr>
		<td width="150" height="30" align="center" valign="middle">&nbsp;</td>
<bs:notEqual bean="actions_forbidden" name="sendMail">		
		<td width="150" height="150" align="center" valign="middle" style="cursor: pointer;" onclick="goAction('about')">
			<jsp:include page="/jsp/included/panel_top.jsp"/>
			<img src="images/decor/128_about.gif"></img>	
			<%request.setAttribute("mess-description","About");%>
			<jsp:include page="/jsp/included/panel_bottom.jsp"/>			
		</td>
</bs:notEqual>
<bs:equal bean="actions_forbidden" name="sendMail">		
		<td width="150" height="150" align="center" valign="middle" style="filter: alpha(opacity=30);opacity: 0.3;">
			<img src="images/decor/128_about.gif"></img>	
		</td>
</bs:equal>			
		<td width="150" height="150" align="center" valign="middle"></td>
<bs:notEqual bean="actions_forbidden" name="sendMail">		
		<td width="150" height="150" align="center" valign="middle" style="cursor: pointer;" onclick="goAction('sendMail')">
			<jsp:include page="/jsp/included/panel_top.jsp"/>
			<img src="images/decor/128_mail_sender.gif"></img>
			<%request.setAttribute("mess-description","Send Mail");%>
			<jsp:include page="/jsp/included/panel_bottom.jsp"/>			
		</td>
</bs:notEqual>
<bs:equal bean="actions_forbidden" name="sendMail">		
		<td width="150" height="150" align="center" valign="middle" style="filter: alpha(opacity=30);opacity: 0.3;">
			<img src="images/decor/128_mail_sender.gif"></img>
		</td>
</bs:equal>		
		<td width="150" height="150" align="center" valign="middle"></td>
		<td width="150" height="150" align="center" valign="middle"></td>
	</tr>

	<tr>
		<td width="150" height="100" align="center" valign="middle">&nbsp;</td>
		<td width="150" height="100" align="center" valign="middle">&nbsp;</td>
<bs:notEqual bean="actions_forbidden" name="builder">		
		<td width="150" height="150" align="center" valign="middle" style="cursor: pointer;" onclick="goAction('builder.bs?middleAction=reload')">
			<jsp:include page="/jsp/included/panel_top.jsp"/>
			<img src="images/decor/128_builder.gif"></img>
			<%request.setAttribute("mess-description","Builder (BETA)");%>
			<jsp:include page="/jsp/included/panel_bottom.jsp"/>
		</td>
</bs:notEqual>	
<bs:equal bean="actions_forbidden" name="builder">		
		<td width="150" height="150" align="center" valign="middle" style="filter: alpha(opacity=30);opacity: 0.3;">
			<img src="images/decor/128_builder.gif"></img>
		</td>
</bs:equal>	
		<td width="150" height="100" align="center" valign="middle">&nbsp;</td>
<bs:notEqual bean="actions_forbidden" name="amanager">		
		<td width="150" height="150" align="center" valign="middle" style="cursor: pointer;" onclick="goAction('amanager.bs?middleAction=reload')">
			<jsp:include page="/jsp/included/panel_top.jsp"/>
			<img src="images/decor/128_users.gif"></img>
			<%request.setAttribute("mess-description","Authentication");%>			
			<jsp:include page="/jsp/included/panel_bottom.jsp"/>
		</td>	
</bs:notEqual>
<bs:equal bean="actions_forbidden" name="amanager">		
		<td width="150" height="150" align="center" valign="middle" style="filter: alpha(opacity=30);opacity: 0.3;">
			<img src="images/decor/128_users.gif"></img>
		</td>	
</bs:equal>					
		<td width="150" height="150" align="center" valign="middle"></td>
	</tr>

	<tr>
		<td width="150" height="150" align="center" valign="middle"></td>
<bs:notEqual bean="actions_forbidden" name="log_users">			
		<td width="150" height="150" align="center" valign="middle" style="cursor: pointer;" onclick="goAction('log_users')">
			<jsp:include page="/jsp/included/panel_top.jsp"/>
			<img src="images/decor/128_sessions.gif"></img>
			<%request.setAttribute("mess-description","Users In Session");%>
			<jsp:include page="/jsp/included/panel_bottom.jsp"/>
		</td>
</bs:notEqual>	
<bs:equal bean="actions_forbidden" name="log_users">			
		<td width="150" height="150" align="center" valign="middle" style="filter: alpha(opacity=30);opacity: 0.3;">
			<img src="images/decor/128_sessions.gif"></img>
		</td>
</bs:equal>		
		<td width="150" height="150" align="center" valign="middle"></td>
<bs:notEqual bean="actions_forbidden" name="resources">		
		<td width="150" height="150" align="center" valign="middle" style="cursor: pointer;" onclick="goAction('resources')">
			<jsp:include page="/jsp/included/panel_top.jsp"/>
			<img src="images/decor/128_resources.gif"></img>
			<%request.setAttribute("mess-description","Resources");%>			
			<jsp:include page="/jsp/included/panel_bottom.jsp"/>
		</td>	
</bs:notEqual>
<bs:equal bean="actions_forbidden" name="resources">		
		<td width="150" height="150" align="center" valign="middle" style="filter: alpha(opacity=30);opacity: 0.3;">
			<img src="images/decor/128_resources.gif"></img>
		</td>	
</bs:equal>			
		
		<td width="150" height="150" align="center" valign="middle"></td>
		<td width="150" height="150" align="center" valign="middle"></td>
	</tr>	
		
	</table>

</td>
</tr>
</table>

	
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

