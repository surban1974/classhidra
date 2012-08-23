<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<bs:equal bean="type" value="open"><%response.setHeader("Content-Disposition","attachment; filename=log.txt");response.setHeader("Content-Transfer-Encoding","base64");response.setContentType("Application/txt");%>
<bs:formelement bean="file"/>
</bs:equal>
<bs:notEqual bean="type" value="open">
<%@ include file="../framework/header.jsp" %> 

<script language="javascript" src="javascript2012/application/login.js"></script>

<body >

<div id="content_Panel_canvas" 
style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; overflow:hidden;"
>
<table border="0" width="100%" height="100%" cellpadding="0" cellspacing="0">
<tr>
<td align="center" bgcolor="#D5E5F7">

<jsp:include page="/jsp/included/panel_top.jsp" >
	<jsp:param name="panel-id" value="panel_Login" />	
	<jsp:param name="panel-onclose" value="window.close();" />

</jsp:include>	

<div id="page" style=" width: 900; height: 530; background-color: white">
<%@ include file="../included/content_page_header.jsp" %>

<center>
<bs:form method="post">
<bs:input type="hidden" name="path"/>

<div align="center">
<br>
	<table width="750px" cellpadding="0" cellspacing="4">
	<tr>
		<td align="left">
			<table width="750px" cellpadding="2" cellspacing="2" >
				<tr>
	      			<td width="750" class="labelContent" align="right" style="border: none;">
	       				<b><bs:message code="logmanagement_2" defaultValue="Componenti"/></b>
	        		</td>
				</tr>
			</table>
	
			<div class="divComponenti">
				<table width="750px" cellpadding="2" cellspacing="2" >
<bs:sequence bean="ListLog">				
	<bs:bean name="current" source="ListLog" index="sequence"/>
					<tr style="cursor:pointer" class="labelContent" onmouseover="this.className='rowTabOver'" onmouseout="this.className='labelContent'"
						onclick="document.forms[0].path.value='<bs:formelement bean="current" name="Path"/>';openLog();">
						<td width="750" class="labelContent"><nobr>
						<img src="images/menu/special/doc.gif" border="0">
						<bs:formelement bean="current" name="Path"/>
						</nobr>
						</td>
					</tr>
</bs:sequence>					
				</table>
			</div>
	
			
		</td>
	</tr>
</table>	
</div>

<script>
function openLog(){
	var URL = "log?path="+escape(document.forms[0].path.value);
	startURL(URL,"Log");
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

  
</bs:notEqual>