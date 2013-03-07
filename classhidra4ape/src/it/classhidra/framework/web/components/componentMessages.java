package it.classhidra.framework.web.components; 

import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.Bean;
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

@Bean (	name="formMessages")

@Action (
		path="messages",
		name="formMessages",
		redirect="/jsp/framework/viewmessages.jsp"
)

public class componentMessages extends action implements i_action, Serializable{
	private static final long serialVersionUID = 3960397543730267863L;

	private Vector view_list;



	
public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	Vector $listmessages = (Vector)request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE);
	Vector local_$listmessages = new Vector();
	if(this.getMiddleAction()==null) this.setMiddleAction("");

		try{
			local_$listmessages = (Vector) util_cloner.clone($listmessages);	
			if(this.getMiddleAction().equals("view")){
				$listmessages.clear();
			}
		}catch(Exception ex){
			
		}
		view_list=local_$listmessages;
	return new redirects(get_infoaction().getRedirect());	

}

public void reimposta(){
	view_list=new Vector();
}

public redirects validate(HttpServletRequest request){
	return null;
}

public Vector getView_list() {
	return view_list;
}

public void setView_list(Vector view_list) {
	this.view_list = view_list;
}
}
