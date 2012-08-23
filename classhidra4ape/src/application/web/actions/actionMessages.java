package application.web.actions; 

import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.util.util_cloner;

import java.io.Serializable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class actionMessages extends action implements i_action, Serializable{
	private static final long serialVersionUID = 3960397543730267863L;

public actionMessages(){
	super();
}	
	
public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	Vector $listmessages = (Vector)request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE);
	Vector local_$listmessages = new Vector();
	if(get_bean().getMiddleAction()==null) get_bean().setMiddleAction("");

		try{
			local_$listmessages = (Vector) util_cloner.clone($listmessages);	
			if(get_bean().getMiddleAction().equals("view")){
				$listmessages.clear();
			}
		}catch(Exception ex){
			
		}
		get_bean().set("view_list",local_$listmessages);
	return new redirects(get_infoaction().getRedirect());	

}

}
