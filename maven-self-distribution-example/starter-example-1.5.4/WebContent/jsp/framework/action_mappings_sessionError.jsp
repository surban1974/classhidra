<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<%@ include file="../framework/header.jsp" %> 


<bs:link />
 



<center>
<bs:equal  bean="REQUEST.PARAMETER" name="js4ajax" value="true">
<table width="100%" >
			<tr>
				<td width="1%"><img src="images/decor/64_mess_error.gif" border="0"></td>
				<td>
					<font color="#B03C3C">
						<b><bs:message code="ajax_carrello_63" defaultValue="Current Session was lost."/></b>
					</font>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center" style="cursor: pointer;" onclick="goAction('login?middleAction=clear')">
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
</bs:equal>

<bs:notEqual  bean="REQUEST.PARAMETER" name="js4ajax" value="true">
<div id="content_Panel_Messages_AMS" 
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
	<jsp:param name="panel-onclose" value="document.getElementById('content_Panel_Messages_AMS').style.display='none'" />	
</jsp:include>	

<table width="500" bgcolor="white">

			<tr>
				<td width="1%">
				<img src="images/decor/64_mess_error.gif" border="0">
				</td>
				<td>
					<font color="#B03C3C">
					<b><bs:message code="ajax_carrello_63" defaultValue="Current Session was lost."/></b>
					</font>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center" style="cursor: pointer;" onclick="goAction('login?middleAction=clear')">
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
ajustPanel;
window.onresize = function(){
	ajustPanel;	
}
</script>
