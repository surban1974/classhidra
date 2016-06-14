<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %>
<%@ page import="java.util.*" %> 
<%@ page import="it.classhidra.core.init.*" %>
<%@ page import="it.classhidra.core.controller.*" %>
<%@ page import="it.classhidra.core.tool.exception.*" %>
<!--  <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"> -->
<!-- view: <%=this.getClass().getName()%> -->
<!-- build <%=bsController.getInfoVersion("it.classhidra.version") %> -->
<%  
	
	i_action 		formAction 			= (request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTION)==null)?new action():(i_action)request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTION);
	info_action		formInfoAction 		= formAction.get_infoaction();
	i_bean 			formBean 			= formAction.get_bean();
	redirects 		formRedirect 		= formAction.getCurrent_redirect();
	info_navigation	formInfoNavigation	= bsController.getFromInfoNavigation(null, request);
		if(formInfoNavigation==null)
			formInfoNavigation = new info_navigation();
	formInfoNavigation.decodeMessage(request);
//	request.getSession().setAttribute(bsController.CONST_BEAN_$NAVIGATION, formInfoNavigation);
	auth_init 		userInfo			= (auth_init)request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION);
	int shw=0;
	if(formInfoAction!=null) shw++;
	if(formBean!=null) shw++;
	if(formRedirect!=null) shw++;
	if(formInfoNavigation!=null) shw++;
	if(userInfo!=null) shw++;
	
	new Integer(shw).toString();

	
%>
<!-- action info context <%=((formAction==null)?" null ":formAction.getInfo_context()) %> -->
<%if(formAction!=null && formBean!=null && !formAction.equals(formBean)){%>
<!-- bean info context <%=((formBean==null)?" null ":formBean.getInfo_context()) %> -->
<%} %>

<head>    
    <title><bs:message code="title_application" defaultValue="Application "/></title>
    <link rel="SHORTCUT ICON" href="images/application.ico">
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="classHidra">
	<meta http-equiv="keywords" content="Google">
	<meta http-equiv="keywords" content="AppEngine">
	<meta http-equiv="description" content="ClassHidra For Google AppEngine">
	
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-76735432-1', 'auto');
  ga('send', 'pageview');

</script>
	
	
</head>


<!--
__deprecated 
<script language="javascript" src="javascript2012/tool.js"></script>
<script language="javascript" src="javascript2012/light_menu.js"></script>
<script language="javascript" src="javascript2012/js4ajax.js"></script>
<script language="javascript" src="javascript2012/List.js"></script>
 -->
 <script type="text/javascript" src="Controller?$action=bsLoadFromFramework&src=javascript/&type=text/javascript&cache=10000"></script>
<link  href="css/framework.css" rel="stylesheet" type="text/css"/>

<style type="text/css">
body
  {
  margin: 0;
 }
div#top_menu{
  overflow: auto;
  width: 100%;
  position: absolute;
  top: 0;
  left: 0;
  }

@media screen{
  body>div#top_menu{
    position: fixed;
    }
}

td.asbts{
	color: rgb(119, 119, 119);
	font-size: 18px;
	line-height: 20px;
	padding: 12px;
	font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
	cursor: pointer;
	text-align: center;
}

td.asbts_sel{
	color: rgb(85, 85, 85);
	background-color: #e7e7e7;
	font-size: 14px;
	line-height: 20px;
	padding: 12px;
	font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
	cursor: pointer;
	text-align: center;
}
div.asbts{
	height: 44px;
	width: 328px;
	background-color: white;
	border : 1px solid #ccc;
	border-radius: 6px;
	text-align: center;
	vertical-align: middle;
	cursor: pointer;
	font-size: 18px;
	color: rgb(51, 51, 51);
    line-height: 1.33333; 
}
div.asbts:hover{
	background-color: #e7e7e7;
}
div.asbts td{
	cursor: pointer;
	font-size: 18px;
	color: rgb(51, 51, 51);
    line-height: 1.33333; 

}
</style>

<div id="top_menu" style="position:absolute; top:0; left:0; z-index: 1;">
<table style=" height: 49px; background-color: #F8F8F8; width: 100%; border-bottom: 1px solid #CCCCCC;" cellpadding="0" cellspacing="0">
	<tr>
		<td class="asbts" 
			style="width: 400px; padding-left: 14px"
			onclick="location.replace('login')" 
			valign="middle"
			>
			<b>CLASSHIDRA</b> Simple J2EE MVC Implementation
		</td>
		<td class="asbts_sel" 
			style="width: 120px; "
			onclick="location.replace('login')" 
			valign="middle"> 
			Demo ClassHidra
		</td>
		<td class="asbts" 
			style="width: 150px; font-size: 14px;"
			onclick="location.replace('hello')" 
			valign="middle"> 
			Starter [Hello World]
		</td>
		<td align="right" valign="middle" style="padding:2px; ">
			<div class="asbts"
			onclick="location.replace('https://sourceforge.net/projects/classhidra/files/classhidra.1/classhidra.1.5.4/')">
			<table cellpadding="0" cellspacing="0" style="width: 100%; height: 100%;" >
				<tr>
					<td align="right" valign="middle" style="padding-top: 2px"><img src="starter/images/classhidra_small.gif" border="0"/></td>
					<td align="left" valign="middle" style="width: 230px;padding-top: 2px">&nbsp;Source Forge Download &raquo;</td>
				</tr>
			</table>
			
			</div>
			
		</td>
		<td>&nbsp;&nbsp;</td>
	</tr>
</table> 

</div>


