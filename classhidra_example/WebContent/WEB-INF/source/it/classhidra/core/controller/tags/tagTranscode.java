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
import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.elements.i_elementDBBase;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_xml;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;


public class tagTranscode extends TagSupport{
	private static final long serialVersionUID = -6037366698116086666L;
	protected String source = null;
	protected String inputField = null;
	protected String outputField = null;
	protected String key = null;
	protected String keyValue = null;
	protected String styleClass=null;
	protected String formatOutput=null;
	protected String formatLanguage=null;
	protected String formatCountry=null;
	protected String method_prefix=null;
	protected String replaceOnBlank=null;
	protected String normalXML=null;



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
		source=null;
		inputField=null;
		outputField=null;
		key=null;
		keyValue=null;
		styleClass=null;
		formatOutput=null;
		method_prefix=null;
		replaceOnBlank=null;
		formatLanguage=null;
		formatCountry=null;
		normalXML=null;
	}
  
	protected String createTagBody() {
		HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
		i_action 	formAction 		= (request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTION)==null)?new action():(i_action)request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTION);


		
		
		if(method_prefix==null) method_prefix="get";
		
		StringBuffer results = new StringBuffer("");
		Object writeValue=null;
		Object anotherBean=null;
		
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
				if(formAction!=null && formAction.get_infoaction()!=null &&  formAction.get_infoaction().getName().equals(nameRightBean))
					rightBean = formAction.get_bean();
				if(rightBean==null) rightBean = request.getAttribute(nameRightBean);
				if(rightBean==null) rightBean = request.getSession().getAttribute(nameRightBean);
				if(rightBean!=null){
					if(methodRightBean==null || methodRightBean.equals("")) valueKey = rightBean;
					else{
						try{
							valueSource = util_reflect.prepareWriteValueForTag(rightBean,"get",methodRightBean,null);					 
						}catch(Exception e){
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
				if(formAction!=null && formAction.get_infoaction()!=null &&  formAction.get_infoaction().getName().equals(nameRightBean))
					rightBean = formAction.get_bean();
				if(rightBean==null) rightBean = request.getAttribute(nameRightBean);
				if(rightBean==null) rightBean = request.getSession().getAttribute(nameRightBean);
				if(rightBean!=null){
					if(methodRightBean==null || methodRightBean.equals("")) valueKey = rightBean.toString();
					else{
						try{
							valueKey = util_reflect.prepareWriteValueForTag(rightBean,"get",methodRightBean,null).toString();					 
						}catch(Exception e){
						}
					}	
				}		 		
			}catch(Exception e){
			}
		}
		
		if(key==null && keyValue!=null){			
			valueKey = keyValue;					 
		}		

		if(valueSource!=null){
			if(valueSource instanceof Map){
				if(valueKey!=null) writeValue = ((Map)valueSource).get(valueKey);
				else if(key!=null) writeValue = ((Map)valueSource).get(key);
				if(outputField!=null){
					try{
						writeValue = util_reflect.prepareWriteValueForTag(writeValue,"get",outputField,null);					 
					}catch(Exception e){
					}
				}
			}
			if(valueSource instanceof List){
				writeValue = findElementFromList((List)valueSource, valueKey, inputField);
				if(outputField!=null){
					try{
						writeValue = util_reflect.prepareWriteValueForTag(writeValue,"get",outputField,null);					 
					}catch(Exception e){
					}
				}
			}
		
		}

		
		if(writeValue!=null){
			if(styleClass!=null){
				results.append(" <span class=\"");
				results.append(styleClass);
				results.append("\">");
			}
			try{
				writeValue=util_format.makeFormatedString(formatOutput, formatLanguage,formatCountry, writeValue);
				if(replaceOnBlank != null) writeValue=util_format.replace(writeValue.toString(),replaceOnBlank,"");
			}catch(Exception e){}	
			results.append(writeValue);			
			if(styleClass!=null) results.append(" </span>");
		}
		if(normalXML!=null && normalXML.toLowerCase().equals("true"))
			return util_xml.normalXML(results.toString(),null);
		else return results.toString();
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

	private static Object findElementFromList(List elements, Object valueKey, String field){
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
}

