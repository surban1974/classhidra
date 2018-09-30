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



import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.elements.elementBeanBase;
import it.classhidra.core.tool.elements.i_elementDBBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_makeValue;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_supportbean;
import it.classhidra.core.tool.util.util_xml;
import it.classhidra.serialize.JsonMapper;
import it.classhidra.serialize.Serialized;
import it.classhidra.serialize.XmlMapper;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;



public class bean extends elementBeanBase implements i_bean  {
	private static final long serialVersionUID = 3917073481415967577L;

	protected info_bean _infobean;
	protected info_action _infoaction;
	protected auth_init current_auth;
	protected HashMap parametersMP;
	protected HashMap parametersFly;
	protected String $id_returnPointOfService;
	protected String middleAction;
	protected String _saltid;
	protected String js4ajax="false";
	protected boolean refresh=false;
	
	protected String outputappliedfor;
	protected String outputserializedname;
	
	protected boolean xmloutput=false;
	protected String xmloutput_encoding="";
	
	protected boolean jsonoutput=false;
	protected String jsonoutput_encoding="";
	
	protected boolean binaryoutput=false;
	protected String binaryoutput_encoding="";

	protected boolean gzipoutput=false;
	
	protected boolean transformationoutput=false;
	
	protected boolean virtual=false;
	protected int countActions;
	protected boolean asyncInterrupt=false;

	protected listener_bean listener_b;

	protected Object delegated;
	
	protected info_context info_context = new info_context(this.getClass());
	
