<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<%@ include file="../framework/header.jsp" %>  
<body >

<div id="content_Panel_canvas" 
style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; overflow:hidden;"
>
<table border="0" width="100%" height="100%" cellpadding="0" cellspacing="0">
<tr>
<td align="center" bgcolor="">

<jsp:include page="/jsp/included/panel_top.jsp" >
	<jsp:param name="panel-id" value="panel_Login" />	
	<jsp:param name="panel-onclose" value="window.close();" />

</jsp:include>	

<div id="page" style=" width: 900; height: 530; background-color: white;">



<%@ include file="../included/content_page_header.jsp" %>
<script language="javascript" src="javascript2012/builder.js"></script>
<script>
	var cr_x=0;
	var cr_y=0;
	var cr_s=0;
	var posTop=0;
	var posLeft=0;
</script>	

<table>
<tr>

	<td align="center"><script>ObjectDraw("page1","button",1,"syncro","runSubmit('syncro')","page_section","","images/menu/","",true,24,"35");</script></td>
	<td align="center"><script>ObjectDraw("page1","button",2,"clear","runSubmit('clear')","page_section","","images/menu/","",true,24,"35");</script></td>
	<td align="center"><script>ObjectDraw("page1","button",3,"peview/load new","showAsPopup('builder?middleAction=preview',950,550)","page_section","","images/menu/","",true,24,"35");</script></td>
	<td id="general"> 
<table  cellspacing="0" >
	<tr>
		<td width="0%" class="page_section">encoding:</td>
		<td width="150" id="general||||"><bs:input name="l_actions.xmlEncoding" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'xmlEncoding')" onfocus="makeFocus(this)"/></td>

		<td width="0%" class="page_section">externalloader:</td>
		<td width="150" id="general||||"><bs:input name="l_actions.externalloader" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'externalloader')" onfocus="makeFocus(this)"/></td>
	</tr>
</table>						

	</td>
</tr>
</table>

<center>

<bs:form onhistory="" method="post">
<bs:input type="hidden" name="scrollLeft"/>
<bs:input type="hidden" name="scrollTop"/>

<bs:input type="hidden" name="id_selected_action"/>
<bs:input type="hidden" name="id_selected_stream"/>
<bs:input type="hidden" name="id_selected_redirect"/>
<bs:input type="hidden" name="id_selected_bean"/>
<bs:input type="hidden" name="id_selected_transformation"/>

<bs:input type="hidden" name="display_streams"/>
<bs:input type="hidden" name="display_actions"/>
<bs:input type="hidden" name="display_beans"/>
<bs:input type="hidden" name="display_redirects"/>


<div id="canvas_report" style=" width:900px;height:450px; 
	overflow: scroll; 
"
>

<table class="tableBodyTabb" border="0" id="canvas_table" >
<tr>
	<td style="border: dashed 1px silver">
		<table width="100%" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td  height="20" width="50%" valign="middle">
					<img id="action-streams_img" src="images/menu/16_<bs:equal name="display_streams" value="false">close</bs:equal><bs:equal name="display_streams" value="true">open</bs:equal>.gif" border="0" style="cursor:pointer" onclick="draw_div_section('action-streams','display_streams')">
				</td>
				<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
				<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section">action-streams</span></nobr></td>
				<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
				<td  height="20" width="50%"><img src="images/decor/20_add.gif" border="0" style="cursor:pointer" onclick="addEntity('stream','','')"></td>
			</tr>
		</table>
	</td>
	<td style="border: dashed 1px silver">
		<table width="100%" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td  height="20" width="50%" valign="middle"><img id="action-mappings_img" src="images/menu/16_<bs:equal name="display_actions" value="false">close</bs:equal><bs:equal name="display_actions" value="true">open</bs:equal>.gif" border="0" style="cursor:pointer" onclick="draw_div_section('action-mappings','display_actions')"></td>
				<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
				<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section">action-mappings</span></nobr></td>
				<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
				<td  width="50%"><img src="images/decor/20_add.gif" border="0" style="cursor:pointer" onclick="addEntity('action','','')"></td>
			</tr>
		</table>
	</td>
	<td style="border: dashed 1px silver">
		<table width="100%" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td  height="20" width="50%" valign="middle"><img id="form-beans_img" src="images/menu/16_<bs:equal name="display_beans" value="false">close</bs:equal><bs:equal name="display_beans" value="true">open</bs:equal>.gif" border="0" style="cursor:pointer" onclick="draw_div_section('form-beans','display_beans')"></td>
				<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
				<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section">form-beans</span></nobr></td>
				<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
				<td  width="50%"><img src="images/decor/20_add.gif" border="0" style="cursor:pointer" onclick="addEntity('bean','','')"></td>
			</tr>
		</table>
	</td>
	<td style="border: dashed 1px silver">
		<table width="100%" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td  height="20" width="50%" valign="middle"><img id="form-redirects_img" src="images/menu/16_<bs:equal name="display_redirects" value="false">close</bs:equal><bs:equal name="display_redirects" value="true">open</bs:equal>.gif" border="0" style="cursor:pointer" onclick="draw_div_section('form-redirects','display_redirects')"></td>
				<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
				<td  height="20" style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section">form-redirects</span></nobr></td>
				<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
				<td  width="50%"><img src="images/decor/20_add.gif" border="0" style="cursor:pointer" onclick="addEntity('redirect1','','')"></td>
			</tr>
		</table>
	</td>



	
