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



import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.elements.elementBeanBase;
import it.classhidra.core.tool.elements.i_elementDBBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_makeValue;
import it.classhidra.core.tool.util.util_multipart;
import it.classhidra.core.tool.util.util_reflect;

import java.io.DataInputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


import javax.servlet.http.HttpServletRequest;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


 
public class bean extends elementBeanBase implements i_bean  {
	private static final long serialVersionUID = 3917073481415967577L;

	private info_bean _infobean;
	protected info_action _infoaction;
	private auth_init current_auth;
	private HashMap parametersMP;
	private HashMap parametersFly;
	public String $id_returnPointOfService;
	public String middleAction;
	public String js4ajax="false";
	public boolean refresh=false;
	private boolean xmloutput=false;
	private String xmloutput_encoding="";
	private boolean jsonoutput=false;
	private String jsonoutput_encoding="";
	private boolean transformationoutput=false;
	private boolean binaryoutput=false;
	private String binaryoutput_encoding="";
	private boolean virtual=false;
	public int countActions;
	
	private listener_bean listener_b;
	
	private Object delegated;




public void reimposta(){
	if(parametersFly==null)
		parametersFly = new HashMap();
}

public redirects validate(HttpServletRequest request){
	return null;
}




public void init(HttpServletRequest request) throws bsControllerException{
	if(request==null) return;
	if(current_auth==null){
		try{
			current_auth = bsController.checkAuth_init(request);
		}catch(Exception ex){
		}
	}

	if(get_infoaction()!=null && get_infoaction().getReloadAfterNextNavigated()!=null && get_infoaction().getReloadAfterNextNavigated().equalsIgnoreCase("false")){
		String id_prev = request.getParameter(bsConstants.CONST_BEAN_$NAVIGATION);
		if(id_prev!=null && id_prev.indexOf(":")>-1){
			id_prev = id_prev.substring(0,id_prev.indexOf(":"));
			info_navigation nav = (info_navigation)request.getSession().getAttribute(bsConstants.CONST_BEAN_$NAVIGATION);
			if(nav!=null){
				int level_prev = nav.findLevel(id_prev, 0);
				int level_curr = nav.findLevel(get_infoaction().getPath(), 0);
				if(level_curr<level_prev && level_curr!=-1) return;
			}

		
		}

	}
	
	if(request.getContentType()!=null && request.getContentType().indexOf("multipart")>-1){
		initMultiPart(request);
		return;
	}
	
	if(request.getContentType()!=null && request.getContentType().toLowerCase().indexOf("application/json")>-1){
		if(initJsonPart(request)) return;
	}
	
	initNormal(request);

}

