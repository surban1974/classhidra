package it.classhidra.core.controller;

import java.io.Serializable;

public interface i_externalloader extends Serializable{
	public static final String ACTIONS_actions="actions";
	public static final String ACTIONS_actioncalls="actioncalls";
	public static final String ACTIONS_restmapping="restmapping";
	public static final String ACTIONS_streams="streams";
	public static final String ACTIONS_streams_apply_to_actions="streams_apply_to_actions";
	public static final String ACTIONS_beans="beans";
	public static final String ACTIONS_redirects="redirects";
	public static final String ACTIONS_error="error";
	public static final String ACTIONS_auth_error="auth_error";
	public static final String ACTIONS_session_error="session_error";
	
	public static final String ACTIONS_listener_actions="listener_actions";
	public static final String ACTIONS_listener_beans="listener_beans";
	public static final String ACTIONS_listener_streams="listener_streams";
	public static final String ACTIONS_memoryInContainer_streams="memoryInContainer_streams";
	public static final String ACTIONS_provider="provider";
	public static final String ACTIONS_instance_navigated="instance_navigated";
	public static final String ACTIONS_instance_local_container="instance_local_container";
	public static final String ACTIONS_instance_scheduler_container="instance_scheduler_container";
	
	public static final String ACTIONS_instance_onlysession="instance_onlysession"; 
	public static final String ACTIONS_instance_servletcontext="instance_servletcontext";

	
	public static final String MESSAGES_messages="messages";

	public static final String AUTHENTICATIONS_targets="targets";
	public static final String AUTHENTICATIONS_targets_allowed="targets_allowed";
	public static final String AUTHENTICATIONS_mtargets="mtargets";
	public static final String AUTHENTICATIONS_mtargets_allowed="mtargets_allowed";
	public static final String AUTHENTICATIONS_ctargets="ctargets";
	public static final String AUTHENTICATIONS_ctargets_allowed="ctargets_allowed";
	public static final String AUTHENTICATIONS_area="area";
	public static final String AUTHENTICATIONS_info_relations="info_relations";
	
	public static final String ORGANIZATIONS_nodes="nodes";
	
	public static final String MENU_menu="menu";
	
	public static final String USERS_users="users";
	public static final String USERS_groups="groups";
	public static final String USERS_targets="targets";
	public static final String USERS_matriculations="matriculations";
	
	

	
	
	public Object getProperty(String key);
	public void setProperty(String key,Object value);
	public void load();
}
