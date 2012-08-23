package application.web.forms; 

import java.util.Date;

import it.classhidra.core.controller.bean;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.i_menu_element;



public class formContent extends bean implements i_bean{
	private static final long serialVersionUID = 4236354371706619470L;
	private String menuSource;
	i_menu_element element_menu_html;
	
	private Date date_Sistema; 
	private Date date_Valuta ;
	private Date date_Contabile;
	
	private boolean firstEnter=false;

public void reimposta(){
	menuSource="";
	element_menu_html = null;
	date_Sistema = null;
	date_Valuta = null;
	date_Contabile = null;
}

public String getMenuSource() {
	return menuSource;
}

public void setMenuSource(String menuSource) {
	this.menuSource = menuSource;
}

public i_menu_element getElement_menu_html() {
	return element_menu_html;
}

public void setElement_menu_html(i_menu_element elementMenuHtml) {
	element_menu_html = elementMenuHtml;
}

public boolean getFirstEnter() {
	return firstEnter;
}

public void setFirstEnter(boolean firstEnter) {
	this.firstEnter = firstEnter;
}

public Date getDate_Sistema() {
	return date_Sistema;
}

public void setDate_Sistema(Date dateSistema) {
	date_Sistema = dateSistema;
}

public Date getDate_Valuta() {
	return date_Valuta;
}

public void setDate_Valuta(Date dateValuta) {
	date_Valuta = dateValuta;
}

public Date getDate_Contabile() {
	return date_Contabile;
}

public void setDate_Contabile(Date dateContabile) {
	date_Contabile = dateContabile;
}

}