	public void initNormal(HttpServletRequest request) throws bsControllerException{
		xmloutput=false;
		jsonoutput=false;
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
						if(delegated!=null){
							makedValue=util_makeValue.makeFormatedValue1(delegated,format,value,key,replaceOnBlank,replaceOnErrorFormat);
							if(makedValue==null) makedValue=util_makeValue.makeFormatedValue1(this,format,value,key,replaceOnBlank,replaceOnErrorFormat);
						}else{
							makedValue=util_makeValue.makeFormatedValue1(this,format,value,key,replaceOnBlank,replaceOnErrorFormat);
						}
					}else{
						if(delegated!=null){
							makedValue=util_makeValue.makeValue1(delegated,value,key);
							if(makedValue==null) makedValue=util_makeValue.makeValue1(this,value,key);
						}else{
							makedValue=util_makeValue.makeValue1(this,value,key);
						}
					}
					setCampoValueWithPoint(key,makedValue);
				}catch(Exception e){
					try{
						Object makedValue=null;
						if(format!=null){
							if(delegated!=null){
								makedValue=util_makeValue.makeFormatedValue(delegated,format,value,getCampoValue(key),replaceOnBlank,replaceOnErrorFormat);
								if(makedValue==null) makedValue=util_makeValue.makeFormatedValue(this,format,value,getCampoValue(key),replaceOnBlank,replaceOnErrorFormat);
							}else{
								makedValue=util_makeValue.makeFormatedValue(this,format,value,getCampoValue(key),replaceOnBlank,replaceOnErrorFormat);
							}
						}else makedValue=util_makeValue.makeValue(value,getCampoValue(key));
						setCampoValueWithPoint(key,makedValue);
					}catch(Exception ex){
						if(parametersFly==null) parametersFly = new HashMap();
						if(key!=null && key.length()>0 && key.indexOf(0)!='$') parametersFly.put(key, value);
					}
				}
			}else{
				
				Object writeValue=null;
				Object current_requested = (delegated==null)?this:delegated;
				
				String last_field_name = null;
				StringTokenizer st = new StringTokenizer(key,".");
				while(st.hasMoreTokens()){				
					if(st.countTokens()>1){
						String current_field_name = st.nextToken();
						try{
							if(writeValue==null && current_requested instanceof Map) writeValue = ((Map)current_requested).get(current_field_name);
							if(writeValue==null && current_requested instanceof List) writeValue = ((List)current_requested).get(Integer.valueOf(current_field_name));
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
									)
							);
						else setCampoValuePoint(
									current_requested,
									last_field_name,
									util_makeValue.makeValue1(current_requested,value,last_field_name)
								);
					}catch(Exception e){
						try{
							if(format!=null)
								setCampoValuePoint(current_requested,key,util_makeValue.makeFormatedValue((delegated==null)?this:delegated,format,value,getCampoValue(key),replaceOnBlank,replaceOnErrorFormat));
							else setCampoValuePoint(current_requested,key,util_makeValue.makeValue(value,getCampoValue(key)));
						}catch(Exception ex){
						}
					}
				}


			}
		}
	}


	public void initMultiPart(HttpServletRequest request) throws bsControllerException{
		HashMap parameters = util_multipart.popolateHashMap(request);
		initPartFromMap(parameters);
	}
	
	public boolean initJsonPart(HttpServletRequest request) throws bsControllerException{
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

		if(isJson) initPartFromMap(parameters);
		
		return isJson;
	}
	

	private void initPartFromMap(HashMap parameters) throws bsControllerException{
		if(parameters==null) parameters=new HashMap();
		parametersMP = parameters;
		
		xmloutput=false;
		jsonoutput=false;
		boolean inputBase64 = (parameters.get(bsController.CONST_ID_INPUTBASE64)!=null &&
				(
						parameters.get(bsController.CONST_ID_INPUTBASE64).toString().equalsIgnoreCase("true") ||
						parameters.get(bsController.CONST_ID_INPUTBASE64).toString().equalsIgnoreCase(new BASE64Encoder().encode("true".getBytes()))		
				)
			);


//		Vector en = new Vector(parameters.keySet());
//		for(int k=0;k<parameters.keySet().size();k++){
		for (Object elem : parameters.keySet()) {
			String key = (String)elem;
			
			if(parameters.get(key) instanceof String ){
				String value = (String)parameters.get(key);
				String format = (String)parameters.get("$format_"+key);
				String replaceOnBlank = (String)parameters.get("$replaceOnBlank_"+key);
				String replaceOnErrorFormat = (String)parameters.get("$replaceOnErrorFormat_"+key);

				if(inputBase64){
					String charset = (parameters.get("$REQUEST_CHARSET")==null || parameters.get("$REQUEST_CHARSET").toString().equals(""))?"UTF-8":parameters.get("$REQUEST_CHARSET").toString();

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
							if(delegated!=null){
								makedValue=util_makeValue.makeFormatedValue1(delegated,format,value,key,replaceOnBlank,replaceOnErrorFormat);
								if(makedValue==null) makedValue=util_makeValue.makeFormatedValue1(this,format,value,key,replaceOnBlank,replaceOnErrorFormat);
							}else{
								makedValue=util_makeValue.makeFormatedValue1(this,format,value,key,replaceOnBlank,replaceOnErrorFormat);
							}
						}else{
							if(delegated!=null){
								makedValue=util_makeValue.makeValue1(delegated,value,key);
								if(makedValue==null) makedValue=util_makeValue.makeValue1(this,value,key);
							}else{
								makedValue=util_makeValue.makeValue1(this,value,key);
							}
						}
						setCampoValueWithPoint(key,makedValue);
					}catch(Exception e){
						try{
							Object makedValue=null;

							if(format!=null){
								if(delegated!=null){
									makedValue=util_makeValue.makeFormatedValue(delegated,format,value,getCampoValue(key),replaceOnBlank,replaceOnErrorFormat);
									if(makedValue==null) makedValue=util_makeValue.makeFormatedValue(this,format,value,getCampoValue(key),replaceOnBlank,replaceOnErrorFormat);
								}else{
									makedValue=util_makeValue.makeFormatedValue(this,format,value,getCampoValue(key),replaceOnBlank,replaceOnErrorFormat);
								}
							}else makedValue=util_makeValue.makeValue(value,getCampoValue(key));

							setCampoValueWithPoint(key,makedValue);
						}catch(Exception ex){
							if(parametersFly==null) parametersFly = new HashMap();
							if(key!=null && key.length()>0 && key.indexOf(0)!='$') parametersFly.put(key, value);
						}
					}

				}else{
					Object writeValue=null;
					Object current_requested = (delegated==null)?this:delegated;
					
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
								if(writeValue==null && current_requested instanceof List) writeValue = ((List)current_requested).get(Integer.valueOf(current_field_name));

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
										)
								);
							else setCampoValuePoint(
										current_requested,
										last_field_name,
										util_makeValue.makeValue1(current_requested,value,last_field_name)
									);
						}catch(Exception e){
							try{
								if(format!=null)
									setCampoValuePoint(current_requested,key,util_makeValue.makeFormatedValue((delegated==null)?this:delegated,format,value,getCampoValue(key),replaceOnBlank,replaceOnErrorFormat));
								else setCampoValuePoint(current_requested,key,util_makeValue.makeValue(value,getCampoValue(key)));
							}catch(Exception ex){
							}
						}
					}


				}
			}
		}

	}


