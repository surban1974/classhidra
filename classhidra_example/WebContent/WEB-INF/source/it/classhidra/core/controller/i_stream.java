/**
* Creation date: (29/01/2009)
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


public interface i_stream extends listener_stream, Serializable{
	void init(HttpServletRequest request,HttpServletResponse response) throws bsControllerException;
	redirects streamservice_enter(HttpServletRequest request, HttpServletResponse response) throws bsControllerException;
	redirects streamservice_exit(HttpServletRequest request, HttpServletResponse response) throws bsControllerException;
	redirects streamservice_enter(HashMap wsParameters) throws bsControllerException;
	redirects streamservice_exit(HashMap wsParameters) throws bsControllerException;
	info_stream get_infostream();
	void set_infostream(info_stream stream);
	RequestDispatcher redirect(ServletContext scontext, redirects _redirect, String id_action) throws ServletException, UnavailableException;
	listener_stream getListener_s();
	void setListener_s(listener_stream listenerS);
}
