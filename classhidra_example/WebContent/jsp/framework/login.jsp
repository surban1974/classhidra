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

<div id="page" style=" width: 900; height: 530; background-color: white; ">
<%@ include file="../included/content_page_header.jsp" %>

<center>




	<bs:form method="post" onhistory="">

<!-- <bs:formelement name="user"/> - <bs:formelement name="password"/> - <bs:formelement name="group"/> - <bs:formelement name="groups.size"/> -->
		<input type="hidden" name="code">
		<bs:input type="hidden" name="group"/>
		<bs:input type="hidden" name="target"/>
		<bs:input type="hidden" name="lang"/>
	
	
<table border="0" width="100%" height="450" >
<tr>
<td align="center" style="border-bottom-style: solid 1px;" valign="middle">
	

	
	 
	
	
<jsp:include page="/jsp/included/panel_top.jsp"/>	
			
			<table  >
	
				<tr>
					<td 
						valign="middle" 
						align="center" 
						width="150"
						
						style="border-right-style: solid;border-right-width: 1px ; border-right-color: #D5E5F7; width: 150px; height: 150px;">
						<img src="images/decor/128_logo.gif" border="0" >
						
					</td>
					<td align="center" valign="top" > 
	
						<table width="100%" >
						<tr>
							<td><img src="images/space.gif"/></td>
						</tr>	
				<bs:equal name="groups.size" value="0">							
						<tr>
							<td  width="150" class="signOnLabel"><nobr><bs:message code="login_1" defaultValue="Utente"/>&nbsp;&nbsp;&nbsp;</nobr></td>
						</tr>
						<tr>
							<td width="300" ><bs:input name="user" size="30" toUpperCase="no"  onfocus="try{document.getElementById('password').value='';}catch(e){}" styleClass="signOn"/></td>
						</tr>
								
						<tr>
							<td  width="150" class="signOnLabel"><nobr><bs:message code="login_2" defaultValue="Parole d'ordine"/>&nbsp;&nbsp;&nbsp;</nobr></td>
						</tr>
						<tr>
							<td width="300" ><bs:input name="password" type="password" size="30" toUpperCase="no"   styleClass="signOn"/></td>
						</tr>
				</bs:equal>						
				<bs:more name="groups.size" value="0">						
						<tr>
							<td  width="150" class="signOnLabel"><nobr><bs:message code="login_1" defaultValue="Utente"/>&nbsp;&nbsp;&nbsp;</nobr></td>
						</tr>
						<tr>
							<td class="signOn" width="300" style="	color: #666667;font-style: normal;border: solid 1px silver;"><bs:formelement bean="$authentication" name="_userDesc"/><bs:input type="hidden" name="user" /></td>
						</tr>
						<tr>
							<td width="150" class="signOnLabel"><bs:message code="login_3" defaultValue="Ruoli"/>&nbsp;&nbsp;&nbsp;</td>
						</tr>								
						<tr>
							<td width="300" style="border-left: solid 1px silver;">
					
										<bs:list
											addHiddenInput="false" 
											name="group" 
											list="groups"
											key_values="code"
											propertys="code;" 
											tr_onclick="runSubmit('content')"
											tr_styleClass="colTab1"	
											styleClass="divLists0"	
											tb_width="100%"
											width="300"
											
										/>	
									
								</td>
							</tr>
							
				</bs:more>		
						</table>
						
					</td>
	
				</tr>
			</table>		
<%request.setAttribute("mess-description","Version "+new version_init("application.version").get_version());%>
			
<jsp:include page="/jsp/included/panel_bottom.jsp"/>			
<br></br>	
<br></br>
<br></br>
<br></br>
</td>

</tr>
<tr>
	<td style="border-top-style: solid;border-top-width: 1px ; border-top-color:#D5E5F7; height: 10px" valign="bottom">
	</td>
</tr>
<tr>
	<td style=" height: 30px" valign="bottom">

						<table cellpadding="0" cellspacing="0" width="100%">
							<tr>
								<td width="5%">&nbsp;</td>
								<td width="1%"><script>ObjectDraw("page1","button",1,"","changeLang('IT')","page_section","IT.gif","images/menu/","",true,24,"35");</script></td>
								<td width="1%">&nbsp;</td>
								<td width="1%"><script>ObjectDraw("page1","button",2,"","changeLang('EN')","page_section","EN.gif","images/menu/","",true,24,"35");</script></td>
								<td width="1%">&nbsp;</td>
								<td width="1%" ><script>ObjectDraw("page1","button",16,"","showAsIFrame('jsp/help/Manuale.pdf',900,550)","page_section_active","help.gif","images/menu/","",true,24,"35");</script></td>
								<td width="1%">&nbsp;</td>
								<td  width="1%" align="right"><script>ObjectDraw("page1","button",15,"&nbsp;New from version 1.4.3&nbsp;&nbsp;","showAsIFrame('jsp/help/news.pdf',900,550)","page_section_active","","images/menu/","",true,24);</script></td>

								<td width="100%" align="center">&nbsp;</td>
							
	<bs:equal name="groups.size" value="0">	
								<td align="center" width="20px">&nbsp;</td>
								<td width="1%" ><script>ObjectDraw("page1","button",10,"<bs:message code="login_6" defaultValue="Avanti"/>","enter()","page_section","","images/menu/","",true,24);</script></td>
								<td align="center" width="20px">&nbsp;</td>
								<td width="1%" ><script>ObjectDraw("page1","button",11,"<bs:message code="login_61" defaultValue="Avanti come Guest"/>","enterGuest()","page_section","","images/menu/","",true,24);</script></td>
								<td align="center" width="20px">&nbsp;</td>
								<td width="1%" ><script>ObjectDraw("page1","button",12,"<bs:message code="login_62" defaultValue="Avanti come User"/>","enterUser()","page_section","","images/menu/","",true,24);</script></td>

	<td align="center" width="20px">&nbsp;</td>
	</bs:equal>
	<bs:more name="groups.size" value="0">	
								<td align="center" width="20px">&nbsp;</td>
								<td  width="1%" align="right"><script>ObjectDraw("page1","button",5,"<bs:message code="login_7" defaultValue="Pulisci"/>","clear_l()","page_section","","images/menu/","",true,24);</script></td>
								<td align="center" width="20px">&nbsp;</td>
	</bs:more>					
								<td width="5%">&nbsp;</td>							
							</tr>
						</table>

	
	</td>

</tr>	
	

</table>	

	

	
	</bs:form>
	
<script>
try{
	document.getElementById("user").focus();
}catch(e){	
}
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

