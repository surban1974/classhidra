<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<style type="text/css">

div#footer_fixedbox{
  overflow: auto;
  width: 100%;
  position: absolute;
  bottom:0;
  left: 0;
  
  }
  

  


</style>


<bs:bean name="actions_forbidden" source="$authentication" property="_actions_forbidden"/>
<bs:bean name="navigationPrev" source="$navigation" property="prevIRedirect"/>
<bs:bean name="navigationLast" source="$navigation" property="lastIRedirect"/>
<bs:bean name="navigationAll" source="navigationLast" property="allChildIRedirect"/>

<script type="text/javascript" src="./javascript2012/content_page_header.js"></script>
<div id="fixedbox" style="width: 100%;z-index: 1000;overflow: hidden" >

	<table cellpadding="0" cellspacing="0" width="100%" bgcolor="white">
		<tr>
		
<bs:equal bean="$authentication" method_prefix="" name="is_logged" value="true">
			<td width="0%" background="images/corners/panel_t.gif" onclick="menu()" valign="middle" style="cursor: pointer">
				<img id="menu_img" src="images/menu/16_close.gif" border="0" height="13"></img>&nbsp;
			</td>
</bs:equal>
	
			<td  width="100%" background="images/corners/panel_t.gif" valign="middle">
				<div style="100%">
					<div id="fixedbox_show_navigate_story" style=" overflow: hidden;">
					<table cellspacing="0" cellpadding="0" border="0">
					<tr>
	<bs:sequence bean="navigationAll">
		<bs:bean name="navigationEntity" source="navigationAll" index="SEQUENCE"/>
		<bs:notIsNull bean="navigationEntity" name="iRedirect">
			<bs:notEqual bean="navigationEntity" name="desc_second" value="::::">
				<td><bs:more bean="bs:sequence" name="index" value="0">&#8594;</bs:more></td>
				<td >
					<script>ObjectDraw(
								"page4",
								"button",
								"5<bs:formelement bean="bs:sequence" name="index"/>" ,
								"<bs:formelement bean="navigationEntity" name="desc_second"/>",
								"goAction('<bs:formelement bean="navigationEntity" name="iAction.path"/>?middleAction=reload')",
								"page_section",
								"",
								"images/menu/",
								"",
								"false",
								24
							);
					</script>
					
				</td>
			</bs:notEqual>	
		</bs:notIsNull>	
	</bs:sequence>

					
					
					</tr>
					</table>
					</div>
				</div>
				
			</td>
		
			<td background="images/corners/panel_t.gif" align="right" valign="top" width="1%" style="cursor: pointer;">
				<table cellspacing="0" border="0" cellpadding="0">
					<tr>
						<bs:notEqual name="_infoaction.path" value="">
							<td background="images/menu/page_center_n.gif"><script>ObjectDraw("page4","button",400,"","goAction('<bs:formelement name="_infoaction.path"/>?middleAction=reload')","page_section","reload.gif","images/menu/","","false", 24);</script></td>
						</bs:notEqual>
						<bs:equal name="_infoaction.path" value="">
							<td background="images/menu/page_center_n.gif"><script>ObjectDraw("page4","button",400,"","goAction('<bs:formelement bean="navigationLast" name="IAction.path"/>.bs?middleAction=reload')","page_section","reload.gif","images/menu/","","false", 24);</script></td>
						</bs:equal>
<bs:equal bean="$authentication" method_prefix="" name="is_logged" value="true">						
						<td background="images/menu/page_center_n.gif"><script>ObjectDraw("page4","button",401,"","if(document.getElementById('$action')){goAction('minimizer?middleAction=close&source='+document.getElementById('$action').value);}else{goAction('minimizer?middleAction=close&source=');}","page_section","save.gif","images/menu/","","false", 24);</script></td>
</bs:equal>
			<td id="page_img_minimize" align="right" valign="middle" width="1%" style="cursor: pointer; background-image:url('images/corners/panel_t.gif');display:none;">
				<img src="images/corners/minimize.gif" border="0" 
				onclick="showAsPanelNormal('page');try{afterShowAsPanelNormal('page');}catch(e){};try{document.getElementById('top_menu').style.display='block';}catch(e){}">
			</td>
			<td id="page_img_maximize" align="right" valign="middle" width="1%" style="cursor: pointer; background-image:url('images/corners/panel_t.gif');">
				<img src="images/corners/maximize.gif" border="0" 
				onclick="showAsPanelMax('page');try{afterShowAsPanelMax('page');}catch(e){};try{document.getElementById('top_menu').style.display='none';}catch(e){}">
			</td>

						<td background="images/menu/page_center_n.gif"><script>ObjectDraw("page4","button",45,"","goAction('login?middleAction=reload')","page_section","close.gif","images/menu/","","false", 24);</script></td>
				    </tr>
				</table>
			</td>
		</tr>

	</table>

</div>




<div id="menu_operation" style="position:absolute; background-color:white; display: none;z-index: 1001; ">


<jsp:include page="/jsp/included/panel_top.jsp">
	<jsp:param name="panel-show-header" value="false" />
	<jsp:param name="panel-show-close-button" value="false" /> 
	<jsp:param name="panel-description" value="Menu" />	
	<jsp:param name="panel-id" value="panel_Menu" />
	<jsp:param name="panel-onclose" value="menu()" />	

</jsp:include>	
<div id="menu_operation_0" style=" padding: 5px;"></div>	
<jsp:include page="/jsp/included/panel_bottom.jsp"/>	
</div>	



<script>
ajax_makeRequest("menuCreator?menu_id=","menu_operation_0","JSAfter_showAsPopup","",false);
//window.setTimeout("ajustPage1()", 500);
</script>

<bs:equal bean="REQUEST.PARAMETER" name="refreshMenu" value="true">
<script>
menu();
</script>
</bs:equal>

