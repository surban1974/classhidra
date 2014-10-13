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

<div id="page" style=" width: 1000; height: 530; background-color: white">
<%@ include file="../included/content_page_header.jsp" %>

<center>
	<bs:form method="post" onhistory="">

<table border="0" width="100%" height="450" >
<tr>
<td align="center" style="border-bottom-style: solid 1px;" valign="middle">	

<jsp:include page="/jsp/included/panel_top.jsp"/>
	<table>
		<tr>
			<td rowspan="6"
						valign="middle" 
						align="center" 
						width="150"
						
						style="border-right-style: solid;border-right-width: 1px ; border-right-color: #D5E5F7"
			><img src="images/logo.gif" border="0" ></td>
			<td align="right" width="" class="labelLeft"><nobr><b><bs:message code="about_1" defaultValue="Application"/>:</b></nobr></td>
			<td class="labelContent"> ClassHidra Framework</td>
		</tr>	
		<tr>
			<td align="right" width="" class="labelLeft"><nobr><b><bs:message code="about_2" defaultValue="Version"/>:</b></nobr></td>
			<td class="labelContent"> 1.4.10.ape</td>
		</tr>	
		<tr>
			<td align="right" width="" class="labelLeft"><nobr><b><bs:message code="about_3" defaultValue="Author"/>:</b></nobr></td>
			<td class="labelContent"> Svyatoslav Urbanovych </td>
		</tr>	
		<tr>
			<td align="right" width="" class="labelLeft"><nobr><b><bs:message code="about_4" defaultValue="EMail"/>:</b></nobr></td>
			<td class="labelContent"> Svyatoslav.Urbanovych@gmail.com </td>
		</tr>
		<tr>
			<td align="right" width="" class="labelLeft"><nobr><b><bs:message code="about_6" defaultValue="Storage"/>:</b></nobr></td>
			<td class="labelContent"> http://sourceforge.net/projects/classhidra/ </td>
		</tr>	
			
		<tr>
			<td align="right" width="" class="labelLeft"><nobr><b><bs:message code="about_5" defaultValue="Description"/>:</b></nobr></td>
			<td class="labelContent"><nobr> ClassHidra, Java open-source ModelViewController implementation.<br> Servlets and JSP technology. Web Application Development.<br> Integrated with Spring from v.1.2.0(classhigra2spring plugin)</nobr> </td>
		</tr>	
		
		
	</table>
<jsp:include page="/jsp/included/panel_bottom.jsp"/>
</td>
</tr>
<tr>
	<td style="border-top-style: solid;border-top-width: 1px ; border-top-color:#D5E5F7; height: 30px" valign="bottom">
						<table cellpadding="0" cellspacing="0" width="100%">
							<tr>
								<td width="100%" align="center">
								<td width="1%" ><script>ObjectDraw("page1","button",0,"<bs:message code="action_action1" defaultValue="Download From SourceForge"/>","location.replace('http://sourceforge.net/projects/classhidra/')","page_section","","images/menu/","",true,24);</script></td>
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

