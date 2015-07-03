package application.replacedAsComponent.web.actions;


import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_menu_element;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.controller.load_menu;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.framework.web.beans.menu_element;

import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import application.replacedAsComponent.web.forms.formMenuCreator;


public class actionMenuCreator extends action implements i_action, Serializable{
	private static final long serialVersionUID = 7394761843939384352L;

public actionMenuCreator(){
	super();
}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	i_menu_element element = prepareElement(request);
	if(element!=null){

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
		if(get_bean().get("menu_id").equals("")) get_bean().set("menu_id","menu_level_0");

		menu_element founded = 	new menu_element(
				element.find((String)get_bean().get("menu_id"))
				);
		if(founded!=null){
			if(get_bean().get("menu_action").equals("visual_true")) founded.setVisibilityChildren(true);
			if(get_bean().get("menu_action").equals("visual_false")) founded.setVisibilityChildren(false);
			get_bean().set("menu_html",founded.generateHTML(request));
		}

	}
	return new redirects(get_infoaction().getRedirect());
}

private i_menu_element prepareElement(HttpServletRequest request){
	try{
		formMenuCreator fc = (formMenuCreator)get_bean();
/*
		formContent fc = null;

		info_navigation	formInfoNavigation = (request.getSession().getAttribute(bsController.CONST_BEAN_$NAVIGATION)==null)?new info_navigation():(info_navigation)request.getSession().getAttribute(bsController.CONST_BEAN_$NAVIGATION);

		info_navigation current = formInfoNavigation.find("content");

		if(current==null) return null;

		fc = (formContent)current.get_content();

		if(fc==null) return null;
*/
		i_menu_element element = fc.getElement_menu_html();
		if(element==null){
			load_menu menu_config = new load_menu(new menu_element());
			try{
				menu_config.load_from_resources();
			}catch (Exception e) {
			}

			element = menu_config.get_menu();
		}
		element.authentication_clear(((auth_init)request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION)).get_actions_forbidden());

		element.setVisible(true);
		element.calculate_potential_elements();
		element.analyse_potential_group(true);

		fc.setElement_menu_html(element);
		return element;
	}catch(Exception e){
		bsController.writeLog("Load_current_menu error: "+e.toString(),iStub.log_ERROR);
	}
	return null;

}



}
