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

package it.classhidra.core.tool.log;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.init.log_init;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_provider;

public class log_generator implements i_log_generator {
	private log_init init;
	private iStub logStub;
	private boolean readError=false;
	
public log_generator(log_init _init) {
	super();
	this.init = _init;
	logStub = stubFactory(init.get_LogStub());
	try{
		if(init==null) throw new Exception("Log INIT is NULL");
//		if(init.get_LogPath()==null) throw new Exception("Log INIT->LogPath is NULL");
		if(init.get_LogPath()==null)
			init.set_LogPath("");
		else if(init.get_LogPath()!=null && !init.get_LogPath().equals("")) {
			if(!new File(new log_FileManager(init).getRealPathName()).exists() && logStub==null)
				readError = true;
		}
	}catch(Exception e){	
		readError = true;
		util_format.writeToConsole(_init,"Load_log ERROR: "+e.toString());
		return;
	}

}

public void setInit(log_init _init) {
	this.init = _init;
	logStub = stubFactory(init.get_LogStub());
	try{
		readError = true;
	}catch(Exception e){	
		readError = true;
//	   	bsController.writeLog("Load_log ERROR:"+e.toString(),iStub.log_ERROR);
		util_format.writeToConsole(_init,"Load_log ERROR: "+e.toString());
		return;
	}
}

public static i_log_pattern_web patternFactory(String className){
	i_log_pattern_web element = null;
	if(className==null || className.equals("")) return new log_patternSimple();
	if(element==null){
		try{	
			element = (i_log_pattern_web)util_provider.getInstanceFromProvider(
							new String[]{
									bsController.getAppInit().get_context_provider(),
									bsController.getAppInit().get_cdi_provider(),
									bsController.getAppInit().get_ejb_provider()
							},
							className);
		}catch (Exception e) {
			element = new log_patternSimple();
		}
	}
	return element;
}

public static iStub stubFactory(String className){
	iStub element = null;
	if(className==null || className.equals("")) return null;
	if(element==null){
		try{	
			element = (iStub)util_provider.getInstanceFromProvider(
							new String[]{
									bsController.getAppInit().get_context_provider(),
									bsController.getAppInit().get_cdi_provider(),
									bsController.getAppInit().get_ejb_provider()
							},
							className);
		}catch (Exception e) {
			element = null;
		}
	}
	return element;
}

public static iStub stubFactory(log_init _init){
	if(_init==null) return null;
	String className = _init.get_LogStub();
	if(className==null || className.equals("")) return null;
	iStub element = null;
	
	if(element==null){
		try{	
			element = (iStub)util_provider.getInstanceFromProvider(
							new String[]{
									bsController.getAppInit().get_context_provider(),
									bsController.getAppInit().get_cdi_provider(),
									bsController.getAppInit().get_ejb_provider()
							},
							className);
		}catch (Exception e) {
			element = null;
		}
	}
	return element;
}

public static HashMap<String,Object> prepare4stub(
		String class_info,
		String mess,
		Exception exception,
		Throwable throwable,
		HttpServletRequest request,
		ServletContext context,
		String level,
		String user){
	
	HashMap<String,Object> result = new HashMap<String, Object>();
		result.put(iStub.log_class_info,class_info);
		result.put(iStub.log_stub_mess,mess);
		result.put(iStub.log_stub_exception,exception);
		result.put(iStub.log_stub_throwable,throwable);
		result.put(iStub.log_stub_request,request);
		result.put(iStub.log_stub_servletcontext,context);
		result.put(iStub.log_stub_level,level);
		result.put(iStub.log_stub_user,user);
	
	return result;
}

public synchronized i_log_pattern get_log_Pattern(){ 
	return patternFactory( init.get_LogPattern());
}	

public synchronized iStub get_log_Stub(){ 
	return stubFactory( init.get_LogStub());
}	


public synchronized void writeLog(HttpServletRequest request, String msg, String level) throws IOException {
	i_log_pattern_web logPattern = patternFactory( init.get_LogPattern());
	if(logPattern==null) logPattern = new log_patternSimple();
	String log_mess = logPattern.prepare(request,msg,level);	
	
	if(logStub==null) logStub = stubFactory(init);
	if(logStub!=null){
		logStub.write(prepare4stub(null, log_mess, null, null, request,null, level,null));
	}else{	
		if(init==null || init.isStackLevel(level)){
			util_format.writeToConsole(init,log_mess);
			if(init.get_LogPath()!=null && !init.get_LogPath().equals("")) {
				log_FileManager fm = new log_FileManager(init);
					fm.createFile(true,msg);
					fm.writeLineRecord(log_mess);
					fm.closeFile();
			}
		}
	}	
}

public synchronized void writeLog(String msg,String level) throws IOException {
		i_log_pattern logPattern = patternFactory( init.get_LogPattern());
		if(logPattern==null) logPattern = new log_patternSimple();
		String log_mess = logPattern.prepare(msg,level);		
		
		if(logStub==null) logStub = stubFactory(init);
		if(logStub!=null){
			logStub.write(prepare4stub(null, log_mess, null, null, null,null, level,null));
		}else{
			if(init==null || init.isStackLevel(level)){
				util_format.writeToConsole(init,log_mess);
				if(init.get_LogPath()!=null && !init.get_LogPath().equals("")) {
					log_FileManager fm = new log_FileManager(init);
						fm.createFile(true,msg);
						fm.writeLineRecord(log_mess);
					fm.closeFile();
				}
			}
		}
}

public static synchronized void writeLog(log_init _init, String msg, String level) throws IOException {
	i_log_pattern logPattern = patternFactory( _init.get_LogPattern());
	if(logPattern==null) logPattern = new log_patternSimple();
	String log_mess = logPattern.prepare(msg,level);	
	
	iStub _logStub = stubFactory(_init);
	if(_logStub!=null){
		_logStub.write(prepare4stub(null, log_mess, null, null, null,null, level,null));
	}else{	
		if(_init==null || _init.isStackLevel(level)){
			util_format.writeToConsole(_init,log_mess);
			if(_init.get_LogPath()!=null && !_init.get_LogPath().equals("")) {
				log_FileManager fm = new log_FileManager(_init);
					fm.createFile(true,log_mess);
					fm.writeLineRecord(log_mess);
				fm.closeFile();
			}
		}
	}
}

public synchronized void writeLog(Object obj_request, String msg,String userIP, String userMatricola,String classFrom, String level) throws IOException {
	if(obj_request==null || !(obj_request instanceof HttpServletRequest)) return;
	HttpServletRequest request = (HttpServletRequest)obj_request;
	i_log_pattern_web logPattern = patternFactory( init.get_LogPattern());
	if(logPattern==null) logPattern = new log_patternSimple();
	String log_mess = logPattern.prepare(request,msg,userIP,userMatricola,classFrom,level);	
	
	if(logStub==null) logStub = stubFactory(init);
	if(logStub!=null){
		logStub.write(prepare4stub(classFrom, log_mess, null, null, request,null, level,userMatricola));
	}else{
		if(init==null || init.isStackLevel(level)){
			util_format.writeToConsole(init,log_mess);
			if(init.get_LogPath()!=null && !init.get_LogPath().equals("")) {
				log_FileManager fm = new log_FileManager(init);
					fm.createFile(true,msg);
					fm.writeLineRecord(log_mess);
				fm.closeFile();
			}
		}
	}
}

public synchronized void writeLog(String msg,String userIP, String userMatricola,String classFrom,String level) throws IOException {
		i_log_pattern logPattern = patternFactory( init.get_LogPattern());
		if(logPattern==null) logPattern = new log_patternSimple();
		String log_mess = logPattern.prepare(msg,userIP,userMatricola,classFrom,level);		

		if(logStub==null) logStub = stubFactory(init);
		if(logStub!=null){
			logStub.write(prepare4stub(classFrom, log_mess, null, null, null,null, level,userMatricola));
		}else{	
			if(init==null || init.isStackLevel(level)){
				util_format.writeToConsole(init,log_mess);
				if(init.get_LogPath()!=null && !init.get_LogPath().equals("")) {
					log_FileManager fm = new log_FileManager(init);
						fm.createFile(true,msg);
						fm.writeLineRecord(log_mess);
					fm.closeFile();
				}
			}
		}
}
public synchronized void writeLog(String msg,String userIP, String userMatricola,String classFrom,String level, ServletContext context) throws IOException {
	i_log_pattern logPattern = patternFactory( init.get_LogPattern());
	if(logPattern==null) logPattern = new log_patternSimple();
	String log_mess = logPattern.prepare(msg,userIP,userMatricola,classFrom,level);	

	if(logStub==null) logStub = stubFactory(init);
	if(logStub!=null){
		logStub.write(prepare4stub(classFrom, log_mess, null, null, null,context, level,userMatricola));
	}else{	
		if(init==null || init.isStackLevel(level)){
			util_format.writeToConsole(init,log_mess);
			if(init.get_LogPath()!=null && !init.get_LogPath().equals("")) {
				log_FileManager fm = new log_FileManager(init);
					fm.createFile(true,msg);
					fm.writeLineRecord(log_mess);
				fm.closeFile();
			}
		}
	}
}

public static synchronized void writeLog(log_init _init, String msg,String userIP, String userMatricola,String classFrom, String level) throws IOException {
	i_log_pattern logPattern = patternFactory( _init.get_LogPattern());
	if(logPattern==null) logPattern = new log_patternSimple();
	String log_mess = logPattern.prepare(msg,userIP,userMatricola,classFrom,level);	
	
	iStub _logStub = stubFactory(_init);
	if(_logStub!=null){
		_logStub.write(prepare4stub(classFrom, log_mess, null, null, null,null, level,userMatricola));
	}else{	
		if(_init==null || _init.isStackLevel(level)){
			util_format.writeToConsole(_init,log_mess);
			if(_init.get_LogPath()!=null && !_init.get_LogPath().equals("")) {
				log_FileManager fm = new log_FileManager(_init);
					fm.createFile(true,logPattern.prepare(msg,userIP,userMatricola,classFrom,level));
					fm.writeLineRecord(log_mess);
				fm.closeFile();
			}
		}
	}
}

public String get_log_Filename() throws IOException {
	log_FileManager fm = new log_FileManager(init);
	fm.createFile(true,"");
	fm.closeFile();
	return fm.getRealPathName();
}

public String get_log_Content(String lineSep) throws IOException {
	if(lineSep==null) lineSep = System.getProperty("line.separator");
	log_FileManager fm = new log_FileManager(init);
	fm.createFile(true,"");
	fm.readFile();
	BufferedReader br = fm.getBufferedReader();
	String content="";
	String thisLine;
	while ((thisLine = br.readLine()) != null) { 
		content+=thisLine+lineSep;
	} 	
	br.close();
	fm.closeFile();
	return content;
}
	public boolean isReadError() {
		return readError;
	}

	public void setReadError(boolean b) {
		readError = b;
	}

}


