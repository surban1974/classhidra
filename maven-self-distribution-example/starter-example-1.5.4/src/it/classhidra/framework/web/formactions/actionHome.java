package it.classhidra.framework.web.formactions;

import it.classhidra.core.controller.*;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsControllerException;

import java.io.Serializable;
import javax.servlet.http.*;
import javax.servlet.*;



public class actionHome extends action implements i_action, Serializable{
	private static final long serialVersionUID = -4790613509665242531L;

public actionHome(){
	super();
}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	if(!get_bean().get("lang").equals("")){
		try{
			auth_init aInit = (auth_init)request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION);
			aInit.set_language((String)get_bean().get("lang"));
		}catch(Exception e){
		}
	}

	return new redirects(get_infoaction().getRedirect());
}

}
