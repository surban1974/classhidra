package it.classhidra.framework.web.components;   


import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.ActionMapping;
import it.classhidra.annotation.elements.Redirect;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.info_action;
import it.classhidra.core.controller.info_entity;
import it.classhidra.core.controller.info_redirect;
import it.classhidra.core.controller.info_relation;
import it.classhidra.core.controller.info_section;
import it.classhidra.core.controller.load_actions;
import it.classhidra.core.controller.load_authentication;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.core.tool.exception.message;
import it.classhidra.core.tool.jaas_authentication.info_group;
import it.classhidra.core.tool.jaas_authentication.info_target;
import it.classhidra.core.tool.jaas_authentication.info_user;
import it.classhidra.core.tool.jaas_authentication.load_users;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_cloner;
import it.classhidra.core.tool.util.util_find;
import it.classhidra.framework.web.beans.option_element;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ActionMapping (
		redirects={
				@Redirect(	
						path="/jsp/amanager/canvas.jsp",
						descr="Users/Groups/Authentications",
						mess_id="title_fw_amanager"
				)
			}
)

//@Bean (	name="formAmanager")

@Action (
	path="amanager",
	name="formAmanager",
	redirect="/jsp/amanager/canvas.jsp",
	navigated="false",
	syncro="true",
    memoryInSession="false",
    memoryAsLastInstance="true",
    reloadAfterAction="true",
	redirects={
			@Redirect(	
				auth_id="amn_id",
				path="*"
			)
	}
)


public class componentAmanager extends action implements i_action, Serializable{
	private static final long serialVersionUID = 6641876370416839602L;
	private load_actions l_actions;
	private load_users l_users;
	private load_authentication l_authentication;
	private String id_selected_action;
	private String id_selected_redirect;
	private String id_selected_section;
	private String id_selected_group;
	private String id_selected_target;
	private String id_selected_user;
	private String id_selected_relation;

	private String type_selected;
	private String mode="user";
	
	private info_action selected_action;
	private info_redirect selected_redirect;
	private info_section selected_section;
	private info_user selected_user;
	private info_group selected_group;
	private info_target selected_target;
	private info_relation selected_relation;
	
	private Vector elements_true_false;
	
	private boolean display_users=true;
	private boolean display_actions=false;
	private boolean display_groups=false;
	private boolean display_targets=false;
	private boolean display_relations=false;

	
	private String xmlContent;



public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	
	componentAmanager form = this;
	
	if(form.getMiddleAction()==null) form.setMiddleAction("");
/*	
	Vector tmp1 = new Vector(request.getParameterMap().keySet());
	String tmp_str = "";
	for(int i=0;i<tmp1.size();i++){	
		tmp_str+=tmp1.get(i)+"=[";
		try{
			String[] arr = (String[])request.getParameterMap().get(tmp1.get(i));
			for(int j=0;j<arr.length;j++) tmp_str+=arr[j]+";";
		}catch(Exception e){
			tmp_str+="?;";
		}
		tmp_str+="]";
	}
	new bsControllerException(request.getRequestURI() +"|"+ request.getQueryString(), iStub.log_ERROR);
	new bsControllerException(tmp_str, iStub.log_ERROR); 
*/
	
//	new bsControllerException(request.getSession().getId()+"|"+request.getSession().hashCode()+"|"+form.hashCode(), iStub.log_ERROR); 
	if(!form.getL_users().isReadOk() || form.getMiddleAction().equals("reload")){
		try{
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

			form.getL_users().reimposta();
			form.getL_users().initData(bsController.getUser_config().toString());

		}catch(Exception ex){
			new bsControllerMessageException(new message("E", "", ex.toString()), request, iStub.log_ERROR);
		}

	}
	
	
	
	if(!form.getL_actions().isReadOk() || form.getMiddleAction().equals("reload")){
		try{
			load_actions tmp = (load_actions)util_cloner.clone(bsController.getAction_config());
			if(tmp.getXmlEncoding().equals("")) tmp.setXmlEncoding("ISO-8859-1");
			form.getL_actions().reimposta();
			form.getL_actions().getV_info_actions().clear();
			form.getL_actions().initWithData(tmp.toXml());
			form.getL_actions().setReadOk_File(true);
		}catch(Exception ex){
			new bsControllerMessageException(new message("E", "", ex.toString()), request, iStub.log_ERROR);
		}

	}

	if(!form.getL_authentication().isReadOk() || form.getMiddleAction().equals("reload")){
		try{
			load_authentication tmp = (load_authentication)util_cloner.clone(bsController.getAuth_config());
			if(tmp.getXmlEncoding().equals("")) tmp.setXmlEncoding("ISO-8859-1");	
			form.getL_authentication().reimposta();
			form.getL_authentication().initData(tmp.toXml());
			form.getL_authentication().setReadOk_File(true);
		}catch(Exception ex){
			new bsControllerMessageException(new message("E", "", ex.toString()), request, iStub.log_ERROR);
		}

	}
	

