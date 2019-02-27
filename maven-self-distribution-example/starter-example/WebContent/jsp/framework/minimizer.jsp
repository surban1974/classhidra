<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<table cellpadding="0" cellspacing="0">
<tr>

<bs:sequence name="keys">				
	<bs:bean name="current" property="keys" index="sequence"/>
	<td>
<table cellpadding="0" cellspacing="0" style="border: solid 1px silver" >
<tr>
	<td align="center"
		valign="middle"
		id="button_s_c_500<bs:formelement bean="bs:sequence" name="index"/>"
		nowrap="nowrap" 
		height="18"
		background="images/menu/button_center_n.gif"
		style="cursor:pointer"
		onMouseOver="
			window.status='<bs:formelement bean="current" name="desc"/>';
			menuMouseOver1(500<bs:formelement bean="bs:sequence" name="index"/>,'button','images/menu/');
			return true;" 
		onMouseOut="
			window.status='';
			menuMouseOut1(500<bs:formelement bean="bs:sequence" name="index"/>,'button','images/menu/');
			return true;"
		 onMouseDown="
			window.status='';
		 	return true;"
		 onMouseUp="
			 window.status='';
			 return true;"
		 onClick="goAction('minimizer?middleAction=open&source=<bs:formelement bean="current" name="code"/>')"
		>		
		
		
		<span class="page_section"  
			  id="button_s_c_span_500<bs:formelement bean="bs:sequence" name="index"/>" >
			
			<img src="images/menu/button_left_n.gif" border="0" >
			<bs:formelement bean="current" name="desc"/>
			<img src="images/menu/button_right_n.gif" border="0" >
		</span>
	</td>
	<td align="center"
		valign="middle"
		id="button_s_c_510<bs:formelement bean="bs:sequence" name="index"/>"
		nowrap="nowrap" 
		height="18"
		width="20"
		background="images/menu/button_center_n.gif"
		style="cursor:pointer"
		onMouseOver="
			window.status='open';
			menuMouseOver1(510<bs:formelement bean="bs:sequence" name="index"/>,'button','images/menu/');
			return true;" 
		onMouseOut="
			window.status='';
			menuMouseOut1(510<bs:formelement bean="bs:sequence" name="index"/>,'button','images/menu/');
			return true;"
		 onMouseDown="
			window.status='';
		 	return true;"
		 onMouseUp="
			 window.status='';
			 return true;"
			 onClick="goAction('minimizer?middleAction=open&source=<bs:formelement bean="current" name="code"/>')"
		>		
		
		<nobr>
		<span class="page_section"  
			  id="button_s_c_span_510<bs:formelement bean="bs:sequence" name="index"/>">

				<img src="images/menu/maximize.gif" border="0" >

		</span>
		</nobr>
	</td>
	<td align="center"
		valign="middle"
		id="button_s_c_520<bs:formelement bean="bs:sequence" name="index"/>"
		nowrap="nowrap" 
		height="18"
		width="20"
		background="images/menu/button_center_n.gif"
		style="cursor:pointer"
		onMouseOver="
			window.status='delete';
			menuMouseOver1(520<bs:formelement bean="bs:sequence" name="index"/>,'button','images/menu/');
			return true;" 
		onMouseOut="
			window.status='';
			menuMouseOut1(520<bs:formelement bean="bs:sequence" name="index"/>,'button','images/menu/');
			return true;"
		 onMouseDown="
			window.status='';
		 	return true;"
		 onMouseUp="
			 window.status='';
			 return true;"
		 onClick="ajax_makeRequest('minimizer?middleAction=delete&source=<bs:formelement bean="current" name="code"/>','footer_fixedbox')"
		>		
		
		<nobr>
		<span class="page_section"  
			  id="button_s_c_span_520<bs:formelement bean="bs:sequence" name="index"/>">

				<img src="images/menu/Delete.gif" border="0" >

		</span>
		</nobr>
	</td>

	
</tr>
</table>
	</td>
	<td style="width: 5px"></td>
	
</bs:sequence>
</table>	