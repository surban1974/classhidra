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
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.IOException;
import java.util.HashMap;

public class tagInput extends BodyTagSupport{
	private static final long serialVersionUID = -1L;
	protected String bean = null;
	protected String objId = null;// id
	protected String name = null;
	protected String accept = null;
	protected String styleClass=null;
	protected String lang = null;
	protected String style = null;
	protected String title = null;
	protected String dir=null;//"ltr"

	protected String type = null;//"text"
	protected String readonly = null;//"readonly"
	protected String size = null;
	protected String src = null;
	protected String tabindex = null;
	protected String usemap = null;
	protected String accesskey = null;
	protected String align = null;//"top"
	protected String alt = null;
	protected String border = null;
	protected String checked = null;//"checked"
	protected String disabled = null;//"disabled"
	protected String width = null;
	protected String height = null;
	protected String value = null;
	protected String ismap = null;//"ismap"
	protected String maxlength = null;

	protected String autocomplete = null;
	protected String autofocus = null;
	protected String form = null;
	protected String formaction = null;
	protected String formenctype = null;
	protected String formmethod = null;
	protected String formnovalidate = null;
	protected String formtarget = null;
	protected String list = null;
	protected String max = null;
	protected String min = null;
	protected String multiple = null;
	protected String pattern = null;
	protected String placeholder = null;
	protected String required = null;
	protected String step = null;




	protected String formatInput = null;
	protected String formatOutput = null;
	protected String formatLanguage=null;
	protected String formatCountry=null;
	protected String toUpperCase = null;
	protected String toTrim = null;



	protected String onclick = null;
	protected String ondblclick = null;
	protected String onhelp = null;
	protected String onkeydown = null;
	protected String onkeypress = null;
	protected String onkeyup = null;
	protected String onmousedown = null;
	protected String onmousemove = null;
	protected String onmouseout = null;
	protected String onmouseover = null;
	protected String onmouseup = null;
	protected String onblur = null;
	protected String onchange = null;
	protected String onfocus = null;
	protected String onselect = null;



	protected String oncontextmenu = null;
	protected String onformchange = null;
	protected String onforminput = null;
	protected String oninput = null;
	protected String oninvalid = null;
	protected String ondrag = null;
	protected String ondragend = null;
	protected String ondragenter = null;
	protected String ondragleave = null;
	protected String ondragover = null;
	protected String ondragstart = null;
	protected String ondrop = null;
	protected String onmousewheel = null;

	protected String clear = null;

	protected String method_prefix=null;
	protected String checkedvalue=null;
	protected String replaceOnBlank=null;
	protected String replaceOnErrorFormat=null;
	
	protected String solveBeanName=null;
	protected String asyncUpdate=null;
	protected String asyncUpdateJsFunction=null;

	public int doStartTag() throws JspException {
		StringBuffer results = new StringBuffer();
		results.append(this.createTagBody());
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		value=null;
		return EVAL_BODY_INCLUDE;
	}

