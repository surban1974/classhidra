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
import java.util.StringTokenizer;

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
import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.elements.i_elementDBBase;
import it.classhidra.core.tool.exception.bsTagEndRendering;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_xml;


public class tagTranscode extends ClTagSupport implements DynamicAttributes {
	private static final long serialVersionUID = -1L;
	protected String objId = null;// id
	protected String source = null;
	protected String inputField = null;
	protected String outputField = null;
	protected String key = null;
	protected String keyValue = null;
	protected String showKeyAsDefaultValue = null;
	protected String styleClass=null;
	protected String formatOutput=null;
	protected String formatCurrency=null;
	protected String formatTimeZoneShift=null;
	protected String formatLanguage=null;
	protected String formatCountry=null;
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
	protected String additionalAttr=null;
	protected String component=null;
	
	protected Map<String,Object> tagAttributes = new HashMap<String, Object>();
	protected List<?> arguments=null;
	

	public int doStartTag() throws JspException {
		arguments = new ArrayList<Object>();
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		final StringBuffer results = new StringBuffer();
		results.append(this.createTagBody());
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		
		if(component!=null && component.equalsIgnoreCase("true") && (objId!=null )) {
			final HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
			String componentId = (String)request.getAttribute(i_tag_helper.CONST_TAG_COMPONENT_ID);
			if(componentId!=null && (componentId.equals(objId) )) {
				PageContext pageContext = (PageContext)request.getAttribute(i_tag_helper.CONST_TAG_PAGE_CONTEXT);
				if(pageContext!=null) {
					try {						
						pageContext.getOut().write(this.createTagBody());
						throw new bsTagEndRendering(objId);
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

	public void release() {
		super.release();
		source=null;
		inputField=null;
		outputField=null;
		key=null;
		keyValue=null;
		showKeyAsDefaultValue=null;
		styleClass=null;
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
		additionalAttr=null;
		component=null;
		objId = null;
		
		tagAttributes = new HashMap<String, Object>();
		arguments=null;
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
		i_action 	formAction 		= (request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTION)==null)?new action():(i_action)request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTION);
		i_bean formBean=null;
		if(formAction==null) 
			formAction 	= (i_action)request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTION);		
		if(formAction==null) formAction = new action(); 
		if(formAction!=null){
			formBean = formAction.get_bean();
			if(formBean!=null)
				formBean=formBean.asBean();
		}

		
		if(source!=null)
			source=checkParametersIfDynamic(source, null);
		
		if(method_prefix==null) method_prefix="get";
		
		final StringBuffer results = new StringBuffer("");
		Object writeValue=null;

		
		Object valueKey=null;
		Object valueSource=null;
		
		
		if(source!=null ){
			try{
				Object rightBean = null;
				String nameRightBean = "";
				String methodRightBean = "";
				StringTokenizer st = new StringTokenizer(source,".");
				
				while(st.hasMoreTokens()){
					String current = st.nextToken();
					if(nameRightBean.equals("")) nameRightBean=current;
					else{
						if(methodRightBean.equals("")) methodRightBean=current;
						else methodRightBean+="."+current;
					}
				}
				if(formAction!=null && formAction.get_infoaction()!=null &&  formAction.get_infoaction().getName().equals(nameRightBean)){
					if(formAction.get_bean()!=null)
						rightBean = formAction.get_bean().asBean();
				}
				if(rightBean==null) rightBean = request.getAttribute(nameRightBean);
				if(rightBean==null) rightBean = request.getSession().getAttribute(nameRightBean);
				try{
					if(rightBean==null) rightBean = (bsController.getFromInfoNavigation(null, request)).find(nameRightBean).get_content();
				}catch(Exception e){
				}
				if(rightBean==null) rightBean = bsController.getFromOnlySession(nameRightBean, request);
				if(rightBean==null) rightBean = bsController.getFromOnlyServletContext(nameRightBean, request);
				if(rightBean==null) rightBean = bsController.getProperty(nameRightBean,request);
				if(rightBean==null && formBean!=null) {
					rightBean = util_reflect.prepareWriteValueForTag(formBean,method_prefix,nameRightBean,arg);
					if(rightBean==null && arg!=null && arg.length>0)
						rightBean = util_reflect.prepareWriteValueForTag(formBean,method_prefix,nameRightBean,null);
				}
				
				if(rightBean!=null){
					if(methodRightBean==null || methodRightBean.equals("")) valueSource = rightBean;
					else{
						try{
							valueSource = util_reflect.prepareWriteValueForTag(rightBean,"get",methodRightBean,arg);					 
						}catch(Exception e){
						}
						if(valueSource==null && arg!=null){
							try{
								valueSource = util_reflect.prepareWriteValueForTag(rightBean,"get",methodRightBean,null);					 
							}catch(Exception e){
							}						
						}
					}	
				}		 		
			}catch(Exception e){
			}
		}		

		if(key!=null ){
			try{
				Object rightBean = null;
				String nameRightBean = "";
				String methodRightBean = "";
				StringTokenizer st = new StringTokenizer(key,".");
				
				while(st.hasMoreTokens()){
					String current = st.nextToken();
					if(nameRightBean.equals("")) nameRightBean=current;
					else{
						if(methodRightBean.equals("")) methodRightBean=current;
						else methodRightBean+="."+current;
					}
				}
				if(formAction!=null && formAction.get_infoaction()!=null &&  formAction.get_infoaction().getName().equals(nameRightBean)){
					if(formAction.get_bean()!=null)
						rightBean = formAction.get_bean().asBean();
				}
				if(rightBean==null) rightBean = request.getAttribute(nameRightBean);
				if(rightBean==null) rightBean = request.getSession().getAttribute(nameRightBean);
				try{
					if(rightBean==null) rightBean = (bsController.getFromInfoNavigation(null, request)).find(nameRightBean).get_content();
				}catch(Exception e){
				}
				if(rightBean==null) rightBean = bsController.getFromOnlySession(nameRightBean, request);
				if(rightBean==null) rightBean = bsController.getFromOnlyServletContext(nameRightBean, request);
				if(rightBean==null) rightBean = bsController.getProperty(nameRightBean,request);
				if(rightBean==null && formBean!=null) {
					rightBean = util_reflect.prepareWriteValueForTag(formBean,method_prefix,nameRightBean,arg);
					if(rightBean==null && arg!=null && arg.length>0)
						rightBean = util_reflect.prepareWriteValueForTag(formBean,method_prefix,nameRightBean,null);
				}
				
				if(rightBean!=null){
					if(methodRightBean==null || methodRightBean.equals("")) valueKey = rightBean.toString();
					else{
						try{
							valueKey = util_reflect.prepareWriteValueForTag(rightBean,"get",methodRightBean,arg).toString();					 
						}catch(Exception e){
						}
						if(valueKey==null && arg!=null){
							try{
								valueKey = util_reflect.prepareWriteValueForTag(rightBean,"get",methodRightBean,null).toString();					 
							}catch(Exception e){
							}
						}
					}	
				}		 		
			}catch(Exception e){
			}
		}
		
		if(key==null && keyValue!=null ){
			if(showKeyAsDefaultValue==null || !showKeyAsDefaultValue.equalsIgnoreCase("false"))
				valueKey = keyValue;					 
		}		

		if(valueSource!=null){
			if(valueSource instanceof Map){
				if(valueKey!=null) writeValue = ((Map<?,?>)valueSource).get(valueKey);
				else if(key!=null) writeValue = ((Map<?,?>)valueSource).get(key);
				if(outputField!=null){
					try{
						writeValue = util_reflect.prepareWriteValueForTag(writeValue,"get",outputField,null);					 
					}catch(Exception e){
					}
				}
			}
			if(valueSource instanceof List){
				writeValue = findElementFromList((List<?>)valueSource, valueKey, inputField);
				if(writeValue==null) writeValue = findElementFromListAsString((List<?>)valueSource, valueKey, inputField);
				if(outputField!=null){
					try{
						writeValue = util_reflect.prepareWriteValueForTag(writeValue,"get",outputField,null);					 
					}catch(Exception e){
					}
				}
			}
		
		}

		if(writeValue==null && valueKey!=null) writeValue=valueKey;
		
		if(writeValue!=null){
			if(objId!=null || styleClass!=null || additionalAttr!=null || tagAttributes.size()>0){
				results.append(" <span ");
				if(objId!=null){
					results.append(" id=\"");
					results.append(objId);
					results.append('"');
				}				
				if(styleClass!=null){
					results.append(" class=\"");
					results.append(styleClass);
					results.append('"');
				}
				if(additionalAttr!=null){
					results.append(" ");
					results.append(additionalAttr);
					results.append(" ");
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
			
			
			try{
				
				if(formatLocationFromUserAuth!=null && formatLocationFromUserAuth.equalsIgnoreCase("true") && auth==null)
					auth=bsController.checkAuth_init((HttpServletRequest) this.pageContext.getRequest());
				
				if(formatLocationFromUserAuth!=null && formatLocationFromUserAuth.equalsIgnoreCase("true") && auth!=null)
					writeValue=util_format.makeFormatedString(formatOutput, auth.get_language_profile(), auth.get_country(), formatTimeZoneShift, formatCurrency, writeValue);
				else
					writeValue=util_format.makeFormatedString(formatOutput, formatLanguage, formatCountry, formatTimeZoneShift, formatCurrency, writeValue);
				if(replaceOnBlank != null && writeValue!=null && replaceOnBlank.equals(writeValue.toString())) 
					writeValue=util_format.replace(writeValue.toString(),replaceOnBlank,"");
			}catch(Exception e){}	
				
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
			
			if(styleClass!=null || additionalAttr!=null)
				results.append(" </span>");
		}

		return results.toString();
	}		
	


	public String getStyleClass() {
		return styleClass;
	}
	public void setStyleClass(String string) {
		styleClass = string;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}



	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	private static Object findElementFromList(List<?> elements, Object valueKey, String field){
		if(valueKey==null || field==null) return null;
		for(int i=0;i<elements.size();i++){
			if(field==null){
				if(elements.get(i)!=null && valueKey.equals(elements.get(i))) return elements.get(i);
			}else{
				if(elements.get(i) instanceof i_bean){
					i_bean el = (i_bean)elements.get(i);
					if(el!=null && valueKey.equals(el.get(field))) return el;
				}
				if(elements.get(i) instanceof i_elementDBBase){
					i_elementDBBase el = (i_elementDBBase)elements.get(i);
					if(el!=null && valueKey.equals(el.getCampoValue(field))) return el;
				}
				if(elements.get(i) instanceof i_elementBase){
					i_elementBase el = (i_elementBase)elements.get(i);
					if(el!=null && valueKey.toString().equals(el.getCampoValue(field))) return el;
				}
			}
		}
		return null;
	}
	
	private static Object findElementFromListAsString(List<?> elements, Object valueKey, String field){
		if(valueKey==null || field==null) return null;
		for(int i=0;i<elements.size();i++){
			if(field==null){
				if(elements.get(i)!=null && valueKey.toString().equals(elements.get(i).toString())) return elements.get(i);
			}else{
				if(elements.get(i) instanceof i_bean){
					i_bean el = (i_bean)elements.get(i);
					if(el!=null && el.get(field)!=null && valueKey.toString().equals(el.get(field).toString())) return el;
				}
				if(elements.get(i) instanceof i_elementDBBase){
					i_elementDBBase el = (i_elementDBBase)elements.get(i);
					if(el!=null && el.getCampoValue(field)!=null && valueKey.toString().equals(el.getCampoValue(field).toString())) return el;
				}
				if(elements.get(i) instanceof i_elementBase){
					i_elementBase el = (i_elementBase)elements.get(i);
					if(el!=null && el.getCampoValue(field)!=null && valueKey.toString().equals(el.getCampoValue(field).toString())) return el;
				}
			}
		}
		return null;
	}

	public String getInputField() {
		return inputField;
	}

	public void setInputField(String inputField) {
		this.inputField = inputField;
	}

	public String getOutputField() {
		return outputField;
	}

	public void setOutputField(String outputField) {
		this.outputField = outputField;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getNormalASCII() {
		return normalASCII;
	}

	public void setNormalASCII(String normalASCII) {
		this.normalASCII = normalASCII;
	}

	public String getShowKeyAsDefaultValue() {
		return showKeyAsDefaultValue;
	}

	public void setShowKeyAsDefaultValue(String showKeyAsDefaultValue) {
		this.showKeyAsDefaultValue = showKeyAsDefaultValue;
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

	public List<?> getArguments() {
		return arguments;
	}

	public void setArguments(List<?> arguments) {
		this.arguments = arguments;
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

	public String getComponent() {
		return component;
	}

	public void setComponent(String componentId) {
		this.component = componentId;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getNormalXMLCDATA() {
		return normalXMLCDATA;
	}

	public void setNormalXMLCDATA(String normalXMLCDATA) {
		this.normalXMLCDATA = normalXMLCDATA;
	}

	public String getFormatLocationFromUserAuth() {
		return formatLocationFromUserAuth;
	}

	public void setFormatLocationFromUserAuth(String formatLocationFromUserAuth) {
		this.formatLocationFromUserAuth = formatLocationFromUserAuth;
	}

	public String getFormatCurrency() {
		return formatCurrency;
	}

	public void setFormatCurrency(String formatCurrency) {
		this.formatCurrency = formatCurrency;
	}

	public String getFormatTimeZoneShift() {
		return formatTimeZoneShift;
	}

	public void setFormatTimeZoneShift(String formatTimeZoneShift) {
		this.formatTimeZoneShift = formatTimeZoneShift;
	}	
}

