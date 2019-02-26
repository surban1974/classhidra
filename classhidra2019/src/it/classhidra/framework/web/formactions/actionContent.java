package it.classhidra.framework.web.formactions;

import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.framework.web.formbeans.formMenuCreator;



public class actionContent extends action implements i_action, Serializable{
	private static final long serialVersionUID = -5614762424636026915L;

public actionContent(){
	super();
}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	try{
		formMenuCreator fmc = (formMenuCreator)bsController.getFromOnlySession("formMenuCreator", request);
		fmc.getElement().hideDynamicElements();
		fmc.getElement().calculate_potential_elements();
		fmc.getElement().analyse_potential_group(true);
		fmc.getElement().hideDynamicElements();
		request.setAttribute("refreshMenu","true");
	}catch(Exception e){
	}
	if(!get_bean().get("menuSource").equals("")) return new redirects("/actions/"+(String)get_bean().get("menuSource")+bsController.getAppInit().get_extention_do());
	else return new redirects(get_infoaction().getRedirect());
}
}
