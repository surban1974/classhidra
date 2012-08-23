<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<bs:equal name="middleAction" value="view_redirect">
	<bs:equal name="type_selected" value="close">
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_redirect_<bs:formelement name="selected_action.path"/>_<bs:formelement name="selected_redirect.path"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_redirect('<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>',false)">
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement name="selected_redirect.path"/>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('redirect','<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>','')"/>
					</td>
				</tr>
			</table>
	</bs:equal>
	<bs:equal name="type_selected" value="open">
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_redirect_<bs:formelement name="selected_action.path"/>_<bs:formelement name="selected_redirect.path"/>" src="images/menu/16_open.gif"/ border="0" style="cursor:pointer"
						onclick="draw_redirect('<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>',false)">
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<font color="blue"><bs:formelement name="selected_redirect.path"/></font>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('redirect','<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>','')"/>
					</td>
				</tr>
			</table>
<jsp:include page="/jsp/included/panel_top.jsp" />	
						<table width="400"   cellspacing="0">
							<tr>
								<td>&nbsp;&nbsp;</td>
								<td width="0%" class="page_section">path:</td>
								<td width="100%" id="redirect|<bs:formelement name="selected_action.path"/>|<bs:formelement name="selected_redirect.path"/>||path"><bs:input name="selected_redirect.path" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'path')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td>&nbsp;&nbsp;</td>
								<td width="0%" class="page_section">auth_id:</td>
								<td width="100%" id="redirect|<bs:formelement name="selected_action.path"/>|<bs:formelement name="selected_redirect.path"/>||auth_id"><bs:input name="selected_redirect.auth_id" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'auth_id')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td>&nbsp;&nbsp;</td>
								<td width="0%" class="page_section">navigated:</td>
								<td width="100%" id="redirect|<bs:formelement name="selected_action.path"/>|<bs:formelement name="selected_redirect.path"/>||navigated">
									<bs:select name="selected_redirect.navigated" style="border: solid 1px silver; width: 100%" onchange="updateValue(this,'navigated')" onfocus="makeFocus(this)">									
										<bs:options property="elements_true_false" value="code"  label="desc" />
									</bs:select>
								</td>
							</tr>
							
							<tr>
								<td>&nbsp;&nbsp;</td>
								<td width="0%" class="page_section">comment:</td>
								<td width="100%" id="redirect|<bs:formelement name="selected_action.path"/>|<bs:formelement name="selected_redirect.path"/>||comment"><bs:input name="selected_redirect.comment" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'comment')" onfocus="makeFocus(this)"/></td>
							</tr>
						</table>
						
						<table width="100%" style="border: dashed 1px silver" cellspacing="0">
						<tbody id="transformationoutputs_<bs:formelement name="selected_action.path"/>_<bs:formelement name="selected_redirect.path"/>">
						<tr>
							<td colspan="2">
									<table width="100%" cellspacing="0" cellpadding="0" border="0">
										<tr>
											<td  height="20" width="50%" valign="middle">&nbsp;</td>
											<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
											<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section">redirect-transformationoutput</span></nobr></td>
											<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
											<td  height="20" width="50%"><img src="images/decor/20_add.gif" border="0" style="cursor:pointer" onclick="addEntity('transformationoutput1','<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>')"></td>
										</tr>
									</table>
								</td>
	
						
						</tr> 
	
	<bs:sequence name="selected_redirect.v_info_transformationoutput">				
				<bs:bean name="current_transformationoutput" property="selected_redirect.v_info_transformationoutput" index="sequence"/>	
						<tr >
							<td id="transformationoutput_<bs:formelement name="selected_action.path"/>_<bs:formelement name="selected_redirect.path"/>_<bs:formelement bean="current_transformationoutput" name="name"/>">
								<table width="400" cellspacing="0">
									<tr>
										<td width="0%" background="images/corners/panel_t.gif" valign="middle">
											<img id="img_transformationoutput_<bs:formelement name="selected_action.path"/>_<bs:formelement name="selected_redirect.path"/>_<bs:formelement bean="current_transformationoutput" name="name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
											onclick="draw_transformationoutput1('<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>','<bs:formelement bean="current_transformationoutput" name="name"/>',false)">
										</td>
										<td width="100%" background="images/corners/panel_t.gif" class="page_section">
											<bs:formelement bean="current_transformationoutput" name="name"/>&nbsp;&nbsp;
										</td>	
										<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
											<img src="images/menu/close_section.gif" style="cursor:pointer"
											onclick="removeEntity('transformationoutput1','<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>','<bs:formelement bean="current_transformationoutput" name="name"/>')"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
	
	</bs:sequence>		
						</tbody>
						</table>	
						<table width="100%" style="border: dashed 1px silver" cellspacing="0">
						<tbody id="sections_<bs:formelement name="selected_action.path"/>_<bs:formelement name="selected_redirect.path"/>">
						<tr>
							<td colspan="2">
									<table width="100%" cellspacing="0" cellpadding="0" border="0">
										<tr>
											<td  height="20" width="50%" valign="middle">&nbsp;</td>
											<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
											<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section">view-section</span></nobr></td>
											<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
											<td  height="20" width="50%"><img src="images/decor/20_add.gif" border="0" style="cursor:pointer" onclick="addEntity('section','<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>')"></td>
										</tr>
									</table>
								</td>
	
						
						</tr> 
	
	<bs:sequence name="selected_redirect.v_info_sections">				
				<bs:bean name="current_section" property="selected_redirect.v_info_sections" index="sequence"/>	
						<tr >
							<td id="section_<bs:formelement name="selected_action.path"/>_<bs:formelement name="selected_redirect.path"/>_<bs:formelement bean="current_section" name="name"/>">
								<table width="400" cellspacing="0">
									<tr>
										<td width="0%" background="images/corners/panel_t.gif" valign="middle">
											<img id="img_section_<bs:formelement name="selected_action.path"/>_<bs:formelement name="selected_redirect.path"/>_<bs:formelement bean="current_section" name="name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
											onclick="draw_section('<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>','<bs:formelement bean="current_section" name="name"/>',false)">
										</td>
										<td width="100%" background="images/corners/panel_t.gif" class="page_section">
											<bs:formelement bean="current_section" name="name"/>&nbsp;&nbsp;
										</td>	
										<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
											<img src="images/menu/close_section.gif" style="cursor:pointer"
											onclick="removeEntity('section','<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>','<bs:formelement bean="current_section" name="name"/>')"/>
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