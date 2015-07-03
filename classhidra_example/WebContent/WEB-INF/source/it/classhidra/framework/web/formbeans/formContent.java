package it.classhidra.framework.web.formbeans;

import javax.servlet.http.HttpServletRequest;

import it.classhidra.core.controller.bean;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.redirects;



public class formContent extends bean implements i_bean{
	private static final long serialVersionUID = -1125497534735376530L;
	private String menuSource;

public void reimposta(){
	menuSource="";
}

public redirects validate(HttpServletRequest request){
	return null;
}
	public String getMenuSource() {
		return menuSource;
	}

	public void setMenuSource(String string) {
		menuSource = string;
	}

}