	if(form.getMiddleAction().equals("reload")){
		form.setMiddleAction("blank");
	}
	
	if(form.getMiddleAction().equals("preview")){
		if(form.getL_users()!=null){
			preLoadUsers(form);
			
			form.setXmlContent(form.getL_users().toXml());
		}
		return new redirects("/jsp/amanager/preview.jsp");
	}
	
	if(form.getMiddleAction().equals("preview_auth")){
		if(form.getL_authentication()!=null){
			
			preLoadAuthentications(form);
			
			form.setXmlContent(form.getL_authentication().toXml());
			
		}
		return new redirects("/jsp/amanager/preview.jsp");
	}	
	
	if(form.getMiddleAction().equals("load")){
		try{
			
			preLoadUsers(form);
			
			form.getL_users().initData(form.getXmlContent());
		}catch(Exception e){
			new bsControllerMessageException(new message("E", "", e.toString()), request, iStub.log_ERROR);
		}
		return new redirects(get_infoaction().getRedirect());
	}
	if(form.getMiddleAction().equals("load_auth")){
		try{
			
			preLoadAuthentications(form);			
			
			form.getL_authentication().initData(form.getXmlContent());
		}catch(Exception e){
			new bsControllerMessageException(new message("E", "", e.toString()), request, iStub.log_ERROR);
		}
		return new redirects(get_infoaction().getRedirect());
	}	
	
	if(form.getMiddleAction().equals("syncro") || form.getMiddleAction().equals("syncro_auth")){
		String oldXML = null;
		try{
			if(form.getL_users()!=null){
				if(bsController.getUser_config() instanceof load_users){
					oldXML=((load_users)bsController.getUser_config()).toXml();
					((load_users)bsController.getUser_config()).reimposta();
					
					preLoadUsers(form);
					
					((load_users)bsController.getUser_config()).initData(form.getL_users().toXml());
				}
			}
		}catch(Exception e){
			if(oldXML!=null){
				((load_users)bsController.getUser_config()).initData(oldXML);
			}
			new bsControllerMessageException(new message("E", "", e.toString()), request, iStub.log_ERROR);
		}
	

		oldXML = null;
		try{
			if(form.getL_authentication()!=null){

					oldXML=bsController.getAuth_config().toXml();
					bsController.getAuth_config().reimposta();					
					preLoadAuthentications(form);
					bsController.getAuth_config().initData(form.getL_authentication().toXml());
			}
		}catch(Exception e){
			if(oldXML!=null){
				try{
					bsController.getAuth_config().initData(oldXML);
				}catch(Exception ex){
					new bsControllerMessageException(new message("E", "", ex.toString()), request, iStub.log_ERROR);
				}
			}
			new bsControllerMessageException(new message("E", "", e.toString()), request, iStub.log_ERROR);
		}
		return new redirects(get_infoaction().getRedirect());
	}		
	
	if(form.getMiddleAction().equals("clear")){
		try{
			if(form.getL_actions()!=null){
				form.getL_actions().getV_info_actions().clear();
				form.getL_actions().getV_info_beans().clear();
				form.getL_actions().getV_info_redirects().clear();
				form.getL_actions().getV_info_streams().clear();
				form.getL_actions().getV_info_transformationoutput().clear();
				load_actions l_actions = new load_actions();
				l_actions.initBuilder(form.getL_actions().toXml());
				form.setL_actions(l_actions);
				
			}

		}catch(Exception e){
			new bsControllerMessageException(new message("E", "", e.toString()), request, iStub.log_ERROR);
		}
		return new redirects(get_infoaction().getRedirect());
	}
	
