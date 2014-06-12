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
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.elements.i_elementBeanBase;
import it.classhidra.core.tool.elements.i_elementDBBase;
import it.classhidra.core.tool.exception.bsControllerException;




public interface i_bean extends i_elementBeanBase,listener_bean{
	void reimposta();
	void init(HttpServletRequest request)  throws bsControllerException;
	void init(HashMap _content)  throws bsControllerException;
	void init(i_bean another_bean) throws bsControllerException;
	void reInit(i_elementDBBase _i_el); 
	redirects validate(HttpServletRequest request)  throws bsControllerException;
	info_bean get_infobean();
	void set_infobean(info_bean bean);
	
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
	boolean getJsonoutput();
	void setJsonoutput(boolean jsonoutput);
	boolean getTransformationoutput();
	void setTransformationoutput(boolean transformationoutput);
	boolean getBinaryoutput();
	void setBinaryoutput(boolean binaryoutput);
	Object getDelegated();
	void setDelegated(Object delegated);
	boolean getVirtual();
	void setVirtual(boolean virtual);
	
	listener_bean getListener_b();
	void setListener_b(listener_bean listener);
}
