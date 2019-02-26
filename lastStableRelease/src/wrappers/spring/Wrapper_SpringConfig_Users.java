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



import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.tool.jaas_authentication.load_users;
import it.classhidra.core.tool.log.stubs.iStub;



@Component(bsConstants.CONST_BEAN_$USERS_CONFIG)
@Scope(value="singleton")

public class Wrapper_SpringConfig_Users extends bsProvidedWrapper {
	private static final long serialVersionUID = 1L;
	private static load_users instance;

	public Wrapper_SpringConfig_Users(){
		super();		
		bsController.writeLog("Instanced Spring Wrapper for [load_users] -> "+this.getClass().getName(), iStub.log_INFO);
	}	
		
	
	@Override
	public Object getInstance() {
		return instance;
	}

	@Override
	public boolean setInstance(Object _instance) {
		if(_instance instanceof load_users)
			instance=(load_users)_instance;
		return true;
	}
}
