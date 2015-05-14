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
import it.classhidra.core.tool.util.util_format;

import java.io.Serializable;
import java.util.HashMap;

import javax.servlet.http.*;
import javax.servlet.*;

public class action extends bean implements i_action, Serializable{
	private static final long serialVersionUID = -6220391111031079562L;
	private i_bean _bean;
	private redirects current_redirect;
	private boolean included = false;
	private listener_action listener_a;
	
	public action(){
		super();
	}	
	
	public void init(HttpServletRequest request,	HttpServletResponse response) throws bsControllerException{
		if(_bean!=null){
			_bean.onPreInit(request);
			_bean.init(request); 
			_bean.onPostInit(request);
		}
	}
	public void init(HashMap wsParameters) throws bsControllerException{
		if(_bean!=null){
			_bean.onPreInit(wsParameters);
			_bean.init(wsParameters);
			_bean.onPostInit(wsParameters);
		}
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

	@ActionCall (name="asyncupdate", navigated="false")
	public redirects asyncupdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {

		String target=request.getParameter("target");
		String formatOutput=request.getParameter("$formatOutput_"+target);
		String formatLanguage=request.getParameter("$formatLanguage_"+target);
		String formatCountry=request.getParameter("$formatCountry_"+target);
		String replaceOnBlank=request.getParameter("$replaceOnBlank_"+target);
		String replaceOnErrorFormat=request.getParameter("$replaceOnErrorFormat_"+target);
		
		Object writeValue = get_bean().get(target);
		String value = null;
		try{
			value = util_format.makeFormatedString(formatOutput,formatLanguage,formatCountry,writeValue);
			if(replaceOnBlank != null) value=util_format.replace(value,replaceOnBlank,"");			
		}catch(Exception e){
			if(replaceOnErrorFormat != null)
				value = replaceOnErrorFormat;
		}
		

		try{
			if(value!=null)
				response.getOutputStream().write(new String("document.getElementById('"+target+"').value='"+value+"';").getBytes());
			else response.getOutputStream().write(new String("void();").getBytes());
		}catch(Exception ex){
		}
		return null;
	}
	
	public i_bean get_bean() {
		if(_bean!=null && _bean.getClass().getName().equals(this.getClass().getName())) return this;
		if(_bean==null) return this;
		return _bean;
	}
	
	public void set_bean(i_bean form) {
		_bean = form;
		if(_bean!=null) _bean.set_infoaction(_infoaction);
	}
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
		return getMethodAndCall(this.getClass(), annotation_name);
	}
	
	private Object[] getMethodAndCall(Class cls, String annotation_name){
		info_call call=null;
		java.lang.reflect.Method[] mtds = cls.getMethods();
		for(int i=0;i<mtds.length;i++){
			java.lang.reflect.Method current = mtds[i];
			try{
				ActionCall a_call = current.getAnnotation(ActionCall.class);
				if(a_call!=null && a_call.name().equals(annotation_name)){
					call = new info_call();
					call.setName(a_call.name());
					if(a_call.method()==null || a_call.method().equals(""))
						call.setMethod(current.getName());
					else
						call.setMethod(a_call.method());
					if(a_call.navigated()==null || a_call.navigated().equals(""))
						call.setNavigated("false");
					else call.setNavigated(a_call.navigated());
					return new Object[]{current,call};
				}
			}catch (Exception e) {
			}
		}
		if(!cls.equals(action.class) && cls.getSuperclass()!=null)
			return getMethodAndCall(cls.getSuperclass(), annotation_name);
		return null;
	}
	
	public listener_action getListener_a() {
		return listener_a;
	}

	public void setListener_a(listener_action listenerA) {
		listener_a = listenerA;
		if(listener_a!=null) listener_a.setOwner((i_action)this);
	}	

	public void onPostActionservice(redirects redirect,HttpServletRequest request, HttpServletResponse response) {
		if(listener_a!=null) listener_a.onPostActionservice(redirect,request, response);
	}

	public void onPostSyncroservice(redirects redirect,HttpServletRequest request, HttpServletResponse response) {
		if(listener_a!=null) listener_a.onPostSyncroservice(redirect,request, response);
	}

	public void onPreActionservice(HttpServletRequest request, HttpServletResponse response) {
		if(listener_a!=null) listener_a.onPreActionservice(request, response);
	}

	public void onPreSyncroservice(HttpServletRequest request, HttpServletResponse response) {
		if(listener_a!=null) listener_a.onPreSyncroservice(request, response);
	}

	public void onPostInit(HttpServletRequest request, HttpServletResponse response) {
		if(listener_a!=null) listener_a.onPostInit(request, response);
	}

	public void onPreInit(HttpServletRequest request, HttpServletResponse response) {
		if(listener_a!=null) listener_a.onPreInit(request, response);
	}

	public void onPostActionservice(redirects redirect,HashMap content) {
		if(listener_a!=null) listener_a.onPostActionservice(redirect,content);
	}

	public void onPostSyncroservice(redirects redirect,HashMap content) {
		if(listener_a!=null) listener_a.onPostSyncroservice(redirect,content);
	}

	public void onPreActionservice(HashMap content) {
		if(listener_a!=null) listener_a.onPreActionservice(content);
	}

	public void onPreSyncroservice(HashMap content) {
		if(listener_a!=null) listener_a.onPreSyncroservice(content);
	}

	public void onPostSet_bean() {
		if(listener_a!=null) listener_a.onPostSet_bean();
	}

	public void onPreSet_bean() {
		if(listener_a!=null) listener_a.onPreSet_bean();
	}

	public void onPostSetCurrent_redirect() {
		if(listener_a!=null) listener_a.onPostSetCurrent_redirect();
	}

	public void onPreSetCurrent_redirect() {
		if(listener_a!=null) listener_a.onPreSetCurrent_redirect();
	}

	public void onPostRedirect(RequestDispatcher rd) {
		if(listener_a!=null) listener_a.onPostRedirect(rd);
	}

	public void onPreRedirect() {	
		if(listener_a!=null) listener_a.onPreRedirect();
	}

	public void onPostRedirectError(RequestDispatcher rd) {
		if(listener_a!=null) listener_a.onPostRedirectError(rd);
	}

	public void onPreRedirectError() {
		if(listener_a!=null) listener_a.onPreRedirectError();
	}

	public void onPostTransform(Object output) {
		if(listener_a!=null) listener_a.onPostTransform(output);
	}

	public void onPreTransform(Object input) {
		if(listener_a!=null) listener_a.onPreTransform(input);
	}

	public void onPostActionCall(redirects redirect,String idCall, HttpServletRequest request,HttpServletResponse response) {
		if(listener_a!=null) listener_a.onPostActionCall(redirect,idCall, request, response);
	}

	public void onPreActionCall(String idCall, HttpServletRequest request, HttpServletResponse response) {
		if(listener_a!=null) listener_a.onPreActionCall(idCall, request, response);		
	}

	public void onPostInstance() {
		if(listener_a!=null) listener_a.onPostInstance();
	}

	public void onPostInstanceFromProvider() {
		if(listener_a!=null) listener_a.onPostInstanceFromProvider();
	}
	
	public void setOwner(i_action owner) {
	}



}
