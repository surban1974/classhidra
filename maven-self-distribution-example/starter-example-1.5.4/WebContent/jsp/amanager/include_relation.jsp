<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<bs:equal name="middleAction" value="view_relation">
	<bs:equal name="type_selected" value="close">
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_relation_<bs:formelement name="selected_relation.name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_relation('<bs:formelement name="selected_relation.name"/>',false)">					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement name="selected_relation.name"/>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('relation','<bs:formelement name="selected_relation.name"/>','','')"/>
					</td>
				</tr>
			</table>
	</bs:equal>
	<bs:equal name="type_selected" value="open">
	
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_relation_<bs:formelement name="selected_relation.name"/>" src="images/menu/16_open.gif"/ border="0" style="cursor:pointer"
						onclick="draw_relation('<bs:formelement name="selected_relation.name"/>',false)">					
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<font color="blue"><bs:formelement name="selected_relation.name"/></font>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('relation','<bs:formelement name="selected_relation.name"/>','','')"/>
					</td>
				</tr>
			</table>
	
<jsp:include page="/jsp/included/panel_top.jsp" />			
						<table width="400"   cellspacing="0">
							<tr>
								<td width="0%" class="page_section">name:</td>
								<td width="100%" id="relation|<bs:formelement name="selected_relation.name"/>|||name"><bs:input name="selected_relation.name" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'name')" onfocus="makeFocus(this)"/></td>
							</tr>

							<tr>
								<td width="0%" class="page_section">comment:</td>
								<td width="100%" id="relation|<bs:formelement name="selected_relation.name"/>|||comment"><bs:input name="selected_relation.comment" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'comment')" onfocus="makeFocus(this)"/></td>
							</tr>
					</table>						
					<table width="400" cellspacing="0">
						<tbody id="usersIntoGroup_<bs:formelement name="selected_relation.name"/>">
						<tr>
								<td colspan="2">
									<table width="100%" cellspacing="0" cellpadding="0" border="0">
										<tr>
											<td  height="20" width="50%" valign="middle">&nbsp;</td>
											<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
											<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section">targets</span></nobr></td>
											<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
											<td  height="20" width="50%"><img src="images/decor/20_add.gif" border="0" style="cursor:pointer" 
												onclick="showAsPopup('amanager?middleAction=list_targetsIntoRelation&id_selected_relation=<bs:formelement name="selected_relation.name"/>',400,300)"></td>
										</tr>
									</table>
								</td>
													
						</tr> 
	
	<bs:sequence name="selected_relation.v_info_targets">				
				<bs:bean name="current_target" property="selected_relation.v_info_targets" index="sequence"/>	
						<tr >
							<td id="targetIntoRelation_<bs:formelement name="selected_relation.name"/>_<bs:formelement bean="current_target" name="name"/>">
								<table width="400" cellspacing="0" style="border: dashed 1px silver">
									<tr>
										<td width="0%" valign="middle">
											<img id="img_targetIntoRelation_<bs:formelement name="selected_relation.name"/>_<bs:formelement bean="current_target" name="name"/>" src="images/decor/16_users.gif"/ border="0" style="cursor:pointer"
											>
										</td>
										<td width="100%" >
											<bs:formelement bean="current_target" name="name"/>&nbsp;&nbsp;
										</td>	
										<td width="0%"  valign="middle">			
											<img src="images/menu/close_section.gif" style="cursor:pointer"
											onclick="removeEntity('targetIntoRelation','<bs:formelement name="selected_relation.name"/>','<bs:formelement bean="current_target" name="name"/>','')"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
	
	</bs:sequence>		
						</tbody>
						</table>
						
					<table width="400" cellspacing="0">
						<tbody id="usersIntoGroup_<bs:formelement name="selected_relation.name"/>">
						<tr>
								<td colspan="2">
									<table width="100%" cellspacing="0" cellpadding="0" border="0">
										<tr>
											<td  height="20" width="50%" valign="middle">&nbsp;</td>
											<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
											<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section">groups</span></nobr></td>
											<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
											<td  height="20" width="50%"><img src="images/decor/20_add.gif" border="0" style="cursor:pointer" 
												onclick="showAsPopup('amanager?middleAction=list_groupsIntoRelation&id_selected_relation=<bs:formelement name="selected_relation.name"/>',400,300)"></td>
										</tr>
									</table>
								</td>
													
						</tr> 
	
	<bs:sequence name="selected_relation.v_info_groups">				
				<bs:bean name="current_group" property="selected_relation.v_info_groups" index="sequence"/>	
						<tr >
							<td id="groupIntoRelation_<bs:formelement name="selected_relation.name"/>_<bs:formelement bean="current_group" name="name"/>">
								<table width="400" cellspacing="0" style="border: dashed 1px silver">
									<tr>
										<td width="0%" valign="middle">
											<img id="img_groupIntoRelation_<bs:formelement name="selected_relation.name"/>_<bs:formelement bean="current_group" name="name"/>" src="images/decor/16_users.gif"/ border="0" style="cursor:pointer"
											>
										</td>
										<td width="100%" >
											<bs:formelement bean="current_group" name="name"/>&nbsp;&nbsp;
										</td>	
										<td width="0%"  valign="middle">			
											<img src="images/menu/close_section.gif" style="cursor:pointer"
											onclick="removeEntity('groupIntoRelation','<bs:formelement name="selected_relation.name"/>','<bs:formelement bean="current_group" name="name"/>','')"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
	
	</bs:sequence>		
						</tbody>
						</table>						
						
						
						
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_relation_auth_<bs:formelement name="selected_relation.name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_auth('<bs:formelement name="selected_relation.name"/>',false)">					
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						authentications&nbsp;&nbsp;
					</td>
				</tr>
				<tr>
					<td id="relation_auth_<bs:formelement name="selected_relation.name"/>" colspan="2"></td>
				</tr>
			</table>	
	

<jsp:include page="/jsp/included/panel_bottom.jsp" />				
	</bs:equal>
</bs:equal>	