public void init(HashMap _content) throws bsControllerException{
	if(_content==null) return;
	
	xmloutput=false;
	jsonoutput=false;
	boolean inputBase64 = (_content.get(bsController.CONST_ID_INPUTBASE64)!=null &&
			(
					_content.get(bsController.CONST_ID_INPUTBASE64).toString().equalsIgnoreCase("true") ||
					_content.get(bsController.CONST_ID_INPUTBASE64).toString().equalsIgnoreCase(new BASE64Encoder().encode("true".getBytes()))		
			)
		);
	
//	Object[] keys = _content.keySet().toArray();
//	for (int ii = 0; ii < keys.length; ii++){
	for (Object elem :  _content.keySet()) {
		String key = (String)elem;
		
//		String key = (String)keys[ii];
		String value = (String)_content.get(key);
		String format = (String)_content.get("$format_"+key);
		String replaceOnBlank = (String)_content.get("$replaceOnBlank_"+key);
		String replaceOnErrorFormat = (String)_content.get("$replaceOnErrorFormat_"+key);

		if(inputBase64){
			String charset = (_content.get("$REQUEST_CHARSET")==null || _content.get("$REQUEST_CHARSET").toString().equals(""))?"UTF-8":_content.get("$REQUEST_CHARSET").toString();

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
					if(delegated!=null){
						makedValue=util_makeValue.makeFormatedValue1(delegated,format,value,key,replaceOnBlank,replaceOnErrorFormat);
						if(makedValue==null) makedValue=util_makeValue.makeFormatedValue1(this,format,value,key,replaceOnBlank,replaceOnErrorFormat);
					}else{
						makedValue=util_makeValue.makeFormatedValue1(this,format,value,key,replaceOnBlank,replaceOnErrorFormat);
					}
				}else{
					if(delegated!=null){
						makedValue=util_makeValue.makeValue1(delegated,value,key);
						if(makedValue==null) makedValue=util_makeValue.makeValue1(this,value,key);
					}else{
						makedValue=util_makeValue.makeValue1(this,value,key);
					}
				}
				setCampoValueWithPoint(key,makedValue);
			}catch(Exception e){
				try{
					Object makedValue=null;

					if(format!=null){
						if(delegated!=null){
							makedValue=util_makeValue.makeFormatedValue(delegated,format,value,getCampoValue(key),replaceOnBlank,replaceOnErrorFormat);
							if(makedValue==null) makedValue=util_makeValue.makeFormatedValue(this,format,value,getCampoValue(key),replaceOnBlank,replaceOnErrorFormat);
						}else{
							makedValue=util_makeValue.makeFormatedValue(this,format,value,getCampoValue(key),replaceOnBlank,replaceOnErrorFormat);
						}
					}else makedValue=util_makeValue.makeValue(value,getCampoValue(key));

					setCampoValueWithPoint(key,makedValue);
				}catch(Exception ex){
					if(parametersFly==null) parametersFly = new HashMap();
					if(key!=null && key.length()>0 && key.indexOf(0)!='$') parametersFly.put(key, value);
				}
			}
		}else{
			Object writeValue=null;
			Object current_requested = (delegated==null)?this:delegated;

		
			String last_field_name = null;
			StringTokenizer st = new StringTokenizer(key,".");
			while(st.hasMoreTokens()){				
				if(st.countTokens()>1){
					String current_field_name = st.nextToken();
					try{
						writeValue = util_reflect.getValue(current_requested,"get"+util_reflect.adaptMethodName(current_field_name),null);
						if(writeValue==null) writeValue = util_reflect.getValue(current_requested,current_field_name,null);
						if(writeValue==null && current_requested instanceof Map) writeValue = ((Map)current_requested).get(current_field_name);
						if(writeValue==null && current_requested instanceof List) writeValue = ((List)current_requested).get(Integer.valueOf(current_field_name));
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
								)
						);
					else setCampoValuePoint(
								current_requested,
								last_field_name,
								util_makeValue.makeValue1(current_requested,value,last_field_name)
							);
				}catch(Exception e){
					try{
						if(format!=null)
							setCampoValuePoint(current_requested,key,util_makeValue.makeFormatedValue((delegated==null)?this:delegated,format,value,getCampoValue(key),replaceOnBlank,replaceOnErrorFormat));
						else setCampoValuePoint(current_requested,key,util_makeValue.makeValue(value,getCampoValue(key)));
					}catch(Exception ex){
					}
				}
			}
		}

	}
}

