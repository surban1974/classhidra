package examples.ejb.classhidra.framework.web.components;


import java.io.OutputStream;
import java.io.Serializable;
import java.util.StringTokenizer;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.NavigatedDirective;
import it.classhidra.annotation.elements.Redirect;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.init.project_init;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.jaas_authentication.load_users;
import it.classhidra.core.tool.util.util_container;




@Action (
	path="resources",
	redirect="/jsp/framework/resources.jsp",
	syncro="true",
	entity=@Entity(
			property="allway:public"
	),
	redirects={
			@Redirect(
				auth_id="res_id",
				path="*"
			)
	}
)

@NavigatedDirective
@Stateless
@Local(i_action.class)
public class EjbComponentRecources extends action implements i_action, Serializable{
	private static final long serialVersionUID = 6641876370416839602L;

public EjbComponentRecources(){
	super();
}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	if(this.getMiddleAction()==null) this.setMiddleAction("");
	if(this.getMiddleAction().equals("reload")) this.setMiddleAction("");

	boolean isScript=false;

	if(!this.getMiddleAction().equals("")){
		String pinAuth = bsController.getAppInit().get_pin();
		isScript=true;
		if(pinAuth!=null && pinAuth.trim().length()>0){
			String isPin = request.getParameter("isPin");
			if(isPin==null){
				try{
					OutputStream out = response.getOutputStream();
					String jscript="";
					jscript+="var inPut = prompt('PIN:', '');";
					jscript+="if (inPut == null || inPut == '')";
					jscript+="	dhtmlLoadScript('resources?middleAction=');";
					jscript+="else dhtmlLoadScript('resources?isPin=true&pin='+inPut+'&middleAction="+this.getMiddleAction()+"');";
					jscript+="";
					out.write(jscript.getBytes());
				}catch(Exception e){
				}
				return null;
			}else{
				try{
					String pin = request.getParameter("pin");
					if(	pin!=null){
						if(	pinAuth.equals(pin) ||
							pinAuth.equals(bsController.encrypt(pin))
						){
							isScript=true;
						}else{
							OutputStream out = response.getOutputStream();
							String jscript="alert('PIN incorrect')";
							out.write(jscript.getBytes());
							return null;
						}
					}
				}catch (Exception e) {
				}
			}
		}
	}

	if(this.getMiddleAction().equals("users_config")){
		bsController.setUser_config(new load_users());
			try{
				((load_users)bsController.getUser_config()).init();
				if(((load_users)bsController.getUser_config()).isReadError()) ((load_users)bsController.getUser_config()).load_from_resources();
				if(((load_users)bsController.getUser_config()).isReadError()) ((load_users)bsController.getUser_config()).init(bsController.getAppInit().get_path_config()+bsController.CONST_XML_USERS);
				if(((load_users)bsController.getUser_config()).isReadError()) bsController.setUser_config(null);
			}catch(bsControllerException je){
			}

	}
	if(this.getMiddleAction().equals("authentication_config")){
		bsController.setReInit(true);
		bsController.getAuth_config();
		bsController.setReInit(false);
	}
	if(this.getMiddleAction().equals("messages_config")){
		bsController.setReInit(true);
		bsController.getMess_config();
		bsController.setReInit(false);
	}
	if(this.getMiddleAction().equals("menu_config")){
		bsController.setReInit(true);
		bsController.getMenu_config();
		bsController.setReInit(false);
	}
	if(this.getMiddleAction().equals("actions_config")){
		bsController.setReInit(true);
		bsController.getAction_config();
		bsController.setReInit(false);
	}

	if(this.getMiddleAction().equals("db_init")){
		bsController.setReInit(true);
		bsController.getDbInit();
		bsController.setReInit(false);
	}
	if(this.getMiddleAction().equals("log_init")){
		bsController.setReInit(true);
		bsController.getLogG();
		bsController.setReInit(false);
	}
	if(this.getMiddleAction().indexOf("removefromcontainer")==0){
		StringTokenizer st = new StringTokenizer(this.getMiddleAction(),":");
		if(st.hasMoreTokens()){
			st.nextToken();
			if(st.hasMoreTokens()){
				String key = st.nextToken();
				util_container.getContainer().removeFromLocalContainer(key);
				this.put("containerkeys",util_container.getContainer().getContainerKeys());
			}
		}
		bsController.setReInit(true);
		bsController.getLogG();
		bsController.setReInit(false);
	}


	this.setMiddleAction("");
	if(this.getMiddleAction().equals("")){

		this.put("app_init",bsController.getAppInit().getLoadedFrom());
		this.put("project_init",new project_init(null).getLoadedFrom());
		this.put("auth_init",new auth_init().getLoadedFrom());
		this.put("log_init",bsController.getLogInit().getLoadedFrom());
		this.put("db_init",bsController.getDbInit().getLoadedFrom());
		this.put("actions_config",bsController.getAction_config().getLoadedFrom());
		this.put("authentication_config",bsController.getAuth_config().getLoadedFrom());
		this.put("messages_config",bsController.getMess_config().getLoadedFrom());
		this.put("menu_config",bsController.getMenu_config().getLoadedFrom());
		try{
			this.put("users_config",((load_users)bsController.getUser_config()).getLoadedFrom());
		}catch(Exception e){
		}
		this.put("containerkeys",util_container.getContainer().getContainerKeys());
	}

	if(isScript){
		try{
			OutputStream out = response.getOutputStream();
			String jscript="alert('Operation is finished')";
			out.write(jscript.getBytes());
		}catch(Exception e){

		}
		return null;

	}else
		return new redirects(get_infoaction().getRedirect());
}

}
