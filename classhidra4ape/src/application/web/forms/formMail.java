package application.web.forms; 

import it.classhidra.core.controller.bean;
import it.classhidra.core.controller.i_bean;



public class formMail extends bean implements i_bean{
	private static final long serialVersionUID = -1L;
	private String s_name;
	private String s_email;
	private String mess;


public void reimposta(){
	s_name="";
	s_email="";
	mess="";
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

}
