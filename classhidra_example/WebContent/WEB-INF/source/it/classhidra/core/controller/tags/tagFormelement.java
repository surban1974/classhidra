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


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.DynamicAttributes;

import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_tag;
import it.classhidra.core.tool.util.util_xml;


public class tagFormelement extends ClTagSupport implements DynamicAttributes {
	private static final long serialVersionUID = -1L;
	protected String bean = null;
	protected String name = null;
	protected String styleClass=null;
	protected String formatOutput=null;
	protected String formatLanguage=null;
	protected String formatCountry=null;
	protected String method_prefix=null;
	protected String replaceOnBlank=null;
	protected String normalXML=null;
	protected String normalASCII=null;
	protected String normalHTML=null;
	protected String additionalAttr=null;
	
	protected Map tagAttributes = new HashMap();
	protected List arguments=null;


	public int doStartTag() throws JspException {
		arguments = new ArrayList();
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		StringBuffer results = new StringBuffer();
		results.append(this.createTagBody());
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		return super.doEndTag();
	}

	public void release() {
		super.release();
		bean=null;
		name=null;
		styleClass=null;
		formatOutput=null;
		method_prefix=null;
		replaceOnBlank=null;
		formatLanguage=null;
		formatCountry=null;
		normalXML=null;
		normalASCII=null;
		normalHTML=null;
		additionalAttr=null;
		tagAttributes = new HashMap();
		arguments=null;
	}
  
	protected String createTagBody() {
		try{
			Object[] arg = null;
			if(arguments!=null && arguments.size()>0){
				arg = new Object[arguments.size()];
				for(int i=0;i<arguments.size();i++)
					arg[i]=arguments.get(i);
			}
			if(arguments!=null)
				arguments.clear();
			HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
			i_action formAction=null;
			i_bean formBean=null;
			if(bean!=null){
				HashMap pool = (HashMap)request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTIONPOOL);
				if(pool!=null) formAction = (i_action)pool.get(bean);
			}
			if(formAction!=null) bean = null;
			else formAction 	= (i_action)request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTION);		
			if(formAction==null) formAction = new action(); 
			if(bean==null){
				formBean = formAction.get_bean();
				if(formBean!=null)
					formBean=formBean.asBean();
			}
	
			
			if(name!=null)
				name=checkParametersIfDynamic(name, null);
			
			if(method_prefix==null) method_prefix="get";
			
	
			Object writeValue=null;
			Object anotherBean=null;
			if(bean==null){
				if(formBean!=null){
					writeValue = util_reflect.prepareWriteValueForTag(formBean,method_prefix,name,arg);
				}
			}else{
				if(name!=null){
					if(bean.equals(bsConstants.CONST_TAG_REQUESTPARAMETER)) anotherBean = request.getParameter(name);
					if(bean.equals(bsConstants.CONST_TAG_SYSTEMPROPERTY)) anotherBean = System.getProperty(name);
				}
				if(anotherBean==null) anotherBean = request.getAttribute(bean);
				if(anotherBean==null) anotherBean = request.getSession().getAttribute(bean);
				if(anotherBean==null) anotherBean = request.getSession().getServletContext().getAttribute(bean);
				if(name!=null){
					if(anotherBean==null) anotherBean = request.getAttribute(name);
					if(anotherBean==null) anotherBean = request.getSession().getAttribute(name);
					if(anotherBean==null) anotherBean = request.getSession().getServletContext().getAttribute(name);
				}
	
				if(anotherBean==null) anotherBean = util_tag.getBeanAsBSTag(bean,this);
				try{
					if(anotherBean==null) anotherBean = (bsController.getFromInfoNavigation(null, request)).find(bean).get_content();
				}catch(Exception e){
				}
				if(anotherBean==null) anotherBean = bsController.getFromOnlySession(bean, request);
				if(anotherBean==null) anotherBean = bsController.getProperty(bean,request);
				
				if(anotherBean==null){
					try{
						Object obj = getParent();
						String bean_name = (String)util_reflect.getValue(anotherBean,method_prefix+"bean",arg);
						if(bean.equals(bean_name)) anotherBean = obj;
					}catch(Exception e){					
					}
				}
				
				if(anotherBean!=null){
					if(name==null){
						writeValue = anotherBean;
					}
					else{
						try{
							writeValue = util_reflect.prepareWriteValueForTag(anotherBean,method_prefix,name,arg);					 
						}catch(Exception e){
						}
					}	
				}		 		
			}	
			String prefixName=null;
			
