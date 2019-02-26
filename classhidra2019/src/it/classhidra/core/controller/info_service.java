/**
* Creation date: (09/05/2006)
* @author: Svyatoslav Urbanovych svyatoslav.urbanovych@gmail.com 
*/

/********************************************************************************
*
*	Copyright (C) 2006  Svyatoslav Urbanovych
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
package it.classhidra.core.controller;

import it.classhidra.core.tool.elements.elementBase;
import it.classhidra.core.tool.elements.i_elementBase;

import javax.servlet.http.HttpServletRequest;


public class info_service extends elementBase implements i_elementBase{
	private static final long serialVersionUID = 6067497885324975367L;
	public String parent_pointOfLaunch;
	public String child_pointOfReturn;
	public String id_pointOfService;
	public boolean complete;
	
	
	public info_service(){
		super();
		reimposta();
	}

	public info_service(String parent_pointOfLaunch,String child_pointOfReturn,String id_pointOfService){
		super();
		reimposta();
		this.parent_pointOfLaunch=parent_pointOfLaunch;
		this.child_pointOfReturn=child_pointOfReturn;
		this.id_pointOfService=id_pointOfService;
		if(	this.parent_pointOfLaunch!=null && !this.parent_pointOfLaunch.equals("undefined") &&
			this.child_pointOfReturn!=null && !this.child_pointOfReturn.equals("undefined") && 
			this.id_pointOfService!=null && !this.id_pointOfService.equals("undefined")) complete = true;
		else  complete = false;	
		
	}

	public info_service(HttpServletRequest request){
		super();
		reimposta();
		this.parent_pointOfLaunch=request.getParameter(bsController.CONST_SERVICE_$PARENT_POINTOFLAUNCH);
		this.child_pointOfReturn=request.getParameter(bsController.CONST_SERVICE_$CHILD_POINTOFRETURN);
		this.id_pointOfService=request.getParameter(bsController.CONST_SERVICE_$ID_POINTOFSERVICE);
		if(	this.parent_pointOfLaunch!=null && !this.parent_pointOfLaunch.equals("undefined") &&
			this.child_pointOfReturn!=null && !this.child_pointOfReturn.equals("undefined") && 
		 	this.id_pointOfService!=null && !this.id_pointOfService.equals("undefined")) complete = true;
		else  complete = false;	
	}


	public void reimposta(){
		parent_pointOfLaunch="";
		child_pointOfReturn="";
		id_pointOfService="";
		complete=false;
	}


	public String getChild_pointOfReturn() {
		return child_pointOfReturn;
	}

	public boolean isComplete() {
		return complete;
	}

	public String getId_pointOfService() {
		return id_pointOfService;
	}

	public String getParent_pointOfLaunch() {
		return parent_pointOfLaunch;
	}

	public void setChild_pointOfReturn(String string) {
		child_pointOfReturn = string;
	}

	public void setComplete(boolean b) {
		complete = b;
	}

	public void setId_pointOfService(String string) {
		id_pointOfService = string;
	}

	public void setParent_pointOfLaunch(String string) {
		parent_pointOfLaunch = string;
	}

}
