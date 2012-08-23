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

import it.classhidra.core.controller.i_bean;

import java.util.HashMap;

public interface i_elementDBBase extends i_elementBase{
public Object clone();
public boolean control();
public boolean equals(i_elementDBBase el);
public boolean find(i_elementDBBase el);
public void reimposta();
public String sql_Select();
public String sql_Delete();
public String sql_Insert();
public String sql_Update(i_elementDBBase element_mod);
public String sql_Select(String alias);
public String sql_Delete(String alias);
public String sql_Insert(String alias);
public String sql_Update(i_elementDBBase element_mod,String alias);
public void reInit(java.sql.ResultSet rs);
public void reInit(i_bean another_bean);
public boolean sql_getFromResultSet(java.sql.ResultSet rs);
public boolean sql_getFromResultSet(java.sql.ResultSet rs, java.sql.ResultSetMetaData rsmd);
public boolean sql_getFromResultSet(java.sql.ResultSet rs, java.sql.ResultSetMetaData rsmd, String prefisso);
public HashMap getFields();
public void setFields(HashMap fields);
public i_elementDBBase load_use_itself() throws Exception;
public i_elementDBBase load_use_sql(String sql) throws Exception;}
