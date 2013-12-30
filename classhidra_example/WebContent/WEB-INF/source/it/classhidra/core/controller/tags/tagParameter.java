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


import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.info_navigation;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_tag;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


public class tagParameter extends TagSupport{
	private static final long serialVersionUID = 1L;
	protected String name = null;	
	protected String source=null;
	protected String property=null;
	protected String method_prefix=null;
	protected String value=null;

	public int doStartTag() throws JspException {
		try{
			HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
			i_action formAction=null;
			i_bean formBean=null;
			if(source!=null){
				HashMap pool = (HashMap)request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTIONPOOL);
				if(pool!=null) formAction = (i_action)pool.get(source);
			}
			if(formAction!=null) source = null;
			else formAction 	= (i_action)request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTION);		
			if(formAction==null) formAction = new action(); 
			if(source==null) formBean = formAction.get_bean();
			
			if(value!=null){
				
			}
			if(value==null){
				Object anotherBean=null;
				
				if(method_prefix==null) method_prefix="get";
					
				if(source==null)
					anotherBean = formBean;
				else{
					if(source.equals(bsConstants.CONST_TAG_REQUESTPARAMETER)) anotherBean = request.getParameter(property);
					if(source.equals(bsConstants.CONST_TAG_SYSTEMPROPERTY)) anotherBean = System.getProperty(property);
					if(anotherBean==null) anotherBean = request.getAttribute(source);
					if(anotherBean==null) anotherBean  = request.getSession().getAttribute(source); 
					if(anotherBean==null) anotherBean  = request.getSession().getServletContext().getAttribute(source); 
					if(anotherBean==null) anotherBean = util_tag.getBeanAsBSTag(source,this);
					try{
						if(anotherBean==null) anotherBean = ((info_navigation)request.getSession().getAttribute(bsConstants.CONST_BEAN_$NAVIGATION)).find(source).get_content();
					}catch(Exception e){
					}					
					
				}
				
				if(anotherBean==null) return EVAL_BODY_INCLUDE;
				
				Object obj = null;
				if(property!=null){
					obj = util_reflect.prepareWriteValueForTag(anotherBean,method_prefix,property,null);
				}else obj = anotherBean;	
				if(obj!=null) value=obj.toString();
			}
			try{
				Object obj = getParent();
				if(obj!=null){
					HashMap parameters = (HashMap)util_reflect.prepareWriteValueForTag(obj,"get","parameters",null);
					if(value!=null) parameters.put(name,value);
					else  parameters.put(name,"");
				}
			}catch(Exception e){
			}

		}catch(Exception e){		
		}		
		return EVAL_BODY_INCLUDE; 
		

	}
	public int doEndTag() throws JspException {
		value=null;
		return super.doEndTag();
	}
	public void release() {
		super.release();
		name=null;
		source=null;
		property=null;
		method_prefix=null;
		value=null;
	}
	public String getName() {
		return name;
	}
	public String getProperty() {
		return property;
	}
	public String getSource() {
		return source;
	}
	public void setName(String string) {
		name = string;
	}
	public void setProperty(String string) {
		property = string;
	}
	public void setSource(String string) {
		source = string;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMethod_prefix() {
		return method_prefix;
	}

	public void setMethod_prefix(String method_prefix) {
		this.method_prefix = method_prefix;
	}



}

