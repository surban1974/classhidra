/**
* Creation date: (04/07/2015)
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
package it.classhidra.core.tool.util;



import java.io.DataInputStream;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;


import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.info_navigation;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.serialize.JsonMapper;
import it.classhidra.serialize.JsonReader2Map;
import it.classhidra.serialize.Serialized;
import it.classhidra.serialize.XmlMapper;
import it.classhidra.serialize.XmlReader2Map;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;



public class util_supportbean  {


	public static HashMap<String,Object> request2map(HttpServletRequest request){
		HashMap<String,Object> result = new HashMap<String,Object>();
		Enumeration<?> en = request.getParameterNames();
		while(en.hasMoreElements()){
			String key = (String)en.nextElement();
			String value = request.getParameter(key);
			result.put(key, value);
		}
		en = request.getAttributeNames();
		while(en.hasMoreElements()){
			String key = (String)en.nextElement();
			Object value = request.getAttribute(key);
			result.put(key, value);
		}	
		HashMap<String,Object> parameters = util_multipart.popolateHashMap(request);
		result.putAll(parameters);
		
		return result;
	}
	
	
	public static void init(i_action action_instance, HashMap<String,Object> request2map, HttpServletRequest request) throws bsControllerException{
//if(action_instance!=null)
//	action_instance.setCurrent_redirect(null);
		if(request==null) return;
	
		if(bsController.getAppInit().get_ejb_avoid_loop_reentrant()==null || !bsController.getAppInit().get_ejb_avoid_loop_reentrant().equals("true")){
			if(action_instance.getRealBean()!=null){
				action_instance.getRealBean().onPreInit(request2map);
				util_supportbean.init(action_instance.getRealBean(), request);
				action_instance.getRealBean().onPostInit(request2map);
			}
		}else{
			if(!action_instance.isBeanEqualAction()){
				if(action_instance.getRealBean()!=null){
					action_instance.getRealBean().onPreInit(request2map);
					util_supportbean.init(action_instance.getRealBean(), request);
					action_instance.getRealBean().onPostInit(request2map);
				}
			}else{
				action_instance.onPreInit(request2map);
				util_supportbean.init(action_instance, request);
				action_instance.onPostInit(request2map);
			}
		}
	
	}
	
	
	public static void init(i_bean bean, HttpServletRequest request) throws bsControllerException{
		if(request==null) return;
		if(bean.getCurrent_auth()==null){
			try{
				bean.setCurrent_auth(bsController.checkAuth_init(request));
			}catch(Exception ex){
			}
		}
	
		if(bean.get_infoaction()!=null && bean.get_infoaction().getReloadAfterNextNavigated()!=null && bean.get_infoaction().getReloadAfterNextNavigated().equalsIgnoreCase("false")){
			String id_prev = request.getParameter(bsController.CONST_BEAN_$NAVIGATION);
			if(id_prev!=null && id_prev.indexOf(":")>-1){
				id_prev = id_prev.substring(0,id_prev.indexOf(":"));
				info_navigation nav = bsController.getFromInfoNavigation(null, request);
				if(nav!=null){
					int level_prev = nav.findLevel(id_prev, 0);
					int level_curr = nav.findLevel(bean.get_infoaction().getPath(), 0);
					if(level_curr<level_prev && level_curr!=-1) return;
				}
	
	
			}
	
		}
	
		if(request.getContentType()!=null && request.getContentType().indexOf("multipart")>-1){
			initMultiPart(bean,request);
			return;
		}
	
		if(request.getContentType()!=null && request.getContentType().toLowerCase().indexOf("application/json")>-1){
			if(initJsonPart(bean,request)) return;
		}
		
		if(request.getContentType()!=null && request.getContentType().toLowerCase().indexOf("application/xml")>-1){
			if(initXmlPart(bean,request)) return;
		}
	
		initNormal(bean, request);
	
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void initNormal(i_bean bean,HttpServletRequest request) throws bsControllerException{
		bean.setXmloutput(false);
		bean.setXmloutput_encoding("");
		bean.setJsonoutput(false);
		bean.setJsonoutput_encoding("");
		bean.setBinaryoutput(false);
		bean.setBinaryoutput_encoding("");
		bean.setOutputappliedfor(null);
		bean.setOutputserializedname(null);
		

		boolean inputBase64 = (request.getParameter(bsController.CONST_ID_INPUTBASE64)!=null &&
				(
						request.getParameter(bsController.CONST_ID_INPUTBASE64).equalsIgnoreCase("true") ||
						request.getParameter(bsController.CONST_ID_INPUTBASE64).equalsIgnoreCase(util_base64.encode("true".getBytes()))
				)
			);
		
		

		Enumeration<?> en = request.getParameterNames();
		while(en.hasMoreElements()){
			String key = (String)en.nextElement();
			String value = request.getParameter(key);
			String format = request.getParameter("$format_"+key);
			String replaceOnBlank = request.getParameter("$replaceOnBlank_"+key);
			String replaceOnErrorFormat = request.getParameter("$replaceOnErrorFormat_"+key);

			if(inputBase64){
				String charset = (request.getCharacterEncoding()==null || request.getCharacterEncoding().equals(""))?"UTF-8":request.getCharacterEncoding();
				try{
					if(value!=null) value=new String(util_base64.decode(value),charset);
				}catch(Exception e){}
				try{
					if(format!=null) format=new String(util_base64.decode(format),charset);
				}catch(Exception e){}
				try{
					if(replaceOnBlank!=null) replaceOnBlank=new String(util_base64.decode(replaceOnBlank),charset);
				}catch(Exception e){}
				try{
					if(replaceOnErrorFormat!=null) replaceOnErrorFormat=new String(util_base64.decode(replaceOnErrorFormat),charset);
				}catch(Exception e){
				}
			}

			if(key.indexOf(".")==-1){
				try{
					Object makedValue=null;
					if(format!=null){
						if(bean.getDelegated()!=null){
							makedValue=util_makeValue.makeFormatedValue1(bean.getDelegated(),format,value,key,replaceOnBlank,replaceOnErrorFormat);
							if(makedValue==null) makedValue=util_makeValue.makeFormatedValue1(bean,format,value,key,replaceOnBlank,replaceOnErrorFormat);
						}else{
							makedValue=util_makeValue.makeFormatedValue1(bean,format,value,key,replaceOnBlank,replaceOnErrorFormat);
						}
					}else{
						if(bean.getDelegated()!=null){
							makedValue=util_makeValue.makeValue1(bean.getDelegated(),value,key);
							if(makedValue==null) makedValue=util_makeValue.makeValue1(bean,value,key);
						}else{
							makedValue=util_makeValue.makeValue1(bean,value,key);
						}
					}
					if(!bean.setCampoValueWithPoint(key,makedValue))
						throw new Exception();
				}catch(Exception e){
					try{
						Object makedValue=null;
						if(format!=null){
							if(bean.getDelegated()!=null){
								makedValue=util_makeValue.makeFormatedValue(bean.getDelegated(),format,value,bean.getCampoValue(key),replaceOnBlank,replaceOnErrorFormat);
								if(makedValue==null) makedValue=util_makeValue.makeFormatedValue(bean,format,value,bean.getCampoValue(key),replaceOnBlank,replaceOnErrorFormat);
							}else{
								makedValue=util_makeValue.makeFormatedValue(bean,format,value,bean.getCampoValue(key),replaceOnBlank,replaceOnErrorFormat);
							}
						}else makedValue=util_makeValue.makeValue(value,bean.getCampoValue(key));
						if(!bean.setCampoValueWithPoint(key,makedValue))
							throw new Exception();
					}catch(Exception ex){
						bean.getInitErrors().put(key,"Init Normal: ["+key+"] not found in the bean ["+bean.getClass().getName()+"]. Will be added into FLY.");
						if(bean.getParametersFly()==null)
							bean.setParametersFly(new HashMap<String, Object>());
						if(key!=null && key.length()>0 && key.indexOf(0)!='$')
							bean.getParametersFly().put(key, value);
					}
				}
			}else{

				Object writeValue=null;
				Object current_requested = (bean.getDelegated()==null)?bean:bean.getDelegated();

				String last_field_name = null;
				StringTokenizer st = new StringTokenizer(key,".");
				while(st.hasMoreTokens()){
					if(st.countTokens()>1){
						String current_field_name = st.nextToken();
						try{
							if(writeValue==null && current_requested instanceof Map) writeValue = ((Map)current_requested).get(current_field_name);
							if(writeValue==null && current_requested instanceof List) writeValue = ((List)current_requested).get(Integer.valueOf(current_field_name).intValue());
							if(writeValue==null && current_requested instanceof Set) writeValue = Arrays.asList(((Set)current_requested)).toArray()[Integer.valueOf(current_field_name).intValue()];

							if(writeValue==null && current_requested.getClass().isArray()){
								Class<?> componentType = current_requested.getClass().getComponentType();
								if(!componentType.isPrimitive())
									writeValue = ((Object[])current_requested)[Integer.valueOf(current_field_name).intValue()];
								else{
									if (boolean.class.isAssignableFrom(componentType))
										writeValue = ((boolean[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
									else if (byte.class.isAssignableFrom(componentType))
										writeValue = ((byte[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
									else if (char.class.isAssignableFrom(componentType))
										writeValue = ((char[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
									else if (double.class.isAssignableFrom(componentType))
										writeValue = ((double[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
									else if (float.class.isAssignableFrom(componentType))
										writeValue = ((float[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
									else if (int.class.isAssignableFrom(componentType))
										writeValue = ((int[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
									else if (long.class.isAssignableFrom(componentType))
										writeValue = ((long[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
									else if (short.class.isAssignableFrom(componentType))
										writeValue = ((short[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
								}
							}

							if(writeValue==null) writeValue = util_reflect.getValue(current_requested,"get"+util_reflect.adaptMethodName(current_field_name),null);
							if(writeValue==null) writeValue = util_reflect.getValue(current_requested,current_field_name,null);
							if(writeValue==null && current_requested instanceof i_bean) writeValue = ((i_bean)current_requested).get(current_field_name);
						}catch(Exception e){
						}
						current_requested = writeValue;
					}else{
						last_field_name = st.nextToken();
					}
					writeValue = null;
				}

				if(current_requested!=null){
					try{
						if(format!=null)
							bean.setCampoValuePoint(
									current_requested,
									last_field_name,
									util_makeValue.makeFormatedValue1(
												current_requested,
												format,
												value,
												last_field_name,
												replaceOnBlank,
												replaceOnErrorFormat
									),
									false
							);
						else bean.setCampoValuePoint(
									current_requested,
									last_field_name,
									util_makeValue.makeValue1(current_requested,value,last_field_name),
									false
								);
					}catch(Exception e){
						try{
							if(format!=null)
								bean.setCampoValuePoint(current_requested,key,util_makeValue.makeFormatedValue((bean.getDelegated()==null)?bean:bean.getDelegated(),format,value,bean.getCampoValue(key),replaceOnBlank,replaceOnErrorFormat),false);
							else 
								bean.setCampoValuePoint(current_requested,key,util_makeValue.makeValue(value,bean.getCampoValue(key)),false);
						}catch(Exception ex){
							bean.getInitErrors().put(key,"Init Normal: ["+key+"] not found in the bean ["+bean.getClass().getName()+"].");
						}
					}
				}else{
					bean.getInitErrors().put(key,"Init Normal: ["+key+"] not found in the bean ["+bean.getClass().getName()+"].");
				}
			}
		}
		
		bean.initFromMap((HashMap<String,Object>)request.getAttribute(bsController.CONST_REST_URLMAPPEDPARAMETERS),true);
		
	}


	@SuppressWarnings("unchecked")
	public static void initMultiPart(i_bean bean, HttpServletRequest request) throws bsControllerException{
		HashMap<String,Object> parameters = util_multipart.popolateHashMap(request);
		bean.initPartFromMap(parameters);
		bean.initFromMap((HashMap<String,Object>)request.getAttribute(bsController.CONST_REST_URLMAPPEDPARAMETERS),true);
	}
	
	public static boolean initJsonPart(i_bean bean, HttpServletRequest request) throws bsControllerException{
		return initJsonPart(bean, request, null);
	}

	
	@SuppressWarnings("unchecked")
	public static boolean initJsonPart(i_bean bean, HttpServletRequest request, JsonMapper mapper) throws bsControllerException{
		boolean isJson=false;
		HashMap<String,Object> parameters = new HashMap<String,Object>();

		
		DataInputStream in = null;
		byte[] dataBytes = null;
		try{
			in = new DataInputStream(request.getInputStream());
			int formDataLength = request.getContentLength();

			dataBytes = new byte[formDataLength];
			int bytesRead = 0;
			int totalBytesRead = 0;
			while (totalBytesRead < formDataLength && totalBytesRead>-1) {
				bytesRead = in.read(dataBytes, totalBytesRead, formDataLength);
				totalBytesRead += bytesRead;
			}
			
			in.close();

			String json = new String(dataBytes,0,dataBytes.length).trim();

			if(mapper!=null){
				parameters = (HashMap<String, Object>)mapper.mapping(bean, json, parameters);
				if(parameters!=null)
					isJson=true;
			}else{
				try{
					parameters = (HashMap<String, Object>)new JsonReader2Map().mapping(bean, json, parameters);
					if(parameters!=null)
						isJson=true;
				}catch(Exception e){
				}		
			}

			if(!isJson){
				if(json.charAt(0)=='{' && json.charAt(json.length()-1)=='}') isJson=true;
				if(isJson){
					if(json.charAt(0)=='{' && json.length()>0) json=json.substring(1,json.length());
					if(json.charAt(json.length()-1)=='}' && json.length()>0) json=json.substring(0,json.length()-1);
					StringTokenizer st = new StringTokenizer(json,",");
					while(st.hasMoreTokens()){
						String pair = st.nextToken();
						StringTokenizer st1 = new StringTokenizer(pair,":");
						String key=null;
						String value=null;
						if(st1.hasMoreTokens()) key=st1.nextToken();
						if(st1.hasMoreTokens()) value=st1.nextToken();
						if(key!=null && value!=null){
							key=key.trim();
							if(key.charAt(0)=='"' && key.length()>0) key=key.substring(1,key.length());
							if(key.charAt(key.length()-1)=='"' && key.length()>0) key=key.substring(0,key.length()-1);
							value=value.trim();
							if(value.charAt(0)=='"' && value.length()>0) value=value.substring(1,value.length());
							if(value.charAt(value.length()-1)=='"' && value.length()>0) value=value.substring(0,value.length()-1);
							parameters.put(key, value);
						}
					}
				}
			}
		}catch(Exception e){
			new bsException(e, iStub.log_ERROR);
		}finally{
			try{
				in.close();
			}catch (Exception ex) {
			}
		}

		if(isJson){
			bean.initFromMap(parameters,false);
			if(dataBytes!=null)
				request.setAttribute(bsController.CONST_RECOVERED_REQUEST_CONTENT, dataBytes);
		}

		bean.initFromMap((HashMap<String,Object>)request.getAttribute(bsController.CONST_REST_URLMAPPEDPARAMETERS),true);		

		return isJson;
	}

	public static boolean initXmlPart(i_bean bean, HttpServletRequest request) throws bsControllerException{
		return initXmlPart(bean, request, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean initXmlPart(i_bean bean, HttpServletRequest request, XmlMapper mapper) throws bsControllerException{
		boolean isXml=false;
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		DataInputStream in = null;
		byte[] dataBytes = null;
		try{
			in = new DataInputStream(request.getInputStream());
			int formDataLength = request.getContentLength();

			dataBytes = new byte[formDataLength];
			int bytesRead = 0;
			int totalBytesRead = 0;
			while (totalBytesRead < formDataLength && totalBytesRead>-1) {
				bytesRead = in.read(dataBytes, totalBytesRead, formDataLength);
				totalBytesRead += bytesRead;
			}
			in.close();

			String xml = new String(dataBytes,0,dataBytes.length).trim();
			if(mapper!=null){
				parameters = (HashMap<String,Object>)mapper.mapping(bean, xml, parameters);
				isXml=true;
			}else{
				parameters = (HashMap<String,Object>)new XmlReader2Map().mapping(bean, xml, parameters);
				if(parameters!=null)
					isXml=true;
			}


		}catch(Exception e){
			new bsException(e, iStub.log_ERROR);
		}finally{
			try{
				in.close();
			}catch (Exception ex) {
			}
		}

		if(isXml){
			bean.initFromMap(parameters,false);
			if(dataBytes!=null)
				request.setAttribute(bsController.CONST_RECOVERED_REQUEST_CONTENT, dataBytes);

		}

		bean.initFromMap((HashMap)request.getAttribute(bsController.CONST_REST_URLMAPPEDPARAMETERS),true);		

		return isXml;
	}

	
//******************************************************************
	
	public static void init(Object bean, String prefix, HttpServletRequest request) throws bsControllerException{
		init(bean, prefix, request, null);
	}
	
	public static void init(Object bean, String prefix, HttpServletRequest request, i_bean instance) throws bsControllerException{
		if(request==null) return;

		if(request.getContentType()!=null && request.getContentType().indexOf("multipart")>-1){
			initMultiPart(bean,prefix,request);
			return;
		}
	
		if(request.getContentType()!=null && request.getContentType().toLowerCase().indexOf("application/json")>-1){
			if(initJsonPart(bean,prefix,request,(instance!=null)?instance.getJsonMapper():bsController.checkJsonMapper())) 
				return;
		}
		
		if(request.getContentType()!=null && request.getContentType().toLowerCase().indexOf("application/xml")>-1){
			if(initXmlPart(bean,prefix,request,(instance!=null)?instance.getXmlMapper():bsController.checkXmlMapper()))
				return;
		}
		
	
		initNormal(bean, prefix, request);
	
	}
	
	
	@SuppressWarnings("unchecked")
	public static void initMultiPart(Object bean, String prefix, HttpServletRequest request) throws bsControllerException{
		HashMap<String,Object> parameters = util_multipart.popolateHashMap(request);
		initPartFromMap(bean,prefix,parameters);
		initFromMap(bean, prefix, (HashMap<String,Object>)request.getAttribute(bsController.CONST_REST_URLMAPPEDPARAMETERS));
	}
	
	
	public static void initPartFromMap(Object bean, String prefix, HashMap<String,Object> parameters) throws bsControllerException{
		if(parameters==null) 
			parameters=new HashMap<String,Object>();
		initFromMap(bean,prefix,parameters);
	}
	
	@SuppressWarnings("unchecked")
	public static boolean initJsonPart(Object bean, String prefix, HttpServletRequest request, JsonMapper mapper) throws bsControllerException{
		boolean isJson=false;
		Map<String,Object> parameters = new HashMap<String,Object>();
		try{
			byte[] dataBytes = (byte[])request.getAttribute(bsController.CONST_RECOVERED_REQUEST_CONTENT);
			if(dataBytes!=null){
				String json = new String(dataBytes,0,dataBytes.length).trim();
				if(mapper!=null){
					parameters = (Map<String,Object>)mapper.mapping(null, json, parameters);
					if(parameters!=null)
						isJson=true;
				}else{
					try{
						parameters = (Map<String,Object>)new JsonReader2Map().mapping(null, json, parameters);
						if(parameters!=null)
							isJson=true;
					}catch(Exception e){
					}		
				}
				if(!isJson){
					if(json.charAt(0)=='{' && json.charAt(json.length()-1)=='}') isJson=true;
					if(isJson){
						if(json.charAt(0)=='{' && json.length()>0) json=json.substring(1,json.length());
						if(json.charAt(json.length()-1)=='}' && json.length()>0) json=json.substring(0,json.length()-1);
						StringTokenizer st = new StringTokenizer(json,",");
						while(st.hasMoreTokens()){
							String pair = st.nextToken();
							StringTokenizer st1 = new StringTokenizer(pair,":");
							String key=null;
							String value=null;
							if(st1.hasMoreTokens()) key=st1.nextToken();
							if(st1.hasMoreTokens()) value=st1.nextToken();
							if(key!=null && value!=null){
								key=key.trim();
								if(key.charAt(0)=='"' && key.length()>0) key=key.substring(1,key.length());
								if(key.charAt(key.length()-1)=='"' && key.length()>0) key=key.substring(0,key.length()-1);
								value=value.trim();
								if(value.charAt(0)=='"' && value.length()>0) value=value.substring(1,value.length());
								if(value.charAt(value.length()-1)=='"' && value.length()>0) value=value.substring(0,value.length()-1);
								parameters.put(key, value);
							}
						}
					}
				}
			}


		}catch(Exception e){

		}finally{
		}

		if(isJson)
			initFromMap(bean,prefix,parameters);

		initFromMap(bean,prefix,(Map<String,Object>)request.getAttribute(bsController.CONST_REST_URLMAPPEDPARAMETERS));		

		return isJson;
	}

	@SuppressWarnings("unchecked")
	public static boolean initXmlPart(Object bean, String prefix, HttpServletRequest request, XmlMapper mapper) throws bsControllerException{
		boolean isXml=false;
		Map<String,Object> parameters = new HashMap<String,Object>();
		try{

			byte[] dataBytes = (byte[])request.getAttribute(bsController.CONST_RECOVERED_REQUEST_CONTENT);
			if(dataBytes!=null){
				String xml = new String(dataBytes,0,dataBytes.length).trim();
				if(mapper!=null){
					parameters = (Map<String,Object>)mapper.mapping(null, xml, parameters);
					isXml=true;
				}else{
					parameters = (Map<String,Object>)new XmlReader2Map().mapping(null, xml, parameters);
					if(parameters!=null)
						isXml=true;
				}
			}

		}catch(Exception e){

		}finally{
		}

		if(isXml)
			initFromMap(bean,prefix,parameters);

		initFromMap(bean,prefix,(Map<String,Object>)request.getAttribute(bsController.CONST_REST_URLMAPPEDPARAMETERS));		

		return isXml;
	}

	
	
	@SuppressWarnings("rawtypes")
	public static void initNormal(Object bean, String prefix, HttpServletRequest request) throws bsControllerException{

		boolean inputBase64 = (request.getParameter(bsController.CONST_ID_INPUTBASE64)!=null &&
				(
						request.getParameter(bsController.CONST_ID_INPUTBASE64).equalsIgnoreCase("true") ||
						request.getParameter(bsController.CONST_ID_INPUTBASE64).equalsIgnoreCase(util_base64.encode("true".getBytes()))
				)
			);
		
		

		Enumeration<?> en = request.getParameterNames();
		while(en.hasMoreElements()){
			String key = (String)en.nextElement();
			if(key.indexOf(prefix+".")==0){
				String value = request.getParameter(key);
				String format = request.getParameter("$format_"+key);
				String replaceOnBlank = request.getParameter("$replaceOnBlank_"+key);
				String replaceOnErrorFormat = request.getParameter("$replaceOnErrorFormat_"+key);
	
				if(inputBase64){
					String charset = (request.getCharacterEncoding()==null || request.getCharacterEncoding().equals(""))?"UTF-8":request.getCharacterEncoding();
					try{
						if(value!=null) value=new String(util_base64.decode(value),charset);
					}catch(Exception e){}
					try{
						if(format!=null) format=new String(util_base64.decode(format),charset);
					}catch(Exception e){}
					try{
						if(replaceOnBlank!=null) replaceOnBlank=new String(util_base64.decode(replaceOnBlank),charset);
					}catch(Exception e){}
					try{
						if(replaceOnErrorFormat!=null) replaceOnErrorFormat=new String(util_base64.decode(replaceOnErrorFormat),charset);
					}catch(Exception e){
					}
				}
	
				if(key.indexOf(".")==-1){
					try{
						Object makedValue=null;
						if(format!=null){
								makedValue=util_makeValue.makeFormatedValue1(bean,format,value,key,replaceOnBlank,replaceOnErrorFormat);
						}else{
							makedValue=util_makeValue.makeValue1(bean,value,key);
						}
						if(!setCampoValueWithPoint(bean,key,makedValue))
							throw new Exception();
					}catch(Exception e){
						try{
							Object makedValue=null;
							if(format!=null){
								makedValue=util_makeValue.makeFormatedValue(bean,format,value,getCampoValue(bean,key),replaceOnBlank,replaceOnErrorFormat);
							}else makedValue=util_makeValue.makeValue(value,getCampoValue(bean,key));
							if(!setCampoValueWithPoint(bean,key,makedValue))
								throw new Exception();
						}catch(Exception ex){
						}
					}
				}else{
	
					Object writeValue=null;
					Object current_requested = bean;
	
					String last_field_name = null;
					StringTokenizer st = new StringTokenizer(key,".");
					while(st.hasMoreTokens()){
						if(st.countTokens()>1){
							String current_field_name = st.nextToken();
							try{
								if(writeValue==null && current_requested instanceof Map) writeValue = ((Map)current_requested).get(current_field_name);
								if(writeValue==null && current_requested instanceof List) writeValue = ((List)current_requested).get(Integer.valueOf(current_field_name).intValue());
								if(writeValue==null && current_requested instanceof Set) writeValue = Arrays.asList(((Set)current_requested)).toArray()[Integer.valueOf(current_field_name).intValue()];
								if(writeValue==null && current_requested.getClass().isArray()){
									Class<?> componentType = current_requested.getClass().getComponentType();
									if(!componentType.isPrimitive())
										writeValue = ((Object[])current_requested)[Integer.valueOf(current_field_name).intValue()];
									else{
										if (boolean.class.isAssignableFrom(componentType))
											writeValue = ((boolean[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
										else if (byte.class.isAssignableFrom(componentType))
											writeValue = ((byte[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
										else if (char.class.isAssignableFrom(componentType))
											writeValue = ((char[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
										else if (double.class.isAssignableFrom(componentType))
											writeValue = ((double[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
										else if (float.class.isAssignableFrom(componentType))
											writeValue = ((float[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
										else if (int.class.isAssignableFrom(componentType))
											writeValue = ((int[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
										else if (long.class.isAssignableFrom(componentType))
											writeValue = ((long[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
										else if (short.class.isAssignableFrom(componentType))
											writeValue = ((short[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
									}
								}

								if(writeValue==null) writeValue = util_reflect.getValue(current_requested,"get"+util_reflect.adaptMethodName(current_field_name),null);
								if(writeValue==null) writeValue = util_reflect.getValue(current_requested,current_field_name,null);
								if(writeValue==null && current_requested instanceof i_bean) writeValue = ((i_bean)current_requested).get(current_field_name);
							}catch(Exception e){
							}
							current_requested = writeValue;
						}else{
							last_field_name = st.nextToken();
						}
						writeValue = null;
					}
	
					if(current_requested!=null){
						try{
							if(format!=null)
								setCampoValuePoint(
										current_requested,
										last_field_name,
										util_makeValue.makeFormatedValue1(
													current_requested,
													format,
													value,
													last_field_name,
													replaceOnBlank,
													replaceOnErrorFormat
										),
										false
								);
							else setCampoValuePoint(
										current_requested,
										last_field_name,
										util_makeValue.makeValue1(current_requested,value,last_field_name),
										false
									);
						}catch(Exception e){
							try{
								if(format!=null)
									setCampoValuePoint(current_requested,key,util_makeValue.makeFormatedValue(bean,format,value,getCampoValue(bean,key),replaceOnBlank,replaceOnErrorFormat),false);
								else setCampoValuePoint(current_requested,key,util_makeValue.makeValue(value,getCampoValue(bean,key)),false);
							}catch(Exception ex){
							}
						}
					}
				}
			}
		}
		
	}	
	
	
	
	
	
	@SuppressWarnings("rawtypes")
	public static void initFromMap(Object bean, String prefix, Map<String,Object> parameters) throws bsControllerException{

		if(parameters==null)
			return;
		boolean inputBase64 = (parameters.get(bsController.CONST_ID_INPUTBASE64)!=null &&
				(
						parameters.get(bsController.CONST_ID_INPUTBASE64).toString().equalsIgnoreCase("true") ||
						parameters.get(bsController.CONST_ID_INPUTBASE64).toString().equalsIgnoreCase(util_base64.encode("true".getBytes()))
				)
			);



		
		for (Object elem : parameters.keySet()) {
			String key = (String)elem;

			if(key.indexOf(prefix+".")==0 && parameters.get(key) instanceof String ){
				String value = (String)parameters.get(key);
				String format = (String)parameters.get("$format_"+key);
				String replaceOnBlank = (String)parameters.get("$replaceOnBlank_"+key);
				String replaceOnErrorFormat = (String)parameters.get("$replaceOnErrorFormat_"+key);
				
				key = key.substring((prefix+".").length(),key.length());

				if(inputBase64){
					String charset = (parameters.get("$REQUEST_CHARSET")==null || parameters.get("$REQUEST_CHARSET").toString().equals(""))?"UTF-8":parameters.get("$REQUEST_CHARSET").toString();

					try{
						if(value!=null) value=new String(util_base64.decode(value),charset);
					}catch(Exception e){}
					try{
						if(format!=null) format=new String(util_base64.decode(format),charset);
					}catch(Exception e){}
					try{
						if(replaceOnBlank!=null) replaceOnBlank=new String(util_base64.decode(replaceOnBlank),charset);
					}catch(Exception e){}
					try{
						if(replaceOnErrorFormat!=null) replaceOnErrorFormat=new String(util_base64.decode(replaceOnErrorFormat),charset);
					}catch(Exception e){
					}
				}


				if(key.indexOf(".")==-1){
					try{
						Object makedValue=null;
						if(format!=null){
							makedValue=util_makeValue.makeFormatedValue1(bean,format,value,key,replaceOnBlank,replaceOnErrorFormat);
						}else{
							Object current_requested = bean;

							Serialized serialized_annotation = null;
							Object checkForDes = parameters.get(JsonMapper.CONST_ID_CHECKFORDESERIALIZE);								
							if(checkForDes!=null && checkForDes.toString().equalsIgnoreCase("true")){
								Method ret_method = util_reflect.getSetMethod(current_requested, key);
								if(ret_method!=null)
									serialized_annotation =ret_method.getAnnotation(Serialized.class);
								if(serialized_annotation==null){
									Field ret_field = util_reflect.getField(current_requested, key);
									if(ret_field!=null)
										serialized_annotation =ret_field.getAnnotation(Serialized.class);
								}
							}
							if(	serialized_annotation!=null &&
								serialized_annotation.input()!=null &&
								serialized_annotation.input().format()!=null &&
								!serialized_annotation.input().format().equals(""))
								setCampoValuePoint(
										current_requested,
										key,
										util_makeValue.makeFormatedValue1(
													current_requested,
													serialized_annotation.input().format(),
													value,
													key,
													serialized_annotation.input().replaceOnBlank(),
													serialized_annotation.input().replaceOnErrorFormat()
													
										),
										false
								);
							else{	
									makedValue=util_makeValue.makeValue1(bean,value,key);
								
									if(!setCampoValueWithPoint(bean,key,makedValue))
										throw new Exception();
							}
							
							
							
							
//							makedValue=util_makeValue.makeValue1(bean,value,key);
						}
//						if(!setCampoValueWithPoint(bean,key,makedValue))
//							throw new Exception();
					}catch(Exception e){
						try{
							Object makedValue=null;

							if(format!=null){
								makedValue=util_makeValue.makeFormatedValue(bean,format,value,getCampoValue(bean,key),replaceOnBlank,replaceOnErrorFormat);
							}else makedValue=util_makeValue.makeValue(value,getCampoValue(bean,key));

							if(!setCampoValueWithPoint(bean,key,makedValue))
								throw new Exception();
						}catch(Exception ex){

						}
					}

				}else{
					Object writeValue=null;
					Object current_requested = bean;

					String last_field_name = null;
					StringTokenizer st = new StringTokenizer(key,".");
					while(st.hasMoreTokens()){
						if(st.countTokens()>1){
							String current_field_name = st.nextToken();
							try{
								writeValue = util_reflect.getValue(current_requested,"get"+util_reflect.adaptMethodName(current_field_name),null);
								if(writeValue==null) writeValue = util_reflect.getValue(current_requested,current_field_name,null);
								if(writeValue==null && current_requested instanceof i_bean) writeValue = ((i_bean)current_requested).get(current_field_name);
								if(writeValue==null && current_requested instanceof Map) writeValue = ((Map)current_requested).get(current_field_name);
								if(writeValue==null && current_requested instanceof List) writeValue = ((List)current_requested).get(Integer.valueOf(current_field_name).intValue());
								if(writeValue==null && current_requested instanceof Set) writeValue = Arrays.asList(((Set)current_requested)).toArray()[Integer.valueOf(current_field_name).intValue()];
								if(writeValue==null && current_requested.getClass().isArray()){
									Class<?> componentType = current_requested.getClass().getComponentType();
									if(!componentType.isPrimitive())
										writeValue = ((Object[])current_requested)[Integer.valueOf(current_field_name).intValue()];
									else{
										if (boolean.class.isAssignableFrom(componentType))
											writeValue = ((boolean[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
										else if (byte.class.isAssignableFrom(componentType))
											writeValue = ((byte[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
										else if (char.class.isAssignableFrom(componentType))
											writeValue = ((char[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
										else if (double.class.isAssignableFrom(componentType))
											writeValue = ((double[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
										else if (float.class.isAssignableFrom(componentType))
											writeValue = ((float[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
										else if (int.class.isAssignableFrom(componentType))
											writeValue = ((int[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
										else if (long.class.isAssignableFrom(componentType))
											writeValue = ((long[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
										else if (short.class.isAssignableFrom(componentType))
											writeValue = ((short[])current_requested)[Integer.valueOf(current_field_name).intValue()]; 
									}
								}

								if(writeValue==null)
									writeValue = util_reflect.getValueIfIsInputAnnotation(current_requested, current_field_name, null);
							}catch(Exception e){
							}
							current_requested = writeValue;
						}else{
							last_field_name = st.nextToken();
						}
						writeValue = null;
					}

					if(current_requested!=null){
						try{
							if(format!=null)
								setCampoValuePoint(
										current_requested,
										last_field_name,
										util_makeValue.makeFormatedValue1(
													current_requested,
													format,
													value,
													last_field_name,
													replaceOnBlank,
													replaceOnErrorFormat
										),
										false
								);
							else{
								Serialized serialized_annotation = null;
								Object checkForDes = parameters.get(JsonMapper.CONST_ID_CHECKFORDESERIALIZE);								
								if(checkForDes!=null && checkForDes.toString().equalsIgnoreCase("true")){
									Method ret_method = util_reflect.getSetMethod(current_requested, last_field_name);
									if(ret_method!=null)
										serialized_annotation =ret_method.getAnnotation(Serialized.class);
									if(serialized_annotation==null){
										Field ret_field = util_reflect.getField(current_requested, last_field_name);
										if(ret_field!=null)
											serialized_annotation =ret_field.getAnnotation(Serialized.class);
									}
								}
								if(	serialized_annotation!=null &&
									serialized_annotation.input()!=null &&
									serialized_annotation.input().format()!=null &&
									!serialized_annotation.input().format().equals(""))
									setCampoValuePoint(
											current_requested,
											last_field_name,
											util_makeValue.makeFormatedValue1(
														current_requested,
														serialized_annotation.input().format(),
														value,
														last_field_name,
														serialized_annotation.input().replaceOnBlank(),
														serialized_annotation.input().replaceOnErrorFormat()
											),
											false
									);
								else	
									setCampoValuePoint(
											current_requested,
											last_field_name,
											util_makeValue.makeValue1(current_requested,value,last_field_name),
											false
										);
								
							}
						}catch(Exception e){
							try{
								if(format!=null)
									setCampoValuePoint(current_requested,key,util_makeValue.makeFormatedValue(bean,format,value,getCampoValue(bean,key),replaceOnBlank,replaceOnErrorFormat),false);
								else setCampoValuePoint(current_requested,key,util_makeValue.makeValue(value,getCampoValue(bean,key)),false);
							}catch(Exception ex){
							}
						}
					}


				}
			}
		}

	}
	
	

//******************************************************************
	
		public static Object assignPrimitiveDefault(Class<?> current){
			if (boolean.class.isAssignableFrom(current))
				return false; 
			else if (byte.class.isAssignableFrom(current))
				return (byte)0; 
			else if (char.class.isAssignableFrom(current))
				return (char)' ';
			else if (double.class.isAssignableFrom(current))
				return (double)0;
			else if (float.class.isAssignableFrom(current))
				return (float)0;
			else if (int.class.isAssignableFrom(current))
				return (int)0;
			else if (long.class.isAssignableFrom(current))
				return (long)0;
			else if (short.class.isAssignableFrom(current))
				return (short)0;
			return null;
		}
	
		public static Object init(Class<?> ret_class, String name, HttpServletRequest request) throws bsControllerException{
			return init(ret_class, name, request, null);
		}
		
		public static Object init(Class<?> ret_class, String name, HttpServletRequest request, i_bean instance) throws bsControllerException{
			if(request==null) return 
				null;

			if(request.getContentType()!=null && request.getContentType().indexOf("multipart")>-1){
				return 
					initMultiPart(ret_class,name,request);
			}
		
			if(request.getContentType()!=null && request.getContentType().toLowerCase().indexOf("application/json")>-1){
				return 
					initJsonPart(ret_class,name,request,(instance!=null)?instance.getJsonMapper():bsController.checkJsonMapper());
			}
			
			if(request.getContentType()!=null && request.getContentType().toLowerCase().indexOf("application/xml")>-1){
				return 
					initXmlPart(ret_class,name,request,(instance!=null)?instance.getXmlMapper():bsController.checkXmlMapper());
			}
			
		
			return 
				initNormal(ret_class,name, request);
		
		}
		
		
		@SuppressWarnings("unchecked")
		public static Object initMultiPart(Class<?> ret_class, String name, HttpServletRequest request) throws bsControllerException{
			HashMap<String,Object> parameters = util_multipart.popolateHashMap(request);
			Object result = initPartFromMap(ret_class,name,parameters);
			if(result==null)
				return initFromMap(ret_class, name, (HashMap<String,Object>)request.getAttribute(bsController.CONST_REST_URLMAPPEDPARAMETERS));
			else return result;
		}
		
		
		public static Object initPartFromMap(Class<?> ret_class, String name, HashMap<String,Object> parameters) throws bsControllerException{
			if(parameters==null) 
				parameters=new HashMap<String,Object>();
			return 
					initFromMap(ret_class,name,parameters);
		}
		
		@SuppressWarnings("unchecked")
		public static Object initJsonPart(Class<?> ret_class, String name, HttpServletRequest request, JsonMapper mapper) throws bsControllerException{
			Object result = null;
			boolean isJson=false;
			HashMap<String,Object> parameters = new HashMap<String,Object>();
			try{
				byte[] dataBytes = (byte[])request.getAttribute(bsController.CONST_RECOVERED_REQUEST_CONTENT);
				if(dataBytes!=null){
					String json = new String(dataBytes,0,dataBytes.length).trim();
					if(mapper!=null){
						parameters = (HashMap<String,Object>)mapper.mapping(null, json, parameters);
						if(parameters!=null)
							isJson=true;
					}else{
						try{
							parameters = (HashMap<String,Object>)new JsonReader2Map().mapping(null, json, parameters);
							if(parameters!=null)
								isJson=true;
						}catch(Exception e){
						}		
					}
					if(!isJson){
						if(json.charAt(0)=='{' && json.charAt(json.length()-1)=='}') isJson=true;
						if(isJson){
							if(json.charAt(0)=='{' && json.length()>0) json=json.substring(1,json.length());
							if(json.charAt(json.length()-1)=='}' && json.length()>0) json=json.substring(0,json.length()-1);
							StringTokenizer st = new StringTokenizer(json,",");
							while(st.hasMoreTokens()){
								String pair = st.nextToken();
								StringTokenizer st1 = new StringTokenizer(pair,":");
								String key=null;
								String value=null;
								if(st1.hasMoreTokens()) key=st1.nextToken();
								if(st1.hasMoreTokens()) value=st1.nextToken();
								if(key!=null && value!=null){
									key=key.trim();
									if(key.charAt(0)=='"' && key.length()>0) key=key.substring(1,key.length());
									if(key.charAt(key.length()-1)=='"' && key.length()>0) key=key.substring(0,key.length()-1);
									value=value.trim();
									if(value.charAt(0)=='"' && value.length()>0) value=value.substring(1,value.length());
									if(value.charAt(value.length()-1)=='"' && value.length()>0) value=value.substring(0,value.length()-1);
									parameters.put(key, value);
								}
							}
						}
					}
				}


			}catch(Exception e){

			}finally{
			}

			if(isJson)
				result = initFromMap(ret_class,name,parameters);

			if(result==null)
				result = initFromMap(ret_class,name,(HashMap<String,Object>)request.getAttribute(bsController.CONST_REST_URLMAPPEDPARAMETERS));		

			return result;
		}

		@SuppressWarnings("unchecked")
		public static Object initXmlPart(Class<?> ret_class, String name, HttpServletRequest request, XmlMapper mapper) throws bsControllerException{
			Object result = null;
			boolean isXml=false;
			HashMap<String,Object> parameters = new HashMap<String,Object>();
			try{

				byte[] dataBytes = (byte[])request.getAttribute(bsController.CONST_RECOVERED_REQUEST_CONTENT);
				if(dataBytes!=null){
					String xml = new String(dataBytes,0,dataBytes.length).trim();
					if(mapper!=null){
						parameters = (HashMap<String,Object>)mapper.mapping(null, xml, parameters);
						isXml=true;
					}else{
						parameters = (HashMap<String,Object>)new XmlReader2Map().mapping(null, xml, parameters);
						if(parameters!=null)
							isXml=true;
					}
				}

			}catch(Exception e){

			}finally{
			}

			if(isXml)
				result = initFromMap(ret_class,name,parameters);

			if(result==null)
				result = initFromMap(ret_class,name,(HashMap<String,Object>)request.getAttribute(bsController.CONST_REST_URLMAPPEDPARAMETERS));		

			return result;
		}

		
		
		public static Object initNormal(Class<?> ret_class, String name, HttpServletRequest request) throws bsControllerException{

			boolean inputBase64 = (request.getParameter(bsController.CONST_ID_INPUTBASE64)!=null &&
					(
							request.getParameter(bsController.CONST_ID_INPUTBASE64).equalsIgnoreCase("true") ||
							request.getParameter(bsController.CONST_ID_INPUTBASE64).equalsIgnoreCase(util_base64.encode("true".getBytes()))
					)
				);
			
			

			Enumeration<?> en = request.getParameterNames();
			while(en.hasMoreElements()){
				String key = (String)en.nextElement();
				if(name!=null && key.equals(name)){
					String value = request.getParameter(key);
					String format = request.getParameter("$format_"+key);
					String replaceOnBlank = request.getParameter("$replaceOnBlank_"+key);
					String replaceOnErrorFormat = request.getParameter("$replaceOnErrorFormat_"+key);
		
					if(inputBase64){
						String charset = (request.getCharacterEncoding()==null || request.getCharacterEncoding().equals(""))?"UTF-8":request.getCharacterEncoding();
						try{
							if(value!=null) value=new String(util_base64.decode(value),charset);
						}catch(Exception e){}
						try{
							if(format!=null) format=new String(util_base64.decode(format),charset);
						}catch(Exception e){}
						try{
							if(replaceOnBlank!=null) replaceOnBlank=new String(util_base64.decode(replaceOnBlank),charset);
						}catch(Exception e){}
						try{
							if(replaceOnErrorFormat!=null) replaceOnErrorFormat=new String(util_base64.decode(replaceOnErrorFormat),charset);
						}catch(Exception e){
						}
					}
		

						try{
							Object makedValue=util_makeValue.makeFormatedValue1(ret_class,value,format);	
							return makedValue;
						}catch(Exception e){
						}

				}
			}
			return null;
			
		}	
		
		
		
		
		
		public static Object initFromMap(Class<?> ret_class, String name, Map<String,Object> parameters) throws bsControllerException{

			if(parameters==null)
				return null;
			boolean inputBase64 = (parameters.get(bsController.CONST_ID_INPUTBASE64)!=null &&
					(
							parameters.get(bsController.CONST_ID_INPUTBASE64).toString().equalsIgnoreCase("true") ||
							parameters.get(bsController.CONST_ID_INPUTBASE64).toString().equalsIgnoreCase(util_base64.encode("true".getBytes()))
					)
				);



			
			for (Object elem : parameters.keySet()) {
				String key = (String)elem;

				if(name!=null && key.equals(name)){
					String value =parameters.get(key).toString();
					String format = (String)parameters.get("$format_"+key);
					String replaceOnBlank = (String)parameters.get("$replaceOnBlank_"+key);
					String replaceOnErrorFormat = (String)parameters.get("$replaceOnErrorFormat_"+key);
					


					if(inputBase64){
						String charset = (parameters.get("$REQUEST_CHARSET")==null || parameters.get("$REQUEST_CHARSET").toString().equals(""))?"UTF-8":parameters.get("$REQUEST_CHARSET").toString();

						try{
							if(value!=null) value=new String(util_base64.decode(value),charset);
						}catch(Exception e){}
						try{
							if(format!=null) format=new String(util_base64.decode(format),charset);
						}catch(Exception e){}
						try{
							if(replaceOnBlank!=null) replaceOnBlank=new String(util_base64.decode(replaceOnBlank),charset);
						}catch(Exception e){}
						try{
							if(replaceOnErrorFormat!=null) replaceOnErrorFormat=new String(util_base64.decode(replaceOnErrorFormat),charset);
						}catch(Exception e){
						}
					}


					try{
						if(format!=null){
							Object makedValue=util_makeValue.makeFormatedValue1(ret_class,value,format);	
							return makedValue;
						}else{
							Serialized serialized_annotation = null;
							Object checkForDes = parameters.get(JsonMapper.CONST_ID_CHECKFORDESERIALIZE);								
							if(checkForDes!=null && checkForDes.toString().equalsIgnoreCase("true"))
								serialized_annotation = (Serialized)ret_class.getAnnotation(Serialized.class);
							
							
							Object makedValue=util_makeValue.makeFormatedValue1(
									ret_class,
									value,
									(serialized_annotation!=null &&
									serialized_annotation.input()!=null &&
									serialized_annotation.input().format()!=null &&
									!serialized_annotation.input().format().equals(""))
									?
										serialized_annotation.input().format()
									:
										format);	
							return makedValue;
							

						}
					}catch(Exception e){
						return null;
					}
				}
			}
			return null;
		}	
	
//******************************************************************
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void setCampoValuePoint(Object req, String nome, Object value, boolean log) throws Exception{
		if(req instanceof Map){
			((Map)req).put(nome, value);
		}else if(req instanceof List) {	
			int position = -1;
			try {
				position = Integer.parseInt(nome); 
				if(position>-1) {
					if(((List)req).size()<=position) {
						int diff = position-((List)req).size()+1;
						for(int i=0;i<diff;i++)
							((List)req).add(null);
					}
					((List)req).set(position, value);
				}
			}catch (Exception e) {
			}
		}else{
			boolean res = setValue(req, "set"+util_reflect.adaptMethodName(nome.trim()),new Object[]{value},log);
			if(!res)
				res = setValueMapped(req, "set", nome.trim(),new Object[]{value},false);
		}
	}
	
	public static boolean setCampoValueWithPoint(Object req, String name, Object value) throws Exception{
		try{
			if(name.indexOf('.')==-1){
				try{
					boolean res = setValue(req, "set"+util_reflect.adaptMethodName(name.trim()),new Object[]{value},false);
					if(!res)
						res = setValueMapped(req, "set", name.trim(),new Object[]{value},false);
					return res;
				}catch(Exception e){
					return false;
				}
				
			}else{
				StringTokenizer st = new StringTokenizer(name,".");
				String current_field_name = null;
				String complexName="";
				while(st.hasMoreTokens()){
					String token = st.nextToken();
					if(st.countTokens()>0) complexName+=token;
					if(st.countTokens()>1) complexName+=".";
					if(st.countTokens()==0) current_field_name=token;
				}
				Object writeObj=get(req,complexName);
				if(writeObj==null) writeObj=get(req,complexName);
				if(writeObj==null) return false;

				if(writeObj!=null && current_field_name!=null){
					try{
							setCampoValuePoint(
									writeObj,
									current_field_name,
									value,
									false
								);
							return true;
					}catch(Exception e){
						return false;
					}
				}
				return false;
			}
		}catch(Exception e){
			throw e;
		}
//		return true;
	}	
	
	
	public static boolean setValueMapped(Object requested, String prefix, String nome, Object[] value, boolean log) throws Exception{
		String mapName = null;
		if(mapName==null){
			final String fkey = nome;
			Field[] alldf = util_reflect.getAllDeclaredFields(
					requested.getClass(),
					new Comparable<Object>() {
						public int compareTo(Object field) {
							if(field instanceof Field){
								Serialized annotation = ((Field)field).getAnnotation(Serialized.class);
								if(annotation!=null && annotation.input()!=null && annotation.input().name()!=null && annotation.input().name().equals(fkey))
									return 0;
							}
								return -1;
						}
					}
					);
			for(Field field: alldf){
				mapName = prefix + util_reflect.adaptMethodName(field.getName());
				break;
			}
		}
		if(mapName==null){
			final String fkey = nome;
			Method[] alldm = util_reflect.getAllDeclaredMethods(
					requested.getClass(),
					new Comparable<Object>() {
						public int compareTo(Object method) {
							if(method instanceof Method){
								Serialized annotation = ((Method)method).getAnnotation(Serialized.class);
								if(annotation!=null && annotation.input()!=null && annotation.input().name()!=null && annotation.input().name().equals(fkey))
									return 0;
							}
								return -1;
						}
					}
					);
			for(Method method: alldm){
				mapName =  method.getName();
				break;
			}
			
		}
		if(mapName==null)
			return false;
		return util_reflect.setValue(requested, mapName, value,log);
	}
	
	public static boolean setValue(Object requested, String nome, Object[] value) throws Exception{
		return util_reflect.setValue(requested, nome, value);
	}
	public static boolean setValue(Object requested, String nome, Object[] value, boolean log) throws Exception{
		return util_reflect.setValue(requested, nome, value,log);
	}
	
	public static Object get(Object requested,String value) {
		try{
			Object result = null;
			if(value.indexOf('.')>-1)
				result = util_reflect.prepareWriteValueForTag(requested,"get",value,null);
			else result = getCampoValue(requested,value);
			if(result!=null) return result;
			return null;
		}catch(Exception e){

			return null;
		}
	}
	
	public static Object getValue(Object requested, String nome, Object[] value) throws Exception{
		return util_reflect.getValue(requested,nome,value);
	}
	
	public static Object getCampoValue(Object req, String nome){
		try{
			Object[] par = new Object[0];
			Object resultObject = getValue(req,"get"+util_reflect.adaptMethodName(nome.trim()),par);
			if(resultObject==null) resultObject = getValue(req,"is"+util_reflect.adaptMethodName(nome.trim()),par);
			return resultObject;
		}catch(Exception e){
			try{
				Object[] par = new Object[0];
				Object resultObject = getValue(req,"is"+util_reflect.adaptMethodName(nome.trim()),par);
				return resultObject;
			}catch(Exception ex){
				return null;		
			}
		
		}
	}
	

}
