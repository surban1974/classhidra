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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.DynamicAttributes;

import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.i_tag_helper;
import it.classhidra.core.tool.exception.bsTagEndRendering;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_tag;
import it.classhidra.core.tool.util.util_xml;

public class tagOptions extends ClTagSupport implements DynamicAttributes {
	private static final long serialVersionUID = -1L;
	protected String objId = null;// id
	protected String label= null;
	protected String lang = null;
	protected String style = null;
	protected String title = null;
	protected String value = null;
	protected String bean = null;
	protected String property = null;
	protected String styleClass=null;
	protected String dir=null;//"ltr"

	protected String formatOutput=null;

	protected String onclick=null;
	protected String ondblclick=null;
	protected String onhelp=null;
	protected String onkeydown=null;
	protected String onkeypress=null;
	protected String onkeyup=null;
	protected String onmousedown=null;
	protected String onmousemove=null;
	protected String onmouseout=null;
	protected String onmouseover=null;
	protected String onmouseup=null;


	protected String onblur = null;
	protected String onchange = null;
	protected String oncontextmenu = null;
	protected String onfocus = null;
	protected String onformchange = null;
	protected String onforminput = null;
	protected String oninput = null;
	protected String oninvalid = null;
	protected String onselect = null;
	protected String ondrag = null;
	protected String ondragend = null;
	protected String ondragenter = null;
	protected String ondragleave = null;
	protected String ondragover = null;
	protected String ondragstart = null;
	protected String ondrop = null;
	protected String onmousewheel = null;
	protected String additionalAttr=null;
	
	protected String normalXML=null;
	protected String normalXML10=null;
	protected String normalXML11=null;
	protected String normalXMLCDATA=null;
	protected String charset;
	protected String normalASCII=null;
	protected String normalHTML=null;	



	protected String formatInput = null;
	protected String ignoreCase =null;
	protected String component=null;
	protected String rendering = null;
	
	protected Map<String,Object> tagAttributes = new HashMap<String, Object>();
	protected List<Object> arguments=null;


