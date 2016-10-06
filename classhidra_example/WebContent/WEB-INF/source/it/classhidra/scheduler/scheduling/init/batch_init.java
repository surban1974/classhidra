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

package it.classhidra.scheduler.scheduling.init;


import it.classhidra.core.controller.bsController;
import it.classhidra.core.init.app_init;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.util.util_blob;
import it.classhidra.core.tool.util.util_file;
import it.classhidra.scheduler.common.i_4Batch;
import it.classhidra.scheduler.scheduling.DriverScheduling;
import it.classhidra.scheduler.scheduling.implementation.mysql.db_4Batch;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Properties;

public class batch_init implements Serializable{
	private static final long serialVersionUID = -1L;
	public static final String environment_id_property =			"application.batch.property";

	public static final String id_active=							"active";
	public static final String id_db_prefix=						"db_prefix";
	public static final String id_sleep=							"period.sleep";
	public static final String id_scan= 							"period.batch.scan";
	public static final String id_stub=								"stub.data.manager";

	private String _active="false";
	private String _sleep;
	private String _scan;
	private String _db_prefix="";
	private String _stub="";
	private String loadedFrom="";
	

public batch_init() {
	super();
	init();
}

public batch_init(Properties properties) {
	super();
	if(properties!=null)
		init(properties);
}

public void init() {
	
	Properties property = null;
	String app_path="";
	app_init ainit = bsController.getAppInit();

	try{

		if(ainit.get_db_name()!=null && ainit.isDb_name_valid()){
			if(initDB(ainit.get_path(),ainit.get_db_name())){
				loadedFrom=ainit.get_db_name();
				return;
			}
		}


		app_path=ainit.get_path();
		if(app_path==null || app_path.equals("")) app_path="";
		else app_path+=".";
	}catch(Exception e){
	}

	String property_name = System.getProperty(environment_id_property);
	if(property_name==null || property_name.equals(""))
		property_name = System.getProperty(app_path+environment_id_property);



	
	if(property_name==null || property_name.equals("")){
		property_name="classhidra_scheduler";
		try{
			property = util_file.loadProperty(ainit.get_path_config()+property_name);
			loadedFrom+=" "+ainit.get_path_config()+property_name;
		}catch(Exception e){
			try{
				property = util_file.loadProperty(property_name);
				loadedFrom+=" "+property_name;
			}catch(Exception ex){
			}
		}


		if(property==null){
			try{
				property_name="it.classhidra.scheduler.default";
				property = util_file.loadProperty(property_name);
				loadedFrom+=" "+property_name;
			}catch (Exception e) {
			}
		}

		if(property!=null){
			init(property);
		}else{		
			_active = (System.getProperty(id_active)==null)?_active:System.getProperty(id_active);
			_sleep = (System.getProperty(id_sleep)==null)?_sleep:System.getProperty(id_sleep);
			_scan = (System.getProperty(id_scan)==null)?_scan:System.getProperty(id_scan);
			_db_prefix = (System.getProperty(id_db_prefix)==null)?_db_prefix:System.getProperty(id_db_prefix);
			_stub = (System.getProperty(id_stub)==null)?_stub:System.getProperty(id_stub);
			
			if(_sleep!=null && _scan!=null) loadedFrom="System.property";

		}
	}

}

public void init(Properties ex_property) {
	_active = (ex_property.getProperty(id_active)==null)?_active:ex_property.getProperty(id_active);
	_sleep = (ex_property.getProperty(id_sleep)==null)?_sleep:ex_property.getProperty(id_sleep);
	_scan = (ex_property.getProperty(id_scan)==null)?_scan:ex_property.getProperty(id_scan);
	_db_prefix = (ex_property.getProperty(id_db_prefix)==null)?_db_prefix:ex_property.getProperty(id_db_prefix);
	_stub = (ex_property.getProperty(id_stub)==null)?_stub:ex_property.getProperty(id_stub);

}

public String get_sleep() {
	return _sleep;
}

public long getLsleep() {
	try{
		return Long.parseLong(_sleep);
	}catch(Exception e){
		return 0;
	}
}

public void set_sleep(String _sleep) {
	this._sleep = _sleep;
}

public String get_scan() {
	return _scan;
}



public void set_scan(String _scan) {
	this._scan = _scan;
}

public String getInfo() {
	return "sleep: "+ _sleep + "; scan: "+_scan+"; db_prefix: "+_db_prefix;
}

public String getLoadedFrom() {
	return loadedFrom;
}

public i_4Batch get4BatchManager(){
//Modified 20161005	
	if(	DriverScheduling.getBatchManager()!=null &&
		(_stub!=null && !_stub.equalsIgnoreCase("empty") && DriverScheduling.getBatchManager().getClass().getName().equalsIgnoreCase(_stub))
	)
		return DriverScheduling.getBatchManager();
//--	
	
	i_4Batch batchManager = null;
	if(_stub!=null && _stub.equalsIgnoreCase("empty")){
		batchManager = new i_4Batch() {
			public Object operation(String oper, HashMap form) throws Exception {
				return null;
			}
		};
	}else if(_stub!=null && !_stub.equals("")){
		try{
			batchManager = (i_4Batch)Class.forName(_stub).newInstance();
		}catch(Exception e){		
		}catch(Throwable e){		
		}
	}
	if(batchManager==null) 
		batchManager = new db_4Batch();
	
//Modified 20161005	
	DriverScheduling.setBatchManager(batchManager);
//--	
	return DriverScheduling.getBatchManager();
}

public boolean initDB(String path, String db_name) throws bsControllerException, Exception{
	
	String propData = null;
	
	try{
		propData = util_blob.load_from_config(path+"."+environment_id_property, db_name);
		if(propData==null) propData = util_blob.load_from_config(environment_id_property, db_name);
	}catch(Exception e){
		
	}
	if(propData==null) return false;
	
	Properties ex_properties = new Properties();
	
	try{
		ex_properties.load(new ByteArrayInputStream(propData.getBytes()));
		init(ex_properties);
		return true;
	}catch(Exception e){
		return false;
	}
	


}

public String get_active() {
	return _active;
}

public void set_active(String active) {
	_active = active;
}

public String get_db_prefix() {
	return _db_prefix;
}

public void set_db_prefix(String _db_prefix) {
	this._db_prefix = _db_prefix;
}

public batch_init set_stub(String _stub) {
	this._stub = _stub;
	return this;
}
}
