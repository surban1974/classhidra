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

import it.classhidra.annotation.elements.ActionCall;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;

import java.io.Serializable;
import java.util.HashMap;

import javax.servlet.http.*;
import javax.servlet.*;

public class action extends bean implements i_action, Serializable{
	private static final long serialVersionUID = -6220391111031079562L;
	private i_bean _bean;
//	private info_action _infoaction;
	private redirects current_redirect;
	private boolean included = false;
	
	public action(){
		super();
	}	
	
	public void init(HttpServletRequest request,	HttpServletResponse response) throws bsControllerException{
		if(_bean!=null)
			_bean.init(request); 
	}
	public void init(HashMap wsParameters) throws bsControllerException{
		if(_bean!=null)
			_bean.init(wsParameters); 
	}	
	public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException{
		return new redirects(_infoaction.getRedirect());
	}
	public synchronized redirects syncroservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException{
		return actionservice(request, response);
	}
	public redirects actionservice(HashMap wsParameters) throws  bsControllerException{
		return new redirects(_infoaction.getRedirect());
	}
	public synchronized redirects syncroservice(HashMap wsParameters) throws  bsControllerException{
		return actionservice(wsParameters);
	}
	
	public void actionBeforeRedirect(HttpServletRequest request, HttpServletResponse response) throws bsControllerException{
	}
	public i_bean get_bean() {
		if(_bean!=null && _bean.getClass().getName().equals(this.getClass().getName())) return this;
		if(_bean==null) return this;
		return _bean;
	}
/*
	public info_action get_infoaction() {
		return _infoaction;
	}
*/	
	public void set_bean(i_bean form) {
		_bean = form;
		if(_bean!=null) _bean.set_infoaction(_infoaction);
	}
/*	
	public void set_infoaction(info_action action) {
		_infoaction = action;
	}
*/	
	public redirects getCurrent_redirect() {
		return current_redirect;
	}
	public void setCurrent_redirect(redirects redirect) {
		current_redirect = redirect;
		String uri=null;
		if(current_redirect!=null) uri = current_redirect.get_uri();
		if(uri!=null && uri.indexOf("?")!=-1) uri=uri.substring(0,uri.indexOf("?"));
		try{		
			if(_infoaction.get_redirects().get(uri)!=null)
				current_redirect.set_inforedirect((info_redirect)_infoaction.get_redirects().get(uri));
		}catch(Exception e){	
			if( e instanceof java.lang.NullPointerException){				
			}else new bsControllerException(e,iStub.log_DEBUG);
		}
	}

	public boolean isIncluded() {
		return included;
	}

	public void setIncluded(boolean included) {
		this.included = included;
	}
	
	public java.lang.reflect.Method getMethodForCall(String annotation_name){
		java.lang.reflect.Method result=null;
		java.lang.reflect.Method[] mtds = this.getClass().getMethods();
		for(int i=0;i<mtds.length;i++){
			java.lang.reflect.Method current = mtds[i];
			try{
				ActionCall a_call = current.getAnnotation(ActionCall.class);
				if(a_call!=null && a_call.name().equals(annotation_name))
					return current;
			}catch (Exception e) {
			}
		}
		return result;
	}
	
	public Object[] getMethodAndCall(String annotation_name){
		info_call call=null;
		java.lang.reflect.Method[] mtds = this.getClass().getMethods();
		for(int i=0;i<mtds.length;i++){
			java.lang.reflect.Method current = mtds[i];
			try{
				ActionCall a_call = current.getAnnotation(ActionCall.class);
				if(a_call!=null && a_call.name().equals(annotation_name)){
					call = new info_call();
					call.setMethod(a_call.method());
					call.setNavigated(a_call.navigated());
					return new Object[]{current,call};
				}
			}catch (Exception e) {
			}
		}
		return null;
	}

}
