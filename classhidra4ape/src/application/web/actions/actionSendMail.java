package application.web.actions; 



import it.classhidra.core.controller.action;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.core.tool.log.stubs.iStub;

import java.io.Serializable;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class actionSendMail extends action implements i_action, Serializable{

	private static final long serialVersionUID = -1L;

public actionSendMail(){
	super();
}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {


	if(get_bean().getMiddleAction().equals("send")){
		
		if(get_bean().get("s_mess").equals("")){
			new bsControllerMessageException("error_1",request,null,iStub.log_ERROR);
			return new redirects(get_infoaction().getRedirect());
		}
		
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = (String)get_bean().get("s_mess");

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("svyatoslav.urbanovych@gmail.com", "Admin ClassHidra4api"));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress("classhidra@gmail.com", "ClassHidra4app Messager"));
            msg.setSubject("Message from:"+get_bean().get("s_name")+" email:"+get_bean().get("s_email"));
            msg.setText(msgBody);
            Transport.send(msg);
            new bsControllerMessageException("message_2",request,null,iStub.log_INFO);

        }catch(Exception e){
			new bsControllerMessageException(e.toString(),request,null,iStub.log_ERROR);
		}
/*		
		mail_message mm = new mail_message();
		
		mm.setBODY((String)get_bean().get("s_mess"));
		mm.setBODY_CONTENT_TYPE("html");
		mm.setSMTPHOST("smtp.gmail.com");
		mm.setSMTPPORT("465");
		mm.setSMTPUSESOCKET("true");
		mm.setMAILADDRESS_FROM("classhidra@gmail.com");
		mm.setMAILADDRESS_TO("surban@bigmir.net");
		mm.setMAILUSER("classhidra@gmail.com");
		mm.setMAILPSWD("********");
		mm.setSUBJECT("Message from:"+get_bean().get("s_name")+" email:"+get_bean().get("s_email"));
		try{
			new mail_manager_smtp().service_send(mm);
			new bsControllerMessageException("message_2",request,null,iStub.log_INFO);
		}catch(Exception e){
			new bsControllerMessageException(e.toString(),request,null,iStub.log_ERROR);
		}
*/		
	}

	
	return new redirects(get_infoaction().getRedirect());
}

}
