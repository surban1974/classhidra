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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class tagMessage extends TagSupport{
	private static final long serialVersionUID = -896536174738762236L;
	protected String code = null;	
	protected String styleClass=null;
	protected String defaultValue=null;
	protected HashMap parameters=null;
	protected String normalXML=null;
	protected String normalASCII=null;


	
	public int doEndTag() throws JspException {
		StringBuffer results = new StringBuffer();
		results.append(this.createTagBody());
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		return EVAL_BODY_INCLUDE; 
	}

	public int doStartTag() throws JspException {
		parameters=new HashMap();
		return EVAL_BODY_INCLUDE; 
	}

	public void release() {
		super.release();
		code=null;
		styleClass=null;
		defaultValue=null;
		parameters=null;
		normalXML=null;
		normalASCII=null;
	}
  
	protected String createTagBody() {
		HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
	
		StringBuffer results = new StringBuffer("");
		if(code!=null){
			if(styleClass!=null){
				results.append(" <span class=\"");
				results.append(styleClass);
				results.append("\">");
			}
			results.append(bsController.writeLabel(request,code,defaultValue,parameters));			
			if(styleClass!=null){
				results.append(" </span>");
			}
		}
		String output = results.toString();
		if(normalXML!=null && normalXML.equalsIgnoreCase("true"))
			output = util_xml.normalXML(output,null);
		if(normalASCII!=null && normalASCII.equalsIgnoreCase("true"))
			output = util_xml.normalASCII(output);
		return output;

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
	


}

