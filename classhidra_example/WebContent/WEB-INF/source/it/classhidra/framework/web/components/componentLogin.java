package it.classhidra.framework.web.components; 


import it.classhidra.annotation.elements.Action;

import it.classhidra.annotation.elements.ActionMapping;
import it.classhidra.annotation.elements.Bean;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.Redirect;
import it.classhidra.core.controller.*;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.core.tool.exception.message;
import it.classhidra.core.tool.jaas_authentication.info_target;
import it.classhidra.core.tool.jaas_authentication.info_user;
import it.classhidra.core.tool.jaas_authentication.load_users;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_usersInSession;
import it.classhidra.framework.web.beans.option_element;
import it.classhidra.framework.web.integration.i_module_integration;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.*;
import javax.servlet.*;

@ActionMapping (
		redirects={
				@Redirect(	
						path="/jsp/framework/login.jsp",
						descr="Login",
						mess_id="title_fw_Login"
				)
			},
		actions={
				@Action (
						path="login",
						name="formLogin",
						redirect="/jsp/framework/login.jsp",
						navigated="true",
						memoryInSession="true",
						reloadAfterAction="false",
						help="/jsp/help/help_login.html",
						entity=@Entity(
								property="allway:public"
						)						
				)
				
				
		}
)

@Bean (	name="formLogin")






public class componentLogin extends action implements i_action,i_bean, Serializable{
	private static final long serialVersionUID = 1L;

	private String user;
	private String password;
	private String lang;
	private String group;
	private String target;
	private String prev_user;
	private String module_integration;
	
	private Vector groups;
	
	
	public void reimposta(){
		group="";
		target="";
		user="";
		password="";
		lang="IT";
		prev_user="";
		module_integration="";
		groups=new Vector();
	}
	
	
	public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
		
