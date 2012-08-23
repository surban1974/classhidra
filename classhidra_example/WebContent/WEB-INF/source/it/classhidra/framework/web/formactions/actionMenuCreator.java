package it.classhidra.framework.web.formactions;

import it.classhidra.core.controller.*;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.framework.web.beans.menu_element;
import it.classhidra.framework.web.formbeans.formMenuCreator;

import java.io.Serializable;
import javax.servlet.http.*;
import javax.servlet.*;


public class actionMenuCreator extends action implements i_action, Serializable{
	private static final long serialVersionUID = 7394761843939384352L;

public actionMenuCreator(){
	super();
}	
	
public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	if(get_bean().get("element")==null) prepareElement(request);
	if(get_bean().get("element")!=null){
		
		if(get_bean().get("menu_id").equals("all")){
			menu_element founded = new menu_element((i_menu_element)get_bean().get("element"));
			founded.setVisibilityAllChildren(true);
			get_bean().set("menu_id",founded.getInfo_menu().getId());
			get_bean().set("menu_html",founded.generateHTML(request));
		}
		if(get_bean().get("menu_id").equals("nothing")){
			menu_element founded = new menu_element((i_menu_element)get_bean().get("element"));
			founded.setVisibilityAllChildren(false);
			founded.setVisible(true);
			get_bean().set("menu_id",founded.getInfo_menu().getId());
			get_bean().set("menu_html",founded.generateHTML(request));
		}
		
		menu_element founded = 	new menu_element(
				((i_menu_element)get_bean().get("element")).find((String)get_bean().get("menu_id"))
				);
		if(founded!=null){
			if(get_bean().get("menu_action").equals("visual_true")) founded.setVisibilityChildren(true);
			if(get_bean().get("menu_action").equals("visual_false")) founded.setVisibilityChildren(false);
			get_bean().set("menu_html",founded.generateHTML(request));			
		}

	}
	return new redirects(get_infoaction().getRedirect());	
}

private void prepareElement(HttpServletRequest request){
	try{	
		i_menu_element element = bsController.getMenu_config().get_menu();
		if(element==null){
			bsController.reloadMenu_config(request,new menu_element());
			element = bsController.getMenu_config().get_menu();
		}
		element.authentication_clear(((auth_init)request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION)).get_actions_forbidden());
		element.authentication_clear(((auth_init)request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION)).get_actions_forbidden());
				
		element.setVisible(true);
		element.calculate_potential_elements();
		element.analyse_potential_group(true);
		((formMenuCreator)get_bean()).setElement(element);
	}catch(Exception e){
		bsController.writeLog("Load_current_menu error: "+e.toString(),iStub.log_ERROR);
	}
	
}

}
