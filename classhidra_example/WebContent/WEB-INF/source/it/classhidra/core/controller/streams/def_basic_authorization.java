package it.classhidra.core.controller.streams;


import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_stream;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.controller.stream;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.integration.i_integration;
import it.classhidra.core.tool.jaas_authentication.info_target;
import it.classhidra.core.tool.jaas_authentication.info_user;
import it.classhidra.core.tool.jaas_authentication.load_users;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_usersInSession;
//import sun.misc.BASE64Decoder;

public class def_basic_authorization extends stream implements i_stream{

	private static final long serialVersionUID = 1L;
	



	public redirects streamservice_enter(HttpServletRequest request,HttpServletResponse response) throws bsControllerException {

		auth_init auth = (auth_init)request.getSession().getAttribute(bsController.CONST_REST_$AUTHENTIFICATION); 
		
		if(auth==null || !auth.is_logged()){
			try{
				if(auth==null)
					auth=new auth_init();
				auth = serviceRest(auth, request, response);				
			}catch(Exception e){
				new bsControllerException("REST Authentication -> System error"+e.toString(), iStub.log_ERROR);
			}
		}else if(auth!=null && auth.is_logged())
			return super.streamservice_enter(request, response);

		if(auth==null || !auth.is_logged())
			return new redirects("");
		return super.streamservice_enter(request, response);
	}

	public redirects streamservice_exit(HttpServletRequest request, HttpServletResponse response) throws bsControllerException {		
		return super.streamservice_exit(request, response);
	}

	
	private auth_init serviceRest(auth_init auth,  HttpServletRequest request, HttpServletResponse response)throws Exception {
		PrintWriter out = response.getWriter();
		Enumeration<?> headerNames = request.getHeaderNames();
		
		
		String header_auth = null; 
		
		if(auth==null || !auth.is_logged()){
			while (headerNames.hasMoreElements()) {
				String headerName = (String) headerNames.nextElement();			

				if(headerName.equalsIgnoreCase("authorization")) 
					header_auth = request.getHeader(headerName);
			}			
		}
			
		
		
		if(header_auth == null){
		        response.setStatus(401);
		        response.setHeader("WWW-Authenticate", "basic realm=\"Auth (" + request.getSession().getCreationTime() + ")\"" );
		        request.getSession().setAttribute("checkauth", Boolean.TRUE);
		        out.println("Login Required");
		        return auth;
		}
		
		try{			
			if(header_auth!=null && header_auth.indexOf("Basic ")==0){
				header_auth=header_auth.replace("Basic ", "");
				header_auth = new String(DatatypeConverter.parseBase64Binary(header_auth));
				StringTokenizer st = new StringTokenizer(header_auth,":");

				String user = st.nextToken();
				String password = st.nextToken();
				
				HashMap<String,Object> form = new HashMap<String, Object>();
				form.put("user", user);
				form.put("password", password);
				form.put("auth", auth);
				
				auth = (auth_init)getVerificator().operation(i_integration.o_FINDFORMLIST, form);
				
				
				
				if(auth==null || !auth.is_logged()){
					request.getSession().removeAttribute("auth");
			        response.setStatus(401);
			        response.setHeader("WWW-Authenticate", "basic realm=\"Auth (" + request.getSession().getCreationTime() + ")\"" );
			        request.getSession().setAttribute("checkauth", Boolean.TRUE);
			        out.println("Login Required");
			        return auth;					
				}else{
			    	util_usersInSession.addInSession(auth, request,  new Date());
					request.getSession().setAttribute(bsController.CONST_REST_$AUTHENTIFICATION,auth);
				}
			}
		}catch(Exception e){
			new bsControllerException(e, iStub.log_ERROR);
		}
		

		return auth;
	
	}

	public i_integration getVerificator() {
		return new i_integration() {
			
			public Object operation(String oper, Object form) throws Exception {
				if(oper!=null && oper.equals(o_FINDFORMLIST)){
					if(form!=null && form instanceof Map){
						String user = (String)((Map<?,?>)form).get("user");
						String password = (String)((Map<?,?>)form).get("password");
						auth_init auth = (auth_init)((Map<?,?>)form).get("auth");
						
						if(bsController.getUser_config()==null){
							bsController.setUser_config(new  load_users());
							try{
								((load_users)bsController.getUser_config()).setReadError(false);
								((load_users)bsController.getUser_config()).init();
								if(((load_users)bsController.getUser_config()).isReadError()) 
									((load_users)bsController.getUser_config()).load_from_resources();
								if(((load_users)bsController.getUser_config()).isReadError()) 
									((load_users)bsController.getUser_config()).init(bsController.getAppInit().get_path_config()+bsController.CONST_XML_USERS);
								if(((load_users)bsController.getUser_config()).isReadError()){
									((load_users)bsController.getUser_config()).setReadError(false);
									((load_users)bsController.getUser_config()).load_from_resources();
									if(((load_users)bsController.getUser_config()).isReadError()) bsController.setUser_config(null);
								}
							}catch(bsControllerException je){
								bsController.setUser_config(null);
							}
						}
						
					    info_user iuser = null;

					    if(user!=null && password!=null){  
					    	try{
					    		iuser = (info_user)((load_users)bsController.getUser_config()).get_user(user,password);	 
					    	}catch(Exception e){
					    		new bsControllerException(e, iStub.log_ERROR);
					    	}
					    }

						    
					    if(iuser!=null){
						    auth.set_user(iuser.getName());
						    auth.set_userDesc(iuser.getDescription());
						    auth.set_ruolo(iuser.getGroup());
					    	auth.set_language(iuser.getLanguage());
						    auth.set_matriculation(iuser.getMatriculation());
						    auth.set_target(iuser.getTarget().replace(';','^'));
						    auth.set_mail(iuser.getEmail());
						    	
						    auth.get_target_property().put(bsConstants.CONST_AUTH_TARGET_ISTITUTION, auth.get_target());
						    auth.set_logged(true);
						    auth.setInfouser(iuser);
						    try{
						    	info_target itarget = (info_target)iuser.getV_info_targets().get(0);
						    	auth.setInfotarget(itarget);
						    	auth.get_target_property().putAll(itarget.getProperties());
						    }catch(Exception e){	    		
						    }
						    auth.get_user_property().putAll(iuser.getProperties());

						    	
							new bsControllerException(
									auth.get_user()+":"+auth.get_matricola()+":"+auth.get_user_ip()+" is logged ",						
										iStub.log_INFO);	
							
							return auth;
					    }

					}else
						return null;
						
				}
				return null;
			}
		};
	}

}
