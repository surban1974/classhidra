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


import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.tool.util.util_beanMessageFactory;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_tag;
import it.classhidra.core.tool.util.util_xml_xslt;

public class tagShowBeanViaXslt extends TagSupport{
	private static final long serialVersionUID = 1L;
	private String bean=null;
	private String name = null;
	private String method_prefix=null;
	private String startTag_xslt=null;
	private String endTag_xslt=null;


	public int doStartTag() throws JspException {
		final StringBuffer results = new StringBuffer();
		results.append(this.createStartTagBody());
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		return (EVAL_BODY_INCLUDE);
	
	}

	public int doEndTag() throws JspException {
			
		final StringBuffer results = new StringBuffer();
		results.append(this.createEndTagBody());
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		
		this.release();
		return (EVAL_PAGE);
	
	}

	public void release() {
		super.release();
		bean=null;
		name = null;
		method_prefix=null;
		startTag_xslt=null;
		endTag_xslt=null;

	}
  
	protected String createStartTagBody() {
	
		final StringBuffer results = new StringBuffer("");
		String forInclude = getStringForInclude(startTag_xslt);
		if(forInclude!=null) results.append(forInclude);			
		return results.toString();
	}

	protected String createEndTagBody() {
		
		final StringBuffer results = new StringBuffer("");
		String forInclude = getStringForInclude(endTag_xslt);
		if(forInclude!=null) results.append(forInclude);			
		return results.toString();
	}
	
	
	private String getStringForInclude(String xslt){
		String result = null;
		if(xslt!=null){
			String xslt_path = null;
			try{
				if(new File(xslt).exists()) xslt_path=xslt;
				else{
					xslt_path = this.pageContext.getServletContext().getRealPath("/")+xslt;
					if(!new File(xslt_path).exists()) xslt_path=null;
				}
			}catch(Exception e){
				
			}
			
			String xml = getXmlFromBean();
			
			if(xslt_path!=null && xml!=null){
				try{
					result = util_xml_xslt.xml_xslt_transform2String(xml, new File(xslt_path));
				}catch(Exception e){
					
				}
			}
			

		}
		return result;
	}
	
	private String getXmlFromBean(){
		String xml = null;
		HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
		i_action formAction=null;
		i_bean formBean=null;
		if(bean!=null){
			@SuppressWarnings("unchecked")
			HashMap<String,i_action> pool = (HashMap<String,i_action>)request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTIONPOOL);
			if(pool!=null) formAction = pool.get(bean);
		}
		if(formAction!=null) bean = null;
		else formAction 	= (i_action)request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTION);		
		if(formAction==null) formAction = new action(); 
		if(bean==null){
			formBean = formAction.get_bean();
			if(formBean!=null)
				formBean=formBean.asBean();
		}

		if(method_prefix==null) method_prefix="get";
		Object writeValue=null;
		Object anotherBean = null;
		
		if(bean==null){
			anotherBean = formBean;
			if(anotherBean!=null){
				if(name==null) writeValue=anotherBean;
				else writeValue = util_reflect.prepareWriteValueForTag(formBean,method_prefix,name,null);
			}
			
		}else{
			
			if(bean.equals(bsConstants.CONST_TAG_REQUESTPARAMETER)) anotherBean = request.getParameter(name);
			if(bean.equals(bsConstants.CONST_TAG_SYSTEMPROPERTY)) anotherBean = System.getProperty(name);
			if(anotherBean==null) anotherBean = request.getAttribute(bean);
			if(anotherBean==null) anotherBean = request.getSession().getAttribute(bean);
			if(anotherBean==null) anotherBean = util_tag.getBeanAsBSTag(bean,this);
			try{
				if(anotherBean==null) anotherBean = (bsController.getFromInfoNavigation(null, request)).find(bean).get_content();
			}catch(Exception e){
			}		
			if(anotherBean==null) anotherBean = bsController.getFromOnlySession(bean, request);
			if(anotherBean==null) anotherBean = bsController.getFromOnlyServletContext(bean, request);
			if(anotherBean==null) anotherBean = bsController.getProperty(bean,request);
			
			if(anotherBean!=null){
				if(name==null) writeValue = anotherBean;
				else{
					try{
						writeValue = util_reflect.prepareWriteValueForTag(anotherBean,method_prefix,name,null);
					}catch(Exception e){
					}
				}	
			}		 		
		}
		
		if(writeValue!=null){
			try{
				xml = util_beanMessageFactory.bean2message(writeValue);
			}catch(Exception e){
				
			}
		}
		
		return xml;
	}

	public String getBean() {
		return bean;
	}

	public void setBean(String bean) {
		this.bean = bean;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMethod_prefix() {
		return method_prefix;
	}

	public void setMethod_prefix(String method_prefix) {
		this.method_prefix = method_prefix;
	}

	public String getStartTag_xslt() {
		return startTag_xslt;
	}

	public void setStartTag_xslt(String startTag_xslt) {
		this.startTag_xslt = startTag_xslt;
	}

	public String getEndTag_xslt() {
		return endTag_xslt;
	}

	public void setEndTag_xslt(String endTag_xslt) {
		this.endTag_xslt = endTag_xslt;
	}

}