		auth_init auth=(auth_init)request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION);
		if(auth==null) auth = new auth_init();
		if(	middleAction!=null &&
			(middleAction.equals("undefined") || middleAction.equals("reload"))
			)
				middleAction="";	
		
	
		
		if(	user.equals("") &&
			password.equals("") &&
			group.equals("")){
	
				
		} 
			try{
				
				if(!prev_user.equals(user+"."+password)){
					group=""; 
					prev_user=user+"."+password;
				}		
			}catch(Exception e){
			}
	
		
		
	
		if(	middleAction!=null &&
			middleAction.equals("")){
			if(	!user.equals("") &&
				!password.equals("") &&
				groups.size()==0)
				middleAction="groups";
			
	
		}
		
		if(	middleAction!=null &&
			middleAction.equals("groups")){	
			if(user.equals("") && password.equals("")) user="anonimouse";
	
				login_Non_JAAS(auth, user, password, request);
			if(auth==null || !auth.is_logged()){
				new bsControllerMessageException("error_2",request,null,iStub.log_INFO,new String[]{user});
				group="";
				groups=new Vector();
			}else{
				groups.clear();
				StringTokenizer st = new StringTokenizer(auth.get_ruolo(),";");
				while(st.hasMoreTokens()){
					String current = st.nextToken();
					groups.add(new option_element(current,current)); 
				}			
				lang=auth.get_language();
			}	
			
			return new redirects(get_infoaction().getRedirect());
		}
	
		
		if(	middleAction!=null &&
			middleAction.equals("clear")){
			user="";
			password="";
			group="";
			groups=new Vector();
			auth.set_logged(false);
			HashMap fromSession = (HashMap)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
			if(fromSession!=null) fromSession.remove("formMinimizer");
			return new redirects(get_infoaction().getRedirect());
		}
	
		if(	middleAction!=null &&
			middleAction.equals("lang")){
			try{
				auth.set_language(lang);
			}catch(Exception e){
			}
			return new redirects(get_infoaction().getRedirect());
		}
		
		
		if(	middleAction!=null &&
			!middleAction.equals("") && 
			!middleAction.equals("change_rule") &&
			!group.equals("")){
			try{
				lang=auth.get_language();
				auth.set_ruolo(group);			
				auth.get_authentication_filter().validate_actionPermittedForbidden(auth);		
			}catch(Exception e){
			}
			middleAction="";	
			request.getSession().removeAttribute(bsController.CONST_BEAN_$ONLYINSSESSION);
	
			return new redirects("actions/content?t=1");
	
		}
		if(	middleAction!=null &&
			middleAction.equals("change_rule")){
				try{
					lang=auth.get_language();
					auth.set_ruolo(group);			
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
		    info_user _user = null;
		    
		    i_module_integration imi = null;
		    try{
		    	imi = (i_module_integration)Class.forName(module_integration).newInstance();
		    }catch(Exception e){		    	
		    }
	
		    if(imi==null){
		    	loadUser_config();
		    	_user = ((load_users)bsController.getUser_config()).get_user(user,pass);
		    }else{
		    	bean form = new bean();
				form.put("user",user);
				form.put("password",password);
		    	Object result=null;
				try{
					result = imi.operation(i_module_integration.o_FIND, form);
				}catch(Exception e){			
				}
				
				if(result!=null && result instanceof Vector && ((Vector)result).size()>0){
					_user = (info_user)((Vector)result).get(0);
				}
		    }
	
	
		    
		    if(_user!=null){
		    	auth.setInfouser(_user);
		    	auth.set_user(_user.getName());
		    	auth.set_userDesc(_user.getDescription());
		    	auth.set_ruolo(_user.getGroup());
		    	auth.set_language(_user.getLanguage());
		    	auth.set_matricola(_user.getMatriculation());
		    	auth.set_target(_user.getTarget().replace(';','^'));
		    	auth.get_target_property().put(bsConstants.CONST_AUTH_TARGET_ISTITUTION, auth.get_target());
		    	try{
		    		info_target itarget = (info_target)_user.getV_info_targets().get(0);
		    		auth.setInfotarget(itarget);
		    		auth.get_target_property().putAll(itarget.getProperties());
		    	}catch(Exception e){	    		
		    	}
		    	auth.get_user_property().putAll(_user.getProperties());
		    	auth.set_logged(true);
		    	util_usersInSession.addInSession(auth, request, new Date());
		    	
		    	loadOrganizationsVisibleNodes4sql(auth.get_matricola(),auth);
				new bsControllerException(
						auth.get_user()+":"+auth.get_matricola()+":"+auth.get_user_ip()+" is logged ",						
						iStub.log_INFO);	
	
		    }else{
		    	util_usersInSession.removeFromSession(auth, request);
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

	public void loadOrganizationsVisibleNodes4sql(String matr,auth_init auth){
		String result=null;
		try{
			load_organization lo = bsController.getOrg_config();
			info_nodeorganization node = lo.find_m(matr);
			if(node!=null) {
				HashMap nodes = node.getElementsVisible();
				if(nodes.size()>0){
					Vector v_matr = new Vector(nodes.keySet());
					result="";
					for(int i=0;i<v_matr.size();i++){
						result+="'"+v_matr.get(i)+"'";
						if(i<v_matr.size()-1) result+=",";
					}
				}else{
					result+="'"+matr+"'";
				}
			}
		}catch(Exception e){
			
		}

		auth.get_user_property().put(load_organization.CONST_$VISIBLE_NODES4SQL, result);

	}
 
//	private void  loadUser_config() {
//		if(bsController.getUser_config()==null){
//			bsController.setUser_config(new load_users());
//			try{
//				((load_users)bsController.getUser_config()).setReadError(false);
//					((load_users)bsController.getUser_config()).load_from_resources();
//					if(((load_users)bsController.getUser_config()).isReadError()) bsController.setUser_config(null);
//			}catch(Exception je){
//				bsController.setUser_config(null);
//			}
//		}
//	}


	private void  loadUser_config() {
		if(bsController.getUser_config()==null){
			bsController.setUser_config(new  load_users());
				try{
					((load_users)bsController.getUser_config()).setReadError(false);
					((load_users)bsController.getUser_config()).init();
					if(((load_users)bsController.getUser_config()).isReadError()) ((load_users)bsController.getUser_config()).load_from_resources();
					if(((load_users)bsController.getUser_config()).isReadError()) ((load_users)bsController.getUser_config()).init(bsController.getAppInit().get_path_config()+bsController.CONST_XML_USERS);
					if(((load_users)bsController.getUser_config()).isReadError()){
						((load_users)bsController.getUser_config()).setReadError(false);
						((load_users)bsController.getUser_config()).load_from_resources();
						if(((load_users)bsController.getUser_config()).isReadError()) bsController.setUser_config(null);
					}
				}catch(bsControllerException je){
					bsController.setUser_config(null);
				}
			}
		}
	
	public String getPassword() {
		return password;
	}
	public String getUser() {
		return user;
	}
	public void setPassword(String string) {
		password = string;
	}
	public void setUser(String string) {
		user = string;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String string) {
		lang = string;
	}

	public String getGroup() {
		return group;
	}


	public void setGroup(String string) {
		group = string;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String string) {
		target = string;
	}

	public String getPrev_user() {
		return prev_user;
	}

	public void setPrev_user(String string) {
		prev_user = string;
	}

	public Vector getGroups() {
		return groups;
	}

	public void setGroups(Vector vector) {
		groups = vector;
	}


	public String getModule_integration() {
		return module_integration;
	}


	public void setModule_integration(String moduleIntegration) {
		module_integration = moduleIntegration;
	}

}