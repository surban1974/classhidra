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


public interface i_elementBase extends java.io.Serializable{
	public static String T_C_TEXT = 				"TEXT";
	public static String F_C_DECIMAL = 				"DECIMAL";
	public static String F_C_DECIMAL_CONT=			"DECIMAL_CONT"; 
	public static String F_C_CHAR = 				"CHAR";
	public static String F_C_SHORT = 				"SHORT";
	public static String F_C_DATE = 				"DATE";
	public static String F_C_TIMESTAMP = 			"TIMESTAMP";
	public static String F_C_INTEGER = 				"INTEGER";
Object getCampoValue(String nome);
String getCampoValue(String nome, String tipo);
boolean setCampoValue(String nome, Object[] value) throws Exception;
boolean setCampoValue(String nome, Object value) throws Exception;
Object getValue(Object requested, String nome, Object[] value) throws Exception;	
boolean setValue(Object requested, String nome, Object[] value) throws Exception;
}
