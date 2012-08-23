<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<bs:equal name="middleAction" value="view_apply_to_action">
	<bs:equal name="type_selected" value="close">
						<table width="400" cellspacing="0">
							<tr>
								<td width="0%" background="images/corners/panel_t.gif" valign="middle">
									<img id="img_apply_to_action_<bs:formelement name="selected_stream.name"/>_<bs:formelement name="selected_apply_to_action.action"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
									onclick="draw_apply_to_action('<bs:formelement name="selected_stream.name"/>','<bs:formelement name="selected_apply_to_action.action"/>',false)">
								</td>
								<td  width="100%" background="images/corners/panel_t.gif" class="page_section">
									<bs:formelement name="selected_apply_to_action.action"/>&nbsp;&nbsp;
								</td>	
								<td width="0%" background="images/corners/panel_t.gif">			
									<img src="images/menu/close_section.gif" style="cursor:pointer"
									onclick="removeEntity('apply_to_action','<bs:formelement name="selected_stream.name"/>','<bs:formelement name="selected_apply_to_action.action"/>','')"/>
								</td>
							</tr>
						</table>
		</bs:equal>
	<bs:equal name="type_selected" value="open">
						<table width="400" cellspacing="0">
							<tr>
								<td width="0%" background="images/corners/panel_t.gif" valign="middle">
									<img id="img_apply_to_action_<bs:formelement name="selected_stream.name"/>_<bs:formelement name="selected_apply_to_action.action"/>" src="images/menu/16_open.gif"/ border="0" style="cursor:pointer"
									onclick="draw_apply_to_action('<bs:formelement name="selected_stream.name"/>','<bs:formelement name="selected_apply_to_action.action"/>',false)">
								</td>
								<td width="100%" background="images/corners/panel_t.gif" class="page_section">
									<bs:formelement name="selected_apply_to_action.action"/>&nbsp;&nbsp;
								</td>	
								<td width="0%" background="images/corners/panel_t.gif">			
									<img src="images/menu/close_section.gif" style="cursor:pointer"
									onclick="removeEntity('apply_to_action','<bs:formelement name="selected_stream.name"/>','<bs:formelement name="selected_apply_to_action.action"/>','')"/>
								</td>
								
							</tr>
						</table>
<jsp:include page="/jsp/included/panel_top.jsp" />		
						<table  width="400" cellspacing="0" >
							<tr>
								<td width="0%" class="page_section">action:</td>
								<td width="100%" id="apply_to_action|<bs:formelement name="selected_stream.name"/>|<bs:formelement name="selected_apply_to_action.action"/>||action">
									<bs:select name="selected_apply_to_action.action" style="border: solid 1px silver; width: 100%" onchange="updateValue(this,'action')" onfocus="makeFocus(this)">									
										<option value="*" <bs:equal name="selected_apply_to_action.action" value="*"> SELECTED </bs:equal>>*</option>
										<bs:options property="l_actions.v_info_actions" value="path"  label="path" />
									</bs:select>
								</td>
							</tr>
							<tr>
								<td width="0%" class="page_section">excluded:</td>
								<td width="100%" id="apply_to_action|<bs:formelement name="selected_stream.name"/>|<bs:formelement name="selected_apply_to_action.action"/>||excluded">
									<bs:select name="selected_apply_to_action.excluded" style="border: solid 1px silver; width: 100%" onchange="updateValue(this,'excluded')" onfocus="makeFocus(this)">
										<bs:options property="elements_true_false" value="code"  label="desc" />
									</bs:select>
								</td>
							</tr>
							<tr>
								<td width="0%" class="page_section">provider:</td>
								<td width="100%" id="apply_to_action|<bs:formelement name="selected_stream.name"/>|<bs:formelement name="selected_apply_to_action.action"/>||provider"><bs:input name="selected_apply_to_action.provider" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'provider')" onfocus="makeFocus(this)"/></td>
							</tr>						
							<tr>
								<td width="0%" class="page_section">property:</td>
								<td width="100%" id="apply_to_action|<bs:formelement name="selected_stream.name"/>|<bs:formelement name="selected_apply_to_action.action"/>||property"><bs:input name="selected_apply_to_action.property" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'property')" onfocus="makeFocus(this)"/></td>
							</tr>	
							<tr>
								<td width="0%" class="page_section">comment:</td>
								<td width="100%" id="apply_to_action|<bs:formelement name="selected_stream.name"/>|<bs:formelement name="selected_apply_to_action.action"/>||comment"><bs:input name="selected_apply_to_action.comment" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'comment')" onfocus="makeFocus(this)"/></td>
							</tr>						
												
						</table>
<jsp:include page="/jsp/included/panel_bottom.jsp" />
	</bs:equal>
</bs:equal>
