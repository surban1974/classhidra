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
package it.classhidra.core.controller;




import it.classhidra.core.tool.util.util_classes;

import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class bsResources extends HttpServlet implements bsConstants  {
	private static final long serialVersionUID = 1L;




	public void service(HttpServletRequest request, HttpServletResponse response)throws ServletException, UnavailableException {
		String id=request.getParameter("id");
		if(id!=null){
			byte[] content = null;
			try{
				
				String property_name = "resources/"+ id;

				InputStream is = null;



			    try {
			    	is = this.getClass().getResourceAsStream(property_name);
			    	if(is!=null){
			    		content = util_classes.readInputStream2Byte(is);
			    	}
			    }catch (Exception e) {
			    }finally {
			    	try {

			    		if (is != null) is.close();
			    	}catch (Exception e) {
			    	}
				}

				if(content!=null)
					response.getOutputStream().write(content);
			}catch(Exception ex){
			}
		}

	}
	

}
