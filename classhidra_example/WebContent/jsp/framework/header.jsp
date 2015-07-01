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
	auth_init 		userInfo			= (auth_init)request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION);
	int shw=0;
	if(formInfoAction!=null) shw++;
	if(formBean!=null) shw++;
	if(formRedirect!=null) shw++;
	if(formInfoNavigation!=null) shw++;
	if(userInfo!=null) shw++;
	
	new Integer(shw).toString();

	
%>


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
	

	
	
</head>


<!--
__deprecated 
<script language="javascript" src="javascript2012/tool.js"></script>
<script language="javascript" src="javascript2012/light_menu.js"></script>
<script language="javascript" src="javascript2012/js4ajax.js"></script>
<script language="javascript" src="javascript2012/List.js"></script>
 -->
 <script type="text/javascript" src="Controller?$action=bsLoadFromFramework&src=javascript/&type=text/javascript&cache=100000"></script>
<link  href="css/framework.css" rel="stylesheet" type="text/css"/>




