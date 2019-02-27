<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<bs:equal name="middleAction" value="view_authentication">
<bs:equal name="type_selected" value="open">

<jsp:include page="/jsp/included/panel_top.jsp" />			
		<table cellspacing="0" > 		
		<tbody id="action-mappings" >
		<bs:sequence name="l_actions.v_info_actions" >				
			<bs:bean name="current_action" property="l_actions.v_info_actions" index="sequence"/>
			<tr>
			<td id="action_<bs:formelement name="selected_relation.name"/>_<bs:formelement bean="current_action" name="path"/>"  valign="top"> 
				<table width="400" cellspacing="0" cellpadding="0" 
<bs:equal bean="current_action" name="enabled" value="-1">
	style="border: dashed 1px #EAEAEA"
	bgcolor="#EAEAEA"
</bs:equal>	
<bs:equal bean="current_action" name="enabled" value="0">
	style="border: dashed 1px #FFE0AA"
	bgcolor="#FFE0AA"
</bs:equal>			
<bs:equal bean="current_action" name="enabled" value="1">
	style="border: dashed 1px #C1FFC8"
	bgcolor="#C1FFC8"
</bs:equal>							
				>
				<tr>
					<td width="0%"  valign="middle">
						<img id="img_action_<bs:formelement name="selected_relation.name"/>_<bs:formelement bean="current_action" name="path"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_action('<bs:formelement name="selected_relation.name"/>','<bs:formelement bean="current_action" name="path"/>',false)">
					</td>
	
					<td width="100%"  class="page_section" style="cursor:pointer"
						onclick="enableEntity('action','<bs:formelement name="selected_relation.name"/>','<bs:formelement bean="current_action" name="path"/>','','')"
					>
						<font
<bs:equal bean="current_action" name="enabled" value="-1">
	color="gray"
</bs:equal>	
<bs:equal bean="current_action" name="enabled" value="0">
	color="red"
</bs:equal>			
<bs:equal bean="current_action" name="enabled" value="1">
	color="green"
</bs:equal>							
						>
						<bs:formelement bean="current_action" name="path"/>
						</font>
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