	public void release() {
		super.release();
		bean=null;
		name=null;
		objId=null;
		accept=null;
		styleClass=null;
		lang=null;
		style=null;
		title=null;
		dir=null;//"ltr"

		type = null;//"text"
		readonly = null;//"readonly"
		size = null;
		src = null;
		tabindex = null;
		usemap = null;
		accesskey = null;
		align = null;//"top"
		alt = null;
		border = null;
		checked = null;//"checked"
		disabled = null;//"disabled"
		width = null;
		height = null;
		value = null;
		ismap = null;//"ismap"
		maxlength = null;
		formatInput = null;
		formatOutput = null;
		formatLanguage=null;
		formatCountry=null;

		autocomplete = null;
		autofocus = null;
		form = null;
		formaction = null;
		formenctype = null;
		formmethod = null;
		formnovalidate = null;
		formtarget = null;
		list = null;
		max = null;
		min = null;
		multiple = null;
		pattern = null;
		placeholder = null;
		required = null;
		step = null;


		onclick=null;
		ondblclick=null;
		onhelp=null;
		onkeydown=null;
		onkeypress=null;
		onkeyup=null;
		onmousedown=null;
		onmousemove=null;
		onmouseout=null;
		onmouseover=null;
		onmouseup=null;
		onblur = null;
		onchange = null;
		onfocus = null;
		onselect = null;

		oncontextmenu = null;
		onformchange = null;
		onforminput = null;
		oninput = null;
		oninvalid = null;
		ondrag = null;
		ondragend = null;
		ondragenter = null;
		ondragleave = null;
		ondragover = null;
		ondragstart = null;
		ondrop = null;
		onmousewheel = null;

		clear = "false";
		toUpperCase = null;
		toTrim = null;
		method_prefix=null;
		checkedvalue=null;

		replaceOnBlank=null;
		replaceOnErrorFormat=null;
		
		solveBeanName=null;
		asyncUpdate=null;
		asyncUpdateJsFunction=null;
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
		Object anotherBean = null;
		Object writeValue=null;
		
		String prefixName=null;
		
		if(bean!=null && request.getAttribute(tagBean.CONST_HEAP_BEANS)!=null && ((HashMap)request.getAttribute(tagBean.CONST_HEAP_BEANS)).get(bean)!=null){
			prefixName = ((HashMap)request.getAttribute(tagBean.CONST_HEAP_BEANS)).get(bean).toString();
		}
		if(prefixName==null)
			prefixName=name;
		else prefixName+="."+name;

		String asyncUpdateUrl=null;

//		value="";

		if(value==null){
			if(bean==null && name!=null){
				anotherBean = formBean;
			}else{
				try{
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
						if(anotherBean==null) anotherBean = ((info_navigation)request.getSession().getAttribute(bsConstants.CONST_BEAN_$NAVIGATION)).find(bean).get_content();
					}catch(Exception e){
					}					
				}catch(Exception e){
				}
			}


			if(anotherBean!=null){
				if(name==null){
					writeValue = anotherBean;
					name=bean;
					try{
						value = util_format.makeFormatedString(formatOutput,formatLanguage,formatCountry, writeValue);
					}catch(Exception e){
					}
				}else{
					try{
						writeValue = util_reflect.prepareWriteValueForTag(anotherBean,method_prefix,name,null);
						if(writeValue!=null) value = util_format.makeFormatedString(formatOutput,formatLanguage,formatCountry,writeValue);
					}catch(Exception e){}
				}
			}else{
				if(name==null && bean!=null) name=bean;
			}
		}


		
		if(asyncUpdate!=null && !asyncUpdate.equalsIgnoreCase("false")){
			try{
				asyncUpdateUrl=formAction.get_infoaction().getPath();
				if(bsController.getAppInit().get_actioncall_separator()!=null && !bsController.getAppInit().get_actioncall_separator().equals("")){
					if(!asyncUpdate.equalsIgnoreCase("true"))
						asyncUpdateUrl+=bsController.getAppInit().get_actioncall_separator()+asyncUpdate+"?";
					else
						asyncUpdateUrl+=bsController.getAppInit().get_actioncall_separator()+"asyncupdate?";
				}else asyncUpdateUrl+="?";
				asyncUpdateUrl+="middleAction=undef&";
				if(name!=null){
					if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
						asyncUpdateUrl+=prefixName+"='+this.value+'&target="+prefixName+"&";
					else asyncUpdateUrl+=name+"='+this.value+'&target="+name+"&";
					
					if(formatInput!=null){
						if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
							asyncUpdateUrl+="$format_"+prefixName+"="+formatInput+"&";
						else asyncUpdateUrl+="$format_"+name+"="+formatInput+"&";
					}
					if(formatOutput!=null){
						if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
							asyncUpdateUrl+="$formatOutput_"+prefixName+"="+formatOutput+"&";
						else asyncUpdateUrl+="$formatOutput_"+name+"="+formatOutput+"&";
					}	
					if(formatLanguage!=null){
						if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
							asyncUpdateUrl+="$formatLanguage_"+prefixName+"="+formatLanguage+"&";
						else asyncUpdateUrl+="$formatLanguage_"+name+"="+formatLanguage+"&";
					}
					if(formatCountry!=null){
						if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
							asyncUpdateUrl+="$formatCountry_"+prefixName+"="+formatCountry+"&";
						else asyncUpdateUrl+="$formatCountry_"+name+"="+formatCountry+"&";
					}					
					if(replaceOnBlank!=null){
						if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
							asyncUpdateUrl+="$replaceOnBlank_"+prefixName+"="+replaceOnBlank+"&";
						else asyncUpdateUrl+="$replaceOnBlank_"+name+"="+replaceOnBlank+"&";
					}
					if(replaceOnErrorFormat!=null){
						if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
							asyncUpdateUrl+="$replaceOnErrorFormat_"+prefixName+"="+replaceOnErrorFormat+"&";
						else asyncUpdateUrl+="$replaceOnErrorFormat_"+name+"="+replaceOnErrorFormat+"&";
					}
				}
			}catch(Exception e){
			}
		}
		
