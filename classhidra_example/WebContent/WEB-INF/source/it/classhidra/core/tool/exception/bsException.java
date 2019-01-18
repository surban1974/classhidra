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

package it.classhidra.core.tool.exception;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.init.log_init;
import it.classhidra.core.tool.log.i_log_generator;
import it.classhidra.core.tool.log.log_generator;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_reflect;

public class bsException extends Exception {
	private static final long serialVersionUID = 5306016507625978656L;
	private log_init logInit;
	private i_log_generator logG;

public bsException(String mess) {
		super(mess);	
}		
public bsException(Exception e) {
		super(e.toString());	
		try{
			String _log="";
			if(e.getMessage()!=null) _log+=getMessage()+" ";
			if(_log.equals("")) _log+=e.toString();
			writeLog(_log,iStub.log_ERROR);
		}catch(Exception ex){
		}
}	
public bsException(Throwable t) {
		super(t.toString());	
		try{
			String _log="";
			if(t.getMessage()!=null) _log+=getMessage()+" ";
			if(_log.equals("")) _log+=t.toString();
			writeLog(_log,iStub.log_ERROR);

		}catch(Exception ex){
		}
}	
	
public bsException(Exception e, String level) {
	super(e.toString());	
	try{
		String _log="";
		if(e.getMessage()!=null) _log+=getMessage()+" ";
		if(_log.equals("")) _log+=e.toString();
		writeLog(_log,level);
	}catch(Exception ex){
	}
}
public bsException(Throwable t, String level) {
	super(t.toString());	
	try{
		String _log="";
		if(t.getMessage()!=null) _log+=getMessage()+" ";
		if(_log.equals("")) _log+=t.toString();
		writeLog(_log,level);

	}catch(Exception ex){
	}
}
public bsException(String mess, String level) {
	super(mess);	
	try{
		writeLog(mess,level);
	}catch(Exception e){
	}
}

protected void writeLog(String msg, String level){
	try{
		String classInfo=util_reflect.prepareClassInfo(new String[]{"bsException.java","bsException.java"},new String[]{"writeLog","<init>"});

		if(logG==null) service_mountLog();
		logG.writeLog(msg,"","",classInfo,level);
	}catch(Exception e){}
}

private void service_mountLog(){
	logG = bsController.getLogG();
	if(logG==null) {
		logInit = new log_init();
			logInit.init();
		if(logInit.get_LogGenerator()==null || logInit.get_LogGenerator().equals("")){
			if(	logInit.get_LogPath()==null || logInit.get_LogPath().equals("") && 
				logInit.get_LogStub()==null || logInit.get_LogStub().equals("")	){
				try{
					Object transf = Class.forName("it.classhidra.core.tool.exception.bsIntegrator").newInstance();
					logInit = (log_init)util_reflect.getValue(transf, "getLogInit", null);
				}catch(Exception e){	
					e.toString();
				}catch(Throwable t){
					t.toString();
				}
			}
		}
		if(logInit.get_LogGenerator()!=null && !logInit.get_LogGenerator().equals("")){
			try{
				logG = (i_log_generator)Class.forName(logInit.get_LogGenerator()).newInstance();
				logG.setInit(logInit);
				return;
			}catch(Exception e){	
				util_format.writeToConsole(logInit,"LogGenerator:"+e.toString());			
			}catch (Throwable t) {
				util_format.writeToConsole(logInit,"LogGenerator:"+t.toString());			
			}
		}
		
		logG = new log_generator(logInit);
	}
}

}