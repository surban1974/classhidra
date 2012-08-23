<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<bs:equal name="middleAction" value="view_redirect">
	<bs:equal name="type_selected" value="close">
			<table width="400" cellspacing="0"
<bs:equal name="selected_redirect.enabled" value="-1">
	style="border: dashed 1px #EAEAEA"
	bgcolor="#EAEAEA"
</bs:equal>	
<bs:equal name="selected_redirect.enabled" value="0">
	style="border: dashed 1px #FFE0AA"
	bgcolor="#FFE0AA"
</bs:equal>			
<bs:equal name="selected_redirect.enabled" value="1">
	style="border: dashed 1px #C1FFC8"
	bgcolor="#C1FFC8"
</bs:equal>					
			>
				<tr>
					<td width="10" valign="middle">&nbsp;&nbsp;</td>
					<td width="0%" valign="middle">
						<img id="img_redirect_<bs:formelement name="selected_action.path"/>_<bs:formelement name="selected_redirect.path"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_redirect('<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>',false)">
					</td>
					<td width="100%" class="page_section" style="cursor:pointer"
						onclick="enableEntity('redirect','<bs:formelement name="selected_relation.name"/>','<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>','')"
					>
						<font
<bs:equal name="selected_redirect.enabled" value="-1">
	color="gray"
</bs:equal>	
<bs:equal name="selected_redirect.enabled" value="0">
	color="red"
</bs:equal>			
<bs:equal name="selected_redirect.enabled" value="1">
	color="green"
</bs:equal>							
						>					
						<bs:formelement name="selected_redirect.path"/>
						</font>
					</td>	
				</tr>
			</table>
	</bs:equal>
	<bs:equal name="type_selected" value="open">
			<table width="400" cellspacing="0"
<bs:equal name="selected_redirect.enabled" value="-1">
	style="border: dashed 1px #EAEAEA"
	bgcolor="#EAEAEA"
</bs:equal>	
<bs:equal name="selected_redirect.enabled" value="0">
	style="border: dashed 1px #FFE0AA"
	bgcolor="#FFE0AA"
</bs:equal>			
<bs:equal name="selected_redirect.enabled" value="1">
	style="border: dashed 1px #C1FFC8"
	bgcolor="#C1FFC8"
</bs:equal>				
			>
				<tr>
					<td width="10" valign="middle">&nbsp;&nbsp;</td>
					<td width="0%" valign="middle">
						<img id="img_redirect_<bs:formelement name="selected_action.path"/>_<bs:formelement name="selected_redirect.path"/>" src="images/menu/16_open.gif"/ border="0" style="cursor:pointer"
						onclick="draw_redirect('<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>',false)">
					</td>
					<td width="100%" class="page_section"  style="cursor:pointer"
						onclick="enableEntity('redirect','<bs:formelement name="selected_relation.name"/>','<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>','')"
					>
						<font
<bs:equal name="selected_redirect.enabled" value="-1">
	color="gray"
</bs:equal>	
<bs:equal name="selected_redirect.enabled" value="0">
	color="red"
</bs:equal>			
<bs:equal name="selected_redirect.enabled" value="1">
	color="green"
</bs:equal>							
						>					
						<bs:formelement name="selected_redirect.path"/>
						</font>
					</td>	
				</tr>
			</table>

						
						<table width="400" cellspacing="0">
						<tbody id="sections_<bs:formelement name="selected_action.path"/>_<bs:formelement name="selected_redirect.path"/>">
	
	
	<bs:sequence name="selected_redirect.v_info_sections">				
				<bs:bean name="current_section" property="selected_redirect.v_info_sections" index="sequence"/>	
						<tr >
							<td width="20" valign="middle">&nbsp;&nbsp;&nbsp;&nbsp;</td>
							<td id="section_<bs:formelement name="selected_action.path"/>_<bs:formelement name="selected_redirect.path"/>_<bs:formelement bean="current_section" name="name"/>" style="border: dashed 1px gray"
<bs:equal bean="current_section" name="enabled" value="-1">
	bgcolor="#EAEAEA"
</bs:equal>	
<bs:equal bean="current_section" name="enabled" value="0">
	bgcolor="#FFE0AA"
</bs:equal>			
<bs:equal bean="current_section" name="enabled" value="1">
	bgcolor="#C1FFC8"
</bs:equal>				
							> 
								<table width="400" cellspacing="0">
									<tr>
										<td width="0%" valign="middle"><img src="images/menu/16_area.gif"/>
										</td>
										<td width="100%" class="page_section" style="cursor:pointer"
											onclick="enableEntity('section','<bs:formelement name="selected_relation.name"/>','<bs:formelement name="selected_action.path"/>','<bs:formelement name="selected_redirect.path"/>','<bs:formelement bean="current_section" name="name"/>')"
										>
											<font
<bs:equal bean="current_section" name="enabled" value="-1">
	color="gray"
</bs:equal>	
<bs:equal bean="current_section" name="enabled" value="0">
	color="red"
</bs:equal>			
<bs:equal bean="current_section" name="enabled" value="1">
	color="green"
</bs:equal>							
											>										
											<bs:formelement bean="current_section" name="name"/>
											</font>
										</td>	

									</tr>
								</table>
							</td>
						</tr>
	
	</bs:sequence>		
						</tbody>
						</table>													
					
	</bs:equal>						

</bs:equal>