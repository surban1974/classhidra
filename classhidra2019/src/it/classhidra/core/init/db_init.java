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

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_file;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Properties;


public class db_init implements Serializable{
	private static final long serialVersionUID = -296126065245352700L;

	public final static String id_property				= 	"classhidra_db";
	public final static String id_driver				= 	"application.db.driver";
	public final static String id_url 					= 	"application.db.url";
	public final static String id_server 				= 	"application.db.server";
	public final static String id_dbname 				=	"application.db.dbname";
	public final static String id_datasource 			=	"application.db.datasource";
	public final static String id_connectiontype 		=	"application.db.connectiontype";
	public final static String id_user 					=	"application.db.user";
	public final static String id_password 				=	"application.db.password";
	public final static String id_allwayone 			=	"application.db.allwayone";
	public final static String id 						=	"application.db.id";

	public final static String id_local_pool_size 		=	"application.db.local_pool_size";
	public final static String id_local_pool_timeout 	=	"application.db.local_pool_timeout";
	public final static String id_local_pool_delay 		=	"application.db.local_pool_delay";
	public final static String id_local_pool_provider	=	"application.db.local_pool_provider";

	private String _driver;
	private String _url;
	private String _server;
	private String _dbname;
	private String _datasource;
	private String _connectiontype;
	private String _user;
	private String _password;
	private String _allwayone;
	private String _id;

	private String _local_pool_size;
	private String _local_pool_timeout;
	private String _local_pool_delay;
	private String _local_pool_provider;


	private HashMap<String,db_init> _another_db_init;

	private String loadedFrom="";

	public final static String CT_DRIVERMANAGER 	= 	"drivermanager";
	public final static String CT_JSQLDATASOURCE 	= 	"javaxsqldatasource";
	public final static String CT_POOLDATASOURCE4 	= 	"pooldatasourcev4";
	public final static String CT_POOLDATASOURCE41 	= 	"pooldatasourcev41";
	public final static String CT_POOLDATASOURCE5 	= 	"pooldatasourcev5";
	public final static String CT_POOLDATASOURCE51 	= 	"pooldatasourcev51";
	public final static String CT_LOCALPOOL 		= 	"localpool";

public db_init() {
	super();
}

public void init() {
	if(_another_db_init==null) _another_db_init=new HashMap<String, db_init>();
	Properties property = null;
	String app_path="";
	app_init ainit = bsController.getAppInit();
	try{

		app_path=ainit.get_path();
		if(app_path==null || app_path.equals("")) app_path="";
		else app_path+=".";
	}catch(Exception e){
	}

	String property_name = System.getProperty(bsConstants.db_id_property);
	if(property_name==null || property_name.equals(""))
		property_name = System.getProperty(app_path+bsConstants.db_id_property);

	if(property_name==null || property_name.equals(""))
		property_name = ainit.getResources_path().getProperty(bsController.db_id_property);

	if(property_name==null || property_name.equals(""))
		property_name = ainit.getResources_path().getProperty(app_path+bsController.db_id_property);

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
				new bsException(ex,iStub.log_DEBUG);
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
		init(property,"");
		int ids=0;
		boolean valid=true;
		while(valid){
			db_init current = new db_init();
			valid = current.init(property, Integer.valueOf(ids).toString());
			if(!valid && ids==0){
				ids++;
				valid = current.init(property, Integer.valueOf(ids).toString());
			}
			if(valid && current.get_id()!=null) _another_db_init.put(current.get_id(),current);
			ids++;
		}
	}else{
		loadedFrom+=" System.property";
		_id = (_id==null)?System.getProperty(id):_id;
			if(_id==null) _id="0";
		_driver = (_driver==null)?System.getProperty(id_driver):_driver;
		_url = (_url==null)?System.getProperty(id_url):_url;
		_server = (_server==null)?System.getProperty(id_server):_server;
		_dbname = (_dbname==null)?System.getProperty(id_dbname):_dbname;
		_datasource = (_datasource==null)?System.getProperty(id_datasource):_datasource;
		_connectiontype = (_connectiontype==null)?System.getProperty(id_connectiontype):_connectiontype;
		_user = (_user==null)?System.getProperty(id_user):_user;
		_password = (_password==null)?System.getProperty(id_password):_password;
		_allwayone = (_allwayone==null)?System.getProperty(id_allwayone):_password;

		_local_pool_size = (_local_pool_size==null)?System.getProperty(id_local_pool_size):_local_pool_size;
		_local_pool_timeout = (_local_pool_timeout==null)?System.getProperty(id_local_pool_timeout):_local_pool_timeout;
		_local_pool_delay = (_local_pool_delay==null)?System.getProperty(id_local_pool_delay):_local_pool_delay;
		_local_pool_provider = (_local_pool_provider==null)?System.getProperty(id_local_pool_provider):_local_pool_provider;
	}
}

