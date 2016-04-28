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




