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

import it.classhidra.core.controller.bsController;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;




public class stub_neoHort_log implements iStub{

	public stub_neoHort_log() {
		super();
	}

public void write(HashMap hm){
	try{
		Object mess = hm.get(iStub.log_stub_mess);
		Object exc = hm.get(iStub.log_stub_exception);
		Object request = hm.get(iStub.log_stub_request);
		Object level = hm.get(iStub.log_stub_level);
		bsController.writeLog(
				(request==null)?null:(HttpServletRequest)request,
				((mess==null)?"":(String)mess) + ((exc==null)?"":exc.toString()),
				(level==null)?"DEBUG":(String)level);

	}catch(Exception e){
	}
}
}
