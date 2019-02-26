package it.classhidra.framework.web.formbeans;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import it.classhidra.core.controller.bean;

import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.redirects;



public class formLogin extends bean implements i_bean{
	private static final long serialVersionUID = 281463687355210444L;
	private String user;
	private String password;
	private String lang;
	private String group;
	private String target;
	private String prev_user;

	private Vector groups;

public void reimposta(){
	group="";
	target="";
	user="";
	password="";
	lang="EN";
	prev_user="";

	groups=new Vector();
}

public redirects validate(HttpServletRequest request){
	if(this.middleAction!=null && this.middleAction.equals("lang")) return null;
	if(this.user.equals("") && this.password.equals("")) user="anonimous";
/*
	if(this.user.equals("") || this.password.equals("")){
		new bsControllerMessageException("error_1",request,null,iStub.log_INFO);
		redirects redirect = new redirects(bsController.getAction_config().actionFactory("$login").get_infoaction().getRedirect());
		return redirect;
	}
	if(!this.user.equals("") && !this.password.equals("") && !this.group.equals("")){
		String cur_id = (String)request.getAttribute(bsController.CONST_ID);
		if(cur_id!=null && !cur_id.equals("$login")){
			new bsControllerMessageException("message_1",request,null,iStub.log_INFO);
		}
	}
*/
	return null;
}
public String getPassword() {
	return password;
}
public String getUser() {
	return user;
}
public void setPassword(String string) {
	password = string;
}
public void setUser(String string) {
	user = string;
}

	public String getLang() {
		return lang;
	}

	public void setLang(String string) {
		lang = string;
	}

	public String getGroup() {
		return group;
	}


	public void setGroup(String string) {
		group = string;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String string) {
		target = string;
	}

	public String getPrev_user() {
		return prev_user;
	}

	public void setPrev_user(String string) {
		prev_user = string;
	}

	public Vector getGroups() {
		return groups;
	}

	public void setGroups(Vector vector) {
		groups = vector;
	}

}
