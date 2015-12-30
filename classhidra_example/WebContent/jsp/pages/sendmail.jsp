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


	<table >

	
		<tr>
			<td align="right" width="200" class="labelLeft"><nobr><b><bs:message code="sendmail_1" defaultValue="Name"/>:</b></nobr></td>
			<td width="200" ><bs:input name="s_name" size="30" toUpperCase="no" styleClass ="input200"/></td>
			<td width="200">&nbsp;</td>
			<td width="200">&nbsp;</td>
		</tr>	
		<tr>
			<td align="right" width="200" class="labelLeft"><nobr><b><bs:message code="sendmail_2" defaultValue="EMail"/>:</b></nobr></td>
			<td width="200" ><bs:input name="s_email" size="30" toUpperCase="no" styleClass ="input200"/></td>
			<td width="200">&nbsp;</td>
			<td width="200">&nbsp;</td>
		</tr>	
		<tr>
			<td align="right" width="200" class="labelLeft"><nobr><b><bs:message code="sendmail_3" defaultValue="Message"/>:</b></nobr></td>
			<td colspan="3" width="600"><textarea id="s_mess" name="s_mess" rows="15" cols="74" style="border: solid 1px #D5E5F7"><bs:formelement name="s_mess" ></bs:formelement></textarea></td>
		</tr>	
		
		
		
	</table>

</td>
</tr>
<tr>
	<td style="border-top-style: solid;border-top-width: 1px ; border-top-color:#D5E5F7; height: 30px" valign="bottom">
						<table cellpadding="0" cellspacing="0" width="100%">
							<tr>
								<td width="100%" align="center">
								<td width="1%" ><script>ObjectDraw("page1","button",0,"<bs:message code="sendMail_action1" defaultValue="Send"/>","goAction('sendMail-send')","page_section","","images/menu/","",true,24);</script></td>
							</tr>
						</table>

	
	</td>

</tr>			

</table>

<script>
function runActionOnEnter(){
	
}
</script>
	
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

