<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<bs:equal name="middleAction" value="view_action">
	<bs:equal name="type_selected" value="close">
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_action_<bs:formelement name="selected_action.path"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_action('<bs:formelement name="selected_action.path"/>','',false)">					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement name="selected_action.path"/>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('action','<bs:formelement name="selected_action.path"/>','','')"/>
					</td>
				</tr>
			</table>
	</bs:equal>
	<bs:equal name="type_selected" value="open">
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_action_<bs:formelement name="selected_action.path"/>" src="images/menu/16_open.gif"/ border="0" style="cursor:pointer"
						onclick="draw_action('<bs:formelement name="selected_action.path"/>','',false)">					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<font color="blue"><bs:formelement name="selected_action.path"/></font>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('action','<bs:formelement name="selected_action.path"/>','','')"/>
					</td>
				</tr>
			</table>
	
<jsp:include page="/jsp/included/panel_top.jsp" />			
						<table width="400"   cellspacing="0">
							<tr>
								<td width="0%" class="page_section">path:</td>
								<td width="100%" id="action|<bs:formelement name="selected_action.path"/>|||path"><bs:input name="selected_action.path" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'path')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">type:</td>
								<td width="100%" id="action|<bs:formelement name="selected_action.path"/>|||type"><bs:input name="selected_action.type" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'type')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">name:</td>
								<td width="100%" id="action|<bs:formelement name="selected_action.path"/>|||name">
									<bs:select name="selected_action.name" style="border: solid 1px silver; width: 100%" onchange="updateValue(this,'name')" onfocus="makeFocus(this)">									
										<option value="*" <bs:equal name="selected_apply_to_action.action" value="*"> SELECTED </bs:equal>>*</option>
										<bs:options property="l_actions.v_info_beans" value="name"  label="name" />
									</bs:select>
								</td>
							</tr>
							<tr>
								<td width="0%" class="page_section">redirect:</td>
								<td width="100%" id="action|<bs:formelement name="selected_action.path"/>|||redirect"><bs:input name="selected_action.redirect" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'redirect')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">error:</td>
								<td width="100%" id="action|<bs:formelement name="selected_action.path"/>|||error"><bs:input name="selected_action.error" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'error')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">memoryInSession:</td>
								<td width="100%" id="action|<bs:formelement name="selected_action.path"/>|||memoryInSession">
									<bs:select name="selected_action.memoryInSession" style="border: solid 1px silver; width: 100%" onchange="updateValue(this,'memoryInSession')" onfocus="makeFocus(this)">									
										<bs:options property="elements_true_false" value="code"  label="desc" />
									</bs:select>
								</td>
							</tr>
							<tr>
								<td width="0%" class="page_section">memoryAsLastInstance:</td>
								<td width="100%" id="action|<bs:formelement name="selected_action.path"/>|||memoryAsLastInstance">
									<bs:select name="selected_action.memoryAsLastInstance" style="border: solid 1px silver; width: 100%" onchange="updateValue(this,'memoryAsLastInstance')" onfocus="makeFocus(this)">									
										<bs:options property="elements_true_false" value="code"  label="desc" />
									</bs:select>
								</td>
							</tr>
							
							<tr>
								<td width="0%" class="page_section">reloadAfterAction:</td>
								<td width="100%" id="action|<bs:formelement name="selected_action.path"/>|||reloadAfterAction">
									<bs:select name="selected_action.reloadAfterAction" style="border: solid 1px silver; width: 100%" onchange="updateValue(this,'reloadAfterAction')" onfocus="makeFocus(this)">									
										<bs:options property="elements_true_false" value="code"  label="desc" />
									</bs:select>
								</td>
							</tr>
							<tr>
								<td width="0%" class="page_section">navigated:</td>
								<td width="100%" id="action|<bs:formelement name="selected_action.path"/>|||navigated">
									<bs:select name="selected_action.navigated" style="border: solid 1px silver; width: 100%" onchange="updateValue(this,'navigated')" onfocus="makeFocus(this)">									
										<bs:options property="elements_true_false" value="code"  label="desc" />
									</bs:select>
								</td>
							</tr>
							<tr>
								<td width="0%" class="page_section">syncro:</td>
								<td width="100%" id="action|<bs:formelement name="selected_action.path"/>|||syncro">
									<bs:select name="selected_action.syncro" style="border: solid 1px silver; width: 100%" onchange="updateValue(this,'syncro')" onfocus="makeFocus(this)">									
										<bs:options property="elements_true_false" value="code"  label="desc" />
									</bs:select>
								</td>
							</tr>
							<tr>
								<td width="0%" class="page_section">help:</td>
								<td width="100%" id="action|<bs:formelement name="selected_action.path"/>|||help"><bs:input name="selected_action.help" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'help')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">provider:</td>
								<td width="100%" id="action|<bs:formelement name="selected_action.path"/>|||provider"><bs:input name="selected_action.provider" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'provider')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">property:</td>
								<td width="100%" id="action|<bs:formelement name="selected_action.path"/>|||property"><bs:input name="selected_action.property" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'property')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td width="0%" class="page_section">comment:</td>
								<td width="100%" id="action|<bs:formelement name="selected_action.path"/>|||comment"><bs:input name="selected_action.comment" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'comment')" onfocus="makeFocus(this)"/></td>
							</tr>
							
						</table>
						
						<table width="100%" style="border: dashed 1px silver" cellspacing="0">
						<tbody id="redirects_<bs:formelement name="selected_action.path"/>">
						<tr>
							<td colspan="2">
									<table width="100%" cellspacing="0" cellpadding="0" border="0">
										<tr>
											<td  height="20" width="50%" valign="middle">&nbsp;</td>
											<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
											<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section">redirect</span></nobr></td>
											<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
											<td  height="20" width="50%"><img src="images/decor/20_add.gif" border="0" style="cursor:pointer" onclick="addEntity('redirect','<bs:formelement name="selected_action.path"/>','')"></td>
										</tr>
									</table>
								</td>
	
						
						</tr> 
	
	<bs:sequence name="selected_action.v_info_redirects">				
				<bs:bean name="current_redirect" property="selected_action.v_info_redirects" index="sequence"/>	
