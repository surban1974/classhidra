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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_tag;


public class tagBean extends TagSupport{
	private static final long serialVersionUID = 1L;
	public static final String CONST_HEAP_BEANS = "HEAP_BEANS";
	protected String name = null;
	protected String source=null;
	protected String property=null;
	protected String index=null;
	protected String method_prefix=null;
	
	protected String prefixName=null;
	protected List arguments=null;

	public int doStartTag() throws JspException {
		arguments = new ArrayList();
		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() throws JspException {
		try{
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
			if(source!=null){
				HashMap pool = (HashMap)request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTIONPOOL);
				if(pool!=null) formAction = (i_action)pool.get(source);
			}
			if(formAction!=null) source = null;
			else formAction 	= (i_action)request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTION);		
			if(formAction==null) formAction = new action(); 
			if(source==null){
				formBean = formAction.get_bean();
				if(formBean!=null)
					formBean=formBean.asBean();
			}
			Object anotherBean=null;

			if(method_prefix==null) method_prefix="get";

			if(source==null)
				anotherBean = formBean;
			else{
				if(source.equals(bsConstants.CONST_TAG_REQUESTPARAMETER)) anotherBean = request.getParameter(property);
				if(source.equals(bsConstants.CONST_TAG_SYSTEMPROPERTY)) anotherBean = System.getProperty(property);
				if(anotherBean==null) anotherBean = request.getAttribute(source);
				if(anotherBean==null) anotherBean  = request.getSession().getAttribute(source);
				if(anotherBean==null) anotherBean  = request.getSession().getServletContext().getAttribute(source);
				if(anotherBean==null) anotherBean = util_tag.getBeanAsBSTag(source,this);
				try{
					if(anotherBean==null) anotherBean = (bsController.getFromInfoNavigation(null, request)).find(source).get_content();
				}catch(Exception e){
				}
				if(anotherBean==null) anotherBean = bsController.getFromOnlySession(source, request);
				if(anotherBean==null) anotherBean = bsController.getProperty(source,request);
				
				if(anotherBean!=null){
					
					HashMap pool_seq = (HashMap)request.getAttribute(CONST_HEAP_BEANS);
					if(pool_seq!=null){
						String foundedPrefixName = (String)pool_seq.get(source);
						if(foundedPrefixName!=null)
							prefixName=foundedPrefixName;
						else prefixName=source;					
					}else prefixName=source;
					
				}
				
			}

			
			if(anotherBean==null){
				if(index!=null && index.equalsIgnoreCase("SEQUENCE") && getParent()!=null && getParent() instanceof tagSequence){
					try{

						request.setAttribute(name,((List)((tagSequence)getParent()).getSequence()).get(((tagSequence)getParent()).getIndex()));
						if(prefixName!=null){
							HashMap pool_seq = (HashMap)request.getAttribute(CONST_HEAP_BEANS);
							if(pool_seq==null){
								pool_seq=new HashMap();
								request.setAttribute(CONST_HEAP_BEANS,pool_seq);
							}
							pool_seq.put(name, prefixName);	
						}
						prefixName=null;
						
					}catch(Exception e){

					}
				}
				
				return super.doEndTag();
			}

			Object obj = null;
			if(property!=null){
				obj = util_reflect.prepareWriteValueForTag(anotherBean,method_prefix,property,arg);
				if(obj==null && arg!=null)
					obj = util_reflect.prepareWriteValueForTag(anotherBean,method_prefix,property,null);
				if(prefixName==null || prefixName.equals(""))
					prefixName=property;
				else if(prefixName.lastIndexOf('.')==prefixName.length()-1)
					prefixName+=property;
				else		
					prefixName+="."+property;
				

			}else obj = anotherBean;
			if(obj!=null && index!=null){
				if(index.equalsIgnoreCase("SEQUENCE")){
					try{
						if(getParent()!=null && getParent() instanceof tagSequence){
							index = String.valueOf(((tagSequence)getParent()).getIndex());
						}
					}catch(Exception e){
					}
					if(prefixName!=null)
						prefixName+="."+index;
				}

				Object second_obj=null;
				Object[] par = new Object[1];
				try{
					par[0]=Integer.valueOf(index);
					if(obj instanceof HashMap)
						second_obj=util_reflect.getValue(new Vector(((HashMap)obj).values()), "get",par);

					else second_obj=util_reflect.getValue(obj, "get",par);
				}catch(Exception ex){}
				if(second_obj!=null) obj = second_obj;
				else{
					try{
						par[0]=index;
						if(obj instanceof HashMap)
							second_obj=util_reflect.getValue(new Vector(((HashMap)obj).values()), "get",par);
						else second_obj=util_reflect.getValue(obj, "get",par);
					}catch(Exception ex){}
					if(second_obj!=null) obj = second_obj;
				}
			}
			request.setAttribute(name,obj);
			if(prefixName!=null){
				HashMap pool_seq = (HashMap)request.getAttribute(CONST_HEAP_BEANS);
				if(pool_seq==null){
					pool_seq=new HashMap();
					request.setAttribute(CONST_HEAP_BEANS,pool_seq);
				}
				pool_seq.put(name, prefixName);	
			}
			prefixName=null;
		}catch(Exception e){
		}
		return super.doEndTag();


	}

	public void release() {
		super.release();
		name=null;
		source=null;
		property=null;
		method_prefix=null;
		
		prefixName=null;
		arguments=null;
	}
	public String getName() {
		return name;
	}
	public String getProperty() {
		return property;
	}
	public String getSource() {
		return source;
	}
	public void setName(String string) {
		name = string;
	}
	public void setProperty(String string) {
		property = string;
	}
	public void setSource(String string) {
		source = string;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String string) {
		index = string;
	}

	public String getMethod_prefix() {
		return method_prefix;
	}

	public void setMethod_prefix(String string) {
		method_prefix = string;
	}

	public String getPrefixName() {
		return prefixName;
	}

	public void setPrefixName(String prefixName) {
		this.prefixName = prefixName;
	}

	public List getArguments() {
		return arguments;
	}

	public void setArguments(List arguments) {
		this.arguments = arguments;
	}

}

