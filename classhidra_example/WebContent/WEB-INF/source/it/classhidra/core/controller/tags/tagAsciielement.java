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

package it.classhidra.core.controller.tags;


import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_xml;


public class tagAsciielement extends tagFormelement{
	private static final long serialVersionUID = -1L;

	
	protected String drawTagBody(Object writeValue, String prefixName){
		StringBuffer results = new StringBuffer("");
		if(writeValue!=null){
			if(styleClass!=null || additionalAttr!=null){
				results.append(" <span ");
				if(styleClass!=null){
					results.append(" class=\"");
					results.append(styleClass);
					results.append('"');
				}
				if(additionalAttr!=null){
					results.append(" ");
					results.append(additionalAttr);
				}

				results.append(" $modelWire=\"");
				results.append("formelement:"+prefixName);
				results.append('"');
				
				results.append(">");
			}
			try{
				writeValue=util_format.makeFormatedString(formatOutput, formatLanguage,formatCountry, writeValue);
				if(replaceOnBlank != null && writeValue!=null && replaceOnBlank.equals(writeValue.toString())) 
					writeValue=util_format.replace(writeValue.toString(),replaceOnBlank,"");
			}catch(Exception e){}	
			
			results.append(util_xml.normalASCII((writeValue==null)?"":writeValue.toString()));	
			
			if(styleClass!=null || additionalAttr!=null)
				results.append("</span>");
		}
		return results.toString();
	}

	

}

