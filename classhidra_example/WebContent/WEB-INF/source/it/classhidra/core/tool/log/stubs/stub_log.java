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

package it.classhidra.core.tool.log.stubs;
import it.classhidra.core.tool.util.util_format;

import java.util.HashMap;

public class stub_log implements iStub{

	public stub_log() {
		super();
	}

public void write(HashMap hm){
	try{
		Object class_info = hm.get(iStub.log_class_info);
		Object mess = hm.get(iStub.log_stub_mess);
		Object exception = hm.get(iStub.log_stub_exception);
		Object throwable = hm.get(iStub.log_stub_throwable);		
		Object request = hm.get(iStub.log_stub_request);
		Object servletcontext = hm.get(iStub.log_stub_servletcontext);
		Object level = hm.get(iStub.log_stub_level);

		util_format.writeToConsole(null,
				"\nCLASSINFO:"+class_info + "\nMESSAGE:"+mess + "\nEXCEPTION:" +exception +"\nTHROWABLE:"+throwable+"\nLEVEL:"+level +"\nREQUEST:"+request+"\nSERVLETCONTEXT:"+servletcontext
		);

	}catch(Exception e){
	}
}
}