	if(form.getMiddleAction().equals("list_groupsIntoUser")){
		return new redirects("/jsp/amanager/list.jsp");
	}
	if(form.getMiddleAction().equals("list_targetsIntoUser")){
		return new redirects("/jsp/amanager/list.jsp");
	}	
	if(form.getMiddleAction().equals("list_usersIntoGroup")){
		return new redirects("/jsp/amanager/list.jsp");
	}	
	if(form.getMiddleAction().equals("list_usersIntoTarget")){
		return new redirects("/jsp/amanager/list.jsp");
	}
	if(form.getMiddleAction().equals("list_groupsIntoRelation")){
		return new redirects("/jsp/amanager/list.jsp");
	}
	if(form.getMiddleAction().equals("list_targetsIntoRelation")){
		return new redirects("/jsp/amanager/list.jsp");
	}
	
	
	if(form.getMiddleAction().indexOf("enable_")>-1){

		if(form.getMiddleAction().equals("enable_action")){
			info_relation selected_relation = (info_relation)util_find.findElementFromList(form.getL_authentication().getV_info_relations(), form.getId_selected_relation(), "name");
			info_action selected_action = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			form.setSelected_action(selected_action);
			if(selected_action.getEnabled()!=info_entity.CONST_DISABLED){
				selected_relation.addElement(selected_action.getPath()+".*.*");
				selected_action.setEnableDisable(info_entity.CONST_DISABLED);
			}
			else{
				selected_relation.removeElement(selected_action.getPath()+".*.*");
				selected_action.setEnableDisable(info_entity.CONST_ENABLED);				
			}
			form.setMiddleAction("view_action");
		}
		if(form.getMiddleAction().equals("enable_redirect")){
			info_relation selected_relation = (info_relation)util_find.findElementFromList(form.getL_authentication().getV_info_relations(), form.getId_selected_relation(), "name");
			info_action selected_action = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			info_redirect selected_redirect = (info_redirect)util_find.findElementFromList(selected_action.getV_info_redirects(), form.getId_selected_redirect(), "path");
			form.setSelected_action(selected_action);
			form.setSelected_redirect(selected_redirect);
			if(selected_redirect.getEnabled()!=info_entity.CONST_DISABLED){
				selected_relation.addElement(selected_action.getPath()+"."+selected_redirect.getAuth_id()+".*");
				selected_redirect.setEnableDisable(info_entity.CONST_DISABLED);
			}
			else{
				selected_relation.removeElement(selected_action.getPath()+"."+selected_redirect.getAuth_id()+".*");
				selected_redirect.setEnableDisable(info_entity.CONST_ENABLED);				
			}
			selected_action.refreshEnableDisableLevel();
			form.setMiddleAction("view_action");
		}	
		if(form.getMiddleAction().equals("enable_section")){
			info_relation selected_relation = (info_relation)util_find.findElementFromList(form.getL_authentication().getV_info_relations(), form.getId_selected_relation(), "name");
			info_action selected_action = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			info_redirect selected_redirect = (info_redirect)util_find.findElementFromList(selected_action.getV_info_redirects(), form.getId_selected_redirect(), "path");
			info_section selected_section = (info_section)util_find.findElementFromList(selected_redirect.getV_info_sections(), form.getId_selected_section(), "name");

			form.setSelected_action(selected_action);
			form.setSelected_redirect(selected_redirect);
			form.setSelected_section(selected_section);
			if(selected_section.getEnabled()!=info_entity.CONST_DISABLED){
				selected_relation.addElement(selected_action.getPath()+"."+selected_redirect.getAuth_id()+"."+selected_section.getName());
				selected_section.setEnableDisable(info_entity.CONST_DISABLED);
			}
			else{
				selected_relation.removeElement(selected_action.getPath()+"."+selected_redirect.getAuth_id()+"."+selected_section.getName());
				selected_section.setEnableDisable(info_entity.CONST_ENABLED);				
			}
			selected_redirect.refreshEnableDisableLevel();
			selected_action.refreshEnableDisableLevel();
			form.setMiddleAction("view_action");
		}		
		
		return new redirects("/jsp/amanager/entity.jsp");
	}

	
	if(form.getMiddleAction().indexOf("view_")>-1){
		if(form.getMiddleAction().equals("view_user")){
			info_user selected = (info_user)util_find.findElementFromList(form.getL_users().getV_info_users(), form.getId_selected_user(), "name");
			form.setSelected_user(selected);
		}
		if(form.getMiddleAction().equals("view_group")){
			info_group selected = (info_group)util_find.findElementFromList(form.getL_users().getV_info_groups(), form.getId_selected_group(), "name");
			form.setSelected_group(selected);
		}	
		if(form.getMiddleAction().equals("view_target")){
			info_target selected = (info_target)util_find.findElementFromList(form.getL_users().getV_info_targets(), form.getId_selected_target(), "name");
			form.setSelected_target(selected);
		}		
		
		
		if(form.getMiddleAction().equals("view_action")){
			info_relation selected = (info_relation)util_find.findElementFromList(form.getL_authentication().getV_info_relations(), form.getId_selected_relation(), "name");
			form.setSelected_relation(selected);		
			if(form.getType_selected().equals("open")){
				for(int i=0;i<form.getL_actions().getV_info_actions().size();i++){
					info_action current = (info_action)form.getL_actions().getV_info_actions().get(i);
					current.syncroWithRelations(selected.get_elements());
					if(current.getPath().equals(form.getId_selected_action())){
						form.setSelected_action(current);
					}
				}
			}else{
				info_action selected_a = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
				form.setSelected_action(selected_a);
			}
		}
		
		if(form.getMiddleAction().equals("view_redirect")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			form.setSelected_action(selected);
			if(form.getSelected_action()!=null){
				info_redirect selected_a = (info_redirect)util_find.findElementFromList(form.getSelected_action().getV_info_redirects(), form.getId_selected_redirect(), "path");
				form.setSelected_redirect(selected_a);
			}
		}	
		
		if(form.getMiddleAction().equals("view_section")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			form.setSelected_action(selected);
			if(form.getSelected_action()!=null){
				info_redirect selected_a = (info_redirect)util_find.findElementFromList(form.getSelected_action().getV_info_redirects(), form.getId_selected_redirect(), "path");
				form.setSelected_redirect(selected_a);
				if(form.getSelected_redirect()!=null){
					info_section selected_b = (info_section)util_find.findElementFromList(form.getSelected_redirect().getV_info_sections(), form.getId_selected_section(), "name");
					form.setSelected_section(selected_b);
				}
			}
		}		
		if(form.getMiddleAction().equals("view_relation")){
			info_relation selected = (info_relation)util_find.findElementFromList(form.getL_authentication().getV_info_relations(), form.getId_selected_relation(), "name");
			form.setSelected_relation(selected);
			
					
		}	

		if(form.getMiddleAction().equals("view_authentication")){
			info_relation selected = (info_relation)util_find.findElementFromList(form.getL_authentication().getV_info_relations(), form.getId_selected_relation(), "name");
			form.setSelected_relation(selected);		
			if(form.getType_selected().equals("open")){
				for(int i=0;i<form.getL_actions().getV_info_actions().size();i++){
					info_action current = (info_action)form.getL_actions().getV_info_actions().get(i);
					current.syncroWithRelations(selected.get_elements());
				}
			}
			
		}		
		
		return new redirects("/jsp/amanager/entity.jsp");
	}
	