	protected HashMap initErrors;
	protected HashMap components;





public void reimposta(){
	if(parametersFly==null)
		parametersFly = new HashMap();
}

public redirects validate(HttpServletRequest request){
	return null;
}

public redirects validate(HashMap parameters){
	return null;
}




public void init(HttpServletRequest request) throws bsControllerException{
	if(request==null)
		return;
	if(current_auth==null){
		try{
			current_auth = bsController.checkAuth_init(request);
		}catch(Exception ex){
			new bsException(ex, iStub.log_ERROR);
		}
	}
	if(_saltid==null){
		try{
			_saltid = util_xml.normalHTML(bsController.encrypt(request.getSession().getId()+"$"+System.currentTimeMillis()),null);
		}catch(Exception ex){
			new bsException(ex, iStub.log_ERROR);
		}
	}
	
	getInitErrors().clear();
	
//	if(parametersFly!=null)
//		parametersFly.clear();

	if(get_infoaction()!=null && get_infoaction().getReloadAfterNextNavigated()!=null && get_infoaction().getReloadAfterNextNavigated().equalsIgnoreCase("false")){
		String id_prev = request.getParameter(bsConstants.CONST_BEAN_$NAVIGATION);
		if(id_prev!=null && id_prev.indexOf(":")>-1){
			id_prev = id_prev.substring(0,id_prev.indexOf(":"));
			info_navigation nav = bsController.getFromInfoNavigation(null, request);
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
	
	if(request.getContentType()!=null && request.getContentType().toLowerCase().indexOf("application/xml")>-1){
		if(initXmlPart(request)) return;
	}	

	initNormal(request);

}


	public void initNormal(HttpServletRequest request) throws bsControllerException{
		util_supportbean.initNormal(this, request);
	}


	public void initMultiPart(HttpServletRequest request) throws bsControllerException{
		util_supportbean.initMultiPart(this, request);
	}

	public boolean initJsonPart(HttpServletRequest request) throws bsControllerException{
		return util_supportbean.initJsonPart(this,request,null);
	}
	
	public boolean initJsonPart(HttpServletRequest request, JsonMapper mapper) throws bsControllerException{
		return util_supportbean.initJsonPart(this,request,mapper);
	}

	public boolean initXmlPart(HttpServletRequest request) throws bsControllerException{
		return util_supportbean.initXmlPart(this,request,null);
	}	
	
	public boolean initXmlPart(HttpServletRequest request, XmlMapper mapper) throws bsControllerException{
		return util_supportbean.initXmlPart(this,request,mapper);
	}	

	
	public void initPartFromMap(HashMap parameters) throws bsControllerException{
		if(parameters==null) 
			parameters=new HashMap();
		if(parametersMP==null)
			parametersMP=new HashMap();
		parametersMP.putAll(parameters);
		
		
		initFromMap(parameters,true);
		
	}
	
	public void initFromMap(HashMap parameters, boolean add2fly) throws bsControllerException{
		if(parameters==null) 
			parameters=new HashMap();



		xmloutput=false;
		jsonoutput=false;
		boolean inputBase64 = (parameters.get(bsController.CONST_ID_INPUTBASE64)!=null &&
				(
						parameters.get(bsController.CONST_ID_INPUTBASE64).toString().equalsIgnoreCase("true") ||
						parameters.get(bsController.CONST_ID_INPUTBASE64).toString().equalsIgnoreCase(DatatypeConverter.printBase64Binary("true".getBytes()))
				)
			);


		
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
						if(value!=null) value=new String(DatatypeConverter.parseBase64Binary(value),charset);
					}catch(Exception e){}
					try{
						if(format!=null) format=new String(DatatypeConverter.parseBase64Binary(format),charset);
					}catch(Exception e){}
					try{
						if(replaceOnBlank!=null) replaceOnBlank=new String(DatatypeConverter.parseBase64Binary(replaceOnBlank),charset);
					}catch(Exception e){}
					try{
						if(replaceOnErrorFormat!=null) replaceOnErrorFormat=new String(DatatypeConverter.parseBase64Binary(replaceOnErrorFormat),charset);
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
							Object current_requested = null;
							if(delegated!=null){
								current_requested=delegated;
							}else{
								current_requested=this;
							}
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
								if(delegated!=null){
									makedValue=util_makeValue.makeValue1(delegated,value,key);
									if(makedValue==null) makedValue=util_makeValue.makeValue1(this,value,key);
								}else{
									makedValue=util_makeValue.makeValue1(this,value,key);
								}
								if(!setCampoValueWithPoint(key,makedValue))
									throw new Exception();
							}
							
						}

						
						

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

							if(!setCampoValueWithPoint(key,makedValue))
								throw new Exception();
						}catch(Exception ex){
							getInitErrors().put(key,"Init Map: ["+key+"] not found in the bean ["+this.getClass().getName()+"]."+((add2fly)?" Will e added into FLY.":""));
							if(add2fly){
								if(parametersFly==null) 
									parametersFly = new HashMap();
								if(key!=null && key.length()>0 && key.indexOf(0)!='$') 
									parametersFly.put(key, value);
							}
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
								if(writeValue==null && current_requested instanceof List) writeValue = ((List)current_requested).get(Integer.valueOf(current_field_name).intValue());
								if(writeValue==null && current_requested instanceof Set) writeValue = Arrays.asList(((Set)current_requested)).toArray()[Integer.valueOf(current_field_name).intValue()];
								if(writeValue==null && current_requested.getClass().isArray()){
									Class componentType = current_requested.getClass().getComponentType();
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
									setCampoValuePoint(current_requested,key,util_makeValue.makeFormatedValue((delegated==null)?this:delegated,format,value,getCampoValue(key),replaceOnBlank,replaceOnErrorFormat),false);
								else setCampoValuePoint(current_requested,key,util_makeValue.makeValue(value,getCampoValue(key)),false);
							}catch(Exception ex){
								getInitErrors().put(key,"Init Map: ["+key+"] not found in the bean ["+this.getClass().getName()+"].");
							}
						}
					}else
						getInitErrors().put(key,"Init Map: ["+key+"] not found in the bean ["+this.getClass().getName()+"].");



				}
			}
		}

	}	

public void init(HashMap _content) throws bsControllerException{
	init_(_content);
}

