package application.web.actions;



import it.classhidra.core.controller.action;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.core.tool.log.stubs.iStub;

import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import application.util.mail.mail_manager_smtp;
import application.util.mail.mail_message;



public class actionSendMail extends action implements i_action, Serializable{

	private static final long serialVersionUID = -1L;

public actionSendMail(){
	super();
}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	return new redirects(get_infoaction().getRedirect());
}

public redirects send_mail(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {

	if(get_bean().get("s_mess").equals("")){
			new bsControllerMessageException("error_1",request,null,iStub.log_ERROR);
			return new redirects(get_infoaction().getRedirect());
		}


        String msgBody = (String)get_bean().get("s_mess");



		mail_message mm = (mail_message)get_bean().get("m_message");

		mm.setBODY(msgBody);
		mm.setSUBJECT("Message from:"+get_bean().get("s_name")+" email:"+get_bean().get("s_email"));
		try{
			new mail_manager_smtp().service_send(mm);
			new bsControllerMessageException("message_2",request,null,iStub.log_INFO);
		}catch(Exception e){
			new bsControllerMessageException(e.toString(),request,null,iStub.log_ERROR);
		}

	return new redirects(get_infoaction().getRedirect());
}


}