	if(form.getMiddleAction().indexOf("update_")>-1){
		String name=request.getParameter("name");
		String value=request.getParameter("value");
		
		if(form.getMiddleAction().equals("update_general")){
			if(!form.getMode().equals("relation")){
				if(form.getL_actions()!=null){
					try{
						form.getL_users().setCampoValue(name, value);
					}catch(Exception e){
					}
				}
			}
			if(form.getMode().equals("relation")){
				if(form.getL_authentication()!=null){
					try{
						form.getL_authentication().setCampoValue(name, value);
					}catch(Exception e){
					}
				}
			}
			form.setMiddleAction("view_general");
			return new redirects("/jsp/amanager/entity.jsp");

			
		}
		if(form.getMiddleAction().equals("update_user")){
			info_user selected = (info_user)util_find.findElementFromList(form.getL_users().getV_info_users(), form.getId_selected_user(), "name");
			form.setSelected_user(selected);
			if(name.equals("password")){
				if(form.getSelected_user().getCrypted().equals("true")){
					try{
						value=bsController.encrypt(value);
					}catch(Exception e){
					}
				}
			}					
			try{
				form.getSelected_user().setCampoValue(name, value);
			}catch(Exception e){
			}
				
			if(name.equals("crypted")){
				form.getSelected_user().setPassword("");
			}
			form.setMiddleAction("view_user");
			return new redirects("/jsp/amanager/entity.jsp");
		}
		
		if(form.getMiddleAction().equals("update_group")){
			info_group selected = (info_group)util_find.findElementFromList(form.getL_users().getV_info_groups(), form.getId_selected_group(), "name");
			form.setSelected_group(selected);
			String origName=form.getSelected_group().getName();
			try{
				form.getSelected_group().setCampoValue(name, value);
			}catch(Exception e){
			}
			if(name.equals("name")){
				try{
					
					form.getL_users().get_groups().remove(origName);
					form.getL_users().get_groups().put(form.getSelected_group().getName(),form.getSelected_group());
					form.getL_users().refreshV_info_groups();
					
					for(int i=0;i<form.getL_users().getV_info_users().size();i++){
						info_user iUser = (info_user)form.getL_users().getV_info_users().get(i);
						if(iUser.get_groups().get(origName)!=null){
							iUser.get_groups().remove(origName);
							iUser.get_groups().put(form.getSelected_group().getName(),form.getSelected_group());
							iUser.refreshV_info_groups();
						}
					}

				}catch(Exception e){
				}
			}			
			form.setMiddleAction("view_group");
			return new redirects("/jsp/amanager/entity.jsp");
			
		}
		if(form.getMiddleAction().equals("update_target")){
			info_target selected = (info_target)util_find.findElementFromList(form.getL_users().getV_info_targets(), form.getId_selected_target(), "name");
			form.setSelected_target(selected);
			String origName=form.getSelected_target().getName();
			try{
				form.getSelected_target().setCampoValue(name, value);
			}catch(Exception e){
			}
			if(name.equals("name")){
				try{
					
					form.getL_users().get_targets().remove(origName);
					form.getL_users().get_targets().put(form.getSelected_target().getName(),form.getSelected_target());
					form.getL_users().refreshV_info_targets();
					
					for(int i=0;i<form.getL_users().getV_info_users().size();i++){
						info_user iUser = (info_user)form.getL_users().getV_info_users().get(i);
						if(iUser.getTarget().equals(origName)){
							iUser.setTarget(form.getSelected_target().getName());
						}
					}
					

				}catch(Exception e){
				}
			}			
			form.setMiddleAction("view_target");
			return new redirects("/jsp/amanager/entity.jsp");
		}
		if(form.getMiddleAction().equals("update_relation")){
			info_relation selected = (info_relation)util_find.findElementFromList(form.getL_authentication().getV_info_relations(), form.getId_selected_relation(), "name");
			form.setSelected_relation(selected);
			String origName=form.getSelected_relation().getName();
			try{
				form.getSelected_relation().setCampoValue(name, value);
			}catch(Exception e){
			}
			form.setMiddleAction("view_relation");
			return new redirects("/jsp/amanager/entity.jsp");
			
		}

		return null;
	}
	