	public int doStartTag() throws JspException {
		arguments = new ArrayList<Object>();
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {

		final HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
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
		if(component!=null && component.equalsIgnoreCase("true") && formBean!=null && (objId!=null)) {
			renderComponent(formBean, formAction, this.getClass().getName(), (objId),
					(rendering!=null && rendering.equalsIgnoreCase(i_tag_helper.CONST_TAG_RENDERING_FULL))?true:false);
		}
		List<?> iterator = null;
		if(property!=null)
			property=checkParametersIfDynamic(property, null);
		final StringBuffer results = new StringBuffer();
		try{
			if(bean==null){
				iterator = (List<?>)util_reflect.prepareWriteValueForTag(formBean,"get",property,null);
			}else{
				Object anotherBean = null;
				if(anotherBean==null) anotherBean = request.getAttribute(bean);
				if(anotherBean==null) anotherBean = request.getSession().getAttribute(bean);
				if(anotherBean==null) anotherBean = request.getSession().getServletContext().getAttribute(bean);
				if(anotherBean==null) anotherBean = util_tag.getBeanAsBSTag(bean,this);
				try{
					if(anotherBean==null) anotherBean = (bsController.getFromInfoNavigation(null, request)).find(bean).get_content();
				}catch(Exception e){
				}
				if(anotherBean==null) anotherBean = bsController.getFromOnlySession(bean, request);
				if(anotherBean==null) anotherBean = bsController.getFromOnlyServletContext(bean, request);
				if(anotherBean==null) anotherBean = bsController.getProperty(bean,request);
				
				if(property!=null)
					iterator = (List<?>)util_reflect.prepareWriteValueForTag(anotherBean,"get",property,null);
				else iterator = (List<?>)anotherBean;
			}
		}catch(Exception e){
			results.append("<!--"+e.toString()+"-->");
		}

		if ( iterator != null)
		{
			for(int i=0;i<iterator.size();i++){
				results.append(this.createTagBody(iterator.get(i)));
			}
		}
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		
		if(component!=null && component.equalsIgnoreCase("true") && (objId!=null )) {
			String componentId = (String)request.getAttribute(i_tag_helper.CONST_TAG_COMPONENT_ID);
			if(componentId!=null && (componentId.equals(objId) )) {
				PageContext pageContext = (PageContext)request.getAttribute(i_tag_helper.CONST_TAG_PAGE_CONTEXT);
				if(pageContext!=null) {
					try {						
						pageContext.getOut().write(results.toString());
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
		objId = null;// id
		label= null;
		lang = null;
		style = null;
		title = null;
		value = null;
		bean = null;
		property = null;
		styleClass=null;
		dir=null;//"ltr"
		formatOutput=null;
		formatInput=null;

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
		oncontextmenu = null;
		onfocus = null;
		onformchange = null;
		onforminput = null;
		oninput = null;
		oninvalid = null;
		onselect = null;
		ondrag = null;
		ondragend = null;
		ondragenter = null;
		ondragleave = null;
		ondragover = null;
		ondragstart = null;
		ondrop = null;
		onmousewheel = null;
		ignoreCase = null;
		component=null;
		rendering=null;
		additionalAttr= null;
		
		normalXML=null;
		normalXML10=null;
		normalXML11=null;
		normalXMLCDATA=null;
		charset=null;
		normalASCII=null;
		normalHTML=null;	
		
		tagAttributes = new HashMap<String, Object>();
		arguments=null;

	}

	protected String createTagBody(Object current) {
		Object[] arg = null;
		if(arguments!=null && arguments.size()>0){
			arg = new Object[arguments.size()];
			for(int i=0;i<arguments.size();i++)
				arg[i]=arguments.get(i);
		}
		if(arguments!=null)
			arguments.clear();
		
		final StringBuffer results = new StringBuffer("<option ");

		Object currentValue=null;
		Object currentLabel=null;
		if(current!=null){
			if(value==null) currentValue = current;
			else{
				try{
					currentValue = util_reflect.prepareWriteValueForTag(current,"get",value,arg);
				}catch(Exception e){}
			}
			if(label==null) currentLabel = current;
			else{
				try{
					currentLabel = util_reflect.prepareWriteValueForTag(current,"get",label,arg);
				}catch(Exception e){}
			}
		}

		if(objId!=null){
			results.append(" id=\"");
			results.append(objId);
			results.append('"');
		}
		if (currentValue != null) {
			results.append(" value=\"");
			
			
			if(normalXML!=null && normalXML.toLowerCase().equals("true"))
				results.append(util_xml.normalXML((currentValue==null)?"":currentValue.toString(),charset));	
			else if(normalXML10!=null && normalXML10.toLowerCase().equals("true"))
				results.append(util_xml.escapeXML10((currentValue==null)?"":currentValue.toString(),charset));		
			else if(normalXML11!=null && normalXML11.toLowerCase().equals("true"))
				results.append(util_xml.escapeXML11((currentValue==null)?"":currentValue.toString(),charset));		
			else if(normalXMLCDATA!=null && normalXMLCDATA.toLowerCase().equals("true"))
				results.append(util_xml.normalCDATA((currentValue==null)?"":currentValue.toString(),charset));			
			else if(normalASCII!=null && normalASCII.equalsIgnoreCase("true"))	
				results.append(util_xml.normalASCII((currentValue==null)?"":currentValue.toString()));	
			else if(normalHTML!=null && normalHTML.equalsIgnoreCase("true"))
				results.append(util_xml.normalHTML((currentValue==null)?"":currentValue.toString(), null));	
			else 
				results.append(util_xml.normalHTML((currentValue==null)?"":currentValue.toString(),null));
			
			
			
			results.append('"');
		}else	results.append(" value=\"\"");

		try{
			if(getParent()!=null && getParent() instanceof tagSelect){
				if(((tagSelect)getParent()).getValue().equals(currentValue.toString()))
					results.append(" selected ");
				else{
					if(ignoreCase!=null && ignoreCase.equalsIgnoreCase("true") && ((tagSelect)getParent()).getValue().equalsIgnoreCase(currentValue.toString()))
						results.append(" selected ");
				}
			}
		}catch(Exception e){
		}

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
			results.append(onchange);
			results.append('"');
		}
		if (oncontextmenu != null) {
			results.append(" oncontextmenu=\"");
			results.append(oncontextmenu);
			results.append('"');
		}
		if (onfocus != null) {
			results.append(" onfocus=\"");
			results.append(onfocus);
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
		if (onselect != null) {
			results.append(" onselect=\"");
			results.append(onselect);
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
			results.append(" ");
		}	

	    for(Object attrName : tagAttributes.keySet() ) {
	    	results.append(" ");
	    	results.append(attrName);
	    	results.append("=\"");
	    	results.append(tagAttributes.get(attrName));
	    	results.append("\"");
	      }
		

		results.append('>');
		if (currentLabel != null){
			try{
				currentLabel=util_format.makeFormatedString(formatOutput,currentLabel);
			}catch(Exception e){}
			if(normalXML!=null && normalXML.toLowerCase().equals("true"))
				results.append(util_xml.normalXML((currentLabel==null)?"":currentLabel.toString(),charset));	
			else if(normalXML10!=null && normalXML10.toLowerCase().equals("true"))
				results.append(util_xml.escapeXML10((currentLabel==null)?"":currentLabel.toString(),charset));		
			else if(normalXML11!=null && normalXML11.toLowerCase().equals("true"))
				results.append(util_xml.escapeXML11((currentLabel==null)?"":currentLabel.toString(),charset));		
			else if(normalASCII!=null && normalASCII.equalsIgnoreCase("true"))	
				results.append(util_xml.normalASCII((currentLabel==null)?"":currentLabel.toString()));	
			else if(normalHTML!=null && normalHTML.equalsIgnoreCase("true"))
				results.append(util_xml.normalHTML((currentLabel==null)?"":currentLabel.toString(), null));	
			else 
				results.append(currentLabel);
		}
		results.append("</option>");
		results.append(System.getProperty("line.separator"));

		return results.toString();
	}

	public String getBean() {
		return bean;
	}
	public String getDir() {
		return dir;
	}
	public String getLabel() {
		return label;
	}
	public String getLang() {
		return lang;
	}
	public String getObjId() {
		return objId;
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
	public String getProperty() {
		return property;
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
	public String getValue() {
		return value;
	}
	public void setBean(String string) {
		bean = string;
	}
	public void setDir(String string) {
		dir = string;
	}
	public void setLabel(String string) {
		label = string;
	}
	public void setLang(String string) {
		lang = string;
	}
	public void setObjId(String string) {
		objId = string;
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
	public void setProperty(String string) {
		property = string;
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
	public void setValue(String string) {
		value = string;
	}
	public String getFormatOutput() {
		return formatOutput;
	}
	public void setFormatOutput(String string) {
		formatOutput = string;
	}

	public String getFormatInput() {
		return formatInput;
	}

	public void setFormatInput(String formatInput) {
		this.formatInput = formatInput;
	}

	public String getOnblur() {
		return onblur;
	}

	public void setOnblur(String onblur) {
		this.onblur = onblur;
	}

	public String getOnchange() {
		return onchange;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public String getOncontextmenu() {
		return oncontextmenu;
	}

	public void setOncontextmenu(String oncontextmenu) {
		this.oncontextmenu = oncontextmenu;
	}

	public String getOnfocus() {
		return onfocus;
	}

	public void setOnfocus(String onfocus) {
		this.onfocus = onfocus;
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

	public String getOnselect() {
		return onselect;
	}

	public void setOnselect(String onselect) {
		this.onselect = onselect;
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

	public String getIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(String ignoreCase) {
		this.ignoreCase = ignoreCase;
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

	public List<Object> getArguments() {
		return arguments;
	}

	public void setArguments(List<Object> arguments) {
		this.arguments = arguments;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getRendering() {
		return rendering;
	}

	public void setRendering(String rendering) {
		this.rendering = rendering;
	}

	public String getNormalXML() {
		return normalXML;
	}

	public void setNormalXML(String normalXML) {
		this.normalXML = normalXML;
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

	public String getNormalXMLCDATA() {
		return normalXMLCDATA;
	}

	public void setNormalXMLCDATA(String normalXMLCDATA) {
		this.normalXMLCDATA = normalXMLCDATA;
	}

}