public void init_(HashMap _content) throws bsControllerException{
	if(_content==null) return;
	initFromMap(_content,true);
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
public void reInit(i_bean another_bean){
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

public void clearBeforeStore(){
	if(parametersMP!=null)
		parametersMP.clear();
	if(initErrors!=null)
		initErrors.clear();
	
//	if(parametersFly!=null)
//		parametersFly.clear();

}

public void setCampoValuePoint(Object req, String nome, Object value) throws Exception{
	if(req instanceof Map){
		((Map)req).put(nome, value);
	}else{
		boolean res = setValue(req, "set"+util_reflect.adaptMethodName(nome.trim()),new Object[]{value});
		if(!res)
			res = setValueMapped(req, "set", nome.trim(),new Object[]{value},false);
	}
}

public void setCampoValuePoint(Object req, String nome, Object value, boolean log) throws Exception{
	if(req instanceof Map){
		((Map)req).put(nome, value);
	}else{
		boolean res = setValue(req, "set"+util_reflect.adaptMethodName(nome.trim()),new Object[]{value},log);
		if(!res)
			res = setValueMapped(req, "set", nome.trim(),new Object[]{value},log);
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

public Object getPrimitiveArgument(String name, String s_value){

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

public Collection getCollection(String name){
	Collection result = new ArrayList();
	Object objectResult = get(name);
	if(objectResult==null) return result;
	if(objectResult instanceof Collection ) return ((Collection)objectResult);
	return result;
}

public List getList(String name) {
	List result = new ArrayList();
	Object objectResult = get(name);
	if(objectResult==null) return result;
	if(objectResult instanceof List ) return ((List)objectResult);
	return result;
}

public Map getMap(String name) {
	Map result = new HashMap();
	Object objectResult = get(name);
	if(objectResult==null) return result;
	if(objectResult instanceof Map ) return ((Map)objectResult);
	return result;
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
			try{
				if(delegated!=null){
					boolean res = setValue(delegated, "set"+util_reflect.adaptMethodName(name.trim()),new Object[]{value},false);
					if(!res)
						res = setValueMapped(delegated, "set", name.trim(),new Object[]{value},false);
					
					if(!res)
						res = setValue(this, "set"+util_reflect.adaptMethodName(name.trim()),new Object[]{value},false);
					if(!res)
						res = setValueMapped(this, "set", name.trim(),new Object[]{value},false);
					return res;
				}
				else{
					boolean res = setValue(this, "set"+util_reflect.adaptMethodName(name.trim()),new Object[]{value},false);
					if(!res)
						res = setValueMapped(this, "set", name.trim(),new Object[]{value},false);
					
					if(!res){
						if(parametersFly==null) 
							parametersFly = new HashMap();
						parametersFly.put(name, value);
					}
					return res;
				}
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

public boolean getGzipoutput() {
	return gzipoutput;
}

public void setGzipoutput(boolean gzipoutput) {
	this.gzipoutput = gzipoutput;
}

public listener_bean getListener_b() {
	try{
		return listener_b;
	}catch(Exception e){
		return null;
	}catch(Throwable e){
		return null;
	}
}

public void setListener_b(listener_bean listener) {
	try{
		this.listener_b = listener;
		if(listener_b!=null) listener_b.setOwner(this);
	}catch(Exception e){
	}catch(Throwable e){
	}
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


public void onPreValidate(HashMap _content) {
	if(listener_b!=null) listener_b.onPreValidate(_content);
}

public void onPostValidate(redirects redirect, HashMap _content) {
	if(listener_b!=null) listener_b.onPostValidate(redirect, _content);
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

public String getOutputappliedfor() {
	return outputappliedfor;
}

public void setOutputappliedfor(String outputfor) {
	this.outputappliedfor = outputfor;
}

public String getOutputserializedname() {
	return outputserializedname;
}

public void setOutputserializedname(String outputserializedname) {
	this.outputserializedname = outputserializedname;
}

public boolean convert2xml(){
	return false;
}

public boolean convert2json(){
	return false;
}

public i_action asAction(){
	return (action)this;
}

public i_bean asBean(){
	return (bean)this;
}

public info_context getInfo_context() {
	return info_context;
}

public void setInfo_context(i_info_context info_context) {
	this.info_context = (info_context)info_context;
}

public Map getInitErrors(){ 
	if(initErrors==null)
		initErrors = new HashMap();
	return initErrors;
}

public boolean isAsyncInterrupt() {
	return asyncInterrupt;
}

public void setAsyncInterrupt(boolean asyncInterrupt) {
	this.asyncInterrupt = asyncInterrupt;
}

public JsonMapper getJsonMapper() {
	return null;
}

public XmlMapper getXmlMapper() {
	return null;
}

public String get$csrf() {
	return _saltid;
}

public redirects validate(String currentAction, String newAction, String newActionCall, HttpServletRequest request)
		throws bsControllerException {
	return null;
}

public redirects validate(String currentAction, String newAction, String newActionCall, HashMap parameters) throws bsControllerException {
	return null;
}

public Map getComponents() {
	if(components==null)
		components = new HashMap();
	return components;	
}

}
