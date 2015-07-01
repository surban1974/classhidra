/**
* Creation date: (10/06/2014)
* @author: Svyatoslav Urbanovych svyatoslav.urbanovych@gmail.com 
*/

/********************************************************************************
*
*	Copyright (C) 2005  Svyatoslav Urbanovych
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General License for more details.

* You should have received a copy of the GNU General License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*********************************************************************************/
package it.classhidra.core.controller;

import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface listener_action{
	void onPreInit(HttpServletRequest request, HttpServletResponse response);
	void onPreInit(HashMap _content);
	void onPreActionservice(HttpServletRequest request, HttpServletResponse response);
	void onPreSyncroservice(HttpServletRequest request, HttpServletResponse response);
	void onPreActionservice(HashMap _content);
	void onPreSyncroservice(HashMap _content);
	void onPreSet_bean();
	void onPreSetCurrent_redirect();
	void onPreRedirect();
	void onPreRedirectError();
	void onPreTransform(Object input);
	void onPreActionCall(String id_call, HttpServletRequest request, HttpServletResponse response);
	
	
	void onPostInit(HttpServletRequest request, HttpServletResponse response);
	void onPostInit(HashMap _content);
	void onPostActionservice(redirects redirect, HttpServletRequest request, HttpServletResponse response);
	void onPostSyncroservice(redirects redirect,HttpServletRequest request, HttpServletResponse response);
	void onPostActionservice(redirects redirect,HashMap _content);
	void onPostSyncroservice(redirects redirect,HashMap _content);	
	void onPostSet_bean();
	void onPostSetCurrent_redirect();
	void onPostRedirect(RequestDispatcher rd);
	void onPostRedirectError(RequestDispatcher rd);
	void onPostTransform(Object output);
	void onPostActionCall(redirects redirect,String id_call, HttpServletRequest request, HttpServletResponse response);
	void onPostInstance();
	void onPostInstanceFromProvider();
	
	void setOwner(i_action owner);
}
