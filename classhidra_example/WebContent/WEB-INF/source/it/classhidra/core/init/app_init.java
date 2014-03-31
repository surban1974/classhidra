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

import it.classhidra.core.tool.util.util_file;
import it.classhidra.core.tool.util.util_format;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;


public class app_init implements Serializable{ 
	public String get_path_root() {
		return _path_root;
	}

	public void set_path_root(String _path_root) {
		this._path_root = _path_root;
	}

	private static final long serialVersionUID = -1L;
	static public String CONST_PAGESIZE					= 	"application.const.pagesize";
	static public String id_path 						= 	"application.path";
	static public String id_enterpoint 					= 	"application.auth.action.enterpoint";
	static public String id_nav_excluded 				= 	"application.auth.tag_navigation.action_excluded";
	static public String id_load_res_mode				= 	"application.load.resource.mode";
	static public String id_external_loader				= 	"application.external_loader.class";
	static public String id_init_loader					= 	"application.init_loader.class";
	static public String id_pin							=	"application.pin";
	static public String id_db_name 					=	"application.config.db";
	static public String id_extention_do 				=	"application.extention.do";
	static public String id_actioncall_separator 		=	"application.actioncall.separator";

	
	static public String id_transf_elaborationmode		=	"application.transformation.event.after.elaborationmode";
	static public String id_transf_elaborationpoint		=	"application.transformation.event.after.elaborationpoint";
	static public String id_transf_elaborationwrapper	=	"application.transformation.event.after.elaborationwrapper";
	static public String id_annotation_scanner			= 	"application.annotation.scanner";
	static public String id_package_annotated			=	"application.package.annotated";
	static public String id_debug						=	"application.debug";
	
	static public String id_statistic					=	"application.statistic";
	static public String id_statistic_provider			=	"application.statistic.provider";
	static public String id_statistic_stacklength		= 	"application.statistic.stacklength";
	
	private String _path;
	private String _path_root;
	private String _path_config;
	private String _enterpoint;
	private String application_path_config;
	private String _nav_excluded;
	private String _load_res_mode;
	private String _external_loader;
	private String _init_loader;
	private String _pin;
	private String _db_name;
	private String _extention_do;
	private String _actioncall_separator;
	private String _debug;
	private String _statistic;
	private String _statistic_provider;
	private String _annotation_scanner;
	private String _statistic_stacklength;



	private String _transf_elaborationmode		=	"include";
	private String _transf_elaborationpoint		=	"controller";
	private String _transf_elaborationwrapper	=	"it.classhidra.core.controller.wrappers.bsCharResponseWrapper";

	
	
	public HashMap content;
	public List _list_package_annotated;
	
	public static String _rows_on_page="20";
	
	private String loadedFrom="";
	private String loadedFromRP="";
	private String loadedFromSP="";
	private Properties resources_path;
	private Properties synonyms_path;
	
	public app_init(){
		super();
		reimposta();
	}
	
