<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<%@ include file="../framework/header.jsp" %>  
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

<div id="page" style=" width: 900; height: 530; background-color: white; overflow: auto;">



<%@ include file="../included/content_page_header.jsp" %>
<script language="javascript" src="javascript2012/amanager.js"></script>
<script>
	var cr_x=0;
	var cr_y=0;
	var cr_s=0;
	var posTop=0;
	var posLeft=0;
</script>	

<table>
<tr>
	<td>
		<table cellspacing="0" cellpadding="0">
			<tr>
				<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
				<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section"><bs:formelement name="current_redirect.description"/></span></nobr></td>
				<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
			</tr>
		</table>
	</td>	
<bs:notEqual name="mode" value="relation">
	<td align="center"><script>ObjectDraw("page1","button",1,"syncro","runSubmit('syncro')","page_section","","images/menu/","",true,24,"35");</script></td>
	<td align="center"><script>ObjectDraw("page1","button",3,"peview/load new","showAsPopup('amanager?middleAction=preview',900,530)","page_section","","images/menu/","",true,24,"35");</script></td>
	<td id="general">
	<table  cellspacing="0">
		<tr>
			<td width="0%" class="page_section">encoding:</td>
			<td width="150" id="general||||"><bs:input name="l_users.xmlEncoding" style="border: solid 1px silver; width: 100%" toUpperCase="no" onblur="updateValue(this,'xmlEncoding')" onfocus="makeFocus(this)"/></td>
			<td width="0%" class="page_section">externalloader:</td>
			<td width="150" id="general||||"><bs:input name="l_users.externalloader" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'externalloader')" onfocus="makeFocus(this)"/></td>

		</tr>
	</table>						
	</td>

</bs:notEqual>
<bs:equal name="mode" value="relation">
	
	<td align="center"><script>ObjectDraw("page1","button",1,"syncro","runSubmit('syncro_auth')","page_section","","images/menu/","",true,24,"35");</script></td>
	<td align="center"><script>ObjectDraw("page1","button",3,"peview/load new","showAsPopup('amanager?middleAction=preview_auth',950,480)","page_section","","images/menu/","",true,24,"35");</script></td>
	<td>
	<table  cellspacing="0">
		<tr>
			<td width="0%" class="page_section">encoding:</td>
			<td width="150" id="general||||"><bs:input name="l_authentication.xmlEncoding" style="border: solid 1px silver; width: 100%" toUpperCase="no" onblur="updateValue(this,'xmlEncoding')"/></td>
			<td width="0%" class="page_section">externalloader:</td>
			<td width="150" id="general||||"><bs:input name="l_authentication.externalloader" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'externalloader')"/></td>

		</tr>
	</table>						
	</td>
</bs:equal>
</tr>
</table>

<center>
<bs:form onhistory="" method="post">

<bs:input type="hidden" name="scrollLeft"/>
<bs:input type="hidden" name="scrollTop"/>

<bs:input type="hidden" name="id_selected_user"/>
<bs:input type="hidden" name="id_selected_group"/>
<bs:input type="hidden" name="id_selected_target"/>

<bs:input type="hidden" name="id_selected_action"/>
<bs:input type="hidden" name="id_selected_redirect"/>


<bs:input type="hidden" name="display_users"/>
<bs:input type="hidden" name="display_targets"/>
<bs:input type="hidden" name="display_groups"/>
<bs:input type="hidden" name="display_relations"/>
<bs:input type="hidden" name="display_actions"/>

<bs:input type="hidden" name="type_selected"/>
<bs:input type="hidden" name="mode"/>



<table class="tableBodyTabb" border="0" id="canvas_table" >
<tr>
	<td style="border: dashed 1px silver">
		<table width="100%" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td  height="20" width="50%" valign="middle"><img id="users_img" src="images/menu/16_<bs:equal name="display_users" value="false">close</bs:equal><bs:equal name="display_users" value="true">open</bs:equal>.gif" border="0" style="cursor:pointer" 
				onclick="
					document.getElementById('mode').value='user';
					document.getElementById('display_users').value='true';
					document.getElementById('display_targets').value='false';
					document.getElementById('display_groups').value='false';
					document.getElementById('display_relations').value='false';
					runSubmit('blank');
				"
				></td>
				<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
				<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section">users</span></nobr></td>
				<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
				<td  width="50%"><bs:equal name="display_users" value="true"><img src="images/decor/20_add.gif" border="0" style="cursor:pointer" onclick="addEntity('user','','')"></bs:equal></td>
			</tr>
		</table>
	</td>
	<td style="border: dashed 1px silver">
		<table width="100%" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td  height="20" width="50%" valign="middle"><img id="targets_img" src="images/menu/16_<bs:equal name="display_targets" value="false">close</bs:equal><bs:equal name="display_targets" value="true">open</bs:equal>.gif" border="0" style="cursor:pointer" 
				onclick="
					document.getElementById('mode').value='target';
					document.getElementById('display_users').value='false';
					document.getElementById('display_targets').value='true';
					document.getElementById('display_groups').value='false';
					document.getElementById('display_relations').value='false';
					runSubmit('blank');
				"
				></td>
				<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
				<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section">targets</span></nobr></td>
				<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
				<td  width="50%"><bs:equal name="display_targets" value="true"><img src="images/decor/20_add.gif" border="0" style="cursor:pointer" onclick="addEntity('target','','')"></bs:equal></td>
			</tr>
		</table>
	</td>
	
	<td style="border: dashed 1px silver">
		<table width="100%" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td  height="20" width="50%" valign="middle"><img id="groups_img" src="images/menu/16_<bs:equal name="display_groups" value="false">close</bs:equal><bs:equal name="display_groups" value="true">open</bs:equal>.gif" border="0" style="cursor:pointer" 
				onclick="
					document.getElementById('mode').value='group';
					document.getElementById('display_users').value='false';
					document.getElementById('display_targets').value='false';
					document.getElementById('display_groups').value='true';
					document.getElementById('display_relations').value='false';
					runSubmit('blank');
				"
				></td>
				<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
				<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section">groups</span></nobr></td>
				<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
				<td  width="50%"><bs:equal name="display_groups" value="true"><img src="images/decor/20_add.gif" border="0" style="cursor:pointer" onclick="addEntity('group','','')"></bs:equal></td>
			</tr>
		</table>
	</td>

	
	<td style="border: dashed 1px silver">
		<table width="100%" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td  height="20" width="50%" valign="middle"><img id="relations_img" src="images/menu/16_<bs:equal name="display_relations" value="false">close</bs:equal><bs:equal name="display_relations" value="true">open</bs:equal>.gif" border="0" style="cursor:pointer" 
				onclick="
					document.getElementById('mode').value='relation';
					document.getElementById('display_users').value='false';
					document.getElementById('display_targets').value='false';
					document.getElementById('display_groups').value='false';
					document.getElementById('display_relations').value='true';
					runSubmit('blank');
				"
				></td>
				<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
				<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section">authentication-relations</span></nobr></td>
				<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
				<td  width="50%"><bs:equal name="display_relations" value="true"><img src="images/decor/20_add.gif" border="0" style="cursor:pointer" onclick="addEntity('relation','','')"></bs:equal></td>
			</tr>
		</table>
	</td>	
