<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<bs:equal name="middleAction" value="view_general">
<table  cellspacing="0" >
		<tr>
			<td width="0%" class="page_section">encoding:</td>
			<td width="150" id="general||||"><bs:input name="l_users.xmlEncoding" style="border: solid 1px silver; width: 100%" toUpperCase="no" onblur="updateValue(this,'xmlEncoding')"/></td>
			<td width="0%" class="page_section">externalloader:</td>
			<td width="150" id="general||||"><bs:input name="l_users.externalloader" style="border: solid 1px silver; width: 100%" toUpperCase="no" onchange="updateValue(this,'externalloader')"/></td>

		</tr>
</table>		
</bs:equal>
<%@ include file="../amanager/include_user.jsp" %>
<%@ include file="../amanager/include_group.jsp" %>
<%@ include file="../amanager/include_target.jsp" %>
<%@ include file="../amanager/include_action.jsp" %>
<%@ include file="../amanager/include_redirect.jsp" %>
<%@ include file="../amanager/include_relation.jsp" %>
<%@ include file="../amanager/include_authentication.jsp" %>