public boolean init(Properties property, String prefix) {
	if(_another_db_init==null) _another_db_init=new HashMap<String, db_init>();
	if(prefix.equals("")){
		_id = (_id==null)?property.getProperty(id):_id;
		_connectiontype = (_connectiontype==null)?property.getProperty(id_connectiontype):_connectiontype;
		_driver = (_driver==null)?property.getProperty(id_driver):_driver;
		_url = (_url==null)?property.getProperty(id_url):_url;
		_server = (_server==null)?property.getProperty(id_server):_server;
		_dbname = (_dbname==null)?property.getProperty(id_dbname):_dbname;
		_datasource = (_datasource==null)?property.getProperty(id_datasource):_datasource;
		_user = (_user==null)?property.getProperty(id_user):_user;
		_password = (_password==null)?property.getProperty(id_password):_password;
		_allwayone = (_allwayone==null)?property.getProperty(id_allwayone):_password;

		_local_pool_size = (_local_pool_size==null)?property.getProperty(id_local_pool_size):_local_pool_size;
		_local_pool_timeout = (_local_pool_timeout==null)?property.getProperty(id_local_pool_timeout):_local_pool_timeout;
		_local_pool_delay = (_local_pool_delay==null)?property.getProperty(id_local_pool_delay):_local_pool_delay;
		_local_pool_provider = (_local_pool_provider==null)?property.getProperty(id_local_pool_provider):_local_pool_provider;

		return true;
	}else{
		if(property.getProperty(id_connectiontype+"."+prefix)==null) return false;
		_id = property.getProperty(id+"."+prefix);
		if(_id==null)
			_id=prefix;
		_connectiontype = property.getProperty(id_connectiontype+"."+prefix);
		_driver = property.getProperty(id_driver+"."+prefix);
		_url = property.getProperty(id_url+"."+prefix);
		_server = property.getProperty(id_server+"."+prefix);
		_dbname = property.getProperty(id_dbname+"."+prefix);
		_datasource = property.getProperty(id_datasource+"."+prefix);
		_user = property.getProperty(id_user+"."+prefix);
		_password = property.getProperty(id_password+"."+prefix);
		_allwayone = property.getProperty(id_allwayone+"."+prefix);

		_local_pool_size = property.getProperty(id_local_pool_size+"."+prefix);
		_local_pool_timeout = property.getProperty(id_local_pool_timeout+"."+prefix);
		_local_pool_delay = property.getProperty(id_local_pool_delay+"."+prefix);
		_local_pool_provider = property.getProperty(id_local_pool_provider+"."+prefix);

		return (_driver!=null)||(_url!=null)||(_server!=null)||(_dbname!=null)||(_datasource!=null)||(_user!=null)||(_password!=null);
	}

}


public String get_connectiontype() {
	return _connectiontype;
}
public String get_datasource() {
	return _datasource;
}
public String get_dbname() {
	return _dbname;
}
public String get_driver() {
	return _driver;
}
public String get_password() {
	return _password;
}
public String get_server() {
	return _server;
}
public String get_url() {
	return _url;
}
public String get_user() {
	return _user;
}
public void set_connectiontype(String string) {
	_connectiontype = string;
}
public void set_datasource(String string) {
	_datasource = string;
}
public void set_dbname(String string) {
	_dbname = string;
}
public void set_driver(String string) {
	_driver = string;
}
public void set_password(String string) {
	_password = string;
}
public void set_server(String string) {
	_server = string;
}
public void set_url(String string) {
	_url = string;
}
public void set_user(String string) {
	_user = string;
}
	public HashMap<String,db_init> get_another_db_init() {
		return _another_db_init;
	}

	public void set_another_db_init(HashMap<String,db_init> map) {
		_another_db_init = map;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String string) {
		_id = string;
	}

	public String get_local_pool_size() {
		return _local_pool_size;
	}

	public void set_local_pool_size(String _local_pool_size) {
		this._local_pool_size = _local_pool_size;
	}

	public String get_local_pool_timeout() {
		return _local_pool_timeout;
	}

	public void set_local_pool_timeout(String _local_pool_timeout) {
		this._local_pool_timeout = _local_pool_timeout;
	}

	public String get_local_pool_delay() {
		return _local_pool_delay;
	}

	public void set_local_pool_delay(String _local_pool_delay) {
		this._local_pool_delay = _local_pool_delay;
	}

	public String get_local_pool_provider() {
		return _local_pool_provider;
	}

	public void set_local_pool_provider(String _local_pool_provider) {
		this._local_pool_provider = _local_pool_provider;
	}

	public String get_allwayone() {
		if(_allwayone==null) return "false";
		return _allwayone;
	}

	public void set_allwayone(String _allwayone) {
		this._allwayone = _allwayone;
	}

	public String getLoadedFrom() {
		return loadedFrom;
	}

}
