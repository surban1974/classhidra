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

package it.classhidra.core.tool.log;


import it.classhidra.core.controller.bsController;
import it.classhidra.core.tool.util.util_format;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class log_patternSimple implements i_log_pattern_web{
	
	public String prepare(String mes,String level){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
		Date currentTime = new Date();
		String message="["+formatter.format(currentTime)+"] ["+level+"] "+mes;
		if(bsController.getAppInit().get_path()!=null && !bsController.getAppInit().get_path().equals(""))
			message="["+bsController.getAppInit().get_path()+"]"+message;
		if(level==null || level.toUpperCase().equals("DEBUG")) 
			util_format.writeToConsole(null,message);			
		return message;
	}
	
	public String prepare(HttpServletRequest request, String mes,String level){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
		Date currentTime = new Date();
		String message="["+formatter.format(currentTime)+"] ["+level+"] "+mes;
		if(bsController.getAppInit().get_path()!=null && !bsController.getAppInit().get_path().equals(""))
			message="["+bsController.getAppInit().get_path()+"]"+message;
		if(level==null || level.toUpperCase().equals("DEBUG")) 
			util_format.writeToConsole(null,message);
		return message;
	}	

	public String prepare(String mes, String userIP, String userMatricola, String classFrom, String level){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
		Date currentTime = new Date();
		String message="["+formatter.format(currentTime)+"]["+level+"]"+userIP+"["+userMatricola+"]["+mes+"]["+classFrom+"]";
		if(bsController.getAppInit().get_path()!=null && !bsController.getAppInit().get_path().equals(""))
			message="["+bsController.getAppInit().get_path()+"]"+message;
		if(level==null || level.toUpperCase().equals("DEBUG")) 
			util_format.writeToConsole(null,message);			
		return message;
	}
	
	public String prepare(HttpServletRequest request, String mes, String userIP, String userMatricola,String classFrom, String level){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
		Date currentTime = new Date();
		String message="["+formatter.format(currentTime)+"]["+level+"]"+userIP+"["+userMatricola+"]["+mes+"]["+classFrom+"]";
		if(bsController.getAppInit().get_path()!=null && !bsController.getAppInit().get_path().equals(""))
			message="["+bsController.getAppInit().get_path()+"]"+message;
		if(level==null || level.toUpperCase().equals("DEBUG")) 
			util_format.writeToConsole(null,message);
		return message;
	}	
}