	if(form.getMiddleAction().indexOf("remove_")>-1){
		if(form.getMiddleAction().equals("remove_user")){
			info_user selected = (info_user)util_find.findElementFromList(form.getL_users().getV_info_users(), form.getId_selected_user(), "name");
			try{

				
				if(form.getL_users().getV_info_users().remove(selected)){
					
					boolean founded=false;
				    Iterator iterator = form.getL_users().get_users().keySet().iterator();
				    while (iterator.hasNext() && !founded) {
				      String key = (String) iterator.next();
				      info_user current =  (info_user)form.getL_users().get_users().get(key);
				      if(current.getName().equals(selected.getName())){
				    	 founded=true;
				    	 form.getL_users().get_users().remove(key);
				      }
				    }

					for(int i=0;i<form.getL_users().getV_info_groups().size();i++){
						info_group iGroup = (info_group)form.getL_users().getV_info_groups().get(i);
						if(iGroup.get_users().get(selected.getName())!=null){
							iGroup.get_users().remove(selected.getName());
							iGroup.refreshV_info_users();
						}
					}					

				}
			}catch(Exception e){
			}
			return new redirects(get_infoaction().getRedirect());
		}
		if(form.getMiddleAction().equals("remove_groupIntoUser")){
			info_user selected = (info_user)util_find.findElementFromList(form.getL_users().getV_info_users(), form.getId_selected_user(), "name");
			info_group selected_a = (info_group)util_find.findElementFromList(form.getL_users().getV_info_groups(), form.getId_selected_group(), "name");
			if(selected.get_groups().get(selected_a.getName())!=null){
				selected.get_groups().remove(selected_a.getName());
				selected.refreshV_info_groups();
				
				selected_a.get_users().remove(selected.getName());
				selected_a.refreshV_info_users();
			}
			form.setSelected_user(selected);
			form.setMiddleAction("view_user");
			return new redirects("/jsp/amanager/entity.jsp");
		}
		
		if(form.getMiddleAction().equals("remove_group")){
			info_group selected = (info_group)util_find.findElementFromList(form.getL_users().getV_info_groups(), form.getId_selected_group(), "name");
			try{

				
				if(form.getL_users().getV_info_groups().remove(selected)){
					form.getL_users().get_groups().remove(selected.getName());
					for(int i=0;i<form.getL_users().getV_info_users().size();i++){
						info_user iUser = (info_user)form.getL_users().getV_info_users().get(i);
						if(iUser.get_groups().get(selected.getName())!=null){
							iUser.get_groups().remove(selected.getName());
							iUser.refreshV_info_groups();
						}
					}
					
				}
			}catch(Exception e){
			}		
			return new redirects(get_infoaction().getRedirect());
		}
		if(form.getMiddleAction().equals("remove_target")){
			info_target selected = (info_target)util_find.findElementFromList(form.getL_users().getV_info_targets(), form.getId_selected_target(), "name");
			try{

				
				if(form.getL_users().getV_info_targets().remove(selected)){
					form.getL_users().get_targets().remove(selected.getName());
					for(int i=0;i<form.getL_users().getV_info_users().size();i++){
						info_user iUser = (info_user)form.getL_users().getV_info_users().get(i);
						if(iUser.getTarget().equals(selected.getName())){
							iUser.setTarget("");
						}
					}
 
				}
			}catch(Exception e){
			}	
			return new redirects(get_infoaction().getRedirect());
		}
		if(form.getMiddleAction().equals("remove_relation")){
			info_relation selected = (info_relation)util_find.findElementFromList(form.getL_authentication().getV_info_relations(), form.getId_selected_relation(), "name");
			try{
				form.getL_authentication().getV_info_relations().remove(selected);

			}catch(Exception e){
			}
			return new redirects(get_infoaction().getRedirect());
		}		
		if(form.getMiddleAction().equals("remove_userIntoGroup")){
			info_group selected = (info_group)util_find.findElementFromList(form.getL_users().getV_info_groups(), form.getId_selected_group(), "name");
			info_user selected_a = (info_user)util_find.findElementFromList(form.getL_users().getV_info_users(), form.getId_selected_user(), "name");
			if(selected.get_users().get(selected_a.getName())!=null){
				selected.get_users().remove(selected_a.getName());
				selected.refreshV_info_users();
				
				selected_a.get_groups().remove(selected.getName());
				selected_a.refreshV_info_groups();
			}
			form.setSelected_group(selected);
			form.setMiddleAction("view_group");
			return new redirects("/jsp/amanager/entity.jsp");
			
		}
		if(form.getMiddleAction().equals("remove_userIntoTarget")){
			info_target selected = (info_target)util_find.findElementFromList(form.getL_users().getV_info_targets(), form.getId_selected_target(), "name");
			info_user selected_a = (info_user)util_find.findElementFromList(form.getL_users().getV_info_users(), form.getId_selected_user(), "name");
			if(selected.get_users().get(selected_a.getName())!=null){
				selected.get_users().remove(selected_a.getName());
				selected.refreshV_info_users();
				
				selected_a.setTarget("");
			}
			form.setSelected_target(selected);
			form.setMiddleAction("view_target");
			return new redirects("/jsp/amanager/entity.jsp");
			
		}
		if(form.getMiddleAction().equals("remove_targetIntoRelation")){
			info_relation selected = (info_relation)util_find.findElementFromList(form.getL_authentication().getV_info_relations(), form.getId_selected_relation(), "name");
			info_target selected_a = (info_target)util_find.findElementFromList(selected.getV_info_targets(), form.getId_selected_target(), "name");
			if(selected.get_targets().get(selected_a.getName())!=null){
				selected.get_targets().remove(selected_a.getName());
				selected.refreshV_info_targets();

			}
			form.setSelected_relation(selected);
			form.setMiddleAction("view_relation");
			return new redirects("/jsp/amanager/entity.jsp");
			
		}
		if(form.getMiddleAction().equals("remove_groupIntoRelation")){
			info_relation selected = (info_relation)util_find.findElementFromList(form.getL_authentication().getV_info_relations(), form.getId_selected_relation(), "name");
			info_group selected_a = (info_group)util_find.findElementFromList(selected.getV_info_groups(), form.getId_selected_group(), "name");
			if(selected.get_groups().get(selected_a.getName())!=null){
				selected.get_groups().remove(selected_a.getName());
				selected.refreshV_info_groups();

			}
			form.setSelected_relation(selected);
			form.setMiddleAction("view_relation");
			return new redirects("/jsp/amanager/entity.jsp");
			
		}
		
			
		
		
		return null;
	}	
	
