/**
* Creation date: (07/04/2006)
* @author: Svyatoslav Urbanovych svyatoslav.urbanovych@gmail.com
*/

/********************************************************************************
*
*	Copyright (C) 2005  Svyatoslav Urbanovych
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*********************************************************************************/
package it.classhidra.core.init;


import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Properties;

import it.classhidra.core.i_authentication_filter;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_auth_manager;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.info_action;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.jaas_authentication.info_group;
import it.classhidra.core.tool.jaas_authentication.info_target;
import it.classhidra.core.tool.jaas_authentication.info_user;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_auth;
import it.classhidra.core.tool.util.util_file;
import it.classhidra.core.tool.util.util_provider;



public class auth_init implements Serializable{


	private static final long serialVersionUID = -7458249663298875859L;


	public final static String id_property				= 	"classhidra_auth";
	public final static String id_manager 				= 	"application.auth.manager";
	public final static String id_authentication_filter = 	"application.auth.filter";
	public final static String id_jaas_systemname 		= 	"application.auth.jaas_systemname";
	public final static String id_jaas_defaultfilename 	=	"application.auth.jaas_fileconfig";
	public final static String id_jaas_policy 			=	"application.auth.policy";


	private String jaas_managername;
	private String jaas_systemname;
	private String jaas_defaultfilename;
	private String jaas_policyname;

	private String name_authentication_filter;


	private info_user infouser;
	private info_target infotarget;
	private info_group infogroup;
	private String _user;
	private String _userDesc;
	private String _ruolo;
	private String _ruoloDesc;
	private String _ruoli;
	private String _language;
	private String _language_profile;
	private String _country;
	private String _timezone;
	private String _matricola;
	private String _mail;
	private String _target;
	private String _user_ip;



	private String _ticker;
	private String _sessionId;


	private String _wac_fascia;
	private String _risoluzione;

	private BigDecimal _cd_ist;

	private HashMap<String,Object> requestHeader;
	private HashMap<String,info_action> _actions_allowed;
	private HashMap<String,info_action> _actions_forbidden;
	

	private HashMap<String,Object> _target_property;
	private HashMap<String,Object> _user_property;

	private i_auth_manager _manager;
	private i_authentication_filter _authentication_filter;

	private boolean _logged;

	private String loadedFrom="";

	public auth_init(){
		super();
		reimposta();
		init();

	}

	public void init() {
		Properties property = null;

		String app_path="";
		app_init ainit = bsController.getAppInit();
		try{

			app_path=ainit.get_path();
			if(app_path==null || app_path.equals("")) app_path="";
			else app_path+=".";
		}catch(Exception e){
		}

		String property_name = System.getProperty(bsController.auth_id_property);
		if(property_name==null || property_name.equals(""))
			property_name = System.getProperty(app_path+bsController.auth_id_property);

		if(property_name==null || property_name.equals(""))
			property_name = ainit.getResources_path().getProperty(bsController.auth_id_property);

		if(property_name==null || property_name.equals(""))
			property_name = ainit.getResources_path().getProperty(app_path+bsController.auth_id_property);


		if(property_name==null || property_name.equals("")){
			property_name = id_property;
			try{
				property = util_file.loadProperty(ainit.get_path_config()+property_name);
				loadedFrom+=" "+ainit.get_path_config()+property_name;
			}catch(Exception e){
				try{
					property = util_file.loadProperty(property_name);
					loadedFrom+=" "+property_name;
				}catch(Exception ex){
//					new bsException(ex,iStub.log_DEBUG);
				}
			}
		}else{
			try{
				property = util_file.loadProperty(property_name);
				loadedFrom+=" "+property_name;
			}catch(Exception e){
				new bsException(e,iStub.log_DEBUG);
			}
		}

		if(property!=null){
			loadedFrom+=" "+property_name;
			jaas_systemname = (property.getProperty(id_jaas_systemname)==null)?"":property.getProperty(id_jaas_systemname);
			jaas_defaultfilename = (property.getProperty(id_jaas_defaultfilename)==null)?"":property.getProperty(id_jaas_defaultfilename);
			jaas_policyname = (property.getProperty(id_jaas_policy)==null)?"":property.getProperty(id_jaas_policy);

			try{
				if(System.getProperty(jaas_systemname)==null || System.getProperty(jaas_systemname).equals("")){

					File jaas_config = new File(jaas_defaultfilename);
					if(jaas_config.exists())
						System.setProperty(jaas_systemname,jaas_config.getAbsolutePath() );
					else{
						jaas_config = new File(bsController.getAppInit().get_path_config() + jaas_defaultfilename);
						System.setProperty(jaas_systemname,jaas_config.getAbsolutePath());
					}
//				System.setProperty(jaas_systemname, (bsController.getAppInit().get_path_config() + jaas_defaultfilename).replace('\\', '/'));
				}
			}catch(Exception e){
			}catch(Throwable e){
			}


			jaas_managername = (property.getProperty(id_manager)==null)?"":property.getProperty(id_manager);
			if(!jaas_managername.equals("")){
				try{
					_manager = (i_auth_manager)util_provider.getInstanceFromProvider(
									new String[]{
											bsController.getAppInit().get_context_provider(),
											bsController.getAppInit().get_cdi_provider(),
											bsController.getAppInit().get_ejb_provider()
									},
									jaas_managername);
				}catch(Exception e){
				}
			}

			name_authentication_filter = (property.getProperty(id_authentication_filter)==null)?"it.classhidra.core.controller.bs_authentication_filters":property.getProperty(id_authentication_filter);
			if(!name_authentication_filter.equals("")){
				try{
					_authentication_filter = (i_authentication_filter)util_provider.getInstanceFromProvider(
									new String[]{
											bsController.getAppInit().get_context_provider(),
											bsController.getAppInit().get_cdi_provider(),
											bsController.getAppInit().get_ejb_provider()
									},
									name_authentication_filter);
				}catch(Exception e){
				}
			}
		}else{
			name_authentication_filter = "it.classhidra.core.controller.bs_authentication_filters";
			if(!name_authentication_filter.equals("")){
				try{
					_authentication_filter = (i_authentication_filter)util_provider.getInstanceFromProvider(
									new String[]{
											bsController.getAppInit().get_context_provider(),
											bsController.getAppInit().get_cdi_provider(),
											bsController.getAppInit().get_ejb_provider()
									},
									name_authentication_filter);
				}catch(Exception e){
				}
			}
			
		}

	}

