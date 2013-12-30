package it.classhidra.core.controller;

public interface i_externalloader {
	public static final String ACTIONS_actions="actions";
	public static final String ACTIONS_streams="streams";
	public static final String ACTIONS_streams_apply_to_actions="streams_apply_to_actions";
	public static final String ACTIONS_beans="beans";
	public static final String ACTIONS_redirects="redirects";
	public static final String ACTIONS_error="error";
	public static final String ACTIONS_auth_error="auth_error";
	public static final String ACTIONS_session_error="session_error";
	
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
