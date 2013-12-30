package it.classhidra.core.controller.streams;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_stream;
import it.classhidra.core.controller.info_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.controller.stream;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.core.tool.exception.message;
import it.classhidra.core.tool.log.stubs.iStub;

import java.util.HashMap;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class def_control_middleaction extends stream implements i_stream{

	private static final long serialVersionUID = 1L;


	public redirects streamservice_enter(HttpServletRequest request,HttpServletResponse response) throws bsControllerException {
		String redirectURI=null;
		auth_init auth = null; 
		try{
			auth = bsController.checkAuth_init(request);
		}catch(Exception e){			
		}
		
		if(	auth==null ||
			auth.get_target()==null ||
			auth.get_target().trim().equals("") ||
			auth.get_ruolo()==null ||
			auth.get_ruolo().trim().equals("")){
			return super.streamservice_enter(request, response);
		}
		
		String id_action = (String)request.getAttribute(bsController.CONST_ID);
		if(id_action==null)
			return super.streamservice_enter(request, response);
		
		info_action i_a = (info_action)bsController.getAction_config().get_actions().get(id_action);
		if(i_a==null){
			return super.streamservice_enter(request, response);
		}

		if(checkMiddleAction(id_action, auth, request, response)){		
			new bsControllerMessageException(new message("E", "ch_error_8", "Insufficient permissions for that type of operation."), request, iStub.log_ERROR);
			redirectURI=id_action+bsController.getAppInit().get_extention_do()+"?middleAction=error_permission_point";
			if(redirectURI!=null)
				return new redirects(redirectURI);
			
			redirectURI = service_AuthRedirect(id_action,request.getSession().getServletContext(),request, response);
			return new redirects(redirectURI);
		}
		
		String id_complete = (String)request.getAttribute(bsController.CONST_ID_COMPLETE);
		if(id_complete==null || id_complete.equals(id_action))
			return super.streamservice_enter(request, response);
		
		if(checkMiddleAction(id_complete, auth, request, response)){		
			new bsControllerMessageException(new message("E", "ch_error_8", "Insufficient permissions for that type of operation."), request, iStub.log_ERROR);
			redirectURI=id_action+bsController.getAppInit().get_extention_do()+"?middleAction=error_permission_point";
			if(redirectURI!=null)
				return new redirects(redirectURI);
			
			redirectURI = service_AuthRedirect(id_complete,request.getSession().getServletContext(),request, response);
			return new redirects(redirectURI);
		}
		return super.streamservice_enter(request, response);
		
	}

	public redirects streamservice_exit(HttpServletRequest request, HttpServletResponse response) throws bsControllerException {
		return super.streamservice_exit(request, response);
	}

	
	private String service_AuthRedirect(String id_action,ServletContext servletContext, HttpServletRequest request, HttpServletResponse response){
		String redirectURI="";
		try{
			if(bsController.getAction_config()==null ||
				bsController.getAction_config().getError()==null ||
				bsController.getAction_config().getError().equals("")){
				redirectURI="";
			}else redirectURI = bsController.getAction_config().getError();

		}catch(Exception ex){
			bsController.writeLog(request, "Controller authentication error. Action: ["+id_action+"] URI: ["+redirectURI+"]");		
		}	
		return redirectURI;
	}
	
	
	private boolean checkMiddleAction(String id_entity,auth_init auth, HttpServletRequest request, HttpServletResponse response){
		String middle_action = request.getParameter(bsController.CONST_ID_$MIDDLE_ACTION);
		if(middle_action!=null && middle_action.trim().equals("")) middle_action="_blank";
		

		HashMap m_forbidden =null;
		HashMap m_allowed =null;
		try{
			m_forbidden = (HashMap)((HashMap)bsController.getAuth_config().get_mtargets().get(auth.get_target())).get(auth.get_ruolo());
		}catch(Exception e){			
		}
		try{
			m_allowed = (HashMap)((HashMap)bsController.getAuth_config().get_mtargets_allowed().get(auth.get_target())).get(auth.get_ruolo());
		}catch(Exception e){			
		}

		if(m_forbidden==null){
			return false;
		}
		
		HashMap mactions = (HashMap)m_forbidden.get(id_entity);
		if(mactions==null){
			mactions = (HashMap)m_forbidden.get("*");
		}
		if(mactions==null){
			return false;
		}

		boolean b_forbidden=false;
		if(mactions.get("*")!=null)
			b_forbidden=true;
		
		if(!b_forbidden && middle_action!=null && mactions.get(middle_action)!=null)
			b_forbidden=true;
		
		if(!b_forbidden){
			return false;
		}
		
		if(m_allowed!=null){
			HashMap mactions_allowed = (HashMap)m_allowed.get(id_entity);
			if(mactions_allowed==null){
				mactions_allowed = (HashMap)m_allowed.get("*");
			}
			if(mactions_allowed!=null){
				if(	mactions_allowed.get("*")!=null ||
					(middle_action!=null && mactions_allowed.get(middle_action)!=null)
				)
					return false;
				
			}
		}
		
		return true;
		
	}
}
