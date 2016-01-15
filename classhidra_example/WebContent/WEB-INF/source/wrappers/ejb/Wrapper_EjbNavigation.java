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
package wrappers.ejb;
 
import javax.ejb.Local;
import javax.ejb.Stateful;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.controller.i_ProviderWrapper;
import it.classhidra.core.controller.info_navigation;
import it.classhidra.core.tool.log.stubs.iStub;


@Stateful(name="ejb_navigation")
@Local(i_ProviderWrapper.class)
public class Wrapper_EjbNavigation extends bsProvidedWrapper implements i_ProviderWrapper{
	private static final long serialVersionUID = 1L;
	private info_navigation instance = new info_navigation();

	public Wrapper_EjbNavigation(){
		super();		
		bsController.writeLog("Instanced Ejb Wrapper for [info_navigation] -> "+this.getClass().getName(), iStub.log_INFO);
	}		
	
	@Override
	public Object getInstance() {
		if(instance!=null && (instance.getId()==null || instance.getId().equals("")))
			return null;
		return instance;
	}

	@Override
	public boolean setInstance(Object _instance) {
		if(_instance!=null && _instance instanceof info_navigation){
			if(instance!=null && (instance.getId()==null || instance.getId().equals(""))){
				try{
					instance.reInit((info_navigation)_instance);
				}catch(Exception e){
					
				}
			}
		}
		return true;
	}

}