			if(bean!=null && request.getAttribute(tagBean.CONST_HEAP_BEANS)!=null && ((HashMap)request.getAttribute(tagBean.CONST_HEAP_BEANS)).get(bean)!=null){
				prefixName = ((HashMap)request.getAttribute(tagBean.CONST_HEAP_BEANS)).get(bean).toString();
			}
			if(prefixName==null)
				prefixName=name;
			else prefixName+="."+name;

		
			String html = drawTagBody(writeValue, prefixName);
	
			prefixName=null;
			return html;
		}catch(Exception e){
		}			

		return "";
	}	
	
	protected String drawTagBody(Object writeValue, String prefixName){
		StringBuffer results = new StringBuffer("");
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
			try{
				writeValue=util_format.makeFormatedString(formatOutput, formatLanguage,formatCountry, writeValue);
				if(replaceOnBlank != null && writeValue!=null && replaceOnBlank.equals(writeValue.toString())) 
					writeValue=util_format.replace(writeValue.toString(),replaceOnBlank,"");
			}catch(Exception e){}	
			

			if(normalXML!=null && normalXML.toLowerCase().equals("true"))
				results.append(util_xml.normalXML((writeValue==null)?"":writeValue.toString(),null));	
			else if(normalASCII!=null && normalASCII.equalsIgnoreCase("true"))	
				results.append(util_xml.normalASCII((writeValue==null)?"":writeValue.toString()));	
			else if(normalHTML!=null && normalHTML.equalsIgnoreCase("true"))
				results.append(util_xml.normalHTML((writeValue==null)?"":writeValue.toString(), null));	
			else 
				results.append(writeValue);
			
			if(styleClass!=null || additionalAttr!=null)
				results.append("</span>");
		}
		return results.toString();
	}
	

	public String getName() {
		return name;
	}
	public String getStyleClass() {
		return styleClass;
	}
	public void setName(String string) {
		name = string;
	}
	public void setStyleClass(String string) {
		styleClass = string;
	}
	public String getBean() {
		return bean;
	}
	public void setBean(String string) {
		bean = string;
	}
	public String getFormatOutput() {
		return formatOutput;
	}
	public void setFormatOutput(String string) {
		formatOutput = string;
	}

	public String getMethod_prefix() {
		return method_prefix;
	}

	public void setMethod_prefix(String string) {
		method_prefix = string;
	}
	public String getReplaceOnBlank() {
		return replaceOnBlank;
	}
	public void setReplaceOnBlank(String replaceOnBlank) {
		this.replaceOnBlank = replaceOnBlank;
	}

	public String getNormalXML() {
		return normalXML;
	}

	public void setNormalXML(String normalXML) {
		this.normalXML = normalXML;
	}

	public String getFormatLanguage() {
		return formatLanguage;
	}

	public void setFormatLanguage(String formatLanguage) {
		this.formatLanguage = formatLanguage;
	}

	public String getFormatCountry() {
		return formatCountry;
	}

	public void setFormatCountry(String formatCountry) {
		this.formatCountry = formatCountry;
	}

	public String getNormalASCII() {
		return normalASCII;
	}

	public void setNormalASCII(String normalASCII) {
		this.normalASCII = normalASCII;
	}

	public String getNormalHTML() {
		return normalHTML;
	}

	public void setNormalHTML(String normalHTML) {
		this.normalHTML = normalHTML;
	}

	public String getAdditionalAttr() {
		return additionalAttr;
	}

	public void setAdditionalAttr(String additionalAttr) {
		this.additionalAttr = additionalAttr;
	}

	public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
		tagAttributes.put(localName, value);
	}

	public List getArguments() {
		return arguments;
	}

	public void setArguments(List arguments) {
		this.arguments = arguments;
	}


}

