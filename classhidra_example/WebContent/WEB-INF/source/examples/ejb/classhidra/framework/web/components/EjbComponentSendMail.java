package examples.ejb.classhidra.framework.web.components;



import java.io.Serializable;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import application.util.mail.mail_manager_smtp;
import application.util.mail.mail_message;
import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.NavigatedDirective;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.core.tool.log.stubs.iStub;


				@Action (
						path="sendMail",
						name="formMail",
						redirect="/jsp/framework/content.jsp",
						reloadAfterAction="true"
				)

//@Named("sendMail")
//@SessionScoped
@NavigatedDirective(memoryContent="true")
@Stateful
@Local(i_action.class)
public class EjbComponentSendMail extends action implements i_action, Serializable{

	private static final long serialVersionUID = -1L;
	private String s_name;
	private String s_email;
	private String mess;
	
	@Inject
	private mail_message m_message;
	
public EjbComponentSendMail(){
	super();
}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {


	if(get_bean().getMiddleAction().equals("send")){

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

	}


	return new redirects(get_infoaction().getRedirect());
}

public void reimposta(){
	s_name="";
	s_email="";
	mess="";
	
	m_message.setBODY_CONTENT_TYPE("html");
	m_message.setSMTPHOST("smtp.gmail.com");
	m_message.setSMTPPORT("465");
	m_message.setSMTPUSESOCKET("true");
	m_message.setMAILADDRESS_FROM("classhidra@gmail.com");
	m_message.setMAILADDRESS_TO("classhidra@gmail.com");
	m_message.setMAILUSER("classhidra@gmail.com");
	m_message.setMAILPSWD("password_must_be_changed");

}


public String getS_name() {
	return s_name;
}


public void setS_name(String sName) {
	s_name = sName;
}


public String getS_email() {
	return s_email;
}


public void setS_email(String sEmail) {
	s_email = sEmail;
}


public String getMess() {
	return mess;
}


public void setMess(String mess) {
	this.mess = mess;
}


public mail_message getM_message() {
	return m_message;
}


public void setM_message(mail_message mMessage) {
	m_message = mMessage;
}


}