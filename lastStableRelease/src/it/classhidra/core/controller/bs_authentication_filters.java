package it.classhidra.core.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import it.classhidra.core.i_authentication_filter;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.util.util_cloner;

public class bs_authentication_filters implements i_authentication_filter { 

	

	private static final long serialVersionUID = 1L;
	
	
	public void validate_actionPermittedForbidden(auth_init auth){
		HashMap actions_orig = new HashMap();
		try{
			actions_orig = (HashMap)util_cloner.clone(load_actions.get_actions());
		}catch(Exception ex){
		}
		
		HashMap permited_all = new HashMap();
		HashMap permited = new HashMap();
		Vector key_all = new Vector(actions_orig.keySet());
		for(int i=0;i<key_all.size();i++) permited_all.put(key_all.get(i), actions_orig.get(key_all.get(i)));
		
		if(	auth==null ||
			bsController.getAuth_config()== null ||
		 	bsController.getAuth_config().get_targets()==null ||
		 	bsController.getAuth_config().get_targets_allowed()==null ||
		 	bsController.getAuth_config().get_area()==null) return;
		 	
		String _targets = auth.get_target();		
		String _roles=auth.get_ruolo();


		StringTokenizer st_t = new StringTokenizer(_targets, bsController.CONST_ROLE_SEPARATOR);
		while(st_t.hasMoreTokens()){
			String _target = st_t.nextToken().trim();
			StringTokenizer st = new StringTokenizer(_roles, bsController.CONST_ROLE_SEPARATOR);
			HashMap permited_current = new HashMap();
						
			while(st.hasMoreTokens()){				
				String _role = st.nextToken().trim();					
				
				HashMap hActions = null;
				try{
					hActions = (HashMap)((HashMap)bsController.getAuth_config().get_targets().get(_target)).get(_role);
				}catch(Exception e){
				}
				
//	Change 2018-01-16 for wildcard - FORBIDDEN					
				try {
					if(	bsController.getAuth_config().get_targets().get("*")!=null &&
						((HashMap)bsController.getAuth_config().get_targets().get("*")).get(_role)!=null) {
						if(hActions==null)
							hActions = (HashMap)((HashMap)bsController.getAuth_config().get_targets().get("*")).get(_role);
						else
							hActions.putAll(
									(HashMap)((HashMap)bsController.getAuth_config().get_targets().get("*")).get(_role)
									);
					}
				}catch(Exception e){
				}
				try {
					if(	bsController.getAuth_config().get_targets().get(_target)!=null &&
						((HashMap)bsController.getAuth_config().get_targets().get(_target)).get("*")!=null) {
						if(hActions==null)
							hActions = (HashMap)((HashMap)bsController.getAuth_config().get_targets().get(_target)).get("*");
						else
							hActions.putAll(
									(HashMap)((HashMap)bsController.getAuth_config().get_targets().get(_target)).get("*")
									);
						}					
				}catch(Exception e){
				}					
//	***	
				
				HashMap hAllowedActions = null;
				try{
					hAllowedActions = (HashMap)((HashMap)bsController.getAuth_config().get_targets_allowed().get(_target)).get(_role);
				}catch(Exception e){
				}
				
//				Change 2018-01-16 for wildcard - ALLOWED				
				try {
					if(	bsController.getAuth_config().get_targets_allowed().get("*")!=null &&
						((HashMap)bsController.getAuth_config().get_targets_allowed().get("*")).get(_role)!=null) {
						if(hAllowedActions==null)
							hAllowedActions = (HashMap)((HashMap)bsController.getAuth_config().get_targets_allowed().get("*")).get(_role);
						else
							hAllowedActions.putAll(
									(HashMap)((HashMap)bsController.getAuth_config().get_targets_allowed().get("*")).get(_role)
									);
					}
				}catch(Exception e){
				}
				try {
					if(	bsController.getAuth_config().get_targets_allowed().get(_target)!=null &&
						((HashMap)bsController.getAuth_config().get_targets_allowed().get(_target)).get("*")!=null) {
						if(hAllowedActions==null)
							hAllowedActions = (HashMap)((HashMap)bsController.getAuth_config().get_targets_allowed().get(_target)).get("*");
						else
							hAllowedActions.putAll(
									(HashMap)((HashMap)bsController.getAuth_config().get_targets_allowed().get(_target)).get("*")
									);
						}					
				}catch(Exception e){
				}					
//	***					


				try{
					permited_current = (HashMap)util_cloner.clone(permited_all);
				}catch(Exception e){
				}
				
				if(bsController.getAppInit().get_actioncall_separator()!=null && !bsController.getAppInit().get_actioncall_separator().equals("")){
					char separator=bsController.getAppInit().get_actioncall_separator().charAt(0);

					if(hActions!=null){
						Iterator it = hActions.entrySet().iterator();
						while (it.hasNext()) {
							Map.Entry pairs = (Map.Entry)it.next();
							String key = pairs.getKey().toString();
							if(key.indexOf(separator)>-1 &&
								permited_current.get(key)==null){
								String id_action=key.substring(0,key.indexOf(separator));
								info_action ia = (info_action)permited_current.get(id_action);
								if(ia!=null){
									try{
										permited_current.put(key, util_cloner.clone(ia));
									}catch(Exception e){
									}
								}
							}
						}
					}
					if(hAllowedActions!=null){
						Iterator it = hAllowedActions.entrySet().iterator();
						while (it.hasNext()) {
							Map.Entry pairs = (Map.Entry)it.next();
							String key = pairs.getKey().toString();
							if(key.indexOf(separator)>-1 &&
								permited_current.get(key)==null){
								String id_action=key.substring(0,key.indexOf(separator));
								info_action ia = (info_action)permited_current.get(id_action);
								if(ia!=null){
									try{
										permited_current.put(key, util_cloner.clone(ia));
									}catch(Exception e){
									}
								}
							}
						}
					}
					
				}
				
				if(hActions!=null){
					Vector keys = new Vector(hActions.keySet());
					for(int i=0;i<keys.size();i++){
						try{
							String k_keys = (String)keys.get(i);
							if(	hActions.get(k_keys)!=null &&
								((HashMap)hActions.get(k_keys)).get("*")!=null &&
								((HashMap)((HashMap)hActions.get(k_keys)).get("*")).get("*")!=null
							){
								
								if(	hAllowedActions!=null &&
									hAllowedActions.get(k_keys)!=null &&
									((HashMap)hAllowedActions.get(k_keys)).get("*")!=null &&
									((HashMap)((HashMap)hAllowedActions.get(k_keys)).get("*")).get("*")!=null
								){
									
								}else								
									permited_current.remove(k_keys);
							}
							else{
								if(	hAllowedActions!=null &&
										hAllowedActions.get(k_keys)!=null &&
										((HashMap)hAllowedActions.get(k_keys)).get("*")!=null){
								}else{
									Vector keys_redirect = new Vector(((HashMap)hActions.get(k_keys)).keySet());
									for(int j=0;j<keys_redirect.size();j++){
										String k_redirect = (String)keys_redirect.get(j);
										if(	hAllowedActions!=null &&
												hAllowedActions.get(k_keys)!=null &&
												((HashMap)hAllowedActions.get(k_keys)).get(k_redirect)!=null &&
												((HashMap)((HashMap)hAllowedActions.get(k_keys)).get(k_redirect)).get("*")!=null){
										}else{
											
											HashMap permited_current_auth_redirects = ((info_action)permited_current.get(k_keys)).get_auth_redirects();
											HashMap permited_current_redirects = ((info_action)permited_current.get(k_keys)).get_redirects();
											info_redirect ir_a = (info_redirect)permited_current_auth_redirects.get(k_redirect);
		
											
											if(ir_a!=null){
												info_redirect ir = (info_redirect)permited_current_redirects.get(ir_a.getPath());
												
												Vector keys_sections= new Vector( ((HashMap)((HashMap)hActions.get(k_keys)).get(k_redirect)).keySet());
												for(int k=0;k<keys_sections.size();k++){
													String k_section = (String)keys_sections.get(k);
													if(	hAllowedActions!=null &&
															hAllowedActions.get(k_keys)!=null &&
															((HashMap)hAllowedActions.get(k_keys)).get(k_redirect)!=null &&
															((HashMap)((HashMap)hAllowedActions.get(k_keys)).get(k_redirect)).get(k_section)!=null){
													}else{
														if(ir.get_sections().get(k_section)!=null){
															((info_section)ir.get_sections().get(k_section)).setAllowed(false);
															((info_section)ir_a.get_sections().get(k_section)).setAllowed(false);
														}
														if(k_section!=null && k_section.equals("*")){
															((info_redirect)permited_current_auth_redirects.get(k_redirect)).setAllowedSection(false);
															String ir_path = ir.getPath();
															((info_redirect)permited_current_redirects.get(ir_path)).setAllowedSection(false);
// Update 05/01/2016															
															permited_current_auth_redirects.remove(k_redirect);
															permited_current_redirects.remove(ir_path);
														}
													}
												}
											}
										}
									}
								}
								
								
								
							}
						}catch(Exception e){	 
						}
					}
				}
				PutAll(permited,permited_current);
//				permited.putAll(permited_current);
			}			
		}
		auth.set_actions_permited(permited);
		key_all = new Vector(permited.keySet());
		for(int i=0;i<key_all.size();i++) permited_all.remove(key_all.get(i));
		auth.set_actions_forbidden(permited_all);
		
		
	}
	
