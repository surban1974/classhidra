package it.classhidra.framework.web.formbeans;

import it.classhidra.core.controller.bean;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.redirects;

import javax.servlet.http.HttpServletRequest;





public class formMessagesUtility extends bean implements i_bean{

	private static final long serialVersionUID = -8312566785420939929L;
	private String pathInput;
	private String pathOutput;
	private String langs;

public void reimposta(){
	pathInput="/jsp/";
	pathOutput="";
	langs="IT;EN;DC;FR;";
}

public redirects validate(HttpServletRequest request){
	return null;
}

public String getLangs() {
	return langs;
}

public void setLangs(String langs) {
	this.langs = langs;
}

public String getPathInput() {
	return pathInput;
}

public void setPathInput(String pathInput) {
	this.pathInput = pathInput;
}

public String getPathOutput() {
	return pathOutput;
}

public void setPathOutput(String pathOutput) {
	this.pathOutput = pathOutput;
}



}
