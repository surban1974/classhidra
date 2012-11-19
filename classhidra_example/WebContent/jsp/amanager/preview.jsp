<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %>
<%@ include file="../framework/header_ajax.jsp" %>
<bs:form name="builder_preview" onhistory=""  >
<div >
	<textarea id="xmlContent" name="xmlContent" wrap="off" style="width:100%;height: 500;overflow:scroll"><bs:formelement name="xmlContent"/> </textarea>
</div>
<table width="100%">
	<tr>
		<td width="50%" align="left" valign="middle" ><font color="red">actions.xml must be inserted into the package /config/ </font></td>
		<td width="50%" align="right" valign="middle" class="page_section">Load XML into builder: </td>
		<bs:notEqual name="mode" value="relation">
			<td width="1%" align="right" valign="middle" style="cursor:pointer"><img src="images/decor/24_add.gif" border="0" alt="Load xml" onclick="runSubmitF('builder_preview','load')"></img></td>
		</bs:notEqual>
		<bs:equal name="mode" value="relation">
			<td width="1%" align="right" valign="middle" style="cursor:pointer"><img src="images/decor/24_add.gif" border="0" alt="Load xml" onclick="runSubmitF('builder_preview','load_auth')"></img></td>
		</bs:equal>
	</tr>
</table>
</bs:form>  

