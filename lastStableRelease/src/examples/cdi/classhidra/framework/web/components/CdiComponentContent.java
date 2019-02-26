package examples.cdi.classhidra.framework.web.components;



import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.NavigatedDirective;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;







@Action (
		path="content",
		name="beanContent",
		redirect="/jsp/framework/content.jsp",
		reloadAfterAction="true"
	)
@NavigatedDirective(memoryContent="true")

public class CdiComponentContent extends action implements i_action, Serializable{
	private static final long serialVersionUID = -1L;


public CdiComponentContent(){
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


}
