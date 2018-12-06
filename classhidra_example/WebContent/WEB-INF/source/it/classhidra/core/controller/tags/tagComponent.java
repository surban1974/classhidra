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
import it.classhidra.core.controller.i_tag_helper;
import it.classhidra.core.tool.exception.bsTagEndRendering;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.DynamicAttributes;

public class tagComponent extends ClTagSupport implements DynamicAttributes {
	private static final long serialVersionUID = 1L;
	protected String objId = null;// id
	protected String linkedId = null;
	protected String styleClass=null;
	protected String bean = null;
	protected String domelement = null;
	protected String rendering = null;

	protected Map tagAttributes = new HashMap();

	
	public int doEndTag() throws JspException {		
		final StringBuffer results = new StringBuffer();
		results.append(this.createEndTagBody());
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		
		if(objId!=null || linkedId!=null) {
			final HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
			String componentId = (String)request.getAttribute(i_tag_helper.CONST_TAG_COMPONENT_ID);
			if(componentId!=null && componentId.equals(objId)) {
				PageContext pageContext = (PageContext)request.getAttribute(i_tag_helper.CONST_TAG_PAGE_CONTEXT);
				if(pageContext!=null) {
					try {
						pageContext.getOut().write(this.createTagBody());
						if(this.getBodyContent()!=null)
							pageContext.getOut().write(this.getBodyContent().getString());
						pageContext.getOut().write(this.createEndTagBody());
						throw new bsTagEndRendering(objId);
					}catch(Exception e) {
						if(e instanceof bsTagEndRendering)
							throw (bsTagEndRendering)e;
					}
				}
				request.removeAttribute(i_tag_helper.CONST_TAG_COMPONENT_ID);
			}else if(componentId!=null && componentId.equals(linkedId)) {
				PageContext pageContext = (PageContext)request.getAttribute(i_tag_helper.CONST_TAG_PAGE_CONTEXT);
				if(pageContext!=null) {
					try {
						if(this.getBodyContent()!=null)
							pageContext.getOut().write(this.getBodyContent().getString());
						throw new bsTagEndRendering(linkedId);
					}catch(Exception e) {
						if(e instanceof bsTagEndRendering)
							throw (bsTagEndRendering)e;
					}
				}
				request.removeAttribute(i_tag_helper.CONST_TAG_COMPONENT_ID);
			}
			
		}

		this.release();
		return super.doEndTag(); 
	}

	public int doStartTag() throws JspException {
	
		if(objId!=null || linkedId!=null) {
			final HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
			
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
	
			
			renderComponent(
					formBean,
					formAction,
					this.getClass().getName(),
					((objId!=null)?objId:((linkedId!=null)?linkedId:"")),
					(rendering!=null && rendering.equalsIgnoreCase(i_tag_helper.CONST_TAG_RENDERING_FULL))?true:false);
		}
		
		final StringBuffer results = new StringBuffer();
		results.append(this.createTagBody());
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		
		if(objId!=null || linkedId!=null) {
			final HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
			String componentId = (String)request.getAttribute(i_tag_helper.CONST_TAG_COMPONENT_ID);
			if(componentId!=null && (componentId.equals(objId) || componentId.equals(linkedId))) {		
				return EVAL_BODY_BUFFERED;
			}
		}
		return EVAL_BODY_INCLUDE; 
	}

	public void release() {
		super.release();
		objId = null;
		linkedId = null;
		styleClass=null;
		bean=null;
		domelement=null;
		rendering=null;
		tagAttributes = new HashMap();
	}
  
	protected String createTagBody() {
	
		final StringBuffer results = new StringBuffer("");
		if(objId!=null){
			if(domelement!=null)
				results.append(" <"+domelement);
			else
				results.append(" <span");
			
			results.append(" id=\"");
			results.append(objId);
			results.append('"');
					
			if(styleClass!=null){
				results.append(" class=\"");
				results.append(styleClass);
				results.append("\"");
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
		return results.toString();

	}

	protected String createEndTagBody() {
		
		final StringBuffer results = new StringBuffer("");
		if(objId!=null) {
			if(domelement!=null)
				results.append("</"+domelement+">");
			else
				results.append("</span>");
		}
		return results.toString();

	}
	
	
	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String string) {
		styleClass = string;
	}

	
	public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
		tagAttributes.put(localName, value);
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getBean() {
		return bean;
	}

	public void setBean(String bean) {
		this.bean = bean;
	}

	public String getDomelement() {
		return domelement;
	}

	public void setDomelement(String domelement) {
		this.domelement = domelement;
	}

	public String getLinkedId() {
		return linkedId;
	}

	public void setLinkedId(String linkedId) {
		this.linkedId = linkedId;
	}

	public String getRendering() {
		return rendering;
	}

	public void setRendering(String rendering) {
		this.rendering = rendering;
	}


}

