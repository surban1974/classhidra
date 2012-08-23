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
<bs:form name="formRisources" onhistory="" method="post">


<div align="center">
<br>
	<table width="750px" cellpadding="0" cellspacing="4">
	<tr>
		<td align="left" class="labelLeft" width="100">APP_INIT</td>
		<td align="left" class="labelContent" ><bs:formelement name="app_init"/></td>
		<td></td>
	</tr>
	<tr>
		<td align="left" class="labelLeft" width="100">AUTH_INIT</td>
		<td align="left" class="labelContent" ><bs:formelement name="auth_init"/></td>
		<td width="10"><script>ObjectDraw("page25","button",25,"","runSubmitAjaxLoadScript('formRisources','authentication_config')","page_section","reload.gif","images/menu/");</script></td>
	</tr>	
	<tr>
		<td align="left" class="labelLeft" width="100">LOG_INIT</td>
		<td align="left" class="labelContent" ><bs:formelement name="log_init"/></td>
		<td width="10"><script>ObjectDraw("page25","button",25,"","runSubmitAjaxLoadScript('formRisources','log_init')","page_section","reload.gif","images/menu/");</script></td>
	</tr>		
	<tr>
		<td align="left" class="labelLeft" width="100">DB_INIT</td>
		<td align="left" class="labelContent" ><bs:formelement name="db_init"/></td>
		<td width="10"><script>ObjectDraw("page24","button",24,"","runSubmitAjaxLoadScript('formRisources','db_init')","page_section","reload.gif","images/menu/");</script></td>
	</tr>		
	<tr>
		<td align="left" class="labelLeft" width="100">ACTIONS_CONFIG</td>
		<td align="left" class="labelContent" ><bs:formelement name="actions_config"/></td>
		<td width="10"><script>ObjectDraw("page25","button",25,"","runSubmitAjaxLoadScript('formRisources','actions_config')","page_section","reload.gif","images/menu/");</script></td>
	</tr>		
	<tr>
		<td align="left" class="labelLeft" width="100">AUTHENTICATIONS_CONFIG</td>
		<td align="left" class="labelContent" ><bs:formelement name="authentication_config"/></td>
		<td width="10"><script>ObjectDraw("page26","button",26,"","runSubmitAjaxLoadScript('formRisources','authentication_config')","page_section","reload.gif","images/menu/");</script></td>

	</tr>		
	<tr>
		<td align="left" class="labelLeft" width="100">MENU_CONFIG</td>
		<td align="left" class="labelContent" ><bs:formelement name="menu_config"/></td>
		<td width="10"><script>ObjectDraw("page27","button",27,"","runSubmitAjaxLoadScript('formRisources','menu_config')","page_section","reload.gif","images/menu/");</script></td>
	</tr>		
	<tr>
		<td align="left" class="labelLeft" width="100">USERS_CONFIG</td>
		<td align="left" class="labelContent" ><bs:formelement name="users_config"/></td>
		<td width="10"><script>ObjectDraw("page28","button",28,"","runSubmitAjaxLoadScript('formRisources','users_config')","page_section","reload.gif","images/menu/");</script></td>

	</tr>	
	<tr>
		<td align="left" class="labelLeft" width="100">ORGANIZATIONS_CONFIG</td>
		<td align="left" class="labelContent" ><bs:formelement name="organizations_config"/></td>
		<td width="10"><script>ObjectDraw("page29","button",29,"","runSubmitAjaxLoadScript('formRisources','organizations_config')","page_section","reload.gif","images/menu/");</script></td>

	</tr>		
		
	<tr>
		<td align="left" class="labelLeft" width="100" valign="top">MESSAGES_CONFIG</td>
		<td align="left" class="labelContent" ><bs:formelement name="messages_config"/></td>
		<td width="10" valign="top"><script>ObjectDraw("page30","button",30,"","runSubmitAjaxLoadScript('formRisources','messages_config')","page_section","reload.gif","images/menu/");</script></td>
	</tr>	
	<tr>
		<td align="left" class="labelLeft" width="100">PROJECT_INIT</td>
		<td align="left" class="labelContent" ><bs:formelement name="project_init"/></td>
		<td width="10"></td>
	</tr>						
	</table>

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

