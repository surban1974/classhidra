package it.classhidra.core.controller.streams;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.controller.i_stream;
import it.classhidra.core.controller.info_action;
import it.classhidra.core.controller.stream;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsControllerException;

public class def_control_permission extends stream implements i_stream{

	private static final long serialVersionUID = 1L;


	public redirects streamservice_enter(HttpServletRequest request,HttpServletResponse response) throws bsControllerException {
		String redirectURI=null;
		auth_init auth = null;
		try{
			auth = bsController.checkAuth_init(request);
		}catch(Exception e){
		}
		if(	auth==null || auth.get_authentication_filter()==null) return super.streamservice_enter(request, response);


		String id_action = (String)request.getAttribute(bsController.CONST_ID);

		info_action i_a = (info_action)bsController.getAction_config().get_actions().get(id_action);
		if(i_a!=null && (i_a.getProperty("allway").equalsIgnoreCase("public") || i_a.getProperty("always").equalsIgnoreCase("public")))
			return super.streamservice_enter(request, response);

		if(	auth!=null &&
			auth.get_authentication_filter()!=null &&
			!auth.get_authentication_filter().check_actionIsPermitted(auth,id_action)){

			redirectURI = service_AuthRedirect(id_action,request.getSession().getServletContext(),request, response);
			return new redirects(redirectURI);
		}

		String id_complete = (String)request.getAttribute(bsController.CONST_ID_COMPLETE);
		if(id_complete==null || id_complete.equals(id_action))
			return super.streamservice_enter(request, response);

		if(	auth!=null &&
			auth.get_authentication_filter()!=null &&
			!auth.get_authentication_filter().check_actionIsPermitted(auth,id_complete)){
			redirectURI = service_AuthRedirect(id_complete,request.getSession().getServletContext(),request, response);
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
		String id_complete = (String)request.getAttribute(bsController.CONST_ID_COMPLETE);
		i_action action_instance = (i_action)request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTION);
		if(action_instance!=null && action_instance.get_infoaction()!=null && (action_instance.get_infoaction().getProperty("allway").equalsIgnoreCase("public") || action_instance.get_infoaction().getProperty("always").equalsIgnoreCase("public"))){
		}else{
			if(	action_instance!=null &&
				auth!=null &&
				auth.get_authentication_filter()!=null &&
				!auth.get_authentication_filter().check_redirectIsPermitted(auth,action_instance)){

				redirectURI = service_AuthRedirect(id_complete,request.getSession().getServletContext(),request, response);
				return new redirects(redirectURI);
			}
		}

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
}