</tr>
<tr>

	<td align="center" valign="top" >
		<table  > 
		<tbody id="users" <bs:equal name="display_users" value="false">style="display:none"</bs:equal>>
		<bs:sequence name="l_users.v_info_users" >				
			<bs:bean name="current_user" property="l_users.v_info_users" index="sequence"/>
			<tr>
			<td id="user_<bs:formelement bean="current_user" name="name"/>" valign="top"> 
				<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_user_<bs:formelement bean="current_user" name="name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_user('<bs:formelement bean="current_user" name="name"/>',false)">
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement bean="current_user" name="name"/>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('user','<bs:formelement bean="current_user" name="name"/>','','')"/>
					</td>
				</tr>
				</table>
			</td>			
		</tr>
		</bs:sequence>	
		</tbody>
		</table>
	</td>

	<td align="center" valign="top" >
		<table  > 
		<tbody id="targets" <bs:equal name="display_targets" value="false">style="display:none"</bs:equal>>
		<bs:sequence name="l_users.v_info_targets" >				
			<bs:bean name="current_target" property="l_users.v_info_targets" index="sequence"/>
			<tr>
			<td id="target_<bs:formelement bean="current_target" name="name"/>" valign="top"> 
				<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_target_<bs:formelement bean="current_target" name="name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_target('<bs:formelement bean="current_target" name="name"/>',false)">
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement bean="current_target" name="name"/>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('target','<bs:formelement bean="current_target" name="name"/>','','')"/>
					</td>
				</tr>
				</table>
			</td>			
		</tr>
		</bs:sequence>	
		</tbody>
		</table>
	</td>

	<td align="center" valign="top" >
		<table  > 
		<tbody id="groups" <bs:equal name="display_groups" value="false">style="display:none"</bs:equal>>
		<bs:sequence name="l_users.v_info_groups" >				
			<bs:bean name="current_group" property="l_users.v_info_groups" index="sequence"/>
			<tr>
			<td id="group_<bs:formelement bean="current_group" name="name"/>" valign="top"> 
				<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_group_<bs:formelement bean="current_group" name="name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_group('<bs:formelement bean="current_group" name="name"/>',false)">
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement bean="current_group" name="name"/>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('group','<bs:formelement bean="current_group" name="name"/>','','')"/>
					</td>
				</tr>
				</table>
			</td>			
		</tr>
		</bs:sequence>	
		</tbody>
		</table>
	</td>

	<td align="center" valign="top" >
		<table  > 
		<tbody id="relations" <bs:equal name="display_relations" value="false">style="display:none"</bs:equal>>
		<bs:sequence name="l_authentication.v_info_relations" >				
			<bs:bean name="current_relation" property="l_authentication.v_info_relations" index="sequence"/>
			<tr>
			<td id="relation_<bs:formelement bean="current_relation" name="name"/>" valign="top"> 
				<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_relation_<bs:formelement bean="current_relation" name="name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_relation('<bs:formelement bean="current_relation" name="name"/>',false)">
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement bean="current_relation" name="name"/>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('relation','<bs:formelement bean="current_relation" name="name"/>','','')"/>
					</td>
				</tr>
				</table>
			</td>			
		</tr>
		</bs:sequence>	
		</tbody>
		</table>
	</td>


</tr>	
</table>
	
	




</bs:form>
</center>
<script>


	var posObj = getPosition(document.getElementById("canvas_report"));
	posTop=posObj.y;
	posLeft=posObj.x;

//	makeUnselectable(document.getElementById("canvas_report"));
	document.getElementById("canvas_report").scrollTop="<bs:formelement name="scrollTop"/>";
	document.getElementById("canvas_report").scrollLeft="<bs:formelement name="scrollLeft"/>";


	if(document.getElementById("canvas_table").offsetHeight>450){
		document.getElementById("canvas_report").style.overflowY="scroll";
		document.getElementById("canvas_report").style.height = 450;
	}else{
		document.getElementById("canvas_report").style.overflowY="auto";
	}
</script>




</div>
<jsp:include  page="/jsp/included/panel_bottom.jsp" />


</td>
</tr>
</table>
</div>
</body>
<%@ include file="../framework/footer.jsp" %>



