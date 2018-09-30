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


import it.classhidra.core.controller.bsController;
import it.classhidra.core.tool.util.util_xml;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.DynamicAttributes;

public class tagMessage extends ClTagSupport implements DynamicAttributes {
	private static final long serialVersionUID = -896536174738762236L;
	protected String objId = null;// id
	protected String code = null;	
	protected String styleClass=null;
	protected String defaultValue=null;
	protected HashMap parameters=null;
	protected String normalXML=null;
	protected String normalXML10=null;
	protected String normalXML11=null;
	protected String charset;
	protected String normalASCII=null;
	protected String normalHTML=null;


	protected Map tagAttributes = new HashMap();

	
	public int doEndTag() throws JspException {
		final StringBuffer results = new StringBuffer();
		results.append(this.createTagBody());
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		return super.doEndTag(); 
	}

	public int doStartTag() throws JspException {
		parameters=new HashMap();
		return EVAL_BODY_INCLUDE; 
	}

	public void release() {
		super.release();
		objId = null;
		code=null;
		styleClass=null;
		defaultValue=null;
		parameters=null;
		normalXML=null;
		normalXML10=null;
		normalXML11=null;
		charset=null;
		normalASCII=null;
		normalHTML=null;

		tagAttributes = new HashMap();
	}
  
	protected String createTagBody() {
		HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
	
		final StringBuffer results = new StringBuffer("");
		if(code!=null){
			if(styleClass!=null || tagAttributes.size()>0){
				results.append(" <span class=\"");
				results.append(styleClass);
				results.append("\"");
				if(objId!=null){
					results.append(" id=\"");
					results.append(objId);
					results.append('"');
				}
				
			    for(Object attrName : tagAttributes.keySet() ) {
			    	results.append(" ");
			    	results.append(attrName);
			    	results.append("=\"");
			    	results.append(tagAttributes.get(attrName));
			    	results.append("\"");
			      }

				results.append(">");

			}
			
			final String writeValue = bsController.writeLabel(request,checkParametersIfDynamic(code, null),checkParametersIfDynamic(defaultValue, null),parameters);
			if(normalXML!=null && normalXML.toLowerCase().equals("true"))
				results.append(util_xml.normalXML((writeValue==null)?"":writeValue.toString(),charset));	
			else if(normalXML10!=null && normalXML10.toLowerCase().equals("true"))
				results.append(util_xml.escapeXML10((writeValue==null)?"":writeValue.toString(),charset));		
			else if(normalXML11!=null && normalXML11.toLowerCase().equals("true"))
				results.append(util_xml.escapeXML11((writeValue==null)?"":writeValue.toString(),charset));		
			else if(normalASCII!=null && normalASCII.equalsIgnoreCase("true"))	
				results.append(util_xml.normalASCII((writeValue==null)?"":writeValue.toString()));	
			else if(normalHTML!=null && normalHTML.equalsIgnoreCase("true"))
				results.append(util_xml.normalHTML((writeValue==null)?"":writeValue.toString(), null));	
			else 
				results.append(writeValue);
			
			if(styleClass!=null){
				results.append(" </span>");
			}
		}
//		String output = results.toString();
//		if(normalXML!=null && normalXML.equalsIgnoreCase("true"))
//			output = util_xml.normalXML(output,charset);
		return results.toString();

	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public String getStyleClass() {
		return styleClass;
	}
	public void setDefaultValue(String string) {
		defaultValue = string;
	}
	public void setStyleClass(String string) {
		styleClass = string;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String string) {
		code = string;
	}

	public HashMap getParameters() {
		return parameters;
	}

	public String getNormalXML() {
		return normalXML;
	}

	public void setNormalXML(String normalXML) {
		this.normalXML = normalXML;
	}

	public String getNormalASCII() {
		return normalASCII;
	}

	public void setNormalASCII(String normalASCII) {
		this.normalASCII = normalASCII;
	}
	
	public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
		tagAttributes.put(localName, value);
	}

	public String getNormalXML10() {
		return normalXML10;
	}

	public void setNormalXML10(String normalXML10) {
		this.normalXML10 = normalXML10;
	}

	public String getNormalXML11() {
		return normalXML11;
	}

	public void setNormalXML11(String normalXML11) {
		this.normalXML11 = normalXML11;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getNormalHTML() {
		return normalHTML;
	}

	public void setNormalHTML(String normalHTML) {
		this.normalHTML = normalHTML;
	}



	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}


}