	public void reimposta(){
		_path_config="";
		resources_path=new Properties();
		synonyms_path=new Properties();
		
	}
	public void init() {
		
		if(_list_package_annotated==null) _list_package_annotated=new ArrayList();
		Properties property = null;

		String property_name = System.getProperty(bsConstants.app_id_property);
		try{
			
			if(property_name==null || property_name.equals("")) property_name = "classhidra_app";
			ResourceBundle rb = ResourceBundle.getBundle(property_name);
			Enumeration en = rb.getKeys();
		
			while(en.hasMoreElements()){
				if(property==null) property = new Properties();
				String key = (String)en.nextElement();
				property.setProperty(key,rb.getString(key));
			}
		}catch(Exception e){
			try{
				if(property_name.indexOf(".properties")==-1) property_name+=".properties";		
				property = util_file.loadExternalProperty(property_name);					
			}catch (Exception ex) {
				util_format.writeToConsole(null,"app_init.init():"+e.toString());
				util_format.writeToConsole(null,"app_init.init():"+ex.toString());
			}
			 
		}
		if(property!=null){
			loadedFrom=" "+property_name;
			init(property);
		}else{
			loadedFrom=" System.property";
			_path=(_path==null)?System.getProperty(id_path):_path;
			_path_root=(_path_root==null)?System.getProperty(bsConstants.CONST_ID_PATHROOT):_path_root;
			
			if(_path_config==null && _path!=null)
				_path_config=(_path_config==null)?System.getProperty(_path+"."+bsConstants.CONST_ID_PATHCONFIG):_path_config;
			if(_path_config==null)
				_path_config=(_path_config==null)?System.getProperty(bsConstants.CONST_ID_PATHCONFIG):_path_config;
				
			if(application_path_config==null && _path!=null)
				application_path_config=(application_path_config==null)?System.getProperty(_path+"."+bsConstants.CONST_ID_PATHCONFIG):application_path_config;
			if(application_path_config==null)
				application_path_config=(application_path_config==null)?System.getProperty(bsConstants.CONST_ID_PATHCONFIG):application_path_config;

			_enterpoint=(_enterpoint==null)?System.getProperty(id_enterpoint):_enterpoint;
			_load_res_mode=(_load_res_mode==null)?System.getProperty(id_load_res_mode):_load_res_mode;
			_nav_excluded=(_nav_excluded==null)?System.getProperty(id_nav_excluded):_nav_excluded;
			_external_loader=(_external_loader==null)?System.getProperty(id_external_loader):_external_loader;
			_init_loader=(_init_loader==null)?System.getProperty(id_init_loader):_init_loader;
			_pin=(_pin==null)?System.getProperty(id_pin):_pin;
			_db_name=(_db_name==null)?System.getProperty(id_db_name):_db_name;
			_extention_do=(_extention_do==null)?System.getProperty(id_extention_do):_extention_do;
			_actioncall_separator=(_actioncall_separator==null)?System.getProperty(id_actioncall_separator):_actioncall_separator;

			_debug=(_debug==null)?System.getProperty(id_debug):_debug;
			_statistic=(_statistic==null)?System.getProperty(id_statistic):_statistic;
			_statistic_provider=(_statistic_provider==null)?System.getProperty(id_statistic_provider):_statistic_provider;
			_statistic_stacklength=(_statistic_stacklength==null)?System.getProperty(id_statistic_stacklength):_statistic_stacklength;
			
			
			_annotation_scanner=(_annotation_scanner==null)?System.getProperty(id_annotation_scanner):_annotation_scanner;

			
			
			_transf_elaborationmode=(System.getProperty(id_transf_elaborationmode)!=null)?System.getProperty(id_transf_elaborationmode):_transf_elaborationmode;
			_transf_elaborationpoint=(System.getProperty(id_transf_elaborationpoint)!=null)?System.getProperty(id_transf_elaborationpoint):_transf_elaborationpoint;
			_transf_elaborationwrapper=(System.getProperty(id_transf_elaborationwrapper)!=null)?System.getProperty(id_transf_elaborationwrapper):_transf_elaborationwrapper;
			
			String _package_annotated=System.getProperty(id_package_annotated);
			if(_package_annotated!=null) _list_package_annotated.add(_package_annotated);
			_package_annotated="";
			int ids=0;
			try{
				while(_package_annotated!=null){
					_package_annotated=System.getProperty(id_package_annotated+"."+ids);
					if(_package_annotated!=null) _list_package_annotated.add(_package_annotated);
					ids++;
				}
			}catch(Exception e){				
			}

			
			if(_path_config==null || _path_config.equals("")) _path_config = "/config/";
			if(_enterpoint==null || _enterpoint.equals("")) _enterpoint = "*";
			if(_load_res_mode==null) _load_res_mode="normal";
			if(_extention_do==null) _extention_do=bsConstants.CONST_EXTENTION_DO;
			if(_debug==null) _debug="false";
			if(_statistic==null) _statistic="false";
		}		
		content = new HashMap();
		content.put(CONST_PAGESIZE, (content.get(CONST_PAGESIZE)==null)?
			(System.getProperty(CONST_PAGESIZE)==null)?"10":System.getProperty(CONST_PAGESIZE)
			:(String)content.get(CONST_PAGESIZE));
		
		init_synonyms_path();
		init_resources_path();
	}
	
