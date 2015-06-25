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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_tag;

public class tagOptions extends TagSupport{
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


	protected String formatInput = null;
	protected String ignoreCase =null;


	public int doStartTag() throws JspException {

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

		List				iterator = null;

		StringBuffer results = new StringBuffer();
		try{
			if(bean==null){
				iterator = (List)util_reflect.prepareWriteValueForTag(formBean,"get",property,null);
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
				if(anotherBean==null) anotherBean = bsController.getProperty(bean,request);
				
				if(property!=null)
					iterator = (List)util_reflect.prepareWriteValueForTag(anotherBean,"get",property,null);
				else iterator = (List)anotherBean;
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
		return EVAL_BODY_INCLUDE;
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
		ignoreCase =null;

	}

	protected String createTagBody(Object current) {

		StringBuffer results = new StringBuffer("<option ");

		Object currentValue=null;
		Object currentLabel=null;
		if(current!=null){
			if(value==null) currentValue = current;
			else{
				try{
					currentValue = util_reflect.prepareWriteValueForTag(current,"get",value,null);
				}catch(Exception e){}
			}
			if(label==null) currentLabel = current;
			else{
				try{
					currentLabel = util_reflect.prepareWriteValueForTag(current,"get",label,null);
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
			results.append(currentValue);
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


		results.append('>');
		if (currentLabel != null){
			try{
				currentLabel=util_format.makeFormatedString(formatOutput,currentLabel);
			}catch(Exception e){}
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



}

