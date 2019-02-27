package application.web.forms; 

import application.util.mail.mail_message;
import it.classhidra.core.controller.bean;
import it.classhidra.core.controller.i_bean;



public class formMail extends bean implements i_bean{
	private static final long serialVersionUID = -1L;
	private String s_name;
	private String s_email;
	private String mess;
	private mail_message m_message;


public void reimposta(){
	s_name="";
	s_email="";
	mess="";
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
