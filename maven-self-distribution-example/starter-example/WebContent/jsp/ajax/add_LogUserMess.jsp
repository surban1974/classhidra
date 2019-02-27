<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %>
<%@ include file="../framework/header_ajax.jsp" %>  


<bs:form onhistory="" name="formLogUsers_Popup"  >
<bs:input type="hidden" name="session_id"/>
<bs:input type="hidden" name="type_mess" value="I"/>
<table bgcolor="white" width="100%" >

			<tr>
				<td width="100%">
						<textarea id="txcontent" name="txcontent" style="width:100%;height: 330;border: solid 1px silver; owerflow: auto"></textarea>
				</td>
			</tr>
			<tr>
				<td valign="middle" align="center" width="100%">
				
				<table>
					<tr>

						<td><img src="images/decor/24_mess_ok.gif" style="cursor:pointer"
							onclick="document.getElementById('type_mess').value='I'; runSubmitF('formLogUsers_Popup','add_mess')"></td>
						<td><img src="images/decor/24_mess_warning.gif" style="cursor:pointer"
							onclick="document.getElementById('type_mess').value='W'; runSubmitF('formLogUsers_Popup','add_mess')"></td>
						<td><img src="images/decor/24_mess_error.gif" style="cursor:pointer"
							onclick="document.getElementById('type_mess').value='E'; runSubmitF('formLogUsers_Popup','add_mess')"></td>
					</tr>
				</table>
				</td>				
			</tr>			
</table>




</bs:form>  