		StringBuffer results = new StringBuffer("<input ");
		if(name!=null){
			results.append(" name=\"");
			if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
				results.append(prefixName);
			else results.append(name);
			results.append('"');
		}
		if(objId!=null){
			results.append(" id=\"");
			results.append(objId);
			results.append('"');
		}else{
			if(name!=null){
				results.append(" id=\"");
				if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
					results.append(prefixName);
				else results.append(name);
				results.append('"');
			}
		}
		if (type != null) {
			results.append(" type=\"");
			results.append(type == null ? "text" : type);
			results.append('"');
		}
		if (readonly != null) {
			results.append(" readonly=\"");
			results.append(readonly);
			results.append('"');
		}
		if (size != null) {
			results.append(" size=\"");
			results.append(size);
			results.append('"');
		}
		if (src != null) {
			results.append(" src=\"");
			results.append(src);
			results.append('"');
		}
		if (tabindex != null) {
			results.append(" tabindex=\"");
			results.append(tabindex);
			results.append('"');
		}
		if (usemap != null) {
			results.append(" usemap=\"");
			results.append(usemap);
			results.append('"');
		}
		if (accesskey != null) {
			results.append(" accesskey=\"");
			results.append(accesskey);
			results.append('"');
		}
		if (align != null) {
			results.append(" align=\"");
			results.append(align);
			results.append('"');
		}
		if (alt != null) {
			results.append(" alt=\"");
			results.append(alt);
			results.append('"');
		}
		if (border != null) {
			results.append(" border=\"");
			results.append(border);
			results.append('"');
		}
		if (checked != null) {
			results.append(" checked=\"");
			results.append(checked);
			results.append('"');
		}else{
			
			if ( formBean != null &&
					 type != null && (
					 type.equalsIgnoreCase("radio") || type.equalsIgnoreCase("checkbox")) &&
					 checkedvalue !=null &&
					 (solveBeanName==null || !solveBeanName.equalsIgnoreCase("true")) &&
					 checkedvalue.equalsIgnoreCase(formBean.get(name).toString()))
				
					results.append(" checked=\"checked\"");

			if ( formBean != null &&
					 type != null && (
					 type.equalsIgnoreCase("radio") || type.equalsIgnoreCase("checkbox")) &&
					 checkedvalue !=null &&
					 (solveBeanName!=null && solveBeanName.equalsIgnoreCase("true")) &&
					 checkedvalue.equalsIgnoreCase(formBean.get(prefixName).toString()))
				
					results.append(" checked=\"checked\"");
			

		}
		if (disabled != null) {
			results.append(" disabled=\"");
			results.append(disabled);
			results.append('"');
		}
		if (width != null) {
			results.append(" width=\"");
			results.append(width);
			results.append('"');
		}
		if (height != null) {
			results.append(" height=\"");
			results.append(height);
			results.append('"');
		}
		if (ismap != null) {
			results.append(" ismap=\"");
			results.append(ismap);
			results.append('"');
		}
		if (maxlength != null) {
			results.append(" maxlength=\"");
			results.append(maxlength);
			results.append('"');
		}



