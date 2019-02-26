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

import javax.servlet.http.HttpServletRequest;

public class bsControllerException extends bsException {
	private static final long serialVersionUID = 5306016507625978656L;
/*	
private bsControllerException() {
	super();	
	try{
		String _log="";
		if(getMessage()!=null) _log+=getMessage()+" ";
		bsController.writeLog(_log+toString(),iStub.log_ERROR);
	}catch(Exception e){
	}
}
*/
public bsControllerException(Exception e, String level) {
	super(e.toString());	
	try{
		String _log="";
		if(e.getMessage()!=null) _log+=getMessage()+" ";
		bsController.writeLog(_log+e.toString(),level);
	}catch(Exception ex){
	}
}
public bsControllerException(Throwable t, String level) {
	super(t.toString());	
	try{
		String _log="";
		if(t.getMessage()!=null) _log+=getMessage()+" ";
		bsController.writeLog(_log+t.toString(),level);
	}catch(Exception ex){
	}
}
public bsControllerException(String mess, String level) {
	super(mess);	
	try{
		bsController.writeLog(mess,level);
	}catch(Exception e){
	}
}
public bsControllerException(String mess, HttpServletRequest request, String level) {
	super(mess);	
	try{
		bsController.writeLog(request,mess,level);
	}catch(Exception e){
	}
}

}