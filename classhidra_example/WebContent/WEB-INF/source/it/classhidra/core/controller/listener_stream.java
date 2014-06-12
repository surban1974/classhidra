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

public interface listener_stream {
	void onPreInit(HttpServletRequest request, HttpServletResponse response);
	void onPreEnter(HttpServletRequest request, HttpServletResponse response);
	void onPreExit(HttpServletRequest request, HttpServletResponse response);
	void onPreEnter(HashMap _content);
	void onPreExit(HashMap _content);
	void onPreRedirect(redirects _redirect, String id_action);

	
	void onPostInit(HttpServletRequest request, HttpServletResponse response);
	void onPostEnter(redirects redirect, HttpServletRequest request, HttpServletResponse response);
	void onPostExit(redirects redirect, HttpServletRequest request, HttpServletResponse response);
	void onPostEnter(redirects redirect, HashMap _content);
	void onPostExit(redirects redirect, HashMap _content);
	void onPostRedirect(RequestDispatcher rd);
	void onPostInstance();
	void onPostInstanceFromProvider();
	
	void setOwner(i_stream owner);
	
}
