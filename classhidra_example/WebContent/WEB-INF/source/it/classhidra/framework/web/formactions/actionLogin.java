package it.classhidra.framework.web.formactions;

import it.classhidra.core.controller.*;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.framework.web.beans.option_element;



import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.*;
import javax.servlet.*;



public class actionLogin extends action implements i_action, Serializable{
	private static final long serialVersionUID = -7900425554873526553L;

public actionLogin(){
	super();
}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	auth_init auth=(auth_init)request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION);
	if(auth==null) auth = new auth_init();
	if(	get_bean().get(bsController.CONST_ID_$MIDDLE_ACTION)!=null &&
		get_bean().get(bsController.CONST_ID_$MIDDLE_ACTION).equals("undefined"))
			get_bean().set(bsController.CONST_ID_$MIDDLE_ACTION,"");

	boolean fromTicker=false;

	if(	get_bean().get("user").equals("") &&
		get_bean().get("password").equals("") &&
		get_bean().get("group").equals("")){
		try{

			if(auth.readTicker(request)){
				if(auth.get_user()!=null){
					get_bean().set("user",auth.get_user());
					get_bean().set("password","**********");
					get_bean().set("group",auth.get_ruolo());
					fromTicker=true;
				}
			}
		}catch(Exception e){
		}

	}
	if(fromTicker){
		if(!((String)get_bean().get("prev_user")).equals(get_bean().get("user")+"."+get_bean().get("password")))
			get_bean().set("prev_user",get_bean().get("user")+"."+get_bean().get("password"));
		Vector groups = (Vector)get_bean().get("groups");
		groups.clear();
		StringTokenizer st = new StringTokenizer(auth.get_ruoli(),";");
		while(st.hasMoreTokens()){
			String current = st.nextToken();
			groups.add(new option_element(current,current));
		}

	}else{
		try{

			if(!((String)get_bean().get("prev_user")).equals(get_bean().get("user")+"."+get_bean().get("password"))){
				get_bean().set("group","");
				get_bean().set("prev_user",get_bean().get("user")+"."+get_bean().get("password"));
			}
		}catch(Exception e){
		}
	}



	if(	get_bean().get(bsController.CONST_ID_$MIDDLE_ACTION)!=null &&
		get_bean().get(bsController.CONST_ID_$MIDDLE_ACTION).equals("")){
		if(	!get_bean().get("user").equals("") &&
			!get_bean().get("password").equals("") &&
			((Vector)get_bean().get("groups")).size()==0)
			get_bean().set(bsController.CONST_ID_$MIDDLE_ACTION,"groups");


	}

	if(	get_bean().get(bsController.CONST_ID_$MIDDLE_ACTION)!=null &&
		get_bean().get(bsController.CONST_ID_$MIDDLE_ACTION).equals("groups")){
		if(get_bean().get("user").equals("") && get_bean().get("password").equals("")) get_bean().set("user","anonimouse");
//		if(auth!=null && !auth.is_logged()){
			auth.init((String)get_bean().get("user"),(String)get_bean().get("password"),request);
//		}
		if(auth==null || !auth.is_logged()){
			new bsControllerMessageException("error_2",request,null,iStub.log_INFO);
			get_bean().set("group","");
			get_bean().set("groups",new Vector());
		}else{
			Vector groups = (Vector)get_bean().get("groups");
			groups.clear();
			StringTokenizer st = new StringTokenizer(auth.get_ruolo(),";");
			while(st.hasMoreTokens()){
				String current = st.nextToken();
				groups.add(new option_element(current,current));
			}
			get_bean().set("lang",auth.get_language());
		}

		return new redirects(get_infoaction().getRedirect());
	}


	if(	get_bean().get(bsController.CONST_ID_$MIDDLE_ACTION)!=null &&
		get_bean().get(bsController.CONST_ID_$MIDDLE_ACTION).equals("clear")){
		get_bean().set("user","");
		get_bean().set("password","");
		get_bean().set("group","");
		get_bean().set("groups",new Vector());
		response.addCookie(new Cookie(bsController.CONST_AUTH_TICKER,""));
		auth.set_logged(false);
		return new redirects(get_infoaction().getRedirect());
	}

	if(	get_bean().get(bsController.CONST_ID_$MIDDLE_ACTION)!=null &&
		get_bean().get(bsController.CONST_ID_$MIDDLE_ACTION).equals("lang")){
		try{
			auth.set_language((String)get_bean().get("lang"));
		}catch(Exception e){
		}
		return new redirects(get_infoaction().getRedirect());
	}


	if(	get_bean().get(bsController.CONST_ID_$MIDDLE_ACTION)!=null &&
		!get_bean().get(bsController.CONST_ID_$MIDDLE_ACTION).equals("") &&
		!get_bean().get(bsController.CONST_ID_$MIDDLE_ACTION).equals("change_rule")){
		try{
			get_bean().set("lang",auth.get_language());
			auth.set_ruolo((String)get_bean().get("group"));
			auth.saveTicker(response);
			auth.get_authentication_filter().validate_actionPermittedForbidden(auth);
		}catch(Exception e){
		}
		bsController.clearOnlySession(request);
		return new redirects("/actions/"+(String)get_bean().get(bsController.CONST_ID_$MIDDLE_ACTION)+bsController.getAppInit().get_extention_do());

	}
	if(	get_bean().get(bsController.CONST_ID_$MIDDLE_ACTION)!=null &&
		get_bean().get(bsController.CONST_ID_$MIDDLE_ACTION).equals("change_rule")){
			try{
				get_bean().set("lang",auth.get_language());
				auth.set_ruolo((String)get_bean().get("group"));
				auth.saveTicker(response);
				auth.get_authentication_filter().validate_actionPermittedForbidden(auth);
			}catch(Exception e){
			}
		}
	return new redirects(get_infoaction().getRedirect());

}
}