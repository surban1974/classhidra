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


import it.classhidra.core.controller.bsController;
import it.classhidra.core.tool.exception.message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*; 

import java.util.HashMap;
import java.util.Vector;
import java.io.IOException;

public class showErrorsMessages extends TagSupport{
	private static final long serialVersionUID = -219781416727117100L;
	protected String style = null;
	protected String styleClass = null;
	protected HashMap parameters=null;


	public int doStartTag() throws JspException {
		parameters=new HashMap();
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
		
		this.release();
		return EVAL_BODY_INCLUDE;
	}

	public void release() {
		super.release();
		style=null;
		styleClass=null;
	}
  
	protected String createTagBody() {
		HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
		final StringBuffer results = new StringBuffer("");
		Vector $listmessages = new Vector();
		try{
			$listmessages = (Vector)request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE);
		}catch(Exception e){
		}
		for(int i=0;i<$listmessages.size();i++){
			message mess = (message)$listmessages.get(i);
			String color = "black";
			if(mess.getTYPE().equals("E")) 	color="red";
			if(mess.getTYPE().equals("W")) color="orange";
			if(mess.getTYPE().equals("I")) 	color="green";

			results.append("<tr>"+System.getProperty("line.separator"));
			results.append("	<td >"+System.getProperty("line.separator"));
			results.append("	<table");
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
			if (styleClass == null && style==null)results.append(" width=500 ");			
			results.append(">"+System.getProperty("line.separator"));
			results.append("	<tr>"+System.getProperty("line.separator"));
			results.append("	<td align='center' bgcolor='white' onclick=\"if(document.getElementById('bar_mess').style) document.getElementById('bar_mess').style.visibility='hidden';\" style='cursor:pointer'>"+System.getProperty("line.separator")); 
			results.append("	<font color='"+color+"'>"+mess.getDESC_MESS()+"</font>"+System.getProperty("line.separator"));
			results.append("	</td>"+System.getProperty("line.separator"));
			results.append("	</tr>"+System.getProperty("line.separator"));
			results.append("	</table>"+System.getProperty("line.separator"));
			results.append("	</td>"+System.getProperty("line.separator"));
			results.append("</tr>"+System.getProperty("line.separator"));
		}		
		return results.toString();
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

	public HashMap getParameters() {
		return parameters;
	}

}

