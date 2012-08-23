package it.classhidra.framework.web.formbeans;

import javax.servlet.http.HttpServletRequest;

import it.classhidra.core.controller.bean;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.redirects;



public class formChangeAuth extends bean implements i_bean{
	private static final long serialVersionUID = -5758690579013910457L;
	private String name;
	private String password;
	private String password_old;
	private String matriculation;
	private String language;
	private String group;
	private String target;
	private String wac_fascia;
	
	
public void reimposta(){
	name="";
	password="";
	password_old="";
	matriculation="";
	language="";
	group="";
	target="";
	wac_fascia="";

}

public redirects validate(HttpServletRequest request){
	return null;
}

	public String getGroup() {
		return group;
	}

	public String getLanguage() {
		return language;
	}

	public String getMatriculation() {
		return matriculation;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getTarget() {
		return target;
	}

	public String getWac_fascia() {
		return wac_fascia;
	}


	public void setGroup(String string) {
		group = string;
	}

	public void setLanguage(String string) {
		language = string;
	}

	public void setMatriculation(String string) {
		matriculation = string;
	}

	public void setName(String string) {
		name = string;
	}

	public void setPassword(String string) {
		password = string;
	}

	public void setTarget(String string) {
		target = string;
	}

	public void setWac_fascia(String string) {
		wac_fascia = string;
	}

	public String getPassword_old() {
		return password_old;
	}

	public void setPassword_old(String string) {
		password_old = string;
	}

}
