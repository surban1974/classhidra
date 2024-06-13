<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<%@ include file="../framework/header.jsp" %> 

<script language="javascript" src="javascript2012/application/login.js"></script>

<body >

<div id="content_Panel_canvas" 
style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; overflow:hidden;"
>
<table border="0" width="100%" height="100%" cellpadding="0" cellspacing="0">
<tr>
<td align="center" bgcolor="">

<jsp:include page="/jsp/included/panel_top.jsp" >
	<jsp:param name="panel-id" value="panel_About" />	
	<jsp:param name="panel-onclose" value="window.close();" />
</jsp:include>	

<div id="page" style=" width: 900; height: 530; background-color: white">
<%@ include file="../included/content_page_header.jsp" %>

<center>
	<bs:form method="post" onhistory="">
		<bs:input type="hidden" name="cd_btch"/>
		<bs:input type="hidden" name="cd_p_btch"/>

<table border="0" width="100%" height="450" >
<tr>
<td style="border-bottom-style: solid 1px;" valign="top">	

	<table>
	<tr>		
<bs:equal name="next_scan">	
		<td ><img src="images/menu/28_stop_bw.gif" style="cursor:pointer" onclick="runSubmit('stop')"/></td>

		<td class="page_section">&nbsp;&nbsp;Next scan: </td>
		<td class="colTab1White" style="background:white"><bs:formelement name="next_scan" formatOutput="dd/MM/yyyy HH:mm:sss"/></td>
		<td class="page_section">&nbsp;&nbsp;Period scan(min):</td>
		<td class="colTab1White" style="background:white"><bs:formelement name="period_scan"/></td>
		<td class="page_section">&nbsp;&nbsp;Difference:</td>
		<td class="colTab1White" style="background:white"><bs:formelement name="res_scan"/></td>
		<td>&nbsp;</td>
		<td ><img src="images/menu/28_reload_bw.gif" style="cursor:pointer" onclick="runSubmit('reInit')"/></td>		
</bs:equal>
<bs:notEqual name="next_scan">	
		<td ><img src="images/menu/28_start_bw.gif" style="cursor:pointer" onclick="runSubmit('reInit')"/></td>
