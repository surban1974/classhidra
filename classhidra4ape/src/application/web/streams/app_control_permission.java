package application.web.streams;


import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_stream;
import it.classhidra.core.controller.info_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.controller.stream;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_usersInSession;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class app_control_permission extends stream implements i_stream{

	private static final long serialVersionUID = 1L;
	public static String CONST_IPCNT_INSERT = "CONST_IPCNT_INSERT";

	public redirects streamservice_enter(HttpServletRequest request,HttpServletResponse response) throws bsControllerException {
		

	
		String redirectURI=null;
		auth_init auth = null; 
		
		

		
		if(request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION)==null){
			auth = new auth_init();
			try{
				auth.init(request);
				if(auth.get_user().equals("")) auth.set_language("en");
				auth.set_ruolo("guest");
				auth.set_user("guest");
				auth.set_matricola("guest");
//				auth.set_language("it");
			}catch(bsException je){}
			request.getSession().setAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION,auth);
		}else{
			auth = (auth_init)request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION);
			if(!auth.is_logged()){
				if(!auth.get_user().equals("guest")) auth.set_language("en");
				auth.set_ruolo("guest");
				auth.set_user("guest");
				auth.set_matricola("guest");
//				auth.set_language("it");
				request.getSession().setAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION,auth);
			}
		}

		
		

		String id_action = (String)request.getAttribute(bsController.CONST_ID);
		
		
		
		if(	auth!=null &&
			auth.get_authentication_filter()!=null &&
			!auth.get_authentication_filter().check_actionIsPermitted(auth,id_action)){
			redirectURI = service_AuthRedirect(id_action,request.getSession().getServletContext(),request, response);
			return new redirects(redirectURI);
		}

		info_action i_a = (info_action)bsController.getAction_config().get_actions().get(id_action);
		if(i_a!=null && !auth.is_logged() && !i_a.getProperty("allway").equals("public")){
			redirectURI = service_ErrorRedirect(id_action,request.getSession().getServletContext(),request, response);
			 new bsControllerMessageException(
						"error_9",
						request,
						null,
						iStub.log_ERROR,
						new Object[]{});
			 return new redirects(redirectURI);
		}
		
		return super.streamservice_enter(request, response);
		
	}

	public redirects streamservice_exit(HttpServletRequest request, HttpServletResponse response) throws bsControllerException {

		String redirectURI=null;
		auth_init auth = null; 
		try{
			auth = bsController.checkAuth_init(request);
		}catch(Exception e){			
		}
		String id_action = (String)request.getAttribute(bsController.CONST_ID);		
		i_action action_instance = (i_action)request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTION);
		if(	action_instance!=null &&
			auth!=null &&
			auth.get_authentication_filter()!=null &&
			!auth.get_authentication_filter().check_redirectIsPermitted(auth,action_instance)){

			redirectURI = service_AuthRedirect(id_action,request.getSession().getServletContext(),request, response);
			return new redirects(redirectURI);
		}
		util_usersInSession.addInSession(auth, request, null);
		return super.streamservice_exit(request, response);
	}

	
	private String service_AuthRedirect(String id_action,ServletContext servletContext, HttpServletRequest request, HttpServletResponse response){
		String redirectURI="";
		try{
			if(bsController.getAction_config()==null ||
				bsController.getAction_config().getAuth_error()==null ||
				bsController.getAction_config().getAuth_error().equals("")){
				redirectURI="";
			}else redirectURI = bsController.getAction_config().getAuth_error();

		}catch(Exception ex){
			bsController.writeLog(request, "Controller authentication error. Action: ["+id_action+"] URI: ["+redirectURI+"]");		
		}	
		return redirectURI;
	}
	private String service_ErrorRedirect(String id_action,ServletContext servletContext, HttpServletRequest request, HttpServletResponse response){
		String redirectURI="";
		try{
			if(bsController.getAction_config()==null ||
				bsController.getAction_config().getAuth_error()==null ||
				bsController.getAction_config().getAuth_error().equals("")){
				redirectURI="";
			}else redirectURI = bsController.getAction_config().getError();

		}catch(Exception ex){
			bsController.writeLog(request, "Controller authentication error. Action: ["+id_action+"] URI: ["+redirectURI+"]");		
		}	
		return redirectURI;
	}
	

	

}
