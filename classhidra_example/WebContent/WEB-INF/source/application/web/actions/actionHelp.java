package application.web.actions;

import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.controller.*;
import java.io.Serializable;
import javax.servlet.http.*;
import javax.servlet.*;



public class actionHelp extends action implements i_action, Serializable{
	private static final long serialVersionUID = 77477796283694045L;

public actionHelp(){
	super();
}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	if(get_bean().get("helpRedirect").equals("")) get_bean().set("helpRedirect",get_bean().get("helpId"));
	i_action action_instance = null;
	try{
		action_instance = bsController.getAction_config().actionFactory((String)get_bean().get("helpId"));
	}catch(Exception e){
	}
	if(action_instance==null || action_instance.get_infoaction().getHelp().equals(""))
		return new redirects(get_infoaction().getError());

	else return new redirects(action_instance.get_infoaction().getHelp());
}

}
