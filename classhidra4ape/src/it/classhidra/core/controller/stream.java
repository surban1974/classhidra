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

import it.classhidra.core.tool.exception.bsControllerException;

import java.io.Serializable;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class stream implements i_stream, Serializable{
	private static final long serialVersionUID = -1;
	private HttpServletRequest _request;
	private i_stream _stream;
	private info_stream _infostream;

	
	public stream(){
		super();
	}	
	
	public void init(HttpServletRequest request,	HttpServletResponse response) throws bsControllerException{
		_request = request;
		if(_stream!=null)
			_stream.init(_request,response); 
	}
	public redirects streamservice_enter(HttpServletRequest request, HttpServletResponse response) throws bsControllerException{
		return null;
	}
	public redirects streamservice_exit(HttpServletRequest request, HttpServletResponse response) throws bsControllerException{
		return null;
	}
	public redirects streamservice_enter(HashMap wsParameters) throws bsControllerException{
		return null;
	}
	public redirects streamservice_exit(HashMap wsParameters) throws bsControllerException{
		return null;
	}	

	public info_stream get_infostream() {		
		return _infostream;
	}

	public void set_infostream(info_stream stream) {
		_infostream = stream;		
	}

	public RequestDispatcher redirect(ServletContext scontext, redirects _redirect, String id_action) throws ServletException, UnavailableException{
		RequestDispatcher rd=null;
		
		if(_infostream==null) return rd;
		if(_infostream.get_apply_to_action().size()==0 || _infostream.get_apply_to_action().get("*")!=null){
			info_action iAction = new info_action();
			iAction.get_redirects().put("*", _redirect.get_inforedirect());
			iAction.setRedirect(_redirect.get_uri());
			rd = _redirect.redirect(scontext, iAction);
			return rd;
		}else{ 
			
//			info_action iAction = (info_action)bsController.getAction_config().get_actions().get(id_action);
			info_action iAction = (info_action)load_actions.get_actions().get(id_action);

			if(iAction!=null) rd = _redirect.redirect(scontext, iAction);
		}
		

		return rd;
	}

}
