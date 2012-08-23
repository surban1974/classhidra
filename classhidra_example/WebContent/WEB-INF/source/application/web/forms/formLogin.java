package application.web.forms; 

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import it.classhidra.core.controller.bean;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_bean;

import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.core.tool.log.stubs.iStub;



public class formLogin extends bean implements i_bean{
	private static final long serialVersionUID = -566404967999875610L;
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
	lang="IT";
	prev_user="";

	groups=new Vector();
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
