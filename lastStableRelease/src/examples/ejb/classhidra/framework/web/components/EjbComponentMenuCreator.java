package examples.ejb.classhidra.framework.web.components;


import java.io.Serializable;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
//import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.SessionDirective;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_menu_element;
import it.classhidra.core.controller.load_menu;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.framework.web.beans.menu_element;

@Action (
		path="menuCreator",
		name="formMenuCreator",
//		memoryInSession="true",
		redirect="/jsp/framework/menuCreator.jsp"
)

//@Named("menuCreator")
//@SessionScoped
@SessionDirective
@Stateful
@Local(i_action.class)
public class EjbComponentMenuCreator extends action implements i_action, Serializable{
	private static final long serialVersionUID = 7394761843939384352L;

	private String menu_id;
	private String menu_html;
	private String menu_frame;
	private String menu_action;
	private String menu_lang;
	
//	@Inject
	private i_menu_element element;
	
	@Resource
	SessionContext sessionContext;		
	
	i_menu_element element_menu_html;

	

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	
	if(element==null || (element!=null && element.getInfo_menu()==null)){
		load_menu menu_config = new load_menu(new menu_element());
		try{
			menu_config.load_from_resources();	
			if(!menu_config.isReadOk())
				menu_config.load_from_resources(request.getSession().getServletContext());
		}catch (Exception e) {
		}

		i_menu_element loaded = menu_config.get_menu();
		
		
		reimposta();
		if(element==null){
			element=loaded;
		}else{
			element.setParent(loaded.getParent());
			element.setChildren(loaded.getChildren());
			element.setChildren_info(loaded.getChildren_info());
			element.setInfo_menu(loaded.getInfo_menu());
			element.setVisible(loaded.isVisible());
			element.setNext(loaded.isNext());
			element.setPrev(loaded.isPrev());
			element.setLevel(loaded.getLevel());
			element.setPotential_elements(loaded.getPotential_elements());
		}
		element.authentication_clear(((auth_init)request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION)).get_actions_forbidden());

		element.setVisible(true);
		element.calculate_potential_elements();
		element.analyse_potential_group(true);

	}
	
	if(element!=null){

		if(menu_id.equals("all")){
			menu_element founded = new menu_element(element);
			founded.setVisibilityAllChildren(true);
			menu_id=founded.getInfo_menu().getId();
			menu_html=founded.generateHTML(request);
		}
		if(menu_id.equals("nothing")){
			menu_element founded = new menu_element(element);
			founded.setVisibilityAllChildren(false);
			founded.setVisible(true);
			menu_id=founded.getInfo_menu().getId();
			menu_html=founded.generateHTML(request);
		}
		if(menu_id.equals("")) menu_id="menu_level_0";

		menu_element founded = 	new menu_element(
				element.find(menu_id)
				);
		if(founded!=null){
			if(menu_action.equals("visual_true")) founded.setVisibilityChildren(true);
			if(menu_action.equals("visual_false")) founded.setVisibilityChildren(false);
			menu_html=founded.generateHTML(request);
		}

	}
	return new redirects(get_infoaction().getRedirect());
}



public void reimposta(){
	menu_id="menu_level_0";
	menu_html="";
	menu_frame="menu";
	menu_action="";
	menu_lang="";
}

public redirects validate(HttpServletRequest request){
	return null;
}
public String getMenu_id() {
	return menu_id;
}

public void setMenu_id(String string) {
	menu_id = string;
}

public i_menu_element getElement() {
	return element;
}

public void setElement(i_menu_element menu_element) {
	element = menu_element;
}


public String getMenu_frame() {
	return menu_frame;
}

public String getMenu_html() {
	return menu_html;
}

public void setMenu_frame(String string) {
	menu_frame = string;
}

public void setMenu_html(String string) {
	menu_html = string;
}

public String getMenu_action() {
	return menu_action;
}

public void setMenu_action(String string) {
	menu_action = string;
}

	public String getMenu_lang() {
		return menu_lang;
	}

	public void setMenu_lang(String string) {
		menu_lang = string;
	}

	public i_menu_element getElement_menu_html() {
		return element_menu_html;
	}

	public void setElement_menu_html(i_menu_element elementMenuHtml) {
		element_menu_html = elementMenuHtml;
	}


}
