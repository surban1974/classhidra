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

package stubs;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_reflect;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class stub_log implements iStub{

	public stub_log() {
		super();
	}

public void write(HashMap hm){
	try{
		Object mess = hm.get(iStub.log_stub_mess);
		Object exception = hm.get(iStub.log_stub_exception);
		Object throwable = hm.get(iStub.log_stub_throwable);		
		Object request = hm.get(iStub.log_stub_request);
		Object level = hm.get(iStub.log_stub_level);
		
		String sMess = (mess==null)?"":mess.toString();
		String sLevel = (level==null)?"":level.toString();

		Throwable thr = (exception==null)?((throwable==null)?null:(Throwable)throwable):(Throwable)exception;
		


//		System.out.println(level +": "+mess );

		
		String classInfo=util_reflect.prepareClassInfo(new String[]{"bsController.java","bsControllerException.java","bsControllerMessageException.java"},new String[]{"writeLog","<init>","<init>"});

		Logger log = Logger.getLogger(classInfo);
		
		if(sLevel.toUpperCase().equals("INFO")) log.log(Level.INFO, sMess, thr);
		if(sLevel.toUpperCase().equals("WARN")) log.log(Level.WARNING, sMess, thr);
		if(sLevel.toUpperCase().equals("DEBUG")) log.log(Level.FINE , sMess, thr);
		if(sLevel.toUpperCase().equals("ERROR")) log.log(Level.SEVERE, sMess, thr);
		if(sLevel.toUpperCase().equals("FATAL")) log.log(Level.SEVERE, sMess, thr);
	}catch(Exception e){
	}
}

/*
 	private static String getLogFile(){
		try{
	    	Enumeration en = log.getRootLogger().getAllAppenders();
	    	String fileName=null;
	    	while(en.hasMoreElements()){
	    		RollingFileAppender rfa = (RollingFileAppender)en.nextElement();
	    		fileName = rfa.getFile();
	    	}
	    	return fileName;
		}catch(Exception e){
			e.toString();
		}
		return null;
	}
	
 */

}