		if (autocomplete != null) {
			results.append(" autocomplete=\"");
			results.append(autocomplete);
			results.append('"');
		}
		if (autofocus != null) {
			results.append(" autofocus=\"");
			results.append(autofocus);
			results.append('"');
		}
		if (form != null) {
			results.append(" form=\"");
			results.append(form);
			results.append('"');
		}
		if (formaction != null) {
			results.append(" formaction=\"");
			results.append(formaction);
			results.append('"');
		}
		if (formenctype != null) {
			results.append(" formenctype=\"");
			results.append(formenctype);
			results.append('"');
		}
		if (formmethod != null) {
			results.append(" formmethod=\"");
			results.append(formmethod);
			results.append('"');
		}
		if (formnovalidate != null) {
			results.append(" formnovalidate=\"");
			results.append(formnovalidate);
			results.append('"');
		}
		if (formtarget != null) {
			results.append(" formtarget=\"");
			results.append(formtarget);
			results.append('"');
		}
		if (list != null) {
			results.append(" list=\"");
			results.append(list);
			results.append('"');
		}
		if (max != null) {
			results.append(" max=\"");
			results.append(max);
			results.append('"');
		}
		if (min != null) {
			results.append(" min=\"");
			results.append(min);
			results.append('"');
		}
		if (multiple != null) {
			results.append(" multiple=\"");
			results.append(multiple);
			results.append('"');
		}
		if (pattern != null) {
			results.append(" pattern=\"");
			results.append(pattern);
			results.append('"');
		}
		if (placeholder != null) {
			results.append(" placeholder=\"");
			results.append(placeholder);
			results.append('"');
		}
		if (required != null) {
			results.append(" required=\"");
			results.append(required);
			results.append('"');
		}
		if (step != null) {
			results.append(" step=\"");
			results.append(step);
			results.append('"');
		}


		if(value!=null)
			try{
				results.append(" value=\"");
				if(replaceOnBlank != null) value=util_format.replace(value,replaceOnBlank,"");
				if ( clear != null && clear.equalsIgnoreCase("true") && value.equalsIgnoreCase("0")) value="";

				results.append(value);
				results.append('"');
				if ( formBean != null &&
					 type != null && (
					 type.equalsIgnoreCase("radio") || type.equalsIgnoreCase("checkbox")) &&
					 (solveBeanName==null || !solveBeanName.equalsIgnoreCase("true")) &&
					 checkedvalue.equalsIgnoreCase(formBean.get(name).toString()))

					results.append(" checked=\"checked\"");

				if ( formBean != null &&
						 type != null && (
						 type.equalsIgnoreCase("radio") || type.equalsIgnoreCase("checkbox")) &&
						 (solveBeanName!=null && solveBeanName.equalsIgnoreCase("true")) &&
						 checkedvalue.equalsIgnoreCase(formBean.get(prefixName).toString()))

						results.append(" checked=\"checked\"");
				
				

		}catch(Exception e){}
		
		

		if (styleClass != null) {
			results.append(" class=\"");
			results.append(styleClass);
			results.append('"');
		}
		if (style != null) {
			results.append(" style=\"");
			results.append(style);
			results.append('"');
		}
		if (accept != null) {
			results.append(" accept=\"");
			results.append(accept);
			results.append('"');
		}
		if (lang != null) {
			results.append(" lang=\"");
			results.append(lang);
			results.append('"');
		}
		if (title != null) {
			results.append(" title=\"");
			results.append(title);
			results.append('"');
		}
		if (dir != null) {
			results.append(" dir=\"");
			results.append(dir);
			results.append('"');
		}
		if (onclick != null) {
			results.append(" onclick=\"");
			results.append(onclick);
			results.append('"');
		}
		if (ondblclick != null) {
			results.append(" ondblclick=\"");
			results.append(ondblclick);
			results.append('"');
		}
		if (onhelp != null) {
			results.append(" onhelp=\"");
			results.append(onhelp);
			results.append('"');
		}