	public void init(String user, String password, Object obj_request){
		util_auth.init(this, user, password, obj_request);
	}

	public void reInit(auth_init another){
		if(another==null)
			return;
		jaas_managername = another.getJaas_managername();
		jaas_systemname = another.getJaas_systemname();
		jaas_defaultfilename = another.getJaas_defaultfilename();
		jaas_policyname = another.getJaas_policyname();

		name_authentication_filter = another.getName_authentication_filter();

		infouser = another.getInfouser();
		infotarget = another.getInfotarget();
		infogroup = another.getInfogroup();
		_user = another.get_user();
		_userDesc = another.get_userDesc();
		_ruolo = another.get_ruolo();
		_ruoloDesc = another.get_ruoloDesc();
		_ruoli = another.get_ruoli();
		_language = another.get_language();
		_language_profile = another.get_language_profile();
		_country = another.get_country();
		_timezone = another.get_timezone();
		_matricola = another.get_matricola();
		_mail = another.get_mail();
		_target = another.get_target();
		_user_ip = another.get_user_ip();
		
		_ticker = another.get_ticker();		
		_sessionId = another.get_sessionId();

		_wac_fascia = another.get_wac_fascia();
		_risoluzione = another.get_risoluzione();

		_cd_ist = another.get_cd_ist();

		requestHeader = another.getRequestHeader();
		_actions_allowed = another.get_actions_allowed();
		_actions_forbidden = another.get_actions_forbidden();
		

		_target_property = another.get_target_property();
		_user_property = another.get_user_property();

		_manager = another.get_manager();
		_authentication_filter = another.get_authentication_filter();

		_logged = another.is_logged();

		loadedFrom = another.getLoadedFrom();
	}

	public void reimposta(){
		infouser=new info_user();
		infotarget=new info_target();
		infogroup=new info_group();
		_user="";
		_userDesc="";
		_ruolo="";
		_ruoloDesc="";
		_ruoli="";
		_matricola="";
		_mail="";
		_language="EN";
		_language_profile="EN";
		_country="";
		_timezone="";
		_target="";
		_user_ip="";
		_ticker="";
		_sessionId="";
		_risoluzione="";



		_wac_fascia="01";


		_logged = false;
		requestHeader = new HashMap<String, Object>();
		_actions_allowed = new HashMap<String, info_action>();
		_actions_forbidden = new HashMap<String, info_action>();
		_target_property = new HashMap<String, Object>();
		_user_property = new HashMap<String, Object>();
	}



	public boolean readTicker(Object obj_request){
		return util_auth.readTicker(this, obj_request);
	}

	public boolean saveTicker(Object obj_response){
		return util_auth.saveTicker(this, obj_response);
	}

	public boolean saveFromForm(i_bean bean, Object obj_request, Object obj_response){
		return util_auth.saveFromForm(this, bean, obj_request, obj_response);
	}

	public void init(Object obj_request) throws bsException{
		util_auth.init(this, obj_request);
	}

	public String get_ruolo() {
		return _ruolo;
	}
	public String get_user() {
		return _user;
	}
	public void set_ruolo(String string) {
		_ruolo = string;
	}
	public void set_user(String string) {
		_user = string;
	}
	public String get_language() {
		return _language;
	}
	public void set_language(String string) {
		_language = string;
	}

	public BigDecimal get_cd_ist() {
		return _cd_ist;
	}
	public void set_cd_ist(BigDecimal decimal) {
		_cd_ist = decimal;
	}
	public String get_header(String id){
		if(requestHeader.get(id)==null) return "";
		return (String)requestHeader.get(id);
	}