	private static void PutAll(HashMap collect, HashMap el){
		Vector keys = new Vector(el.keySet());
		for(int i=0;i<keys.size();i++){
			String key = (String)keys.get(i);
			if(collect.get(key)==null) collect.put(key,el.get(key));
			else{
				HashMap collect_redirects = ((info_action)collect.get(key)).get_auth_redirects();
				HashMap el_redirects = ((info_action)el.get(key)).get_auth_redirects();
				collect_redirects.putAll(el_redirects);
			}
		}
	}
	

	private static void check_authIsFull(auth_init auth){
		if(auth!=null && (auth.get_actions_permitted()==null || auth.get_actions_permitted().size()==0)){
			HashMap actions_orig = new HashMap();
			try{
				actions_orig = (HashMap)util_cloner.clone(load_actions.get_actions());
			}catch(Exception ex){
			}			
			HashMap permited_all = new HashMap();
			Vector key_all = new Vector(actions_orig.keySet());
			for(int i=0;i<key_all.size();i++) permited_all.put(key_all.get(i), actions_orig.get(key_all.get(i)));
			auth.set_actions_permited(permited_all);
		}
	}

	
	public boolean check_actionIsPermitted(auth_init auth, String id_complete){
		
		if(bsController.getAppInit().get_avoid_permission_chech()!=null && bsController.getAppInit().get_avoid_permission_chech().equalsIgnoreCase("true"))
			return true;
	
		if(id_complete!=null && id_complete.equals(bsController.CONST_ID_$ACTION_HELP)) return true;
		if(bsController.getAppInit().get_enterpoint()!=null && !bsController.getAppInit().get_enterpoint().equals("*")){
			if(id_complete!=null && id_complete.equals(bsController.getAppInit().get_enterpoint())) return true;
		}else{
			if(auth==null) return false;
			check_authIsFull(auth);
		}
		
		if(auth!=null && (auth.get_actions_permitted()==null || auth.get_actions_permitted().size()==0)) return false;
		boolean result = true;
		info_action iAction_complete = (info_action)auth.get_actions_permitted().get(id_complete);
		if(	iAction_complete==null){
			if(bsController.getAppInit().get_actioncall_separator()!=null && !bsController.getAppInit().get_actioncall_separator().equals("")){
				char separator=bsController.getAppInit().get_actioncall_separator().charAt(0);
//				String id_call=null;
				String id_action=null;
				if(id_complete!=null && id_complete.indexOf(separator)>-1){
					try{
//						id_call = id_complete.substring(id_complete.indexOf(separator)+1,id_complete.length());
						id_action = id_complete.substring(0,id_complete.indexOf(separator));
					}catch(Exception e){
					}
					if(	id_action!=null &&
						auth.get_actions_permitted().get(id_action)!=null &&
						auth.get_actions_forbidden().get(id_complete)==null){}
					else result = false;
				}else if(bsController.getAction_config().get_actioncalls().get(id_complete)!=null){
					info_call iCall =  (info_call)bsController.getAction_config().get_actioncalls().get(id_complete);
					id_action = iCall.getOwner();
					if(	id_action!=null &&
						auth.get_actions_permitted().get(id_action)!=null &&
						auth.get_actions_forbidden().get(id_complete)==null){}
					else result = false;
				}else result = false;
			}else result = false;
		}
		return result;
		
	}
	public boolean check_redirectIsPermitted(auth_init auth, i_action _action){	
		
		if(_action!=null)
			_action = _action.asAction();
		
		if(bsController.getAppInit().get_avoid_permission_chech()!=null && bsController.getAppInit().get_avoid_permission_chech().equalsIgnoreCase("true"))
			return true;
		
		if(	_action==null ||
			_action.getCurrent_redirect()==null ||
			_action.getCurrent_redirect().is_avoidPermissionCheck())
			return true;
		
		if(	_action!=null &&
			_action.get_infoaction()!=null &&
			(_action.get_infoaction().getProperty("allway").equalsIgnoreCase("public") || _action.get_infoaction().getProperty("always").equals("public"))
			)
			
			return true;

		
		if(_action.get_infoaction().getPath().equals(bsController.CONST_ID_$ACTION_HELP)) return true;
		if(bsController.getAppInit().get_enterpoint()!=null && !bsController.getAppInit().get_enterpoint().equals("*")){
			if(_action.get_infoaction().getPath().equals(bsController.getAppInit().get_enterpoint())) return true;
		}else{
			if(auth==null) return false;
			check_authIsFull(auth);
		}
		if(_action.getCurrent_redirect().get_uri().indexOf("/Controller?")==0) return true;

		if(auth!=null && (auth.get_actions_permitted()==null || auth.get_actions_permitted().size()==0)) return false;
		if(_action.getCurrent_redirect().get_inforedirect()==null) return true;
	
		boolean result=true;
		
		String id_complete = _action.get_infoaction().getPath();
		
		info_action iAction_complete = (info_action)auth.get_actions_permitted().get(id_complete);

		if(	iAction_complete==null){
			if(bsController.getAppInit().get_actioncall_separator()!=null && !bsController.getAppInit().get_actioncall_separator().equals("")){
				char separator=bsController.getAppInit().get_actioncall_separator().charAt(0);
//				String id_call=null;
				String id_action=null;
				if(id_complete!=null && id_complete.indexOf(separator)>-1){
					try{
//						id_call = id_complete.substring(id_complete.indexOf(separator)+1,id_complete.length());
						id_action = id_complete.substring(0,id_complete.indexOf(separator));
					}catch(Exception e){
					}
					if(	id_action!=null &&
						auth.get_actions_permitted().get(id_action)!=null &&
						auth.get_actions_forbidden().get(id_complete)==null){
						info_action iAction = (info_action)auth.get_actions_permitted().get(id_action);
						if(iAction==null) return false;
						else{
							if(	iAction.get_redirects().get(_action.getCurrent_redirect().get_inforedirect().getPath())==null)  result = false;
						}
					}else if(bsController.getAction_config().get_actioncalls().get(id_complete)!=null){
						info_call iCall =  (info_call)bsController.getAction_config().get_actioncalls().get(id_complete);
						id_action = iCall.getOwner();
						if(	id_action!=null &&
							auth.get_actions_permitted().get(id_action)!=null &&
							auth.get_actions_forbidden().get(id_complete)==null){}
						else result = false;
					}else return false;
				}else return false;
				
			}else return false;
		}else{
			if(	iAction_complete.get_redirects().get(_action.getCurrent_redirect().get_inforedirect().getPath())==null){
				if(	iAction_complete.get_redirects().get("*")==null)
					result = false;
			}
		}
		return result;
	}

}
