<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<%@ include file="../framework/header.jsp" %> 

	<link href="css/default.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="javascript2012/jquery.min.js"></script>
	<script src="javascript2012/mobilyblocks.js" type="text/javascript"></script>

	<style type="text/css">

		div.comp {
			background:url(images/menu/circle/96.gif) no-repeat center center;
			width:128px;
			height:128px;
			display:block;
			cursor:pointer;
			position:relative;
			margin:0 auto;
		}

	</style>

	<script type="text/javascript">

		$(function(){
			$('.comp').mobilyblocks({
				trigger: 'click',
				direction: 'counter',
				duration:500,
				zIndex:50,
				widthMultiplier:1.40
			});
		});

	</script>
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
<table border="0" width="100%" height="100%" style="width: 100%; height: 100%;">
<tr>

<td align="center">
	<div class="wrapper">
		<div class="inner">
			<div id="4click" class="comp"> 

				<ul class="reset">
<bs:notEqual bean="actions_forbidden" name="about">	
						<li><a href="javascript:void(goAction('about'))"><img src="images/decor/96_about.gif"  /></a><br>
						<nobr>
						<table  cellspacing="0" cellpadding="0" border="0" onclick="goAction('about')">
							<tr>							
								<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
								<td style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section"><b>About</b></span></nobr></td>
								<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
								
							</tr>
						</table>				
						</nobr>
						</li>
</bs:notEqual>			

<bs:equal bean="actions_forbidden" name="about">	
						<li><img src="images/decor/96_about.gif" style="filter: alpha(opacity=30);opacity: 0.3; cursor: auto" />
						</li>
</bs:equal>	

<bs:notEqual bean="actions_forbidden" name="sendMail">	
						<li><a href="javascript:void(goAction('sendMail'))"><img src="images/decor/96_mail_sender.gif"  /></a><br>
						<nobr>
						<table  cellspacing="0" cellpadding="0" border="0" onclick="goAction('sendMail')">
							<tr>							
								<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
								<td style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section"><b>Send Mail</b></span></nobr></td>
								<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
								
							</tr>
						</table>				
						</nobr>
						</li>
</bs:notEqual>			

<bs:equal bean="actions_forbidden" name="sendMail">	
						<li><img src="images/decor/96_mail_sender.gif" style="filter: alpha(opacity=30);opacity: 0.3; cursor: auto" />
						</li>
</bs:equal>	

<bs:notEqual bean="actions_forbidden" name="builder">	
						<li><a href="javascript:void(goAction('builder?middleAction=reload'))"><img src="images/decor/96_builder.gif"  /></a><br>
						<nobr>
						<table  cellspacing="0" cellpadding="0" border="0" onclick="goAction('builder?middleAction=reload')">
							<tr>							
								<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
								<td style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section"><b>Builder</b></span></nobr></td>
								<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
								
							</tr>
						</table>				
						</nobr>
						</li>
</bs:notEqual>			

<bs:equal bean="actions_forbidden" name="builder">	
						<li><img src="images/decor/96_builder.gif" style="filter: alpha(opacity=30);opacity: 0.3; cursor: auto" />
						</li>
</bs:equal>

<bs:notEqual bean="actions_forbidden" name="amanager">	
						<li><a href="javascript:void(goAction('amanager?middleAction=reload'))"><img src="images/decor/96_users.gif"  /></a><br>
						<nobr>
						<table  cellspacing="0" cellpadding="0" border="0" onclick="goAction('amanager?middleAction=reload')">
							<tr>							
								<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
								<td style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section"><b>Authentication</b></span></nobr></td>
								<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
								
							</tr>
						</table>				
						</nobr>
						</li>
</bs:notEqual>			

<bs:equal bean="actions_forbidden" name="amanager">	
						<li><img src="images/decor/96_users.gif" style="filter: alpha(opacity=30);opacity: 0.3; cursor: auto" />
						</li>
</bs:equal>

<bs:notEqual bean="actions_forbidden" name="log_users">	
						<li><a href="javascript:void(goAction('log_users'))"><img src="images/decor/96_sessions.gif"  /></a><br>
						<nobr>
						<table  cellspacing="0" cellpadding="0" border="0" onclick="goAction('log_users')">
							<tr>							
								<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
								<td style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section"><b>Users In Session</b></span></nobr></td>
								<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
								
							</tr>
						</table>				
						</nobr>
						</li>
</bs:notEqual>			

<bs:equal bean="actions_forbidden" name="log_users">	
						<li><img src="images/decor/96_sessions.gif" style="filter: alpha(opacity=30);opacity: 0.3; cursor: auto" />
						</li>
</bs:equal>

<bs:notEqual bean="actions_forbidden" name="resources">	
						<li><a href="javascript:void(goAction('resources'))"><img src="images/decor/96_resources.gif"  /></a><br>
						<nobr>
						<table  cellspacing="0" cellpadding="0" border="0" onclick="goAction('resources')">
							<tr>							
								<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
								<td style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section"><b>Resources</b></span></nobr></td>
								<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
								
							</tr>
						</table>				
						</nobr>
						</li>
</bs:notEqual>			

<bs:equal bean="actions_forbidden" name="resources">	
						<li><img src="images/decor/96_resources.gif" style="filter: alpha(opacity=30);opacity: 0.3; cursor: auto" />
						</li>
</bs:equal>
				</ul>			

			</div>	
		</div>	
		</div>	

</td>
</tr>
</table>
	</bs:form>
<script>
$(document).ready(function(){$(".trigger").click();})
</script>
</center>



</div>
<jsp:include  page="/jsp/included/panel_bottom.jsp" />


</td>
</tr>
</table>
</div>
</body>
<%@ include file="../framework/footer.jsp" %>

