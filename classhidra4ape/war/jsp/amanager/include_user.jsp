<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<bs:equal name="middleAction" value="view_user">
	<bs:equal name="type_selected" value="close">
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_user_<bs:formelement name="selected_user.name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_user('<bs:formelement name="selected_user.name"/>',false)">					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement name="selected_user.name"/>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('user','<bs:formelement name="selected_user.name"/>','','')"/>
					</td>
				</tr>
			</table>
	</bs:equal>
	<bs:equal name="type_selected" value="open">
	
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_user_<bs:formelement name="selected_user.name"/>" src="images/menu/16_open.gif"/ border="0" style="cursor:pointer"
						onclick="draw_user('<bs:formelement name="selected_user.name"/>',false)">					
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<font color="blue"><bs:formelement name="selected_user.name"/></font>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('user','<bs:formelement name="selected_user.name"/>','','')"/>
					</td>
				</tr>
			</table>
	
<jsp:include page="/jsp/included/panel_top.jsp" />			
						<table width="400"   cellspacing="0">
							<tr>
								<td width="0%" class="page_section">name:</td>
								<td width="100%" id="user|<bs:formelement name="selected_user.name"/>|||name"><bs:input name="selected_user.name" style="border: solid 1px orange; width: 100%" toUpperCase="no" onchange="updateValue(this,'name')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">password:</td>
								<td width="100%" id="user|<bs:formelement name="selected_user.name"/>|||password"><bs:input name="selected_user.password" type="password" style="border: solid 1px orange; width: 100%" toUpperCase="no" onchange="updateValue(this,'password')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr> 
								<td width="0%" class="page_section">pass expired (yyyy-MM-dd):</td>
								<td width="100%" id="user|<bs:formelement name="selected_user.name"/>|||pass_expired"><bs:input name="selected_user.pass_expired" style="border: solid 1px ; width: 100%" toUpperCase="no" onchange="updateValue(this,'pass_expired')" onfocus="makeFocus(this)"/></td>
							</tr>
							
							<tr>
								<td width="0%" class="page_section">matriculation:</td>
								<td width="100%" id="user|<bs:formelement name="selected_user.name"/>|||matriculation"><bs:input name="selected_user.matriculation" style="border: solid 1px orange; width: 100%" toUpperCase="no" onchange="updateValue(this,'matriculation')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">language:</td>
								<td width="100%" id="user|<bs:formelement name="selected_user.name"/>|||language"><bs:input name="selected_user.language" style="border: solid 1px orange; width: 100%" toUpperCase="no" onchange="updateValue(this,'language')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">description:</td>
								<td width="100%" id="user|<bs:formelement name="selected_user.name"/>|||description"><bs:input name="selected_user.description" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'description')" onfocus="makeFocus(this)"/></td>
							</tr>							
							<tr>
								<td width="0%" class="page_section">email:</td>
								<td width="100%" id="user|<bs:formelement name="selected_user.name"/>|||email"><bs:input name="selected_user.email" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'email')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">comment:</td>
								<td width="100%" id="user|<bs:formelement name="selected_user.name"/>|||comment"><bs:input name="selected_user.comment" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'comment')" onfocus="makeFocus(this)"/></td>
							</tr>
							
							<tr>
								<td width="0%" class="page_section">crypted:</td>
								<td width="100%" id="user|<bs:formelement name="selected_user.name"/>|||crypted">
									<bs:select name="selected_user.crypted" style="border: solid 1px silver; width: 100%" onchange="updateValue(this,'crypted')" onfocus="makeFocus(this)">									
										<bs:options property="elements_true_false" value="code"  label="desc" />
									</bs:select>
								</td>
							</tr>
							<tr>
								<td width="0%" class="page_section">target:</td>
								<td width="100%"  id="user|<bs:formelement name="selected_user.name"/>|target||target" 
								style="border: solid 1px orange; width: 100%;cursor: pointer"
								onclick="showAsPopup('amanager?middleAction=list_targetsIntoUser&id_selected_user=<bs:formelement name="selected_user.name"/>',400,300)">&nbsp;<bs:formelement name="selected_user.target"/></td>
							</tr>
							
					</table>						
						<table width="400" cellspacing="0">
						<tbody id="groupsIntoUser_<bs:formelement name="selected_user.name"/>">
						<tr>
								<td colspan="2">
									<table width="100%" cellspacing="0" cellpadding="0" border="0">
										<tr>
											<td  height="20" width="50%" valign="middle">&nbsp;</td>
											<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
											<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section">groups</span></nobr></td>
											<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
											<td  height="20" width="50%"><img src="images/decor/20_add.gif" border="0" style="cursor:pointer" 
												onclick="showAsPopup('amanager?middleAction=list_groupsIntoUser&id_selected_user=<bs:formelement name="selected_user.name"/>',400,300)"></td>
										</tr>
									</table>
								</td>
													
						</tr> 
	
	<bs:sequence name="selected_user.v_info_groups">				
				<bs:bean name="current_group" property="selected_user.v_info_groups" index="sequence"/>	
						<tr >
							<td id="groupIntoUser_<bs:formelement name="selected_user.name"/>_<bs:formelement bean="current_group" name="name"/>">
								<table width="400" cellspacing="0" style="border: dashed 1px silver">
									<tr>
										<td width="0%" valign="middle">
											<img id="img_groupIntoUser_<bs:formelement name="selected_user.name"/>_<bs:formelement bean="current_group" name="name"/>" src="images/decor/16_groups.gif"/ border="0" style="cursor:pointer"
											>
										</td>
										<td width="100%" >
											<bs:formelement bean="current_group" name="name"/>&nbsp;&nbsp;
										</td>	
										<td width="0%"  valign="middle">			
											<img src="images/menu/close_section.gif" style="cursor:pointer"
											onclick="removeEntity('groupIntoUser','<bs:formelement name="selected_user.name"/>','<bs:formelement bean="current_group" name="name"/>','')"/>
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
