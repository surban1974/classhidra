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
import it.classhidra.core.controller.info_navigation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import java.util.HashMap;
import java.util.Vector;
import java.io.IOException;

public class showNavigateStory extends TagSupport{
	private static final long serialVersionUID = -8734241232839720501L;
	protected String style = null;
	protected String styleClass = null;
	protected String index = null;
	protected String _return = null;
	protected HashMap parameters=null;


	public HashMap getParameters() {
		return parameters;
	}

	public int doStartTag() throws JspException {
		parameters=new HashMap();
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
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
		style=null;
		styleClass=null;
		index=null;
		_return=null;
		parameters=null;

	}

	protected String createTagBody() {
		HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
		StringBuffer results = new StringBuffer("");

		try{
			info_navigation	formInfoNavigation		= (request.getSession().getAttribute(bsController.CONST_BEAN_$NAVIGATION)==null)?new info_navigation():(info_navigation)request.getSession().getAttribute(bsController.CONST_BEAN_$NAVIGATION);

			info_navigation iN = formInfoNavigation;
			Vector sub_results = new Vector();
			Vector sub_results_action = new Vector();
			Vector sub_results_name = new Vector();


			while(iN!=null){
				try{
					String subLabel="";
					if(iN.getIRedirect().getMess_id()!=null && !iN.getIRedirect().getMess_id().equals("")){
						subLabel = "<a style=\"cursor:pointer;\" onclick=\"goAction('"+iN.getIAction().getPath()+"','"+iN.getIAction().getWac()+"')\">"+bsController.writeLabel(request,iN.getIRedirect().getMess_id(),iN.getIRedirect().getDescr(),parameters)+iN.getDesc_second()+"</a>";
						sub_results.add(subLabel);
						sub_results_action.add("goAction('"+iN.getIAction().getPath()+"','"+iN.getIAction().getWac()+"')");
						sub_results_name.add(bsController.writeLabel(request,iN.getIRedirect().getMess_id(),iN.getIRedirect().getDescr(),parameters)+iN.getDesc_second());
					}
				}catch(Exception ex){
				}
				iN = iN.getChild();
			}

			int ind = -1;
			try{
				ind = sub_results.size() -1-Integer.valueOf(index).intValue();
				sub_results.get(ind-1);
			}catch(Exception e){
				if(index!=null) ind=-2;
				else ind=-1;
			}


			if(ind==-1){
				for(int i=0;i<sub_results.size();i++){
					String subLabel=(String)sub_results.get(i);
					if(!subLabel.trim().equals("")){
						results.append("<span ");
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
						results.append(">"+subLabel + ((i<sub_results.size()-1)?" &#8594; ":""));
						results.append("</span>");
					}
				}
			}
			if(ind>-1){
				if(_return==null) _return="name";
				if(_return.equals("name")) results.append((String)sub_results_name.get(ind));
				if(_return.equals("action")) results.append((String)sub_results_action.get(ind));
			}

		}catch(Exception e){

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

	public String getIndex() {
		return index;
	}

	public void setIndex(String string) {
		index = string;
	}
	public String get_return() {
		return _return;
	}

	public void set_return(String string) {
		_return = string;
	}

}

