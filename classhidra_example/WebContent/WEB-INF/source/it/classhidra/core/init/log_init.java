/**
* Creation date: (14/12/2005)
* @author: Svyatoslav Urbanovych surban@bigmir.net  svyatoslav.urbanovych@gmail.com
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
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_blob;
import it.classhidra.core.tool.util.util_file;
import it.classhidra.core.tool.util.util_format;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;



public class log_init implements Serializable{
	private static final long serialVersionUID = -6472537615470267850L;

	public final static String id_property		= 	"classhidra_log";
	public final static String id_LogStub 		=	"application.log.stub";
	public final static String id_LogLevel 		=	"application.log.level";

	public final static String id_LogGenerator 	=	"application.log.generator";
	public final static String id_LogPattern 	= 	"application.log.pattern";
	public final static String id_LogPath 		= 	"application.log.path";
	public final static String id_LogName 		= 	"application.log.name";
	public final static String id_LogMaskName 	=	"application.log.maskname";
	public final static String id_LogMaskFormat =	"application.log.maskformat";
	public final static String id_LogMaxLength 	=	"application.log.maxlength";
	public final static String id_LogMaxFiles 	=	"application.log.maxfiles";
	public final static String id_LogFlashRate 	=	"application.log.flashrate";
	public final static String id_LogFlashSize 	=	"application.log.flashsize";
	public final static String id_Write2Concole =	"application.log.write2console";


	private String _LogGenerator;
	private String _LogPattern;
	private String _LogPath;
	private String _LogName;
	private String _LogMaskName;
	private String _LogMaskFormat;
	private String _LogMaxLength;
	private String _LogMaxFiles;
	private String _LogFlashRate;
	private String _LogFlashSize;
	private String _LogStub;
	private String _LogLevel;
	private String _Write2Concole;

	private String loadedFrom="";
	
	private boolean changed=false;
	
	public static Map<String,Integer> logLevels = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
	{
		put(iStub.log_ALL,0);
		put(iStub.log_TRACE,1);
		put(iStub.log_DEBUG,2);
		put(iStub.log_INFO,3);
		put(iStub.log_WARN,4);
		put(iStub.log_ERROR,5);
		put(iStub.log_FATAL,6);
		put(iStub.log_OFF,-1);
	}};



public  log_init() {
	super();
	reimposta();
}

public void reimposta(){
	_LogGenerator=			"";
	_LogPattern=			"it.classhidra.core.tool.log.log_patternSimple";
//	_LogPath=				"";
	_LogName=				"";
	_LogMaskName=			"application_";
	_LogMaskFormat=			"yyyyMMdd_hhmmssss";
	_LogMaxLength=			"1000000";
	_LogMaxFiles=			"10";
	_LogFlashRate=			"0";
	_LogFlashSize=			"1024";
	_LogLevel=				"DEBUG";
	_Write2Concole=			"false";
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

	String property_name = System.getProperty(bsConstants.log_id_property);
	if(property_name==null || property_name.equals(""))
		property_name = System.getProperty(app_path+bsConstants.log_id_property);

	if(property_name==null || property_name.equals(""))
		property_name = ainit.getResources_path().getProperty(bsController.log_id_property);

	if(property_name==null || property_name.equals(""))
		property_name = ainit.getResources_path().getProperty(app_path+bsController.log_id_property);


	
	if(property==null){
		if(property_name==null || property_name.equals("")){
			try{
				if(ainit.get_db_name()!=null && ainit.isDb_name_valid()){
					property = initDB(ainit);
					if(property!=null){
						loadedFrom=ainit.get_db_name();
						init(property);
						return;
					}
				}
			}catch(Exception e){
			}

			property_name = id_property;
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
		}else{
			try{
				property = util_file.loadProperty(property_name);
				loadedFrom+=" "+property_name;
			}catch(Exception e){
			}
		}
	}

	if(property!=null){
		loadedFrom+=" "+property_name;
		init(property);
	}else{
		loadedFrom=" System.property";
		_LogStub = (System.getProperty(id_LogStub)==null)?_LogStub:System.getProperty(id_LogStub);
		_LogLevel = (System.getProperty(id_LogLevel)==null)?_LogLevel:System.getProperty(id_LogLevel);
		_LogPath = (System.getProperty(id_LogPath)==null)?_LogPath:System.getProperty(id_LogPath);
		_LogMaskName = (System.getProperty(id_LogMaskName)==null)?_LogMaskName:System.getProperty(id_LogMaskName);
		_LogMaskFormat = (System.getProperty(id_LogMaskFormat)==null)?_LogMaskFormat:System.getProperty(id_LogMaskFormat);
		_LogName = (System.getProperty(id_LogName)==null)?_LogName:System.getProperty(id_LogName);
		_LogMaxLength = (System.getProperty(id_LogMaxLength)==null)?_LogMaxLength:System.getProperty(id_LogMaxLength);
		_LogFlashSize = (System.getProperty(id_LogFlashSize)==null)?_LogFlashSize:System.getProperty(id_LogFlashSize);
		_LogFlashRate = (System.getProperty(id_LogFlashRate)==null)?_LogFlashRate:System.getProperty(id_LogFlashRate);
		_LogPattern = (System.getProperty(id_LogPattern)==null)?_LogPattern:System.getProperty(id_LogPattern);
		_LogGenerator = (System.getProperty(id_LogGenerator)==null)?_LogGenerator:System.getProperty(id_LogGenerator);

		_Write2Concole = (System.getProperty(id_Write2Concole)==null)?_Write2Concole:System.getProperty(id_Write2Concole);
		if(_LogPath==null || _LogPath.equals("")) return;
		if(_LogName==null || _LogName.equals("")){
			if(_LogMaskFormat==null || _LogMaskFormat.equals("")) _LogMaskFormat = "yyyyMMdd_hhmmssss";
			if(_LogMaskName==null || _LogMaskName.equals("")) _LogMaskName = "application_";
			_LogName = _LogMaskName + util_format.dataToString(Calendar.getInstance().getTime(),_LogMaskFormat);
		}
	}
//	if(initExc!=null) 		new bsControllerException(initExc,iStub.log_DEBUG);
}

public void init(Properties ex_property) {
	_LogStub = (ex_property.getProperty(id_LogStub)==null)?_LogStub:ex_property.getProperty(id_LogStub);
	_LogLevel = (ex_property.getProperty(id_LogLevel)==null)?_LogLevel:ex_property.getProperty(id_LogLevel);
	_LogPath = (ex_property.getProperty(id_LogPath)==null)?_LogPath:ex_property.getProperty(id_LogPath);
	_LogMaskName = (ex_property.getProperty(id_LogMaskName)==null)?_LogMaskName:ex_property.getProperty(id_LogMaskName);
	_LogMaskFormat = (ex_property.getProperty(id_LogMaskFormat)==null)?_LogMaskFormat:ex_property.getProperty(id_LogMaskFormat);
	_LogName = (ex_property.getProperty(id_LogName)==null)?_LogName:ex_property.getProperty(id_LogName);
	_LogMaxLength = (ex_property.getProperty(id_LogMaxLength)==null)?_LogMaxLength:ex_property.getProperty(id_LogMaxLength);
	_LogFlashSize = (ex_property.getProperty(id_LogFlashSize)==null)?_LogFlashSize:ex_property.getProperty(id_LogFlashSize);
	_LogFlashRate = (ex_property.getProperty(id_LogFlashRate)==null)?_LogFlashRate:ex_property.getProperty(id_LogFlashRate);
	_LogPattern = (ex_property.getProperty(id_LogPattern)==null)?_LogPattern:ex_property.getProperty(id_LogPattern);
	_LogGenerator = (ex_property.getProperty(id_LogGenerator)==null)?_LogGenerator:ex_property.getProperty(id_LogGenerator);
	_Write2Concole = (ex_property.getProperty(id_Write2Concole)==null)?_Write2Concole:ex_property.getProperty(id_Write2Concole);

	if(_LogPath==null || _LogPath.equals("")) return;
	if(_LogName==null || _LogName.equals("")){
		if(_LogMaskFormat==null || _LogMaskFormat.equals("")) _LogMaskFormat = "yyyyMMdd_hhmmssss";
		if(_LogMaskName==null) _LogMaskName = "application";
		_LogName = _LogMaskName + util_format.dataToString(Calendar.getInstance().getTime(),_LogMaskFormat);
	}
	setChanged(true);
}


public Properties initDB(app_init ainit) throws bsControllerException, Exception{
	
	Properties property = new Properties();
	String app_path=ainit.get_path();
	if(app_path==null || app_path.equals("")) app_path="";
	else app_path+=".";
	
	String propertyData = null;
	boolean dbValid = false;

	
	try{
		propertyData = util_blob.load_from_config_clear(
				(ainit.getSynonyms_path().getProperty(app_path+bsController.log_id_property)==null)?app_path+bsController.log_id_property:ainit.getSynonyms_path().getProperty(app_path+bsController.log_id_property),
				ainit.get_db_name());
		dbValid=true;
	}catch(Exception e){

//		new bsException(e);
	}
	try{
		if(propertyData==null) propertyData = util_blob.load_from_config_clear(
				(ainit.getSynonyms_path().getProperty(bsController.log_id_property)==null)?bsController.log_id_property:ainit.getSynonyms_path().getProperty(bsController.log_id_property),
				ainit.get_db_name());
		dbValid=true;
	}catch(Exception e){

//		new bsException(e);
	}
	if(!dbValid)
		ainit.setDb_name_valid(false);
	
	if(propertyData==null) return null;
	
	try{
		ByteArrayInputStream instrm = new ByteArrayInputStream(propertyData.getBytes());
		if(instrm!=null) 
			property.load(instrm);
//		else property=null;		
	}catch(Exception e){
		property=null;
		throw e;
	}
	setChanged(true);
	return property;
	



}

public int get_LogFlashRate() {
	try{
		return Integer.valueOf(_LogFlashRate).intValue();
	}catch(Exception e){
		return 0;
	}
}
public int get_LogFlashSize() {
	try{
		return Integer.valueOf(_LogFlashSize).intValue();
	}catch(Exception e){
		return 1024;
	}
}

public String get_LogMaskFormat() {
	return _LogMaskFormat;
}
public String get_LogMaskName() {
	return _LogMaskName;
}
public long get_LogMaxLength() {
	try{
		return Long.valueOf(_LogMaxLength).longValue();
	}catch(Exception e){
		return 1000000;
	}
}
public String get_LogName() {
	return _LogName;
}
public String get_LogPath() {
	return _LogPath;
}
public String get_LogPattern() {
	return _LogPattern;
}
public void set_LogFlashRate(String string) {
	_LogFlashRate = string;
	setChanged(true);
}
public void set_LogFlashSize(String string) {
	_LogFlashSize = string;
	setChanged(true);
}
public void set_LogMaskFormat(String string) {
	_LogMaskFormat = string;
	setChanged(true);
}
public void set_LogMaskName(String string) {
	_LogMaskName = string;
	setChanged(true);
}
public void set_LogMaxLength(String string) {
	_LogMaxLength = string;
	setChanged(true);
}
public void set_LogName(String string) {
	_LogName = string;
	setChanged(true);
}
public void set_LogPath(String string) {
	_LogPath = string;
	setChanged(true);
}
public void set_LogPattern(String string) {
	_LogPattern = string;
	setChanged(true);
}
public int get_LogMaxFiles() {
	try{
		return Integer.valueOf(_LogMaxFiles).intValue();
	}catch(Exception e){
		return 1;
	}

}
public void set_LogMaxFiles(String string) {
	_LogMaxFiles = string;
	setChanged(true);
}

	public static String getId_LogFlashRate() {
		return id_LogFlashRate;
	}

	public static String getId_LogFlashSize() {
		return id_LogFlashSize;
	}

	public static String getId_LogLevel() {
		return id_LogLevel;
	}

	public static String getId_LogMaskFormat() {
		return id_LogMaskFormat;
	}

	public static String getId_LogMaskName() {
		return id_LogMaskName;
	}

	public static String getId_LogMaxFiles() {
		return id_LogMaxFiles;
	}

	public static String getId_LogMaxLength() {
		return id_LogMaxLength;
	}

	public static String getId_LogName() {
		return id_LogName;
	}

	public static String getId_LogPath() {
		return id_LogPath;
	}

	public static String getId_LogPattern() {
		return id_LogPattern;
	}

	public static String getId_LogStub() {
		return id_LogStub;
	}

	public String get_LogLevel() {
		return _LogLevel;
	}

	public String get_LogStub() {
		return _LogStub;
	}


	public void set_LogLevel(String string) {
		_LogLevel = string;
//		setChanged(true);
	}

	public void set_LogStub(String string) {
		_LogStub = string;
		setChanged(true);
	}
	public String toString(){
		String result="";
		result+="application.log.stub="+_LogStub+System.getProperty("line.separator");
		result+="application.log.level="+_LogLevel+System.getProperty("line.separator");
		result+="application.log.generator="+_LogGenerator+System.getProperty("line.separator");
		result+="application.log.pattern="+_LogPattern+System.getProperty("line.separator");
		result+="application.log.path="+_LogPath+System.getProperty("line.separator");
		result+="application.log.name="+_LogName+System.getProperty("line.separator");
		result+="application.log.maskname="+_LogMaskName+System.getProperty("line.separator");
		result+="application.log.maskformat="+_LogMaskFormat+System.getProperty("line.separator");
		result+="application.log.maxlength="+_LogMaxLength+System.getProperty("line.separator");
		result+="application.log.maxfiles="+_LogMaxFiles+System.getProperty("line.separator");
		result+="application.log.flashrate="+_LogFlashRate+System.getProperty("line.separator");
		result+="application.log.flashsize="+_LogFlashSize;
		return result;
	}

	public String get_Write2Concole() {
		return _Write2Concole;
	}

	public void set_Write2Concole(String write2Concole) {
		_Write2Concole = write2Concole;
//		setChanged(true);
	}

	public String get_LogGenerator() {
		return _LogGenerator;
	}

	public void set_LogGenerator(String logGenerator) {
		_LogGenerator = logGenerator;
		setChanged(true);
	}

	public String getLoadedFrom() {
		return loadedFrom;
	}

	public boolean isStackLevel(String level){
		if(_LogLevel==null || _LogLevel.equals("")) return true;
		int int_logLevel = -1;
		int int_inputLogLevel = -1;
		try {
			int_logLevel = ((Integer)logLevels.get(_LogLevel)).intValue();
		}catch(Exception e) {			
		}
		try {
			int_inputLogLevel = ((Integer)logLevels.get(level)).intValue();
		}catch(Exception e) {			
		}
		if(int_inputLogLevel>=int_logLevel)
			return true;
		else
			return false;
/*		
		if(_LogLevel.equals(iStub.log_DEBUG)) return true;
		if(_LogLevel.equals(iStub.log_INFO) && !level.equals(iStub.log_DEBUG)) return true;
		if(_LogLevel.equals(iStub.log_WARN) && !level.equals(iStub.log_DEBUG) && !level.equals(iStub.log_INFO)) return true;
		if(_LogLevel.equals(iStub.log_ERROR) && !level.equals(iStub.log_DEBUG) && !level.equals(iStub.log_INFO) && !level.equals(iStub.log_WARN)) return true;
		if(_LogLevel.equals(iStub.log_FATAL) && !level.equals(iStub.log_DEBUG) && !level.equals(iStub.log_INFO) && !level.equals(iStub.log_WARN)&& !level.equals(iStub.log_ERROR)) return true;

		return false;
*/		
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	


}
