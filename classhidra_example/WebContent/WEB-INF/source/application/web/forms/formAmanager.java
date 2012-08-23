package application.web.forms;   

import it.classhidra.core.controller.bean;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.info_action;
import it.classhidra.core.controller.info_redirect;
import it.classhidra.core.controller.info_relation;
import it.classhidra.core.controller.info_section;
import it.classhidra.core.controller.load_actions;
import it.classhidra.core.controller.load_authentication;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.jaas_authentication.info_group;
import it.classhidra.core.tool.jaas_authentication.info_target;
import it.classhidra.core.tool.jaas_authentication.info_user;
import it.classhidra.core.tool.jaas_authentication.load_users;
import it.classhidra.framework.web.beans.option_element;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;



public class formAmanager extends bean implements i_bean{
	private static final long serialVersionUID = -4499306352896225554L;
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