		if (onkeydown != null) {
			results.append(" onkeydown=\"");
			results.append(onkeydown);
			results.append('"');
		}
		if (onkeypress != null) {
			results.append(" onkeypress=\"");
			results.append(onkeypress);
			results.append('"');
		}
		if (onkeyup != null) {
			results.append(" onkeyup=\"");
			results.append(onkeyup);
			results.append('"');
		}
		if (onmousedown != null) {
			results.append(" onmousedown=\"");
			results.append(onmousedown);
			results.append('"');
		}

		if (onmousemove != null) {
			results.append(" onmousemove=\"");
			results.append(onmousemove);
			results.append('"');
		}
		if (onmouseout != null) {
			results.append(" onmouseout=\"");
			results.append(onmouseout);
			results.append('"');
		}
		if (onmouseover != null) {
			results.append(" onmouseover=\"");
			results.append(onmouseover);
			results.append('"');
		}
		if (onmouseup != null) {
			results.append(" onmouseup=\"");
			results.append(onmouseup);
			results.append('"');
		}
		if (onblur != null) {
			results.append(" onblur=\"");
			if(toUpperCase!=null && (toUpperCase.toUpperCase().equals("NO") || toUpperCase.toUpperCase().equals("FALSE"))){
				if(toTrim!=null && (toTrim.toUpperCase().equals("NO") || toTrim.toUpperCase().equals("FALSE"))){
				}else results.append("this.value=trim(this.value); ");
			}
			else{
				if(toTrim!=null && (toTrim.toUpperCase().equals("NO") || toTrim.toUpperCase().equals("FALSE")))
					results.append("this.value.toUpperCase(); ");
				else results.append("this.value=trim(this.value.toUpperCase()); ");
			}
			results.append(onblur);
			results.append('"');
		}else{
			results.append(" onblur=\"");
			if(toUpperCase!=null && (toUpperCase.toUpperCase().equals("NO") || toUpperCase.toUpperCase().equals("FALSE"))){
				if(toTrim!=null && (toTrim.toUpperCase().equals("NO") || toTrim.toUpperCase().equals("FALSE"))){
				}else results.append("this.value=trim(this.value); ");
			}else{
				if(toTrim!=null && (toTrim.toUpperCase().equals("NO") || toTrim.toUpperCase().equals("FALSE")))
					results.append("this.value.toUpperCase(); ");
				else results.append("this.value=trim(this.value.toUpperCase()); ");
			}
			results.append('"');
		}

		if (onchange != null) {
			results.append(" onchange=\"");
			if(asyncUpdateUrl!=null){
				if(asyncUpdateJsFunction!=null)
					results.append(asyncUpdateJsFunction+"('"+asyncUpdateUrl+"',this.name);");
				else results.append("dhtmlLoadScript('"+asyncUpdateUrl+"');");
			}
			results.append(onchange);
			results.append('"');
		}else{
			if(asyncUpdateUrl!=null){
				results.append(" onchange=\"");
				if(asyncUpdateJsFunction!=null)
					results.append(asyncUpdateJsFunction+"('"+asyncUpdateUrl+"',this.name);");
				else results.append("dhtmlLoadScript('"+asyncUpdateUrl+"');");
				results.append('"');
			}
		}
		
		if (onfocus != null) {
			results.append(" onfocus=\"");
			results.append(onfocus);
			results.append('"');
		}
		if (onselect != null) {
			results.append(" onselect=\"");
			results.append(onselect);
			results.append('"');
		}

		if (oncontextmenu != null) {
			results.append(" oncontextmenu=\"");
			results.append(oncontextmenu);
			results.append('"');
		}
		if (onformchange != null) {
			results.append(" onformchange=\"");
			results.append(onformchange);
			results.append('"');
		}
		if (onforminput != null) {
			results.append(" onforminput=\"");
			results.append(onforminput);
			results.append('"');
		}
		if (oninput != null) {
			results.append(" oninput=\"");
			results.append(oninput);
			results.append('"');
		}
		if (oninvalid != null) {
			results.append(" oninvalid=\"");
			results.append(oninvalid);
			results.append('"');
		}

