/**
* Creation date: (27/07/2012)
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

import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_tag;
import it.classhidra.core.tool.util.util_xml; 



public class tagOperand extends ClTagSupport implements IExpressionArgument{
	private static final long serialVersionUID = -1L;
	protected String bean = null;
	protected String name = null;
	protected String formatOutput=null;
	protected String formatCurrency=null;
	protected String formatLanguage=null;
	protected String formatCountry=null;
	protected String formatTimeZoneShift=null;
	protected String formatLocationFromUserAuth=null;
	protected String method_prefix=null;
	protected String replaceOnBlank=null;
	protected String normalXML=null;
	protected String normalXML10=null;
	protected String normalXML11=null;
	protected String normalXMLCDATA=null;
	protected String charset;
	protected String normalASCII=null;
	protected String normalHTML=null;	
	protected String value=null;



	public int doStartTag() throws JspException {
		
		if(value==null)
			value=this.createTagBody();
		
		try{
			if(getParent()!=null && getParent() instanceof tagExpression){
				tagOperand operand = new tagOperand();
				operand.setValue(value);
				((tagExpression)getParent()).getElements().add(operand);
			}
		}catch(Exception e){
		}

		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() throws JspException {
		this.release();
		return super.doEndTag();
	}	

	public void release() {
		super.release();
		bean=null;
		name=null;
		formatOutput=null;
		formatCurrency=null;
		formatTimeZoneShift=null;
		formatLocationFromUserAuth=null;
		method_prefix=null;
		replaceOnBlank=null;
		formatLanguage=null;
		formatCountry=null;
		normalXML=null;
		normalXML10=null;
		normalXML11=null;
		normalXMLCDATA=null;
		charset=null;
		normalASCII=null;
		normalHTML=null;
		value=null;
	}
  
	protected String createTagBody() {
		HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
		i_action formAction=null;
		i_bean formBean=null;
		
		if(bean!=null)
			bean=checkParametersIfDynamic(bean, null);
		
		if(bean!=null){
			@SuppressWarnings("unchecked")
			HashMap<String,i_action> pool = (HashMap<String,i_action>)request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTIONPOOL);
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
		
		final StringBuffer results = new StringBuffer("");
		Object writeValue=null;
		Object anotherBean=null;
		if(bean==null){
			if(formBean!=null){
				writeValue = util_reflect.prepareWriteValueForTag(formBean,method_prefix,name,null);
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
			if(anotherBean==null) anotherBean = bsController.getFromOnlyServletContext(bean, request);
			if(anotherBean==null) anotherBean = bsController.getProperty(bean,request);
			
			if(anotherBean==null){
				try{
					Object obj = getParent();
					String bean_name = (String)util_reflect.getValue(anotherBean,method_prefix+"bean",null);
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
						writeValue = util_reflect.prepareWriteValueForTag(anotherBean,method_prefix,name,null);					 
					}catch(Exception e){
					}
				}	
			}		 		
		}		
		if(writeValue!=null){
			try{
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
				if(formatTimeZoneShift!=null)
					formatTimeZoneShift=checkParametersIfDynamic(formatTimeZoneShift, null);
				if(replaceOnBlank!=null)
					replaceOnBlank=checkParametersIfDynamic(replaceOnBlank, null);
				
				if(formatLocationFromUserAuth!=null && formatLocationFromUserAuth.equalsIgnoreCase("true")) {
					auth=bsController.checkAuth_init(request);
					writeValue=util_format.makeFormatedString(formatOutput, 
							(formatLanguage==null)?auth.get_language_profile():formatLanguage,
							(formatCountry==null)?auth.get_country():formatCountry,
							(formatTimeZoneShift==null)?auth.get_timezone():formatTimeZoneShift,
							formatCurrency, writeValue);
				}else
					writeValue=util_format.makeFormatedString(formatOutput, formatLanguage,formatCountry, formatTimeZoneShift, formatCurrency, writeValue);
				
				if(replaceOnBlank != null && writeValue!=null && replaceOnBlank.equals(writeValue.toString())) 
					writeValue=util_format.replace(writeValue.toString(),replaceOnBlank,"");
				if(normalXML!=null && normalXML.toLowerCase().equals("true"))
					results.append(util_xml.normalXML((writeValue==null)?"":writeValue.toString(),charset));	
				else if(normalXML10!=null && normalXML10.toLowerCase().equals("true"))
					results.append(util_xml.escapeXML10((writeValue==null)?"":writeValue.toString(),charset));		
				else if(normalXML11!=null && normalXML11.toLowerCase().equals("true"))
					results.append(util_xml.escapeXML11((writeValue==null)?"":writeValue.toString(),charset));		
				else if(normalXMLCDATA!=null && normalXMLCDATA.toLowerCase().equals("true"))
					results.append(util_xml.normalCDATA((writeValue==null)?"":writeValue.toString(),charset));			
				else if(normalASCII!=null && normalASCII.equalsIgnoreCase("true"))	
					results.append(util_xml.normalASCII((writeValue==null)?"":writeValue.toString()));	
				else if(normalHTML!=null && normalHTML.equalsIgnoreCase("true"))
					results.append(util_xml.normalHTML((writeValue==null)?"":writeValue.toString(), null));	
				else 
					results.append(writeValue);
				
				
			}catch(Exception e){}	
			
		}
		return results.toString();
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getNormalXML10() {
		return normalXML10;
	}

	public void setNormalXML10(String normalXML10) {
		this.normalXML10 = normalXML10;
	}

	public String getNormalXML11() {
		return normalXML11;
	}

	public void setNormalXML11(String normalXML11) {
		this.normalXML11 = normalXML11;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
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

	public String getArgumentValue() {
		return getValue();
	}

	public String getNormalXMLCDATA() {
		return normalXMLCDATA;
	}

	public void setNormalXMLCDATA(String normalXMLCDATA) {
		this.normalXMLCDATA = normalXMLCDATA;
	}

	public String getFormatCurrency() {
		return formatCurrency;
	}

	public void setFormatCurrency(String formatCurrency) {
		this.formatCurrency = formatCurrency;
	}

	public String getFormatLocationFromUserAuth() {
		return formatLocationFromUserAuth;
	}

	public void setFormatLocationFromUserAuth(String formatLocationFromUserAuth) {
		this.formatLocationFromUserAuth = formatLocationFromUserAuth;
	}

	public String getFormatTimeZoneShift() {
		return formatTimeZoneShift;
	}

	public void setFormatTimeZoneShift(String formatTimeZoneShift) {
		this.formatTimeZoneShift = formatTimeZoneShift;
	}






}

