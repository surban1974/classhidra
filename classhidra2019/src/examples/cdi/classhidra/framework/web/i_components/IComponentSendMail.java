package examples.cdi.classhidra.framework.web.i_components;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.ActionCall;
import it.classhidra.annotation.elements.NavigatedDirective;
import it.classhidra.annotation.elements.Redirect;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;

@Action (
		path="sendMail",
		name="formMail",
		redirect="/jsp/pages/sendmail.jsp",
		reloadAfterAction="true"
)
@NavigatedDirective(memoryContent="true")
public interface IComponentSendMail extends i_action{

	@ActionCall(name="send", 
			Redirect=@Redirect(
					path="/jsp/pages/sendmail.jsp",
					descr="Send Messages",
					mess_id="title_fw_SendMail"
			)
	)	
	redirects send_mail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, UnavailableException, bsControllerException;

}