		if (ondrag != null) {
			results.append(" ondrag=\"");
			results.append(ondrag);
			results.append('"');
		}
		if (ondragend != null) {
			results.append(" ondragend=\"");
			results.append(ondragend);
			results.append('"');
		}
		if (ondragenter != null) {
			results.append(" ondragenter=\"");
			results.append(ondragenter);
			results.append('"');
		}
		if (ondragleave != null) {
			results.append(" ondragleave=\"");
			results.append(ondragleave);
			results.append('"');
		}
		if (ondragover != null) {
			results.append(" ondragover=\"");
			results.append(ondragover);
			results.append('"');
		}
		if (ondragstart != null) {
			results.append(" ondragstart=\"");
			results.append(ondragstart);
			results.append('"');
		}
		if (ondrop != null) {
			results.append(" ondrop=\"");
			results.append(ondrop);
			results.append('"');
		}
		if (onmousewheel != null) {
			results.append(" onmousewheel=\"");
			results.append(onmousewheel);
			results.append('"');
		}
		results.append(" $modelWire=\"");
		results.append("input:"+prefixName);
		results.append('"');


		
		results.append('>');

		if(name!=null && formatInput!=null){
			results.append("<input name=\"");
			if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
				results.append("$format_"+prefixName);
			else results.append("$format_"+name);
			results.append("\" type=\"hidden\" value=\"");
			results.append(formatInput);
			results.append("\">");
		}
		if(name!=null && replaceOnBlank!=null){
			results.append("<input name=\"");
			if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
				results.append("$replaceOnBlank_"+prefixName);
			else results.append("$replaceOnBlank_"+name);
			results.append("\" type=\"hidden\" value=\"");
			results.append(replaceOnBlank);
			results.append("\">");
		}
		if(name!=null && replaceOnErrorFormat!=null){
			results.append("<input name=\"");
			if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
				results.append("$replaceOnErrorFormat_"+prefixName);
			else results.append("$replaceOnErrorFormat_"+name);
			results.append("\" type=\"hidden\" value=\"");
			results.append(replaceOnErrorFormat);
			results.append("\">");
		}


