<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<bs:equal name="middleAction" value="view_general">
<table  cellspacing="0" >
	<tr>
		<td width="0%" class="page_section">encoding:</td>
		<td width="150" id="general||||"><bs:input name="l_actions.xmlEncoding" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'xmlEncoding')"/></td>

		<td width="0%" class="page_section">externalloader:</td>
		<td width="150" id="general||||"><bs:input name="l_actions.externalloader" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'externalloader')"/></td>
	</tr>
</table>	
</bs:equal>
<%@ include file="../builder/include_stream.jsp" %>
<%@ include file="../builder/include_apply_to_action.jsp" %>
<%@ include file="../builder/include_action.jsp" %>
<%@ include file="../builder/include_redirect.jsp" %>
<%@ include file="../builder/include_transformationoutput.jsp" %>
<%@ include file="../builder/include_transformationoutput1.jsp" %>
<%@ include file="../builder/include_bean.jsp" %>
<%@ include file="../builder/include_redirect1.jsp" %>
<%@ include file="../builder/include_item.jsp" %>
<%@ include file="../builder/include_section.jsp" %>




