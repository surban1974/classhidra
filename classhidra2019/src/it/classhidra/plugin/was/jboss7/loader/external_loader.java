package it.classhidra.plugin.was.jboss7.loader;

import it.classhidra.core.controller.i_externalloader;

public class external_loader implements i_externalloader{
	private static final long serialVersionUID = 1L;



	public Object getProperty(String key) {

		
		if(key.equals(ACTIONS_actions)) return null;
		if(key.equals(ACTIONS_streams)) return null;
		if(key.equals(ACTIONS_streams_apply_to_actions)) return null;
		if(key.equals(ACTIONS_beans)) return null;
		if(key.equals(ACTIONS_redirects)) return null;
		if(key.equals(ACTIONS_error)) return null;
		if(key.equals(ACTIONS_auth_error)) return null;
		if(key.equals(ACTIONS_session_error)) return null;
		
		if(key.equals(MESSAGES_messages)){
			messages_loader ml = new messages_loader();
			ml.load_Messages();
			return ml.get_messages();
		}


		if(key.equals(AUTHENTICATIONS_targets)) return null;
		if(key.equals(AUTHENTICATIONS_targets_allowed)) return null;
		if(key.equals(AUTHENTICATIONS_mtargets)) return null;
		if(key.equals(AUTHENTICATIONS_mtargets_allowed)) return null;
		if(key.equals(AUTHENTICATIONS_area)) return null;
		if(key.equals(AUTHENTICATIONS_info_relations)) return null;
		
		if(key.equals(ORGANIZATIONS_nodes)) return null;
		
		if(key.equals(MENU_menu)) return null;
		
		if(key.equals(USERS_users)) return null;
		if(key.equals(USERS_groups)) return null;
		if(key.equals(USERS_targets)) return null;
		if(key.equals(USERS_matriculations)) return null;
		return null;
	}

	
	
	public void load() {

	}



	public void setProperty(String key, Object value) {
	
	}

	

}
