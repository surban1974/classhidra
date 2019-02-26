package it.classhidra.framework.web.formbeans;

import javax.servlet.http.HttpServletRequest;

import it.classhidra.core.controller.bean;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.redirects;



public class formHelp extends bean implements i_bean{
	private static final long serialVersionUID = 3970841998150927690L;
	private String helpId;
	private String helpSection;
	private String helpRedirect;

public void reimposta(){
	helpId="";
	helpSection="";
	helpRedirect="";
}

public redirects validate(HttpServletRequest request){
	return null;
}

	public String getHelpId() {
		return helpId;
	}
	public String getHelpSection() {
		return helpSection;
	}
	public void setHelpId(String string) {
		helpId = string;
	}
	public void setHelpSection(String string) {
		helpSection = string;
	}
	public String getHelpRedirect() {
		return helpRedirect;
	}
	public void setHelpRedirect(String string) {
		helpRedirect = string;
	}

}
