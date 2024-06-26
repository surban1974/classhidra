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

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.classhidra.annotation.elements.ActionCall;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.Expose;
import it.classhidra.annotation.elements.Redirect;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_supportbean;
import it.classhidra.core.tool.util.util_tag;
import it.classhidra.serialize.JsonWriter;
import it.classhidra.serialize.XmlWriter;

public class action extends bean implements i_action, Serializable{
	private static final long serialVersionUID = -6220391111031079562L;
	protected i_bean _bean;
	protected redirects current_redirect;
	protected boolean included = false;
	protected listener_action listener_a;
	protected boolean beanEqualAction=false;

	public action(){
		super();
	}

	@SuppressWarnings("unchecked")
	public void init(HttpServletRequest request, HttpServletResponse response) throws bsControllerException{
		i_bean bean4init = null;
//setCurrent_redirect(null);		
		if(bsController.getAppInit().get_ejb_avoid_loop_reentrant()==null || !bsController.getAppInit().get_ejb_avoid_loop_reentrant().equals("true")){
			if(getRealBean()!=null)
				bean4init = getRealBean();
			
		}else{
			if(!beanEqualAction){
				if(getRealBean()!=null)
					bean4init = getRealBean();
				
			}else
				bean4init = this;
			
		}
		if(bean4init!=null){
			boolean isRemoteEjb=false;
			HashMap<String,Object> request2map = null;
			if(bean4init.getInfo_context()!=null && bean4init.getInfo_context().isRemote()){
				isRemoteEjb=true;
				try{
					request2map = (HashMap<String,Object>)
							util_reflect.findDeclaredMethod(
								bean4init.asBean().getClass(),
								"convertRequest2Map", new Class[]{HttpServletRequest.class})
							.invoke(null, new Object[]{request});

				}catch (Exception e) {
					new bsControllerException(e, iStub.log_ERROR);
				}catch (Throwable e) {
					new bsControllerException(e, iStub.log_ERROR);
				}
				if(request2map==null)
					request2map = new HashMap<String,Object>();
	//			request2map = util_supportbean.request2map(request);
	
			}
			try{
				if(!isRemoteEjb)
					bean4init.onPreInit(request);
				else
					bean4init.onPreInit(request2map);
			}catch(Exception e){
			}
			try{
				if(!isRemoteEjb)
					bean4init.init(request);
				else
					util_supportbean.init(bean4init, request);
			}catch(Exception e){
				util_supportbean.init(bean4init, request);
			}
			try{
				if(!isRemoteEjb)
					bean4init.onPostInit(request);
				else
					bean4init.onPostInit(request2map);
			}catch(Exception e){
			}			
			
		}

		
	}
	public void init(HashMap<String,Object> wsParameters) throws bsControllerException{
		if(bsController.getAppInit().get_ejb_avoid_loop_reentrant()==null || !bsController.getAppInit().get_ejb_avoid_loop_reentrant().equals("true")){
			if(getRealBean()!=null){
				getRealBean().onPreInit(wsParameters);
				getRealBean().init_(wsParameters);
				getRealBean().onPostInit(wsParameters);
			}
		}else{
			if(!beanEqualAction){
				if(getRealBean()!=null){
					getRealBean().onPreInit(wsParameters);
					getRealBean().init_(wsParameters);
					getRealBean().onPostInit(wsParameters);
				}
			}else{
				this.onPreInit(wsParameters);
				this.init_(wsParameters);
				this.onPostInit(wsParameters);
			}
		}
	}
	public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException{
		return new redirects(_infoaction.getRedirect());
	}
	public synchronized redirects syncroservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException{
		return actionservice(request, response);
	}
	public redirects actionservice(HashMap<String,Object> wsParameters) throws  bsControllerException{
		return new redirects(_infoaction.getRedirect());
	}
	public synchronized redirects syncroservice(HashMap<String,Object> wsParameters) throws  bsControllerException{
		return actionservice(wsParameters);
	}
	public void actionBeforeRedirect(HttpServletRequest request, HttpServletResponse response) throws bsControllerException{
	}
	
