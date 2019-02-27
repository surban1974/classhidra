package it.classhidra.core.tool.util;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class util_auth {

	public static void init(auth_init auth, String user, String password, Object obj_request){
		if(obj_request==null || !(obj_request instanceof HttpServletRequest)) return;
		HttpServletRequest request = (HttpServletRequest)obj_request;
		if(user==null || password==null || user.equals("") ){
			auth.reimposta();
			return; 
		}
		auth.set_user(user);
		auth.get_manager().login_JAAS(auth,password,request);	
		if(!auth.is_logged()){
			auth.init();
			auth.get_manager().login_JAAS(auth,password,request);	
		}
	}

	public static void init(auth_init auth, Object obj_request) throws bsException{
		if(obj_request==null || !(obj_request instanceof HttpServletRequest)) return;
		HttpServletRequest request = (HttpServletRequest)obj_request;
		
		auth.set_user(
				(request.getHeader("USER")==null)?
						(request.getHeader("user")==null)?"Anonimous":request.getHeader("USER")
				:request.getHeader("USER")
		);
		auth.set_ruolo(
				(request.getHeader("RUOLO")==null)?
						(request.getHeader("GROUP")==null)?
								(request.getHeader("group")==null)?
								""
								:request.getHeader("group")
						:request.getHeader("GROUP")
				:request.getHeader("RUOLO")
		);
		
		String _dominio = 	(request.getHeader("DOMINIO")==null)?
								(request.getHeader("DOMAIN")==null)?
										(request.getHeader("domain")==null)?
										""
										:request.getHeader("domain")
								:request.getHeader("DOMAIN")
							:request.getHeader("DOMINIO");
		
		auth.get_target_property().put(bsConstants.CONST_AUTH_TARGET_DOMINO, _dominio);
		auth.get_target_property().put(bsConstants.CONST_AUTH_TARGET_DOMAIN, _dominio);
		
		auth.set_user_ip("["+request.getRemoteAddr()+"] ");
		
		auth.set_risoluzione(
				(request.getParameter("risoluzione")==null || request.getParameter("risoluzione").equals(""))?
						(request.getParameter("risolution")==null || request.getParameter("risolution").equals(""))?
						"1024"
						:request.getParameter("risolution")
				:request.getParameter("risoluzione"));
		

		Enumeration<?> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String headerName = (String)headerNames.nextElement();
			auth.getRequestHeader().put(headerName,request.getHeader(headerName));
		}
		if(auth.getRequestHeader().get("accept-language")!=null){
			String _acc_lang = (String)auth.getRequestHeader().get("accept-language");
			if(_acc_lang.trim().length()>2) _acc_lang = _acc_lang.substring(0,2);	
			auth.set_language(_acc_lang.toUpperCase());
		}
	}
	
	public static boolean readTicker(auth_init auth,Object obj_request){
		if(obj_request==null || !(obj_request instanceof HttpServletRequest)) return false;
		HttpServletRequest request = (HttpServletRequest)obj_request;
		try{
			if(auth.get_manager().readTicker(auth, request)){
				auth.set_logged(true);
				return true;
			}else return false;
		}catch(Exception e){
			return false;
		}
	}

	public static boolean saveTicker(auth_init auth,Object obj_response){
		if(obj_response==null || !(obj_response instanceof HttpServletResponse)) return false;
		HttpServletResponse response = (HttpServletResponse)obj_response;
		
		try{
			return auth.get_manager().saveTicker(auth, response);
		}catch(Exception e){
			new bsException(e,iStub.log_ERROR);
			return false;
		}		
	}

	public static boolean saveFromForm(auth_init auth,i_bean bean, Object obj_request, Object obj_response){
		if(obj_request==null || !(obj_request instanceof HttpServletRequest)) return false;
		if(obj_response==null || !(obj_response instanceof HttpServletResponse)) return false;
		HttpServletRequest request = (HttpServletRequest)obj_request;
		HttpServletResponse response = (HttpServletResponse)obj_response;
		
		try{
			return auth.get_manager().saveFromForm(auth, bean, request, response);
		}catch(Exception e){
			new bsException(e,iStub.log_ERROR);
			return false;
		}
	}	
}
