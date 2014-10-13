package it.classhidra.core.tool.util;


import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.info_navigation;
import it.classhidra.core.init.auth_init;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class util_usersInSession {
	public final static String CONST_APP_USER_CONTAINER = "APP_USER_CONTAINER";
	public final static String CONST_LOGIN_TIME = "LOGIN_TIME";
	public final static String CONST_LOGIN_IP = "LOGIN_IP";
	public final static String CONST_LASTUSE_TIME = "LASTUSE_TIME";
	public final static String CONST_LASTUSE_ACTION = "LASTUSE_ACTION";
	public final static String CONST_LOGIN_SESSION_ID = "LOGIN_SESSION_ID";

	public static void addInSession(auth_init auth, HttpServletRequest request,Date loginDate){
/*		
		try{
			if(auth==null){
				try{
					auth = bsController.checkAuth_init(request);
				}catch(Exception e){			
				}
			}
			if(auth!=null && auth.is_logged() && !auth.get_matricola().equals("guest")){
				HashMap h_user_container = (HashMap)bsController.getFromLocalContainer(CONST_APP_USER_CONTAINER);
				if(h_user_container==null){
					h_user_container = new HashMap();
					bsController.setToLocalContainer(CONST_APP_USER_CONTAINER,h_user_container);
				}
				HashMap user_instance = (HashMap)h_user_container.get(auth.get_matricola());
				if(user_instance==null){
					user_instance = new HashMap();
					h_user_container.put(auth.get_matricola(), user_instance);
				}
				if(user_instance.get(request.getSession().getId())==null)
					user_instance.put(request.getSession().getId(),auth);
	
				
				((auth_init)user_instance.get(request.getSession().getId())).get_user_property().put(CONST_LASTUSE_TIME, new Date());
				((auth_init)user_instance.get(request.getSession().getId())).get_user_property().put(CONST_LOGIN_IP, auth.get_user_ip());
	
				if(loginDate!=null)
					((auth_init)user_instance.get(request.getSession().getId())).get_user_property().put(CONST_LOGIN_TIME, new Date());
				info_navigation	formInfoNavigation	= (info_navigation)request.getSession().getAttribute(bsController.CONST_BEAN_$NAVIGATION);
				if(formInfoNavigation!=null){
					formInfoNavigation = formInfoNavigation.last();
					((auth_init)user_instance.get(request.getSession().getId())).get_user_property().put(CONST_LASTUSE_ACTION, formInfoNavigation.getLastChildRedirect().getDescr());
				} else ((auth_init)user_instance.get(request.getSession().getId())).get_user_property().put(CONST_LASTUSE_ACTION, request.getAttribute(bsController.CONST_ID));
			}
		}catch (Exception e) {
		}
*/		
		try{
			if(auth==null){
				try{
					auth = bsController.checkAuth_init(request);
				}catch(Exception e){			
				}
			}
			if(auth!=null && auth.is_logged() && !auth.get_matricola().equals("guest")){
				HashMap h_user_container = (HashMap)bsController.getFromLocalContainer(CONST_APP_USER_CONTAINER);
				if(h_user_container==null){
					h_user_container = new HashMap();
					bsController.setToLocalContainer(CONST_APP_USER_CONTAINER,h_user_container);
				}
				HttpSession session_instance = (HttpSession)h_user_container.get(request.getSession().getId());
				if(session_instance==null){
					h_user_container.put(request.getSession().getId(), request.getSession());
				}

				
				auth.get_user_property().put(CONST_LASTUSE_TIME, new Date());
				
	
				if(loginDate!=null) auth.get_user_property().put(CONST_LOGIN_TIME, loginDate);
				else if(auth.get_user_property().get(CONST_LOGIN_TIME)==null) auth.get_user_property().put(CONST_LOGIN_TIME, new Date());
				if(auth.get_user_property().get(CONST_LOGIN_IP)==null) auth.get_user_property().put(CONST_LOGIN_IP, auth.get_user_ip());
				if(auth.get_user_property().get(CONST_LOGIN_SESSION_ID)==null) auth.get_user_property().put(CONST_LOGIN_SESSION_ID, request.getSession().getId());
				
				info_navigation	formInfoNavigation	= (info_navigation)request.getSession().getAttribute(bsController.CONST_BEAN_$NAVIGATION);
				if(formInfoNavigation!=null){
					formInfoNavigation = formInfoNavigation.last();
					auth.get_user_property().put(CONST_LASTUSE_ACTION, formInfoNavigation.getLastChildRedirect().getDescr());
				} else auth.get_user_property().put(CONST_LASTUSE_ACTION, request.getAttribute(bsController.CONST_ID));
			}
		}catch (Exception e) {
		}		
	}
	
	public static void removeFromSession(auth_init auth, HttpServletRequest request){
/*		
		try{
			if(auth==null){
				try{
					auth = bsController.checkAuth_init(request);
				}catch(Exception e){			
				}
			}

			HashMap h_user_container = (HashMap)bsController.getFromLocalContainer(CONST_APP_USER_CONTAINER);
			if(h_user_container!=null){
				HashMap user_instance = (HashMap)h_user_container.get(auth.get_matricola());
				if(user_instance!=null){
					user_instance.remove(request.getSession().getId());
					if(user_instance.size()==0) h_user_container.remove(auth.get_matricola());
				}
			}

		}catch (Exception e) {

		}
*/	
		try{
			if(auth==null){
				try{
					auth = bsController.checkAuth_init(request);
				}catch(Exception e){			
				}
			}

			HashMap h_user_container = (HashMap)bsController.getFromLocalContainer(CONST_APP_USER_CONTAINER);
			String session_id = request.getSession().getId();
			if(h_user_container!=null){
				HttpSession session_instance = (HttpSession)h_user_container.get(session_id);
				if(session_instance!=null){
					try{
						session_instance.invalidate();
					}catch(Exception einv){				
					}	
				}
				h_user_container.remove(session_id);
			}
		}catch (Exception e) {
		}
		
	}
	
}
