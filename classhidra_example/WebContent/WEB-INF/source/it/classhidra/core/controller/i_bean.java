/**
* Creation date: (07/04/2006)
* @author: Svyatoslav Urbanovych svyatoslav.urbanovych@gmail.com
*/

/********************************************************************************
*
*	Copyright (C) 2005  Svyatoslav Urbanovych
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General License for more details.

* You should have received a copy of the GNU General License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*********************************************************************************/
package it.classhidra.core.controller;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.elements.i_elementBeanBase;
import it.classhidra.core.tool.elements.i_elementDBBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.serialize.JsonMapper;
import it.classhidra.serialize.XmlMapper;




public interface i_bean extends listener_bean, i_elementBeanBase{
	void reimposta();
	void init(HttpServletRequest request)  throws bsControllerException;
	void init_(HashMap _content)  throws bsControllerException;
	void init(HashMap _content)  throws bsControllerException;
	void init(i_bean another_bean) throws bsControllerException;
	void reInit(i_elementDBBase _i_el);
	void initPartFromMap(HashMap parameters) throws bsControllerException;
	void initFromMap(HashMap parameters, boolean add2fly) throws bsControllerException;
	redirects validate(HttpServletRequest request)  throws bsControllerException;
	redirects validate(HashMap parameters)  throws bsControllerException;
	redirects validate(String currentAction, String newAction, String newActionCall, HttpServletRequest request)  throws bsControllerException;
	redirects validate(String currentAction, String newAction, String newActionCall, HashMap parameters)  throws bsControllerException;	
	info_bean get_infobean();
	void set_infobean(info_bean bean);

	Object getPrimitiveArgument(String name, String s_value);
	Object get(Object requested,String value);
	Object get(String value);
	int getInt(String name);
	short getShort(String name);
	long getLong(String name);
	float getFloat(String name);
	double getDouble(String name);
	byte getByte(String name);
	boolean getBoolean(String name);
	char getChar(String name);
	BigDecimal getBigDecimal(String name);
	String getString(String name);
	Collection getCollection(String name);
	List getList(String name);
	Map getMap(String name);
	

	void set(String name, Object value);
	void put(String name, Object value);
	void set(String name, int value);
	void set(String name, short value);
	void set(String name, long value);
	void set(String name, float value);
	void set(String name, double value);
	void set(String name, byte value);
	void set(String name, boolean value);
	void set(String name, char value);
	void setCampoValuePoint(Object req, String nome, Object value) throws Exception;
	void setCampoValuePoint(Object req, String nome, Object value, boolean log) throws Exception;
	boolean setCampoValueWithPoint(String name, Object value) throws Exception;

	info_action get_infoaction();
	void set_infoaction(info_action action);
	boolean logic_equals(i_bean obj);
	auth_init getCurrent_auth();
	void setCurrent_auth(auth_init current_auth);
	boolean getRefresh();
	void setRefresh(boolean refresh);
	String getMiddleAction();
	void setMiddleAction(String string);
	HashMap getParametersMP();
	void setParametersMP(HashMap parametersMP);
	HashMap getParametersFly();
	void setParametersFly(HashMap parametersFly);
	String getJs4ajax();
	void setJs4ajax(String js4ajax);

	boolean getXmloutput();
	void setXmloutput(boolean xmloutput);
	String getXmloutput_encoding();
	void setXmloutput_encoding(String xmloutputEncoding);
	
	boolean getJsonoutput();
	void setJsonoutput(boolean jsonoutput);
	String getJsonoutput_encoding();
	void setJsonoutput_encoding(String jsonoutputEncoding);
	
	boolean getBinaryoutput();
	void setBinaryoutput(boolean binaryoutput);
	String getBinaryoutput_encoding();
	void setBinaryoutput_encoding(String binaryoutputEncoding);
	
	boolean getGzipoutput();
	void setGzipoutput(boolean gzipoutput);	
	
	String getOutputappliedfor();
	void setOutputappliedfor(String outputfor);

	String getOutputserializedname();
	void setOutputserializedname(String outputserializedname);
	
	boolean getTransformationoutput();
	void setTransformationoutput(boolean transformationoutput);

	Object getDelegated();
	void setDelegated(Object delegated);
	boolean getVirtual();
	void setVirtual(boolean virtual);

	info_context getInfo_context();
	void setInfo_context(i_info_context info_context);
	
	
	listener_bean getListener_b();
	void setListener_b(listener_bean listener);
	i_action asAction();
	i_bean asBean();
	
	void clearBeforeStore();
	Map getInitErrors();
	
	boolean isAsyncInterrupt();
	void setAsyncInterrupt(boolean asyncInterrupt);
	
	JsonMapper getJsonMapper();
	XmlMapper getXmlMapper();
	
	String get$csrf();
	Map getComponents();
//	void set$csrf(String csrf);
}
