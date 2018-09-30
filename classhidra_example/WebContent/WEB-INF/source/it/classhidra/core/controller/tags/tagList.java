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
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.DynamicAttributes;

import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_tag;
import it.classhidra.core.tool.util.util_xml;

public class tagList extends ClTagSupport implements DynamicAttributes {
	private static final long serialVersionUID = 1L;

	protected String objId = null;// id

	protected String addHiddenInput = null;
	protected String name = null;
	protected String list = null;
	protected String style = null;
	protected String styleClass=null;

	protected String bean = null;
	protected String propertys = null;
	protected String key_values = null;
	protected String formatsOutput=null;
	protected String replaceOnBlank=null;
	protected String value = null;
	protected String multiple = null;

	protected String width = null;
	protected String height = null;
	
	protected String min_width = null;
	protected String min_height = null;

	protected String tb_style = null;
	protected String tb_styleClass = null;
	protected String tb_cellpadding = null;
	protected String tb_cellspacing = null;
	protected String tb_width = null;

	protected String scroll_row_height="16";
	protected String tr_style=null;
	protected String tr_styleClass=null;
	protected String tr_onmouseover=null;
	protected String tr_onmouseout=null;
	protected String tr_onmousedown=null;
	protected String tr_onmousemove=null;
	protected String tr_onmouseup=null;
	protected String tr_onclick=null;
	protected String tr_ondblclick=null;

	protected String td_width=null;

	protected String td_style = null;
	protected String td_styleClass = null;
	protected String td_onmouseover=null;
	protected String td_onmouseout=null;
	protected String td_onmousedown=null;
	protected String td_onmousemove=null;
	protected String td_onmouseup=null;
	protected String td_onclick=null;
	protected String td_ondblclick=null;
	protected String td_nobr=null;
	protected String td_styleClassOutput=null;

	protected String onhelp=null;
	protected String onkeydown=null;
	protected String onkeypress=null;
	protected String onkeyup=null;

	protected String disabled=null;

	protected String div_id=null;
	protected String table_id=null;
	protected String array_id=null;

	protected String mark=null;
	protected String mark_styleClass=null;

	protected String method_prefix=null;
	protected String body_as_first_row=null;

	protected int sel_position=-1;
	protected int intScrollTopDiv=0;

	protected String formatLanguage=null;
	protected String formatCountry=null;
	protected String component=null;

	protected Map tagAttributes = new HashMap();



	public String getMethod_prefix() {
		return method_prefix;
	}


	public void setMethod_prefix(String method_prefix) {
		this.method_prefix = method_prefix;
	}


