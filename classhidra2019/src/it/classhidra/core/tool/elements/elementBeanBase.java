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


public abstract class elementBeanBase extends elementDBBase implements i_elementDBBase, i_elementBase, i_elementBeanBase,  java.io.Serializable {
private static final long serialVersionUID = 1L;
public abstract void reimposta();
public Object get(String id){
	if(id.toUpperCase().indexOf("BEAN_")==0){
		id = id.substring(5);
		return getCampoValue(id);
	}else return getCampoValue(id); 
}
}
