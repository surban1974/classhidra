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

package it.classhidra.core.tool.elements;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;

import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.*;
import it.classhidra.serialize.Serialized;


public abstract class elementBase implements i_elementBase, java.io.Serializable, Cloneable {
private static final long serialVersionUID = 1L;
protected String nameTable = "";
protected String namePackage = "";

public elementBase() {
	super();
}
public Object clone() {
	Object clone = null;
	try{
		clone = super.clone();
	}catch(Exception e){
		new bsException(e,iStub.log_DEBUG);
		try{
			clone = util_cloner.clone(this);
		}catch(Exception ex){
			new bsException(ex,iStub.log_DEBUG);
		}
		
	}	
	return clone;
}
public void setCampoValue_light(String nome, Object value) throws Exception{
	if (nome == null || nome.trim().length()==0 || value == null) return; 
		java.lang.reflect.Method mtd = null;
		Class[] cl = new Class[1];
		cl[0] = value.getClass();	
		mtd = this.getClass().getMethod(nome,cl);
		if(mtd!=null){
			Object prm[] = new Object[1];
			prm[0] = value;
			mtd.invoke(this, prm);
		}
}

public Object getCampoValue(String nome){
	try{
		Object[] par = new Object[0];
		Object resultObject = getValue(this,"get"+util_reflect.adaptMethodName(nome.trim()),par);
		if(resultObject==null) resultObject = getValue(this,"is"+util_reflect.adaptMethodName(nome.trim()),par);
		return resultObject;
	}catch(Exception e){
		try{
			Object[] par = new Object[0];
			Object resultObject = getValue(this,"is"+util_reflect.adaptMethodName(nome.trim()),par);
			return resultObject;
		}catch(Exception ex){
			return null;		
		}
	
	}
}

public Object getCampoValue(Object requested,String nome){
	try{
		Object[] par = new Object[0];
		Object resultObject = getValue(requested,"get"+util_reflect.adaptMethodName(nome.trim()),par);
		if(resultObject==null) resultObject = getValue(requested,"is"+util_reflect.adaptMethodName(nome.trim()),par);
		return resultObject;
	}catch(Exception e){
		try{
			Object[] par = new Object[0];
			Object resultObject = getValue(requested,"is"+util_reflect.adaptMethodName(nome.trim()),par);
			return resultObject;
		}catch(Exception ex){
			return null;		
		}
	
	}
}

public String getCampoValue(String nome, String tipo){
	try{ 
		String result=null;
		Object[] par = new Object[0];
		Object resultObject = getValue(this,"get"+util_reflect.adaptMethodName(nome.trim()),par);

		if (resultObject==null) return result;
		if (tipo.trim().equals(i_elementBase.F_C_SHORT))	return ((Short)resultObject).toString();
		if (tipo.trim().equals(i_elementBase.F_C_DECIMAL)) return ((java.math.BigDecimal)resultObject).toString();
		if (tipo.trim().equals(i_elementBase.F_C_INTEGER)) return ((Integer)resultObject).toString();
		if (tipo.trim().equals(i_elementBase.F_C_DECIMAL_CONT)){
			if(resultObject instanceof java.math.BigDecimal) return ((java.math.BigDecimal)resultObject).toString();
			if(resultObject instanceof Integer) return ((Integer)resultObject).toString();
			try{
				return resultObject.toString();
			}catch(Exception e){
				return "";
			}
		}	
		if (tipo.trim().equals(i_elementBase.F_C_CHAR)) return ((String)resultObject);
		if (tipo.trim().equals(i_elementBase.F_C_DATE)){
			java.sql.Date tmp = (java.sql.Date)resultObject;
			Calendar c = Calendar.getInstance();
			c.setTime(tmp);
			String 	res = "";
					res+=String.valueOf(c.get(Calendar.YEAR))+"-";
					res+=((c.get(Calendar.MONTH)<9)?"0"+String.valueOf(c.get(Calendar.MONTH)+1):String.valueOf(c.get(Calendar.MONTH)+1))+"-";
					res+=((c.get(Calendar.DAY_OF_MONTH)<10)?"0"+String.valueOf(c.get(Calendar.DAY_OF_MONTH)):String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
			return res;			
		}	
		if (tipo.trim().equals(i_elementBase.F_C_TIMESTAMP)){
			java.sql.Timestamp tmp = (java.sql.Timestamp)resultObject;
			Calendar c = Calendar.getInstance();
			c.setTime(tmp);
			String 	res = "";
					res+=String.valueOf(c.get(Calendar.YEAR))+"-";
					res+=((c.get(Calendar.MONTH)<9)?"0"+String.valueOf(c.get(Calendar.MONTH)+1):String.valueOf(c.get(Calendar.MONTH)+1))+"-";
					res+=((c.get(Calendar.DAY_OF_MONTH)<10)?"0"+String.valueOf(c.get(Calendar.DAY_OF_MONTH)):String.valueOf(c.get(Calendar.DAY_OF_MONTH)))+"-";
					res+=((c.get(Calendar.HOUR)<10)?"0"+String.valueOf(c.get(Calendar.HOUR)):String.valueOf(c.get(Calendar.HOUR)))+".";
					res+=((c.get(Calendar.MINUTE)<10)?"0"+String.valueOf(c.get(Calendar.MINUTE)):String.valueOf(c.get(Calendar.MINUTE)))+".";
					res+=((c.get(Calendar.SECOND)<10)?"0"+String.valueOf(c.get(Calendar.SECOND)):String.valueOf(c.get(Calendar.SECOND)))+".";
					try{
						res+=String.valueOf(tmp.getNanos()).substring(0,6);
					}catch(Exception e){
						res+="000000";
					}
			return res;
		}	
		return result;
	}catch(Exception e){
		return "";	
	}
	
}
public void reimposta(){
	String fullName = this.getClass().getName();
	while (fullName.indexOf(".")>-1){
		namePackage+=fullName.substring(0,fullName.indexOf("."))+".";
		fullName = fullName.substring(fullName.indexOf(".")+1);
	}
	if (fullName.length()>2 && fullName.charAt(0)=='e' && fullName.charAt(1)=='l')
		nameTable=fullName.substring(2);
}
public boolean setCampoValue(String nome, Object[] value) throws Exception{
	return setValue(this, "set"+util_reflect.adaptMethodName(nome.trim()),value);
}
public boolean setCampoValue(String nome, Object value) throws Exception{
	Object[] par = new Object[1];
	par[0]=value; 
	return setValue(this, "set"+util_reflect.adaptMethodName(nome.trim()),par);
}

public boolean setCampoValue(Object requested,String nome, Object value) throws Exception{
	Object[] par = new Object[1];
	par[0]=value; 
	return setValue(requested, "set"+util_reflect.adaptMethodName(nome.trim()),par);
}

public void setNewFullName(String fullName) {
	namePackage="";
	while (fullName.indexOf(".")>-1){
		namePackage+=fullName.substring(0,fullName.indexOf("."))+".";
		fullName = fullName.substring(fullName.indexOf(".")+1);
	}
	if (fullName.length()>2 && fullName.charAt(0)=='e' && fullName.charAt(1)=='l')
		nameTable=fullName.substring(2);	
}

public Object getValue(Object requested, String nome, Object[] value) throws Exception{
	return util_reflect.getValue(requested,nome,value);
}
	
public boolean setValue(Object requested, String nome, Object[] value) throws Exception{
	return util_reflect.setValue(requested, nome, value);
}
public boolean setValue(Object requested, String nome, Object[] value, boolean log) throws Exception{
	return util_reflect.setValue(requested, nome, value,log);
}
public boolean setValueMapped(Object requested, String prefix, String nome, Object[] value, boolean log) throws Exception{
	String mapName = null;
	if(mapName==null){
		final String fkey = nome;
		Field[] alldf = util_reflect.getAllDeclaredFields(
				requested.getClass(),
				new Comparable() {
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
				new Comparable() {
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
}
