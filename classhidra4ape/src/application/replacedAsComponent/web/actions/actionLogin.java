package application.replacedAsComponent.web.actions; 


import it.classhidra.core.controller.*;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.core.tool.exception.message;
import it.classhidra.core.tool.jaas_authentication.info_user;
import it.classhidra.core.tool.jaas_authentication.load_users;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_usersInSession;
import it.classhidra.framework.web.beans.option_element;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
	if(	get_bean().getMiddleAction()!=null &&
		(get_bean().getMiddleAction().equals("undefined") || get_bean().getMiddleAction().equals("reload"))
		)
			get_bean().setMiddleAction("");	
	

	
		if(	!get_bean().get("user").equals("") &&
			!get_bean().get("password").equals("") &&
			get_bean().getMiddleAction().equals("enter")){
			if(get_bean().get("user").equals("") && get_bean().get("password").equals("")) get_bean().set("user","anonimouse");
	
			login_Non_JAAS(auth, (String)get_bean().get("user"), (String)get_bean().get("password"), request);
		if(auth==null || !auth.is_logged()){
			new bsControllerMessageException("error_2",request,null,iStub.log_INFO,new String[]{(String)get_bean().get("user")});
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
		
	try{
			
		if(!((String)get_bean().get("prev_user")).equals(get_bean().get("user")+"."+get_bean().get("password"))){
			get_bean().set("group",""); 
			get_bean().set("prev_user",get_bean().get("user")+"."+get_bean().get("password"));
		}		
	}catch(Exception e){
	}

	
	

	if(	get_bean().getMiddleAction()!=null &&
		get_bean().getMiddleAction().equals("")){
		if(	!get_bean().get("user").equals("") &&
			!get_bean().get("password").equals("") &&
			((Vector)get_bean().get("groups")).size()==0)
			get_bean().set(bsController.CONST_ID_$MIDDLE_ACTION,"groups");
		

	}
	
	if(	get_bean().getMiddleAction()!=null &&
		get_bean().getMiddleAction().equals("groups")){	
		if(get_bean().get("user").equals("") && get_bean().get("password").equals("")) get_bean().set("user","anonimouse");

			login_Non_JAAS(auth, (String)get_bean().get("user"), (String)get_bean().get("password"), request);
		if(auth==null || !auth.is_logged()){
			new bsControllerMessageException("error_2",request,null,iStub.log_INFO,new String[]{(String)get_bean().get("user")});
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

	
	if(	get_bean().getMiddleAction()!=null &&
		get_bean().getMiddleAction().equals("clear")){
		get_bean().set("user","");
		get_bean().set("password","");
		get_bean().set("group","");
		get_bean().set("groups",new Vector());
		auth.set_logged(false);
		HashMap fromSession = (HashMap)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
		if(fromSession!=null) fromSession.remove("formMinimizer");
		return new redirects(get_infoaction().getRedirect());
	}

	if(	get_bean().getMiddleAction()!=null &&
		get_bean().getMiddleAction().equals("lang")){
		try{
			auth.set_language((String)get_bean().get("lang"));
		}catch(Exception e){
		}
		return new redirects(get_infoaction().getRedirect());
	}
	
	
	if(	get_bean().getMiddleAction()!=null &&
		!get_bean().getMiddleAction().equals("") && 
		!get_bean().getMiddleAction().equals("change_rule") &&
		!get_bean().getMiddleAction().equals("enter")){
		try{
			get_bean().set("lang",auth.get_language());
			auth.set_ruolo((String)get_bean().get("group"));			
			auth.get_authentication_filter().validate_actionPermittedForbidden(auth);		
		}catch(Exception e){
		}
		String redAction = get_bean().getMiddleAction();
		get_bean().setMiddleAction("");	
		request.getSession().removeAttribute(bsController.CONST_BEAN_$ONLYINSSESSION);

		return new redirects("content");

	}
	if(	get_bean().getMiddleAction()!=null &&
		get_bean().getMiddleAction().equals("change_rule")){
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


private boolean login_Non_JAAS(auth_init auth,String userG, String password, HttpServletRequest request){
    try {
	    String user = userG;
	    String pass = password;

    

	    loadUser_config();


	    info_user _user = (info_user)((load_users)bsController.getUser_config()).get_user(user,pass);
	    if(_user!=null){

	    	auth.set_user(_user.getName());
	    	auth.set_userDesc(_user.getDescription());
	    	auth.set_ruolo(_user.getGroup());
	    	auth.set_language(_user.getLanguage());
	    	auth.set_matricola(_user.getMatriculation());
	    	auth.set_target(_user.getTarget().replace(';','^'));
	    	auth.get_target_property().put(bsConstants.CONST_AUTH_TARGET_ISTITUTION, auth.get_target());
	    	util_usersInSession.addInSession(auth, request, new Date());	    	
			new bsControllerException(
					auth.get_user()+":"+auth.get_matricola()+":"+auth.get_user_ip()+" is logged ",						
					iStub.log_INFO);	
	    	
			new bsControllerException(
					auth.get_user()+":"+auth.get_matricola()+":"+auth.get_user_ip()+" is logged ",						
					iStub.log_INFO);	

	        try {
	            Properties props = new Properties();
	            Session session = Session.getDefaultInstance(props, null);

	            Message msg = new MimeMessage(session);
	            msg.setFrom(new InternetAddress("svyatoslav.urbanovych@gmail.com", "Admin ClassHidra4api"));
	            msg.addRecipient(Message.RecipientType.TO,
	                             new InternetAddress("classhidra@gmail.com", "ClassHidra4app Messager"));
	            msg.setSubject("Message from LOGIN SERVICES");
	            msg.setText(auth.get_user()+":"+auth.get_matricola()+":"+auth.get_user_ip()+" is logged ");
	            Transport.send(msg);

	        }catch(Exception e){
			}			
			
	    }else{
	    	
	    	auth.set_logged(false);	
	    	return false;
	    }
	


    } catch (Exception e) {
    	bsController.writeLog("Login Exception "+ e, iStub.log_FATAL);
    	message mess = new message();
    		mess.setTYPE("E");
    		mess.setDESC_MESS("JAAS Exception "+ e);

    	auth.set_logged(false);	
    	return false;
    	
    }	
    auth.set_logged(true);
	return true;	
	
}

 
private void  loadUser_config() {
	if(bsController.getUser_config()==null){
		bsController.setUser_config(new load_users());
			try{
				((load_users)bsController.getUser_config()).setReadError(false);
					((load_users)bsController.getUser_config()).load_from_resources();
					if(((load_users)bsController.getUser_config()).isReadError()) bsController.setUser_config(null);
			}catch(Exception je){
				bsController.setUser_config(null);
			}
		}
	}
}