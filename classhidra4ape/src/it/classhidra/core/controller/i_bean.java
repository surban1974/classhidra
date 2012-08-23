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

import java.math.BigDecimal;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.elements.i_elementBeanBase;
import it.classhidra.core.tool.elements.i_elementDBBase;
import it.classhidra.core.tool.exception.bsControllerException;




public interface i_bean extends i_elementBeanBase{
public void reimposta();
public void init(HttpServletRequest request)  throws bsControllerException;
public void init(HashMap _content)  throws bsControllerException;
public void init(i_bean another_bean) throws bsControllerException;
public void reInit(i_elementDBBase _i_el); 
public redirects validate(HttpServletRequest request)  throws bsControllerException;
public info_bean get_infobean();
public void set_infobean(info_bean bean);

public Object get(Object requested,String value);
public Object get(String value);
public int getInt(String name);
public short getShort(String name);
public long getLong(String name);
public float getFloat(String name);
public double getDouble(String name);
public byte getByte(String name);
public boolean getBoolean(String name);
public char getChar(String name);
public BigDecimal getBigDecimal(String name);
public String getString(String name);

public void set(String name, Object value);
public void put(String name, Object value);
public void set(String name, int value);
public void set(String name, short value);
public void set(String name, long value);
public void set(String name, float value);
public void set(String name, double value);
public void set(String name, byte value);
public void set(String name, boolean value);
public void set(String name, char value);


public info_action get_infoaction();
public void set_infoaction(info_action action);
public boolean logic_equals(i_bean obj);
public auth_init getCurrent_auth();
public void setCurrent_auth(auth_init current_auth);
public boolean getRefresh();
public void setRefresh(boolean refresh);
public String getMiddleAction();
public void setMiddleAction(String string);
public HashMap getParametersMP();
public void setParametersMP(HashMap parametersMP);
public HashMap getParametersFly();
public void setParametersFly(HashMap parametersFly);
public String getJs4ajax();
public void setJs4ajax(String js4ajax);

public boolean getXmloutput();
public void setXmloutput(boolean xmloutput);
public boolean getTransformationoutput();
public void setTransformationoutput(boolean transformationoutput);
public boolean getBinaryoutput();
public void setBinaryoutput(boolean binaryoutput);
public Object getDelegated();
public void setDelegated(Object delegated);
public boolean getVirtual();
public void setVirtual(boolean virtual);
}