</tr>
<tr>
	<td align="center" valign="top"  >
	
		<table cellspacing="0" >
		<tr id="action-streams" <bs:equal name="display_streams" value="false">style="display:none"</bs:equal>>
		<bs:sequence name="l_actions.v_info_streams">				
			<bs:bean name="current_stream" property="l_actions.v_info_streams" index="sequence"/>	
			<td id="stream_<bs:formelement bean="current_stream" name="name"/>" valign="top"> 
				<table cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_stream_<bs:formelement bean="current_stream" name="name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_stream('<bs:formelement bean="current_stream" name="name"/>',false)">
					</td>
					<td  background="images/corners/panel_t.gif" class="page_section">
						<nobr><bs:formelement bean="current_stream" name="name"/>&nbsp;&nbsp;</nobr>
					</td>	
<bs:equal bean="current_stream" name="system" value="false">		
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('stream','<bs:formelement bean="current_stream" name="name"/>','','')"/>
					</td>
					<td width="0%">&#8594;</td>
</bs:equal>									
				</tr>
				</table>

			</td>
			
		</bs:sequence>	
		</tr>	
	
		</table>
	</td>		
	<td align="center" valign="top" >

		<table  > 		
		<tbody id="action-mappings" <bs:equal name="display_actions" value="false">style="display:none"</bs:equal>>
			<tr>
				<td>
				<jsp:include page="/jsp/included/panel_top.jsp" />
					<table width="400"  cellspacing="0">
						<tr>
							<td width="0%" class="page_section">error:</td>
							<td width="100%" id="general||||"><bs:input name="l_actions.error" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'error')"/></td>
						</tr>
						<tr>
							<td width="0%" class="page_section">auth_error:</td>
							<td width="100%" id="general||||"><bs:input name="l_actions.auth_error" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'auth_error')"/></td>
						</tr>
						<tr>
							<td width="0%" class="page_section">session_error:</td>
							<td width="100%" id="general||||"><bs:input name="l_actions.session_error" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'session_error')"/></td>
						</tr>
					</table>
				<jsp:include page="/jsp/included/panel_bottom.jsp" />
				</td>
			</tr>
		<bs:sequence name="l_actions.v_info_actions" >				
			<bs:bean name="current_action" property="l_actions.v_info_actions" index="sequence"/>
			<tr>
			<td id="action_<bs:formelement bean="current_action" name="path"/>" valign="top"> 
				<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_action_<bs:formelement bean="current_action" name="path"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_action('<bs:formelement bean="current_action" name="path"/>',false)">
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement bean="current_action" name="path"/>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('action','<bs:formelement bean="current_action" name="path"/>','','')"/>
					</td>
				</tr>
				</table>
			</td>			
		</tr>
		</bs:sequence>	
		</tbody>
		</table>
	</td>
	<td align="center" valign="top" >
		<table  > 
		<tbody id="form-beans" <bs:equal name="display_beans" value="false">style="display:none"</bs:equal>>
		<bs:sequence name="l_actions.v_info_beans" >				
			<bs:bean name="current_bean" property="l_actions.v_info_beans" index="sequence"/>
			<tr>
			<td id="bean_<bs:formelement bean="current_bean" name="name"/>" valign="top"> 
				<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_bean_<bs:formelement bean="current_bean" name="name"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
						onclick="draw_bean('<bs:formelement bean="current_bean" name="name"/>',false)">
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement bean="current_bean" name="name"/>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('bean','<bs:formelement bean="current_bean" name="name"/>','','')"/>
					</td>
				</tr>
				</table>
			</td>			
		</tr>
		</bs:sequence>	
		</tbody>
		</table>
	</td>

	<td align="center" valign="top" >
		<table  > 
		<tbody id="form-redirects" <bs:equal name="display_redirects" value="false">style="display:none"</bs:equal>>
		<bs:sequence name="l_actions.v_info_redirects" >				
			<bs:bean name="current_redirect" property="l_actions.v_info_redirects" index="sequence"/>
			<tr>
			<td id="redirect_<bs:formelement bean="current_redirect" name="path"/>" valign="top"> 
				<table width="400" cellspacing="0">
				<tr>
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">
						<img id="img_redirect_<bs:formelement bean="current_redirect" name="path"/>" src="images/menu/16_close.gif"/ border="0" style="cursor:pointer"
					onclick="draw_redirect1('<bs:formelement bean="current_redirect" name="path"/>',false)">
					</td>
					<td width="100%" background="images/corners/panel_t.gif" class="page_section">
						<bs:formelement bean="current_redirect" name="path"/>&nbsp;&nbsp;
					</td>	
					<td width="0%" background="images/corners/panel_t.gif" valign="middle">			
						<img src="images/menu/close_section.gif" style="cursor:pointer"
						onclick="removeEntity('redirect1','<bs:formelement bean="current_redirect" name="path"/>','','')"/>
					</td>
				</tr>
				</table>
			</td>			
		</tr>
		</bs:sequence>	
		</tbody>
		</table>
	</td>


</tr>	
</table>
	
</div>



</bs:form>
</center>
<script>


	var posObj = getPosition(document.getElementById("canvas_report"));
	posTop=posObj.y;
	posLeft=posObj.x;

//	makeUnselectable(document.getElementById("canvas_report"));
	document.getElementById("canvas_report").scrollTop="<bs:formelement name="scrollTop"/>";
	document.getElementById("canvas_report").scrollLeft="<bs:formelement name="scrollLeft"/>";


	if(document.getElementById("canvas_table").offsetHeight>450){
		document.getElementById("canvas_report").style.overflowY="scroll";
		document.getElementById("canvas_report").style.height = 450;
	}else{
		document.getElementById("canvas_report").style.overflowY="auto";
	}
</script>




</div>
<jsp:include  page="/jsp/included/panel_bottom.jsp" />


</td>
</tr>
</table>
</div>
</body>
<%@ include file="../framework/footer.jsp" %>



