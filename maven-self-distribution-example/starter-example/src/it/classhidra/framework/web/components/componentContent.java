package it.classhidra.framework.web.components;



import java.io.Serializable;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.ActionMapping;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.Redirect;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_menu_element;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;

@ActionMapping (
		redirects={
				@Redirect(
						path="/jsp/framework/content.jsp",
						descr="Content",
						mess_id="title_fw_Content"
				)
			},
		actions={
				@Action (
						path="content",
						name="formContent",						
						redirect="/jsp/framework/content.jsp",
						navigated="true",
						reloadAfterAction="true",						
						entity=@Entity(
								property="jwt:check"
						)
				)


		}
)


public class componentContent extends action implements i_action, Serializable{

	private static final long serialVersionUID = -1830493478965489235L;
	
	private String menuSource;
	
	private Date date_Sistema; 
	private Date date_Valuta ;
	private Date date_Contabile;
	
	private boolean firstEnter=false;
	
	i_menu_element element_menu_html;
	
	

public componentContent(){
	super();
}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {


	if(get_bean().get("element_menu_html")==null){
		get_bean().set("firstEnter",new Boolean(true));
	}else{
		get_bean().set("firstEnter",new Boolean(false));
	}


	if(get_bean().get("menuSource")!=null && !get_bean().get("menuSource").equals("")){
		String section = (String)get_bean().get("menuSource");
		int intSection=-1;
		try{
			intSection=Integer.parseInt(section);
		}catch(Exception e){
			intSection=-1;
		}
		get_bean().set("menuSource","");

		if(intSection==-1) 	return new redirects(section+"");
		else return new redirects(get_infoaction().getRedirect());
	}


	return new redirects(get_infoaction().getRedirect());
}

public void reimposta(){
	menuSource="";
	date_Sistema = null;
	date_Valuta = null;
	date_Contabile = null;
	element_menu_html = null;
}

public String getMenuSource() {
	return menuSource;
}

public void setMenuSource(String menuSource) {
	this.menuSource = menuSource;
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

public i_menu_element getElement_menu_html() {
	return element_menu_html;
}

public void setElement_menu_html(i_menu_element elementMenuHtml) {
	element_menu_html = elementMenuHtml;
}

}
