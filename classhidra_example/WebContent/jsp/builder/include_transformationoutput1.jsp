<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<bs:equal name="middleAction" value="view_transformationoutput1">
	<bs:equal name="type_selected" value="close">
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_transformationoutput_<bs:formelement name="selected_action.path"/>_<bs:formelement name="selected_redirect.path"/>_<bs:formelement name="selected_transformationoutput.name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_transformationoutput1('<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>','<bs:formelement name="selected_transformationoutput.name"/>',false)">
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement name="selected_transformationoutput.name"/>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('transformationoutput1','<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>','<bs:formelement name="selected_transformationoutput.name"/>')"/>
					</td>
				</tr>
			</table>
	</bs:equal>
	<bs:equal name="type_selected" value="open">
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_transformationoutput_<bs:formelement name="selected_action.path"/>_<bs:formelement name="selected_redirect.path"/>_<bs:formelement name="selected_transformationoutput.name"/>" src="images/menu/16_open.gif"/ border="0" style="cursor:pointer"
						onclick="draw_transformationoutput1('<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>','<bs:formelement name="selected_transformationoutput.name"/>',false)">
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<font color="blue"><bs:formelement name="selected_transformationoutput.name"/></font>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('transformationoutput','<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>','<bs:formelement name="selected_transformationoutput.name"/>')"/>
					</td>
				</tr>
			</table>
<jsp:include page="/jsp/included/panel_top.jsp" />			
						<table width="400" cellspacing="0">
							<tr>
								<td>&nbsp;&nbsp;</td>
								<td width="0%" class="page_section">name:</td>
								<td width="100%" id="transformationoutput1|<bs:formelement name="selected_action.path"/>|<bs:formelement name="selected_redirect.path"/>|<bs:formelement name="selected_transformationoutput.name"/>|name"><bs:input name="selected_transformationoutput.name" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'name')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td>&nbsp;&nbsp;</td>
								<td width="0%" class="page_section">type:</td>
								<td width="100%" id="transformationoutput1|<bs:formelement name="selected_action.path"/>|<bs:formelement name="selected_redirect.path"/>|<bs:formelement name="selected_transformationoutput.name"/>|type"><bs:input name="selected_transformationoutput.type" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'type')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td>&nbsp;&nbsp;</td>
								<td width="0%" class="page_section">path:</td>
								<td width="100%" id="transformationoutput1|<bs:formelement name="selected_action.path"/>|<bs:formelement name="selected_redirect.path"/>|<bs:formelement name="selected_transformationoutput.name"/>|path"><bs:input name="selected_transformationoutput.path" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'path')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td>&nbsp;&nbsp;</td>
								<td width="0%" class="page_section">event:</td>
								<td width="100%" id="transformationoutput1|<bs:formelement name="selected_action.path"/>|<bs:formelement name="selected_redirect.path"/>|<bs:formelement name="selected_transformationoutput.name"/>|event">
									<bs:select name="selected_transformationoutput.event" style="border: solid 1px silver; width: 100%" onchange="updateValue(this,'event')" onfocus="makeFocus(this)">									
										<bs:options property="elements_transformationoutput_event" value="code"  label="desc" />
									</bs:select>
								</td>
							</tr>
							<tr>
								<td>&nbsp;&nbsp;</td>
								<td width="0%" class="page_section">comment:</td>
								<td width="100%" id="transformationoutput1|<bs:formelement name="selected_action.path"/>|<bs:formelement name="selected_redirect.path"/>|<bs:formelement name="selected_transformationoutput.name"/>|comment"><bs:input name="selected_transformationoutput.comment" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'comment')" onfocus="makeFocus(this)"/></td>
							</tr>
						</table>
<jsp:include page="/jsp/included/panel_bottom.jsp" />
	</bs:equal>						

</bs:equal>