	public void init(Properties property) {
		if(_list_package_annotated==null) _list_package_annotated=new ArrayList();
		_path=(_path==null)?property.getProperty(id_path):_path;
		_path_root=(_path_root==null)?property.getProperty(bsConstants.CONST_ID_PATHROOT):_path_root;

		_path_config=(_path_config==null)?property.getProperty(bsConstants.CONST_ID_PATHCONFIG):_path_config;
		if(_path_config==null && _path!=null)
			_path_config=(_path_config==null)?System.getProperty(_path+"."+bsConstants.CONST_ID_PATHCONFIG):_path_config;
		if(_path_config==null)
			_path_config=(_path_config==null)?System.getProperty(bsConstants.CONST_ID_PATHCONFIG):_path_config;
			
		application_path_config=(application_path_config==null)?property.getProperty(bsConstants.CONST_ID_PATHCONFIG):application_path_config;
		if(application_path_config==null && _path!=null)
			application_path_config=(application_path_config==null)?System.getProperty(_path+"."+bsConstants.CONST_ID_PATHCONFIG):application_path_config;
		if(application_path_config==null)
			application_path_config=(application_path_config==null)?System.getProperty(bsConstants.CONST_ID_PATHCONFIG):application_path_config;
		
		_enterpoint=(_enterpoint==null)?property.getProperty(id_enterpoint):_enterpoint;
		_load_res_mode=(_load_res_mode==null)?property.getProperty(id_load_res_mode):_load_res_mode;
		_nav_excluded=(_nav_excluded==null)?property.getProperty(id_nav_excluded):_nav_excluded;
		_external_loader=(_external_loader==null)?property.getProperty(id_external_loader):_external_loader;
		_init_loader=(_init_loader==null)?property.getProperty(id_init_loader):_init_loader;
		_pin=(_pin==null)?property.getProperty(id_pin):_pin;
		_db_name=(_db_name==null)?property.getProperty(id_db_name):_db_name;
		_annotation_scanner=(_annotation_scanner==null)?property.getProperty(id_annotation_scanner):_annotation_scanner;
		_extention_do=(_extention_do==null)?property.getProperty(id_extention_do):bsConstants.CONST_EXTENTION_DO;
		_actioncall_separator=(_actioncall_separator==null)?property.getProperty(id_actioncall_separator):_actioncall_separator;
		_debug=(_debug==null)?property.getProperty(id_debug):_debug;
		_statistic=(_statistic==null)?property.getProperty(id_statistic):_statistic;
		_statistic_provider=(_statistic_provider==null)?property.getProperty(id_statistic_provider):_statistic_provider;
		_statistic_stacklength=(_statistic_stacklength==null)?property.getProperty(id_statistic_stacklength):_statistic_stacklength;

		_transf_elaborationmode=(property.getProperty(id_transf_elaborationmode)!=null)?property.getProperty(id_transf_elaborationmode):_transf_elaborationmode;
		_transf_elaborationpoint=(property.getProperty(id_transf_elaborationpoint)!=null)?property.getProperty(id_transf_elaborationpoint):_transf_elaborationpoint;
		_transf_elaborationwrapper=(property.getProperty(id_transf_elaborationwrapper)!=null)?property.getProperty(id_transf_elaborationwrapper):_transf_elaborationwrapper;
		
		String _package_annotated= property.getProperty(id_package_annotated);
		if(_package_annotated!=null) _list_package_annotated.add(_package_annotated);
		_package_annotated="";
		int ids=0;
		try{
			while(_package_annotated!=null){
				_package_annotated=property.getProperty(id_package_annotated+"."+ids);
				if(_package_annotated!=null) _list_package_annotated.add(_package_annotated);
				ids++;
			}
		}catch(Exception e){				
		}

		
		if(_enterpoint==null || _enterpoint.equals("")) _enterpoint = "*";
		if(_load_res_mode==null) _load_res_mode="normal";
		if(_extention_do==null) _extention_do=bsConstants.CONST_EXTENTION_DO;
		if(_debug==null) _debug="false";
		if(_statistic==null) _statistic="false";
		content = new HashMap();
		content.put(CONST_PAGESIZE, (content.get(CONST_PAGESIZE)==null)?property.getProperty(CONST_PAGESIZE):(String)content.get(CONST_PAGESIZE));
	

	}
	