public void init(i_bean another_bean) throws bsControllerException{
	current_auth = another_bean.getCurrent_auth();
	try{
		Method[] methods = util_reflect.getMethods(this,"get");
		for(int i=0;i<methods.length;i++){
			String methodName = methods[i].getName().substring(3);
			set(methodName,another_bean.get(methodName));
		}
	}catch(Exception ex){
	}

}

public void reInit(i_elementDBBase _i_el) {
	try{
		Method[] methods = util_reflect.getMethods(_i_el,"get");
		for(int i=0;i<methods.length;i++){
			String methodName = methods[i].getName().substring(3);
			try{
				set(methodName,_i_el.getCampoValue(methodName));
			}catch(Exception e){
			}
		}
	}catch(Exception ex){
		new bsControllerException(ex,iStub.log_ERROR);
	}

}

public void setCampoValuePoint(Object req, String nome, Object value) throws Exception{
	if(req instanceof Map){
		((Map)req).put(nome, value);
	}else{
		Object[] par = new Object[1];
		par[0]=value;
		setValue(req, "set"+util_reflect.adaptMethodName(nome.trim()),par);
	}
}

public Object get(String value) {
	try{
		Object requested = (delegated==null)?this:delegated;
		Object result = null;
		if(value.indexOf('.')>-1){
			result = util_reflect.prepareWriteValueForTag(requested,"get",value,null);
			if(result==null){
				if(parametersFly==null) parametersFly = new HashMap();
				result = util_reflect.prepareWriteValueForTag(parametersFly,"get",value,null);
			}
		}else result = getCampoValue(requested,value);
		if(result!=null) return result;
		if(parametersFly!=null){
			return parametersFly.get(value);
		}
		return null;
	}catch(Exception e){
		if(parametersFly!=null){
			return parametersFly.get(value);
		}
		return null;
	}
}

