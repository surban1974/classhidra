/**
* Creation date: (07/04/2006)
* @author: Svyatoslav Urbanovych svyatoslav.urbanovych@gmail.com
*/

/********************************************************************************
*
*	Copyright (C) 2006  Svyatoslav Urbanovych
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



import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.IOException;

public class showLMPage extends TagSupport{
	private static final long serialVersionUID = 7338460159642268170L;
	protected String name = null;
	protected String style = null;
	protected String styleClass = null;


	public int doStartTag() throws JspException {


		final StringBuffer results = new StringBuffer();
		results.append("<div id='"+name+"' name='"+name+"' style='position:absolute;visibility: hidden'>"+System.getProperty("line.separator"));
		results.append("<font face='GENEVA, ARIAL, MS SANS SERIF, SANS-SERIF' size='1'>"+System.getProperty("line.separator"));
		results.append("<table cellspacing='0' border='0'");
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
		if(styleClass == null && style==null) results.append(" class='block_background'");
		results.append(">"+System.getProperty("line.separator"));

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
		name=null;
		style=null;
		styleClass=null;
	}


	public String getStyleClass() {
		return styleClass;
	}
	public void setStyleClass(String string) {
		styleClass = string;
	}
	public String getStyle() {
		return style;
	}

	public void setStyle(String string) {
		style = string;
	}

	public int doEndTag() throws JspException {
		final StringBuffer results = new StringBuffer();

		results.append("</table>"+System.getProperty("line.separator"));
		results.append("</font>"+System.getProperty("line.separator"));
		results.append("</div>"+System.getProperty("line.separator"));

		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}

		this.release();
		return super.doEndTag();
	}

	public String getName() {
		return name;
	}

	public void setName(String string) {
		name = string;
	}

}

