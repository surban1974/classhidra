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


import it.classhidra.core.controller.*;
import it.classhidra.core.init.*;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class tagLink extends TagSupport implements DynamicAttributes {
	private static final long serialVersionUID = -5425047027434695445L;
	protected String charset = null;
	protected String classObj = null;
	protected String dir = null;//"ltr"
	protected String href = null;
	protected String hreflang = null;
	protected String id = null;
	protected String lang = null;
	protected String media = null;
	protected String onclick = null;
	protected String ondblclick = null;
	protected String onhelp = null;
	protected String onkeydown = null;
	protected String onkeypress = null;
	protected String onkeyup = null;
	protected String onmousedown = null;
	protected String onmousemove = null;
	protected String onmouseout = null;
	protected String onmouseover = null;
	protected String onmouseup = null;
	protected String rel = null;
	protected String rev = null;
	protected String style = null;
	protected String target = null;
	protected String title = null;
	protected String type = null;
	protected String additionalAttr = null;


	protected Map tagAttributes = new HashMap();



	public int doStartTag() throws JspException {
		final StringBuffer results = new StringBuffer();
		results.append(this.createTagBody());
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		return (EVAL_BODY_INCLUDE);

	}
/*
	public int doEndTag() throws JspException {

		final StringBuffer results = new StringBuffer("</form>");
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}

		return (EVAL_PAGE);
	}
*/
	public void release() {
		super.release();
		charset = null;
		classObj = null;
		dir = null;//"ltr"
		href = null;
		hreflang = null;
		id = null;
		lang = null;
		media = null;
		onclick = null;
		ondblclick = null;
		onhelp = null;
		onkeydown = null;
		onkeypress = null;
		onkeyup = null;
		onmousedown = null;
		onmousemove = null;
		onmouseout = null;
		onmouseover = null;
		onmouseup = null;
		rel = null;
		rev = null;
		style = null;
		target = null;
		title = null;
		type = null;
		additionalAttr = null;

		
		tagAttributes = new HashMap();
	}

	protected String createTagBody() {


		HttpServletRequest  request   		= (HttpServletRequest) this.pageContext.getRequest();

		final StringBuffer results = new StringBuffer("");
		results.append("<link ");

		if (charset != null) {
			results.append(" charset=\"");
			results.append(charset);
			results.append('"');
		}
		if (classObj != null) {
			results.append(" classObj=\"");
			results.append(classObj);
			results.append('"');
		}
		if (dir != null) {
			results.append(" dir=\"");
			results.append(dir);
			results.append('"');
		}
		if (href != null) {
			results.append(" href=\"");
			results.append(href);
			results.append('"');
		}else{
			results.append(" href=\"../css/Pagine" + ((request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION)!=null)?((auth_init)request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION)).get_risoluzione() : "1024")+ ".css\"");
		}
		if (hreflang != null) {
			results.append(" hreflang=\"");
			results.append(hreflang);
			results.append('"');
		}
		if (id != null) {
			results.append(" id=\"");
			results.append(id);
			results.append('"');
		}
		if (lang != null) {
			results.append(" lang=\"");
			results.append(lang);
			results.append('"');
		}
		if (media != null) {
			results.append(" media=\"");
			results.append(media);
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
		if (rel != null) {
			results.append(" rel=\"");
			results.append(rel);
			results.append('"');
		}else{
			results.append(" rel=\"stylesheet\"");
		}
		if (rev != null) {
			results.append(" rev=\"");
			results.append(rev);
			results.append('"');
		}
		if (style != null) {
			results.append(" style=\"");
			results.append(style);
			results.append('"');
		}
		if (target != null) {
			results.append(" target=\"");
			results.append(target);
			results.append('"');
		}
		if (title != null) {
			results.append(" title=\"");
			results.append(title);
			results.append('"');
		}
		if (type != null) {
			results.append(" type=\"");
			results.append(type);
			results.append('"');
		}else{
			results.append(" type=\"text/css\"");
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
		

		results.append(" />");
		return results.toString();

	}

	public String getCharset() {
		return charset;
	}
	public String getClassObj() {
		return classObj;
	}
	public String getDir() {
		return dir;
	}
	public String getHref() {
		return href;
	}
	public String getHreflang() {
		return hreflang;
	}
	public String getId() {
		return id;
	}
	public String getLang() {
		return lang;
	}
	public String getMedia() {
		return media;
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
	public String getRel() {
		return rel;
	}
	public String getRev() {
		return rev;
	}
	public String getStyle() {
		return style;
	}
	public String getTarget() {
		return target;
	}
	public String getTitle() {
		return title;
	}
	public String getType() {
		return type;
	}
	public void setCharset(String string) {
		charset = string;
	}
	public void setClassObj(String string) {
		classObj = string;
	}
	public void setDir(String string) {
		dir = string;
	}
	public void setHref(String string) {
		href = string;
	}
	public void setHreflang(String string) {
		hreflang = string;
	}
	public void setId(String string) {
		id = string;
	}
	public void setLang(String string) {
		lang = string;
	}
	public void setMedia(String string) {
		media = string;
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
	public void setRel(String string) {
		rel = string;
	}
	public void setRev(String string) {
		rev = string;
	}
	public void setStyle(String string) {
		style = string;
	}
	public void setTarget(String string) {
		target = string;
	}
	public void setTitle(String string) {
		title = string;
	}
	public void setType(String string) {
		type = string;
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


}

