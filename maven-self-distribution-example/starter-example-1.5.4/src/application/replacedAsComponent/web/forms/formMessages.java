package application.replacedAsComponent.web.forms;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import it.classhidra.core.controller.bean;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.redirects;



public class formMessages extends bean implements i_bean{
	private static final long serialVersionUID = -4499306352896225554L;
	private Vector view_list;

public void reimposta(){
	view_list=new Vector();
}

public redirects validate(HttpServletRequest request){
	return null;
}

public Vector getView_list() {
	return view_list;
}

public void setView_list(Vector view_list) {
	this.view_list = view_list;
}

}
