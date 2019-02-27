/**
* Creation date: (15/01/2016)
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

package wrappers.spring;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.init.log_init;
import it.classhidra.core.tool.log.i_log_generator;
import it.classhidra.core.tool.log.i_log_pattern;
import it.classhidra.core.tool.log.i_log_pattern_web;
import it.classhidra.core.tool.log.log_patternSimple;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_provider;



@Component("$defaultLogGenerator")
@Scope(value="singleton")
public class LogGenerator_SpringOnlySession implements i_log_generator {
	private log_init init;
	private iStub logStub;
	private List<String> instance = new ArrayList<String>();
	private i_log_pattern_web pattern;
	
	private boolean readError=false;
	


	
public LogGenerator_SpringOnlySession(){
	super();
	try{
		writeLog("Instanced Spring LogGenerator (only in session) -> "+this.getClass().getName(),iStub.log_INFO);
	}catch(Exception e){
		System.out.println("Instanced Spring LogGenerator (only in session) -> "+this.getClass().getName());
	}}
	


public LogGenerator_SpringOnlySession(log_init _init) {
	super();
	this.init = _init;
	logStub = stubFactory(init.get_LogStub());
	try{
		if(init==null) throw new Exception("Log INIT is NULL");
		if(init.get_LogPath()==null) throw new Exception("Log INIT->LogPath is NULL");

	}catch(Exception e){	
		readError = true;
//	   	bsController.writeLog("Load_log ERROR:"+e.toString(),iStub.log_ERROR);
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

private  i_log_pattern_web patternFactory(String className){

	if(className==null || className.equals("")) return new log_patternSimple();
	if(pattern==null){
		try{	
			pattern = (i_log_pattern_web)util_provider.getInstanceFromProvider(
						new String[]{
								bsController.getAppInit().get_context_provider(),
								bsController.getAppInit().get_cdi_provider(),
								bsController.getAppInit().get_ejb_provider()
						},
						className);
		}catch (Exception e) {
			pattern = new log_patternSimple();
		}
	}
	return pattern;
}

private  iStub stubFactory(String className){
	
	if(className==null || className.equals("")) return null;
	if(logStub==null){
		try{	
			logStub = (iStub)util_provider.getInstanceFromProvider(
							new String[]{
									bsController.getAppInit().get_context_provider(),
									bsController.getAppInit().get_cdi_provider(),
									bsController.getAppInit().get_ejb_provider()
							},
							className);
		}catch (Exception e) {
			logStub = null;
		}
	}
	return logStub;
}

private  iStub stubFactory(log_init _init){
	
	String className = _init.get_LogStub();
	if(className==null || className.equals("")) return null;

	
	if(logStub==null){
		try{	
			logStub = (iStub)util_provider.getInstanceFromProvider(
							new String[]{
									bsController.getAppInit().get_context_provider(),
									bsController.getAppInit().get_cdi_provider(),
									bsController.getAppInit().get_ejb_provider()
							},
							className);
		}catch (Exception e) {
			logStub = null;
		}
	}
	return logStub;
}

public  HashMap<String,Object> prepare4stub(
		String mess,
		Exception exception,
		Throwable throwable,
		HttpServletRequest request,
		ServletContext context,
		String level,
		String user){
	
	HashMap<String,Object> result = new HashMap<String, Object>();
		result.put(iStub.log_stub_mess,mess);
		result.put(iStub.log_stub_exception,exception);
		result.put(iStub.log_stub_throwable,throwable);
		result.put(iStub.log_stub_request,request);
		result.put(iStub.log_stub_servletcontext,context);
		result.put(iStub.log_stub_level,level);
		result.put(iStub.log_stub_user,user);
	
	return result;
}

public i_log_pattern get_log_Pattern(){ 
	return patternFactory( init.get_LogPattern());
}	

public iStub get_log_Stub(){ 
	return stubFactory( init.get_LogStub());
}	



public void writeLog(String msg,String level) throws IOException {
		i_log_pattern logPattern = patternFactory( init.get_LogPattern());
		if(logPattern==null) logPattern = new log_patternSimple();
		String log_mess = logPattern.prepare(msg,level);

		util_format.writeToConsole(init,log_mess);
		
		if(logStub==null) logStub = stubFactory(init);
		if(logStub!=null){
			logStub.write(prepare4stub(log_mess, null, null, null,null, level,null));
		}else{
			instance.add(log_mess);
		}
}


public void writeLog(Object obj_request, String msg,String userIP, String userMatricola,String classFrom, String level) throws IOException {
	if(obj_request==null || !(obj_request instanceof HttpServletRequest)) return;
	HttpServletRequest request = (HttpServletRequest)obj_request;
	i_log_pattern_web logPattern = patternFactory( init.get_LogPattern());
	if(logPattern==null) logPattern = new log_patternSimple();
	String log_mess = logPattern.prepare(request,msg,userIP,userMatricola,classFrom,level);

	util_format.writeToConsole(init,log_mess);
	
	if(logStub==null) logStub = stubFactory(init);
	if(logStub!=null){
		logStub.write(prepare4stub(log_mess, null, null, request,null, level,userMatricola));
	}else{
		if(init.isStackLevel(level)){
			instance.add(log_mess);
		}
	}
}

public void writeLog(String msg,String userIP, String userMatricola,String classFrom,String level) throws IOException {
		i_log_pattern logPattern = patternFactory( init.get_LogPattern());
		if(logPattern==null) logPattern = new log_patternSimple();
		String log_mess = logPattern.prepare(msg,userIP,userMatricola,classFrom,level);

		util_format.writeToConsole(init,log_mess);

		if(logStub==null) logStub = stubFactory(init);
		if(logStub!=null){
			logStub.write(prepare4stub(log_mess, null, null, null,null, level,userMatricola));
		}else{	
			if(init.isStackLevel(level)){
				instance.add(log_mess);
			}
		}
}



public String get_log_Filename() throws IOException {
	if(instance!=null)
		return instance.getClass().getName();
	return "undefined";
}

public String get_log_Content(String lineSep) throws IOException {

	String content="";
	for(int i=0;i<instance.size();i++)
		content+=(String)instance.get(i)+System.getProperty("line.separator");
	return content;
}
	public boolean isReadError() {
		return readError;
	}

	public void setReadError(boolean b) {
		readError = b;
	}

}