	if(form.getMiddleAction().indexOf("add_")>-1){
		if(form.getMiddleAction().equals("add_user")){
			info_user selected = new info_user();
			selected.setName("[new_user]");
			form.getL_users().get_users().put(selected.getName(),selected);
			form.getL_users().refreshV_info_users();
			form.setSelected_user(selected);
			form.setMiddleAction("");
			return new redirects(get_infoaction().getRedirect());
		}
		if(form.getMiddleAction().equals("add_group")){
			info_group selected = new info_group();
			selected.setName("[new_group]");
			form.getL_users().get_groups().put(selected.getName(),selected);
			form.getL_users().refreshV_info_groups();
			form.setSelected_group(selected);
			form.setMiddleAction("");
			return new redirects(get_infoaction().getRedirect());
		}
		
		if(form.getMiddleAction().equals("add_target")){
			info_target selected = new info_target();
			selected.setName("[new_target]");
			form.getL_users().get_targets().put(selected.getName(),selected);
			form.getL_users().refreshV_info_targets();
			form.setSelected_target(selected);
			form.setMiddleAction("");
			return new redirects(get_infoaction().getRedirect());
		}
		if(form.getMiddleAction().equals("add_relation")){
			info_relation selected = new info_relation();
			selected.setName("[new_relation]");
			form.getL_authentication().getV_info_relations().add(selected);
			form.setSelected_relation(selected);
			form.setMiddleAction("");
			return new redirects(get_infoaction().getRedirect());
		}		
		if(form.getMiddleAction().equals("add_groupIntoUser")){
			info_user selected = (info_user)util_find.findElementFromList(form.getL_users().getV_info_users(), form.getId_selected_user(), "name");
			info_group selected_a = (info_group)util_find.findElementFromList(form.getL_users().getV_info_groups(), form.getId_selected_group(), "name");
			if(selected.get_groups().get(selected_a.getName())==null){
				selected.get_groups().put(selected_a.getName(), selected_a);
				selected.refreshV_info_groups();
				
				selected_a.get_users().put(selected.getName(), selected);
				selected_a.refreshV_info_users();
			}
			form.setSelected_user(selected);
			form.setMiddleAction("view_user");
		}		
		if(form.getMiddleAction().equals("add_userIntoGroup")){
			info_group selected = (info_group)util_find.findElementFromList(form.getL_users().getV_info_groups(), form.getId_selected_group(), "name");
			info_user selected_a = (info_user)util_find.findElementFromList(form.getL_users().getV_info_users(), form.getId_selected_user(), "name");
			if(selected.get_users().get(selected_a.getName())==null){
				selected.get_users().put(selected_a.getName(), selected_a);
				selected.refreshV_info_users();
				
				selected_a.get_groups().put(selected.getName(), selected);
				selected_a.refreshV_info_groups();
			}
			form.setSelected_group(selected);
			form.setMiddleAction("view_group");
		}				
		if(form.getMiddleAction().equals("add_userIntoTarget")){
			info_target selected = (info_target)util_find.findElementFromList(form.getL_users().getV_info_targets(), form.getId_selected_target(), "name");
			info_user selected_a = (info_user)util_find.findElementFromList(form.getL_users().getV_info_users(), form.getId_selected_user(), "name");
			if(selected.get_users().get(selected_a.getName())==null){
				selected.get_users().put(selected_a.getName(), selected_a);
				selected.refreshV_info_users();
				
				selected_a.setTarget(selected.getName());
			}
			form.setSelected_target(selected);
			form.setMiddleAction("view_target");
		}	
		
		if(form.getMiddleAction().equals("add_targetIntoUser")){
			info_target selected = (info_target)util_find.findElementFromList(form.getL_users().getV_info_targets(), form.getId_selected_target(), "name");
			info_user selected_a = (info_user)util_find.findElementFromList(form.getL_users().getV_info_users(), form.getId_selected_user(), "name");
			if(selected_a!=null && selected!=null){
				selected_a.setTarget(selected.getName());
				form.setSelected_user(selected_a);
				selected.get_users().put(selected_a.getName(), selected_a);
				selected.refreshV_info_users();
				form.setMiddleAction("view_user");
				return new redirects("/jsp/amanager/entity.jsp");
			}
		}

		if(form.getMiddleAction().equals("add_targetIntoRelation")){
			info_relation selected = (info_relation)util_find.findElementFromList(form.getL_authentication().getV_info_relations(), form.getId_selected_relation(), "name");
			info_target selected_a = (info_target)util_find.findElementFromList(form.getL_users().getV_info_targets(), form.getId_selected_target(), "name");
			if(selected.get_targets().get(selected_a.getName())==null){
				selected.get_targets().put(selected_a.getName(), selected_a);
				selected.refreshV_info_targets();
			}
			form.setSelected_relation(selected);
			form.setMiddleAction("view_relation");
		}
		if(form.getMiddleAction().equals("add_groupIntoRelation")){
			info_relation selected = (info_relation)util_find.findElementFromList(form.getL_authentication().getV_info_relations(), form.getId_selected_relation(), "name");
			info_group selected_a = (info_group)util_find.findElementFromList(form.getL_users().getV_info_groups(), form.getId_selected_group(), "name");
			if(selected.get_groups().get(selected_a.getName())==null){
				selected.get_groups().put(selected_a.getName(), selected_a);
				selected.refreshV_info_groups();
			}
			form.setSelected_relation(selected);
			form.setMiddleAction("view_relation");
		}		


		return new redirects("/jsp/amanager/entity.jsp");
	}		
	return new redirects(get_infoaction().getRedirect());
}

