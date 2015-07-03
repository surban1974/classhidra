package it.classhidra.framework.web.formactions;

import it.classhidra.core.controller.*;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsControllerException;

import java.io.Serializable;
import javax.servlet.http.*;
import javax.servlet.*;



public class actionChangeAuth extends action implements i_action, Serializable{
	private static final long serialVersionUID = 6641876370416839602L;

public actionChangeAuth(){
	super();
}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	if(get_bean().get("name").equals("")){
		try{
			auth_init aInit = (auth_init)request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION);
			get_bean().set("name",aInit.get_user());
			get_bean().set("matriculation",aInit.get_matricola());
			get_bean().set("language",aInit.get_language());
			get_bean().set("group",aInit.get_ruolo().replace('^',';'));
			get_bean().set("target",aInit.get_target().replace('^',';'));
			get_bean().set("wac_fascia",aInit.get_wac_fascia());
		}catch(Exception e){
		}
	}

	if(get_bean().get(bsConstants.CONST_ID_$MIDDLE_ACTION).equals("change")){
		auth_init aInit = null;
		try{
			aInit = (auth_init)request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION);
		}catch(Exception e){
		}
		if (aInit!=null) aInit.saveFromForm(get_bean(), request, response);
	}

	return new redirects(get_infoaction().getRedirect());
}

}
