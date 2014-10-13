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

<div id="page" style=" width: 1000; height: 530; background-color: white">
<%@ include file="../included/content_page_header.jsp" %>
&nbsp;
<div id="canvas_report" style=" width:900px;height:480px; 
	overflow: scroll; 
"
   	
>
<table cellpadding="0" cellspacing="0">
	<bs:sequence bean="navigationAll">
		<bs:bean name="navigationEntity" source="navigationAll" index="SEQUENCE"/>
<tr>
	<td class="page_section" background="images/corners/panel_t.gif" ><bs:formelement bean="navigationEntity" name="id"/></td>
	<td class="page_section" background="images/corners/panel_t.gif" ><bs:formelement bean="navigationEntity" name="desc_second"/></td>
	<td class="page_section" background="images/corners/panel_t.gif" ><bs:formelement bean="navigationEntity" name="iAction.path"/></td>
	<td class="page_section" background="images/corners/panel_t.gif" ><bs:formelement bean="navigationEntity" name="iRedirect.path"/></td>
</tr>
<tr>		
	<td colspan="4"> 
		<textarea rows="27" cols="100"><bs:formelement bean="navigationEntity" name="_content_xml"/></textarea>
	</td>						
</tr>
	</bs:sequence>
</table>
</div>

</div>
<jsp:include  page="/jsp/included/panel_bottom.jsp" />


</td>
</tr>
</table>
</div>
</body>
<%@ include file="../framework/footer.jsp" %>

