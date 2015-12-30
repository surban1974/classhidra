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



import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.info_navigation;
import it.classhidra.core.tool.exception.bsControllerException;

import java.io.DataInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



public class util_supportbean  {


public static HashMap request2map(HttpServletRequest request){
	HashMap result = new HashMap();
	Enumeration en = request.getParameterNames();
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
	HashMap parameters = util_multipart.popolateHashMap(request);
	result.putAll(parameters);
	
	return result;
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
		String id_prev = request.getParameter(bsConstants.CONST_BEAN_$NAVIGATION);
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

	initNormal(bean, request);

}

	public static void initNormal(i_bean bean,HttpServletRequest request) throws bsControllerException{
		bean.setXmloutput(false);
		bean.setJsonoutput(false);
		boolean inputBase64 = (request.getParameter(bsController.CONST_ID_INPUTBASE64)!=null &&
				(
						request.getParameter(bsController.CONST_ID_INPUTBASE64).equalsIgnoreCase("true") ||
						request.getParameter(bsController.CONST_ID_INPUTBASE64).equalsIgnoreCase(new BASE64Encoder().encode("true".getBytes()))
				)
			);

		Enumeration en = request.getParameterNames();
		while(en.hasMoreElements()){
			String key = (String)en.nextElement();
			String value = request.getParameter(key);
			String format = request.getParameter("$format_"+key);
			String replaceOnBlank = request.getParameter("$replaceOnBlank_"+key);
			String replaceOnErrorFormat = request.getParameter("$replaceOnErrorFormat_"+key);

			if(inputBase64){
				String charset = (request.getCharacterEncoding()==null || request.getCharacterEncoding().equals(""))?"UTF-8":request.getCharacterEncoding();
				try{
					if(value!=null) value=new String(new BASE64Decoder().decodeBuffer(value),charset);
				}catch(Exception e){}
				try{
					if(format!=null) format=new String(new BASE64Decoder().decodeBuffer(format),charset);
				}catch(Exception e){}
				try{
					if(replaceOnBlank!=null) replaceOnBlank=new String(new BASE64Decoder().decodeBuffer(replaceOnBlank),charset);
				}catch(Exception e){}
				try{
					if(replaceOnErrorFormat!=null) replaceOnErrorFormat=new String(new BASE64Decoder().decodeBuffer(replaceOnErrorFormat),charset);
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
						if(bean.getParametersFly()==null) bean.setParametersFly(new HashMap());
						if(key!=null && key.length()>0 && key.indexOf(0)!='$') bean.getParametersFly().put(key, value);
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
									)
							);
						else bean.setCampoValuePoint(
									current_requested,
									last_field_name,
									util_makeValue.makeValue1(current_requested,value,last_field_name)
								);
					}catch(Exception e){
						try{
							if(format!=null)
								bean.setCampoValuePoint(current_requested,key,util_makeValue.makeFormatedValue((bean.getDelegated()==null)?bean:bean.getDelegated(),format,value,bean.getCampoValue(key),replaceOnBlank,replaceOnErrorFormat));
							else bean.setCampoValuePoint(current_requested,key,util_makeValue.makeValue(value,bean.getCampoValue(key)));
						}catch(Exception ex){
						}
					}
				}


			}
		}
	}


	public static void initMultiPart(i_bean bean, HttpServletRequest request) throws bsControllerException{
		HashMap parameters = util_multipart.popolateHashMap(request);
		bean.initPartFromMap(parameters);
	}

	public static boolean initJsonPart(i_bean bean, HttpServletRequest request) throws bsControllerException{
		boolean isJson=false;
		HashMap parameters = new HashMap();
		DataInputStream in = null;
		try{
			in = new DataInputStream(request.getInputStream());
			int formDataLength = request.getContentLength();

			byte dataBytes[] = new byte[formDataLength];
			int bytesRead = 0;
			int totalBytesRead = 0;
			while (totalBytesRead < formDataLength && totalBytesRead>-1) {
				bytesRead = in.read(dataBytes, totalBytesRead, formDataLength);
				totalBytesRead += bytesRead;
			}

			String json = new String(dataBytes,0,dataBytes.length).trim();
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


		}catch(Exception e){

		}finally{
			try{
				in.close();
			}catch (Exception ex) {
			}
		}

		if(isJson) bean.initPartFromMap(parameters);

		return isJson;
	}


}
