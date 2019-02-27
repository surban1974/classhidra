<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<%@ include file="../framework/header_ajax.jsp" %> 

<bs:form onhistory="" name="formProcessBatch_Popup">
<div id="div_LogBatch" 
				class="divLists0" 
				style="vertical-align:top;width:100%;height:90%;overflow:auto;">
<table width="100%">
	
	<tr>	 
		<td class="colTab_center" align="center" width="100"><bs:formelement name="selected.cd_btch"/></td>
		<td class="colTab_center" align="center"><bs:formelement name="selected.dsc_btch" /></td>
	</tr>


<bs:sequence name="elementslog">				
	<bs:bean name="current" property="elementslog" index="sequence"/>


<tr >
<td class="colTab1White" ><nobr><bs:formelement bean="current" name="tm_start" formatOutput="dd/MM/yyyy HH:mm:sss"/></nobr></td>
<td class="colTab1White" rowspan="2" width="100%"><font 
<bs:equal bean="current" name="st_exec" value="0">  color="green" </bs:equal>	
<bs:equal bean="current" name="st_exec" value="1">  color="yellow" </bs:equal>
<bs:equal bean="current" name="st_exec" value="2">  color="red" </bs:equal>	

><bs:formelement bean="current" name="dsc_exec_br" /></td>
</tr>	

<tr >
<td class="colTab1White" width="100"><nobr><bs:formelement bean="current" name="tm_fin" formatOutput="dd/MM/yyyy HH:mm:sss"/></nobr></td>
</tr>

</bs:sequence>
</table>
</div>

<table width="100%">

	<tr>	
		<td width="100%">&nbsp;</td>
		<td id="action_clearLog"></td>  
		
	</tr>
</table>
</bs:form>