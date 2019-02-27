package it.classhidra.core.tool.util;



import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_ProviderWrapper;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.info_navigation;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.serialize.Serialized;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;


public class util_reflect {

	private static StringBuffer logString = new StringBuffer();
	private Object object;
	private int leng;
	private int indexOfList;
	private static boolean error = false;
	
public util_reflect() {
	super();
}


public static synchronized String prepareClassInfo(String name){
	try{
		name = name.substring(name.lastIndexOf("."),name.length());
	}catch(Exception e){
	}

	return prepareClassInfo(new String[]{name+".java"},new String[]{});
}

public static synchronized String prepareClassInfo(final String[] javaNames, final String[] methodNames){
	String classInfo="";
//	StackTraceElement ste = null;
	int counter=0;
	try{
		if(javaNames.length==0 || methodNames.length==0){
			final Object[] result = forPrepareClassInfo("","",counter);
			if(result!=null){
				counter = ((Integer)result[0]).intValue();
				classInfo = ((StackTraceElement)result[1]).getClassName()+":"+((StackTraceElement)result[1]).getMethodName();
			}
		}else{

			for(int i=0;i<javaNames.length;i++){
				final Object[] result = forPrepareClassInfo(javaNames[i],methodNames[i],counter);
				if(result!=null){
					counter = ((Integer)result[0]).intValue();
					classInfo = ((StackTraceElement)result[1]).getClassName()+":"+((StackTraceElement)result[1]).getMethodName();
				}
			}
		}
	}catch(Exception e){
		new bsException(e,iStub.log_ERROR);
	}
	return classInfo;
}

private static synchronized Object[] forPrepareClassInfo(final String javaName, final String methodName, final int counter){
	final Object[] result = new Object[2];
	try{
//		StackTraceElement ste = null;
		int i=counter;


		/*
		 * JDK 1.5
				while (i<Thread.currentThread().getStackTrace().length && ste==null){
					if(	Thread.currentThread().getStackTrace()[i].getFileName() != null &&
						Thread.currentThread().getStackTrace()[i].getFileName().equals(javaName) &&
						Thread.currentThread().getStackTrace()[i].getMethodName().equals(methodName)){
						result[0]= Integer.valueOf(i+1);
						result[1] = Thread.currentThread().getStackTrace()[i+1];
						return result;
					}
					i++;
				}
		*/
		//JDK 1.4
		final StackTraceElement[] stel = new Exception().getStackTrace();
	
				while (i<stel.length){
					if(	stel[i].getFileName() != null &&
						stel[i].getFileName().equals(javaName) &&
						stel[i].getMethodName().equals(methodName)){
						result[0]= Integer.valueOf(i+1);
						result[1] = stel[i+1];
						return result;
					}
					i++;
				}
	}catch(Exception e){
	}

	return null;
}

public static synchronized int countPresentClassInfo(String javaName, String methodName){
	int count = 0;
	try{
				for (int i=0;i<new Exception().getStackTrace().length;i++){
					if(	new Exception().getStackTrace()[i].getFileName() != null &&
						new Exception().getStackTrace()[i].getFileName().equals(javaName) &&
						new Exception().getStackTrace()[i].getMethodName().equals(methodName)){
						count++;
					}
				}
	}catch(Exception e){
	}

	return count;
}


public static Method getMethod(String name, Object obj) throws Exception {
	Method mtd[] = getMethods(obj);
	int i = findMethod(name,mtd);
	if (i<0) return null;
	return mtd[i];
}
public static java.lang.reflect.Method[] getMethods(Object obj) throws Exception {
	Method mtd[] = null;
	try {
		mtd = obj.getClass().getMethods();	//prelevo tutti i metodi della classe corrente obj
	} catch(SecurityException e) {
		errorMsg("getMethod","L'accesso alle informazioni della classe "+obj.getClass().getName()+" � negato.",e);
	}
	return mtd;
}
public static java.lang.reflect.Method[] getMethods(Object obj, String s) throws Exception {
	Method mtd[] = null;
	Method retVal[] = null;
	try {
		mtd = obj.getClass().getMethods(); //prelevo tutti i metodi della classe corrente obj
	} catch (SecurityException e) {
		errorMsg("getMethod", "L'accesso alle informazioni della classe " + obj.getClass().getName() + " � negato.", e);
	}
	int count = 0;
	for (int i = 0; i < mtd.length; i++) {
		if (mtd[i].getName().startsWith(s)) {
			count++;
		}
	}
	retVal = new Method[count];
	count = 0;
	for (int i = 0; i < mtd.length; i++) {
		if (mtd[i].getName().startsWith(s)) {
			retVal[count] = mtd[i];
			count++;
		}
	}
	return retVal;
}
public static void errorMsg(String location, String msg, Exception e) throws Exception {
	String errmsg = "Messagge: " + msg ;
	throw new Exception(errmsg);
}

public static Object execStaticMethod(String class_name, String method_name, Class<?>[] arg, Object[] par){
	try{
		Class<?> c_provider = Class.forName(class_name);
		try{
			Method m_getInstance = c_provider.getDeclaredMethod(method_name, arg);
			return m_getInstance.invoke(null, par);
		}catch(Exception e){				
		}
	}catch (Exception e) {
		return null;
	}catch (Throwable e) {
		return null;
	}
	return null;
}

public static int findMethod(String name, Method mtd[]) throws Exception {
	if ((mtd == null) || (name == null)) {
		errorMsg("findMethod", "Error input parameter:\nnome: " + name + "\nmethod: " + mtd, null);
		return -1;
	}
	for (int i = 0; i < mtd.length; i++)
		if (mtd[i].getName().equals(name))
			return i;
	String elenco = "";
	for (int i = 0; i < mtd.length; i++) {
		elenco += mtd[i].getName() + "\n";
	}
	errorMsg("findMethod", "Not found any method with name: " + name + " into:\n" + elenco, null);
	return -1;
}
public static java.lang.reflect.Constructor<?>[] getConstructors(Object obj, String s) throws Exception {
	Constructor<?> mtd[] = null;
	Constructor<?> retVal[] = null;
	try {
		mtd = obj.getClass().getConstructors(); 
	} catch (SecurityException e) {
		errorMsg("getMethod", "Error access class " + obj.getClass().getName() , e);
	}
	int count = 0;
	for (int i = 0; i < mtd.length; i++) {
		if (mtd[i].getName().startsWith(s)) {
			count++;
		}
	}
	retVal = new Constructor[count];
	count = 0;
	for (int i = 0; i < mtd.length; i++) {
		if (mtd[i].getName().startsWith(s)) {
			retVal[count] = mtd[i];
			count++;
		}
	}
	return retVal;
}


public static Object getValue(Object requested, String nome, Object[] value) throws Exception{
	if (nome == null || nome.trim().length()==0 || requested==null) return null;
	if(value==null) value = new Object[0];
	Object resultObject = null;
	try{
		java.lang.reflect.Method mtd = null;
		Class<?>[] cls = new Class[value.length];
		for(int i=0;i<value.length;i++) cls[i]=value[i].getClass();
		mtd = requested.getClass().getMethod(nome,cls);
		if(mtd==null) return null;
		resultObject =mtd.invoke(requested, value);
	}catch(Exception e){
		java.lang.reflect.Method mtd = null;
		if(mtd==null){
			Method[] methods = requested.getClass().getMethods();
			for(int i=0;i<methods.length;i++) {				
		        if (nome.equals(methods[i].getName())) {
		        	Class<?>[] parTypes = methods[i].getParameterTypes();
		        	boolean isCorrect = true;
		        	if(parTypes.length==value.length){
		        		int j=0;
			        	while(j<parTypes.length && isCorrect){
			        		if(!parTypes[j].isAssignableFrom(value[j].getClass()))
			        			isCorrect = false;
			        		j++;
			        	}
			        	if(isCorrect){
			        		try{
			        			resultObject = methods[i].invoke(requested, value);
			        			return resultObject;
			        		}catch(Exception ex){			        			
			        		}
			        	}
		        	}
		        }
		    }			
		}				
		
		Class<?>[] cls = new Class[value.length];
		for(int i=0;i<value.length;i++) cls[i]=value[i].getClass();
		int maxCount = 0;
		boolean fine = false;
		while(!fine && maxCount<20){
			int count = 0;
			for(int i=0;i<cls.length;i++){
				if(cls[i]!=null && !cls[i].isPrimitive() && !cls[i].getName().equals("java.lang.Object")){
					boolean corrected=false;
					if(!(corrected) && cls[i].getName().indexOf(".Long")>0){
						cls[i] = long.class;
						corrected = true;
					}
					if(!(corrected) && cls[i].getName().indexOf(".Integer")>0){
						cls[i] = int.class;
						corrected = true;
					}
					if(!(corrected) && cls[i].getName().indexOf(".Short")>0){
						cls[i] = short.class;
						corrected = true;
					}
					if(!(corrected) && cls[i].getName().indexOf(".Float")>0){
						cls[i] = float.class;
						corrected = true;
					}
					if(!(corrected) && cls[i].getName().indexOf(".Double")>0){
						cls[i] = double.class;
						corrected = true;
					}
					if(!(corrected) && cls[i].getName().indexOf(".Byte")>0){
						cls[i] = byte.class;
						corrected = true;
					}
					if(!(corrected) && cls[i].getName().indexOf(".Boolean")>0){
						cls[i] = boolean.class;
						corrected = true;
					}
					if(!(corrected) && cls[i].getName().indexOf(".Character")>0){
						cls[i] = char.class;
						corrected = true;
					}
//					if(!corrected)cls[i] = cls[i].getSuperclass();
					count++;
				}
			}
			try{
				mtd = requested.getClass().getMethod(nome.trim(),cls);
				resultObject =mtd.invoke(requested, value);

			}catch(Exception ex){
				mtd = null;
			}
			if(count>0 && mtd==null) fine = false;
			else fine = true;
			maxCount++;
		}

	}
	return  resultObject;
}

public static Object getValue(Object requested,Method mtd, Object[] value) throws Exception{
	if (mtd == null || requested==null) return null;
	if(value==null) value = new Object[0];
	Object resultObject = null;
	try{

		Class<?>[] cls = new Class[value.length];
		for(int i=0;i<value.length;i++){
			if(value[i]!=null)
				cls[i]=value[i].getClass();
		}
		resultObject =mtd.invoke(requested, value);
	}catch(Exception e){
	}
	return  resultObject;
}

public static Object getValue(Object requested,Method mtd, Object[] value, Class<?>[] cls) throws Exception{
	if (mtd == null || requested==null) return null;
	if(value==null) value = new Object[0];
	if(cls==null) cls = new Class[0];
	Object resultObject = null;
	try{
		resultObject =mtd.invoke(requested, value);
	}catch(Exception e){
	}
	return  resultObject;
}


public static Object getValueMethodName(Object requested, String nome, Object[] value) throws Exception{
	if (nome == null || nome.trim().length()==0 || requested==null) return null;
	if(value==null) value = new Object[0];
	Object resultObject = null;
	try{
		java.lang.reflect.Method mtd = null;
		Class<?>[] cls = new Class[value.length];
		for(int i=0;i<value.length;i++){
			if(value[i]!=null)
				cls[i]=value[i].getClass();
		}
		mtd = getMethodName(requested,nome,cls);
		if(mtd==null) return null;
		resultObject =mtd.invoke(requested, value);
	}catch(Exception e){
	}
	return  resultObject;
}

public static java.lang.reflect.Method getMethodNull(Object requested, String nome, Class<?>[] cls) throws Exception{
	java.lang.reflect.Method result=null;
	java.lang.reflect.Method[] mtds = requested.getClass().getMethods();
	for(int i=0;i<mtds.length;i++){
		java.lang.reflect.Method current = mtds[i];
		if(nome.equals(current.getName())){
		Class<?>[] par = current.getParameterTypes();
		if(par.length==cls.length){
			boolean isCorrect=true;
			for(int j=0;j<cls.length;j++){
				if(cls[j]!=null){
					if(cls[j].getName().equals(par[j].getName()))
						isCorrect&=true;
					else if(cls[j].isAssignableFrom((par[j])) || par[j].isAssignableFrom((cls[j])))
						isCorrect&=true;
					else
						isCorrect&=false;
				}
			}
			if(isCorrect) return current;
		}
		}
	}
	return result;
}

public static java.lang.reflect.Method getMethodName(Object requested, String nome, Class<?>[] cls) throws Exception{
	java.lang.reflect.Method result=null;
	java.lang.reflect.Method[] mtds = requested.getClass().getMethods();
	for(int i=0;i<mtds.length;i++){
		java.lang.reflect.Method current = mtds[i];
		Class<?>[] par = current.getParameterTypes();
		if(nome.equals(current.getName()) && par.length==cls.length)
			return current;
	}
	return result;
}




public static boolean setValue(Object requested, String nome, Object[] value) throws Exception{
	return setValue(requested, nome, value,true);
}

public static boolean setValue(Object requested, String nome, Object[] value, boolean writeLog) throws Exception{
	if (nome == null || nome.trim().length()==0 || requested==null) return false;
	Class<?>[] cls = null;
	java.lang.reflect.Method mtd = null;
	if(value==null)	value = new Object[0];
/*
	if(value.length==1 && value[0]==null){
		try{
			cls = new Class[1];
			cls[0] = null;
			mtd = getMethodNull(requested, nome, cls);
			if(mtd==null) return false;
			mtd.invoke(requested, value);
			return true;
		}catch(Exception e){
			return false;
		}
	}
*/
	try{

		cls = new Class[value.length];
//		boolean classIsNull = false;
		for(int i=0;i<value.length;i++){
			if(value[i]==null){
				cls[i]=null;
//				classIsNull=true;
			}
			else cls[i]=value[i].getClass();
		}
//		if(classIsNull)  mtd = getMethodNull(requested, nome, cls);
//		else
			mtd = requested.getClass().getMethod(nome,cls);
		if(mtd==null) return false;
		mtd.invoke(requested, value);
	}catch(Exception e){
		mtd = null;
		if(mtd==null){
			Method[] methods = requested.getClass().getMethods();
			for(int i=0;i<methods.length;i++) {				
		        if (nome.equals(methods[i].getName())) {
		        	Class<?>[] parTypes = methods[i].getParameterTypes();
		        	boolean isCorrect = true;
		        	if(parTypes.length==value.length){
		        		int j=0;
			        	while(j<parTypes.length && isCorrect){
			        		if(value[j]!=null && !parTypes[j].isAssignableFrom(value[j].getClass()))
			        			isCorrect = false;
			        		j++;
			        	}
			        	if(isCorrect){
			        		try{
			        			methods[i].invoke(requested, value);
			        			return true;
			        		}catch(Exception ex){			        			
			        		}
			        	}
		        	}
		        }
		    }			
		}				
		
		cls = new Class[value.length];
		for(int i=0;i<value.length;i++) cls[i]=value[i].getClass();
		int maxCount = 0;
		boolean fine = false;
		while(!fine && maxCount<20){
			int count = 0;
			for(int i=0;i<cls.length;i++){
				if(cls[i]!=null && !cls[i].isPrimitive() && !cls[i].getName().equals("java.lang.Object")){
					boolean corrected=false;
					if(!(corrected) && cls[i].getName().indexOf(".Long")>0){
						cls[i] = long.class;
						corrected = true;
					}
					if(!(corrected) && cls[i].getName().indexOf(".Integer")>0){
						cls[i] = int.class;
						corrected = true;
					}
					if(!(corrected) && cls[i].getName().indexOf(".Short")>0){
						cls[i] = short.class;
						corrected = true;
					}
					if(!(corrected) && cls[i].getName().indexOf(".Float")>0){
						cls[i] = float.class;
						corrected = true;
					}
					if(!(corrected) && cls[i].getName().indexOf(".Double")>0){
						cls[i] = double.class;
						corrected = true;
					}
					if(!(corrected) && cls[i].getName().indexOf(".Byte")>0){
						cls[i] = byte.class;
						corrected = true;
					}
					if(!(corrected) && cls[i].getName().indexOf(".Boolean")>0){
						cls[i] = boolean.class;
						corrected = true;
					}
					if(!(corrected) && cls[i].getName().indexOf(".Character")>0){
						cls[i] = char.class;
						corrected = true;
					}
//					if(!corrected)cls[i] = cls[i].getSuperclass();
					count++;
				}
			}
			try{
				mtd = requested.getClass().getMethod(nome.trim(),cls);
			}catch(Exception ex){
				mtd = null;
			}
			if(count>0 && mtd==null) fine = false;
			else fine = true;
			maxCount++;
		}
		if(mtd==null){
			for(int i=0;i<value.length;i++) cls[i]=Object.class;
			try{
				mtd = requested.getClass().getMethod(nome.trim(),cls);
			}catch(Exception ex){
				mtd = null;
			}
		}
/*		
		if(mtd==null){
			Method[] methods = requested.getClass().getMethods();
			for(int i=0;i<methods.length;i++) {				
		        if (nome.equals(methods[i].getName())) {
		        	Class[] parTypes = methods[i].getParameterTypes();
		        	boolean isCorrect = true;
		        	if(parTypes.length==value.length){
		        		int j=0;
			        	while(j<parTypes.length && isCorrect){
			        		if(!parTypes[j].isAssignableFrom(value[j].getClass()))
			        			isCorrect = false;
			        		j++;
			        	}
			        	if(isCorrect){
			        		try{
			        			methods[i].invoke(requested, value);
			        			return true;
			        		}catch(Exception ex){			        			
			        		}
			        	}
		        	}
		        }
		    }			
		}		
*/		
		if(mtd==null){
			String types="";
			if(cls!=null){
				for(int k=0;k<cls.length;k++) types+=cls[k]+",";
			}
			if(nome!=null && !nome.toLowerCase().equals("setclass")){
				if(writeLog) bsController.writeLog("util_reflect->setValue: class: ["+requested.getClass().getName()+"] method: ["+nome+"("+types+")] not found. sorry.",iStub.log_DEBUG);
			}
			return false;
		}

		
		mtd.invoke(requested, value);

	}
	return true;
}

public static boolean setValue_BasedOnlyCountParameters(Object requested, String nome, Object[] value) throws Exception{
	if (nome == null || nome.trim().length()==0 || requested==null) return false;
	if(value==null)	value = new Object[0];
	try{
		Method[] methods = requested.getClass().getMethods();
		for(int j=0;j<methods.length;j++){
			Method current = methods[j];
			if(current.getParameterTypes().length==value.length && nome.equals(current.getName())){
				current.invoke(requested, value);
				return true;
			}
		}
	}catch(Exception e){
		return false;
	}
	return true;
}

public static Object getValue_BasedOnlyCountParameters(Object requested, String nome, Object[] value) throws Exception{
	if (nome == null || nome.trim().length()==0 || requested==null) return null;
	Object resultObject = null;
	if(value==null)	value = new Object[0];
	try{
		Method[] methods = requested.getClass().getMethods();
		for(int j=0;j<methods.length;j++){
			Method current = methods[j];
			if(current.getParameterTypes().length==value.length && nome.equals(current.getName())){
				resultObject = current.invoke(requested, value);
				return resultObject;
			}
		}
	}catch(Exception e){
		return resultObject;
	}
	return resultObject;
}
public static String adaptMethodName(String value){
	if(value==null) return "";
	String result="";
	if(value.length()>1) result=value.substring(0,1).toUpperCase()+value.substring(1);
	else result = value.toUpperCase();
	return result;
}
public static String revAdaptMethodName(String value){
	if(value==null) return "";
	String result="";
	if(value.length()>1) result=value.substring(0,1).toLowerCase()+value.substring(1);
	else result = value.toLowerCase();
	return result;
}


@SuppressWarnings("rawtypes")
public static Object prepareWriteValueForTag(Object requested, String method_prefix,String field_name, Object[] parameters){
	if(requested==null || field_name==null) return null;

	if(requested instanceof i_bean && ((i_bean)requested).getDelegated()!=null) requested=((i_bean)requested).getDelegated();

	if(method_prefix==null) method_prefix="";

	Object writeValue=null;

	if(requested instanceof String ) return requested;

	HashMap<String,String> syn = null;
	if(field_name.indexOf("'")>-1){
		syn = new HashMap<String, String>();
		String tmp ="";
		String field ="";
		boolean startAp=false;
		int cnt=0;
		for(int i=0;i<field_name.length();i++){
			if(field_name.charAt(i)=='\''){
				if(startAp){
					tmp+="$"+cnt+"$";
					syn.put("$"+cnt+"$", field);
					cnt++;
					startAp=false;
					field="";
				}else{
					startAp=true;
					field="";
				}
			}else{
				if(startAp)
					field+=field_name.charAt(i);
				else
					tmp+=field_name.charAt(i);				
			}				
		}
		field_name=tmp;
	}
	
//	StringTokenizer st = new StringTokenizer(field_name,".");
	String[] tokens = field_name.split("\\.");
	if(tokens.length==0 && field_name.length()>0)
		 tokens = new String[] {field_name};
	Object current_requested = requested;
	

	
//	while(st.hasMoreTokens()){
	for(int t=0;t<tokens.length;t++) {	
		try{
			if(	current_requested!=null && current_requested instanceof i_bean)
				current_requested = ((i_bean)current_requested).asBean();
		}catch(Exception e){
		}
		
		try{
			if(	current_requested!=null && current_requested instanceof i_ProviderWrapper)
				current_requested = ((i_ProviderWrapper)current_requested).getInstance();
		}catch(Exception e){
		}
		
		
		
//		String current_field_name = st.nextToken();
		String current_field_name = tokens[t];
		
		if(syn!=null && syn.get(current_field_name)!=null)
			current_field_name=(String)syn.get(current_field_name);
		
		int int4list=-1;
		try{
			if(current_requested!=null && current_requested instanceof AbstractList){
				int4list = Integer.valueOf(current_field_name).intValue();
			}
		}catch(Exception e){			
		}
		try{
			boolean isException=false;
			if(current_requested!=null && current_requested instanceof Map<?,?>){
				try{
					writeValue = ((Map)current_requested).get(current_field_name);
				}catch(Exception ex){
					isException=true;
				}
			}else  if(current_requested!=null && current_requested instanceof List<?> && int4list>-1){
				try{
					writeValue = ((List)current_requested).get(int4list);
				}catch(Exception ex){
					isException=true;
				}
			}else  if(current_requested!=null && current_requested instanceof Set<?> && int4list>-1){
				try{
					writeValue = Arrays.asList(((Set)current_requested)).toArray()[int4list];
				}catch(Exception ex){
					isException=true;
				}
			}else if(current_requested!=null && current_requested.getClass().isArray() && int4list>-1){
				try{
					Class<?> componentType = current_requested.getClass().getComponentType();
					if(!componentType.isPrimitive())
						writeValue = ((Object[])current_requested)[int4list];
					else{
						if (boolean.class.isAssignableFrom(componentType))
							writeValue = ((boolean[])current_requested)[int4list]; 
						else if (byte.class.isAssignableFrom(componentType))
							writeValue = ((byte[])current_requested)[int4list]; 
						else if (char.class.isAssignableFrom(componentType))
							writeValue = ((char[])current_requested)[int4list]; 
						else if (double.class.isAssignableFrom(componentType))
							writeValue = ((double[])current_requested)[int4list]; 
						else if (float.class.isAssignableFrom(componentType))
							writeValue = ((float[])current_requested)[int4list]; 
						else if (int.class.isAssignableFrom(componentType))
							writeValue = ((int[])current_requested)[int4list]; 
						else if (long.class.isAssignableFrom(componentType))
							writeValue = ((long[])current_requested)[int4list]; 
						else if (short.class.isAssignableFrom(componentType))
							writeValue = ((short[])current_requested)[int4list]; 
					}
				}catch(Exception ex){
					isException=true;
				}
			}else if(current_requested!=null && current_requested instanceof info_navigation){
				try{
					
					writeValue = util_reflect.getValue(current_requested,method_prefix+util_reflect.adaptMethodName(current_field_name),(t==tokens.length-1)?parameters:null);
					if(writeValue==null && method_prefix.equals("get"))
						writeValue = util_reflect.getValue(current_requested,"is"+util_reflect.adaptMethodName(current_field_name),(t==tokens.length-1)?parameters:null);

				}catch (Exception e) {
				}
				if(writeValue==null){
					try{
						writeValue = ((info_navigation)current_requested).get(current_field_name);
					}catch(Exception ex){
						isException=true;
					}
				}

			}else{
				writeValue = util_reflect.getValue(current_requested,method_prefix+util_reflect.adaptMethodName(current_field_name),(t==tokens.length-1)?parameters:null);
				if(writeValue==null && method_prefix.equals("get"))
					writeValue = util_reflect.getValue(current_requested,"is"+util_reflect.adaptMethodName(current_field_name),(t==tokens.length-1)?parameters:null);

			}
			if(isException){
				writeValue = util_reflect.getValue(current_requested,method_prefix+util_reflect.adaptMethodName(current_field_name),(t==tokens.length-1)?parameters:null);
				if(writeValue==null && method_prefix.equals("get"))
					writeValue = util_reflect.getValue(current_requested,"is"+util_reflect.adaptMethodName(current_field_name),(t==tokens.length-1)?parameters:null);

			}
			if(writeValue==null) writeValue = util_reflect.getValue(current_requested,current_field_name,(t==tokens.length-1)?parameters:null);
		}catch(Exception e){
		}

		if(writeValue==null){
			try{
				writeValue = ((i_bean)requested).getParametersFly().get(current_field_name);
			}catch(Exception e){
			}
		}
		current_requested = writeValue;
//		writeValue = null;
	}

	if(writeValue==null){
		try{
			writeValue = ((i_bean)requested).getParametersFly().get(field_name);
		}catch(Exception e){
		}
	}

	return writeValue;
}

public static Object prepareWriteValueFromBean(String fromBean, HttpServletRequest request, i_bean formBean){
	Object result=null;
	if(fromBean!=null ){
		try{
			Object rightBean = null;
			String nameRightBean = "";
			String methodRightBean = "";
			StringTokenizer st = new StringTokenizer(fromBean,".");
			
			while(st.hasMoreTokens()){
				String current = st.nextToken();
				if(nameRightBean.equals("")) nameRightBean=current;
				else{
					if(methodRightBean.equals("")) methodRightBean=current;
					else methodRightBean+="."+current;
				}
			}
			
			if(rightBean==null) rightBean = request.getAttribute(nameRightBean);
			if(rightBean==null) rightBean = request.getSession().getAttribute(nameRightBean);
			try{
				if(rightBean==null) rightBean = ( bsController.getFromInfoNavigation(null, request)).find(nameRightBean).get_content();
			}catch(Exception e){
			}	
			if(rightBean==null) rightBean = bsController.getProperty(nameRightBean,request);
			
			if(rightBean==null){
				methodRightBean = nameRightBean+"."+methodRightBean;
				nameRightBean = "";
				rightBean = formBean;					
			}				
			if(rightBean!=null){
				if(methodRightBean==null || methodRightBean.equals("")) result = rightBean.toString();
				else{
					try{
						result = util_reflect.prepareWriteValueForTag(rightBean,"get",methodRightBean,null).toString();					 
					}catch(Exception e){
					}
				}	
			}		 		
		}catch(Exception e){
		}
	}
	return result;
}



public static Field getField(Object req, String key) throws Exception{
	Field fld = null;
	try{
		fld=req.getClass().getField(key);
	}catch(Exception ex){
	}
	if(fld==null){
		try{
			fld=req.getClass().getDeclaredField(key);
		}catch(Exception ex){
		}			
	}
	return fld;
}

public static Field getField(Class<?> req_class, String key) throws Exception{
	Field fld = null;
	try{
		fld=req_class.getField(key);
	}catch(Exception ex){
	}
	if(fld==null){
		try{
			fld=req_class.getDeclaredField(key);
		}catch(Exception ex){
		}			
	}
	return fld;
}

public static Field getFieldRecursive(Class<?> req_class, String key) throws Exception{
	if(req_class==null || key==null)
		return null;
	Field fld = null;
	try{
		fld=req_class.getField(key);
	}catch(Exception ex){
	}
	if(fld==null){
		try{
			fld=req_class.getDeclaredField(key);
		}catch(Exception ex){
		}			
	}
	if(fld==null && req_class.getSuperclass()!=null)
		return getFieldRecursive(req_class.getSuperclass(), key);
	return fld;
}

@SuppressWarnings("rawtypes")
public static Class<?> getRetClass(Object req, String key) throws Exception{
	Field fld = null;
	Method ret_fld = null;
	Class<?> ret_class = null;
	try{
		fld=req.getClass().getField(key);
	}catch(Exception ex){
	}
	if(fld==null){
		try{
			fld=req.getClass().getDeclaredField(key);
		}catch(Exception ex){
		}			
	}
	if(fld==null){
		try{
			ret_fld = req.getClass().getMethod("get"+util_reflect.adaptMethodName(key),new Class[0]);
		}catch(Exception e){
			if(	req instanceof HashMap &&
				((HashMap)req).get(key)!=null) {
				ret_class = ((HashMap)req).get(key).getClass();
			}
			if(	req instanceof HashMap &&
					((HashMap)req).get(key)==null) {
					ret_class = new String().getClass();
				}
		}
	}

	if(fld!=null) ret_class = fld.getType();
	if(fld==null && ret_fld!=null) ret_class = ret_fld.getReturnType();

	if(ret_class==null) return null;
	return ret_class;
}	

public static Method getSetMethod(Object req, String key) throws Exception{
	Method ret_fld = null;
	try{
		ret_fld = req.getClass().getMethod("set"+util_reflect.adaptMethodName(key),new Class[]{util_reflect.getRetClass(req,key)});
	}catch(Exception e){			
	}
	return ret_fld;
}

//	TODO @Deprecated
public static Object convertType(@SuppressWarnings("rawtypes") Class CTarget, Object source, String format) throws Exception {
	String classNameSource = source.getClass().getName();
	String classNameTarget = CTarget.getName();
	String err = "SourceType: "+classNameSource+", TargetTipe: "+classNameTarget+", format: "+format;
	try {
		if ( classNameSource.equals(classNameTarget) ) {
			if (format==null || format.equals("") ) return source;
			// Questa condizione per il momento � prevista solo per le date!
			return util_format.dataToString( util_format.stringToData((String)source,0), format );
		}
		// Inizio gestione da e verso tipi Date
		if ( classNameSource.equals("java.util.Date") ) { // Source di tipo Date
			if ( classNameTarget.equals("short") || classNameTarget.equals("java.lang.Short") )
				return Short.valueOf( util_format.dataToString((Date)source,format).trim() );
			if ( classNameTarget.equals("int") || classNameTarget.equals("java.lang.Integer") )
				return Integer.valueOf( util_format.dataToString((Date)source,format).trim() );
			if ( classNameTarget.equals("java.lang.String") )
				return util_format.dataToString((Date)source,format);
		}
		if ( classNameTarget.equals("java.util.Date")) { // Target di tipo Date
			if ( classNameSource.equals("short")
			|| classNameSource.equals("java.lang.Short")
			|| classNameSource.equals("int")
			|| classNameSource.equals("java.lang.Integer")
			|| classNameSource.equals("long")
			|| classNameSource.equals("java.lang.Long")
			|| classNameSource.equals("java.lang.String"))	return util_format.stringToData(""+source,format);
		}

		if ( classNameSource.equals("java.lang.Integer") &&
			( classNameTarget.equals("int") ||
			classNameTarget.equals("long") ) ) return source;
		if ( classNameSource.equals("java.lang.Short") &&
			( classNameTarget.equals("short") ||
			classNameTarget.equals("int") ||
			classNameTarget.equals("long") ) ) return source;
		if ( classNameSource.equals("java.lang.Long") &&
			classNameTarget.equals("long") ) return source;
		if ( classNameSource.equals("java.lang.Float") &&
			( classNameTarget.equals("float") ||
			classNameTarget.equals("double") ) ) return source;
		if ( classNameSource.equals("java.lang.Double") && classNameTarget.equals("double") ) return source;
		if ( classNameSource.equals("java.lang.Boolean") && classNameTarget.equals("boolean") ) return source;
		if ( classNameSource.equals("java.lang.Character") && classNameTarget.equals("char") ) return source;

		if ( classNameSource.equals("java.lang.Integer") &&	classNameTarget.equals("short") ) return Short.valueOf(source.toString());
		if ( classNameSource.equals("java.lang.Long") ) {
			if ( classNameTarget.equals("int") ) return Integer.valueOf(source.toString());
			if ( classNameTarget.equals("short") ) return Short.valueOf(source.toString());
		}
		if ( classNameSource.equals("java.lang.Float") && classNameTarget.equals("duble") ) return Double.valueOf(source.toString());

		if ( classNameSource.equals("java.lang.String") ) {
			if ( classNameTarget.equals("int") ) return Integer.valueOf(source.toString());
			if ( classNameTarget.equals("short") ) return Short.valueOf(source.toString());
			if ( classNameTarget.equals("long") ) return Long.valueOf(source.toString());
			if ( classNameTarget.equals("float") ) return Float.valueOf(source.toString());
			if ( classNameTarget.equals("double") ) return Double.valueOf(source.toString());
			if ( classNameTarget.equals("char") ) return new Character(source.toString().charAt(0));
			if ( classNameTarget.equals("boolean") ) return Boolean.valueOf(source.toString());
		}

	} catch( NumberFormatException  e) {
		errorMsg("ConvertType",err,e);
	} catch(ClassCastException e) {
		errorMsg("ConvertType",err,e);
	} catch(Exception e) {
		errorMsg("ConvertType",err,e);
	}
	errorMsg("ConvertType","Conversion between:\n"+err+"\nnot complete.\nPosibility lost datas.",null);
	return source;
}

//TODO @Deprecated
public Field findField(String name, Object obj) throws Exception {
	Field fld = null;
	Field[] allFld= null;
	setObject(obj);
	try {
		allFld = obj.getClass().getFields();
	} catch (SecurityException e) {
		errorMsg("findField","Il campo: "+name+"\ninvocato per l'oggetto tipo: "+obj.getClass().getName()+"\nnon � accessibile.",e);
	} catch(NullPointerException e) {
		errorMsg("findField","Il campo: "+name+" � invocato su un oggetto nullo.",e);
	}
	for (int i=0;i<allFld.length;i++) {
		Class<?> tipo = allFld[i].getType();
		if ( allFld[i].getName().equals(name) ) return allFld[i];
		Object campo = getField(allFld[i],obj);
		if (campo == null) continue;
		if ( !tipo.isPrimitive() ) fld = findFieldArray(name,tipo);
		if ( fld != null) return fld;
	}
	return fld;
}

//TODO @Deprecated
public Field findFieldArray(String name, Object obj) throws Exception {
	Field fld = null;
	Field[] allFld= null;
	setObject(obj);
	try {
		allFld = obj.getClass().getFields();
	} catch (SecurityException e) {
		errorMsg("findFieldArray","Il campo: "+name+"\ninvocato per l'oggetto tipo: "+obj.getClass().getName()+"\nnon � accessibile.",e);
	} catch(NullPointerException e) {
		errorMsg("findFieldArray","Il campo: "+name+" � invocato su un oggetto nullo",e);
	}
	for (int i=0;i<allFld.length;i++) {
		Class<?> tipo = allFld[i].getType();
		if ( tipo.isPrimitive() ) continue;
		if ( tipo.getName().equals("java.lang.String") ) continue;
		if ( allFld[i].getName().equals(name) && allFld[i].getType().isArray() ) return allFld[i];
		Object campo = getField(allFld[i],obj);
		if (campo == null) continue;
		fld = findFieldArray(name,campo);
		if ( fld != null) return fld;
	}
	return fld;
}

//TODO @Deprecated
public Field findFieldList(String name, Object obj) throws Exception {
	//class CopyField { public Object field; };
	//CopyField newField = new CopyField();
	Field fld = null;
	Field[] allFld= null;
	setObject(obj);
	try {
		allFld = obj.getClass().getFields();
	} catch (SecurityException e) {
		errorMsg("findFieldArray","Il campo: "+name+"\ninvocato per l'oggetto tipo: "+obj.getClass().getName()+"\nnon � accessibile.",e);
	} catch(NullPointerException e) {
		errorMsg("findFieldArray","Il campo: "+name+" � invocato su un oggetto nullo.",e);
	}
	for (int i=0;i<allFld.length;i++) {	// Inizia la ricerca in tutti i campi trovati
		if ( allFld[i].getName().equals(name) ) { // verifica se il nome � quello cercato
			if ( allFld[i].getType().isArray() ) { // e un array?
				setLeng( Array.getLength( getFieldOnly(allFld[i],obj) ) );
				return allFld[i];
			}
			return allFld[i];
		}
		Class<?> tipo = allFld[i].getType(); // ne deternina il tipo
		if ( tipo.isPrimitive() || tipo.getName().equals("java.lang.String") ) continue; // scarta i tipi string e i tipi primitivi
		Object campo = getField(allFld[i],obj); // altrimenti ne calcola il valore
		if (campo == null) continue; // verifica che sia diverso da null
		if ( campo.getClass().isArray() ) { // e un array?
			setLeng(Array.getLength(campo));// ne determina la lunghezza
			campo = Array.get(campo,indexOfList); // altrimenti ne calcola il valore
			if (campo != null) fld = findFieldList(name,campo); // verifica che sia diverso da null
		} else fld = findFieldList(name,campo);
		// verifica se il campo calcolato appartiene a una classe che pu� contenere, a sua volta, il vettore cercato
		if ( fld != null) return fld;
	}
	return fld; // nel caso in cui non venga trovato il campo viene restituito null
}

//TODO @Deprecated
public Object getField(Field fld, Object obj) throws Exception {
	Object result = null;
	result = getFieldOnly(fld, obj);
	if ( fld.getType().isArray() ) { // se � un array
		try {
			result = Array.get( result, indexOfList); // ritorna il valore dell'array di indice i-esimo
		} catch(ArrayIndexOutOfBoundsException e) {
			errorMsg("setField","Il campo: "+fld+"\ninvocato per l'oggetto tipo: "+obj.getClass().getName()+"\nnon � accessibile.",e);
		} catch(IllegalArgumentException e) {
			errorMsg("setField","Il campo: "+fld+"\nnon � un'istanza dell'oggetto tipo: "+obj.getClass().getName()+".",e);
		} catch(NullPointerException e) {
			errorMsg("setField","Il campo: "+fld+" � invocato su un oggetto nullo.",e);
		}
	}

	return result;
}

//TODO @Deprecated
public static Object getFieldOnly(Field fld, Object obj) throws Exception {
	Object result = null;
	try {
		result = fld.get(obj);
	} catch(IllegalAccessException e) {
		errorMsg("getField","Il campo: "+fld+"\ninvocato per l'oggetto: "+obj.getClass().getName()+"\nnon � accessibile.",e);
	} catch(IllegalArgumentException e) {
		errorMsg("getField","Il campo: "+fld+"\nnon � un'istanza dell'oggetto: "+obj.getClass().getName()+".",e);
	} catch(NullPointerException e) {
		errorMsg("getField","Il campo: "+fld+"\n� invocato su un oggetto nullo.",e);
	}
	return result;
}
//TODO @Deprecated
public int getIndexOfList() {
	return indexOfList;
}
//TODO @Deprecated
public int getLeng() {
	return leng;
}
//TODO @Deprecated
public StringBuffer getLogString() {
	return logString;
}
//TODO @Deprecated
protected Object getObject() {
	return object;
}
//TODO @Deprecated
public Class<?> getType(Field fld) throws Exception {
	if ( fld.getType().isArray() ) { // se � un array
		Object campo = getFieldOnly(fld,getObject()); // calcola il valore
		return Array.get( campo, indexOfList).getClass(); // gli copia il valore dell'array di indice i-esimo
	}
	return fld.getType();
}
//TODO @Deprecated
public static Object invocaGet(Method mtd, Object obj) throws Exception {
	Object value = null;
	Object[] prm = null;
	try {
		value = mtd.invoke(obj,prm); // invoco il metodo che mi restituisce il valore
	} catch(IllegalAccessException e) {
		errorMsg("invocaGet","Il methodo: "+mtd+" non � accessibile.",e);
	} catch(IllegalArgumentException e) {
		errorMsg("invocaGet","Il methodo: "+mtd+"\n� invocato con un numero o con tipi di argomenti errati.",e);
	} catch(java.lang.reflect.InvocationTargetException e) {
		errorMsg("invocaGet","Il methodo: "+mtd+" ha generato un errore.",e);
	} catch(NullPointerException e) {
		errorMsg("invocaGet","Il methodo: "+mtd+" � invocato su un oggetto nullo.",e);
	}
	return value;
}
//TODO @Deprecated
public static Object invocaMetodo(Method mtd, Object obj, Object dato) throws Exception {
	Object value = null;
	Object prm[] = new Object[1];
	prm[0] = dato;
	try {
		value = mtd.invoke(obj, prm); // invoco il metodo che mi setta il valore
	} catch (IllegalAccessException e) {
		errorMsg("invocaMetodo","Il methodo: "+mtd+"\ninvocato con parametro tipo: "+dato.getClass().getName()+"\nnon � accessibile.",e);
	} catch (IllegalArgumentException e) {
		errorMsg("invocaMetodo","Il methodo: "+mtd+"\ninvocato con parametro tipo: "+dato.getClass().getName()+"\nha numero o tipi di argomenti errati.",e);
	} catch (InvocationTargetException e) {
		errorMsg("invocaMetodo","Il methodo: "+mtd+"\ninvocato con parametro tipo: "+dato.getClass().getName()+"\nha generato un errore.",e);
	} catch (NullPointerException e) {
		errorMsg("invocaMetodo","Il methodo: "+mtd+"\n� invocato su un oggetto nullo.",e);
	}
	return value;
}
//TODO @Deprecated
public static void invocaSet(Method mtd, Object obj, Object dato) throws Exception {
	Object prm[] = new Object[1];
	prm[0] = dato;
	try {
		mtd.invoke(obj, prm); // invoco il metodo che mi setta il valore
	} catch (IllegalAccessException e) {
		errorMsg("invocaSet", "Il methodo: " + mtd + "\ninvocato con parametro tipo: " + dato.getClass().getName() + "\nnon � accessibile.", e);
	} catch (IllegalArgumentException e) {
		errorMsg("invocaSet", "Il methodo: " + mtd + "\ninvocato con parametro tipo: " + dato.getClass().getName() + "\nha numero o tipi di argomenti errati.", e);
	} catch (InvocationTargetException e) {
		errorMsg("invocaSet", "Il methodo: " + mtd + "\ninvocato con parametro tipo: " + dato.getClass().getName() + "\nha generato un errore.", e);
	} catch (NullPointerException e) {
		errorMsg("invocaSet", "Il methodo: " + mtd + " � invocato su un oggetto nullo.", e);
	}
}
//TODO @Deprecated
public boolean isError() {
	return error;
}
//TODO @Deprecated
public void setField(Field fld, Object obj, Object value) throws Exception {
	if ( fld.getType().isArray() ) { // se � un array
		Object campo = getFieldOnly(fld,obj); // calcola il valore
		try {
			Array.set( campo, indexOfList, value); // gli copia il valore dell'array di indice i-esimo
		} catch(ArrayIndexOutOfBoundsException e) {
			errorMsg("setField","Il campo: "+fld+"\ninvocato per l'oggetto tipo: "+obj.getClass().getName()+"\nnon � accessibile.",e);
		} catch(IllegalArgumentException e) {
			errorMsg("setField","Il campo: "+fld+"\nnon � un'istanza dell'oggetto "+obj.getClass().getName()+".",e);
		} catch(NullPointerException e) {
			errorMsg("setField","Il campo: "+fld+" � invocato su un oggetto nullo.",e);
		}
	} else {
		try {
//			obj.getClass().isArray();
			fld.set(obj,value);
		} catch(IllegalAccessException e) {
			errorMsg("setField","Il campo: "+fld+"\ninvocato per l'oggetto tipo: "+obj.getClass().getName()+" non � accessibile.",e);
		} catch(IllegalArgumentException e) {
			errorMsg("setField","Il campo: "+fld+"\nnon � un'istanza dell'oggetto "+obj.getClass().getName()+".",e);
		} catch(NullPointerException e) {
			errorMsg("setField","Il campo: "+fld+" � invocato su un oggetto nullo.",e);
		}
	}
}

public static Object getValueIfIsInputAnnotation(Object requested, String name, Object[] value){
	try{
		if(requested==null || name==null || name.equals(""))
			return null;
		for(Method method: requested.getClass().getDeclaredMethods()){
			Serialized annotation = method.getAnnotation(Serialized.class);
			if(annotation!=null && annotation.input()!=null && annotation.input().name()!=null && annotation.input().name().equals(name))
				return method.invoke(requested, value);
		}
		for(Field field: requested.getClass().getDeclaredFields()){
			Serialized annotation = field.getAnnotation(Serialized.class);
			if(annotation!=null && annotation.input()!=null && annotation.input().name()!=null && annotation.input().name().equals(name)){
				if(!field.isAccessible()){
					field.setAccessible(true);
					Object ret = field.get(requested);
					field.setAccessible(false);
					return ret;
				}else
					return field.get(requested);

				
			}
		}
		return null;
	}catch(Exception e){
		return null;
	}
}

@SuppressWarnings("unchecked")
public static Field[] getAllDeclaredFields(Class<?> clazz, @SuppressWarnings("rawtypes") Comparable comp){
	if(clazz==null) return new Field[0];
	List<Field> all = new ArrayList<Field>();
	while(clazz!=null && !clazz.equals(Object.class)){
		for(Field field:clazz.getDeclaredFields()){
			if(comp!=null){
				if(comp.compareTo(field)==0)
					all.add(field);
			}else				
				all.add(field);
		}
		clazz=clazz.getSuperclass();
	}
	Field[] ret = new Field[all.size()];
	for(int i=0;i<all.size();i++)
		ret[i] = (Field)all.get(i);
	return ret;
}

@SuppressWarnings("unchecked")
public static Method[] getAllDeclaredMethods(Class<?> clazz, @SuppressWarnings("rawtypes") Comparable comp){
	if(clazz==null) return new Method[0];
	List<Method> all = new ArrayList<Method>();
	while(clazz!=null && !clazz.equals(Object.class)){
		for(Method method:clazz.getDeclaredMethods()){
			if(comp!=null){
				if(comp.compareTo(method)==0)
					all.add(method);
			}else				
				all.add(method);
		}
		clazz=clazz.getSuperclass();
	}
	Method[] ret = new Method[all.size()];
	for(int i=0;i<all.size();i++)
		ret[i] = (Method)all.get(i);
	return ret;
}

public static Method findDeclaredMethod(Class<?> clazz, String name, Class<?>[] parameterTypes){
	if(clazz==null) return null;
	
	while(clazz!=null && !clazz.equals(Object.class)){
		try{
			return clazz.getDeclaredMethod(name, parameterTypes);
		}catch(Exception e){			
		}
		clazz=clazz.getSuperclass();
	}
	return null;
}

//TODO @Deprecated
public void setIndexOfList(int newValue) {
	this.indexOfList = newValue;
}
//TODO @Deprecated
public void setLeng(int newValue) {
	this.leng = newValue;
}
//TODO @Deprecated
public static void setLogString(String newValue) {
	logString.append(newValue);
}
//TODO @Deprecated
public void setLogString(StringBuffer newValue) {
	logString = newValue;
}
//TODO @Deprecated
private void setObject(Object newValue) {
	this.object = newValue;
}


}