public Object get(Object requested,String value) {
	try{
		Object result = null;
		if(value.indexOf('.')>-1)
			result = util_reflect.prepareWriteValueForTag(requested,"get",value,null);
		else result = getCampoValue(requested,value);
		if(result!=null) return result;
		if(parametersFly!=null){
			return parametersFly.get(value);
		}
		return null;
	}catch(Exception e){
		if(parametersFly!=null){
			return parametersFly.get(value);
		}
		return null;
	}
}

public void put(String name, Object value) {
	if(name==null) return;
	if(parametersFly==null) parametersFly = new HashMap();
	parametersFly.put(name, value);
}

public void set(String name, Object value) {
	try{
		setCampoValueWithPoint(name,value);
	}catch(Exception e){
	}
}

private Object getPrimitiveArgument(String name, String s_value){

	Object primArgument = null;
	Class reqClass = (delegated==null)?this.getClass():delegated.getClass();
	if(name.indexOf('.')>-1){
		StringTokenizer st = new StringTokenizer(name,".");		
/*
		Vector allfields=new Vector();
		while(st.hasMoreTokens())
			allfields.add(st.nextToken());

			
		String complexName="";
		for(int i=0;i<allfields.size()-1;i++){
			complexName+=allfields.get(i);
			if(i!=allfields.size()-2) complexName+=".";
		}
		
		name = (String)allfields.get(allfields.size()-1);
*/

		String complexName="";
		while(st.hasMoreTokens()){
			String token = st.nextToken();			
			if(st.countTokens()>0) complexName+=token;
			if(st.countTokens()>1) complexName+=".";
			if(st.countTokens()==0) name=token;
		}

		
		Object writeObj=get(complexName);
		if(writeObj==null) return primArgument;
		reqClass=writeObj.getClass();			

		

	}


	try{
		java.lang.reflect.Method mtd = null;
		Class[] cls = new Class[0];

		mtd = reqClass.getMethod("get"+util_reflect.adaptMethodName(name),cls);
		if(mtd!=null){
			Class primArgumentClass = mtd.getReturnType();
				if(primArgumentClass.equals(double.class)){
					primArgument = Double.valueOf(s_value);
				}else if(primArgumentClass.equals(int.class)){
					primArgument = Integer.valueOf(s_value);
				}else if(primArgumentClass.equals(boolean.class)){
					primArgument = Boolean.valueOf(s_value);
				}else if(primArgumentClass.equals(char.class)){
					primArgument = s_value;
				}else if(primArgumentClass.equals(byte.class)){
					primArgument = Byte.valueOf(s_value);
				}else if(primArgumentClass.equals(short.class)){
					primArgument = Short.valueOf(s_value);
				}else if(primArgumentClass.equals(long.class)){
					primArgument = Long.valueOf(s_value);
				}else if(primArgumentClass.equals(float.class)){
					primArgument = Float.valueOf(s_value);
				}
			}

	}catch(Exception e){

	}
	return primArgument;
}



