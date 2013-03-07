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


import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.info_action;
import it.classhidra.core.init.auth_init;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class tagForm extends TagSupport{
	private static final long serialVersionUID = 1L;
	protected String bean = null;
	protected String name = null;
	protected String objId = null;
	protected String action = null;
	protected String method = null;
	protected String accept = null;
	protected String accept_charset= null;
	protected String styleClass=null;
	protected String enctype=null;//"application/x-www-form-urlencoded"
	protected String lang = null;
	protected String style = null;
	protected String target = null;
	protected String title = null;
	protected String dir=null;//"ltr"
	protected String wac_fascia = null;	
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
	protected String onreset = null;
	protected String onsubmit = null;
	protected String onhistory = null;


	public int doStartTag() throws JspException {
		StringBuffer results = new StringBuffer();
		results.append(this.createTagBody());
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		return (EVAL_BODY_INCLUDE);
	
	}

	public int doEndTag() throws JspException {
			
		StringBuffer results = new StringBuffer("</form>");
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		return (EVAL_PAGE);
	
	}

	public void release() {
		super.release();
		bean=null;
		name=null;
		objId=null;
		action=null;
		method=null;
		accept=null;
		accept_charset=null;
		styleClass=null;
		enctype=null;//"application/x-www-form-urlencoded"
		lang=null;
		style=null;
		target=null;
		title=null;
		dir=null;//"ltr"
		wac_fascia=null;
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
		onreset=null;
		onsubmit=null;
		onhistory=null;
	}
   
	protected String createTagBody() {
		HttpServletResponse response = (HttpServletResponse) this.pageContext.getResponse();
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
		
		
		try{		
			auth_init aInit = (auth_init)request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION);
			String lang = aInit.get_language();
			if(lang.toUpperCase().equals("UA") || lang.toUpperCase().equals("RU")){
				if ("ISO-8859-1".equalsIgnoreCase(request.getCharacterEncoding()) || request.getCharacterEncoding()==null) {
					try{
						request.setCharacterEncoding("windows-1251");
						response.setContentType("text/html;charset=windows-1251");
//						response.setCharacterEncoding("windows-1251");						
					}catch(Exception e){
					}
				}
			}
		}catch(Exception e){
		}
		info_action	formInfoAction = formAction.get_infoaction();
				
		StringBuffer results = new StringBuffer("");
		if(onhistory!=null)	results.append("<script>"+onhistory+"</script>"); 
		else results.append("<script>history.go(1);</script>"+System.getProperty("line.separator"));
		
		results.append("<form ");
		if(name==null){
			if(formInfoAction!=null) name=formInfoAction.getName();
		}
		if(name!=null){
			results.append(" name=\"");
			results.append(name);
			results.append("\"");
		}

		if(objId==null){
			if(formInfoAction!=null) objId=formInfoAction.getName();
		}
		if(objId!=null){
			results.append(" id=\"");
			results.append(name);
			results.append("\"");
		}
		
		results.append(" action=\"");
		if(formInfoAction!=null)
			results.append(formInfoAction.getPath()+bsController.getAppInit().get_extention_do());			
		else results.append("/Controller");			
		results.append("\"");
		
		results.append(" method=\"");
		results.append(method == null ? "post" : method);
		results.append("\"");

		if (styleClass != null) {
			results.append(" class=\"");
			results.append(styleClass);
			results.append("\"");
		}
		if (style != null) {
			results.append(" style=\"");
			results.append(style);
			results.append("\"");
		}
		if (enctype != null) {
			results.append(" enctype=\"");
			results.append(enctype);
			results.append("\"");
		}
		if (accept != null) {
			results.append(" accept=\"");
			results.append(accept);
			results.append("\"");
		}
		if (accept_charset != null) {
			results.append(" accept-charset=\"");
			results.append(accept_charset);
			results.append("\"");
		}
		if (lang != null) {
			results.append(" lang=\"");
			results.append(lang);
			results.append("\"");
		}
		if (target != null) {
			results.append(" target=\"");
			results.append(target);
			results.append("\"");
		}
		if (title != null) {
			results.append(" title=\"");
			results.append(title);
			results.append("\"");
		}
		if (dir != null) {
			results.append(" dir=\"");
			results.append(dir);
			results.append("\"");
		}
		if (onclick != null) {
			results.append(" onclick=\"");
			results.append(onclick);
			results.append("\"");
		}
		if (ondblclick != null) {
			results.append(" ondblclick=\"");
			results.append(ondblclick);
			results.append("\"");
		}
		if (onhelp != null) {
			results.append(" onhelp=\"");
			results.append(onhelp);
			results.append("\"");
		}
		
		if (onkeydown != null) {
			results.append(" onkeydown=\"");
			results.append(onkeydown);
			results.append("\"");
		}
		if (onkeypress != null) {
			results.append(" onkeypress=\"");
			results.append(onkeypress);
			results.append("\"");
		}
		if (onkeyup != null) {
			results.append(" onkeyup=\"");
			results.append(onkeyup);
			results.append("\"");
		}
		if (onmousedown != null) {
			results.append(" onmousedown=\"");
			results.append(onmousedown);
			results.append("\"");
		}

		if (onmousemove != null) {
			results.append(" onmousemove=\"");
			results.append(onmousemove);
			results.append("\"");
		}
		if (onmouseout != null) {
			results.append(" onmouseout=\"");
			results.append(onmouseout);
			results.append("\"");
		}
		if (onmouseover != null) {
			results.append(" onmouseover=\"");
			results.append(onmouseover);
			results.append("\"");
		}
		if (onmouseup != null) {
			results.append(" onmouseup=\"");
			results.append(onmouseup);
			results.append("\"");
		}
		if (onreset != null) {
			results.append(" onreset=\"");
			results.append(onreset);
			results.append("\"");
		}
		if (onsubmit != null) {
			results.append(" onsubmit=\"");
//			results.append("if(this.action.indexOf('Controller')!=0){if(this.$action){this.$action.value = this.action;} this.action='Controller';} ");
			results.append(onsubmit);
			results.append("\"");
		}
		results.append(">"+System.getProperty("line.separator"));
		results.append("<script>"+System.getProperty("line.separator"));
		results.append("function trim(txt){"+System.getProperty("line.separator"));
		results.append("return txt.replace(/(^\\s+)|(\\s+$)/g,\"\");"+System.getProperty("line.separator"));
		results.append("}"+System.getProperty("line.separator"));
		results.append("</script>"+System.getProperty("line.separator"));

			results.append("<input type=\"hidden\" id=\""+bsController.CONST_ID_$ACTION+"\" name=\""+bsController.CONST_ID_$ACTION+"\" value= \"");			
			results.append(((action==null)?(formInfoAction==null)?"":formInfoAction.getPath():action));
			results.append("\">"+System.getProperty("line.separator"));


			results.append("<input type=\"hidden\" id=\""+bsController.CONST_ID_$MIDDLE_ACTION+"\" name=\""+bsController.CONST_ID_$MIDDLE_ACTION+"\" value= \"\">"+System.getProperty("line.separator"));
			results.append("<input type=\"hidden\" id=\""+bsController.CONST_ID_$ACTION_FROM+"\" name=\""+bsController.CONST_ID_$ACTION_FROM+"\" value= \"\">"+System.getProperty("line.separator"));
			results.append("<input type=\"hidden\" id=\""+bsController.CONST_ID_$NAVIGATION+"\" name=\""+bsController.CONST_ID_$NAVIGATION+"\" value= \"");
			try{
				results.append(formAction.get_infoaction().getPath()+":"+formAction.getCurrent_redirect().get_inforedirect().getPath());
			}catch(Exception e){	
			}
			results.append("\">"+System.getProperty("line.separator"));

//		results.append("<input type=\"hidden\" name=\"parent_pointOfLaunch\" value= \""+((formBean.get("parent_pointOfLaunch")==null)?"":formBean.get("parent_pointOfLaunch"))+"\">"+System.getProperty("line.separator"));
//		results.append("<input type=\"hidden\" name=\"child_pointOfReturn\" value= \""+((formBean.get("child_pointOfReturn")==null)?"":formBean.get("child_pointOfReturn"))+"\">"+System.getProperty("line.separator"));
		return results.toString();
	}
	public String getAccept() {
		return accept;
	}
	public String getAccept_charset() {
		return accept_charset;
	}
	public String getAction() {
		return action;
	}
	public String getDir() {
		return dir;
	}
	public String getEnctype() {
		return enctype;
	}
	public String getLang() {
		return lang;
	}
	public String getMethod() {
		return method;
	}
	public String getName() {
		return name;
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
	public String getOnreset() {
		return onreset;
	}
	public String getOnsubmit() {
		return onsubmit;
	}
	public String getStyle() {
		return style;
	}
	public String getStyleClass() {
		return styleClass;
	}
	public String getTarget() {
		return target;
	}
	public String getTitle() {
		return title;
	}
	public void setAccept(String string) {
		accept = string;
	}
	public void setAccept_charset(String string) {
		accept_charset = string;
	}
	public void setAction(String string) {
		action = string;
	}
	public void setDir(String string) {
		dir = string;
	}
	public void setEnctype(String string) {
		enctype = string;
	}
	public void setLang(String string) {
		lang = string;
	}
	public void setMethod(String string) {
		method = string;
	}
	public void setName(String string) {
		name = string;
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
	public void setOnreset(String string) {
		onreset = string;
	}
	public void setOnsubmit(String string) {
		onsubmit = string;
	}
	public void setStyle(String string) {
		style = string;
	}
	public void setStyleClass(String string) {
		styleClass = string;
	}
	public void setTarget(String string) {
		target = string;
	}
	public void setTitle(String string) {
		title = string;
	}
	public String getObjId() {
		return objId;
	}
	public void setObjId(String string) {
		objId = string;
	}
	public String getOnhistory() {
		return onhistory;
	}
	public void setOnhistory(String string) {
		onhistory = string;
	}
	public String getWac_fascia() {
		return wac_fascia;
	}
	public void setWac_fascia(String string) {
		wac_fascia = string;
	}

	public String getBean() {
		return bean;
	}

	public void setBean(String bean) {
		this.bean = bean;
	}

}

