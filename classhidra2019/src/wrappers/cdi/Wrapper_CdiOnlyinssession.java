/**
* Creation date: (09/01/2016)
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
package wrappers.cdi;
 
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.tool.log.stubs.iStub;

@Named(bsConstants.CONST_BEAN_$ONLYINSSESSION)
@SessionScoped
public class Wrapper_CdiOnlyinssession extends bsProvidedWrapper {
	private static final long serialVersionUID = 1L;

	private static ConcurrentHashMap<Object,Object> instance = new ConcurrentHashMap<Object, Object>();

	public Wrapper_CdiOnlyinssession(){
		super();		
		bsController.writeLog("Instanced Cdi Wrapper for [onlyinssession] -> "+this.getClass().getName(), iStub.log_INFO);
	}
	
	
	@Override
	public Object getInstance() {
		return instance;
	}

	@Override
	public boolean setInstance(Object _instance) {
		if(instance!=null){
			if(_instance!=null && _instance instanceof Map)
				instance.putAll((Map<?,?>)_instance);
		}
		return true;
	}

}
