package it.classhidra.core.controller.tags;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;


import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_tag;

public abstract class ClTagSupport extends BodyTagSupport implements DynamicAttributes {
	private static final long serialVersionUID = 1L;


	protected String checkParametersIfDynamic(String input, i_bean formBean){
		if(input==null || input.equals("") || input.indexOf('{')<0)
			return input;

		
		Pattern pattern = Pattern.compile("\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(input);
		Map tokens = new HashMap();
		while(matcher.find()){
		   String word = matcher.group(1);
		   tokens.put(word, word);
		}		

		if(tokens.size()==0)
			return input;
		
		if(formBean==null){
			HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
			i_action formAction=(i_action)request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTION);		
			if(formAction==null) formAction = new action(); 
			formBean = formAction.get_bean();
			if(formBean!=null)
				formBean=formBean.asBean();
		}
		
		Iterator it = tokens.keySet().iterator();
		while(it.hasNext()){
			String key = (String)it.next();
			input=util_format.replace(input, "{"+key+"}", checkParameterIfDynamic(key,formBean));
		}
		
		
		
		return input;
	}
	
	protected String checkParameterIfDynamic(String parameter, i_bean formBean){
		if(parameter==null || parameter.equals(""))
			return parameter;		
		String result = parameter;
		
		Object writeValue=null;
		Object anotherBean=null;
		if(formBean!=null)
			writeValue = util_reflect.prepareWriteValueForTag(formBean,"get",parameter,null);
		if(writeValue==null){
			String bean = null;
			String name = null;
			if(parameter.indexOf('.')==-1){
				bean = parameter;
			}else{
				String[] splitted = parameter.split("\\.");
				if(splitted.length>0){
					bean = splitted[0];
					name="";
					for(int i=1;i<splitted.length;i++)
						name+=((name.equals(""))?"":".")+splitted[i];
				}
			}
			if(name==null && bean==null)
				return result;
			HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
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
			if(anotherBean==null) anotherBean = bsController.getProperty(bean,request);
			
			if(anotherBean==null){
				try{
					Object obj = getParent();
					String bean_name = (String)util_reflect.getValue(anotherBean,"get"+"bean",null);
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
						writeValue = util_reflect.prepareWriteValueForTag(anotherBean,"get",name,null);					 
					}catch(Exception e){
					}
				}	
			}
		}
		if(writeValue!=null)
			result = writeValue.toString();		
		return result;
	}

	protected void renderComponent(i_bean formBean, i_action formAction, String callerClassName, String cmpId) {
		try {	
			if(this.pageContext.getPage()!=null) {
				final String executorinfo = util_tag.getTagExecutor(callerClassName);
				if(executorinfo!=null) {
					final String[] arrayOfString = executorinfo.split(":");
					util_tag.addTagExecutorObject(arrayOfString[0], this.pageContext.getPage());
					formBean.getComponents().put(
						formAction.get_infoaction().getPath()+":"+formAction.getCurrent_redirect().get_inforedirect().getPath()+":"+cmpId
						,
						executorinfo);	
				}
			}
		}catch(Exception e) {				
		}
	}

	public void setDynamicAttribute(String arg0, String arg1, Object arg2) throws JspException {
	}

}
