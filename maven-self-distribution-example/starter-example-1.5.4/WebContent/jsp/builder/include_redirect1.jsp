<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<bs:equal name="middleAction" value="view_redirect1">
	<bs:equal name="type_selected" value="close">
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_redirect_<bs:formelement name="selected_redirect.path"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_redirect1('<bs:formelement name="selected_redirect.path"/>','',false)">					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement name="selected_redirect.path"/>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('redirect1','<bs:formelement name="selected_redirect.path"/>','','')"/>
					</td>
				</tr>
			</table>
	</bs:equal>
	<bs:equal name="type_selected" value="open">
			<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_redirect_<bs:formelement name="selected_redirect.path"/>" src="images/menu/16_open.gif"/ border="0" style="cursor:pointer"
						onclick="draw_redirect1('<bs:formelement name="selected_redirect.path"/>','',false)">
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<font color="blue"><bs:formelement name="selected_redirect.path"/></font>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('redirect1','<bs:formelement name="selected_redirect.path"/>','','')"/>
					</td>
				</tr>
			</table>
<jsp:include page="/jsp/included/panel_top.jsp" />
						<table width="400" cellspacing="0">
							<tr>
								<td>&nbsp;&nbsp;</td>
								<td width="0%" class="page_section">path:</td>
								<td width="100%" id="redirect1|<bs:formelement name="selected_redirect.path"/>|||path">
									<bs:select name="selected_redirect.path" style="border: solid 1px silver; width: 100%" onchange="updateValue(this,'path')" onfocus="makeFocus(this)">									
										<bs:options property="elements_all_redirects" value="path"  label="path" />
									</bs:select>
								</td>
							</tr>
							<tr>
								<td>&nbsp;&nbsp;</td>
								<td width="0%" class="page_section">descr:</td>
								<td width="100%" id="redirect1|<bs:formelement name="selected_redirect.path"/>|||descr"><bs:input name="selected_redirect.descr" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'descr')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td>&nbsp;&nbsp;</td>
								<td width="0%" class="page_section">mess_id:</td>
								<td width="100%" id="redirect1|<bs:formelement name="selected_redirect.path"/>|||mess_id"><bs:input name="selected_redirect.mess_id" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'mess_id')" onfocus="makeFocus(this)"/></td>
							</tr>
							<tr>
								<td>&nbsp;&nbsp;</td>
								<td width="0%" class="page_section">comment:</td>
								<td width="100%" id="redirect1|<bs:formelement name="selected_redirect.path"/>|||comment"><bs:input name="selected_redirect.comment" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'comment')" onfocus="makeFocus(this)"/></td>
							</tr>
						</table>
<jsp:include page="/jsp/included/panel_bottom.jsp" />						
	</bs:equal>						

</bs:equal>