private void preLoadUsers(componentAmanager form){
	return;
/*	
	try{
		load_users tmp = new load_users();
		String xml = util_classes.getResourceAsTxt(this.getClass().getName(), "resources/example_users.xml");
		tmp.initData(xml);
		
		form.getL_users().get_users().putAll(tmp.get_users());
		form.getL_users().get_groups().putAll(tmp.get_groups());
		form.getL_users().get_targets().putAll(tmp.get_targets());
		
		form.getL_users().refreshV_info_users();
		form.getL_users().refreshV_info_groups();
		form.getL_users().refreshV_info_targets();
		
	}catch(Exception e){
	}
*/
}

private void preLoadAuthentications(componentAmanager form){
	return;
/*	
	try{
		load_authentication tmp = new load_authentication();
		String xml = util_classes.getResourceAsTxt(this.getClass().getName(), "resources/example_authentication.xml");
		tmp.initData(xml);
		
		HashMap _relations = new HashMap();
		
		for(int i=0;i<form.getL_authentication().getV_info_relations().size();i++){
			
			info_relation current = (info_relation)form.getL_authentication().getV_info_relations().get(i);
			if(current.get_targets().get("default_target")!=null &&
				(current.get_groups().get("guest")!=null || current.get_groups().get("user")!=null)
				){
				current.get_targets().remove("default_target");
				current.get_groups().remove("guest");
				current.get_groups().remove("user");
				current.refreshV_info_groups();
				current.refreshV_info_targets();
			}
			_relations.put(current.getName(), current);
			
		}
		for(int i=0;i<tmp.getV_info_relations().size();i++){
			info_relation current = (info_relation)tmp.getV_info_relations().get(i);	
			_relations.put(current.getName(), current);
		}
		Vector v_info_relations = (new Vector(_relations.values()));
		v_info_relations = new util_sort().sort(v_info_relations,"name");

		form.getL_authentication().setV_info_relations(v_info_relations);
		
	}catch(Exception e){
	}
*/
}
public void reimposta(){
	id_selected_action="";
	id_selected_section="";
	id_selected_redirect="";
	id_selected_group="";
	id_selected_target="";
	id_selected_user="";
	id_selected_relation="";
	type_selected="";
	
	elements_true_false = new Vector();
	elements_true_false.add(new option_element("false","false"));
	elements_true_false.add(new option_element("true","true"));
	
	xmlContent="";
	mode="user";
	
	l_actions=new load_actions();
	l_users=new load_users();
	l_authentication=new load_authentication();
	
}

