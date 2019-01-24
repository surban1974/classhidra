<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<bs:equal name="middleAction" value="view_section">
	<bs:equal name="type_selected" value="close">
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_section_<bs:formelement name="selected_action.path"/>_<bs:formelement name="selected_redirect.path"/>_<bs:formelement name="selected_section.name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_section('<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>','<bs:formelement name="selected_section.name"/>',false)">
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement name="selected_section.name"/>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('section','<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>','<bs:formelement name="selected_section.name"/>')"/>
					</td>
				</tr>
			</table>
	</bs:equal>
	<bs:equal name="type_selected" value="open">
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_section_<bs:formelement name="selected_action.path"/>_<bs:formelement name="selected_redirect.path"/>_<bs:formelement name="selected_section.name"/>" src="images/menu/16_open.gif"/ border="0" style="cursor:pointer"
						onclick="draw_section('<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>','<bs:formelement name="selected_section.name"/>',false)">
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<font color="blue"><bs:formelement name="selected_section.name"/></font>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('section','<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>','<bs:formelement name="selected_section.name"/>')"/>
					</td>
				</tr>
			</table>
<jsp:include page="/jsp/included/panel_top.jsp" />			
						<table width="400" cellspacing="0">
							<tr>
								<td>&nbsp;&nbsp;</td>
								<td width="0%" class="page_section">name:</td>
								<td width="100%" id="section|<bs:formelement name="selected_action.path"/>|<bs:formelement name="selected_redirect.path"/>|<bs:formelement name="selected_section.name"/>|name"><bs:input name="selected_section.name" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'name')" onfocus="makeFocus(this)"/></td>
							</tr>
						</table>
<jsp:include page="/jsp/included/panel_bottom.jsp" />
	</bs:equal>						

</bs:equal>

