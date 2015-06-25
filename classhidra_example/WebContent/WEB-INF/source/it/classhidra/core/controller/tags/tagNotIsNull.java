/**
* Creation date: (10/04/2015)
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



import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_tag;


public class tagNotIsNull extends  TagSupport {
	private static final long serialVersionUID = 1L;
	protected String bean=null;
	protected String name=null;
	protected String method_prefix=null;
	
	public int doStartTag() throws JspException {
		if (condition())
			return (EVAL_BODY_INCLUDE);
		else
			return (SKIP_BODY);
	}

	public int doEndTag() throws JspException {
		return (EVAL_PAGE);
	}
	public void release() {
		super.release();
		bean = null;
		name = null;
		method_prefix=null;

	}
	
	private boolean condition() throws JspException{
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
		if(bean==null) formBean = formAction.get_bean();
		
		if(method_prefix==null) method_prefix="get";
		Object writeValue=null;
		Object anotherBean = null;
		if(bean==null && name!=null){

			anotherBean = formBean;			
			writeValue = util_reflect.prepareWriteValueForTag(formBean,method_prefix,name,null);
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
			if(anotherBean==null) anotherBean = bsController.getProperty(bean,request);
			

			if(anotherBean!=null){
				if(name==null) writeValue = anotherBean.toString();
				else{
					try{
						writeValue = util_reflect.prepareWriteValueForTag(anotherBean,method_prefix,name,null);					 
					}catch(Exception e){
					}
				}	
			}		 		
		}
		


		
		if(writeValue!=null){
			if(getParent()!=null && getParent() instanceof tagSwitch)
				((tagSwitch)getParent()).setConditionBreak(true);
			return true;
		}
		return false;
	}
	public String getName() {
		return name;
	}
	public void setName(String string) {
		name = string;
	}
	public String getBean() {
		return bean;
	}
	public void setBean(String string) {
		bean = string;
	}

	public String getMethod_prefix() {
		return method_prefix;
	}

	public void setMethod_prefix(String string) {
		method_prefix = string;
	}

}
