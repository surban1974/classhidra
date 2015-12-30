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
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class bsResources extends HttpServlet implements bsConstants  {
	private static final long serialVersionUID = 1L;




	public void service(HttpServletRequest request, HttpServletResponse response)throws ServletException, UnavailableException {
		String id=request.getParameter("id");
		if(	id!=null &&
			!(id.equalsIgnoreCase(CONST_DIRECTINDACTION_bsLoadFromResources) || id.equalsIgnoreCase(CONST_DIRECTINDACTION_bsLoadFromFramework))	
		){
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
					writeToResponse(content,response);
			}catch(Exception ex){				
				writeToResponse("<![CDATA[ ERROR LOAD RESOURCE ID: "+id+" -> "+ex.toString()+" ]]>","UTF-8",response);
			}
		}
		
		if(id!=null &&
			(id.equalsIgnoreCase(CONST_DIRECTINDACTION_bsLoadFromResources) || id.equalsIgnoreCase(CONST_DIRECTINDACTION_bsLoadFromFramework))	
		){
			String loadSrc = request.getParameter(CONST_DIRECTINDACTION_bsLoadSrc);
			if(loadSrc==null) return;
			String loadType = request.getParameter(CONST_DIRECTINDACTION_bsLoadType);
	
			try{
				byte[] output = null;
				ArrayList resources = null;
				if(id.equalsIgnoreCase(CONST_DIRECTINDACTION_bsLoadFromResources)){
					if(loadSrc.trim().equals("") || loadSrc.lastIndexOf('/')==loadSrc.length()-1)
						resources = util_classes.getResourcesAsByte("it/classhidra/core/controller/resources/"+loadSrc, null);
					else
						util_classes.getResourceAsByte("it/classhidra/core/controller/resources/"+loadSrc);
					if(output==null)
						resources = util_classes.getResourcesAsByte("it/classhidra/core/controller/resources/"+loadSrc, null);							
				}
				if(id.equalsIgnoreCase(CONST_DIRECTINDACTION_bsLoadFromFramework)){
					if(loadSrc.trim().equals("") || loadSrc.lastIndexOf('/')==loadSrc.length()-1)
						resources = util_classes.getResourcesAsByte("it/classhidra/framework/resources/"+loadSrc, "\n\r".getBytes());
					else
						output = util_classes.getResourceAsByte("it/classhidra/framework/resources/"+loadSrc);	
					if(output==null)
						resources = util_classes.getResourcesAsByte("it/classhidra/framework/resources/"+loadSrc, "\n\r".getBytes());						
				}
						
				if(output!=null || resources!=null){
					if(loadType!=null)
						response.setContentType(loadType);
					setCache((HttpServletResponse)response, request.getParameter(bsController.CONST_DIRECTINDACTION_bsLoadCache));
				}
						
				if(output!=null){						
					writeToResponse(output,response);
				}
				if(resources!=null){
					if(loadType!=null)
						response.setContentType(loadType);
					 OutputStream os = response.getOutputStream();
					 for(int i=0;i<resources.size();i++)
						 os.write((byte[])resources.get(i));
		    		 os.flush();
		    		 os.close();
				}					
			}catch(Exception ex){	
				writeToResponse("<![CDATA[ ERROR LOAD RESOURCE ID: "+id+" -> "+ex.toString()+" ]]>","UTF-8",response);
			}
			return;
		}
	}
	
	private static void writeToResponse(String output, String encoding, HttpServletResponse response){
		try{
			OutputStream os = response.getOutputStream();
			if(output!=null){
				if(encoding!=null)
					os.write(output.getBytes(encoding));
				else 
					os.write(output.getBytes());
			}
   		 	os.flush();
   		 	os.close();
		}catch(Exception e){			
		}
	}
	
	private static void writeToResponse(byte[] output, HttpServletResponse response){
		try{
			OutputStream os = response.getOutputStream();
			if(output!=null){
				os.write(output);
			}
   		 	os.flush();
   		 	os.close();
		}catch(Exception e){			
		}
	}	
	
	private static void setCache(HttpServletResponse response, String cacheInSec){
		if(cacheInSec==null) return;
		try{
			final int CACHE_DURATION_IN_SECOND = Integer.valueOf(cacheInSec).intValue();
			long now = System.currentTimeMillis();
			((HttpServletResponse)response).addHeader("Cache-Control", "max-age=" + CACHE_DURATION_IN_SECOND);
			((HttpServletResponse)response).addHeader("Cache-Control", "must-revalidate");//optional
			((HttpServletResponse)response).setDateHeader("Last-Modified", now);
			((HttpServletResponse)response).setDateHeader("Expires", now + CACHE_DURATION_IN_SECOND  * 1000);
		}catch(Exception e){
		}

	}
}