<bs:equal bean="current_redirect" name="system" value="false">				
						<tr >
							<td id="redirect_<bs:formelement name="selected_action.path"/>_<bs:formelement bean="current_redirect" name="path"/>">
								<table width="400" cellspacing="0">
									<tr>
										<td width="0%" background="images/corners/panel_t.gif" valign="middle">
											<img id="img_redirect_<bs:formelement name="selected_action.path"/>_<bs:formelement bean="current_redirect" name="path"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
											onclick="draw_redirect('<bs:formelement name="selected_action.path"/>','<bs:formelement bean="current_redirect" name="path"/>',false)">
										</td>
										<td width="100%" background="images/corners/panel_t.gif" class="page_section">
											<bs:formelement bean="current_redirect" name="path"/>&nbsp;&nbsp;
										</td>	
										<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
											<img src="images/menu/close_section.gif" style="cursor:pointer"
											onclick="removeEntity('redirect','<bs:formelement name="selected_action.path"/>','<bs:formelement bean="current_redirect" name="path"/>','')"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
</bs:equal>	
	</bs:sequence>		
						</tbody>
						</table>
	
	
						<table width="100%" style="border: dashed 1px silver" cellspacing="0">
						<tbody id="transformationoutputs_<bs:formelement name="selected_action.path"/>">
						<tr>
							<td colspan="2">
									<table width="100%" cellspacing="0" cellpadding="0" border="0">
										<tr>
											<td  height="20" width="50%" valign="middle">&nbsp;</td>
											<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
											<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section">action-transformationoutput</span></nobr></td>
											<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
											<td  height="20" width="50%"><img src="images/decor/20_add.gif" border="0" style="cursor:pointer" onclick="addEntity('transformationoutput','<bs:formelement name="selected_action.path"/>','')"></td>
										</tr>
									</table>
								</td>
	
						
						</tr> 
	
	<bs:sequence name="selected_action.v_info_transformationoutput">				
				<bs:bean name="current_transformationoutput" property="selected_action.v_info_transformationoutput" index="sequence"/>	
						<tr >
							<td id="transformationoutput_<bs:formelement name="selected_action.path"/>_<bs:formelement bean="current_transformationoutput" name="name"/>">
								<table width="400" cellspacing="0">
									<tr>
										<td width="0%" background="images/corners/panel_t.gif" valign="middle">
											<img id="img_transformationoutput_<bs:formelement name="selected_action.path"/>_<bs:formelement bean="current_transformationoutput" name="name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
											onclick="draw_transformationoutput('<bs:formelement name="selected_action.path"/>','<bs:formelement bean="current_transformationoutput" name="name"/>',false)">
										</td>
										<td width="100%" background="images/corners/panel_t.gif" class="page_section">
											<bs:formelement bean="current_transformationoutput" name="name"/>&nbsp;&nbsp;
										</td>	
										<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
											<img src="images/menu/close_section.gif" style="cursor:pointer"
											onclick="removeEntity('transformationoutput','<bs:formelement name="selected_action.path"/>','<bs:formelement bean="current_transformationoutput" name="name"/>','')"/>
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
