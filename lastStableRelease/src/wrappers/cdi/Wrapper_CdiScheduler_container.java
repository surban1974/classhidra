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


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.scheduler.scheduling.BatchScheduling;

@Named(bsConstants.CONST_BEAN_$SCHEDULER_CONTAINER)
@ApplicationScoped

public class Wrapper_CdiScheduler_container extends bsProvidedWrapper {
	private static final long serialVersionUID = 1L; 

	private static BatchScheduling instance;

	public Wrapper_CdiScheduler_container(){
		super();		
		bsController.writeLog("Instanced Cdi Wrapper for [scheduler_container] -> "+this.getClass().getName(), iStub.log_INFO);
	}
	
	
	@Override
	public Object getInstance() {
		if(instance==null){
			instance = new BatchScheduling();
//			instance.start();
		}
		return instance;
	}

	@Override
	public boolean setInstance(Object _instance) {
		if(instance!=null && _instance instanceof BatchScheduling){
			try{
			instance.stop();
			((BatchScheduling)_instance).stop();
			instance = (BatchScheduling)_instance;
			instance.start();
			}catch(Exception e){
				new bsException(e,iStub.log_ERROR);
				return false;
			}
		}
		return true;
	}

}
