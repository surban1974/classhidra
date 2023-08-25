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


import java.io.Serializable;
import java.util.Properties;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_file;



public class jwt_init implements Serializable{


	private static final long serialVersionUID = -1L;
	
	public static final String REQUEST_HEADER_AUTHORIZATION="Authorization";
	
	public static final String REQUEST_HEADER_AUTHORIZATION_VALUE_BEARER_TYPE="Bearer";


	

	public final static String id_property							= 	"classhidra_jwt";
	public final static String id_jwt_issuer 						= 	"jwt.issuer";
	public final static String id_jwt_subject 						= 	"jwt.subject";
	public final static String id_jwt_secret 						= 	"jwt.secret";
	public final static String id_jwt_expiresAt 					=	"jwt.expiresAt";
	public final static String id_jwt_notBefore 					=	"jwt.notBefore";
	public final static String id_jwt_reloadIfExpireIn				=	"jwt.reloadIfExpireIn";
	public final static String id_jwt_cookie_name 					=	"jwt.cookie.name";
	public final static String id_jwt_cookie_path 					=	"jwt.cookie.path";
	public final static String id_jwt_cookie_domain 				=	"jwt.cookie.domain";


	private String _jwt_issuer;
	private String _jwt_subject;
	private String _jwt_secret;
	private String _jwt_expiresAt;
	private String _jwt_reloadIfExpireIn;
	private String _jwt_notBefore;
	private String _jwt_cookie_name;
	private String _jwt_cookie_path;
	private String _jwt_cookie_domain;



	public String get_jwt_cookie_domain() {
		return _jwt_cookie_domain;
	}

	public void set_jwt_cookie_domain(String _jwt_cookie_domain) {
		this._jwt_cookie_domain = _jwt_cookie_domain;
	}

	private String loadedFrom="";

	public jwt_init(){
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

		String property_name = System.getProperty(id_property);
		if(property_name==null || property_name.equals(""))
			property_name = System.getProperty(app_path+id_property);

		if(property_name==null || property_name.equals(""))
			property_name = ainit.getResources_path().getProperty(id_property);

		if(property_name==null || property_name.equals(""))
			property_name = ainit.getResources_path().getProperty(app_path+id_property);


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

		if(property==null) {
			property = System.getProperties();
			loadedFrom+=" System.property";
		}else
			loadedFrom+=" "+property_name;
		if(property!=null){
			_jwt_issuer = (property.getProperty(id_jwt_issuer)==null)?"":property.getProperty(id_jwt_issuer);
			_jwt_subject = (property.getProperty(id_jwt_subject)==null)?"":property.getProperty(id_jwt_subject);
			_jwt_secret = (property.getProperty(id_jwt_secret)==null)?"":property.getProperty(id_jwt_secret);
			_jwt_expiresAt = (property.getProperty(id_jwt_expiresAt)==null)?"":property.getProperty(id_jwt_expiresAt);
			_jwt_reloadIfExpireIn = (property.getProperty(id_jwt_reloadIfExpireIn)==null)?"":property.getProperty(id_jwt_reloadIfExpireIn);
			_jwt_notBefore = (property.getProperty(id_jwt_notBefore)==null)?"":property.getProperty(id_jwt_notBefore);
			_jwt_cookie_name = (property.getProperty(id_jwt_cookie_name)==null)?"":property.getProperty(id_jwt_cookie_name);
			_jwt_cookie_path = (property.getProperty(id_jwt_cookie_path)==null)?"":property.getProperty(id_jwt_cookie_path);
			_jwt_cookie_domain = (property.getProperty(id_jwt_cookie_domain)==null)?"":property.getProperty(id_jwt_cookie_domain);
		}
	}



	public void reimposta(){
		_jwt_issuer="";
		_jwt_subject="";
		_jwt_secret="";
		_jwt_expiresAt="";
		_jwt_reloadIfExpireIn="";
		_jwt_notBefore="";
		_jwt_cookie_name="";
		_jwt_cookie_path="";
		_jwt_cookie_domain="";
	}

	public String get_jwt_reloadIfExpireIn() {
		return _jwt_reloadIfExpireIn;
	}

	public void set_jwt_reloadIfExpireIn(String _jwt_reloadIfExpireIn) {
		this._jwt_reloadIfExpireIn = _jwt_reloadIfExpireIn;
	}

	public String get_jwt_issuer() {
		return _jwt_issuer;
	}

	public void set_jwt_issuer(String _jwt_issuer) {
		this._jwt_issuer = _jwt_issuer;
	}

	public String get_jwt_subject() {
		return _jwt_subject;
	}

	public void set_jwt_subject(String _jwt_subject) {
		this._jwt_subject = _jwt_subject;
	}

	public String get_jwt_secret() {
		return _jwt_secret;
	}

	public void set_jwt_secret(String _jwt_secret) {
		this._jwt_secret = _jwt_secret;
	}

	public String get_jwt_expiresAt() {
		return _jwt_expiresAt;
	}

	public void set_jwt_expiresAt(String _jwt_expiresAt) {
		this._jwt_expiresAt = _jwt_expiresAt;
	}

	public String get_jwt_notBefore() {
		return _jwt_notBefore;
	}

	public void set_jwt_notBefore(String _jwt_notBefore) {
		this._jwt_notBefore = _jwt_notBefore;
	}

	public String get_jwt_cookie_name() {
		return _jwt_cookie_name;
	}

	public void set_jwt_cookie_name(String _jwt_cookie_name) {
		this._jwt_cookie_name = _jwt_cookie_name;
	}

	public String get_jwt_cookie_path() {
		return _jwt_cookie_path;
	}

	public void set_jwt_cookie_path(String _jwt_cookie_path) {
		this._jwt_cookie_path = _jwt_cookie_path;
	}

	public String getLoadedFrom() {
		return loadedFrom;
	}

	public void setLoadedFrom(String loadedFrom) {
		this.loadedFrom = loadedFrom;
	}




}
