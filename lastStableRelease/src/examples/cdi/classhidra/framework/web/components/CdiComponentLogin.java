package examples.cdi.classhidra.framework.web.components;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;


import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.ActionCall;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.NavigatedDirective;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bean;
import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.info_nodeorganization;
import it.classhidra.core.controller.load_organization;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.db.db_connection;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.core.tool.exception.message;
import it.classhidra.core.tool.jaas_authentication.info_target;
import it.classhidra.core.tool.jaas_authentication.info_user;
import it.classhidra.core.tool.jaas_authentication.load_users;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_provider;
import it.classhidra.core.tool.util.util_usersInSession;
import it.classhidra.framework.web.beans.option_element;
import it.classhidra.framework.web.integration.i_module_integration;



@Action (
	path="login",
	name="formLogin",
	redirect="/jsp/framework/login.jsp",
	reloadAfterAction="true",
	help="/jsp/help/help_login.html",
	entity=@Entity(
		property="always:public"
	)
)

@NavigatedDirective(memoryContent="true")
//@RequestScoped
public class CdiComponentLogin extends action implements i_action, Serializable{
	private static final long serialVersionUID = 1L;

	private String user;
	private String password;
	private String lang;
	private String group;
	private String target;
	private String prev_user;
	private String module_integration;

	private Vector groups;
	private int panelstatus = 0;
	@ActionCall(name="panelmax")
	public void panelmax(){
		panelstatus=1;
	}
	@ActionCall(name="panelmin")
	public void panelmin(){
		panelstatus=0;
	}
	public int getPanelstatus() {
		return panelstatus;
	}
	public void setPanelstatus(int panelstatus) {
		this.panelstatus = panelstatus;
	}
	


	public CdiComponentLogin(){
		super();
	}


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



	public redirects actionservice(HashMap wsParameters) throws bsControllerException {
		// TODO Auto-generated method stub
		return new redirects(get_infoaction().getRedirect());
	}
	
	public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {


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

				login_Non_JAAS(getCurrent_auth(), user, password, request);
			if(getCurrent_auth()==null || !getCurrent_auth().is_logged()){
				new bsControllerMessageException("error_2",request,null,iStub.log_INFO,new String[]{user});
				group="";
				groups=new Vector();
			}else{
				groups.clear();
				StringTokenizer st = new StringTokenizer(getCurrent_auth().get_ruolo(),";");
				while(st.hasMoreTokens()){
					String current = st.nextToken();
					groups.add(new option_element(current,current));
				}
				lang=getCurrent_auth().get_language();
			}

			return new redirects(get_infoaction().getRedirect());
		}


		if(	middleAction!=null &&
			middleAction.equals("clear")){
			user="";
			password="";
			group="";
			groups=new Vector();
			getCurrent_auth().set_logged(false);
			Map fromSession = bsController.getOnlySession(request);
			if(fromSession!=null) fromSession.remove("formMinimizer");
			return new redirects(get_infoaction().getRedirect());
		}

		if(	middleAction!=null &&
			middleAction.equals("lang")){
			try{
				getCurrent_auth().set_language(lang);
			}catch(Exception e){
			}
			return new redirects(get_infoaction().getRedirect());
		}


		if(	middleAction!=null &&
			!middleAction.equals("") &&
			!middleAction.equals("change_rule") &&
			!group.equals("")){
			try{
				lang=getCurrent_auth().get_language();
				getCurrent_auth().set_ruolo(group);
				getCurrent_auth().get_authentication_filter().validate_actionPermittedForbidden(getCurrent_auth());
			}catch(Exception e){
			}
			middleAction="";
			bsController.clearOnlySession(request);
			
			try{
				((CdiComponentMenuCreator)
						
						((i_action)util_provider.getInstanceFromProvider(
								new String[]{
										bsController.getAction_config().getProvider(),
										bsController.getAppInit().get_context_provider(),
										bsController.getAppInit().get_cdi_provider(),
										bsController.getAppInit().get_ejb_provider()
								},
								"menuCreator",
								CdiComponentMenuCreator.class.getName(),
								request.getSession().getServletContext()
								)
								).asBean()
						).getElement().setInfo_menu(null);
			}catch(Exception e){
			}
			
			return new redirects("actions/content?t=1");

		}
		if(	middleAction!=null &&
			middleAction.equals("change_rule")){
				try{
					lang=getCurrent_auth().get_language();
					getCurrent_auth().set_ruolo(group);
					getCurrent_auth().saveTicker(response);
					getCurrent_auth().get_authentication_filter().validate_actionPermittedForbidden(getCurrent_auth());
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
//		    	imi = (i_module_integration)util_provider.getInstanceFromProvider(new String[]{bsController.getAppInit().get_cdi_provider()}, module_integration);
		    	imi = (i_module_integration)util_provider.getInstanceFromProvider(
						new String[]{
								bsController.getAppInit().get_context_provider(),
								bsController.getAppInit().get_cdi_provider(),
								bsController.getAppInit().get_ejb_provider()
						},
						"null",
						module_integration,
						request.getSession().getServletContext()
						
						);
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
		if(bsController.getUser_config()==null)
			bsController.setUser_config(new  load_users());
		if(((load_users)bsController.getUser_config()).get_users().size()==0){
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