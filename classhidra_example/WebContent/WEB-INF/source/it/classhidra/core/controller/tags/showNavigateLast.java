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
import it.classhidra.core.tool.util.util_format;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.io.IOException;

public class showNavigateLast extends TagSupport implements DynamicAttributes {
	private static final long serialVersionUID = 7713310883516145787L;
	protected String style = null;
	protected String styleClass = null;
	protected String img_path = null;
	protected HashMap<String,String> parameters=null;
	
	protected Map<String,Object> tagAttributes = new HashMap<String, Object>();


	public int doStartTag() throws JspException {
		parameters=new HashMap<String, String>();
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		final StringBuffer results = new StringBuffer();
		if(img_path==null) img_path="../images";
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
		parameters=null;
		img_path=null;
		
		tagAttributes = new HashMap<String, Object>();
	}

	protected String createTagBody() {
		HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
		final StringBuffer results = new StringBuffer("");

		try{
			info_navigation	formInfoNavigation		= bsController.getFromInfoNavigation(null, request);
			if(formInfoNavigation==null)
				formInfoNavigation = new info_navigation();

			info_navigation iN = formInfoNavigation;
			Vector<String> sub_results = new Vector<String>();
			Vector<String> sub_results_action = new Vector<String>();
			Vector<String> sub_results_name = new Vector<String>();


			while(iN!=null){
				if(iN.getIRedirect()!=null){
					try{
						String subLabel="";
						String id = iN.getIAction().getPath();
						if((";"+bsController.getAppInit().get_nav_excluded()).indexOf(";"+id+";")==-1){
							if(iN.getIRedirect().getMess_id()!=null && !iN.getIRedirect().getMess_id().equals("")){
								subLabel = "<a style=\"cursor:pointer;\" )\"><img src='"+img_path+"/menu/special/action.gif' border='0' desc='"+bsController.writeLabel(request,iN.getIRedirect().getMess_id(),iN.getIRedirect().getDescr(),parameters)+iN.getDesc_second()+"' onclick=\"goAction('"+iN.getIAction().getPath()+"','"+iN.getIAction().getWac()+"' onmouseover='try{nlist_over(this)}catch(e){};'></a>";
								sub_results.add(subLabel);
								sub_results_action.add("goAction('"+iN.getIAction().getPath()+"','"+iN.getIAction().getWac()+"')");
								sub_results_name.add(bsController.writeLabel(request,iN.getIRedirect().getMess_id(),iN.getIRedirect().getDescr(),parameters)+iN.getDesc_second());
							}
						}
					}catch(Exception ex){
					}
				}
				iN = iN.getChild();
			}

			int ind = -1;


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
					    for(Object attrName : tagAttributes.keySet() ) {
					    	results.append(" ");
					    	results.append(attrName);
					    	results.append("=\"");
					    	results.append(tagAttributes.get(attrName));
					    	results.append("\"");
					      }

						results.append('>');
						if(i==0 || i==sub_results.size()-1){
							if(i==0){
								if(sub_results.size()>2){
									try{
										String sub_res_return = (String)sub_results_action.get(sub_results_name.size()-2);
										sub_res_return = util_format.replace(sub_res_return,"goAction(","goReturn(");
										results.append("<a class=\"page_section\" style=\"cursor:pointer;\" ><img src='"+img_path+"/menu/special/action_prev.gif' border='0' desc='"+sub_results_name.get(sub_results_name.size()-2)+"' onclick=\""+sub_res_return+"\" onmouseover='nlist_over(this);'></a>");
									}catch(Exception ex){
									}
								}
								results.append("<a style=\"cursor:pointer;\" ><img src='"+img_path+"/menu/special/action_content.gif' border='0' desc='"+sub_results_name.get(i)+"' onclick=\""+sub_results_action.get(i)+"\" onmouseover='try{nlist_over(this)}catch(e){};'></a>");
							}else{
								results.append("<img src='"+img_path+"/menu/special/action_blank.gif' border='0'>");
								results.append("<nobr><a style=\"cursor:pointer;\" onclick=\""+sub_results_action.get(i)+"\">"+sub_results_name.get(i)+"</a></nobr>");
							}
						}else{
							results.append("<a style=\"cursor:pointer;\" ><img src='"+img_path+"/menu/special/action.gif' border='0' desc='"+sub_results_name.get(i)+"' onclick=\""+sub_results_action.get(i)+"\" onmouseover='try{nlist_over(this)}catch(e){};'></a>");
						}
						results.append("</span>");
					}
				}
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

	public HashMap<String,String> getParameters() {
		return parameters;
	}

	public String getImg_path() {
		return img_path;
	}

	public void setImg_path(String imgPath) {
		img_path = imgPath;
	}
	
	public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
		tagAttributes.put(localName, value);
	}

}

