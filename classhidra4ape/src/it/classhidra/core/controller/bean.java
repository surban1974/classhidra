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
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;



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
	private boolean jsonoutput=false;
	private boolean transformationoutput=false;
	private boolean binaryoutput=false;
	private boolean virtual=false;
	public int countActions;
	
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

	if(request.getContentType()!=null && request.getContentType().indexOf("multipart")>-1){
		initMultiPart(request);
		return;
	}
	
	if(request.getContentType()!=null && request.getContentType().toLowerCase().indexOf("application/json")>-1){
		if(initJsonPart(request)) return;
	}
	
	
	xmloutput=false;
	jsonoutput=false;
	
	Enumeration en = request.getParameterNames();
	while(en.hasMoreElements()){
		String key = (String)en.nextElement();
		String value = request.getParameter(key);
		if(key.indexOf(".")==-1){
			try{

				Object makedValue=null;
				if(request.getParameter("$format_"+key)!=null){
					if(delegated!=null){
						makedValue=util_makeValue.makeFormatedValue1(delegated,(String)request.getParameter("$format_"+key),value,key,(String)request.getParameter("$replaceOnBlank_"+key),(String)request.getParameter("$replaceOnErrorFormat_"+key));
						if(makedValue==null) makedValue=util_makeValue.makeFormatedValue1(this,(String)request.getParameter("$format_"+key),value,key,(String)request.getParameter("$replaceOnBlank_"+key),(String)request.getParameter("$replaceOnErrorFormat_"+key));
					}else{
						makedValue=util_makeValue.makeFormatedValue1(this,(String)request.getParameter("$format_"+key),value,key,(String)request.getParameter("$replaceOnBlank_"+key),(String)request.getParameter("$replaceOnErrorFormat_"+key));
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

					if(request.getParameter("$format_"+key)!=null){
						if(delegated!=null){
							makedValue=util_makeValue.makeFormatedValue(delegated,(String)request.getParameter("$format_"+key),value,getCampoValue(key),(String)request.getParameter("$replaceOnBlank_"+key),(String)request.getParameter("$replaceOnErrorFormat_"+key));
							if(makedValue==null) makedValue=util_makeValue.makeFormatedValue(this,(String)request.getParameter("$format_"+key),value,getCampoValue(key),(String)request.getParameter("$replaceOnBlank_"+key),(String)request.getParameter("$replaceOnErrorFormat_"+key));
						}else{
							makedValue=util_makeValue.makeFormatedValue(this,(String)request.getParameter("$format_"+key),value,getCampoValue(key),(String)request.getParameter("$replaceOnBlank_"+key),(String)request.getParameter("$replaceOnErrorFormat_"+key));
						}
					}else makedValue=util_makeValue.makeValue(value,getCampoValue(key));

					setCampoValueWithPoint(key,makedValue);
				}catch(Exception ex){
					if(parametersFly==null) parametersFly = new HashMap();
					if(key!=null && key.length()>0 && key.indexOf(0)!='$') parametersFly.put(key, value);
				}
			}
		}else{
			StringTokenizer st = new StringTokenizer(key,".");
			Vector allfields=new Vector();
			while(st.hasMoreTokens()){
				String current_field_name = st.nextToken();
				allfields.add(current_field_name);
			}

			Object writeValue=null;
			Object current_requested = (delegated==null)?this:delegated;


			for(int i=0;i<allfields.size()-1;i++){
				String current_field_name = (String)allfields.get(i);
				try{
					if(writeValue==null && current_requested instanceof HashMap) writeValue = ((HashMap)current_requested).get(current_field_name);
					if(writeValue==null) writeValue = util_reflect.getValue(current_requested,"get"+util_reflect.adaptMethodName(current_field_name),null);
					if(writeValue==null) writeValue = util_reflect.getValue(current_requested,current_field_name,null);
					if(writeValue==null && current_requested instanceof i_bean) writeValue = ((i_bean)current_requested).get(current_field_name);
				}catch(Exception e){
				}
				current_requested = writeValue;
				writeValue = null;
			}
			String current_field_name = (String)allfields.get(allfields.size()-1);
			try{
				if(request.getParameter("$format_"+key)!=null)
					setCampoValuePoint(
							current_requested,
							current_field_name,
							util_makeValue.makeFormatedValue1(
										current_requested,
										(String)request.getParameter("$format_"+key),
										value,
										current_field_name,
										request.getParameter("$replaceOnBlank_"+key),
										request.getParameter("$replaceOnErrorFormat_"+key)
							)
					);
				else setCampoValuePoint(
							current_requested,
							current_field_name,
							util_makeValue.makeValue1(current_requested,value,current_field_name)
						);
			}catch(Exception e){
				try{
					if(request.getParameter("$format_"+key)!=null)
						setCampoValuePoint(current_requested,key,util_makeValue.makeFormatedValue((delegated==null)?this:delegated,(String)request.getParameter("$format_"+key),value,getCampoValue(key),(String)request.getParameter("$replaceOnBlank_"+key),(String)request.getParameter("$replaceOnErrorFormat_"+key)));
					else setCampoValuePoint(current_requested,key,util_makeValue.makeValue(value,getCampoValue(key)));
				}catch(Exception ex){
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

		Vector en = new Vector(parameters.keySet());
		for(int k=0;k<en.size();k++){
			String key = (String)en.get(k);
			if(parameters.get(key) instanceof String ){
				String value = (String)parameters.get(key);

				if(key.indexOf(".")==-1){
					try{
						Object makedValue=null;
						if(parameters.get("$format_"+key)!=null){
							if(delegated!=null){
								makedValue=util_makeValue.makeFormatedValue1(delegated,(String)parameters.get("$format_"+key),value,key,(String)parameters.get("$replaceOnBlank_"+key),(String)parameters.get("$replaceOnErrorFormat_"+key));
								if(makedValue==null) makedValue=util_makeValue.makeFormatedValue1(this,(String)parameters.get("$format_"+key),value,key,(String)parameters.get("$replaceOnBlank_"+key),(String)parameters.get("$replaceOnErrorFormat_"+key));
							}else{
								makedValue=util_makeValue.makeFormatedValue1(this,(String)parameters.get("$format_"+key),value,key,(String)parameters.get("$replaceOnBlank_"+key),(String)parameters.get("$replaceOnErrorFormat_"+key));
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

							if(parameters.get("$format_"+key)!=null){
								if(delegated!=null){
									makedValue=util_makeValue.makeFormatedValue(delegated,(String)parameters.get("$format_"+key),value,getCampoValue(key),(String)parameters.get("$replaceOnBlank_"+key),(String)parameters.get("$replaceOnErrorFormat_"+key));
									if(makedValue==null) makedValue=util_makeValue.makeFormatedValue(this,(String)parameters.get("$format_"+key),value,getCampoValue(key),(String)parameters.get("$replaceOnBlank_"+key),(String)parameters.get("$replaceOnErrorFormat_"+key));
								}else{
									makedValue=util_makeValue.makeFormatedValue(this,(String)parameters.get("$format_"+key),value,getCampoValue(key),(String)parameters.get("$replaceOnBlank_"+key),(String)parameters.get("$replaceOnErrorFormat_"+key));
								}
							}else makedValue=util_makeValue.makeValue(value,getCampoValue(key));

							setCampoValueWithPoint(key,makedValue);
						}catch(Exception ex){
							if(parametersFly==null) parametersFly = new HashMap();
							if(key!=null && key.length()>0 && key.indexOf(0)!='$') parametersFly.put(key, value);
						}
					}

/*
					try{
						if(parameters.get("$format_"+key)!=null)
							setCampoValueWithPoint(key,util_makeValue.makeFormatedValue1((delegated==null)?this:delegated,(String)parameters.get("$format_"+key),value,key,(String)parameters.get("$replaceOnBlank_"+key),(String)parameters.get("$replaceOnErrorFormat_"+key)));
						else setCampoValueWithPoint(key,util_makeValue.makeValue1(this,value,key));
					}catch(Exception e){
						try{
							if(parameters.get("$format_"+key)!=null)
								setCampoValueWithPoint(key,util_makeValue.makeFormatedValue((delegated==null)?this:delegated,(String)parameters.get("$format_"+key),value,getCampoValue(key),(String)parameters.get("$replaceOnBlank_"+key),(String)parameters.get("$replaceOnErrorFormat_"+key)));
							else setCampoValueWithPoint(key,util_makeValue.makeValue(value,getCampoValue(key)));
						}catch(Exception ex){
							if(parametersFly==null) parametersFly = new HashMap();
							if(key!=null && key.length()>0 && key.indexOf(0)!='$') parametersFly.put(key, value);
						}
					}
*/
				}else{
					StringTokenizer st = new StringTokenizer(key,".");
					Vector allfields=new Vector();
					while(st.hasMoreTokens()){
						String current_field_name = st.nextToken();
						allfields.add(current_field_name);
					}

					Object writeValue=null;
					Object current_requested = (delegated==null)?this:delegated;


					for(int i=0;i<allfields.size()-1;i++){
						String current_field_name = (String)allfields.get(i);
						try{
							writeValue = util_reflect.getValue(current_requested,"get"+util_reflect.adaptMethodName(current_field_name),null);
							if(writeValue==null) writeValue = util_reflect.getValue(current_requested,current_field_name,null);
							if(writeValue==null && current_requested instanceof i_bean) writeValue = ((i_bean)current_requested).get(current_field_name);
							if(writeValue==null && current_requested instanceof HashMap) writeValue = ((HashMap)current_requested).get(current_field_name);
							
						}catch(Exception e){
						}
						current_requested = writeValue;
					}
					String current_field_name = (String)allfields.get(allfields.size()-1);
					try{
						if(parameters.get("$format_"+key)!=null)
							setCampoValuePoint(
									current_requested,
									current_field_name,
									util_makeValue.makeFormatedValue1(
												current_requested,
												(String)parameters.get("$format_"+key),
												value,
												current_field_name,
												(String)parameters.get("$replaceOnBlank_"+key),
												(String)parameters.get("$replaceOnErrorFormat_"+key)
									)
							);
						else setCampoValuePoint(
									current_requested,
									current_field_name,
									util_makeValue.makeValue1(current_requested,value,current_field_name)
								);
					}catch(Exception e){
						try{
							if(parameters.get("$format_"+key)!=null)
								setCampoValuePoint(current_requested,key,util_makeValue.makeFormatedValue((delegated==null)?this:delegated,(String)parameters.get("$format_"+key),value,getCampoValue(key),(String)parameters.get("$replaceOnBlank_"+key),(String)parameters.get("$replaceOnErrorFormat_"+key)));
							else setCampoValuePoint(current_requested,key,util_makeValue.makeValue(value,getCampoValue(key)));
						}catch(Exception ex){
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
	
	Object[] keys = _content.keySet().toArray();
	for (int ii = 0; ii < keys.length; ii++){
		String key = (String)keys[ii];
		String value = (String)_content.get(key);

		if(key.indexOf(".")==-1){
			try{

				Object makedValue=null;
				if(_content.get("$format_"+key)!=null){
					if(delegated!=null){
						makedValue=util_makeValue.makeFormatedValue1(delegated,(String)_content.get("$format_"+key),value,key,(String)_content.get("$replaceOnBlank_"+key),(String)_content.get("$replaceOnErrorFormat_"+key));
						if(makedValue==null) makedValue=util_makeValue.makeFormatedValue1(this,(String)_content.get("$format_"+key),value,key,(String)_content.get("$replaceOnBlank_"+key),(String)_content.get("$replaceOnErrorFormat_"+key));
					}else{
						makedValue=util_makeValue.makeFormatedValue1(this,(String)_content.get("$format_"+key),value,key,(String)_content.get("$replaceOnBlank_"+key),(String)_content.get("$replaceOnErrorFormat_"+key));
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

					if(_content.get("$format_"+key)!=null){
						if(delegated!=null){
							makedValue=util_makeValue.makeFormatedValue(delegated,(String)_content.get("$format_"+key),value,getCampoValue(key),(String)_content.get("$replaceOnBlank_"+key),(String)_content.get("$replaceOnErrorFormat_"+key));
							if(makedValue==null) makedValue=util_makeValue.makeFormatedValue(this,(String)_content.get("$format_"+key),value,getCampoValue(key),(String)_content.get("$replaceOnBlank_"+key),(String)_content.get("$replaceOnErrorFormat_"+key));
						}else{
							makedValue=util_makeValue.makeFormatedValue(this,(String)_content.get("$format_"+key),value,getCampoValue(key),(String)_content.get("$replaceOnBlank_"+key),(String)_content.get("$replaceOnErrorFormat_"+key));
						}
					}else makedValue=util_makeValue.makeValue(value,getCampoValue(key));

					setCampoValueWithPoint(key,makedValue);
				}catch(Exception ex){
					if(parametersFly==null) parametersFly = new HashMap();
					if(key!=null && key.length()>0 && key.indexOf(0)!='$') parametersFly.put(key, value);
				}
			}
		}else{
			StringTokenizer st = new StringTokenizer(key,".");
			Vector allfields=new Vector();
			while(st.hasMoreTokens()){
				String current_field_name = st.nextToken();
				allfields.add(current_field_name);
			}

			Object writeValue=null;
			Object current_requested = (delegated==null)?this:delegated;


			for(int i=0;i<allfields.size()-1;i++){
				String current_field_name = (String)allfields.get(i);
				try{
					writeValue = util_reflect.getValue(current_requested,"get"+util_reflect.adaptMethodName(current_field_name),null);
					if(writeValue==null) writeValue = util_reflect.getValue(current_requested,current_field_name,null);
					if(writeValue==null && current_requested instanceof HashMap){
						writeValue = ((HashMap)current_requested).get(current_field_name);
					}
				}catch(Exception e){
				}
				current_requested = writeValue;
			}
			String current_field_name = (String)allfields.get(allfields.size()-1);
			try{
				if(_content.get("$format_"+key)!=null)
					setCampoValuePoint(
							current_requested,
							current_field_name,
							util_makeValue.makeFormatedValue1(
										current_requested,
										(String)_content.get("$format_"+key),
										value,
										current_field_name,
										(String)_content.get("$replaceOnBlank_"+key),
										(String)_content.get("$replaceOnErrorFormat_"+key)
							)
					);
				else setCampoValuePoint(
							current_requested,
							current_field_name,
							util_makeValue.makeValue1(current_requested,value,current_field_name)
						);
			}catch(Exception e){
				try{
					if(_content.get("$format_"+key)!=null)
						setCampoValuePoint(current_requested,key,util_makeValue.makeFormatedValue((delegated==null)?this:delegated,(String)_content.get("$format_"+key),value,getCampoValue(key),(String)_content.get("$replaceOnBlank_"+key),(String)_content.get("$replaceOnErrorFormat_"+key)));
					else setCampoValuePoint(current_requested,key,util_makeValue.makeValue(value,getCampoValue(key)));
				}catch(Exception ex){
				}
			}
		}


/*
		try{
			if(_content.get("$format_"+key)!=null)
				setCampoValueWithPoint(key,util_makeValue.makeFormatedValue1((delegated==null)?this:delegated,(String)_content.get("$format_"+key),value,key,(String)_content.get("$replaceOnBlank_"+key),(String)_content.get("$replaceOnErrorFormat_"+key)));
			else setCampoValueWithPoint(key,util_makeValue.makeValue1((delegated==null)?this:delegated,value,key));
		}catch(Exception e){
			try{
				if(_content.get("$format_"+key)!=null)
					setCampoValueWithPoint(key,util_makeValue.makeFormatedValue((delegated==null)?this:delegated,(String)_content.get("$format_"+key),value,getCampoValue(key),(String)_content.get("$replaceOnBlank_"+key),(String)_content.get("$replaceOnErrorFormat_"+key)));
				else setCampoValueWithPoint(key,util_makeValue.makeValue(value,getCampoValue(key)));
			}catch(Exception ex){
				if(parametersFly==null) parametersFly = new HashMap();
				if(key!=null && key.length()>0 && key.indexOf(0)!='$') parametersFly.put(key, value);
			}
		}
*/
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
	if(req instanceof HashMap){
		((HashMap)req).put(nome, value);
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
		Vector allfields=new Vector();
		while(st.hasMoreTokens()){
			String current_field_name = st.nextToken();
			allfields.add(current_field_name);
		}
		String complexName="";
		for(int i=0;i<allfields.size()-1;i++){
			complexName+=allfields.get(i);
			if(i!=allfields.size()-2) complexName+=".";
		}

		Object writeObj=get(complexName);
		if(writeObj==null) return primArgument;
		name = (String)allfields.get(allfields.size()-1);
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
				boolean res = setValue(delegated, "set"+util_reflect.adaptMethodName(name.trim()),new Object[]{value},false);;
				if(!res) res = setValue(this, "set"+util_reflect.adaptMethodName(name.trim()),new Object[]{value},false);;
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
			Vector allfields=new Vector();
			while(st.hasMoreTokens()){
				String current_field_name = st.nextToken();
				allfields.add(current_field_name);
			}
			String complexName="";
			for(int i=0;i<allfields.size()-1;i++){
				complexName+=allfields.get(i);
				if(i!=allfields.size()-2) complexName+=".";
			}

			Object writeObj=get(complexName);
			if(writeObj==null) writeObj=get(this,complexName);
			if(writeObj==null) return false;
			String current_field_name = (String)allfields.get(allfields.size()-1);
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
	middleAction = string;
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



}