public redirects validate(HttpServletRequest request){
	return null;
}

public load_actions getL_actions() {
	return l_actions;
}

public void setL_actions(load_actions lActions) {
	l_actions = lActions;
}

public String getId_selected_action() {
	return id_selected_action;
}

public void setId_selected_action(String idSelectedAction) {
	id_selected_action = idSelectedAction;
}


public String getId_selected_redirect() {
	return id_selected_redirect;
}

public void setId_selected_redirect(String idSelectedRedirect) {
	id_selected_redirect = idSelectedRedirect;
}

public info_action getSelected_action() {
	return selected_action;
}

public void setSelected_action(info_action selectedAction) {
	selected_action = selectedAction;
}

public info_redirect getSelected_redirect() {
	return selected_redirect;
}

public void setSelected_redirect(info_redirect selectedRedirect) {
	selected_redirect = selectedRedirect;
}

public String getType_selected() {
	return type_selected;
}

public void setType_selected(String typeSelected) {
	type_selected = typeSelected;
}

public Vector getElements_true_false() {
	return elements_true_false;
}

public void setElements_true_false(Vector elementsTrueFalse) {
	elements_true_false = elementsTrueFalse;
}

public String getXmlContent() {
	return xmlContent;
}

public void setXmlContent(String xmlContent) {
	this.xmlContent = xmlContent;
}

public String getId_selected_section() {
	return id_selected_section;
}

public void setId_selected_section(String idSelectedSection) {
	id_selected_section = idSelectedSection;
}

public info_section getSelected_section() {
	return selected_section;
}

public void setSelected_section(info_section selectedSection) {
	selected_section = selectedSection;
}

public String getId_selected_group() {
	return id_selected_group;
}

public void setId_selected_group(String idSelectedGroup) {
	id_selected_group = idSelectedGroup;
}

public String getId_selected_user() {
	return id_selected_user;
}

public void setId_selected_user(String idSelectedUser) {
	id_selected_user = idSelectedUser;
}

public info_user getSelected_user() {
	return selected_user;
}

public void setSelected_user(info_user selectedUser) {
	selected_user = selectedUser;
}

public load_users getL_users() {
	return l_users;
}

public void setL_users(load_users lUsers) {
	l_users = lUsers;
}

public load_authentication getL_authentication() {
	return l_authentication;
}

public void setL_authentication(load_authentication lAuthentication) {
	l_authentication = lAuthentication;
}

public boolean isDisplay_users() {
	return display_users;
}

public void setDisplay_users(boolean displayUsers) {
	display_users = displayUsers;
}

public boolean isDisplay_actions() {
	return display_actions;
}

public void setDisplay_actions(boolean displayActions) {
	display_actions = displayActions;
}

public boolean isDisplay_groups() {
	return display_groups;
}

public void setDisplay_groups(boolean displayGroups) {
	display_groups = displayGroups;
}

public info_group getSelected_group() {
	return selected_group;
}

public void setSelected_group(info_group selectedGroup) {
	selected_group = selectedGroup;
}

public String getMode() {
	return mode;
}

public void setMode(String mode) {
	this.mode = mode;
	if(mode!=null){
		if(mode.equals("user")){
			display_users=true;
			display_actions=false;
			display_groups=false;			
		}
		if(mode.equals("group")){
			display_users=false;
			display_actions=true;
			display_groups=true;			
		}
		
	}
}

public String getId_selected_target() {
	return id_selected_target;
}

public void setId_selected_target(String idSelectedTarget) {
	id_selected_target = idSelectedTarget;
}

public info_target getSelected_target() {
	return selected_target;
}

public void setSelected_target(info_target selectedTarget) {
	selected_target = selectedTarget;
}

public boolean isDisplay_targets() {
	return display_targets;
}

public void setDisplay_targets(boolean displayTargets) {
	display_targets = displayTargets;
}

public String getId_selected_relation() {
	return id_selected_relation;
}

public void setId_selected_relation(String idSelectedRelation) {
	id_selected_relation = idSelectedRelation;
}

public info_relation getSelected_relation() {
	return selected_relation;
}

public void setSelected_relation(info_relation selectedRelation) {
	selected_relation = selectedRelation;
}

public boolean isDisplay_relations() {
	return display_relations;
}

public void setDisplay_relations(boolean displayRelations) {
	display_relations = displayRelations;
}


}

