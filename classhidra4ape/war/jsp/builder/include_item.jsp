<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<bs:equal name="middleAction" value="view_item">
	<bs:equal name="type_selected" value="close">
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_item_<bs:formelement name="selected_bean.name"/>_<bs:formelement name="selected_item.name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_item('<bs:formelement name="selected_bean.name"/>','<bs:formelement name="selected_item.name"/>',false)">
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement name="selected_item.name"/>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('item','<bs:formelement name="selected_bean.name"/>','<bs:formelement name="selected_item.name"/>','')"/>
					</td>
				</tr>
			</table>
	</bs:equal>
	<bs:equal name="type_selected" value="open">
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_item_<bs:formelement name="selected_bean.name"/>_<bs:formelement name="selected_item.name"/>" src="images/menu/16_open.gif"/ border="0" style="cursor:pointer"
						onclick="draw_item('<bs:formelement name="selected_bean.name"/>','<bs:formelement name="selected_item.name"/>',false)">
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<font color="blue"><bs:formelement name="selected_item.name"/></font>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('item','<bs:formelement name="selected_bean.name"/>','<bs:formelement name="selected_item.name"/>','')"/>
					</td>
				</tr>
			</table>
<jsp:include page="/jsp/included/panel_top.jsp" />	
						<table width="400"   cellspacing="0">
							<tr>
								<td>&nbsp;&nbsp;</td>
								<td width="0%" class="page_section">name:</td>
								<td width="100%" id="item|<bs:formelement name="selected_bean.name"/>|<bs:formelement name="selected_item.name"/>||name"><bs:input name="selected_item.name" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'name')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td>&nbsp;&nbsp;</td>
								<td width="0%" class="page_section">type:</td>
								<td width="100%" id="item|<bs:formelement name="selected_bean.name"/>|<bs:formelement name="selected_item.name"/>||type">
									<bs:select name="selected_item.type" style="border: solid 1px silver; width: 100%" onchange="updateValue(this,'type')" onfocus="makeFocus(this)">									
										<option value="..." >...</option>
										<bs:options property="elements_item_types" value="code"  label="desc" />
									</bs:select>
									<bs:input name="selected_item.type" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'type')" onfocus="makeFocus(this)"/>
								</td>
							</tr>
							<tr>
								<td>&nbsp;&nbsp;</td>
								<td width="0%" class="page_section">defValue:</td>
								<td width="100%" id="item|<bs:formelement name="selected_bean.name"/>|<bs:formelement name="selected_item.name"/>||defValue"><bs:input name="selected_item.defValue" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'defValue')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td>&nbsp;&nbsp;</td>
								<td width="0%" class="page_section">comment:</td>
								<td width="100%" id="item|<bs:formelement name="selected_bean.name"/>|<bs:formelement name="selected_item.name"/>||comment"><bs:input name="selected_item.comment" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'comment')" onfocus="makeFocus(this)"/></td>
							</tr>
						</table>
						
<jsp:include page="/jsp/included/panel_bottom.jsp" />						
	</bs:equal>						

</bs:equal>