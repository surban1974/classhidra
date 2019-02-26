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

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface i_action extends listener_action, i_bean, Serializable{
	void init(HttpServletRequest request,HttpServletResponse response) throws bsControllerException;
	redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException;
	redirects syncroservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException;
	void actionBeforeRedirect(HttpServletRequest request, HttpServletResponse response) throws bsControllerException;
	void actionBeforeRedirect(HashMap<String,Object> wsParameters) throws bsControllerException;
	void init(HashMap<String,Object> wsParameters) throws bsControllerException;
	redirects actionservice(HashMap<String,Object> wsParameters) throws  bsControllerException;
	redirects syncroservice(HashMap<String,Object> wsParameters) throws  bsControllerException;
	i_bean get_bean();
	void set_bean(i_bean form);
	void set_bean(i_bean form, info_context iContext);
	info_action get_infoaction();
	void set_infoaction(info_action action);
	redirects getCurrent_redirect();
	void setCurrent_redirect(redirects redirect);
	boolean isIncluded();
	void setIncluded(boolean included);
	java.lang.reflect.Method getMethodForCall(String annotation_name);
	Object[] getMethodAndCall(String annotation_name);
	listener_action getListener_a();
	void setListener_a(listener_action listenerA);
	i_bean asBean();
	i_bean getRealBean();
	i_action asAction();
	boolean isBeanEqualAction();
	void enterActionContext(i_action oldAction, HttpServletRequest request, HttpServletResponse response);
	void enterActionContext(i_action oldAction,HashMap<String,Object> _content);	
	void leaveActionContext(i_action newAction, HttpServletRequest request, HttpServletResponse response);
	void leaveActionContext(i_action newAction,HashMap<String,Object> _content);
	
	action_payload delegatePayloadProcess(String id_call, iContext context, boolean beanInitFromRequest) throws bsControllerException,ServletException, UnavailableException;
}
