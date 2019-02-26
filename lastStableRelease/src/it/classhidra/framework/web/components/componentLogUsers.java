package it.classhidra.framework.web.components;

import it.classhidra.annotation.elements.Access;
import it.classhidra.annotation.elements.AccessRelation;
import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.ActionMapping;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.Redirect;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.message;
import it.classhidra.core.tool.util.util_usersInSession;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@ActionMapping (
		redirects={
				@Redirect(
						path="/jsp/framework/log_users.jsp",
						descr="Users In Session",
						mess_id="title_fw_Log_users"
				)
			}
)



@Action (
	path="log_users",
	redirect="/jsp/framework/log_users.jsp",
	navigated="true",
	redirects={
			@Redirect(
				auth_id="logu_id",
				path="*"
			)
	},
    entity=@Entity(
		permissions=@Access(
				forbidden={
						@AccessRelation(targets="default_target;", rules="GUESTS;" )	
				}
		)
	)
)

public class componentLogUsers extends action implements i_action, Serializable{
	private static final long serialVersionUID = 6534122783978835682L;





public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {



	HashMap h_user_container = (HashMap)bsController.getFromLocalContainer(util_usersInSession.CONST_APP_USER_CONTAINER);
	if(h_user_container==null) h_user_container = new HashMap();
	if(this.getMiddleAction()==null) this.setMiddleAction("");
	if(this.getMiddleAction().equals("view_mess")){
		return new redirects("/jsp/ajax/add_LogUserMess.jsp?session_id="+this.get("session_id"));
	}
	if(this.getMiddleAction().equals("add_mess")){
		try{
			message mess = new message();
			mess.setTYPE((String)this.get("type_mess"));
			auth_init auth = this.getCurrent_auth();
			mess.setDESC_MESS("From ["+auth.get_userDesc()+"]"+auth.get_user_ip()+" <br>" + (String)this.get("txcontent"));

			if(this.get("session_id").toString().equals("0")){
				Vector elements = new Vector(h_user_container.keySet());

				for(int i=0;i<elements.size();i++){
					String key = (String)elements.get(i);
					HttpSession current_session = (HttpSession)h_user_container.get(key);
					if(current_session!=null){
						Vector $listmessages = (Vector)current_session.getAttribute(bsController.CONST_BEAN_$LISTMESSAGE);
						if($listmessages==null){
							$listmessages = new Vector();
							current_session.setAttribute(bsController.CONST_BEAN_$LISTMESSAGE,$listmessages);
						}
						$listmessages.add(mess);
					}
				}

			}else{
				HttpSession current_session = (HttpSession)h_user_container.get(this.get("session_id").toString());
				if(current_session!=null){
					Vector $listmessages = (Vector)current_session.getAttribute(bsController.CONST_BEAN_$LISTMESSAGE);
					if($listmessages==null){
						$listmessages = new Vector();
						current_session.setAttribute(bsController.CONST_BEAN_$LISTMESSAGE,$listmessages);
					}
					$listmessages.add(mess);
				}
			}
		}catch(Exception e){
		}

	}
	if(this.getMiddleAction().equals("remove")){
		try{
			HttpSession current_session = (HttpSession)h_user_container.get(this.get("session_id").toString());
			auth_init auth =  (auth_init)current_session.getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION);
			auth.reimposta();
			auth.set_logged(false);
			auth.set_ruolo("guest");
			auth.set_user("guest");
			auth.set_userDesc("guest");
			auth.set_matricola("guest");
			auth.set_language("it");
			h_user_container.remove(this.get("session_id").toString());
		}catch(Exception e){
		}
	}
	if(this.getMiddleAction().equals("clear")){
		h_user_container.clear();
	}
		Vector elements = new Vector(h_user_container.keySet());
		int i=0;
		while(i<elements.size()){
			String key = (String)elements.get(i);
			try{
				HttpSession current_session = (HttpSession)h_user_container.get(key);
				auth_init current_auth = (auth_init)current_session.getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION);
				elements.set(i,current_auth);
				i++;
			}catch(Exception e){
				h_user_container.remove(key);
				elements.remove(i);
			}
		}
		request.setAttribute("elements", elements);
	return new redirects(get_infoaction().getRedirect());
}

}
