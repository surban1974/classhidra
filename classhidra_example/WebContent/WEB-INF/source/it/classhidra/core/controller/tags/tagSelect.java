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
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.*;
import java.io.IOException;
import java.util.HashMap;


public class tagSelect extends tagInput{
	private static final long serialVersionUID = -1L;
	private String multiple=null;
	public int doEndTag() throws JspException {
			
		StringBuffer results = new StringBuffer("</select>");
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		value=null;
		return EVAL_BODY_INCLUDE;
	
	}
	protected String createTagBody() {
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
		

		
//		if(value==null){
			if(bean==null && name!=null){
				writeValue = formBean.get(name);
				try{
					if(writeValue!=null) value = util_format.makeFormatedString(formatOutput,formatLanguage,formatCountry,writeValue);
				}catch(Exception e){}
			}else{
				Object anotherBean = null;
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
				
				if(anotherBean!=null){
					if(name==null){
						writeValue = anotherBean;
						name=bean;
						try{
							value = util_format.makeFormatedString(formatOutput,formatLanguage,formatCountry,writeValue);
						}catch(Exception e){	 
						}
					}
					else{
						try{
							writeValue = util_reflect.prepareWriteValueForTag(anotherBean,method_prefix,name,null);
							if(writeValue!=null) value = util_format.makeFormatedString(formatOutput,formatLanguage,formatCountry,writeValue);		 
						}catch(Exception e){}
					}	
				}		 		
			}
//		}	
		StringBuffer results = new StringBuffer("<select ");
		if(name!=null){
			results.append(" name=\"");
			results.append(name);
			results.append("\"");
		}
		if(objId!=null){
			results.append(" id=\"");
			results.append(objId);
			results.append("\"");
		}else{
			if(name!=null){
				results.append(" id=\"");
				results.append(name);
				results.append("\"");
			}			
		}
		if (type != null) {
			results.append(" type=\"");
			results.append(type == null ? "text" : type);
			results.append("\"");
		}
		if (readonly != null) {
			results.append(" readonly=\"");
			results.append(readonly);
			results.append("\"");
		}
		if (size != null) {
			results.append(" size=\"");
			results.append(size);
			results.append("\"");
		}
		if (src != null) {
			results.append(" src=\"");
			results.append(src);
			results.append("\"");
		}
		if (tabindex != null) {
			results.append(" tabindex=\"");
			results.append(tabindex);
			results.append("\"");
		}
		if (usemap != null) {
			results.append(" usemap=\"");
			results.append(usemap);
			results.append("\"");
		}
		if (accesskey != null) {
			results.append(" accesskey=\"");
			results.append(accesskey);
			results.append("\"");
		}
		if (align != null) {
			results.append(" align=\"");
			results.append(align);
			results.append("\"");
		}
		if (alt != null) {
			results.append(" alt=\"");
			results.append(alt);
			results.append("\"");
		}
		if (border != null) {
			results.append(" border=\"");
			results.append(border);
			results.append("\"");
		}
		if (checked != null) {
			results.append(" checked=\"");
			results.append(checked);
			results.append("\"");
		}
		if (disabled != null) {
			results.append(" disabled=\"");
			results.append(disabled);
			results.append("\"");
		}
		if (width != null) {
			results.append(" width=\"");
			results.append(width);
			results.append("\"");
		}
		if (height != null) {
			results.append(" height=\"");
			results.append(height);
			results.append("\"");
		}
		if (ismap != null) {
			results.append(" ismap=\"");
			results.append(ismap);
			results.append("\"");
		}
		if (maxlength != null) {
			results.append(" maxlength=\"");
			results.append(maxlength);
			results.append("\"");
		}
		if (multiple != null) {
			results.append(" multiple =\"");
			results.append(multiple);
			results.append("\"");
		}		
		
		if(value!=null)
			if(formatOutput!=null) value=util_format.prepareContentString(formatOutput,value);
			if(replaceOnBlank != null) value=util_format.replace(value,replaceOnBlank,"");

			try{			
				results.append(" value=\"");
				results.append(value);
				results.append("\"");
		}catch(Exception e){}	
			
		if (styleClass != null) {
			results.append(" class=\"");
			results.append(styleClass);
			results.append("\"");
		}
		if (style != null) {
			results.append(" style=\"");
			results.append(style);
			results.append("\"");
		}
		if (accept != null) {
			results.append(" accept=\"");
			results.append(accept);
			results.append("\"");
		}
		if (lang != null) {
			results.append(" lang=\"");
			results.append(lang);
			results.append("\"");
		}
		if (title != null) {
			results.append(" title=\"");
			results.append(title);
			results.append("\"");
		}
		if (dir != null) {
			results.append(" dir=\"");
			results.append(dir);
			results.append("\"");
		}
		if (onclick != null) {
			results.append(" onclick=\"");
			results.append(onclick);
			results.append("\"");
		}
		if (ondblclick != null) {
			results.append(" ondblclick=\"");
			results.append(ondblclick);
			results.append("\"");
		}
		if (onhelp != null) {
			results.append(" onhelp=\"");
			results.append(onhelp);
			results.append("\"");
		}
		
		if (onkeydown != null) {
			results.append(" onkeydown=\"");
			results.append(onkeydown);
			results.append("\"");
		}
		if (onkeypress != null) {
			results.append(" onkeypress=\"");
			results.append(onkeypress);
			results.append("\"");
		}
		if (onkeyup != null) {
			results.append(" onkeyup=\"");
			results.append(onkeyup);
			results.append("\"");
		}
		if (onmousedown != null) {
			results.append(" onmousedown=\"");
			results.append(onmousedown);
			results.append("\"");
		}
	
		if (onmousemove != null) {
			results.append(" onmousemove=\"");
			results.append(onmousemove);
			results.append("\"");
		}
		if (onmouseout != null) {
			results.append(" onmouseout=\"");
			results.append(onmouseout);
			results.append("\"");
		}
		if (onmouseover != null) {
			results.append(" onmouseover=\"");
			results.append(onmouseover);
			results.append("\"");
		}
		if (onmouseup != null) {
			results.append(" onmouseup=\"");
			results.append(onmouseup);
			results.append("\"");
		}
		if (onblur != null) {
			results.append(" onblur=\"");
			results.append(onblur);
			results.append("\"");
		}
		if (onchange != null) {
			results.append(" onchange=\"");
			results.append("this.value=this[this.selectedIndex].value; "+onchange);
			results.append("\"");
		}else{
			results.append(" onchange=\"");
			results.append("this.value = this[this.selectedIndex].value; ");
			results.append("\"");
		}
		if (onfocus != null) {
			results.append(" onfocus=\"");
			results.append(onfocus);
			results.append("\"");
		}
		if (onselect != null) {
			results.append(" onselect=\"");
			results.append(onselect);
			results.append("\"");
		}
		
		results.append(">");
		
		if(name!=null && formatInput!=null){
			results.append("<input name=\"");
			results.append("$format_"+name);
			results.append("\" type=\"hidden\" value=\"");
			results.append(formatInput);
			results.append("\">");
		}

		if(name!=null && replaceOnBlank!=null){
			results.append("<input name=\"");
			results.append("$replaceOnBlank_"+name);
			results.append("\" type=\"hidden\" value=\"");
			results.append(replaceOnBlank);
			results.append("\">");
		}
		if(name!=null && replaceOnErrorFormat!=null){
			results.append("<input name=\"");
			results.append("$replaceOnErrorFormat_"+name);
			results.append("\" type=\"hidden\" value=\"");
			results.append(replaceOnErrorFormat);
			results.append("\">");
		}				
		return results.toString();
	}
	public int doStartTag() throws JspException {
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
	public void release() {
		super.release();
		multiple=null;
	}
	public String getMultiple() {
		return multiple;
	}
	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

}

