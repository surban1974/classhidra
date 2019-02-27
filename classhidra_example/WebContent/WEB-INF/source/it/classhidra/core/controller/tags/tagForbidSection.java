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
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.controller.info_action;
import it.classhidra.core.controller.info_redirect;
import it.classhidra.core.init.auth_init;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.*;
import java.io.IOException;
import java.util.HashMap;

public class tagForbidSection extends  ClTagSupport {
	private static final long serialVersionUID = -1L;
	protected String bean=null;
	protected String name=null;
	protected String trace=null;

	public int doStartTag() throws JspException {
		try {
			if(trace==null || trace.equalsIgnoreCase("true"))pageContext.getOut().print("<!--START FORBIDDEN BOOKMARK NAME=\""+name+"\"-->");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		if (condition())
			return (EVAL_BODY_INCLUDE);
		else
			return (SKIP_BODY);
	}

	public int doEndTag() throws JspException {
		try {
			if(trace==null || trace.equalsIgnoreCase("true"))pageContext.getOut().print("<!--FINISH FORBIDDEN BOOKMARK NAME=\""+name+"\"-->");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		
		this.release();
		return (EVAL_PAGE);
	}
	public void release() {
		super.release();
		bean = null;
		name = null;
		trace = null;
	}
	private boolean condition() throws JspException{
		boolean result=true;
		HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
		i_action formAction=null;
		i_bean formBean=null;
		
		if(bean!=null)
			bean=checkParametersIfDynamic(bean, null);
		
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

		redirects 		formRedirect 	=  formAction.getCurrent_redirect();
		try{
			auth_init auth = bsController.checkAuth_init(request);
			info_action i_a = (info_action)auth.get_actions_permitted().get(formAction.get_infoaction().getPath());
			info_redirect i_r = (info_redirect)i_a.get_auth_redirects().get(formRedirect.get_inforedirect().getAuth_id());


			if(name!=null)
				name=checkParametersIfDynamic(name, null);
			
			if(name!=null &&
				i_r!=null &&
				i_r.get_sections()!=null &&
				i_r.get_sections().get(name)!=null) result = false;
			}catch(Exception e){

			}

		return result;
	}
	public String getName() {
		return name;
	}
	public void setName(String string) {
		name = string;
	}
	public String getTrace() {
		return trace;
	}
	public void setTrace(String string) {
		trace = string;
	}

	public String getBean() {
		return bean;
	}

	public void setBean(String bean) {
		this.bean = bean;
	}
}