	@ActionCall (name="componentupdate", navigated="false", entity=@Entity(
			property="allway:public"
	))
	public String componentupdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
		String navigation=request.getParameter("navigationId");
		String id=request.getParameter("componentId");
		return
			util_tag.getTagContent(navigation+":"+id,(String)get_bean().getComponents().get(navigation+":"+id),this,request,response);
	}


	@ActionCall (name="asyncupdate", navigated="false")
	public redirects asyncupdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {

		String target=request.getParameter("target");
		String formatOutput=request.getParameter("$formatOutput_"+target);
		String formatLanguage=request.getParameter("$formatLanguage_"+target);
		String formatCountry=request.getParameter("$formatCountry_"+target);
		String formatTimeZoneShift = request.getParameter("$formatTimeZoneShift_"+target);
		String replaceOnBlank=request.getParameter("$replaceOnBlank_"+target);
		String replaceOnErrorFormat=request.getParameter("$replaceOnErrorFormat_"+target);

		Object writeValue = get_bean().get(target);
		String value = null;
		try{
			value = util_format.makeFormatedString(formatOutput,formatLanguage,formatCountry,formatTimeZoneShift,writeValue);
			if(replaceOnBlank != null) value=util_format.replace(value,replaceOnBlank,"");
		}catch(Exception e){
			if(replaceOnErrorFormat != null)
				value = replaceOnErrorFormat;
		}


		try{
			if(value!=null)
				response.getOutputStream().write(new String("document.getElementById('"+target+"').value='"+value+"';").getBytes());
			else response.getOutputStream().write(new String("void(0);").getBytes());
		}catch(Exception ex){
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@ActionCall(name="json",navigated="false",Redirect=@Redirect(contentType="application/json", contentEncoding = "utf-8"))
	public String modelAsJson(HttpServletRequest request, HttpServletResponse response){
		
		String modelName = getString("outputserializedname");
		if(modelName==null || modelName.equals(""))
			modelName = (request==null)?modelName:request.getParameter("outputserializedname");
		
		String outputappliedfor = getOutputappliedfor();
		if(outputappliedfor==null || outputappliedfor.equals(""))
			outputappliedfor = (request==null)?outputappliedfor:request.getParameter("outputappliedfor");
		if(outputappliedfor!=null && !outputappliedfor.trim().equals(""))
			return JsonWriter.object2json(get_bean().asBean().get(outputappliedfor), (modelName==null || modelName.equals(""))?outputappliedfor:modelName);
		

		if(modelName==null || modelName.equals("")){
			if(get_infobean()!=null && get_infobean().getName()!=null && !get_infobean().getName().equals(""))
				modelName = get_infobean().getName();
			else if(get_infoaction()!=null && get_infoaction().getName()!=null && !get_infoaction().getName().equals(""))
				modelName = get_infoaction().getName();
			else
				modelName = "model";
		}
		if(	bsController.getLocalContainer()!=null &&
				bsController.getLocalContainer().get(bsConstants.CONST_FORM_SERIALIZE_FILTERS)!=null &&
				bsController.getLocalContainer().get(bsConstants.CONST_FORM_SERIALIZE_FILTERS) instanceof List) 
				return JsonWriter.object2json(this.get_bean().asBean(), modelName, (List<String>)bsController.getLocalContainer().get(bsConstants.CONST_FORM_SERIALIZE_FILTERS));	
		else		
			return JsonWriter.object2json(this.get_bean().asBean(), modelName);
	}
	
	@SuppressWarnings("unchecked")
	@ActionCall(name="jsonservice",Redirect=@Redirect(contentType="application/json", contentEncoding = "utf-8"))
	public String modelAsJsonService(HttpServletRequest request, HttpServletResponse response) throws Exception{

		actionservice(request, response);
		String modelName = getString("outputserializedname");
		if(modelName==null || modelName.equals(""))
			modelName = (request==null)?modelName:request.getParameter("outputserializedname");
		
		String outputappliedfor = getOutputappliedfor();
		if(outputappliedfor==null || outputappliedfor.equals(""))
			outputappliedfor = (request==null)?outputappliedfor:request.getParameter("outputappliedfor");
		if(outputappliedfor!=null && !outputappliedfor.trim().equals(""))
			return JsonWriter.object2json(get_bean().asBean().get(outputappliedfor), (modelName==null || modelName.equals(""))?outputappliedfor:modelName);
		

		if(modelName==null || modelName.equals("")){
			if(get_infobean()!=null && get_infobean().getName()!=null && !get_infobean().getName().equals(""))
				modelName = get_infobean().getName();
			else if(get_infoaction()!=null && get_infoaction().getName()!=null && !get_infoaction().getName().equals(""))
				modelName = get_infoaction().getName();
			else
				modelName = "model";
		}
		
		if(	bsController.getLocalContainer()!=null &&
			bsController.getLocalContainer().get(bsConstants.CONST_FORM_SERIALIZE_FILTERS)!=null &&
			bsController.getLocalContainer().get(bsConstants.CONST_FORM_SERIALIZE_FILTERS) instanceof List) 
			return JsonWriter.object2json(this.get_bean().asBean(), modelName, (List<String>)bsController.getLocalContainer().get(bsConstants.CONST_FORM_SERIALIZE_FILTERS));	
		else
			return JsonWriter.object2json(this.get_bean().asBean(), modelName);			

		

	}	
	
	@SuppressWarnings("unchecked")
	@ActionCall(name="xml",navigated="false",Redirect=@Redirect(contentType="application/xml", contentEncoding = "utf-8"))
	public String modelAsXml(HttpServletRequest request, HttpServletResponse response){
		
		String modelName = getString("outputserializedname");
		if(modelName==null || modelName.equals(""))
			modelName = (request==null)?modelName:request.getParameter("outputserializedname");
		
		String outputappliedfor = getOutputappliedfor();
		if(outputappliedfor==null || outputappliedfor.equals(""))
			outputappliedfor = (request==null)?outputappliedfor:request.getParameter("outputappliedfor");
		if(outputappliedfor!=null && !outputappliedfor.trim().equals(""))
			return XmlWriter.object2xml(get_bean().asBean().get(outputappliedfor), (modelName==null || modelName.equals(""))?outputappliedfor:modelName);
				
		

		if(modelName==null || modelName.equals("")){
			if(get_infobean()!=null && get_infobean().getName()!=null && !get_infobean().getName().equals(""))
				modelName = get_infobean().getName();
			else if(get_infoaction()!=null && get_infoaction().getName()!=null && !get_infoaction().getName().equals(""))
				modelName = get_infoaction().getName();
			else
				modelName = "model";
		}
		if(	bsController.getLocalContainer()!=null &&
				bsController.getLocalContainer().get(bsConstants.CONST_FORM_SERIALIZE_FILTERS)!=null &&
				bsController.getLocalContainer().get(bsConstants.CONST_FORM_SERIALIZE_FILTERS) instanceof List) 
				return XmlWriter.object2xml(this.get_bean().asBean(), modelName, (List<String>)bsController.getLocalContainer().get(bsConstants.CONST_FORM_SERIALIZE_FILTERS));	
		else		
			return XmlWriter.object2xml(this.get_bean().asBean(), modelName);
	}
	
	@SuppressWarnings("unchecked")
	@ActionCall(name="xmlservice",Redirect=@Redirect(contentType="application/xml", contentEncoding = "utf-8"))
	public String modelAsXmlService(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		actionservice(request, response);
		String modelName = getString("outputserializedname");
		if(modelName==null || modelName.equals(""))
			modelName = (request==null)?modelName:request.getParameter("outputserializedname");
		
		String outputappliedfor = getOutputappliedfor();
		if(outputappliedfor==null || outputappliedfor.equals(""))
			outputappliedfor = (request==null)?outputappliedfor:request.getParameter("outputappliedfor");
		if(outputappliedfor!=null && !outputappliedfor.trim().equals(""))
			return XmlWriter.object2xml(get_bean().asBean().get(outputappliedfor), (modelName==null || modelName.equals(""))?outputappliedfor:modelName);
				
		

		if(modelName==null || modelName.equals("")){
			if(get_infobean()!=null && get_infobean().getName()!=null && !get_infobean().getName().equals(""))
				modelName = get_infobean().getName();
			else if(get_infoaction()!=null && get_infoaction().getName()!=null && !get_infoaction().getName().equals(""))
				modelName = get_infoaction().getName();
			else
				modelName = "model";
		}
		if(	bsController.getLocalContainer()!=null &&
			bsController.getLocalContainer().get(bsConstants.CONST_FORM_SERIALIZE_FILTERS)!=null &&
			bsController.getLocalContainer().get(bsConstants.CONST_FORM_SERIALIZE_FILTERS) instanceof List) 
			return XmlWriter.object2xml(this.get_bean().asBean(), modelName, (List<String>)bsController.getLocalContainer().get(bsConstants.CONST_FORM_SERIALIZE_FILTERS));	
		else
			return XmlWriter.object2xml(this.get_bean().asBean(), modelName);
	}		

	public i_bean get_bean() {
		if(bsController.getAppInit().get_ejb_avoid_loop_reentrant()==null || !bsController.getAppInit().get_ejb_avoid_loop_reentrant().equals("true")){
			if(getRealBean()!=null && getRealBean().getClass().getName().equals(this.asBean().getClass().getName())) return this;
			if(getRealBean()==null) return this;
			return getRealBean();
		}else{
			if(getRealBean()!=null && getRealBean().getClass().getName().equals(this.asBean().getClass().getName())) return this;
			if(getRealBean()==null){
				if(!beanEqualAction)
					return this;
				else
					return this.asBean();
			}else{
				if(!beanEqualAction)
					return getRealBean();
				else
					return this.asBean();				
			}
		}
	}

	public void set_bean(i_bean form) {
		set_bean(form,null);
	}
	public void set_bean(i_bean form, info_context iContext) {
		if(	iContext!=null &&
			form!=null &&
			iContext.isProxiedEjb() &&
			this.getInfo_context()!=null &&
			iContext.getProxiedId()==this.getInfo_context().getProxiedId() &&
			this.get_infoaction()!=null)
			
			beanEqualAction=true;

		_bean = form;
		
		if(_bean!=null)
			get_bean().set_infoaction(_infoaction);
	}
	public redirects getCurrent_redirect() {
		return current_redirect;
	}
	public void setCurrent_redirect(redirects redirect) {
		current_redirect = redirect;
		String uri=null;
		if(current_redirect!=null) uri = current_redirect.get_uri();
		if(uri!=null && uri.indexOf("?")!=-1) uri=uri.substring(0,uri.indexOf("?"));
		if(current_redirect!=null && current_redirect.get_inforedirect()!=null && current_redirect.get_inforedirect().isExternal())
			return;
		else {
			try{
				info_redirect local = (info_redirect)_infoaction.get_redirects().get(uri);
				if(local!=null){
					info_redirect global = null;
					try{
						global = (info_redirect)load_actions.get_redirects().get(uri);
					}catch(Exception e){
					}
					if(global==null)
						current_redirect.set_inforedirect(local);
					else{
						if((local.getDescr()==null || local.getDescr().equals("")) && (global.getDescr()!=null && !global.getDescr().equals("")))
							local.setDescr(global.getDescr());
						if((local.getMess_id()==null || local.getMess_id().equals("")) && (global.getMess_id()!=null && !global.getMess_id().equals("")))
							local.setMess_id(global.getMess_id());	
						if((local.getContentType()==null || local.getContentType().equals("")) && (global.getContentType()!=null && !global.getContentType().equals("")))
							local.setContentType(global.getContentType());	
						if((local.getContentName()==null || local.getContentName().equals("")) && (global.getContentName()!=null && !global.getContentName().equals("")))
							local.setContentName(global.getContentName());	
						if((local.getContentEncoding()==null || local.getContentEncoding().equals("")) && (global.getContentEncoding()!=null && !global.getContentEncoding().equals("")))
							local.setContentEncoding(global.getContentEncoding());	
						if((local.getTransformationName()==null || local.getTransformationName().equals("")) && (global.getTransformationName()!=null && !global.getTransformationName().equals("")))
							local.setTransformationName(global.getTransformationName());	
						if((local.getAvoidPermissionCheck()==null || local.getAvoidPermissionCheck().equals("")) && (global.getAvoidPermissionCheck()!=null && !global.getAvoidPermissionCheck().equals("")))
							local.setAvoidPermissionCheck(global.getAvoidPermissionCheck());	
						
	
						
						current_redirect.set_inforedirect(local);
					}
				}else{
					info_redirect global = null;
					try{
						global = (info_redirect)load_actions.get_redirects().get(uri);
					}catch(Exception e){
					}
					if(global!=null)
						current_redirect.set_inforedirect(global);
					else if(get_infoaction()!=null && get_infoaction().getIRedirect()!=null)
						current_redirect.set_inforedirect(get_infoaction().getIRedirect());
					
				}
			}catch(Exception e){
				if( e instanceof java.lang.NullPointerException){
				}else new bsControllerException(e,iStub.log_ERROR);
			}
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

	private Object[] getMethodAndCall(Class<?> cls, String annotation_name){
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
					call.setAnnotated("true");
					call.setMappedMethodParameterTypes(current.getParameterTypes());
					Expose call_exposed = a_call.Expose();
					if(call_exposed!=null)
						call.addExposed(call_exposed.method()).addExposed(call_exposed.methods());
					
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

	public void onPostActionservice(redirects redirect,HashMap<String,Object> content) {
		if(listener_a!=null) listener_a.onPostActionservice(redirect,content);
	}

	public void onPostSyncroservice(redirects redirect,HashMap<String,Object> content) {
		if(listener_a!=null) listener_a.onPostSyncroservice(redirect,content);
	}

	public void onPreActionservice(HashMap<String,Object> content) {
		if(listener_a!=null) listener_a.onPreActionservice(content);
	}

	public void onPreSyncroservice(HashMap<String,Object> content) {
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
	
	public void onPreActionCall(String id_call, HashMap<String,Object> _content) {
		if(listener_a!=null) listener_a.onPreActionCall(id_call, _content);
	}

	public void onPostActionCall(redirects redirect, String id_call, HashMap<String,Object> _content) {
		if(listener_a!=null) listener_a.onPostActionCall(redirect, id_call, _content);
	}

	public void actionBeforeRedirect(HashMap<String,Object> wsParameters) throws bsControllerException {

	}	

	public void setOwner(i_action owner) {
	}

	public i_bean asBean(){
		return (bean)this;
	}
	
	public i_bean getRealBean(){
		return _bean;
	}

	public boolean isBeanEqualAction() {
		return beanEqualAction;
	}


	public static Map<String,Object> convertRequest2Map(HttpServletRequest request){
		return util_supportbean.request2map(request);
	}

	public static void convertMap2Request(HttpServletRequest request, HttpServletResponse response){
		
	}
	
	public void enterActionContext(i_action oldAction, HttpServletRequest request, HttpServletResponse response) {
	}

	public void enterActionContext(i_action oldAction, HashMap<String,Object> _content) {
	}	

	public void leaveActionContext(i_action newAction, HttpServletRequest request, HttpServletResponse response) {
	}

	public void leaveActionContext(i_action newAction, HashMap<String,Object> _content) {
	}	

	public action_payload delegatePayloadProcess(String id_call, iContext context, boolean beanInitFromRequest)
			throws bsControllerException,ServletException, UnavailableException{
		if(get_infoaction()!=null && get_infoaction().getLocked()!=null && get_infoaction().getLocked().equalsIgnoreCase("true"))
			return delegatePayloadProcessLocked(id_call, context, beanInitFromRequest);
		else
			return delegatePayloadProcessUnlocked(id_call, context, beanInitFromRequest);
	}

	private synchronized action_payload delegatePayloadProcessLocked(String id_call, iContext context, boolean beanInitFromRequest)
			throws bsControllerException,ServletException, UnavailableException{		
		return bsController.getActionInstancePayloadDelegated(this, id_call, context, beanInitFromRequest);	
	}
	
	private action_payload delegatePayloadProcessUnlocked(String id_call, iContext context, boolean beanInitFromRequest)
			throws bsControllerException,ServletException, UnavailableException{		
		return bsController.getActionInstancePayloadDelegated(this, id_call, context, beanInitFromRequest);	
	}
}
