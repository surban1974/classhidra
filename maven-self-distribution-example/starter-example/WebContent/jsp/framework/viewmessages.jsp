<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<bs:equal name="middleAction" value="control">
<bs:notEqual name="view_list.size" value="0" >
	showAsPopup("messages?middleAction=view",500,75);
</bs:notEqual>
</bs:equal>
<bs:equal name="middleAction" value="view">


<bs:notEqual name="view_list.size" value="0" >
<table width="500" bgcolor="white">
<bs:sequence name="view_list">
<bs:bean name="current_mess" property="view_list" index="sequence"/>
	<tr>
	<td >
		<table width="100%" >
		<tr>
		
<bs:equal bean="current_mess" name="TYPE" value="E">
		<td width="1%" >
			<img src="images/decor/64_mess_error.gif" border="0">	
		</td>
		<td><font color="#B03C3C"><b><bs:formelement bean="current_mess" name="DESC_MESS"/></b></font></td>		
</bs:equal>
<bs:equal bean="current_mess" name="TYPE" value="I">		
		<td width="1%" >
			<img src="images/decor/64_mess_ok.gif" border="0">	
		</td>
		<td><font color="#54AA4C"><b><bs:formelement bean="current_mess" name="DESC_MESS"/></b></font></td>		
</bs:equal>
<bs:equal bean="current_mess" name="TYPE" value="W">		
		<td width="1%" >
			<img src="images/decor/64_mess_warning.gif" border="0">	
		</td>
		<td><font color="#DCC434"><b><bs:formelement bean="current_mess" name="DESC_MESS"/></b></font></td>		
</bs:equal>		
		</tr>
		</table>
	</td>
	</tr>
</bs:sequence>

		
</table>		

</bs:notEqual>
</bs:equal>

