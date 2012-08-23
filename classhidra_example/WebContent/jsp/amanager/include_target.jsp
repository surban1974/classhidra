<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<bs:equal name="middleAction" value="view_target">
	<bs:equal name="type_selected" value="close">
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_target_<bs:formelement name="selected_target.name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_target('<bs:formelement name="selected_target.name"/>',false)">					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement name="selected_target.name"/>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('target','<bs:formelement name="selected_target.name"/>','','')"/>
					</td>
				</tr>
			</table>
	</bs:equal>
	<bs:equal name="type_selected" value="open">
	
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_target_<bs:formelement name="selected_target.name"/>" src="images/menu/16_open.gif"/ border="0" style="cursor:pointer"
						onclick="draw_target('<bs:formelement name="selected_target.name"/>',false)">					
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<font color="blue"><bs:formelement name="selected_target.name"/></font>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('target','<bs:formelement name="selected_target.name"/>','','')"/>
					</td>
				</tr>
			</table>
	
<jsp:include page="/jsp/included/panel_top.jsp" />			
						<table width="400"   cellspacing="0">
							<tr>
								<td width="0%" class="page_section">name:</td>
								<td width="100%" id="target|<bs:formelement name="selected_target.name"/>|||name"><bs:input name="selected_target.name" style="border: solid 1px orange; width: 100%" toUpperCase="no" onchange="updateValue(this,'name')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">comment:</td>
								<td width="100%" id="target|<bs:formelement name="selected_target.name"/>|||comment"><bs:input name="selected_target.comment" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'comment')" onfocus="makeFocus(this)"/></td>
							</tr>
					</table>						
						<table width="400" cellspacing="0">
						<tbody id="usersIntoTarget_<bs:formelement name="selected_target.name"/>">
						<tr>
								<td colspan="2">
									<table width="100%" cellspacing="0" cellpadding="0" border="0">
										<tr>
											<td  height="20" width="50%" valign="middle">&nbsp;</td>
											<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
											<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section">users</span></nobr></td>
											<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
											<td  height="20" width="50%"><img src="images/decor/20_add.gif" border="0" style="cursor:pointer" 
												onclick="showAsPopup('amanager?middleAction=list_usersIntoTarget&id_selected_target=<bs:formelement name="selected_target.name"/>',400,300)"></td>
										</tr>
									</table>
								</td>
													
						</tr> 
	
	<bs:sequence name="selected_target.v_info_users">				
				<bs:bean name="current_user" property="selected_target.v_info_users" index="sequence"/>	
						<tr >
							<td id="userIntoTarget_<bs:formelement name="selected_target.name"/>_<bs:formelement bean="current_user" name="name"/>">
								<table width="400" cellspacing="0" style="border: dashed 1px silver">
									<tr>
										<td width="0%" valign="middle">
											<img id="img_userIntoTarget_<bs:formelement name="selected_target.name"/>_<bs:formelement bean="current_user" name="name"/>" src="images/decor/16_users.gif"/ border="0" style="cursor:pointer"
											>
										</td>
										<td width="100%" >
											<bs:formelement bean="current_user" name="name"/>&nbsp;&nbsp;
										</td>	
										<td width="0%"  valign="middle">			
											<img src="images/menu/close_section.gif" style="cursor:pointer"
											onclick="removeEntity('userIntoTarget','<bs:formelement name="selected_target.name"/>','<bs:formelement bean="current_user" name="name"/>','')"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
	
	</bs:sequence>		
						</tbody>
						</table>


<jsp:include page="/jsp/included/panel_bottom.jsp" />				
	</bs:equal>
</bs:equal>	