public void set(String name, int value) {
	Object primArgument =  getPrimitiveArgument(name, String.valueOf(value));
	try{
		if(primArgument!=null) setCampoValueWithPoint(name,primArgument);
		else setCampoValueWithPoint(name,Integer.valueOf(value));
	}catch(Exception e){
	}
}
public void set(String name, short value) {
	Object primArgument =  getPrimitiveArgument(name, String.valueOf(value));
	try{
		if(primArgument!=null) setCampoValueWithPoint(name,primArgument);
		else setCampoValueWithPoint(name,Short.valueOf(value));
	}catch(Exception e){
	}
}
public void set(String name, long value) {
	Object primArgument =  getPrimitiveArgument(name, String.valueOf(value));
	try{
		if(primArgument!=null) setCampoValueWithPoint(name,primArgument);
		else setCampoValueWithPoint(name,Long.valueOf(value));
	}catch(Exception e){
	}
}
public void set(String name, float value) {
	Object primArgument =  getPrimitiveArgument(name, String.valueOf(value));
	try{
		if(primArgument!=null) setCampoValueWithPoint(name,primArgument);
		else setCampoValueWithPoint(name,Float.valueOf(value));
	}catch(Exception e){
	}
}
public void set(String name, double value) {
	Object primArgument =  getPrimitiveArgument(name, String.valueOf(value));
	try{
		if(primArgument!=null) setCampoValueWithPoint(name,primArgument);
		else setCampoValueWithPoint(name,Double.valueOf(value));
	}catch(Exception e){
	}
}
public void set(String name, byte value) {
	Object primArgument =  getPrimitiveArgument(name, String.valueOf(value));
	try{
		if(primArgument!=null) setCampoValueWithPoint(name,primArgument);
		else setCampoValueWithPoint(name,Byte.valueOf(value));
	}catch(Exception e){
	}
}
public void set(String name, boolean value) {
	Object primArgument =  getPrimitiveArgument(name, String.valueOf(value));
	try{
		if(primArgument!=null) setCampoValueWithPoint(name,primArgument);
		else setCampoValueWithPoint(name,Boolean.valueOf(value));
	}catch(Exception e){
	}
}
public void set(String name, char value) {
	Object primArgument =  getPrimitiveArgument(name, String.valueOf(value));
	try{
		if(primArgument!=null) setCampoValueWithPoint(name,primArgument);
		else setCampoValueWithPoint(name,new Character(value));
	}catch(Exception e){
	}
}


public int getInt(String name) {
	int result = 0;
	Object objectResult = get(name);
	if(objectResult==null) return result;
	if(objectResult instanceof Integer ) return ((Integer)objectResult).intValue();
	try{
		return new BigDecimal(objectResult.toString().trim()).intValue();
	}catch(Exception e){
	}
	return result;
}

public short getShort(String name) {
	short result = 0;
	Object objectResult = get(name);
	if(objectResult==null) return result;
	if(objectResult instanceof Short ) return ((Short)objectResult).shortValue();
	try{
		return new BigDecimal(objectResult.toString().trim()).shortValue();
	}catch(Exception e){
	}
	return result;
}

public long getLong(String name) {
	long result = 0;
	Object objectResult = get(name);
	if(objectResult==null) return result;
	if(objectResult instanceof Long ) return ((Long)objectResult).longValue();
	try{
		return new BigDecimal(objectResult.toString().trim()).longValue();
	}catch(Exception e){
	}
	return result;
}

public float getFloat(String name) {
	float result = 0;
	Object objectResult = get(name);
	if(objectResult==null) return result;
	if(objectResult instanceof Float ) return ((Float)objectResult).floatValue();
	try{
		return new BigDecimal(objectResult.toString().trim()).floatValue();
	}catch(Exception e){
	}
	return result;
}

public double getDouble(String name) {
	double result = 0;
	Object objectResult = get(name);
	if(objectResult==null) return result;
	if(objectResult instanceof Double ) return ((Double)objectResult).doubleValue();
	try{
		return new BigDecimal(objectResult.toString().trim()).doubleValue();
	}catch(Exception e){
	}
	return result;
}

public byte getByte(String name) {
	byte result = 0;
	Object objectResult = get(name);
	if(objectResult==null) return result;
	if(objectResult instanceof Byte ) return ((Byte)objectResult).byteValue();
	try{
		return Byte.valueOf(objectResult.toString().trim()).byteValue();
	}catch(Exception e){
	}
	return result;
}

public boolean getBoolean(String name) {
	boolean result = false;
	Object objectResult = get(name);
	if(objectResult==null) return result;
	if(objectResult instanceof Boolean ) return ((Boolean)objectResult).booleanValue();
	try{
		return Boolean.valueOf(objectResult.toString().trim()).booleanValue();
	}catch(Exception e){
	}
	return result;
}

public char getChar(String name) {
	char result = ' ';
	Object objectResult = get(name);
	if(objectResult==null) return result;
	if(objectResult instanceof Character ) return ((Character)objectResult).charValue();
	try{
		return new Character(objectResult.toString().trim().charAt(0)).charValue();
	}catch(Exception e){
	}
	return result;
}

