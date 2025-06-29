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
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.DynamicAttributes;

import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.i_tag_helper;
import it.classhidra.core.tool.exception.bsTagEndRendering;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_tag;
import it.classhidra.core.tool.util.util_xml;


public class tagSelect extends tagInput implements DynamicAttributes {
	private static final long serialVersionUID = -1L;
	private String multiple=null;
	
	public int doStartTag() throws JspException {
		final StringBuffer results = new StringBuffer();
		results.append(this.createTagBody());
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		
		if(component!=null && component.equalsIgnoreCase("true") && (objId!=null || name!=null)) {
			final HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
			String componentId = (String)request.getAttribute(i_tag_helper.CONST_TAG_COMPONENT_ID);
			if(componentId!=null && (componentId.equals(objId) || componentId.equals(name))) {		
				return EVAL_BODY_BUFFERED;
			}
		}
		
		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() throws JspException {

		final StringBuffer results = new StringBuffer();
		results.append(createEndTagBody());
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		value=null;
		
		if(component!=null && component.equalsIgnoreCase("true") && (objId!=null || name!=null)) {
			final HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
			String componentId = (String)request.getAttribute(i_tag_helper.CONST_TAG_COMPONENT_ID);
			if(componentId!=null && (componentId.equals(objId) || componentId.equals(name))) {
				PageContext pageContext = (PageContext)request.getAttribute(i_tag_helper.CONST_TAG_PAGE_CONTEXT);
				if(pageContext!=null) {
					try {						
						pageContext.getOut().write(this.createTagBody());
						if(this.getBodyContent()!=null)
							pageContext.getOut().write(this.getBodyContent().getString());
						pageContext.getOut().write(this.createEndTagBody());

						if(objId!=null)
							throw new bsTagEndRendering(objId);
						else 
							throw new bsTagEndRendering(name);
					}catch(Exception e) {
						if(e instanceof bsTagEndRendering)
							throw (bsTagEndRendering)e;
					}

				}
				request.removeAttribute(i_tag_helper.CONST_TAG_COMPONENT_ID);
			}
			
		}
		
		this.release();
		return EVAL_BODY_INCLUDE;

	}
	
	protected String createEndTagBody() {
		
		final StringBuffer results = new StringBuffer("</select>");
		
		if(formatCurrency!=null || (formatTimeZoneShift!=null && formatTimeZone!=null)) {
			results.append("</section>");
		}			
		
		String prefixName=null;
		HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
		if(bean!=null && request.getAttribute(tagBean.CONST_HEAP_BEANS)!=null) {
			@SuppressWarnings("unchecked")
			final HashMap<String, String> hashMap = (HashMap<String,String>)request.getAttribute(tagBean.CONST_HEAP_BEANS);
			if(hashMap!=null && hashMap.get(bean)!=null)
					prefixName = hashMap.get(bean);
		}

		if(prefixName==null)
			prefixName=name;
		else prefixName+="."+name;
		
		if(name!=null && formatInput!=null){
			results.append("<input name=\"");
			if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
				results.append("$format_"+prefixName);
			else results.append("$format_"+name);
			results.append("\" type=\"hidden\" value=\"");
			results.append(formatInput);
			results.append("\">");
		}
		if(name!=null && formatOutput!=null){
			results.append("<input name=\"");
			if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
				results.append("$formatOutput_"+prefixName);
			else results.append("$formatOutput_"+name);
			results.append("\" type=\"hidden\" value=\"");
			results.append(formatOutput);
			results.append("\">");
		}
		if(name!=null && formatLanguage!=null){
			results.append("<input name=\"");
			if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
				results.append("$formatLanguage_"+prefixName);
			else results.append("$formatLanguage_"+name);
			results.append("\" type=\"hidden\" value=\"");
			results.append(formatLanguage);
			results.append("\">");
		}
		if(name!=null && formatCountry!=null){
			results.append("<input name=\"");
			if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
				results.append("$formatCountry_"+prefixName);
			else results.append("$formatCountry_"+name);
			results.append("\" type=\"hidden\" value=\"");
			results.append(formatCountry);
			results.append("\">");
		}
		if(name!=null && formatTimeZoneShift!=null){
			results.append("<input name=\"");
			if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
				results.append("$formatTimeZoneShift_"+prefixName);
			else results.append("$formatTimeZoneShift_"+name);
			results.append("\" type=\"hidden\" value=\"");
			results.append(formatTimeZoneShift);
			results.append("\">");
		}
		if(name!=null && formatLocationFromUserAuth!=null){
			results.append("<input name=\"");
			if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
				results.append("$formatLocationFromUserAuth_"+prefixName);
			else results.append("$formatLocationFromUserAuth_"+name);
			results.append("\" type=\"hidden\" value=\"");
			results.append(formatLocationFromUserAuth);
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
		
		
		return results.toString();

	}
	
	protected String createTagBody() {
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
		if(component!=null && component.equalsIgnoreCase("true") && formBean!=null && (objId!=null || name!=null)) {
			renderComponent(formBean, formAction, this.getClass().getName(), ((objId!=null)?objId:((name!=null)?name:"")),
					(rendering!=null && rendering.equalsIgnoreCase(i_tag_helper.CONST_TAG_RENDERING_FULL))?true:false);
		}
		if(name!=null)
			name=checkParametersIfDynamic(name, null);
		if(method_prefix==null) method_prefix="get";

		Object writeValue=null;

		String prefixName=null;
		
		if(bean!=null && request.getAttribute(tagBean.CONST_HEAP_BEANS)!=null) {
			@SuppressWarnings("unchecked")
			final HashMap<String, String> hashMap = (HashMap<String,String>)request.getAttribute(tagBean.CONST_HEAP_BEANS);
			if(hashMap!=null && hashMap.get(bean)!=null)
					prefixName = hashMap.get(bean);
		}

		if(prefixName==null)
			prefixName=name;
		else prefixName+="."+name;
		
		String asyncUpdateUrl=null;

		if(formatLocationFromUserAuth==null && bsController.getAppInit().get_tag_format_user_auth()!=null && !bsController.getAppInit().get_tag_format_user_auth().equals(""))
			formatLocationFromUserAuth=bsController.getAppInit().get_tag_format_user_auth();			
		if(formatLocationFromUserAuth==null && bsController.getFromLocalContainer(bsConstants.CONST_TAG_ALL_FORMATLOCATIONFROMUSERAUTH)!=null)
			formatLocationFromUserAuth=bsController.getFromLocalContainer(bsConstants.CONST_TAG_ALL_FORMATLOCATIONFROMUSERAUTH).toString();

		if(formatLanguage==null && bsController.getAppInit().get_tag_format_language()!=null && !bsController.getAppInit().get_tag_format_language().equals(""))
			formatLanguage=bsController.getAppInit().get_tag_format_language();			
		if(formatLanguage==null && bsController.getFromLocalContainer(bsConstants.CONST_TAG_ALL_FORMATLANGUAGE)!=null)
			formatLanguage=bsController.getFromLocalContainer(bsConstants.CONST_TAG_ALL_FORMATLANGUAGE).toString();

		if(formatCountry==null && bsController.getAppInit().get_tag_format_country()!=null && !bsController.getAppInit().get_tag_format_country().equals(""))
			formatCountry=bsController.getAppInit().get_tag_format_country();			
		if(formatCountry==null && bsController.getFromLocalContainer(bsConstants.CONST_TAG_ALL_FORMATCOUNTRY)!=null)
			formatCountry=bsController.getFromLocalContainer(bsConstants.CONST_TAG_ALL_FORMATCOUNTRY).toString();
		if(formatCurrency==null && bsController.getAppInit().get_tag_format_currency()!=null && !bsController.getAppInit().get_tag_format_currency().equals(""))
			formatCurrency=bsController.getAppInit().get_tag_format_currency();			
		if(formatCurrency==null && bsController.getFromLocalContainer(bsConstants.CONST_TAG_ALL_FORMATCURRENCY)!=null)
			formatCurrency=bsController.getFromLocalContainer(bsConstants.CONST_TAG_ALL_FORMATCURRENCY).toString();
		if(formatTimeZoneShift==null && bsController.getAppInit().get_tag_format_timezone_shift()!=null && !bsController.getAppInit().get_tag_format_timezone_shift().equals(""))
			formatTimeZoneShift=bsController.getAppInit().get_tag_format_currency();			
		if(formatTimeZoneShift==null && bsController.getFromLocalContainer(bsConstants.CONST_TAG_ALL_FORMATTIMEZONESHIFT)!=null)
			formatTimeZoneShift=bsController.getFromLocalContainer(bsConstants.CONST_TAG_ALL_FORMATTIMEZONESHIFT).toString();
		
		if(formatLocationFromUserAuth!=null)
			formatLocationFromUserAuth=checkParametersIfDynamic(formatLocationFromUserAuth, null);
		if(formatLanguage!=null)
			formatLanguage=checkParametersIfDynamic(formatLanguage, null);
		if(formatCountry!=null)
			formatCountry=checkParametersIfDynamic(formatCountry, null);
		if(formatCurrency!=null)
			formatCurrency=checkParametersIfDynamic(formatCurrency, null);
		if(formatOutput!=null)
			formatOutput=checkParametersIfDynamic(formatOutput, null);
		if(formatInput!=null)
			formatInput=checkParametersIfDynamic(formatInput, null);
		if(formatTimeZoneShift!=null)
			formatTimeZoneShift=checkParametersIfDynamic(formatTimeZoneShift, null);
		if(replaceOnBlank!=null)
			replaceOnBlank=checkParametersIfDynamic(replaceOnBlank, null);
		if(replaceOnErrorFormat!=null)
			replaceOnErrorFormat=checkParametersIfDynamic(replaceOnErrorFormat, null);
		if(placeholder!=null)
				placeholder=checkParametersIfDynamic(placeholder, null);

		
//		if(value==null){
			if(bean==null && name!=null){
				writeValue = formBean.get(name);
				try{
					if(writeValue!=null) {
						if(formatLocationFromUserAuth!=null && formatLocationFromUserAuth.equalsIgnoreCase("true")) {
							auth=bsController.checkAuth_init(request);
							value=util_format.makeFormatedString(formatOutput, 
									(formatLanguage==null)?auth.get_language_profile():formatLanguage,
									(formatCountry==null)?auth.get_country():formatCountry,
									(formatTimeZoneShift==null)?auth.get_timezone():formatTimeZoneShift,
									null, writeValue);
							if(formatLanguage==null)
								formatLanguage = auth.get_language_profile();
							if(formatCountry==null)
								formatCountry = auth.get_country();
							if(formatTimeZoneShift==null)
								formatTimeZoneShift = auth.get_timezone();
						}else
							value = util_format.makeFormatedString(formatOutput,formatLanguage,formatCountry, formatTimeZoneShift, null, writeValue);
						
					}
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
				try{
					if(anotherBean==null) anotherBean = (bsController.getFromInfoNavigation(null, request)).find(bean).get_content();
				}catch(Exception e){
				}
				if(anotherBean==null) anotherBean = bsController.getFromOnlySession(bean, request);
				if(anotherBean==null) anotherBean = bsController.getFromOnlyServletContext(bean, request);
				if(anotherBean==null) anotherBean = bsController.getProperty(bean,request);
				
				if(anotherBean!=null){
					if(name==null){
						writeValue = anotherBean;
						name=bean;
						try{
							value = util_format.makeFormatedString(formatOutput,formatLanguage,formatCountry,formatCurrency, writeValue);
						}catch(Exception e){
						}
					}
					else{
						try{
							writeValue = util_reflect.prepareWriteValueForTag(anotherBean,method_prefix,name,arg);
							if(writeValue!=null) value = util_format.makeFormatedString(formatOutput,formatLanguage,formatCountry,formatCurrency, writeValue);
						}catch(Exception e){}
					}
				}
			}
//		}
			
		
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
							asyncUpdateUrl+=prefixName+"='+this.value+'&target="+util_format.normaliseURLParameter(prefixName)+"&";
						else asyncUpdateUrl+=name+"='+this.value+'&target="+util_format.normaliseURLParameter(name)+"&";
						
						if(formatInput!=null){
							if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
								asyncUpdateUrl+="$format_"+prefixName+"="+util_format.normaliseURLParameter(formatInput)+"&";
							else asyncUpdateUrl+="$format_"+name+"="+util_format.normaliseURLParameter(formatInput)+"&";
						}
						if(formatOutput!=null){
							if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
								asyncUpdateUrl+="$formatOutput_"+prefixName+"="+util_format.normaliseURLParameter(formatOutput)+"&";
							else asyncUpdateUrl+="$formatOutput_"+name+"="+util_format.normaliseURLParameter(formatOutput)+"&";
						}	
						if(formatLanguage!=null){
							if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
								asyncUpdateUrl+="$formatLanguage_"+prefixName+"="+util_format.normaliseURLParameter(formatLanguage)+"&";
							else asyncUpdateUrl+="$formatLanguage_"+name+"="+util_format.normaliseURLParameter(formatLanguage)+"&";
						}
						if(formatCountry!=null){
							if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
								asyncUpdateUrl+="$formatCountry_"+prefixName+"="+util_format.normaliseURLParameter(formatCountry)+"&";
							else asyncUpdateUrl+="$formatCountry_"+name+"="+util_format.normaliseURLParameter(formatCountry)+"&";
						}	
						if(formatTimeZoneShift!=null){
							if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
								asyncUpdateUrl+="$formatTimeZoneShift_"+prefixName+"="+util_format.normaliseURLParameter(formatTimeZoneShift)+"&";
							else asyncUpdateUrl+="$formatTimeZoneShift_"+name+"="+util_format.normaliseURLParameter(formatTimeZoneShift)+"&";
						}					
						if(replaceOnBlank!=null){
							if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
								asyncUpdateUrl+="$replaceOnBlank_"+prefixName+"="+util_format.normaliseURLParameter(replaceOnBlank)+"&";
							else asyncUpdateUrl+="$replaceOnBlank_"+name+"="+util_format.normaliseURLParameter(replaceOnBlank)+"&";
						}
						if(replaceOnErrorFormat!=null){
							if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
								asyncUpdateUrl+="$replaceOnErrorFormat_"+prefixName+"="+util_format.normaliseURLParameter(replaceOnErrorFormat)+"&";
							else asyncUpdateUrl+="$replaceOnErrorFormat_"+name+"="+util_format.normaliseURLParameter(replaceOnErrorFormat)+"&";
						}
					}
				}catch(Exception e){
				}
			}
	
			
		final StringBuffer results = new StringBuffer("");
		String inputId=null;
		if(objId!=null){
			inputId=objId;
		}else{
			if(name!=null){				
				if(solveBeanName!=null && solveBeanName.equalsIgnoreCase("true"))
					inputId=prefixName;
				else inputId=name;
			}
		}
		
		if(formatCurrency!=null || (formatTimeZoneShift!=null && formatTimeZone!=null)) {
			results.append("<section class=\"");
			if(formatCurrency!=null) {
				results.append(" selectFormatCurrencySectionStyles");
			}
			if(formatTimeZoneShift!=null && formatTimeZone!=null) {
				results.append(" selectFormatTimeZoneShiftSectionStyles");
			}
			results.append("\" >");
			if(formatCurrency!=null) {
				results.append("<label for=\""+inputId+"\" class=\"selectFormatCurrencyLabelStyles\">"+util_format.getCurrensySymbolByCode(formatCurrency)+"</label>");			
			}
			if(formatTimeZoneShift!=null && formatTimeZone!=null) {
				results.append("<label for=\""+inputId+"\" class=\"selectFormatTimeZoneShiftLabelStyles\">"+updateFormatTimezone()+"</label>");			
			}			
		}		
		
		results.append("<select ");
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
		if (multiple != null) {
			results.append(" multiple =\"");
			results.append(multiple);
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
		if (required != null) {
			results.append(" required=\"");
			results.append(required);
			results.append('"');
		}
		if(value!=null)
			if(formatOutput!=null) value=util_format.prepareContentString(formatOutput,value);
//			if(replaceOnBlank != null) value=util_format.replace(value,replaceOnBlank,"");
			if(replaceOnBlank != null && value!=null && replaceOnBlank.equals(value)) 
				value=util_format.replace(value,replaceOnBlank,"");

			try{
				results.append(" value=\"");
				results.append(util_xml.normalHTML((value==null)?"":value.toString(),null));
				results.append('"');
		}catch(Exception e){}

		if (styleClass != null) {
			results.append(" class=\"");
			if(formatCurrency!=null || (formatTimeZoneShift!=null && formatTimeZone!=null)) {
				results.append(styleClass);
				if(formatCurrency!=null)
					results.append(" inputFormatCurrencyStyles");
				if(formatTimeZoneShift!=null && formatTimeZone!=null)
					results.append(" inputFormatTimeZoneShiftStyles");
			}else
				results.append(styleClass);
			results.append('"');
		}else if(formatCurrency!=null || (formatTimeZoneShift!=null && formatTimeZone!=null)) {
			results.append(" class=\"");
			if(formatCurrency!=null)
				results.append(" inputFormatCurrencyStyles");
			if(formatTimeZoneShift!=null && formatTimeZone!=null)
				results.append(" inputFormatTimeZoneShiftStyles");	
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
			results.append(onblur);
			results.append('"');
		}
		if (onchange != null) {
			results.append(" onchange=\"");
			results.append("this.value=this[this.selectedIndex].value;");
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
		results.append("select:"+prefixName);
		results.append('"');

		results.append('>');

		
		prefixName=null;
		return results.toString();
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

