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

