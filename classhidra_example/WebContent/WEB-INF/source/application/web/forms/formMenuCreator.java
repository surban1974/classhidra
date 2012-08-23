package application.web.forms;

import javax.servlet.http.HttpServletRequest;

import it.classhidra.core.controller.bean;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.i_menu_element;
import it.classhidra.core.controller.redirects;



public class formMenuCreator extends bean implements i_bean{
	private static final long serialVersionUID = -6394052562506866988L;
	private String menu_id;
	private String menu_html;
	private String menu_frame;
	private String menu_action;
	private String menu_lang;
	i_menu_element element;
	i_menu_element element_menu_html;

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
