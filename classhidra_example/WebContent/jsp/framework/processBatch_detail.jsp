<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 

<div id="" style="width:100%;height: 100%; margin-bottom: -67px;">


<center>				
<bs:form name="formCurrencyDetail" onhistory="">

	<table >
		<tr>
			<td style="width: 580px;">
				<table class="tableBodyTabb"  >

					<tr >
						<td class="colTab" style="width:250px"><label class="label">Id:</label></td>
						<td class="signOn"><bs:htmlout name="selected.cd_btch" />&nbsp;</td>
					</tr>
				
					<tr >
						<td class="colTab" style="width:250px"><label class="label">Parent:</label></td>
						<td><bs:input  name="selected.cd_p_btch" styleClass="signOn" toUpperCase="no"  /></td>
					</tr>
					<tr >
						<td class="colTab" style="width:250px">Description:</label></td>
						<td ><bs:input  name="selected.dsc_btch" styleClass="signOn" toUpperCase="no" style="background-color:#FFC8C6;" /></td>
					</tr>
					<tr >
						<td class="colTab" style="width:250px">Order:</label></td>
						<td ><bs:input  name="selected.ord" styleClass="signOn" toUpperCase="no" style="background-color:#FFC8C6;"/></td>
					</tr>					
					<tr >
						<td class="colTab" style="width:250px">Period:</label></td>
						<td ><bs:input  name="selected.period" styleClass="signOn" toUpperCase="no" style="background-color:#FFC8C6;"/></td>
					</tr>									
				</table>
			</td>	
		</tr>
	</table>
	</bs:form>
</center>	
</div>	

<div style="width:100%;height: 38px;">
<center>
<br>
	<table>		
		<tr >
			<td style="height: 40px; " valign="middle" align="center">
				<table style="height: 100%">
					<tr>
						<td align="center" width="50px" id="action_remove" class="buttonAsJs" 
							onclick="try{closePanel('panel_Popup');}catch(e){};document.getElementById('content_Panel_Popup').style.display='none';">
							<img src="images/menu/exit.gif"/>
						</td>
						<td>&nbsp;</td>	
	
<!-- 
						<td align="center" width="50px" id="action_remove" class="buttonAsJs" 
							onclick="this.style.display='none';document.getElementById('action_remove_ok').style.display='block';">
							<img src="images/menu/remove.gif"/>
						</td>
						<td align="center" valign="middle" width="50px" id="action_remove_ok" class="buttonAsJs" style="border-color: red; display: none;"
							onclick="runSubmit('detail_remove','formCurrencyDetail')">
							<nobr>&nbsp;<bs:message code="label_action_confirm" defaultValue="Confirm"/>&nbsp;</nobr>
						</td>						
 -->	
						<td>&nbsp;</td>				
						<td align="center" width="50px" id="action_update" class="buttonAsJs" 
							onclick="runSubmit('detail_update','formCurrencyDetail')">
							<img src="images/menu/ok.gif"/>
						</td>
				

					</tr>
				</table>
			</td>
		</tr>
		
	</table>	
</center>	
</div>

				