public BigDecimal getBigDecimal(String name) {
	BigDecimal result = null;
	Object objectResult = get(name);
	if(objectResult==null) return result;
	if(objectResult instanceof Double ) return new BigDecimal(((Double)objectResult).toString());
	try{
		return new BigDecimal(objectResult.toString().trim());
	}catch(Exception e){
	}
	return result;
}

public String getString(String name) {
	String result = "";
	Object objectResult = get(name);
	if(objectResult==null) return result;
	result = objectResult.toString();
	return result;
}

public boolean setCampoValueWithPoint(String name, Object value) throws Exception{
	try{
		if(name.indexOf('.')==-1){
			if(delegated!=null){
				boolean res = setValue(delegated, "set"+util_reflect.adaptMethodName(name.trim()),new Object[]{value},false);
				if(!res) res = setValue(this, "set"+util_reflect.adaptMethodName(name.trim()),new Object[]{value},false);
				return res;
			}
			else{
				boolean res = setValue(this, "set"+util_reflect.adaptMethodName(name.trim()),new Object[]{value},false);
				if(!res){
					if(parametersFly==null) parametersFly = new HashMap();
					parametersFly.put(name, value);
				}
				return res;
			}

		}else{
			StringTokenizer st = new StringTokenizer(name,".");
/*			
			Vector allfields=new Vector();
			while(st.hasMoreTokens())
				allfields.add(st.nextToken());
			
			String complexName="";
			for(int i=0;i<allfields.size()-1;i++){
				complexName+=allfields.get(i);
				if(i!=allfields.size()-2) complexName+=".";
			}

			Object writeObj=get(complexName);
			if(writeObj==null) writeObj=get(this,complexName);
			if(writeObj==null) return false;
			String current_field_name = (String)allfields.get(allfields.size()-1);
*/
			String current_field_name = null;
			String complexName="";
			while(st.hasMoreTokens()){
				String token = st.nextToken();			
				if(st.countTokens()>0) complexName+=token;
				if(st.countTokens()>1) complexName+=".";
				if(st.countTokens()==0) current_field_name=token;
			}
			Object writeObj=get(complexName);
			if(writeObj==null) writeObj=get(this,complexName);
			if(writeObj==null) return false;
			
			if(writeObj!=null && current_field_name!=null){
				try{
						setCampoValuePoint(
								writeObj,
								current_field_name,
								value
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
//	return true;
}

public info_bean get_infobean() {
	return _infobean;
}
public void set_infobean(info_bean bean) {
	_infobean = bean;
}
public info_action get_infoaction() {
	return _infoaction;
}
public void set_infoaction(info_action action) {
	_infoaction = action;
}

public boolean logic_equals(i_bean obj){
	return this.equals(obj);
}


public String getMiddleAction() {
	return middleAction;
}

public void setMiddleAction(String string) {
	if(string!=null){
		if(string.equalsIgnoreCase("null")) middleAction=null;
		else if(string.equalsIgnoreCase("undefined") || string.equalsIgnoreCase("undef")){}
		else middleAction = string;
	}else middleAction = string;
}
public void set$maction(String string) {
	if(string!=null){
		if(string.equalsIgnoreCase("null")) middleAction=null;
		else if(string.equalsIgnoreCase("undefined") || string.equalsIgnoreCase("undef")){}
		else middleAction = string;
	}else middleAction = string;
}

public String get$id_returnPointOfService() {
	return $id_returnPointOfService;
}

public void set$id_returnPointOfService(String string) {
	$id_returnPointOfService = string;
}

public int getCountActions() {
	return countActions;
}

public void setCountActions(int i) {
	countActions = i;
}

public auth_init getCurrent_auth() {
	return current_auth;
}

public void setCurrent_auth(auth_init current_auth) {
	this.current_auth = current_auth;
}

public boolean isRefresh() {
	return refresh;
}
public boolean getRefresh() {
	return refresh;
}

public void setRefresh(boolean refresh) {
	this.refresh = refresh;
}

public HashMap getParametersMP() {
	return parametersMP;
}

public void setParametersMP(HashMap parametersMP) {
	this.parametersMP = parametersMP;
}

public HashMap getParametersFly() {
	return parametersFly;
}

public void setParametersFly(HashMap parametersFly) {
	this.parametersFly = parametersFly;
}

public String getJs4ajax() {
	return js4ajax;
}

public void setJs4ajax(String js4ajax) {
	this.js4ajax = js4ajax;
}

public boolean getXmloutput() {
	return xmloutput;
}

public void setXmloutput(boolean xmloutput) {
	this.xmloutput = xmloutput;
}

public boolean getJsonoutput() {
	return jsonoutput;
}

public void setJsonoutput(boolean jsonoutput) {
	this.jsonoutput = jsonoutput;
}

public boolean getTransformationoutput() {
	return transformationoutput;
}

public void setTransformationoutput(boolean transformationoutput) {
	this.transformationoutput = transformationoutput;
}

public boolean getBinaryoutput() {
	return binaryoutput;
}

public void setBinaryoutput(boolean binaryoutput) {
	this.binaryoutput = binaryoutput;
}

public Object getDelegated() {
	return delegated;
}

public void setDelegated(Object delegated) {
	this.delegated = delegated;
}

public boolean getVirtual() {
	return virtual;
}

public void setVirtual(boolean virtual) {
	this.virtual = virtual;
}

public listener_bean getListener_b() {
	return listener_b;
}

public void setListener_b(listener_bean listener) {
	this.listener_b = listener;
	if(listener_b!=null) listener_b.setOwner(this);
}

public void onPostInit(HashMap content) {
	if(listener_b!=null) listener_b.onPostInit(content);
}

public void onPostInit(HttpServletRequest request) {
	if(listener_b!=null) listener_b.onPostInit(request);
}

public void onPostInit(i_bean anotherBean) {
	if(listener_b!=null) listener_b.onPostInit(anotherBean);
}

public void onPostValidate(redirects redirect,HttpServletRequest request) {
	if(listener_b!=null) listener_b.onPostValidate(redirect, request);
}

public void onPreInit(HashMap content) {
	if(listener_b!=null) listener_b.onPreInit(content);
}

public void onPreInit(HttpServletRequest request) {
	if(listener_b!=null) listener_b.onPreInit(request);
}

public void onPreInit(i_bean anotherBean) {
	if(listener_b!=null) listener_b.onPreInit(anotherBean);
}

public void onPreValidate(HttpServletRequest request) {
	if(listener_b!=null) listener_b.onPreValidate(request);
}

public void onPostInstance() {
	if(listener_b!=null) listener_b.onPostInstance();
}

public void onPostInstanceFromProvider() {
	if(listener_b!=null) listener_b.onPostInstanceFromProvider();
}

public void onAddToNavigation() {
	if(listener_b!=null) listener_b.onAddToNavigation();
}

public void onGetFromNavigation() {
	if(listener_b!=null) listener_b.onGetFromNavigation();
}

public void onAddToSession() {
	if(listener_b!=null) listener_b.onAddToSession();
}

public void onGetFromSession() {
	if(listener_b!=null) listener_b.onGetFromSession();
}

public void onAddToLastInstance() {
	if(listener_b!=null) listener_b.onAddToLastInstance();	
}


public void onGetFromLastInstance() {
	if(listener_b!=null) listener_b.onGetFromLastInstance();
}

public void setOwner(i_bean owner) {
}

public String getXmloutput_encoding() {
	return xmloutput_encoding;
}

public void setXmloutput_encoding(String xmloutputEncoding) {
	xmloutput_encoding = xmloutputEncoding;
}

public String getJsonoutput_encoding() {
	return jsonoutput_encoding;
}

public void setJsonoutput_encoding(String jsonoutputEncoding) {
	jsonoutput_encoding = jsonoutputEncoding;
}

public String getBinaryoutput_encoding() {
	return binaryoutput_encoding;
}

public void setBinaryoutput_encoding(String binaryoutputEncoding) {
	binaryoutput_encoding = binaryoutputEncoding;
}

public boolean convert2xml(){
	return false;
}

public boolean convert2json(){
	return false;
}



}