</bs:notEqual>
	</tr>
	</table>
	
	<table width="100%">
		<tr>
			<td  class="labelLeft">Batch:</td>
		</tr>	
	</table>
 <!-- 
		<bs:bean name="curr" constructor="java.lang.String">
			<bs:parameter value="AZN"></bs:parameter>  
		</bs:bean>
		
		<bs:bean name="dec" constructor="java.lang.Double">
			<bs:parameter value="-100.12"></bs:parameter> 
		</bs:bean>
		
		<bs:formelement bean="dec"  formatOutput_="0,000.00" formatLanguage="ua" formatCountry="UA" formatCurrency="{curr}" normalASCII="true"/>
	
	
	
	 										<bs:list
											addHiddenInput="false" 
											name="group" 
											list="elements"
											key_values="code"
											propertys="cd_btch;dsc_btch;ord;period;" 
											formatsOutput=";;0,000.00;;"
											formatsCurrency=";;{curr};;"
											
											tr_styleClass="rowSignOn"	
											td_style="font-size: 14px; height:24px"											

											styleClass="divLists0"	
											tb_width="100%"
											width="300"
											
										/>
	
 -->		
	<table width="100%">
		
		<tr>
			<td  style="cursor:pointer" width="0%"><img src="images/menu/16_open.gif" 
				onclick="	document.getElementById('cd_p_btch').value='<bs:formelement name="parent.cd_p_btch"/>';
							runSubmit('child')"
			/></td>
	
			<td  class="colTab1White" colspan="10" width="100%">
			<bs:formelement  name="parent.cd_btch"/>&nbsp;
			</td>
		</tr>
		
		<tr>	
			<td  align="center" width="0%"></td>			
			<td  align="center" width="0%"></td>		
			<td class="colTab_center" align="center" width="1%">Order</td>
			<td class="colTab_center" align="center" width="100">Id</td>
			<td class="colTab_center" align="center">Description</td>
			<td class="colTab_center" align="center" width="100">Period</td>
			<td class="colTab_center" align="center" width="100">Last Exec. Time</td>
			<td class="colTab_center" align="center" width="100">Next Exec. Time</td>
			
			<td  align="center" width="0%">&nbsp;</td>
			<td  align="center" width="0%">&nbsp;</td>
			<td  align="center" width="0%">&nbsp;</td>
	
			
		</tr>

 
 
	
		<bs:sequence name="elements">
		<bs:bean name="current_batch" property="elements" index="sequence"/> 
		<tr >
			<td  align="center" width="0%"></td>
			<td  style="cursor:pointer" width="0%"><img src="images/menu/16_close.gif" 
				onclick="	document.getElementById('cd_p_btch').value='<bs:formelement bean="current_batch" name="cd_btch"/>';
							runSubmit('child')"
			/></td>
		
		
			<td class="colTab1White" width="1%">
				<bs:formelement bean="current_batch" name="ord"/> 
			</td>
		
			<td class="colTab1White" width="70">
				<bs:formelement bean="current_batch" name="cd_btch"/>
			</td>
			<td class="colTab1White" >
				<bs:formelement bean="current_batch" name="dsc_btch"/>
			</td>
			<td class="colTab1White" >
				<bs:formelement bean="current_batch" name="period"/>
			</td>
			
			<td id="tm_last_<bs:formelement bean="current_batch" name="cd_btch"/>" class="colTab1White" width="100">
				<bs:formelement bean="current_batch" name="tm_last" formatOutput="dd/MM/yyyy HH:mm"/>
			</td>
			<td id="tm_next_<bs:formelement bean="current_batch" name="cd_btch"/>" class="colTab1White" width="100">
				<bs:formelement bean="current_batch" name="tm_next" formatOutput="dd/MM/yyyy HH:mm"/>
			</td>
		
			<td  width="0%" align="center" valign="middle" style="cursor:pointer">
			
			<img id="img_operation_<bs:formelement bean="current_batch" name="cd_btch"/>"
	<bs:equal bean="current_batch" name="state" value="0"> src="images/decor/24_start_g.gif" </bs:equal>	
	<bs:equal bean="current_batch" name="state" value="-1"> src="images/decor/24_start_y.gif" </bs:equal>	
	<bs:equal bean="current_batch" name="state" value="1"> src="images/decor/24_stop_r.gif" </bs:equal>			
			src="images/decor/24_start_g.gif" alt="run"
				onclick="	document.getElementById('cd_btch').value='<bs:formelement bean="current_batch" name="cd_btch"/>';
							runSubmit('exec_batch')"
			/>
	
			</td>
			<td  style="cursor:pointer" width="0%">
				<img src="images/decor/24_log_batch.gif" alt="log"
				onclick="typeOfPopup='log';showAsPopup('processBatch?middleAction=log&cd_btch=<bs:formelement bean="current_batch" name="cd_btch"/>',800,500)"	
			/></td>	
			<td  style="cursor:pointer" width="0%">
				<img src="images/menu/24_edit.gif" alt="log"
				onclick="typeOfPopup='detail';showAsPopup('processBatch?middleAction=detail&cd_btch=<bs:formelement bean="current_batch" name="cd_btch"/>',500,300)"	
			/></td>					
		</tr>	
		
		</bs:sequence>	
		</table>
		<br>
	
		<table width="100%">
			<tr>
				<td  class="labelLeft">Scheduled for next execution:</td>
			</tr>	
		</table>
		<table width="100%">
		
			
			<tr>	
		
				<td class="colTab_center" align="center" width="100">Id</td>
				<td class="colTab_center" align="center">Description</td>
				<td class="colTab_center" align="center" width="100">Scheduled Time</td>
				<td class="colTab_center" align="center" width="100">State</td>
		
		
				
			</tr>
		
			<bs:sequence name="elementsthread">
			<bs:bean name="current_thread" property="elementsthread" index="sequence"/>
			<tr >
			
				<td class="colTab1White" width="70">
					<bs:formelement bean="current_thread" name="batch.cd_btch"/>
				</td>
				<td class="colTab1White" width="100%">
					<bs:formelement bean="current_thread" name="batch.dsc_btch"/>
				</td>
				<td class="colTab1White" >
				<nobr><bs:formelement bean="current_thread" name="exec_time" formatOutput="dd/MM/yyyy HH:mm:sss"/></nobr>
				</td>
				<td class="colTab1White" width="70">
					<bs:formelement bean="current_thread" name="stateThread"/>
				</td>
				
			</tr>	
			
			</bs:sequence>	
			</table>


</td>
</tr>
</table>

	
	
	</bs:form>

</center>



</div>
<jsp:include  page="/jsp/included/panel_bottom.jsp" />


</td>
</tr>
</table>
</div>
</body>
<%@ include file="../framework/footer.jsp" %>

