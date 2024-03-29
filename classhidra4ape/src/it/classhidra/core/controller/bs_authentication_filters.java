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
				HashMap hAllowedActions = null;
				try{
					hAllowedActions = (HashMap)((HashMap)bsController.getAuth_config().get_targets_allowed().get(_target)).get(_role);
				}catch(Exception e){
				}
				


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
							if(	hActions.get(keys.get(i))!=null &&
								((HashMap)hActions.get(keys.get(i))).get("*")!=null &&
								((HashMap)((HashMap)hActions.get(keys.get(i))).get("*")).get("*")!=null
							){
								
								if(	hAllowedActions!=null &&
									hAllowedActions.get(keys.get(i))!=null &&
									((HashMap)hAllowedActions.get(keys.get(i))).get("*")!=null &&
									((HashMap)((HashMap)hAllowedActions.get(keys.get(i))).get("*")).get("*")!=null
								){
									
								}else								
									permited_current.remove(keys.get(i));
							}
							else{
								if(	hAllowedActions!=null &&
										hAllowedActions.get(keys.get(i))!=null &&
										((HashMap)hAllowedActions.get(keys.get(i))).get("*")!=null){
								}else{
									Vector keys_redirect = new Vector(((HashMap)hActions.get(keys.get(i))).keySet());
									for(int j=0;j<keys_redirect.size();j++){
										if(	hAllowedActions!=null &&
												hAllowedActions.get(keys.get(i))!=null &&
												((HashMap)hAllowedActions.get(keys.get(i))).get(keys_redirect.get(j))!=null &&
												((HashMap)((HashMap)hAllowedActions.get(keys.get(i))).get(keys_redirect.get(j))).get("*")!=null){
										}else{
											
											HashMap permited_current_auth_redirects = ((info_action)permited_current.get(keys.get(i))).get_auth_redirects();
											HashMap permited_current_redirects = ((info_action)permited_current.get(keys.get(i))).get_redirects();
											info_redirect ir_a = (info_redirect)permited_current_auth_redirects.get(keys_redirect.get(j));
		
											
											if(ir_a!=null){
												info_redirect ir = (info_redirect)permited_current_redirects.get(ir_a.getPath());
												
												Vector keys_sections= new Vector( ((HashMap)((HashMap)hActions.get(keys.get(i))).get(keys_redirect.get(j))).keySet());
												for(int k=0;k<keys_sections.size();k++){
													if(	hAllowedActions!=null &&
															hAllowedActions.get(keys.get(i))!=null &&
															((HashMap)hAllowedActions.get(keys.get(i))).get(keys_redirect.get(j))!=null &&
															((HashMap)((HashMap)hAllowedActions.get(keys.get(i))).get(keys_redirect.get(j))).get(keys_sections.get(k))!=null){
													}else{
														((info_section)ir.get_sections().get(keys_sections.get(k))).setAllowed(false);
														((info_section)ir_a.get_sections().get(keys_sections.get(k))).setAllowed(false);
														if(keys_sections.get(k).equals("*")){
															((info_redirect)permited_current_auth_redirects.get(keys_redirect.get(j))).setAllowedSection(false);
															String ir_path = ir.getPath();
															((info_redirect)permited_current_redirects.get(ir_path)).setAllowedSection(false);
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
	
		if(id_complete!=null && id_complete.equals(bsController.CONST_ID_$ACTION_HELP)) return true;
		if(!bsController.getAppInit().get_enterpoint().equals("*")){
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
				}else result = false;
			}else result = false;
		}
		return result;
		
	}
	public boolean check_redirectIsPermitted(auth_init auth, i_action _action){	
		
		if(_action.get_infoaction().getPath().equals(bsController.CONST_ID_$ACTION_HELP)) return true;
		if(!bsController.getAppInit().get_enterpoint().equals("*")){
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
					}
					else return false;
				}else return false;
				
			}else return false;
		}else{
			if(	iAction_complete.get_redirects().get(_action.getCurrent_redirect().get_inforedirect().getPath())==null)  result = false;
		}
		return result;
	}

}
