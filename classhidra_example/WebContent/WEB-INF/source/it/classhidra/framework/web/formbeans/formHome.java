package it.classhidra.framework.web.formbeans;

import javax.servlet.http.HttpServletRequest;

import it.classhidra.core.controller.bean;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.redirects;



public class formHome extends bean implements i_bean{
	private static final long serialVersionUID = 149516054527417887L;
	private String lang;
	
public void reimposta(){
	lang="IT";
}

public redirects validate(HttpServletRequest request){
	return null;
}
	public String getLang() {
		return lang;
	}

	public void setLang(String string) {
		lang = string;
	}

}
