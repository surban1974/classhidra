package examples.ejb.classhidra.framework.web.components;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import application.util.mail.mail_manager_smtp;
import application.util.mail.mail_message;
import examples.ejb.classhidra.framework.web.i_components.IComponentSendMail;
import it.classhidra.annotation.elements.NavigatedDirective;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.core.tool.exception.message;
import it.classhidra.core.tool.log.stubs.iStub;


@NavigatedDirective(memoryContent="true")
@Stateful
//@Local(IComponentSendMail.class)
@Remote(IComponentSendMail.class)


public class EjbComponentSendMail extends action implements IComponentSendMail, Serializable{

	private static final long serialVersionUID = -1L;
	private String s_name;
	private String s_email;
	private String s_mess;
	public String getS_mess() {
		return s_mess;
	}

	public void setS_mess(String s_mess) {
		this.s_mess = s_mess;
	}


	private String mess;
	
	private mail_message m_message;
	
	private static HashMap map2request = new HashMap();
	
	@Resource
	SessionContext sessionContext;		
	
public EjbComponentSendMail(){
	super();
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

//@Remote implementation
public redirects send_mail(HashMap _content) throws ServletException, UnavailableException, bsControllerException{
	if(get_bean().get("s_mess").equals("")){
		addMessage(new message("E", "error_1", ""));
		return new redirects(get_infoaction().getRedirect());
	}
    String msgBody = (String)get_bean().get("s_mess");
	mail_message mm = (mail_message)get_bean().get("m_message");

	mm.setBODY(msgBody);
	mm.setSUBJECT("Message from:"+get_bean().get("s_name")+" email:"+get_bean().get("s_email"));
	try{
		new mail_manager_smtp().service_send(mm);
		addMessage(new message("I", "message_2", ""));
	}catch(Exception e){
		addMessage(new message("E", "?", e.toString()));
	}
	return new redirects(get_infoaction().getRedirect());	
}
private void addMessage(message mess){
	ArrayList<message> errors = (ArrayList)map2request.get("errors");
	if(errors==null){
		errors = new ArrayList<message>();
		map2request.put("errors",errors);
	}
	errors.add(mess);
}

public static Map convertRequest2Map(HttpServletRequest request){
//	return util_supportbean.request2map(request);
	return new HashMap();
}

public static void convertMap2Request(HttpServletRequest request, HttpServletResponse response){
	try{
		ArrayList<message> errors = (ArrayList<message>)map2request.get("errors");
		if(errors!=null){
			for(message error:errors)
				new bsControllerMessageException(error, request);
			
			errors.clear();
		}

	}catch(Exception e){
		
	}
}	

//***

public void reimposta(){
	s_name="";
	s_email="";
	s_mess="";
	mess="";
	
	if(m_message==null)
		m_message = new mail_message();
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
