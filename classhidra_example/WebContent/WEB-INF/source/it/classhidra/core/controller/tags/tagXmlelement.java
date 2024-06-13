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


import javax.servlet.jsp.tagext.DynamicAttributes;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_xml;


public class tagXmlelement extends tagFormelement implements DynamicAttributes {
	private static final long serialVersionUID = -1L;
	
	protected String drawTagBody(Object writeValue, String prefixName){
		final StringBuffer results = new StringBuffer("");
		if(writeValue!=null){
			if(styleClass!=null || additionalAttr!=null || tagAttributes.size()>0){
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
			    for(Object attrName : tagAttributes.keySet() ) {
			    	results.append(" ");
			    	results.append(attrName);
			    	results.append("=\"");
			    	results.append(tagAttributes.get(attrName));
			    	results.append("\"");
			      }

				results.append(" $modelWire=\"");
				results.append("formelement:"+prefixName);
				results.append('"');
				
				results.append(">");
			}
			
			if(formatLocationFromUserAuth==null && bsController.getAppInit().get_tag_format_user_auth()!=null && !bsController.getAppInit().get_tag_format_user_auth().equals(""))
				formatLocationFromUserAuth=bsController.getAppInit().get_tag_format_user_auth();			
			if(formatLocationFromUserAuth==null && bsController.getFromLocalContainer(bsConstants.CONST_TAG_ALL_FORMATLOCATIONFROMUSERAUTH)!=null)
				formatLocationFromUserAuth=bsController.getFromLocalContainer(bsConstants.CONST_TAG_ALL_FORMATLOCATIONFROMUSERAUTH).toString();

			if(formatLanguage==null && bsController.getAppInit().get_tag_format_language()!=null && !bsController.getAppInit().get_tag_format_language().equals(""))
				formatLanguage=bsController.getAppInit().get_tag_format_language();			
			if(formatLanguage==null && bsController.getFromLocalContainer(bsConstants.CONST_TAG_ALL_FORMATLANGUAGE)!=null)
				formatLanguage=bsController.getFromLocalContainer(bsConstants.CONST_TAG_ALL_FORMATLANGUAGE).toString();

			if(formatCountry==null && bsController.getAppInit().get_tag_format_country()!=null && !bsController.getAppInit().get_tag_format_country().equals(""))
				formatCountry=bsController.getAppInit().get_tag_format_country();			
			if(formatCountry==null && bsController.getFromLocalContainer(bsConstants.CONST_TAG_ALL_FORMATCOUNTRY)!=null)
				formatCountry=bsController.getFromLocalContainer(bsConstants.CONST_TAG_ALL_FORMATCOUNTRY).toString();			
			if(formatCurrency==null && bsController.getAppInit().get_tag_format_currency()!=null && !bsController.getAppInit().get_tag_format_currency().equals(""))
				formatCurrency=bsController.getAppInit().get_tag_format_currency();			
			if(formatCurrency==null && bsController.getFromLocalContainer(bsConstants.CONST_TAG_ALL_FORMATCURRENCY)!=null)
				formatCurrency=bsController.getFromLocalContainer(bsConstants.CONST_TAG_ALL_FORMATCURRENCY).toString();
			
			if(formatLocationFromUserAuth!=null)
				formatLocationFromUserAuth=checkParametersIfDynamic(formatLocationFromUserAuth, null);
			if(formatLanguage!=null)
				formatLanguage=checkParametersIfDynamic(formatLanguage, null);
			if(formatCountry!=null)
				formatCountry=checkParametersIfDynamic(formatCountry, null);
			if(formatCurrency!=null)
				formatCurrency=checkParametersIfDynamic(formatCurrency, null);
			if(formatOutput!=null)
				formatOutput=checkParametersIfDynamic(formatOutput, null);
			
			try{
				if(formatLocationFromUserAuth!=null && formatLocationFromUserAuth.equalsIgnoreCase("true") && auth!=null)
					writeValue=util_format.makeFormatedString(formatOutput, auth.get_language(), auth.get_country(), formatCurrency, writeValue);
				else
					writeValue=util_format.makeFormatedString(formatOutput, formatLanguage,formatCountry, formatCurrency, writeValue);
				if(replaceOnBlank != null && writeValue!=null && replaceOnBlank.equals(writeValue.toString())) 
					writeValue=util_format.replace(writeValue.toString(),replaceOnBlank,"");
			}catch(Exception e){}	
			
			if(normalXML10!=null && normalXML10.toLowerCase().equals("true"))
				results.append(util_xml.escapeXML10((writeValue==null)?"":writeValue.toString(),charset));		
			else if(normalXML11!=null && normalXML11.toLowerCase().equals("true"))
				results.append(util_xml.escapeXML11((writeValue==null)?"":writeValue.toString(),charset));		
			else if(normalXMLCDATA!=null && normalXMLCDATA.toLowerCase().equals("true"))
				results.append(util_xml.normalCDATA((writeValue==null)?"":writeValue.toString(),charset));			
			else 
				results.append(util_xml.normalXML((writeValue==null)?"":writeValue.toString(),charset));
			
			if(styleClass!=null || additionalAttr!=null)
				results.append("</span>");
		}
		return results.toString();
	}
	
	public void release() {
		super.release();		
	}

}