	public String get_target() {
		return _target;
	}
	public void set_target(String string) {
		_target = string;
	}

	public String get_wac_fascia() {
		return _wac_fascia;
	}
	public void set_wac_fascia(String string) {
		_wac_fascia = string;
	}
	public String get_matricola() {
		return _matricola;
	}
	public void set_matricola(String string) {
		_matricola = string;
	}
	public void set_matriculation(String string) {
		_matricola = string;
	}	
	public String get_risoluzione() {
		return _risoluzione;
	}
	public void set_risoluzione(String string) {
		_risoluzione = string;
	}
	public String get_ruoloDesc() {
		return _ruoloDesc;
	}
	public void set_ruoloDesc(String string) {
		_ruoloDesc = string;
	}

	public HashMap<String,info_action> get_actions_permitted() {
		return _actions_allowed;
	}

	public void set_actions_permited(HashMap<String,info_action> map) {
		_actions_allowed = map;
	}
	
	public HashMap<String,info_action> get_actions_allowed() {
		return _actions_allowed;
	}

	public void set_actions_allowed(HashMap<String,info_action> map) {
		_actions_allowed = map;
	}
	

	public String get_user_ip() {
		return _user_ip;
	}


	public void set_user_ip(String string) {
		_user_ip = string;
	}

	public HashMap<String,info_action> get_actions_forbidden() {
		return _actions_forbidden;
	}

	public String get_ticker() {
		return _ticker;
	}

	public void set_ticker(String _ticker) {
		this._ticker = _ticker;
	}

	public void set_actions_forbidden(HashMap<String,info_action> map) {
		_actions_forbidden = map;
	}

	public boolean is_logged() {
		return _logged;
	}

	public void set_logged(boolean _logged) {
		this._logged = _logged;
	}

	public String get_ruoli() {
		return _ruoli;
	}

	public void set_ruoli(String _ruoli) {
		this._ruoli = _ruoli;
	}

	public String getJaas_defaultfilename() {
		return jaas_defaultfilename;
	}

	public String getJaas_managername() {
		return jaas_managername;
	}

	public String getJaas_policyname() {
		return jaas_policyname;
	}

	public String getJaas_systemname() {
		return jaas_systemname;
	}

	public String getName_authentication_filter() {
		return name_authentication_filter;
	}

	public void setName_authentication_filter(String name_authentication_filter) {
		this.name_authentication_filter = name_authentication_filter;
	}

	public i_authentication_filter get_authentication_filter() {
		return _authentication_filter;
	}

	public void set_authentication_filter(
			i_authentication_filter _authentication_filter) {
		this._authentication_filter = _authentication_filter;
	}

	public HashMap<String,Object> get_target_property() {
		return _target_property;
	}

	public void set_target_property(HashMap<String,Object> _target_property) {
		this._target_property = _target_property;
	}

	public String get_userDesc() {
		return _userDesc;
	}

	public void set_userDesc(String desc) {
		_userDesc = desc;
	}
	public HashMap<String,Object> get_user_property() {
		return _user_property;
	}

	public void set_user_property(HashMap<String,Object> _user_property) {
		this._user_property = _user_property;
	}



	public i_auth_manager get_manager() {
		return _manager;
	}

	public HashMap<String,Object> getRequestHeader() {
		return requestHeader;
	}

	public String getLoadedFrom() {
		return loadedFrom;
	}

	public boolean convert2xml(){
		return false;
	}
	
	public boolean convert2json(){
		return false;
	}

	public String get_mail() {
		return _mail;
	}

	public void set_mail(String _mail) {
		this._mail = _mail;
	}

	public info_user getInfouser() {
		return infouser;
	}

	public void setInfouser(info_user infouser) {
		this.infouser = infouser;
	}
	
	public String toString(){
		String result="";
		result+=(_user!=null)?_user+"|":"|";
		result+=(_target!=null)?_target+"|":"|";
		result+=(_ruolo!=null)?_ruolo+"|":"|";
		result+=(_matricola!=null)?_matricola+"|":"|";
		result+=(_user_ip!=null)?_user_ip+"|":"|";
		return result;
	}

	public info_target getInfotarget() {
		return infotarget;
	}

	public void setInfotarget(info_target infotarget) {
		this.infotarget = infotarget;
	}

	public info_group getInfogroup() {
		return infogroup;
	}

	public void setInfogroup(info_group infogroup) {
		this.infogroup = infogroup;
	}

	public String get_country() {
		return _country;
	}

	public void set_country(String _country) {
		this._country = _country;
	}

	public String get_timezone() {
		return _timezone;
	}

	public void set_timezone(String _timezone) {
		this._timezone = _timezone;
	}

	public String get_sessionId() {
		return _sessionId;
	}

	public void set_sessionId(String _sessionId) {
		this._sessionId = _sessionId;
	}

	public String get_language_profile() {
		return _language_profile;
	}

	public void set_language_profile(String _language_profile) {
		this._language_profile = _language_profile;
	}

}
