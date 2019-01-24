package application.replacedAsComponent.web.forms;

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import it.classhidra.core.controller.bean;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.redirects;



public class formMinimizer extends bean implements i_bean{
	private static final long serialVersionUID = 1L;
	private HashMap fromSessions;
	private HashMap elements;
	private HashMap labels;
	private Vector keys;
	private String source;

public void reimposta(){
	fromSessions=new HashMap();
	elements=new HashMap();
	labels=new HashMap();
	keys=new Vector();
	source="";
}

public redirects validate(HttpServletRequest request){
	return null;
}

public HashMap getElements() {
	return elements;
}

public void setElements(HashMap elements) {
	this.elements = elements;
}

public Vector getKeys() {
	return keys;
}

public void setKeys(Vector keys) {
	this.keys = keys;
}

public String getSource() {
	return source;
}

public void setSource(String source) {
	this.source = source;
}

public HashMap getLabels() {
	return labels;
}

public void setLabels(HashMap labels) {
	this.labels = labels;
}

public HashMap getFromSessions() {
	return fromSessions;
}

public void setFromSessions(HashMap fromSessions) {
	this.fromSessions = fromSessions;
}


}
