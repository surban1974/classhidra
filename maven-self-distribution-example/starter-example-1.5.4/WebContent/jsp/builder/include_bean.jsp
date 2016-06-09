<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<bs:equal name="middleAction" value="view_bean">
	<bs:equal name="type_selected" value="close">
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_bean_<bs:formelement name="selected_bean.name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_bean('<bs:formelement name="selected_bean.name"/>','',false)">
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement name="selected_bean.name"/>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('bean','<bs:formelement name="selected_bean.name"/>','','')"/>
					</td>
				</tr>
			</table>
	</bs:equal>
	<bs:equal name="type_selected" value="open">
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_bean_<bs:formelement name="selected_bean.name"/>" src="images/menu/16_open.gif"/ border="0" style="cursor:pointer"
						onclick="draw_bean('<bs:formelement name="selected_bean.name"/>','',false)">
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<font color="blue"><bs:formelement name="selected_bean.name"/></font>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('bean','<bs:formelement name="selected_bean.name"/>','','')"/>
					</td>
				</tr>
			</table>
<jsp:include page="/jsp/included/panel_top.jsp" />
						<table width="400"  cellspacing="0">
							<tr>
								<td width="0%" class="page_section">name:</td>
								<td width="100%" id="bean|<bs:formelement name="selected_bean.name"/>|||name"><bs:input name="selected_bean.name" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'name')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">type:</td>
								<td width="100%" id="bean|<bs:formelement name="selected_bean.name"/>|||type"><bs:input name="selected_bean.type" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'type')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">property:</td>
								<td width="100%" id="bean|<bs:formelement name="selected_bean.name"/>|||property"><bs:input name="selected_bean.property" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'property')" onfocus="makeFocus(this)"/></td>
							</tr>														
							<tr>
								<td width="0%" class="page_section">provider:</td>
								<td width="100%" id="bean|<bs:formelement name="selected_bean.name"/>|||provider"><bs:input name="selected_bean.provider" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'provider')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">comment:</td>
								<td width="100%" id="bean|<bs:formelement name="selected_bean.name"/>|||comment"><bs:input name="selected_bean.comment" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'comment')" onfocus="makeFocus(this)"/></td>
							</tr>														
							<tr>
								<td width="0%" class="page_section">model:</td>
								<td width="100%" id="bean|<bs:formelement name="selected_bean.name"/>|||model"><bs:input name="selected_bean.model" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'model')" onfocus="makeFocus(this)"/></td>
							</tr>
																					
						</table>
						
						<table width="100%" style="border: dashed 1px silver" cellspacing="0">
						<tbody id="items_<bs:formelement name="selected_bean.name"/>">
						<tr>
							<td colspan="2">
									<table width="100%" cellspacing="0" cellpadding="0" border="0">
										<tr>
											<td  height="20" width="50%" valign="middle">&nbsp;</td>
											<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
											<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section">model-items(virtual)</span></nobr></td>
											<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
											<td  height="20" width="50%"><img src="images/decor/20_add.gif" border="0" style="cursor:pointer" onclick="addEntity('item','<bs:formelement name="selected_bean.name"/>','')"></td>
										</tr>
									</table>
								</td>
	
						
						</tr> 
	
	<bs:sequence name="selected_bean.v_info_items">				
				<bs:bean name="current_item" property="selected_bean.v_info_items" index="sequence"/>	
						<tr >
							<td id="item_<bs:formelement name="selected_bean.name"/>_<bs:formelement bean="current_item" name="name"/>">
								<table width="400" cellspacing="0">
									<tr>
										<td width="0%" background="images/corners/panel_t.gif" valign="middle">
											<img id="img_item_<bs:formelement name="selected_bean.name"/>_<bs:formelement bean="current_item" name="name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
											onclick="draw_item('<bs:formelement name="selected_bean.name"/>','<bs:formelement bean="current_item" name="name"/>',false)">
										</td>
										<td width="100%" background="images/corners/panel_t.gif" class="page_section">
											<bs:formelement bean="current_item" name="name"/>&nbsp;&nbsp;
										</td>	
										<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
											<img src="images/menu/close_section.gif" style="cursor:pointer"
											onclick="removeEntity('item','<bs:formelement name="selected_bean.name"/>','<bs:formelement bean="current_item" name="name"/>','')"/>
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