		value=null;
		prefixName=null;
		return results.toString();
	}
	public String getAccept() {
		return accept;
	}
	public String getDir() {
		return dir;
	}
	public String getLang() {
		return lang;
	}
	public String getName() {
		return name;
	}
	public String getOnclick() {
		return onclick;
	}
	public String getOndblclick() {
		return ondblclick;
	}
	public String getOnhelp() {
		return onhelp;
	}
	public String getOnkeydown() {
		return onkeydown;
	}
	public String getOnkeypress() {
		return onkeypress;
	}
	public String getOnkeyup() {
		return onkeyup;
	}
	public String getOnmousedown() {
		return onmousedown;
	}
	public String getOnmousemove() {
		return onmousemove;
	}
	public String getOnmouseout() {
		return onmouseout;
	}
	public String getOnmouseover() {
		return onmouseover;
	}
	public String getOnmouseup() {
		return onmouseup;
	}
	public String getStyle() {
		return style;
	}
	public String getStyleClass() {
		return styleClass;
	}
	public String getTitle() {
		return title;
	}
	public void setAccept(String string) {
		accept = string;
	}
	public void setDir(String string) {
		dir = string;
	}
	public void setLang(String string) {
		lang = string;
	}
	public void setName(String string) {
		name = string;
	}
	public void setOnclick(String string) {
		onclick = string;
	}
	public void setOndblclick(String string) {
		ondblclick = string;
	}
	public void setOnhelp(String string) {
		onhelp = string;
	}
	public void setOnkeydown(String string) {
		onkeydown = string;
	}
	public void setOnkeypress(String string) {
		onkeypress = string;
	}
	public void setOnkeyup(String string) {
		onkeyup = string;
	}
	public void setOnmousedown(String string) {
		onmousedown = string;
	}
	public void setOnmousemove(String string) {
		onmousemove = string;
	}
	public void setOnmouseout(String string) {
		onmouseout = string;
	}
	public void setOnmouseover(String string) {
		onmouseover = string;
	}
	public void setOnmouseup(String string) {
		onmouseup = string;
	}
	public void setStyle(String string) {
		style = string;
	}
	public void setStyleClass(String string) {
		styleClass = string;
	}
	public void setTitle(String string) {
		title = string;
	}
	public String getAccesskey() {
		return accesskey;
	}
	public String getAlign() {
		return align;
	}
	public String getAlt() {
		return alt;
	}
	public String getBorder() {
		return border;
	}
	public String getChecked() {
		return checked;
	}
	public String getDisabled() {
		return disabled;
	}
	public String getHeight() {
		return height;
	}
	public String getIsmap() {
		return ismap;
	}
	public String getMaxlength() {
		return maxlength;
	}
	public String getOnblur() {
		return onblur;
	}
	public String getOnchange() {
		return onchange;
	}
	public String getOnfocus() {
		return onfocus;
	}
	public String getOnselect() {
		return onselect;
	}
	public String getReadonly() {
		return readonly;
	}
	public String getSize() {
		return size;
	}
	public String getSrc() {
		return src;
	}
	public String getTabindex() {
		return tabindex;
	}
	public String getType() {
		return type;
	}
	public String getUsemap() {
		return usemap;
	}
	public String getValue() {
		return value;
	}
	public String getWidth() {
		return width;
	}
	public void setAccesskey(String string) {
		accesskey = string;
	}
	public void setAlign(String string) {
		align = string;
	}
	public void setAlt(String string) {
		alt = string;
	}
	public void setBorder(String string) {
		border = string;
	}
	public void setChecked(String string) {
		checked = string;
	}
	public void setDisabled(String string) {
		disabled = string;
	}
	public void setHeight(String string) {
		height = string;
	}
	public void setIsmap(String string) {
		ismap = string;
	}
	public void setMaxlength(String string) {
		maxlength = string;
	}
	public void setOnblur(String string) {
		onblur = string;
	}
	public void setOnchange(String string) {
		onchange = string;
	}
	public void setOnfocus(String string) {
		onfocus = string;
	}
	public void setOnselect(String string) {
		onselect = string;
	}
	public void setReadonly(String string) {
		readonly = string;
	}
	public void setSize(String string) {
		size = string;
	}
	public void setSrc(String string) {
		src = string;
	}
	public void setTabindex(String string) {
		tabindex = string;
	}
	public void setType(String string) {
		type = string;
	}
	public void setUsemap(String string) {
		usemap = string;
	}
	public void setValue(String string) {
		value = string;
	}
	public void setWidth(String string) {
		width = string;
	}

	public String getBean() {
		return bean;
	}
	public void setBean(String string) {
		bean = string;
	}
	public String getObjId() {
		return objId;
	}
	public void setObjId(String string) {
		objId = string;
	}
	public String getFormatInput() {
		return formatInput;
	}
	public String getFormatOutput() {
		return formatOutput;
	}
	public void setFormatInput(String string) {
		formatInput = string;
	}
	public void setFormatOutput(String string) {
		formatOutput = string;
	}
	public String getClear()
	{
		return clear;
	}
	public void setClear(String string)
	{
		clear = string;
	}
	public String getToUpperCase() {
		return toUpperCase;
	}
	public void setToUpperCase(String string) {
		toUpperCase = string;
	}

	public String getMethod_prefix() {
		return method_prefix;
	}

	public void setMethod_prefix(String string) {
		method_prefix = string;
	}

	public String getCheckedvalue(){
		return checkedvalue;
	}

	public void setCheckedvalue(String newCheckValue) {
		checkedvalue = newCheckValue;
	}

	public String getReplaceOnBlank() {
		return replaceOnBlank;
	}

	public void setReplaceOnBlank(String string) {
		replaceOnBlank = string;
	}

	public String getReplaceOnErrorFormat() {
		return replaceOnErrorFormat;
	}

	public void setReplaceOnErrorFormat(String replaceOnErrorFormat) {
		this.replaceOnErrorFormat = replaceOnErrorFormat;
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

	public String getToTrim() {
		return toTrim;
	}

	public void setToTrim(String toTrim) {
		this.toTrim = toTrim;
	}

	public String getOncontextmenu() {
		return oncontextmenu;
	}

	public void setOncontextmenu(String oncontextmenu) {
		this.oncontextmenu = oncontextmenu;
	}

	public String getOnformchange() {
		return onformchange;
	}

	public void setOnformchange(String onformchange) {
		this.onformchange = onformchange;
	}

	public String getOnforminput() {
		return onforminput;
	}

	public void setOnforminput(String onforminput) {
		this.onforminput = onforminput;
	}

	public String getOninput() {
		return oninput;
	}

	public void setOninput(String oninput) {
		this.oninput = oninput;
	}

	public String getOninvalid() {
		return oninvalid;
	}

	public void setOninvalid(String oninvalid) {
		this.oninvalid = oninvalid;
	}

	public String getOndrag() {
		return ondrag;
	}

	public void setOndrag(String ondrag) {
		this.ondrag = ondrag;
	}

	public String getOndragend() {
		return ondragend;
	}

	public void setOndragend(String ondragend) {
		this.ondragend = ondragend;
	}

	public String getOndragenter() {
		return ondragenter;
	}

	public void setOndragenter(String ondragenter) {
		this.ondragenter = ondragenter;
	}

	public String getOndragleave() {
		return ondragleave;
	}

	public void setOndragleave(String ondragleave) {
		this.ondragleave = ondragleave;
	}

	public String getOndragover() {
		return ondragover;
	}

	public void setOndragover(String ondragover) {
		this.ondragover = ondragover;
	}

	public String getOndragstart() {
		return ondragstart;
	}

	public void setOndragstart(String ondragstart) {
		this.ondragstart = ondragstart;
	}

	public String getOndrop() {
		return ondrop;
	}

	public void setOndrop(String ondrop) {
		this.ondrop = ondrop;
	}

	public String getOnmousewheel() {
		return onmousewheel;
	}

	public void setOnmousewheel(String onmousewheel) {
		this.onmousewheel = onmousewheel;
	}

	public String getAutocomplete() {
		return autocomplete;
	}

	public void setAutocomplete(String autocomplete) {
		this.autocomplete = autocomplete;
	}

	public String getAutofocus() {
		return autofocus;
	}

	public void setAutofocus(String autofocus) {
		this.autofocus = autofocus;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getFormaction() {
		return formaction;
	}

	public void setFormaction(String formaction) {
		this.formaction = formaction;
	}

	public String getFormenctype() {
		return formenctype;
	}

	public void setFormenctype(String formenctype) {
		this.formenctype = formenctype;
	}

	public String getFormmethod() {
		return formmethod;
	}

	public void setFormmethod(String formmethod) {
		this.formmethod = formmethod;
	}

	public String getFormnovalidate() {
		return formnovalidate;
	}

	public void setFormnovalidate(String formnovalidate) {
		this.formnovalidate = formnovalidate;
	}

	public String getFormtarget() {
		return formtarget;
	}

	public void setFormtarget(String formtarget) {
		this.formtarget = formtarget;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getSolveBeanName() {
		return solveBeanName;
	}

	public void setSolveBeanName(String solveBeanName) {
		this.solveBeanName = solveBeanName;
	}

	public String getAsyncUpdate() {
		return asyncUpdate;
	}

	public void setAsyncUpdate(String asyncUpdate) {
		this.asyncUpdate = asyncUpdate;
	}

	public String getAsyncUpdateJsFunction() {
		return asyncUpdateJsFunction;
	}

	public void setAsyncUpdateJsFunction(String asyncUpdateJsScript) {
		this.asyncUpdateJsFunction = asyncUpdateJsScript;
	}


}