	public void init_resources_path() {
		
		String app_path="";
		try{
			app_path=get_path();
			if(app_path==null || app_path.equals("")) app_path="";
			else app_path+=".";
		}catch(Exception e){
		}

		String property_name = System.getProperty(bsConstants.resources_id_property);
		if(property_name==null || property_name.equals(""))
			property_name = System.getProperty(app_path+bsConstants.resources_id_property);

		if(property_name==null || property_name.equals("")){
			property_name = "classhidra_resources_path";
			try{
				resources_path = util_file.loadProperty(get_path_config()+property_name);
				loadedFromRP=" "+get_path_config()+property_name;
			}catch(Exception e){
				try{
					resources_path = util_file.loadProperty(property_name);
					loadedFromRP=" "+property_name;
				}catch(Exception ex){
//					new bsException(ex,iStub.log_DEBUG);
				}
			}
		}else{
			try{
				resources_path = util_file.loadProperty(property_name);
				loadedFromRP=" "+property_name;
			}catch(Exception e){
//				new bsException(e,iStub.log_DEBUG);
			}
		}



	}
	
	public void init_synonyms_path() {
		
		String app_path="";
		try{
			app_path=get_path();
			if(app_path==null || app_path.equals("")) app_path="";
			else app_path+=".";
		}catch(Exception e){
		}

		String property_name = System.getProperty(bsConstants.synonyms_id_property);
		if(property_name==null || property_name.equals(""))
			property_name = System.getProperty(app_path+bsConstants.synonyms_id_property);

		if(property_name==null || property_name.equals("")){
			property_name = "classhidra_synonyms_path";
			try{
				synonyms_path = util_file.loadProperty(get_path_config()+property_name);
				loadedFromSP=" "+get_path_config()+property_name;
			}catch(Exception e){
				try{
					synonyms_path = util_file.loadProperty(property_name);
					loadedFromSP=" "+property_name;
				}catch(Exception ex){
//					new bsException(ex,iStub.log_DEBUG);
				}
			}
		}else{
			try{
				synonyms_path = util_file.loadProperty(property_name);
				loadedFromSP=" "+property_name;
			}catch(Exception e){
//				new bsException(e,iStub.log_DEBUG);
			}
		}



	}
	
	public String get(String id){
		try{
			return (String)content.get(id);
		}catch(Exception e){
			return "";
		}
	}
	public String get_path_config() {
		return _path_config;
	}

	public void set_path_config(String string) {
		_path_config = string;
	}

	public String toString(){
		String result="";
		result="application.app.property="+_path_config;
		return result;
	}

	public String getApplication_path_config() {
		return application_path_config;
	}

	public String get_enterpoint() {
		return _enterpoint;
	}

	public String get_nav_excluded() {
		return _nav_excluded;
	}

	public String get_load_res_mode() {
		return _load_res_mode;
	}

	public void set_load_res_mode(String _load_res_mode) {
		this._load_res_mode = _load_res_mode;
	}

	public String get_path() {
		return _path;
	}

	public void set_path(String path) {
		_path = path;
	}

	public String getLoadedFrom() {
		return loadedFrom;
	}

	public String get_external_loader() {
		return _external_loader;
	}

	public String get_transf_elaborationmode() {
		return _transf_elaborationmode;
	}

	public String get_transf_elaborationpoint() {
		return _transf_elaborationpoint;
	}

	public String get_transf_elaborationwrapper() {
		return _transf_elaborationwrapper;
	}

	public String get_pin() {
		return _pin;
	}

	public void set_pin(String pin) {
		_pin = pin;
	}

	public String getLoadedFromRP() {
		return loadedFromRP;
	}

	public Properties getResources_path() {
		if(resources_path==null) return new Properties();
		return resources_path;
	}

	public String get_db_name() {
		return _db_name;
	}

	public void set_db_name(String dbName) {
		_db_name = dbName;
	}

	public Properties getSynonyms_path() {
		return synonyms_path;
	}

	public String getLoadedFromSP() {
		return loadedFromSP;
	}

	public String get_extention_do() {
//		if(_extention_do==null) return bsController.getAppInit().get_extention_do();
		return _extention_do;
	}

	public void set_extention_do(String extentionDo) {
		_extention_do = extentionDo;
	}



	public List get_list_package_annotated() {
		return _list_package_annotated;
	}

	public String get_debug() {
		return _debug;
	}
	
	public String get_annotation_scanner() {
		return _annotation_scanner;
	}

	public String get_init_loader() {
		return _init_loader;
	}

	public String get_statistic() {
		return _statistic;
	}

	public String get_statistic_provider() {
		return _statistic_provider;
	}

	public String get_statistic_stacklength() {
		return _statistic_stacklength;
	}

	public String get_actioncall_separator() {
		return _actioncall_separator;
	}

}
