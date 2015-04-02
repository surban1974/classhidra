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

import javax.servlet.http.HttpServletRequest;

public interface listener_bean {
	void onPreInit(HttpServletRequest request);
	void onPreInit(HashMap _content);
	void onPreInit(i_bean another_bean);
	void onPreValidate(HttpServletRequest request);
	void onAddToNavigation();
	void onGetFromNavigation();
	void onAddToSession();
	void onAddToLastInstance();
	void onGetFromSession();
	void onGetFromLastInstance();
	
	void onPostInit(HttpServletRequest request);
	void onPostInit(HashMap _content);
	void onPostInit(i_bean another_bean);
	void onPostValidate(redirects redirect, HttpServletRequest request);
	void onPostInstance();
	void onPostInstanceFromProvider();
	
	void setOwner(i_bean owner);
	
}
