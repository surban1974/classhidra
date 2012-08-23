<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<%@ include file="../framework/header.jsp" %> 

<script language="javascript" src="javascript2012/application/login.js"></script>

<body >

<div id="content_Panel_canvas" 
style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; overflow:hidden;"
>
<%@ include file="../included/content_page_header.jsp" %>
<table border="1" width="100%" height="100%" cellpadding="0" cellspacing="0">
<tr>
<td align="center" bgcolor="">



<div id="page0" style=" width: 450; height: 220;  position: absolute; top: 100; left: 200; filter: alpha(opacity=20);opacity: 0.2; ">
<jsp:include page="/jsp/included/panel_top.jsp" >
	<jsp:param name="panel-id" value="panel_Login" />	
	<jsp:param name="panel-onclose" value="window.close();" />

</jsp:include>	
<center>
<img src="images/canvas.gif" style="width: 450; height: 220;"></img>

</center>
<jsp:include  page="/jsp/included/panel_bottom.jsp" />
</div>

<div id="page1" style=" width: 900; height: 550;  white; position: relative;">
<jsp:include page="/jsp/included/panel_top.jsp" >
	<jsp:param name="panel-id" value="panel_Login" />	
	<jsp:param name="panel-onclose" value="window.close();" />

</jsp:include>	
<center>
<img src="images/canvas.gif" style="width: 900; height: 550"></img>

</center>
<jsp:include  page="/jsp/included/panel_bottom.jsp" />
</div>




</td>
</tr>
</table>
</div>
</body>
<%@ include file="../framework/footer.jsp" %>