	public int doStartTag() throws JspException {

		final StringBuffer results = new StringBuffer();

		try{
			JspWriter writer = pageContext.getOut();

			div_id="";
			table_id="";
			array_id="";
			if(objId!=null){
				div_id="div_"+objId;
				table_id="table_"+objId;
				array_id+=objId;
			}else{
				if(bean!=null){
					div_id="div_"+bean+"_"+name;
					table_id="table_"+bean+"_"+name;
					array_id="array_"+bean+"_"+name;
				}
				else{
					div_id="div_formBean_"+name;
					table_id="table_formBean_"+name;
					array_id="array_formBean_"+name;
				}
			}

			results.append(createDIV_TagBodyStart());
			if(body_as_first_row!=null && body_as_first_row.toUpperCase().equals("TRUE")){
				Vector v_td_width = new Vector();
				Vector v_propertys = new Vector();
				int count_propertys=0;

				if(propertys!=null){
					StringTokenizer st = new StringTokenizer(propertys,";");
					while(st.hasMoreTokens()){
						v_propertys.add(st.nextToken().trim());
						count_propertys++;
					}
				}
				if(td_width!=null){
					StringTokenizer st = new StringTokenizer(td_width,";");
					while(st.hasMoreTokens()) v_td_width.add(st.nextToken().trim() );
					int len = v_td_width.size();
					for(int i=0;i<count_propertys-len;i++) v_td_width.add(null);
				}else{
					for(int i=0;i<count_propertys;i++) v_td_width.add(null);
				}
				results.append(createDIV_TagBodyTable(v_td_width));
			}

			writer.print(results);
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		return EVAL_BODY_INCLUDE;
	}


	public int doEndTag() throws JspException {

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
		if(bean==null){
			formBean = formAction.get_bean();
			if(formBean!=null)
				formBean=formBean.asBean();
		}
		
		if(component!=null && component.equalsIgnoreCase("true") && formBean!=null && (objId!=null || name!=null)) {
			renderComponent(formBean, formAction, this.getClass().getName(), ((objId!=null)?objId:((name!=null)?name:"")));
		}

		List iterator = null;

		Vector v_propertys = new Vector();
		Vector v_values = new Vector();
		Vector v_formatsOutput = new Vector();
		Vector v_replaceOnBlank = new Vector();
		Vector v_td_width = new Vector();
		Vector v_td_styleClassOutput = new Vector();
		Vector v_key_values = new Vector();

		if(method_prefix==null) method_prefix="get";

		Object anotherBean=null;


		final StringBuffer results = new StringBuffer();
		try{
			if(bean==null){
				anotherBean = formBean;
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
				if(anotherBean==null) anotherBean = bsController.getProperty(bean,request);
			}

			if(anotherBean!=null){
				Object writeValue=null;
				if(name==null){
					writeValue = anotherBean.toString();
					name=bean;
					try{
						value = writeValue.toString();
					}catch(Exception e){
					}
				}
				else{
					try{
						writeValue = util_reflect.prepareWriteValueForTag(anotherBean,method_prefix,name,null);
						if(writeValue!=null) value = writeValue.toString();
					}catch(Exception e){}

				}
				if(list!=null)
//					iterator = (List)util_reflect.getValue(anotherBean,"get"+util_reflect.adaptMethodName(list),null);
					iterator = (List)util_reflect.prepareWriteValueForTag(anotherBean,"get",list,null);
				else iterator = (List)anotherBean;

				int count_propertys=0;
				if(propertys!=null){
					StringTokenizer st = new StringTokenizer(propertys,";");
					while(st.hasMoreTokens()){
						v_propertys.add(st.nextToken().trim());
						count_propertys++;
					}
				}
				if(formatsOutput!=null){
					StringTokenizer st = new StringTokenizer(formatsOutput,";");
					while(st.hasMoreTokens()) v_formatsOutput.add(st.nextToken().trim());
					int len = v_formatsOutput.size();
					for(int i=0;i<count_propertys-len;i++) v_formatsOutput.add(null);
				}else{
					for(int i=0;i<count_propertys;i++) v_formatsOutput.add(null);
				}
				if(replaceOnBlank!=null){
					StringTokenizer st = new StringTokenizer(replaceOnBlank,";");
					while(st.hasMoreTokens()) v_replaceOnBlank.add(st.nextToken().trim());
					int len = v_replaceOnBlank.size();
					for(int i=0;i<count_propertys-len;i++) v_replaceOnBlank.add(null);
				}else{
					for(int i=0;i<count_propertys;i++) v_replaceOnBlank.add(null);
				}


				if(td_styleClassOutput!=null){
					StringTokenizer st = new StringTokenizer(td_styleClassOutput,";");
					while(st.hasMoreTokens()) v_td_styleClassOutput.add(st.nextToken().trim());
					int len = v_td_styleClassOutput.size();
					for(int i=0;i<count_propertys-len;i++) v_td_styleClassOutput.add(null);
				}else{
					for(int i=0;i<count_propertys;i++) v_td_styleClassOutput.add(null);
				}
				if(td_width!=null){
					StringTokenizer st = new StringTokenizer(td_width,";");
					while(st.hasMoreTokens()) v_td_width.add(st.nextToken().trim() );
					int len = v_td_width.size();
					for(int i=0;i<count_propertys-len;i++) v_td_width.add(null);
				}else{
					for(int i=0;i<count_propertys;i++) v_td_width.add(null);
				}
				if(value!=null){
					StringTokenizer st = new StringTokenizer(value,";");
					while(st.hasMoreTokens()) v_values.add(st.nextToken().trim());
				}
				if(key_values!=null){
					StringTokenizer st = new StringTokenizer(key_values,";");
					while(st.hasMoreTokens()) v_key_values.add(st.nextToken().trim());
				}
			}
		}catch(Exception e){
		}




		JspWriter writer = pageContext.getOut();

		try {
			if(body_as_first_row==null || body_as_first_row.toUpperCase().equals("FALSE")){
				results.append(createDIV_TagBodyTable(v_td_width));
			}
			results.append(createDIV_TagBodyFinish(iterator, v_propertys, v_values, v_formatsOutput,v_replaceOnBlank, v_td_width, v_td_styleClassOutput,v_key_values));
/*
			String row_id ="";
			try{
				if(objId!=null){
					row_id = "tr_"+objId+"_"+v_values.get(v_values.size()-1);
				}else{
					if(bean==null) row_id = "tr_formBean_"+name+"_"+v_values.get(v_values.size()-1);
					else row_id = "tr_"+bean+"_"+name+"_"+v_values.get(v_values.size()-1);
				}

				if(objId!=null){
					div_id="div_"+objId;
				}else{
					if(bean!=null){
						div_id="div_"+bean+"_"+name;
					}
					else{
						div_id="div_formBean_"+name;
					}
				}



				try{
					if(request.getParameter(div_id+"_scrollTop")!=null){
						intScrollTopDiv = Integer.valueOf(request.getParameter(div_id+"_scrollTop")).intValue();
					}
				}catch(Exception e){

				}


				results.append("<script language='javascript'>");
				results.append("try{");
				results.append("window.setTimeout(\"if(document.getElementById('"+row_id+"')){if(document.getElementById('"+div_id+"')){document.getElementById('"+div_id+"').scrollTop="+intScrollTopDiv+";}}\",1000);");
				results.append("}catch(e){}");
				results.append("</script>"+System.getProperty("line.separator"));
			}catch(Exception e){
			}
*/
			writer.print(results);
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		return EVAL_BODY_INCLUDE;
	}

	public void release() {
		super.release();
		objId = null;// id
		addHiddenInput = null;
		name = null;
		list = null;
		style = null;
		styleClass=null;

		multiple = null;

		width = null;
		height = null;
		min_width = null;
		min_height = null;

		bean = null;
		propertys = null;
		key_values = null;
		formatsOutput=null;
		replaceOnBlank=null;
		value = null;

		tb_style = null;
		tb_styleClass = null;
		tb_cellpadding = null;
		tb_cellspacing = null;
		tb_width = null;

		tr_style=null;
		tr_styleClass=null;
		tr_onmouseover=null;
		tr_onmouseout=null;
		tr_onmousedown=null;
		tr_onmousemove=null;
		tr_onmouseup=null;
		tr_onclick=null;
		tr_ondblclick=null;

		td_width=null;
		td_style = null;
		td_styleClass = null;

		td_onmouseover=null;
		td_onmouseout=null;
		td_onmousedown=null;
		td_onmousemove=null;
		td_onmouseup=null;
		td_onclick=null;
		td_ondblclick=null;
		td_nobr=null;

		onhelp=null;
		onkeydown=null;
		onkeypress=null;
		onkeyup=null;

		disabled=null;

		div_id=null;
		table_id=null;
		array_id=null;

		mark=null;
		mark_styleClass=null;
		body_as_first_row=null;

		formatLanguage=null;
		formatCountry=null;
		component=null;

		tagAttributes = new HashMap();
		
		scroll_row_height="16";
	}

	protected String createDIV_TagBodyStart() {
		final StringBuffer results = new StringBuffer("");
/*
		if(addHiddenInput!=null && addHiddenInput.toUpperCase().equals("FALSE")){
		}else{
			results.append(" <input type=\"hidden\"");
			if(objId!=null){
				results.append(" id=\"");
				results.append(objId);
				results.append('"');
			}
			if(name!=null){
				results.append(" name=\"");
				results.append(name);
				results.append('"');
			}
			if (value != null) {
				results.append(" value=\"");
				results.append(value);
				results.append('"');
			}
			results.append("/>"+System.getProperty("line.separator"));
		}
*/

		if(objId!=null){
			div_id="div_"+objId;
			table_id="table_"+objId;
			array_id+=objId;
		}else{
			if(bean!=null){
				div_id="div_"+bean+"_"+name;
				table_id="table_"+bean+"_"+name;
				array_id="array_"+bean+"_"+name;
			}
			else{
				div_id="div_formBean_"+name;
				table_id="table_formBean_"+name;
				array_id="array_formBean_"+name;
			}
		}




//		results.append(" <input type=\"hidden\" name=\""+div_id+"_scrollTop\" value=\""+intScrollTopDiv+"\"/>"+System.getProperty("line.separator"));
		results.append("<div id=\""+div_id+"\"");
		
		if(addHiddenInput!=null && addHiddenInput.toUpperCase().equals("FALSE")){
		}else{
			results.append(" <input type=\"hidden\"");
			if(objId!=null){
				results.append(" id=\"");
				results.append(objId);
				results.append('"');
			}
			if(name!=null){
				results.append(" name=\"");
				results.append(name);
				results.append('"');
			}
			if (value != null) {
				results.append(" value=\"");
				results.append(value);
				results.append('"');
			}
			results.append("/>"+System.getProperty("line.separator"));
		}

		if (styleClass != null) {
			results.append(" class=\"");
			results.append(styleClass);
			results.append('"');
		}

		if (style != null) {
			results.append(" style=\"");
			results.append(style);

			if(width!=null) results.append("width:"+width+ ((width.indexOf('%')==-1)?"px;":";"));
			if(height!=null) results.append("height:"+height+((height.indexOf('%')==-1)?"px;":";"));
			if(min_width!=null) results.append("min-width:"+min_width+ ((min_width.indexOf('%')==-1)?"px;":";"));
			if(min_height!=null) results.append("min-height:"+min_height+((min_height.indexOf('%')==-1)?"px;":";"));
			results.append('"');
		}else{
			results.append(" style=\"");
			results.append("vertical-align:top;");
			if(width!=null) results.append("width:"+width+((width.indexOf('%')==-1)?"px;":";"));
			if(height!=null) results.append("height:"+height+((height.indexOf('%')==-1)?"px;":";"));
			if(min_width!=null) results.append("min-width:"+min_width+ ((min_width.indexOf('%')==-1)?"px;":";"));
			if(min_height!=null) results.append("min-height:"+min_height+((min_height.indexOf('%')==-1)?"px;":";"));
			results.append('"');
		}
		if (onhelp != null) {
			results.append(" onhelp=\"");
			results.append(onhelp);
			results.append('"');
		}
		if(disabled==null){
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
		}
		
	    for(Object attrName : tagAttributes.keySet() ) {
	    	results.append(" ");
	    	results.append(attrName);
	    	results.append("=\"");
	    	results.append(tagAttributes.get(attrName));
	    	results.append("\"");
	      }

		
		results.append(">"+System.getProperty("line.separator"));
results.append(" <input type=\"hidden\" name=\""+div_id+"_scrollTop\" value=\""+intScrollTopDiv+"\"/>"+System.getProperty("line.separator"));	

if(addHiddenInput!=null && addHiddenInput.toUpperCase().equals("FALSE")){
}else{
	results.append(" <input type=\"hidden\"");
	if(objId!=null){
		results.append(" id=\"");
		results.append(objId);
		results.append('"');
	}
	if(name!=null){
		results.append(" name=\"");
		results.append(name);
		results.append('"');
	}
	if (value != null) {
		results.append(" value=\"");
		results.append(value);
		results.append('"');
	}
	results.append("/>"+System.getProperty("line.separator"));
}
results.append("<a id=\"a_"+div_id+"\" href=\"javascript:void(0)\" style=\"text-decoration: none;\">"+System.getProperty("line.separator"));

		return results.toString();
	}

	protected String createDIV_TagBodyTable( Vector v_td_width) {
		final StringBuffer results = new StringBuffer("");



		int table_width=0;
		try{
			for(int w=0;w<v_td_width.size();w++){
				table_width+= Integer.valueOf((String)v_td_width.get(w)).intValue();
			}
		}catch(Exception e){
			table_width=0;
		}

		results.append("<table id=\""+table_id+"\"");
		if(tb_width!=null){
			results.append(" width=\"");
			results.append(tb_width);
			results.append('"');

		}
		if(name!=null){
			results.append(" name=\"table_");
			results.append(name);
			results.append('"');
		}
		if (tb_style != null) {
			results.append(" style=\"border:none;");
			results.append(tb_style);
			results.append('"');
		}else{
			results.append(" style=\"border:none;\"");
		}
		if (tb_styleClass != null) {
			results.append(" class=\"");
			results.append(tb_styleClass);
			results.append('"');
		}else{
			results.append(" class=\"");
			results.append("tableBodyTab");
			results.append('"');
		}
		if (tb_cellpadding != null) {
			results.append(" cellpadding=\"");
			results.append(tb_cellpadding);
			results.append('"');
		}else results.append(" cellpadding=\"0\"");
		if (tb_cellspacing != null) {
			results.append(" cellspacing=\"");
			results.append(tb_cellspacing);
			results.append('"');
		}else results.append(" cellspacing=\"2\"");

		results.append(">"+System.getProperty("line.separator"));

		return results.toString();
	}

	protected String createDIV_TagBodyFinish(List iterator, Vector v_propertys, Vector v_values, Vector v_formatsOutput, Vector v_replaceOnBlank, Vector v_td_width, Vector v_td_styleClassOutput, Vector v_key_values) {
		final StringBuffer results = new StringBuffer("");
		HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();



		if ( iterator != null){
			for(int i=0;i<iterator.size();i++){
				String stylePariDispari="";
				if(i%2!=0) stylePariDispari="Disp";
				results.append(
					createTR_TagBody(i,iterator.get(i),v_propertys, v_values, v_formatsOutput,v_replaceOnBlank,v_td_width,v_td_styleClassOutput,v_key_values,stylePariDispari)
				);
			}
		}

		results.append("</table>"+System.getProperty("line.separator"));
		results.append(createSCRIPT_TagBody(v_values));
		results.append("</a>"+System.getProperty("line.separator"));
		
		String row_id ="";
		try{
			if(objId!=null){
				row_id = "tr_"+objId+"_"+v_values.get(v_values.size()-1);
			}else{
				if(bean==null) row_id = "tr_formBean_"+name+"_"+v_values.get(v_values.size()-1);
				else row_id = "tr_"+bean+"_"+name+"_"+v_values.get(v_values.size()-1);
			}

			if(objId!=null){
				div_id="div_"+objId;
			}else{
				if(bean!=null){
					div_id="div_"+bean+"_"+name;
				}
				else{
					div_id="div_formBean_"+name;
				}
			}



			try{
				if(request.getParameter(div_id+"_scrollTop")!=null){
					intScrollTopDiv = Integer.valueOf(request.getParameter(div_id+"_scrollTop")).intValue();
				}
			}catch(Exception e){

			}


			results.append("<script language='javascript'>");
			results.append("try{");
			results.append("window.setTimeout(\"if(document.getElementById('"+row_id+"')){if(document.getElementById('"+div_id+"')){document.getElementById('"+div_id+"').scrollTop="+intScrollTopDiv+";}}\",1000);");
			results.append("}catch(e){}");
			results.append("</script>"+System.getProperty("line.separator"));
		}catch(Exception e){
		}



		results.append("</div>"+System.getProperty("line.separator"));


		return results.toString();
	}

	protected String createTR_TagBody(int current_position, Object current,Vector v_propertys, Vector v_values, Vector v_formatsOutput, Vector v_replaceOnBlank, Vector v_td_width,Vector v_td_styleClassOutput, Vector v_key_values, String stylePariDispari) {
		final StringBuffer results = new StringBuffer("");
		Object writeObject = null;

		boolean selectedValue = false;
		if(v_key_values.size()==0) writeObject = current.toString();
		else{
			try{

				for(int vk=0;vk<v_key_values.size();vk++){
					Object local_writeObject = null;
					String local_key_values = (String)v_key_values.get(vk);
					local_writeObject = util_reflect.prepareWriteValueForTag(current,method_prefix,local_key_values,null);
					if(local_writeObject==null) local_writeObject = util_reflect.getValue(current,local_key_values,null);
					if(v_key_values.size()>1){
						if(local_writeObject!=null){
							if(writeObject!=null) writeObject=writeObject+"|"+local_writeObject.toString();
							else writeObject=local_writeObject.toString();
						}
					}else{
						writeObject=local_writeObject;
					}
				}
				if(writeObject.toString().equals(v_values.get(v_values.size()-1))) sel_position = current_position;
				int k=0;
				while(k<v_values.size() && !selectedValue){
					if(	writeObject.toString().equals(v_values.get(k))) selectedValue=true;
					else k++;
				}
			}catch(Exception e){
			}

		}

/*
		if(key_values==null) writeObject = current.toString();
		else{
			try{
				writeObject = util_reflect.prepareWriteValueForTag(current,method_prefix,key_values,null);
				if(writeObject==null) writeObject = util_reflect.getValue(current,key_values,null);
				if(writeObject.toString().equals(v_values.get(v_values.size()-1))) sel_position = current_position;
				int k=0;
				while(k<v_values.size() && !selectedValue){
					if(	writeObject.toString().equals(v_values.get(k))) selectedValue=true;
					else k++;
				}

			}catch(Exception e){
			}
		}
*/
		results.append("<tr ");
		if(objId!=null){
			results.append(" id=\"tr_");
			results.append(objId+"_"+writeObject);
			results.append('"');
		}else{
			if(bean==null) results.append(" id=\"tr_formBean_"+name+"_"+writeObject+"\"");
			else results.append(" id=\"tr_"+bean+"_"+name+"_"+writeObject+"\"");
		}
		if (tr_style != null) {
			results.append(" style=\"");
			results.append(tr_style);
			results.append('"');
		}else{
			results.append(" style=\"");
			results.append("cursor:pointer");
			results.append('"');
		}
		if (tr_styleClass != null) {
			results.append(" class=\"");
			if(selectedValue && multiple==null)
				results.append("colTabSel");
			else
				results.append(tr_styleClass);
			//results.append(tr_styleClass+((selectedValue && multiple==null)?"Dark":""));
			results.append('"');
		}else{
			results.append(" class=\"");
			if(selectedValue && multiple==null)
				results.append("colTabSel");
			else
				results.append("colTab1"+stylePariDispari);
			//results.append("colTab1"+((selectedValue && multiple==null)?"Dark":""));
			results.append('"');
		}
		if(disabled==null){
			if(!selectedValue || multiple!=null){
				if (tr_onmouseover != null) {
					results.append(" onmouseover=\"");
					results.append(tr_onmouseover);
					results.append('"');
				}else{
					results.append(" onmouseover=\"");
					results.append("if(!inListArray("+array_id+",this.id)) this.className='rowTabOver'");
					results.append('"');
				}

				if (tr_onmouseout != null) {
					results.append(" onmouseout=\"");
					results.append(tr_onmouseout);
					results.append('"');
				}else{
					results.append(" onmouseout=\"");
					if (tr_styleClass != null) results.append("if(!inListArray("+array_id+",this.id)) this.className='"+tr_styleClass+"'");
					else results.append("if(!inListArray("+array_id+",this.id)) this.className='colTab1"+stylePariDispari+"'");
					results.append('"');
				}
			}
			if (tr_onclick != null) {
				results.append(" onclick=\"");
				if(name!=null && key_values!=null){
					results.append("elaborate_"+name+"(this.id,'");
					results.append(writeObject);
					results.append("');");
				}
				if (tr_onclick != null) results.append(tr_onclick);
				else results.append("");
				results.append('"');
			}
			if (tr_onmousedown != null) {
				results.append(" onmousedown=\"");
				results.append(tr_onmousedown);
				results.append('"');
			}
			if (tr_onmousemove != null) {
				results.append(" onmousemove=\"");
				results.append(tr_onmousemove);
				results.append('"');
			}
			if (tr_onmouseup != null) {
				results.append(" onmouseup=\"");
				results.append(tr_onmouseup);
				results.append('"');
			}


			if (tr_ondblclick != null) {
				results.append(" ondblclick=\"");
				results.append(tr_ondblclick);
				results.append('"');
			}


		}
		results.append(">"+System.getProperty("line.separator"));

		if(v_propertys!=null){
			for(int i=0;i<v_propertys.size();i++){
				results.append(createTD_TagBody(current,v_propertys.get(i), v_formatsOutput.get(i),v_replaceOnBlank.get(i), v_td_width.get(i),v_td_styleClassOutput.get(i)));
			}
		}

		results.append("</tr>"+System.getProperty("line.separator"));
		return results.toString();
	}


	protected String createTD_TagBody(Object current, Object v_propertys_current, Object v_formatsOutput_current, Object v_replaceOnBlank_current, Object v_td_width_current, Object v_td_styleClassOutput_current) {

		final StringBuffer results = new StringBuffer("<td ");
		if(v_td_width_current!=null){
		}

		if (v_td_width_current != null) {
			results.append(" width=\"");
			results.append(v_td_width_current);
			results.append('"');
		}

		if (td_style != null) {
			results.append(" style=\"");
			results.append(td_style);
			results.append('"');
		}

		if (v_td_styleClassOutput_current != null && !v_td_styleClassOutput_current.toString().trim().equals("")) {
			results.append(" class=\"");
			results.append(v_td_styleClassOutput_current);
			results.append('"');
		}else{
			if (td_styleClass != null) {
				results.append(" class=\"");
				results.append(td_styleClass);
				results.append('"');
			}
		}

		if(disabled==null){
			if (td_onmouseover != null) {
				results.append(" onmouseover=\"");
				results.append(td_onmouseover);
				results.append('"');
			}
			if (td_onmouseout != null) {
				results.append(" onmouseout=\"");
				results.append(tr_onmouseout);
				results.append('"');
			}
			if (td_onmousedown != null) {
				results.append(" onmousedown=\"");
				results.append(td_onmousedown);
				results.append('"');
			}
			if (td_onmousemove != null) {
				results.append(" onmousemove=\"");
				results.append(td_onmousemove);
				results.append('"');
			}
			if (td_onmouseup != null) {
				results.append(" onmouseup=\"");
				results.append(td_onmouseup);
				results.append('"');
			}
			if (td_onclick != null) {
				results.append(" ondblclick=\"");
				results.append(td_onclick);
				results.append('"');
			}
			if (td_ondblclick != null) {
				results.append(" onclick=\"");
				results.append(td_ondblclick);
				results.append('"');
			}
		}
		if(td_nobr==null || td_nobr.toUpperCase().equals("TRUE"))
			results.append("><nobr>");
		else results.append('>');

		boolean marked=false;
		if(mark!=null){
			try{
//				Object mark_obj = util_reflect.getValue(current,"get"+util_reflect.adaptMethodName(mark),null);
				Object mark_obj = util_reflect.prepareWriteValueForTag(current,"get",mark,null);
				if(mark_obj==null) mark_obj = util_reflect.getValue(current,mark,null);
				if(mark_obj.toString().toUpperCase().equals("TRUE")) marked=true;
			}catch(Exception e){
			}
		}

		Object writeValue=null;
		if(current!=null){
			if(v_propertys_current==null) writeValue = current.toString();
			else{
				try{
//					writeValue = util_reflect.getValue(current,method_prefix+util_reflect.adaptMethodName(v_propertys_current.toString()),null);
//					if(writeValue==null) writeValue = util_reflect.getValue(current,v_propertys_current.toString(),null);
					writeValue = util_reflect.prepareWriteValueForTag(current,method_prefix,v_propertys_current.toString(),null);
				}catch(Exception e){}
			}
		}
		try{
			if(!v_formatsOutput_current.toString().equals(""))
				writeValue=util_format.makeFormatedString(v_formatsOutput_current.toString(),formatLanguage,formatCountry,writeValue);
			if(!v_replaceOnBlank_current.toString().equals("") && v_replaceOnBlank_current.toString().equals(writeValue.toString()))
				writeValue=util_format.replace(writeValue.toString(),v_replaceOnBlank_current.toString(),"");

		}catch(Exception e){}
		if(marked){
			if(mark_styleClass!=null) results.append("span class=\""+mark_styleClass+"\">");
			else results.append("<font color=\"red\">");
		}
		if(writeValue!=null)
			results.append(util_xml.normalHTML(writeValue.toString(),null));
 
		if(marked){
			if(mark_styleClass!=null) results.append("</span>");
			else results.append("</font>");
		}
		if(td_nobr==null || td_nobr.toUpperCase().equals("TRUE"))
			results.append("</nobr></td>"+System.getProperty("line.separator"));
		else results.append("</td>"+System.getProperty("line.separator"));

		return results.toString();
	}

	protected String createSCRIPT_TagBody(Vector v_values) {
		final StringBuffer results = new StringBuffer("");
		results.append("<script>"+System.getProperty("line.separator"));



		results.append("	var "+array_id+" = new Array();"+System.getProperty("line.separator"));
		results.append("	var values_"+array_id+" = new Array();"+System.getProperty("line.separator"));

		if(v_values!=null){

			for(int i=0;i<v_values.size();i++){
				String tr_id="tr_";
				String current_values = (String)v_values.get(i);
				if(current_values!=null) current_values=current_values.replace('"','\"');
				if(objId!=null)	tr_id+=objId+"_"+v_values.get(i);
				else{
					if(bean==null) tr_id+="formBean_"+name+"_"+v_values.get(i);
					else tr_id+=bean+"_"+name+"_"+v_values.get(i);
				}
				results.append("	"+array_id+"["+i+"]=\""+tr_id+"\";"+System.getProperty("line.separator"));
				results.append("	values_"+array_id+"["+i+"]=\""+v_values.get(i)+"\";"+System.getProperty("line.separator"));
			}
		}
		results.append(System.getProperty("line.separator"));
		if(multiple!=null)
			results.append("	initListArray("+array_id+");"+System.getProperty("line.separator"));
		if(disabled==null){
			results.append("function elaborate_"+name+"(obj_name,value_click){"+System.getProperty("line.separator"));

			if(multiple==null){
				results.append("	document.forms[0]."+name+".value=value_click;"+System.getProperty("line.separator"));
			}else{
				results.append("	document.forms[0]."+name+".value=refreshListArray("+array_id+",values_"+array_id+",obj_name,value_click);"+System.getProperty("line.separator"));
			}
			results.append("	document.forms[0]."+div_id+"_scrollTop.value=document.getElementById(\""+div_id+"\").scrollTop;"+System.getProperty("line.separator"));
			results.append("}"+System.getProperty("line.separator"));
		}
		results.append("</script>"+System.getProperty("line.separator"));

		return results.toString();
	}

	public String getBean() {
		return bean;
	}

	public String getFormatsOutput() {
		return formatsOutput;
	}

	public String getName() {
		return name;
	}

	public String getObjId() {
		return objId;
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

	public String getPropertys() {
		return propertys;
	}

	public String getStyle() {
		return style;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public String getTb_cellpadding() {
		return tb_cellpadding;
	}

	public String getTb_cellspacing() {
		return tb_cellspacing;
	}


	public String getTb_styleClass() {
		return tb_styleClass;
	}

	public String getTd_onclick() {
		return td_onclick;
	}

	public String getTd_ondblclick() {
		return td_ondblclick;
	}

	public String getTd_onmousedown() {
		return td_onmousedown;
	}

	public String getTd_onmousemove() {
		return td_onmousemove;
	}

	public String getTd_onmouseout() {
		return td_onmouseout;
	}

	public String getTd_onmouseover() {
		return td_onmouseover;
	}

	public String getTd_onmouseup() {
		return td_onmouseup;
	}

	public String getTd_width() {
		return td_width;
	}

	public String getTr_onclick() {
		return tr_onclick;
	}

	public String getTr_ondblclick() {
		return tr_ondblclick;
	}

	public String getTr_onmousedown() {
		return tr_onmousedown;
	}

	public String getTr_onmousemove() {
		return tr_onmousemove;
	}

	public String getTr_onmouseout() {
		return tr_onmouseout;
	}

	public String getTr_onmouseover() {
		return tr_onmouseover;
	}

	public String getTr_onmouseup() {
		return tr_onmouseup;
	}

	public String getTr_style() {
		return tr_style;
	}

	public String getTr_styleClass() {
		return tr_styleClass;
	}

	public String getValue() {
		return value;
	}

	public void setBean(String string) {
		bean = string;
	}



	public void setName(String string) {
		name = string;
	}

	public void setObjId(String string) {
		objId = string;
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

	public void setPropertys(String string) {
		propertys = string;
	}

	public void setStyle(String string) {
		style = string;
	}

	public void setStyleClass(String string) {
		styleClass = string;
	}

	public void setTb_cellpadding(String string) {
		tb_cellpadding = string;
	}

	public void setTb_cellspacing(String string) {
		tb_cellspacing = string;
	}


	public void setTb_styleClass(String string) {
		tb_styleClass = string;
	}

	public void setTd_onclick(String string) {
		td_onclick = string;
	}

	public void setTd_ondblclick(String string) {
		td_ondblclick = string;
	}

	public void setTd_onmousedown(String string) {
		td_onmousedown = string;
	}

	public void setTd_onmousemove(String string) {
		td_onmousemove = string;
	}

	public void setTd_onmouseout(String string) {
		td_onmouseout = string;
	}

	public void setTd_onmouseover(String string) {
		td_onmouseover = string;
	}

	public void setTd_onmouseup(String string) {
		td_onmouseup = string;
	}



	public void setTr_onclick(String string) {
		tr_onclick = string;
	}

	public void setTr_ondblclick(String string) {
		tr_ondblclick = string;
	}

	public void setTr_onmousedown(String string) {
		tr_onmousedown = string;
	}

	public void setTr_onmousemove(String string) {
		tr_onmousemove = string;
	}

	public void setTr_onmouseout(String string) {
		tr_onmouseout = string;
	}

	public void setTr_onmouseover(String string) {
		tr_onmouseover = string;
	}

	public void setTr_onmouseup(String string) {
		tr_onmouseup = string;
	}

	public void setTr_style(String string) {
		tr_style = string;
	}

	public void setTr_styleClass(String string) {
		tr_styleClass = string;
	}

	public void setValue(String string) {
		value = string;
	}

	public String getList() {
		return list;
	}

	public void setList(String string) {
		list = string;
	}

	public String getTb_style() {
		return tb_style;
	}

	public String getTd_style() {
		return td_style;
	}

	public String getTd_styleClass() {
		return td_styleClass;
	}

	public void setTb_style(String string) {
		tb_style = string;
	}

	public void setTd_style(String string) {
		td_style = string;
	}

	public void setTd_styleClass(String string) {
		td_styleClass = string;
	}

	public String getKey_values() {
		return key_values;
	}

	public void setKey_values(String string) {
		key_values = string;
	}

	public String getHeight() {
		return height;
	}

	public String getWidth() {
		return width;
	}

	public void setHeight(String string) {
		height = string;
	}

	public void setWidth(String string) {
		width = string;
	}

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String string) {
		multiple = string;
	}

	public String getAddHiddenInput() {
		return addHiddenInput;
	}

	public void setAddHiddenInput(String string) {
		addHiddenInput = string;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String string) {
		disabled = string;
	}

	public String getScroll_row_height() {
		return scroll_row_height;
	}

	public void setScroll_row_height(String string) {
		scroll_row_height = string;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String string) {
		mark = string;
	}

	public String getMark_styleClass() {
		return mark_styleClass;
	}

	public void setMark_styleClass(String string) {
		mark_styleClass = string;
	}

	public String getTb_width() {
		return tb_width;
	}

	public void setTb_width(String string) {
		tb_width = string;
	}


	public String getTd_nobr() {
		return td_nobr;
	}


	public void setTd_nobr(String td_nobr) {
		this.td_nobr = td_nobr;
	}


	public String getBody_as_first_row() {
		return body_as_first_row;
	}


	public void setBody_as_first_row(String body_as_first_row) {
		this.body_as_first_row = body_as_first_row;
	}


	public String getTd_styleClassOutput() {
		return td_styleClassOutput;
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


	public String getReplaceOnBlank() {
		return replaceOnBlank;
	}

	public void setFormatsOutput(String string) {
		if(string==null || string.length()==0) formatsOutput = string;
//		while(string.indexOf(";;")>-1) string = string.replace(";;","; ;");
		while(string.indexOf(";;")>-1) string = util_format.replace(string,";;","; ;");
		if(string.indexOf(";")==0) string = " "+ string;
		formatsOutput=string;
	}

	public void setReplaceOnBlank(String string) {
		if(string==null || string.length()==0) replaceOnBlank = string;
		while(string.indexOf(";;")>-1) string = util_format.replace(string,";;","; ;");
		if(string.indexOf(";")==0) string = " "+ string;
		replaceOnBlank=string;
	}
/*
	public void setTd_styleClassOutput(String tdStyleClassOutput) {
		td_styleClassOutput = tdStyleClassOutput;
	}
	public void setTd_width(String string) {
		td_width = string;
	}
*/
	public void setTd_styleClassOutput(String string) {
		if(string==null || string.length()==0) td_styleClassOutput = string;
		while(string.indexOf(";;")>-1) string = util_format.replace(string,";;","; ;");
		if(string.indexOf(";")==0) string = " "+ string;
		td_styleClassOutput=string;
	}
	public void setTd_width(String string) {
		if(string==null || string.length()==0) td_width = string;
		while(string.indexOf(";;")>-1) string = util_format.replace(string,";;","; ;");
		if(string.indexOf(";")==0) string = " "+ string;
		td_width=string;
	}


	public String getMin_width() {
		return min_width;
	}


	public void setMin_width(String minWidth) {
		min_width = minWidth;
	}


	public String getMin_height() {
		return min_height;
	}


	public void setMin_height(String minHeight) {
		min_height = minHeight;
	}
	
	public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
		tagAttributes.put(localName, value);
	}


	public String getTable_id() {
		return table_id;
	}


	public void setTable_id(String table_id) {
		this.table_id = table_id;
	}


	public String getComponent() {
		return component;
	}


	public void setComponent(String component) {
		this.component = component;
	}
}

