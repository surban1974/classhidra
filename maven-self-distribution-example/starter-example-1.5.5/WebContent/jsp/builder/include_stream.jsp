<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<bs:equal name="middleAction" value="view_stream">
	<bs:equal name="type_selected" value="close">
			<table cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_stream_<bs:formelement name="selected_stream.name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_stream('<bs:formelement name="selected_stream.name"/>',false)">
					</td>
					<td  background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement name="selected_stream.name"/>&nbsp;&nbsp;
					</td>	
<bs:equal name="selected_stream.system" value="false">		
					<td width="0%" background="images/corners/panel_t.gif">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('stream','<bs:formelement name="selected_stream.name"/>','','')"/>					</td>
					<td width="0%">&#8594;</td>
</bs:equal>									
				</tr>
			</table>

	
	</bs:equal>
	<bs:equal name="type_selected" value="open">	
			<table cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_stream_<bs:formelement name="selected_stream.name"/>" src="images/menu/16_open.gif"/ border="0" style="cursor:pointer"
						onclick="draw_stream('<bs:formelement name="selected_stream.name"/>',false)">
					</td>
					<td  background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement name="selected_stream.name"/>&nbsp;&nbsp;
					</td>	
<bs:equal name="selected_stream.system" value="false">		
					<td width="0%" background="images/corners/panel_t.gif">			
					<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('stream','<bs:formelement name="selected_stream.name"/>','','')"/>
					</td>
					<td width="0%">&#8594;</td>
</bs:equal>									
				</tr>
			</table>
	
<jsp:include page="/jsp/included/panel_top.jsp" />		
						<table width="400" cellspacing="0">
							<tr>
								<td width="0%" class="page_section">name:</td>
								<td width="100%" id="stream|<bs:formelement name="selected_stream.name"/>|||name"><bs:input name="selected_stream.name" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'name')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">type:</td>
								<td width="100%" id="stream|<bs:formelement name="selected_stream.name"/>|||type"><bs:input name="selected_stream.type" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'type')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">order:</td>
								<td width="100%" id="stream|<bs:formelement name="selected_stream.name"/>|||order"><bs:input name="selected_stream.order" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'order')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">provider:</td>
								<td width="100%" id="stream|<bs:formelement name="selected_stream.name"/>|||provider"><bs:input name="selected_stream.provider" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'provider')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">property:</td>
								<td width="100%" id="stream|<bs:formelement name="selected_stream.name"/>|||property"><bs:input name="selected_stream.property" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'property')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">comment:</td>
								<td width="100%" id="stream|<bs:formelement name="selected_stream.name"/>|||comment"><bs:input name="selected_stream.comment" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'comment')" onfocus="makeFocus(this)"/></td>
							</tr>
							
						</table>
						<table  width="100%" style="border: dashed 1px silver" cellspacing="0">
						<tbody id="applys-to-action_<bs:formelement name="selected_stream.name"/>">
						<tr>
							<td colspan="2">
									<table width="100%" cellspacing="0" cellpadding="0" border="0">
										<tr>
											<td  height="20" width="50%" valign="middle">&nbsp;</td>
											<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
											<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section">apply-to-action</span></nobr></td>
											<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
											<td  height="20" width="50%"><img src="images/decor/20_add.gif" border="0" style="cursor:pointer" onclick="addEntity('apply_to_action','<bs:formelement name="selected_stream.name"/>','')"></td>
										</tr>
									</table>
								</td>
	
						
						</tr> 
						
	<bs:sequence name="selected_stream.v_info_apply_to_action">				
				<bs:bean name="current_apply_to_action" property="selected_stream.v_info_apply_to_action" index="sequence"/>	
						<tr >
							<td id="apply_to_action_<bs:formelement name="selected_stream.name"/>_<bs:formelement bean="current_apply_to_action" name="action"/>">
								<table width="400"  cellspacing="0">
									<tr>
										<td width="0%" background="images/corners/panel_t.gif" valign="middle">
											<img id="img_apply_to_action_<bs:formelement name="selected_stream.name"/>_<bs:formelement bean="current_apply_to_action" name="action"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
											onclick="draw_apply_to_action('<bs:formelement name="selected_stream.name"/>','<bs:formelement bean="current_apply_to_action" name="action"/>',false)">
										</td>
										<td width="100%" background="images/corners/panel_t.gif" class="page_section">
											<bs:formelement bean="current_apply_to_action" name="action"/>&nbsp;&nbsp;
										</td>	
					<bs:equal name="selected_stream.system" value="false">		
										<td width="0%" background="images/corners/panel_t.gif">			
											<img src="images/menu/close_section.gif" style="cursor:pointer"
											onclick="removeEntity('apply_to_action','<bs:formelement name="selected_stream.name"/>','<bs:formelement bean="current_apply_to_action" name="action"/>','')"/>										</td>
					</bs:equal>									
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