package it.classhidra.framework.web.components;


import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.Bean;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_menu_element;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.controller.load_menu;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.framework.web.beans.menu_element;

import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Bean (	name="formMenuCreator")

@Action (
		path="menuCreator",
		name="formMenuCreator",
		redirect="/jsp/framework/menuCreator.jsp",
        memoryInSession="true",
        entity=@Entity(
				property="always:public"
		)
)

public class componentMenuCreator extends action implements i_action, Serializable{
	private static final long serialVersionUID = 7394761843939384352L;

	private String menu_id;
	private String menu_html;
	private String menu_frame;
	private String menu_action;
	private String menu_lang;
	i_menu_element element;
	i_menu_element element_menu_html;


public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	element = prepareElement(request);
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

private i_menu_element prepareElement(HttpServletRequest request){
	try{
		componentMenuCreator fc = this;

		i_menu_element element = fc.getElement_menu_html();
		if(element==null){
//			load_menu menu_config = new load_menu(new menu_element());
			load_menu menu_config = bsController.getMenu_config().reInit(new menu_element());
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
