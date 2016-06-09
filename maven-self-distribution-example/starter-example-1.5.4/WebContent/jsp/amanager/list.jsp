<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<div id="div_list" 
				class="divLists0" 
				style="vertical-align:top;width:400px;height:270px;">

<bs:equal name="middleAction" value="list_groupsIntoUser">

<table cellpadding="2" cellspacing="2" class="tableBodyTab">

		<bs:sequence name="l_users.v_info_groups" >				
			<bs:bean name="current_group" property="l_users.v_info_groups" index="sequence"/>

<tr style="cursor:pointer"  onmouseover="this.className='rowTabOver'" onmouseout="this.className='colTab1white'" 
	onclick="addEntity('groupIntoUser','<bs:formelement name="id_selected_user"/>','<bs:formelement bean="current_group" name="name"/>');
	">

<td align="left" class="colTab1white" width="400">&nbsp;<bs:formelement bean="current_group" name="name"/></td>

</tr>	
</bs:sequence>
</table>
</bs:equal>

<bs:equal name="middleAction" value="list_groupsIntoRelation">

<table cellpadding="2" cellspacing="2" class="tableBodyTab">

		<bs:sequence name="l_users.v_info_groups" >				
			<bs:bean name="current_group" property="l_users.v_info_groups" index="sequence"/>

<tr style="cursor:pointer"  onmouseover="this.className='rowTabOver'" onmouseout="this.className='colTab1white'" 
	onclick="addEntity('groupIntoRelation','<bs:formelement name="id_selected_relation"/>','<bs:formelement bean="current_group" name="name"/>');
	">

<td align="left" class="colTab1white" width="400">&nbsp;<bs:formelement bean="current_group" name="name"/></td>

</tr>	
</bs:sequence>
</table>
</bs:equal>

<bs:equal name="middleAction" value="list_usersIntoGroup">

<table cellpadding="2" cellspacing="2" class="tableBodyTab">

		<bs:sequence name="l_users.v_info_users" >				
			<bs:bean name="current_user" property="l_users.v_info_users" index="sequence"/>

<tr style="cursor:pointer"  onmouseover="this.className='rowTabOver'" onmouseout="this.className='colTab1white'" 
	onclick="addEntity('userIntoGroup','<bs:formelement name="id_selected_group"/>','<bs:formelement bean="current_user" name="name"/>');
	">

<td align="left" class="colTab1white" width="400">&nbsp;<bs:formelement bean="current_user" name="name"/></td>

</tr>	
</bs:sequence>
</table>
</bs:equal>


<bs:equal name="middleAction" value="list_usersIntoTarget">

<table cellpadding="2" cellspacing="2" class="tableBodyTab">

		<bs:sequence name="l_users.v_info_users" >				
			<bs:bean name="current_user" property="l_users.v_info_users" index="sequence"/>

<tr style="cursor:pointer"  onmouseover="this.className='rowTabOver'" onmouseout="this.className='colTab1white'" 
	onclick="addEntity('userIntoTarget','<bs:formelement name="id_selected_target"/>','<bs:formelement bean="current_user" name="name"/>');
	">

<td align="left" class="colTab1white" width="400">&nbsp;<bs:formelement bean="current_user" name="name"/></td>

</tr>	
</bs:sequence>
</table>
</bs:equal>

<bs:equal name="middleAction" value="list_targetsIntoRelation">

<table cellpadding="2" cellspacing="2" class="tableBodyTab">

		<bs:sequence name="l_users.v_info_targets" >				
			<bs:bean name="current_target" property="l_users.v_info_targets" index="sequence"/>

<tr style="cursor:pointer"  onmouseover="this.className='rowTabOver'" onmouseout="this.className='colTab1white'" 
	onclick="addEntity('targetIntoRelation','<bs:formelement name="id_selected_relation"/>','<bs:formelement bean="current_target" name="name"/>');
	">

<td align="left" class="colTab1white" width="400">&nbsp;<bs:formelement bean="current_target" name="name"/></td>

</tr>	
</bs:sequence>
</table>
</bs:equal>

<bs:equal name="middleAction" value="list_targetsIntoUser">

<table cellpadding="2" cellspacing="2" class="tableBodyTab">

		<bs:sequence name="l_users.v_info_targets" >				
			<bs:bean name="current_target" property="l_users.v_info_targets" index="sequence"/>

<tr style="cursor:pointer"  onmouseover="this.className='rowTabOver'" onmouseout="this.className='colTab1white'" 
	onclick="addEntity('targetIntoUser','<bs:formelement name="id_selected_user"/>','<bs:formelement bean="current_target" name="name"/>');
	">

<td align="left" class="colTab1white" width="400">&nbsp;<bs:formelement bean="current_target" name="name"/></td>

</tr>	
</bs:sequence>
</table>
</bs:equal>

</div>