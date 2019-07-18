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


public class tagEqual extends  ClTagSupport implements IExpressionArgument{
	private static final long serialVersionUID = 1L;
	protected String bean=null;
	protected String name=null;
	protected String value=null;
	protected String valueFromBean=null;
	protected String method_prefix=null;
	protected String field=null;
	protected String formatOutput=null;
	protected String formatLanguage=null;
	protected String formatCountry=null;	
	protected String formatLocationFromUserAuth=null;
	protected String ignoreCase =null;
	
	protected Boolean argumentValue;
	
	public int doStartTag() throws JspException {
		argumentValue=null;
		if (condition())
			return (EVAL_BODY_INCLUDE);
		else
			return (SKIP_BODY);
	}

	public int doEndTag() throws JspException {
		if(argumentValue!=null && getParent()!=null && getParent() instanceof tagExpression){
			tagOperand operand = new tagOperand();
			operand.setValue(argumentValue.toString());
			((tagExpression)getParent()).getElements().add(operand);
		}
		this.release();
		return (EVAL_PAGE);
	}
	public void release() {
		super.release();
		bean = null;
		name = null;
		value = null;
		method_prefix=null;
		valueFromBean=null;
		field=null;
		formatOutput=null;
		formatLanguage=null;
		formatCountry=null;	
		formatLocationFromUserAuth=null;
		ignoreCase=null;
		argumentValue=null;
	}
	
	private boolean condition() throws JspException{
		HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
		i_action formAction=null;
		i_bean formBean=null;
		
		if(bean!=null)
			bean=checkParametersIfDynamic(bean, null);
		
		if(valueFromBean!=null)
			valueFromBean=checkParametersIfDynamic(valueFromBean, null);
		
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
		if(name!=null)
			name=checkParametersIfDynamic(name, null);
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
			if(anotherBean==null) anotherBean = bsController.getFromOnlySession(bean, request);
			if(anotherBean==null) anotherBean = bsController.getFromOnlyServletContext(bean, request);
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
		
		if(field!=null ){
			try{
				value = util_reflect.prepareWriteValueForTag(anotherBean,method_prefix,field,null).toString();
			}catch(Exception e){
			}
		}

		if(valueFromBean!=null ){
			try{
				
				value = util_reflect.prepareWriteValueFromBean(valueFromBean, request, (formAction==null)?formBean:formAction.get_bean().asBean()).toString();
/*				
				Object rightBean = null;
				String nameRightBean = "";
				String methodRightBean = "";
				StringTokenizer st = new StringTokenizer(valueFromBean,".");
				
				while(st.hasMoreTokens()){
					String current = st.nextToken();
					if(nameRightBean.equals("")) nameRightBean=current;
					else{
						if(methodRightBean.equals("")) methodRightBean=current;
						else methodRightBean+="."+current;
					}
				}
				
				if(rightBean==null) rightBean = request.getAttribute(nameRightBean);
				if(rightBean==null) rightBean = request.getSession().getAttribute(nameRightBean);
				if(rightBean!=null){
					if(methodRightBean==null || methodRightBean.equals("")) value = rightBean.toString();
					else{
						try{
							value = util_reflect.prepareWriteValueForTag(rightBean,"get",methodRightBean,null).toString();					 
						}catch(Exception e){
						}
					}	
				}
*/						 		
			}catch(Exception e){
			}
		}
		if(formatLocationFromUserAuth==null && bsController.getAppInit().get_tag_format_user_auth()!=null && !bsController.getAppInit().get_tag_format_user_auth().equals(""))
			formatLocationFromUserAuth=bsController.getAppInit().get_tag_format_user_auth();
		try{
			if(formatLocationFromUserAuth!=null && formatLocationFromUserAuth.equalsIgnoreCase("true")) {
				auth=bsController.checkAuth_init(request);
				writeValue=util_format.makeFormatedString(formatOutput, auth.get_language(), auth.get_country(), writeValue);
			}
			else
				writeValue=util_format.makeFormatedString(formatOutput, formatLanguage,formatCountry, writeValue);
		}catch (Exception e) {
		}
		
		if(value==null && writeValue!=null){
			if(getParent()!=null && getParent() instanceof tagSwitch)
				((tagSwitch)getParent()).setConditionBreak(true);
			argumentValue=true;
			return true;
		}
		if(value==null || writeValue==null) {
			argumentValue=false;
			return false;
		}
		if(writeValue.toString().equals(value)){
			if(getParent()!=null && getParent() instanceof tagSwitch)
				((tagSwitch)getParent()).setConditionBreak(true);
			argumentValue=true;
			return true;
		}
		if(ignoreCase!=null && ignoreCase.equalsIgnoreCase("true") && writeValue.toString().equalsIgnoreCase(value)){
			if(getParent()!=null && getParent() instanceof tagSwitch)
				((tagSwitch)getParent()).setConditionBreak(true);
			argumentValue=true;
			return true;
		}
		argumentValue=false;
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
	public String getValue() {
		return value;
	}
	public void setBean(String string) {
		bean = string;
	}
	public void setValue(String string) {
		value = string;
	}

	public String getMethod_prefix() {
		return method_prefix;
	}

	public void setMethod_prefix(String string) {
		method_prefix = string;
	}

	public String getField() {
		return field;
	}

	public void setField(String string) {
		field = string;
	}

	public String getValueFromBean() {
		return valueFromBean;
	}

	public void setValueFromBean(String valueFromBean) {
		this.valueFromBean = valueFromBean;
	}

	public String getFormatOutput() {
		return formatOutput;
	}

	public void setFormatOutput(String formatOutput) {
		this.formatOutput = formatOutput;
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

	public String getIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(String ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public String getArgumentValue() {
		if(argumentValue==null)
			return null;
		else
			return argumentValue.toString();
	}

	public String getFormatLocationFromUserAuth() {
		return formatLocationFromUserAuth;
	}

	public void setFormatLocationFromUserAuth(String formatLocationFromUserAuth) {
		this.formatLocationFromUserAuth = formatLocationFromUserAuth;
	}

}
